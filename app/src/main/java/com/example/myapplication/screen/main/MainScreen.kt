package com.example.myapplication.screen.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
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

private const val NAVIGATION_RAIL_MIN_WIDTH_DP = 600

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isDetailRoute = currentRoute?.startsWith("${SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName}/") == true
    val shouldShowNavigation = !isDetailRoute
    val shouldUseNavigationRail = configuration.screenWidthDp >= NAVIGATION_RAIL_MIN_WIDTH_DP

    if (shouldUseNavigationRail && shouldShowNavigation) {
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
        ) {
            SearchingNavigationRail(navController = navController)
            MainNavHost(
                navController = navController,
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxSize(),
            )
        }
    } else {
        Scaffold(
            bottomBar = {
                if (shouldShowNavigation) {
                    SearchingBottomBar(navController)
                }
            },
        ) { innerPadding ->
            MainNavHost(
                navController = navController,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            )
        }
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SearchingRoute.SEARCHING_SCREEN.routeName,
        modifier = modifier,
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
                effectFlow = viewModel.effect,
                setEvent = viewModel::setEvent,
                onNavigateToDetail = { uri ->
                    navController.navigate("${SearchingRoute.SEARCHING_DETAIL_SCREEN.routeName}/$uri")
                },
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
