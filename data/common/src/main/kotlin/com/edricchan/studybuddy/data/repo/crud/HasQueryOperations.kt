package com.edricchan.studybuddy.data.repo.crud

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository that has query operations.
 *
 * This interface is separate from [CrudRepository] to facilitate
 * repositories that might not have the ability to perform queries on the
 * underlying data.
 * @see CrudRepository
 * @param T Concrete POJO class that the CRUD operations will return.
 * @param Query Type representing a class of some kind which can be used to query
 * data from the underlying database.
 */
interface HasQueryOperations<T, Query> {
    /** Retrieves items of type [T] that match the given [query] criteria. */
    fun findAll(query: Query): Flow<List<T>>

    /**
     * Retrieves the first item of type [T] that matches the given [query] criteria,
     * or `null` if no items were matched.
     */
    fun findFirst(query: Query): Flow<T?> = findAll(query).map { it.firstOrNull() }
}
