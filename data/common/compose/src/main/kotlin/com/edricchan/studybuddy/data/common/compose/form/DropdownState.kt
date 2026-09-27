package com.edricchan.studybuddy.data.common.compose.form

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import kotlinx.serialization.Serializable

/** Represents whether a value has been selected in a drop-down field. */
@Serializable
sealed interface DropdownState<out Value> {
    /** No value has been set yet. */
    data object Unset : DropdownState<Nothing>

    /** A [value] has been set. */
    data class Selected<Value>(val value: Value) : DropdownState<Value>

    companion object {
        /** Gets the [androidx.compose.runtime.saveable.Saver] for this state. */
        fun <Value> Saver(): Saver<DropdownState<Value>, Any> = listSaver(
            save = {
                listOf(
                    it is Unset,
                    it.valueOrNull()
                )
            },
            restore = { (isUnset, value) ->
                if (isUnset as Boolean) Unset
                else Selected(value as Value)
            }
        )
    }
}

/**
 * Retrieves the currently selected value for the receiver [DropdownState]
 * or `null` if [DropdownState.Unset].
 */
fun <Value> DropdownState<Value>.valueOrNull(): Value? = when (this) {
    is DropdownState.Selected -> value
    else -> null
}
