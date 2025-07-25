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
import com.example.domain.entity.DocumentEntity
import com.example.domain.entity.SearchingViewType
import com.example.myapplication.R
import com.example.myapplication.util.getISOTimeToString

@Composable
fun SearchingItem(
    documentEntity: DocumentEntity,
    onClickSearchingItem: (DocumentEntity) -> Unit = {},
    onClickSaveButton: () -> Unit = {}
) {
    val viewType = documentEntity.searchingViewType

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp),
        onClick = {
            onClickSearchingItem.invoke(documentEntity)
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
                        SearchingViewType.Image -> documentEntity.thumbnailUrl
                        SearchingViewType.Video -> documentEntity.thumbnail
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
                documentEntity = documentEntity,
                modifier = Modifier.weight(1f)
            )

            if (!documentEntity.isSaveButtonVisible) {
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
    documentEntity: DocumentEntity,
    modifier: Modifier
) {
    val viewType = documentEntity.searchingViewType
    Column(
        modifier = modifier
    ) {
        Row {
            Text(text = stringResource(R.string.view_type))
            Image(
                painter = when (documentEntity.searchingViewType) {
                    SearchingViewType.Video -> painterResource(id = R.drawable.vic_searching_result_video)
                    else -> painterResource(id = R.drawable.vic_searching_result_image)
                },
                contentDescription = "Searching View Type"
            )
        }

        Text(
            text = when (viewType) {
                SearchingViewType.Image -> documentEntity.collection ?: ""
                SearchingViewType.Video -> documentEntity.title ?: ""
                else -> ""
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${stringResource(R.string.created_time)} ${getISOTimeToString(documentEntity.datetime, stringResource(R.string.create_time_string))}"
        )
    }
}