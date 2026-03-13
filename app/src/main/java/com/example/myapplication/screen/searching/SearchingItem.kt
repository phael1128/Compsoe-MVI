package com.example.myapplication.screen.searching

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.entity.Document
import com.example.domain.entity.SearchingViewType
import com.example.myapplication.R
import com.example.myapplication.util.getISOTimeToString

@Composable
fun SearchingItem(
    document: Document,
    onClickSearchingItem: (Document) -> Unit = {},
    onClickSaveButton: () -> Unit = {},
) {
    val viewType = document.searchingViewType
    val fallbackTitle = stringResource(R.string.searching_document_fallback_title)
    val fallbackSubtitle = stringResource(R.string.searching_document_fallback_subtitle)

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(30.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 10.dp,
                pressedElevation = 14.dp,
            ),
        onClick = {
            onClickSearchingItem.invoke(document)
        },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
        ) {
            SearchThumbnail(
                document = document,
                modifier =
                    Modifier
                        .size(width = 122.dp, height = 132.dp)
                        .clip(RoundedCornerShape(24.dp)),
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    MediaTypeBadge(viewType = viewType)
                    SaveActionButton(
                        isSaved = document.isSaved,
                        onClick = onClickSaveButton,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = document.primaryLabel(fallbackTitle),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = viewType.badgePalette().containerColor.copy(alpha = 0.16f),
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = getISOTimeToString(document.datetime, stringResource(R.string.create_time_string)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.searching_detail_hint),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun SearchThumbnail(
    document: Document,
    modifier: Modifier = Modifier,
) {
    val thumbnailModel = document.thumbnailModel()

    Box(
        modifier =
            modifier.background(
                brush =
                    Brush.linearGradient(
                        colors =
                            listOf(
                                Color(0xFF193043),
                                Color(0xFF2C6E73),
                                Color(0xFFE8A15B),
                            ),
                    ),
            ),
    ) {
        if (thumbnailModel != null) {
            AsyncImage(
                model = thumbnailModel,
                contentDescription = stringResource(R.string.searching_document_thumbnail),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
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
                                        Color(0x22000000),
                                        Color(0xB0091119),
                                    ),
                            ),
                    ),
            contentAlignment = Alignment.BottomStart,
        ) {
            Text(
                text = stringResource(R.string.searching_thumbnail_badge),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.92f),
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}

@Composable
private fun MediaTypeBadge(viewType: SearchingViewType?) {
    val palette = viewType.badgePalette()

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = palette.containerColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
        ) {
            Icon(
                painter =
                    when (viewType) {
                        SearchingViewType.Video -> painterResource(R.drawable.vic_searching_result_video)
                        SearchingViewType.Image -> painterResource(R.drawable.vic_searching_result_image)
                        null -> painterResource(R.drawable.vic_searching_result_image)
                    },
                contentDescription = null,
                tint = palette.contentColor,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = viewType.badgeLabel(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = palette.contentColor,
            )
        }
    }
}

@Composable
private fun SaveActionButton(
    isSaved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue =
            if (isSaved) {
                Color(0xFFFFF1CC)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            },
        label = "saveContainerColor",
    )
    val iconColor by animateColorAsState(
        targetValue =
            if (isSaved) {
                Color(0xFFE2A400)
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        label = "saveIconColor",
    )

    Surface(
        shape = CircleShape,
        color = containerColor,
        modifier = modifier,
    ) {
        IconButton(
            onClick = {
                if (!isSaved) {
                    onClick.invoke()
                }
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.vic_saved_icon),
                contentDescription =
                    if (isSaved) {
                        stringResource(R.string.saved_icon_saved)
                    } else {
                        stringResource(R.string.saved_icon_unsaved)
                    },
                tint = iconColor,
            )
        }
    }
}

private data class BadgePalette(
    val containerColor: Color,
    val contentColor: Color,
)

private fun SearchingViewType?.badgePalette(): BadgePalette =
    when (this) {
        SearchingViewType.Video ->
            BadgePalette(
                containerColor = Color(0xFFFFE2D2),
                contentColor = Color(0xFF9E4524),
            )
        SearchingViewType.Image ->
            BadgePalette(
                containerColor = Color(0xFFDDF3F2),
                contentColor = Color(0xFF145C63),
            )
        null ->
            BadgePalette(
                containerColor = Color(0xFFE9EEF2),
                contentColor = Color(0xFF41515E),
            )
    }

private fun SearchingViewType?.badgeLabel(): String =
    when (this) {
        SearchingViewType.Image -> "IMAGE"
        SearchingViewType.Video -> "VIDEO"
        null -> "MEDIA"
    }

private fun Document.thumbnailModel(): String? =
    when (searchingViewType) {
        SearchingViewType.Image -> thumbnailUrl
        SearchingViewType.Video -> thumbnail
        null -> thumbnailUrl ?: thumbnail
    }?.takeIf { it.isNotBlank() }

private fun Document.primaryLabel(fallbackTitle: String): String =
    when (searchingViewType) {
        SearchingViewType.Image -> collection
        SearchingViewType.Video -> title
        null -> title ?: collection
    }?.takeIf { it.isNotBlank() } ?: fallbackTitle

private fun Document.secondaryLabel(fallbackSubtitle: String): String =
    when (searchingViewType) {
        SearchingViewType.Image -> displaySiteName ?: docUrl
        SearchingViewType.Video -> author ?: url
        null -> displaySiteName ?: author ?: docUrl ?: url
    }?.takeIf { it.isNotBlank() } ?: fallbackSubtitle
