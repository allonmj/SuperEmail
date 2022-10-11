package com.allonapps.superemail.ui.screens

import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.allonapps.superemail.ui.debounce
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

@Composable
fun TapDetector(
    numFingers: Int = 2,
    listener: () -> Unit,
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val debounce = remember {
        debounce<Unit>(scope = MainScope()) {
            listener.invoke()
        }
    }
    Box(modifier = modifier.pointerInput(Unit) {
        forEachGesture {
            val context = currentCoroutineContext()
            awaitPointerEventScope {
                var event = awaitPointerEvent()
                while (event.changes.any { it.pressed } && context.isActive) {
                    event = awaitPointerEvent()
                    if (event.changes.size == numFingers) {
                        debounce.invoke(Unit)
                    }
                }
            }
        }
    }) {
        content()
    }
}