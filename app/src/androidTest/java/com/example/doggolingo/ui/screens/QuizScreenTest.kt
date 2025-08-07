package com.example.doggolingo.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.doggolingo.domain.DoggoRepository
import com.example.doggolingo.domain.models.Question
import com.example.doggolingo.ui.DoggoViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: DoggoViewModel

    class FakeDoggoRepository : DoggoRepository {

        private val choices = listOf("A", "B", "C", "D")
        private val questions = listOf(Question("url1", choices, "A"), Question("url2", choices, "B"))

        override suspend fun getAllBreeds(): Set<String> = setOf("testBreed")
        override suspend fun loadQuestions(breeds: Set<String>): List<Question> = questions
    }

    @Before
    fun setUp() {
        viewModel = DoggoViewModel(FakeDoggoRepository())
    }

    @Test
    fun quizScreen_displaysQuestion_andAcceptsAnswer() = runTest {
        viewModel.loadQuiz()

        composeTestRule.setContent {
            QuizScreen(viewModel = viewModel)
        }

        composeTestRule
            .onNodeWithText("Question 1 of 2")
            .assertIsDisplayed()

        listOf("A", "B", "C", "D").forEach {
            composeTestRule.onNodeWithText(it).assertIsDisplayed()
        }

        composeTestRule.onNodeWithText("A").performClick()

        composeTestRule.waitUntil(timeoutMillis = 2000) {
            viewModel.currentQuestionId.value == 1
        }

        composeTestRule
            .onNodeWithText("Question 2 of 2")
            .assertIsDisplayed()
    }
}