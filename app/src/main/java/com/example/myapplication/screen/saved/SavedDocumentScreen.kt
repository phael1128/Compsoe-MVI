package com.example.myapplication.screen.saved

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.contract.SavedDocumentContract

@Composable
fun SavedDocumentScreen(
    uiState: SavedDocumentContract.UiState,
    setEvent: (SavedDocumentContract.Event) -> Unit,
) {
    LaunchedEffect(Unit) {
        setEvent(SavedDocumentContract.Event.OnLoadSavedDocuments)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp),
        content = {
            items(uiState.savedDocuments) { item ->
                SavedDocumentItem(item)
            }
        },
    )
}
