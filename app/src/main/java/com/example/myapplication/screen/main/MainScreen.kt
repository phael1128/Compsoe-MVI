package com.example.myapplication.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.common.SearchingRoute
import com.example.myapplication.screen.saved.SavedSearchingScreen
import com.example.myapplication.screen.searching.SearchingScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { SearchingBottomBar(navController) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SearchingRoute.SEARCHING_SCREEN.routeName,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(SearchingRoute.SEARCHING_SCREEN.routeName) {
                SearchingScreen()
            }
            composable(SearchingRoute.SAVED_SEARCHING_SCREEN.routeName) {
                SavedSearchingScreen()
            }
        }
    }
}