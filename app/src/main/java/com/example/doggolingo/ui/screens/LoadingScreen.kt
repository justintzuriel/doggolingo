package com.example.doggolingo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.doggolingo.ui.DoggoViewModel

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, viewModel: DoggoViewModel) {
    DisposableEffect(Unit) {
        viewModel.loadQuiz()
        onDispose {
            viewModel.cancelQuizLoad()
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text("Loading...")
    }
}