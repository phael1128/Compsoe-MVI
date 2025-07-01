package com.example.myapplication.screen.searching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
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
import com.example.myapplication.R
import com.example.myapplication.common.SearchingRoute
import com.example.myapplication.model.SearchingIntent
import com.example.myapplication.util.isFinishedScroll
import com.example.myapplication.viewmodels.SearchingViewModel

@Composable
fun SearchingScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<SearchingViewModel>()
    val columnListScrollState = rememberLazyListState()

    LaunchedEffect(columnListScrollState) {
        snapshotFlow {
            columnListScrollState.isFinishedScroll()
        }.collect { isFinish ->
            if (isFinish) {
                viewModel.setIntent(SearchingIntent.Searching)
            }
        }
    }

    Column {
        LazyColumn(
            state = columnListScrollState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        ) {
            items(viewModel.searchingUiState.value.size) { index ->
                SearchingItem(
                    documentEntity = viewModel.searchingUiState.value[index],
                    onClick = { documentEntity ->
                        navController.navigate(SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName)
                    }
                )
            }
        }
        UserSearchingTextField()
    }
}

@Composable
fun UserSearchingTextField() {
    val viewModel = hiltViewModel<SearchingViewModel>()

    Row(
        modifier = Modifier
            .padding(4.dp)
    ) {
        TextField(
            value = viewModel.userSearchingData.value,
            onValueChange = {
                viewModel.userSearchingData.value = it
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White
                ),
            textStyle = TextStyle(fontSize = 16.sp)
        )
        Spacer(
            modifier = Modifier.padding(4.dp)
        )
        Button(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterVertically),
            onClick = {
                viewModel.setIntent(SearchingIntent.Searching)
            }
        ) {
            Text(
                text = stringResource(R.string.searching_data),
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}