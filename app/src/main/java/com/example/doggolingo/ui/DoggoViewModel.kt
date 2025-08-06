package com.example.doggolingo.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doggolingo.domain.DoggoRepository
import com.example.doggolingo.domain.models.Question
import com.example.doggolingo.ui.models.AnswerResult
import com.example.doggolingo.ui.models.AnswerState
import com.example.doggolingo.ui.models.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoggoViewModel @Inject constructor(val repository: DoggoRepository) : ViewModel() {

    private val _navEvent = MutableSharedFlow<NavigationEvent>()
    val navEvent: SharedFlow<NavigationEvent>
        get() = _navEvent.asSharedFlow()

    private val _answerResult = MutableSharedFlow<AnswerResult>()
    val answerResult: SharedFlow<AnswerResult>
        get() = _answerResult.asSharedFlow()

    private val _currentQuestionId = MutableStateFlow<Int>(0)
    val currentQuestionId: StateFlow<Int>
        get() = _currentQuestionId.asStateFlow()

    private val _questions = mutableListOf<Question>()
    val questions: List<Question>
        get() = _questions.toList()
    
    var score = 0
        private set

    private var breeds = setOf<String>()
    private var loadJob: Job? = null

    fun onStart() {
        viewModelScope.launch {
            _navEvent.emit(NavigationEvent.LOADING)
        }
    }

    fun loadQuiz(context: Context) {
        loadJob = viewModelScope.launch {
            breeds = repository.getAllBreeds()
            resetQuizState()
            _questions.addAll(repository.loadQuestions(context, breeds))
            if (breeds.isNotEmpty() && _questions.isNotEmpty()) {
                _navEvent.emit(NavigationEvent.QUIZ)
            } else {
                _navEvent.emit(NavigationEvent.TITLE)
            }
        }
    }

    fun cancelQuizLoad() {
        loadJob?.cancel()
    }

    fun checkAnswer(id: Int, choice: String) {
        viewModelScope.launch {
            val answer = _questions[id].answer
            val isCorrect = choice == answer
            if (isCorrect) {
                score += 1
            }
            val state = if (isCorrect) AnswerState.CORRECT else AnswerState.WRONG
            _answerResult.emit(AnswerResult(state, choice))
        }
    }

    fun nextQuestion() {
        viewModelScope.launch {
            if (_currentQuestionId.value < _questions.size - 1) {
                _currentQuestionId.value += 1
            } else {
                _navEvent.emit(NavigationEvent.RESULT)
            }
        }
    }

    fun onTryAgain() {
        viewModelScope.launch {
            _navEvent.emit(NavigationEvent.LOADING)
        }
    }

    fun onBackToTitle() {
        viewModelScope.launch {
            _navEvent.emit(NavigationEvent.TITLE)
        }
    }

    fun resetQuizState() {
        score = 0
        _currentQuestionId.value = 0
        _questions.clear()
    }
}