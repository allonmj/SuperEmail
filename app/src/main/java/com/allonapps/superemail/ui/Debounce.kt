package com.allonapps.superemail.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long = 500,
    scope: CoroutineScope,
    action: (T) -> Unit
): (T) -> Unit {
    var job: Job? = null
    return { param: T ->
        if (job == null) {
            job = scope.launch {
                action(param)
                delay(delayMillis)
                job = null
            }
        }
    }
}