package com.example.myapplication.screen.searching

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.entity.SearchingViewType
import com.example.myapplication.R
import com.example.myapplication.contract.SearchingContract
import com.example.myapplication.util.isFinishedScroll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchingScreen(
    uiState: SearchingContract.UiState,
    effectFlow: Flow<SearchingContract.Effect>?,
    setEvent: (SearchingContract.Event) -> Unit,
    onNavigateToDetail: (String) -> Unit,
) {
    val columnListScrollState = rememberLazyListState()

    LaunchedEffect(columnListScrollState) {
        snapshotFlow {
            columnListScrollState.isFinishedScroll()
        }.collect { isFinish ->
            if (isFinish) {
                setEvent(SearchingContract.Event.OnLoadMoreData)
            }
        }
    }

    LaunchedEffect(effectFlow) {
        effectFlow?.collectLatest { effect ->
            when (effect) {
                is SearchingContract.Effect.NavigateToDetail -> {
                    onNavigateToDetail(effect.uri)
                }
                is SearchingContract.Effect.ShowError -> {
                    // TODO: Show error toast/snackbar
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
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f),
                                ),
                        ),
                ),
    ) {
        LoadingProgress(uiState.isLoading)

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = columnListScrollState,
                contentPadding = PaddingValues(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f),
            ) {
                item {
                    SearchingResultHeader(
                        searchQuery = uiState.searchQuery,
                        documentCount = uiState.documents.size,
                    )
                }

                if (uiState.documents.isEmpty() && !uiState.isLoading) {
                    item {
                        SearchingEmptyState(searchQuery = uiState.searchQuery)
                    }
                } else {
                    itemsIndexed(uiState.documents) { index, document ->
                        SearchingItem(
                            document = document,
                            onClickSearchingItem = {
                                val uri =
                                    if (document.searchingViewType == SearchingViewType.Image) {
                                        document.docUrl
                                    } else {
                                        document.url
                                    }.let { selectedUrlString ->
                                        Uri.encode(selectedUrlString)
                                    }
                                onNavigateToDetail(uri)
                            },
                            onClickSaveButton = {
                                setEvent(SearchingContract.Event.OnSaveDocument(index))
                            },
                        )
                    }
                }
            }

            UserSearchingTextField(
                searchQuery = uiState.searchQuery,
                onQueryChanged = { setEvent(SearchingContract.Event.OnSearchQueryChanged(it)) },
                onSearchClick = { setEvent(SearchingContract.Event.OnSearchClick) },
            )
        }
    }
}

@Composable
private fun SearchingResultHeader(
    searchQuery: String,
    documentCount: Int,
) {
    val title =
        if (searchQuery.isBlank()) {
            stringResource(R.string.searching_results_title)
        } else {
            "\"$searchQuery\""
        }
    val subtitle =
        if (searchQuery.isBlank()) {
            stringResource(R.string.searching_results_subtitle)
        } else {
            stringResource(R.string.searching_results_count, documentCount)
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchingEmptyState(searchQuery: String) {
    val isIdle = searchQuery.isBlank()

    Card(
        shape = RoundedCornerShape(30.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth(),
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
                text =
                    if (isIdle) {
                        stringResource(R.string.searching_empty_idle_title)
                    } else {
                        stringResource(R.string.searching_empty_result_title)
                    },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text =
                    if (isIdle) {
                        stringResource(R.string.searching_empty_idle_message)
                    } else {
                        stringResource(R.string.searching_empty_result_message)
                    },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun UserSearchingTextField(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
) {
    Surface(
        tonalElevation = 12.dp,
        shadowElevation = 18.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onQueryChanged,
                placeholder = {
                    Text(text = stringResource(R.string.searching_input_hint))
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontSize = 16.sp),
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.52f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.52f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
            )

            Button(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(20.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                onClick = onSearchClick,
            ) {
                Text(
                    text = stringResource(R.string.searching_data),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}

@Composable
fun LoadingProgress(isLoading: Boolean) {
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.08f)),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp,
            )
        }
    }
}
