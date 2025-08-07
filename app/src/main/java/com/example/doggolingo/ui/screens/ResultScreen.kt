package com.example.doggolingo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doggolingo.ui.DoggoViewModel

@Composable
fun ResultScreen(modifier: Modifier = Modifier, viewModel: DoggoViewModel) {

    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Your Score:")
            Text(
                text = viewModel.score.toString(),
                style = TextStyle(fontSize = 30.sp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { viewModel.onTryAgain() }) {
                Text("Try again")
            }
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { viewModel.onBackToTitle() }) {
                Text("Back to title")
            }
        }
    }
}