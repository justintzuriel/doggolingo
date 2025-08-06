package com.example.doggolingo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessStarted
import com.example.doggolingo.ui.DoggoViewModel
import com.example.doggolingo.R

@Composable
fun TitleScreen(modifier: Modifier = Modifier, viewModel: DoggoViewModel) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_home_screen_logo),
            contentDescription = null
        )
        Text("Get to know your breeds!")
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier.width(200.dp),
            onClick = dropUnlessStarted { viewModel.onStart() }
        ) {
            Text("Start")
        }
    }
}