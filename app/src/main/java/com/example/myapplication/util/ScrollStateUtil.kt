package com.example.myapplication.util

import androidx.compose.foundation.lazy.LazyListState

/**
 * Pagination 처리를 위한 확장 함수
 *
 * page를 증가 시켜서 서버에 요청하기 전, 현재 스크롤 상태로부터 페이징 처리를 할 것인지 안할 것인지 판단한다.
 * 만약 리스트의 마지막으로 부터10 번째의 아이템이 보여졌다면 다음 페이지를 요청하고, 그게 아니라면 요청하지 않는다.
 * @return
 */
fun LazyListState.isFinishedScroll(): Boolean {
    return layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 11
}