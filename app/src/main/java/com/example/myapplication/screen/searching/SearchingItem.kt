package com.example.myapplication.screen.searching

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onClickSaveButton: () -> Unit = {}
) {
    val viewType = document.searchingViewType

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp),
        onClick = {
            onClickSearchingItem.invoke(document)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.size(76.dp)
            ) {
                AsyncImage(
                    model = when (viewType) {
                        SearchingViewType.Image -> document.thumbnailUrl
                        SearchingViewType.Video -> document.thumbnail
                        else -> null
                    },
                    contentDescription = "Searching loaded Result",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }

            Spacer(
                modifier = Modifier.padding(4.dp)
            )

            SearchingContent(
                document = document,
                modifier = Modifier.weight(1f)
            )

            if (!document.isSaveButtonVisible) {
                IconButton(
                    onClick = {
                        onClickSaveButton.invoke()
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.vic_saved_icon),
                        contentDescription = "저장하기"
                    )
                }
            }
        }
    }
}

@Composable
fun SearchingContent(
    document: Document,
    modifier: Modifier
) {
    val viewType = document.searchingViewType
    Column(
        modifier = modifier
    ) {
        Row {
            Text(text = stringResource(R.string.view_type))
            Image(
                painter = when (document.searchingViewType) {
                    SearchingViewType.Video -> painterResource(id = R.drawable.vic_searching_result_video)
                    else -> painterResource(id = R.drawable.vic_searching_result_image)
                },
                contentDescription = "Searching View Type"
            )
        }

        Text(
            text = when (viewType) {
                SearchingViewType.Image -> document.collection ?: ""
                SearchingViewType.Video -> document.title ?: ""
                else -> ""
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${stringResource(R.string.created_time)} ${getISOTimeToString(document.datetime, stringResource(R.string.create_time_string))}"
        )
    }
}