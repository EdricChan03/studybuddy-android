package com.edricchan.studybuddy.data.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/** Represents the result of an asynchronous operation. */
sealed class Result<out T> {
    /** The asynchronous operation has yet to be completed. */
    data object Loading : Result<Nothing>()

    /** The asynchronous operation did not complete successfully, throwing the specified [error]. */
    data class Error(val error: Throwable) : Result<Nothing>()

    /** The asynchronous operation completed successfully, and produced the resulting [value]. */
    data class Success<T>(val value: T) : Result<T>()

    /**
     * The asynchronous operation did not run as expected,
     * returning the specified error [message].
     */
    data class Failed(val message: String) : Result<Nothing>() {
        /** Converts the receiver [Failed] to its [Result.Error] equivalent. */
        fun asError() = Error(Exception(message))
    }

    override fun toString(): String {
        return when (this) {
            Loading -> "Loading"
            is Error -> "Error(error=$error)"
            is Success -> "Success(value=$value)"
            is Failed -> "Failed(message=$message)"
        }
    }
}

/** Creates a [Result.Success] with the specified [value]. */
fun <T> Result(value: T) = Result.Success(value)

/** Creates a [Result.Error] with the specified [exception]. */
fun <T> Result(exception: Exception) = Result.Error(exception)

/** Creates a [Result.Failed] with the specified [message]. */
fun <T> Result(message: String) = Result.Failed(message)

/** Returns whether the receiver [Result] is [Result.Loading]. */
@OptIn(ExperimentalContracts::class)
fun Result<*>.isLoading(): Boolean {
    contract {
        returns(true) implies (this@isLoading is Result.Loading)
    }

    return this is Result.Loading
}

/** Returns whether the receiver [Result] is [Result.Success]. */
@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Result.Success<T>)
    }

    return this is Result.Success<T>
}

/** Returns whether the receiver [Result] is [Result.Error]. */
@OptIn(ExperimentalContracts::class)
fun Result<*>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Result.Error)
    }

    return this is Result.Error
}

/** Returns whether the receiver [Result] is [Result.Failed]. */
@OptIn(ExperimentalContracts::class)
fun Result<*>.isFailed(): Boolean {
    contract {
        returns(true) implies (this@isFailed is Result.Failed)
    }

    return this is Result.Failed
}

/**
 * Calls the specified function block and returns its encapsulated
 * result if invocation was successful, catching any [Throwable] exception
 * that was thrown from the [block] function execution and encapsulating it as a failure.
 * @see kotlin.runCatching
 */
inline fun <R> runCatching(block: () -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and
 * returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function
 * execution and encapsulating it as a failure.
 * @see kotlin.runCatching
 */
inline fun <T, R> T.runCatching(block: T.() -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
