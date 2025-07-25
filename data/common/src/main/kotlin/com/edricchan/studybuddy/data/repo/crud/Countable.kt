package com.edricchan.studybuddy.data.repo.crud

/**
 * Indicates that a repository can return the number of elements from its data-source.
 *
 * @param T The appropriate numeric data type that [count] should return.
 */
interface Countable<T : Number> {
    /** Retrieves the number of elements in the repository. */
    suspend fun count(): T
}
