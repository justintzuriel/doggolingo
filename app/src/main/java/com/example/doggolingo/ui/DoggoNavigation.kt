package com.example.doggolingo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doggolingo.ui.models.NavigationEvent
import com.example.doggolingo.ui.screens.LoadingScreen
import com.example.doggolingo.ui.screens.QuizScreen
import com.example.doggolingo.ui.screens.ResultScreen
import com.example.doggolingo.ui.screens.TitleScreen

@Composable
fun DoggoNavigation() {
    val navController = rememberNavController()
    val viewModel: DoggoViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect {
            when (it) {
                NavigationEvent.TITLE -> {
                    navController.navigate(NavigationEvent.TITLE.route) {
                        popUpTo(NavigationEvent.TITLE.route) {
                            inclusive = true
                        }
                    }
                }
                NavigationEvent.LOADING -> {
                    navController.navigate(NavigationEvent.LOADING.route)
                }
                NavigationEvent.QUIZ -> {
                    navController.navigate(NavigationEvent.QUIZ.route) {
                        popUpTo(NavigationEvent.LOADING.route) {
                            inclusive = true
                        }
                    }
                }
                NavigationEvent.RESULT -> {
                    navController.navigate(NavigationEvent.RESULT.route) {
                        popUpTo(NavigationEvent.QUIZ.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = NavigationEvent.TITLE.route) {
        composable(NavigationEvent.TITLE.route) {
            TitleScreen(viewModel = viewModel)
        }
        composable(NavigationEvent.LOADING.route) {
            LoadingScreen(viewModel = viewModel)
        }
        composable(NavigationEvent.QUIZ.route) {
            QuizScreen(viewModel = viewModel)
        }
        composable(NavigationEvent.RESULT.route) {
            ResultScreen(viewModel = viewModel)
        }
    }
}