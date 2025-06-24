package com.example.myapplication.screen.searching

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.model.SearchingIntent
import com.example.myapplication.viewmodels.SearchingViewModel

@Composable
fun SearchingScreen() {
    val viewModel = hiltViewModel<SearchingViewModel>()
    Column {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        ) {
            items(viewModel.searchingUiState.value.size) { index ->
                SearchingItem(viewModel.searchingUiState.value[index])
            }
        }
        UserSearchingTextField()
    }
}

@Composable
fun UserSearchingTextField() {
    val viewModel = hiltViewModel<SearchingViewModel>()

    Row(
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        TextField(
            value = viewModel.userSearchingData.value,
            onValueChange = {
                viewModel.userSearchingData.value = it
            },
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier.padding(4.dp)
        )
        Button(
            modifier = Modifier.wrapContentSize(),
            onClick = {
                viewModel.setIntent(SearchingIntent.Searching)
            }
        ) {
            Text(
                text = stringResource(R.string.searching_data)
            )
        }
    }
}