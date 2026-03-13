package com.example.myapplication.screen.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.entity.Document
import com.example.domain.entity.SearchingViewType
import com.example.domain.entity.fakeDocument
import com.example.myapplication.R
import com.example.myapplication.ui.MediaTypeBadge
import com.example.myapplication.ui.badgeLabel
import com.example.myapplication.ui.badgePalette
import com.example.myapplication.ui.primaryLabel
import com.example.myapplication.ui.secondaryLabel
import com.example.myapplication.ui.thumbnailModel
import com.example.myapplication.util.getISOTimeToString

@Composable
fun SavedDocumentItem(
    document: Document,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onCopyLinkClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(28.dp)
    val viewType = document.searchingViewType
    val fallbackTitle = stringResource(R.string.saved_document_fallback_title)
    val fallbackSubtitle = stringResource(R.string.saved_document_fallback_subtitle)
    var isMenuExpanded by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp,
            ),
    ) {
        Column {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.92f)
                        .clip(shape),
            ) {
                ThumbnailSection(document = document)

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                ) {
                    MediaTypeBadge(viewType = viewType)

                    Box {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                        ) {
                            IconButton(
                                onClick = {
                                    isMenuExpanded = true
                                },
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.vic_more_vertical),
                                    contentDescription = stringResource(R.string.saved_document_more_actions),
                                    tint = Color.White,
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = {
                                isMenuExpanded = false
                            },
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.saved_document_remove_action)) },
                                onClick = {
                                    isMenuExpanded = false
                                    onRemoveClick.invoke()
                                },
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.saved_document_share_action)) },
                                onClick = {
                                    isMenuExpanded = false
                                    onShareClick.invoke()
                                },
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.saved_document_copy_link_action)) },
                                onClick = {
                                    isMenuExpanded = false
                                    onCopyLinkClick.invoke()
                                },
                            )
                        }
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
            ) {
                Text(
                    text = document.primaryLabel(fallbackTitle),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = viewType.badgePalette().containerColor.copy(alpha = 0.18f),
                ) {
                    Text(
                        text = document.secondaryLabel(fallbackSubtitle),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.52f),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = getISOTimeToString(document.datetime, stringResource(R.string.create_time_string)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ThumbnailSection(document: Document) {
    val thumbnailModel = document.thumbnailModel()

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.linearGradient(
                            colors =
                                listOf(
                                    Color(0xFF1A2D3C),
                                    Color(0xFF365C73),
                                    Color(0xFFCC7A5A),
                                ),
                        ),
                ),
    ) {
        if (thumbnailModel != null) {
            AsyncImage(
                model = thumbnailModel,
                contentDescription = stringResource(R.string.saved_document_thumbnail),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = document.searchingViewType.badgeLabel(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.92f),
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        Color(0x12000000),
                                        Color(0xAA09111A),
                                    ),
                            ),
                    ),
        )
    }
}

@Preview
@Composable
fun SavedDocumentImageItemPreview() {
    SavedDocumentItem(
        document =
            fakeDocument().copy(
                collection = "Beautiful interior inspiration",
                displaySiteName = "example.com",
                datetime = "2025-07-23T14:11:00.000+09:00",
                searchingViewType = SearchingViewType.Image,
            ),
    )
}

@Preview
@Composable
fun SavedDocumentVideoItemPreview() {
    SavedDocumentItem(
        document =
            fakeDocument().copy(
                title = "Compose MVI Architecture Walkthrough",
                author = "Open Studio",
                datetime = "2025-07-23T14:11:00.000+09:00",
                searchingViewType = SearchingViewType.Video,
            ),
    )
}
