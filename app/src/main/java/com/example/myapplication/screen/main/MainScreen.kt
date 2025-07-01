package com.example.myapplication.screen.main

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.common.SearchingRoute
import com.example.myapplication.screen.saved.SavedSearchingScreen
import com.example.myapplication.screen.searching.SearchingDetailScreen
import com.example.myapplication.screen.searching.SearchingScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = currentRoute?.startsWith("${SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName}/") ?: true

    Scaffold(
        bottomBar = {
            if (!shouldShowBottomBar) {
                SearchingBottomBar(navController)
            }
        }
    ) { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = SearchingRoute.SEARCHING_SCREEN.routeName,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(SearchingRoute.SEARCHING_SCREEN.routeName) {
                SearchingScreen(navController)
            }
            composable(SearchingRoute.SAVED_SEARCHING_SCREEN.routeName) {
                SavedSearchingScreen()
            }
            composable(
                "${SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName}/{uri}",
                arguments = listOf(navArgument("uri") { type = NavType.StringType })
            ) { backStackEntry ->
                val uri = backStackEntry.arguments?.getString("uri") ?: return@composable

                SearchingDetailScreen(uri)
            }
        }
    }
}