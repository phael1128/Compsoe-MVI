package com.example.myapplication.screen.searching

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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

    Box {
        LoadingProgress(uiState.isLoading)
        Column {
            LazyColumn(
                state = columnListScrollState,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
            ) {
                items(uiState.documents.size) { index ->
                    SearchingItem(
                        document = uiState.documents[index],
                        onClickSearchingItem = { document ->
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
            UserSearchingTextField(
                searchQuery = uiState.searchQuery,
                onQueryChanged = { setEvent(SearchingContract.Event.OnSearchQueryChanged(it)) },
                onSearchClick = { setEvent(SearchingContract.Event.OnSearchClick) },
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
    Row(
        modifier =
            Modifier
                .padding(4.dp),
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onQueryChanged,
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = Color.White,
                    ),
            textStyle = TextStyle(fontSize = 16.sp),
        )
        Spacer(
            modifier = Modifier.padding(4.dp),
        )
        Button(
            modifier =
                Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterVertically),
            onClick = onSearchClick,
        ) {
            Text(
                text = stringResource(R.string.searching_data),
                style = TextStyle(fontSize = 16.sp),
            )
        }
    }
}

@Composable
fun LoadingProgress(isLoading: Boolean) {
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator(
                modifier =
                    Modifier.padding(8.dp),
                color = Color.Black,
                strokeWidth = 4.dp,
            )
        }
    }
}
