package com.example.myapplication.screen.saved

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.contract.SavedDocumentContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SavedDocumentScreen(
    uiState: SavedDocumentContract.UiState,
    effectFlow: Flow<SavedDocumentContract.Effect>?,
    setEvent: (SavedDocumentContract.Event) -> Unit,
    onNavigateToDetail: (String) -> Unit,
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        setEvent(SavedDocumentContract.Event.OnLoadSavedDocuments)
    }

    LaunchedEffect(effectFlow) {
        effectFlow?.collectLatest { effect ->
            when (effect) {
                is SavedDocumentContract.Effect.NavigateToDetail -> {
                    onNavigateToDetail(Uri.encode(effect.uri))
                }
                is SavedDocumentContract.Effect.ShareLink -> {
                    context.startActivity(
                        Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, effect.link)
                            },
                            context.getString(R.string.saved_document_share_action),
                        ),
                    )
                }
                is SavedDocumentContract.Effect.CopyLink -> {
                    clipboardManager.setText(AnnotatedString(effect.link))
                    Toast.makeText(
                        context,
                        context.getString(R.string.saved_document_link_copied_message),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                is SavedDocumentContract.Effect.ShowMessage -> {
                    Toast.makeText(context, context.getString(effect.messageResId), Toast.LENGTH_SHORT).show()
                }
            }
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
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                ),
                        ),
                ),
    ) {
        if (uiState.savedDocuments.isEmpty()) {
            SavedDocumentEmptyState(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp),
            )
            return@Box
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SavedDocumentHeader()
            }

            itemsIndexed(uiState.savedDocuments) { index, item ->
                SavedDocumentItem(
                    document = item,
                    onClick = {
                        setEvent(SavedDocumentContract.Event.OnClickSavedDocument(index))
                    },
                    onRemoveClick = {
                        setEvent(SavedDocumentContract.Event.OnRemoveSavedDocument(index))
                    },
                    onShareClick = {
                        setEvent(SavedDocumentContract.Event.OnShareSavedDocument(index))
                    },
                    onCopyLinkClick = {
                        setEvent(SavedDocumentContract.Event.OnCopySavedDocumentLink(index))
                    },
                )
            }
        }
    }
}

@Composable
private fun SavedDocumentHeader() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
    ) {
        Text(
            text = stringResource(R.string.saved_documents_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = stringResource(R.string.saved_documents_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SavedDocumentEmptyState(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
        ) {
            Text(
                text = stringResource(R.string.saved_documents_empty_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.saved_documents_empty_message),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
