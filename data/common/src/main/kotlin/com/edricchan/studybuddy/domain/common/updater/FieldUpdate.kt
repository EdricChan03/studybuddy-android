package com.edricchan.studybuddy.domain.common.updater

import kotlinx.serialization.Serializable

/** Represents an update operation to be performed to a field in a document. */
@Serializable
sealed interface FieldUpdate<out T> {
    /** Leave the field as-is. */
    data object Unchanged : FieldUpdate<Nothing>

    /** Set a new value to this field. */
    data class Assign<T>(val value: T) : FieldUpdate<T>
}
