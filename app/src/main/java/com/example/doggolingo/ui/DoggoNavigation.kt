package com.example.doggolingo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doggolingo.ui.models.NavigationTarget
import com.example.doggolingo.ui.screens.LoadingScreen
import com.example.doggolingo.ui.screens.QuizScreen
import com.example.doggolingo.ui.screens.ResultScreen
import com.example.doggolingo.ui.screens.TitleScreen

/**
 * Sets up navigation and handles navigation events
 */
@Composable
fun DoggoNavigation() {

    val navController = rememberNavController()
    val viewModel: DoggoViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            if (!isRouteAllowed(it, navController)) return@collect
            navController.navigate(it.route) {
                popUpTo(NavigationTarget.TITLE.route) {
                    inclusive = (it == NavigationTarget.TITLE)
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = NavigationTarget.TITLE.route) {
        composable(NavigationTarget.TITLE.route) {
            TitleScreen(viewModel = viewModel)
        }
        composable(NavigationTarget.LOADING.route) {
            LoadingScreen(viewModel = viewModel)
        }
        composable(NavigationTarget.QUIZ.route) {
            QuizScreen(viewModel = viewModel)
        }
        composable(NavigationTarget.RESULT.route) {
            ResultScreen(viewModel = viewModel)
        }
    }
}

val allowedRoutes = mapOf<String, List<String>>(
    NavigationTarget.TITLE.route to listOf(NavigationTarget.LOADING.route),
    NavigationTarget.LOADING.route to listOf(NavigationTarget.QUIZ.route),
    NavigationTarget.QUIZ.route to listOf(NavigationTarget.RESULT.route),
    NavigationTarget.RESULT.route to listOf(
        NavigationTarget.TITLE.route,
        NavigationTarget.LOADING.route
    )
)

fun isRouteAllowed(target: NavigationTarget, navController: NavController): Boolean {
    return navController.currentDestination?.route?.let {
        target.route in (allowedRoutes[it] ?: listOf())
    } == true
}