package com.example.myapplication.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.entity.Document
import com.example.domain.entity.SearchingViewType
import com.example.myapplication.R

@Composable
fun MediaTypeBadge(
    viewType: SearchingViewType?,
    modifier: Modifier = Modifier,
) {
    val palette = viewType.badgePalette()

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = palette.containerColor,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
        ) {
            Icon(
                painter =
                    when (viewType) {
                        SearchingViewType.Video -> painterResource(R.drawable.vic_searching_result_video)
                        SearchingViewType.Image -> painterResource(R.drawable.vic_searching_result_image)
                        null -> painterResource(R.drawable.vic_searching_result_image)
                    },
                contentDescription = null,
                tint = palette.contentColor,
            )
            Text(
                text = viewType.badgeLabel(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = palette.contentColor,
            )
        }
    }
}

@Composable
fun OverlayGlassBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Color.White.copy(alpha = 0.2f),
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
        )
    }
}

data class BadgePalette(
    val containerColor: Color,
    val contentColor: Color,
)

fun SearchingViewType?.badgePalette(): BadgePalette =
    when (this) {
        SearchingViewType.Video -> {
            BadgePalette(
                containerColor = Color(0xFFFFE2D2),
                contentColor = Color(0xFF9E4524),
            )
        }

        SearchingViewType.Image -> {
            BadgePalette(
                containerColor = Color(0xFFDDF3F2),
                contentColor = Color(0xFF145C63),
            )
        }

        null -> {
            BadgePalette(
                containerColor = Color(0xFFE9EEF2),
                contentColor = Color(0xFF41515E),
            )
        }
    }

fun SearchingViewType?.badgeLabel(): String =
    when (this) {
        SearchingViewType.Image -> "IMAGE"
        SearchingViewType.Video -> "VIDEO"
        null -> "MEDIA"
    }

fun Document.thumbnailModel(): String? =
    when (searchingViewType) {
        SearchingViewType.Image -> thumbnailUrl
        SearchingViewType.Video -> thumbnail
        null -> thumbnailUrl ?: thumbnail
    }?.takeIf { it.isNotBlank() }

fun Document.primaryLabel(fallbackTitle: String): String =
    when (searchingViewType) {
        SearchingViewType.Image -> collection
        SearchingViewType.Video -> title
        null -> title ?: collection
    }?.takeIf { it.isNotBlank() } ?: fallbackTitle

fun Document.secondaryLabel(fallbackSubtitle: String): String =
    when (searchingViewType) {
        SearchingViewType.Image -> displaySiteName ?: docUrl
        SearchingViewType.Video -> author ?: url
        null -> displaySiteName ?: author ?: docUrl ?: url
    }?.takeIf { it.isNotBlank() } ?: fallbackSubtitle
