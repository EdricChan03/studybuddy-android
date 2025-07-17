package com.edricchan.studybuddy.exts.androidx.compose

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Adds a filter to the specified [onClick] lambda where calls to the
 * [onClick] lambda within the specified [debounceTime] are not registered.
 * @param debounceTime Desired interval to filter lambda calls by.
 * @param getElapsedRealTimeMillis Lambda to be used to get the current elapsed real-time,
 * in milli-seconds. Defaults to [SystemClock.elapsedRealtime] if not specified.
 * @param onClick Lambda to be called when the interval is passed.
 * @return Wrapped lambda to be used in code where a lambda is expected.
 */
@Composable
inline fun debounced(
    debounceTime: Duration = 500.milliseconds,
    crossinline getElapsedRealTimeMillis: () -> Long = { SystemClock.elapsedRealtime() },
    crossinline onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(getElapsedRealTimeMillis()) }
    return {
        getElapsedRealTimeMillis().let { currentTime ->
            if ((currentTime - lastClickTime) >= debounceTime.inWholeMilliseconds) {
                lastClickTime = currentTime
                onClick()
            }
        }
    }
}
