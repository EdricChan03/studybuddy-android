package com.edricchan.studybuddy.utils.coroutines.flow

import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// Implementation from
// https://github.com/Kotlin/kotlinx.coroutines/issues/2631#issuecomment-870565860

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
private class DerivedStateFlow<T>(
    private val getValue: () -> T,
    private val flow: Flow<T>
) : StateFlow<T> {
    override val replayCache get() = listOf(value)

    override val value get() = getValue()

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        coroutineScope { flow.distinctUntilChanged().stateIn(this).collect(collector) }
    }
}

/**
 * Returns a [StateFlow] containing the results of applying the given [transform]
 * function to each value of the original flow.
 */
fun <T1, R> StateFlow<T1>.mapState(transform: (a: T1) -> R): StateFlow<R> {
    return DerivedStateFlow(
        getValue = { transform(this.value) },
        flow = this.map { a -> transform(a) }
    )
}

/**
 * Returns a [StateFlow] whose values are generated with [transform] function
 * by combining the most recently emitted values by each flow.
 */
fun <T1, T2, R> combineStates(
    flow: StateFlow<T1>,
    flow2: StateFlow<T2>,
    transform: (a: T1, b: T2) -> R
): StateFlow<R> {
    return DerivedStateFlow(
        getValue = { transform(flow.value, flow2.value) },
        flow = combine(flow, flow2) { a, b -> transform(a, b) }
    )
}
