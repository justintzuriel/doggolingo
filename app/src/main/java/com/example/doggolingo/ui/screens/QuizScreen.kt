package com.example.doggolingo.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.doggolingo.ui.DoggoViewModel
import com.example.doggolingo.R
import com.example.doggolingo.ui.components.AnswerButton
import com.example.doggolingo.ui.models.AnswerResult
import com.example.doggolingo.ui.models.AnswerState
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(modifier: Modifier = Modifier, viewModel: DoggoViewModel) {
    val questionId by viewModel.currentQuestionId.collectAsState()
    val total = viewModel.questions.size
    val question = viewModel.questions.getOrNull(questionId) ?: return

    var answerResult by remember { mutableStateOf(AnswerResult(AnswerState.NONE, "")) }

    LaunchedEffect(Unit) {
        viewModel.answerResult.collect {
            answerResult = it
            delay(1500)
            answerResult = AnswerResult(AnswerState.NONE, "")
            viewModel.nextQuestion()
        }
    }

    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Question ${questionId + 1} of $total")
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Crossfade(targetState = question.imageUrl) { imageUrl ->
                    AsyncImage(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp)
                            .padding(10.dp),
                        model = imageUrl,
                        contentDescription = "question_image"
                    )
                }
                if (answerResult.state != AnswerState.NONE) {
                    val res =
                        if (answerResult.state == AnswerState.CORRECT) R.raw.checkmark else R.raw.invalid
                    val comp by rememberLottieComposition(LottieCompositionSpec.RawRes(res))

                    LottieAnimation(
                        modifier = Modifier
                            .width(300.dp)
                            .height(300.dp),
                        composition = comp
                    )
                }
            }
            question.choices.forEach {
                AnswerButton(
                    text = it,
                    result = answerResult,
                    answer = question.answer,
                    onClick = { viewModel.checkAnswer(questionId, it) }
                )
            }
        }
    }
}