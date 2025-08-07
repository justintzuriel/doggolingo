package com.example.doggolingo.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.doggolingo.ui.models.AnswerResult
import com.example.doggolingo.ui.models.AnswerState

/**
 * A stylized button that changes color depending on the current AnswerResult
 */
@Composable
fun AnswerButton(
    text: String,
    answer: String,
    result: AnswerResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state = when {
        result.state == AnswerState.NONE -> ButtonState.AVAILABLE
        text == answer -> ButtonState.CORRECT
        text == result.choice -> ButtonState.WRONG
        else -> ButtonState.UNAVAILABLE
    }

    val (bgColor, contentColor) = when (state) {
        ButtonState.AVAILABLE -> {
            ButtonDefaults.buttonColors().let {
                it.containerColor to it.contentColor
            }
        }
        ButtonState.CORRECT -> {
            Color(0xFFA5D6A7) to Color(0xFF1B5E20)
        }
        ButtonState.WRONG -> {
            Color(0xFFEF9A9A) to Color(0xFFB71C1C)
        }
        else -> {
            ButtonDefaults.buttonColors().let {
                it.disabledContainerColor to it.disabledContentColor
            }
        }
    }

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = bgColor,
        contentColor = contentColor,
        disabledContainerColor = bgColor,
        disabledContentColor = contentColor
    )

    Button(
        modifier = modifier.width(200.dp),
        onClick = onClick,
        colors = buttonColors,
        enabled = state == ButtonState.AVAILABLE
    ) {
        Text(text)
    }
}

enum class ButtonState {
    AVAILABLE,      // not answered yet
    UNAVAILABLE,    // answered, unselected wrong choice
    CORRECT,        // answered, correct choice
    WRONG           // answered, selected wrong choice (if answered incorrectly)
}