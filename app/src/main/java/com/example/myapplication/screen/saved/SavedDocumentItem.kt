package com.example.myapplication.screen.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.entity.Document
import com.example.domain.entity.SearchingViewType
import com.example.domain.entity.fakeDocument
import com.example.myapplication.R
import com.example.myapplication.util.getISOTimeToString

@Composable
fun SavedDocumentItem(
    document: Document
) {
    Card(
        onClick = {

        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val viewType = document.searchingViewType
        Column {
            AsyncImage(
                model = when (viewType) {
                    SearchingViewType.Image -> document.thumbnailUrl
                    SearchingViewType.Video -> document.thumbnail
                    else -> null
                },
                contentDescription = "saved document loaded Result",
                modifier = Modifier
                    .fillMaxSize()
                    .sizeIn(minHeight = 120.dp),
                contentScale = ContentScale.FillBounds
            )

            Spacer(
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = when (viewType) {
                    SearchingViewType.Image -> document.collection ?: ""
                    SearchingViewType.Video -> document.title ?: ""
                    else -> ""
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "${stringResource(R.string.created_time)} ${
                    getISOTimeToString(document.datetime, stringResource(
                        R.string.create_time_string)
                    )
                }"
            )
        }
    }
}

@Preview
@Composable
fun SavedDocumentITemPreview() {
    SavedDocumentItem(fakeDocument())
}
