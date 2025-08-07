package com.example.doggolingo.ui

import app.cash.turbine.test
import com.example.doggolingo.domain.DoggoRepository
import com.example.doggolingo.domain.models.Question
import com.example.doggolingo.ui.models.AnswerResult
import com.example.doggolingo.ui.models.AnswerState
import com.example.doggolingo.ui.models.NavigationTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DoggoViewModelTest {

    private lateinit var viewModel: DoggoViewModel

    class FakeDoggoRepository : DoggoRepository {

        private val choices = listOf("A", "B", "C", "D")
        private val questions = listOf(Question("url1", choices, "A"), Question("url2", choices, "B"))

        override suspend fun getAllBreeds(): Set<String> = setOf("testBreed")
        override suspend fun loadQuestions(breeds: Set<String>): List<Question> = questions
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = DoggoViewModel(FakeDoggoRepository())
    }

    @Test
    fun `onStartClicked emits LOADING`() = runTest {
        viewModel.navEvent.test {
            viewModel.onStartClicked()
            assertEquals(NavigationTarget.LOADING, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadQuiz populates questions & emits QUIZ`() = runTest {
        viewModel.navEvent.test {
            viewModel.loadQuiz()
            assertEquals(viewModel.questions.size, 2)
            assertEquals(NavigationTarget.QUIZ, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `checkAnswer with correct answer increases score & emits CORRECT`() = runTest {
        viewModel.answerResult.test {
            viewModel.loadQuiz()
            assertEquals(0, viewModel.score)

            viewModel.checkAnswer(0, "A")
            testScheduler.advanceUntilIdle()
            assertEquals(1, viewModel.score)
            assertEquals(AnswerResult(AnswerState.CORRECT, "A"), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `checkAnswer with wrong answer does not increase score & emits WRONG`() = runTest {
        viewModel.answerResult.test {
            viewModel.loadQuiz()
            assertEquals(0, viewModel.score)

            viewModel.checkAnswer(0, "B")
            testScheduler.advanceUntilIdle()
            assertEquals(0, viewModel.score)
            assertEquals(AnswerResult(AnswerState.WRONG, "B"), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `nextQuestion not finished increases currentQuestionId`() = runTest {
        viewModel.currentQuestionId.test {
            viewModel.loadQuiz()
            assertEquals(0, awaitItem())

            viewModel.nextQuestion()
            assertEquals(1, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `nextQuestion finished emits RESULT`() = runTest {
        viewModel.navEvent.test {
            viewModel.loadQuiz()
            skipItems(1)

            repeat(2) {
                viewModel.nextQuestion()
            }
            assertEquals(NavigationTarget.RESULT, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTryAgain emits LOADING`() = runTest {
        viewModel.navEvent.test {
            viewModel.onTryAgain()
            assertEquals(NavigationTarget.LOADING, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBackToTitle emits TITLE`() = runTest {
        viewModel.navEvent.test {
            viewModel.onBackToTitle()
            assertEquals(NavigationTarget.TITLE, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}