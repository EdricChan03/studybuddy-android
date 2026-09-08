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

// Constructors

/**
 * Compares the given [source] and [newValue] with [shouldUpdate], returning a [FieldUpdate]
 * respectively.
 */
inline fun <T> compareFields(
    source: T,
    newValue: T,
    shouldUpdate: (source: T, newValue: T) -> Boolean = { source, newValue -> source != newValue }
): FieldUpdate<T> = if (shouldUpdate(source, newValue)) FieldUpdate.Assign(newValue)
else FieldUpdate.Unchanged

/** Returns a [FieldUpdate.Assign] with the given [newValue]. */
fun <T> setField(newValue: T): FieldUpdate.Assign<T> = FieldUpdate.Assign(newValue)

/** Returns an [FieldUpdate.Unchanged]. */
fun unchangedField(): FieldUpdate.Unchanged = FieldUpdate.Unchanged

// Conditionals

/** Runs the given [block] if the receiver [FieldUpdate] is [FieldUpdate.Assign]. */
inline fun <T> FieldUpdate<T>.ifAssigned(block: (T) -> Unit) {
    if (this is FieldUpdate.Assign) block(value)
}

/**
 * Retrieves the [value][FieldUpdate.Assign.value] for the receiver [FieldUpdate]
 * or the result of [defaultValue] if the receiver is [FieldUpdate.Unchanged].
 */
inline fun <T> FieldUpdate<T>.getOrElse(defaultValue: () -> T): T =
    if (this is FieldUpdate.Assign) value else defaultValue()

// Mapping

/**
 * Runs and returns the result from the [assign] function if the receiver [FieldUpdate]
 * is [FieldUpdate.Assign] with its [FieldUpdate.Assign.value] passed as the argument,
 * or the result of [unchanged] otherwise.
 */
inline fun <T, R> FieldUpdate<T>.fold(
    assign: (T) -> R,
    unchanged: () -> R
): R = if (this is FieldUpdate.Assign) assign(value) else unchanged()

/**
 * Maps the [FieldUpdate.Assign.value] with the given [transform] function if the
 * receiver [FieldUpdate] is [FieldUpdate.Assign], or [FieldUpdate.Unchanged] otherwise.
 */
inline fun <T, R> FieldUpdate<T>.map(
    transform: (T) -> R
): FieldUpdate<R> = if (this is FieldUpdate.Assign) setField(newValue = transform(value))
else unchangedField()

// Resolving

/**
 * Gets the current [FieldUpdate.Assign.value] of the receiver [FieldUpdate], or
 * [current] otherwise.
 */
fun <T> FieldUpdate<T>.resolve(current: T): T = if (this is FieldUpdate.Assign) value else current

/**
 * Gets the current [FieldUpdate.Assign.value] of the receiver [FieldUpdate], or
 * `null` otherwise.
 */
fun <T> FieldUpdate<T>.resolveOrNull(): T? = if (this is FieldUpdate.Assign) value else null
