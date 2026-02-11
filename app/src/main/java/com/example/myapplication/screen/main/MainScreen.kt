package com.example.myapplication.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.common.SearchingRoute
import com.example.myapplication.screen.saved.SavedDocumentScreen
import com.example.myapplication.screen.searching.SearchingDetailScreen
import com.example.myapplication.screen.searching.SearchingScreen
import com.example.myapplication.viewmodels.SavedDocumentViewModel
import com.example.myapplication.viewmodels.SearchingViewModel

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
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SearchingRoute.SEARCHING_SCREEN.routeName,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(SearchingRoute.SEARCHING_SCREEN.routeName) {
                val viewModel: SearchingViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                SearchingScreen(
                    uiState = uiState,
                    effectFlow = viewModel.effect,
                    setEvent = viewModel::setEvent,
                    onNavigateToDetail = { uri ->
                        navController.navigate("${SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName}/$uri")
                    },
                )
            }
            composable(SearchingRoute.SAVED_SEARCHING_SCREEN.routeName) {
                val viewModel: SavedDocumentViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                SavedDocumentScreen(
                    uiState = uiState,
                    setEvent = viewModel::setEvent,
                )
            }
            composable(
                "${SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName}/{uri}",
                arguments = listOf(navArgument("uri") { type = NavType.StringType }),
            ) { backStackEntry ->
                val uri = backStackEntry.arguments?.getString("uri") ?: return@composable

                SearchingDetailScreen(uri)
            }
        }
    }
}
