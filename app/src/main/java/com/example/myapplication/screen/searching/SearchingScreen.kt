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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.domain.entity.SearchingViewType
import com.example.myapplication.R
import com.example.myapplication.common.SearchingRoute
import com.example.myapplication.model.SearchingIntent
import com.example.myapplication.util.isFinishedScroll
import com.example.myapplication.viewmodels.SearchingViewModel

@Composable
fun SearchingScreen(
    navController: NavController,
    viewModel: SearchingViewModel = hiltViewModel(),
) {
    val columnListScrollState = rememberLazyListState()

    LaunchedEffect(columnListScrollState) {
        snapshotFlow {
            columnListScrollState.isFinishedScroll()
        }.collect { isFinish ->
            if (isFinish) {
                viewModel.setIntent(SearchingIntent.SearchingMoreData)
            }
        }
    }

    Box {
        LoadingProgress(viewModel.isLoadingState.value)
        Column {
            LazyColumn(
                state = columnListScrollState,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
            ) {
                items(viewModel.searchingUiState.value.size) { index ->
                    SearchingItem(
                        documentEntity = viewModel.searchingUiState.value[index],
                        onClick = { documentEntity ->
                            val uri =
                                if (documentEntity.searchingViewType == SearchingViewType.Image) {
                                    documentEntity.docUrl
                                } else {
                                    documentEntity.url
                                }.let { selectedUrlString ->
                                    Uri.encode(selectedUrlString)
                                }
                            navController.navigate("${SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName}/$uri")
                        },
                    )
                }
            }
            UserSearchingTextField()
        }
    }
}

@Composable
fun UserSearchingTextField(viewModel: SearchingViewModel = hiltViewModel()) {
    Row(
        modifier =
            Modifier
                .padding(4.dp),
    ) {
        TextField(
            value = viewModel.userSearchingData.value,
            onValueChange = {
                viewModel.userSearchingData.value = it
            },
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
            onClick = {
                viewModel.setIntent(SearchingIntent.Searching)
            },
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
