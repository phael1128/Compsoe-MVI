package com.example.myapplication.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.myapplication.R
import com.example.myapplication.common.SearchingRoute

data class BottomNavigationModel(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int
)

val bottomNavigationItems = listOf(
    BottomNavigationModel(
        route = SearchingRoute.SEARCHING_SCREEN.routeName,
        label = R.string.searching,
        icon = R.drawable.vic_searching_data
    ),
    BottomNavigationModel(
        route = SearchingRoute.SAVED_SEARCHING_SCREEN.routeName,
        label = R.string.saved_searching,
        icon = R.drawable.vic_saved_data
    )
)