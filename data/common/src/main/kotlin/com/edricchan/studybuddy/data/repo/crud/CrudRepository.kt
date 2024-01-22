package com.edricchan.studybuddy.data.repo.crud

import kotlinx.coroutines.flow.Flow

/**
 * Generic repository interface for Create, Read, Update and Delete operations.
 *
 * This interface does not have any tight coupling with any specific database implementation,
 * so it should be safe for use across databases.
 *
 * ## Querying
 * This interface does **not** expose the ability to perform additional querying on the
 * data. If needed, classes should also implement the [HasQueryOperations] interface for
 * such functionality.
 * @param T A concrete POJO class that the CRUD operations will return.
 * @param Id Type representing the POJO's `id` field. Usually a [String].
 * @param Reference Type representing a class which can be used to retrieve the underlying
 * representation of a [T] class from the database.
 */
interface CrudRepository<T, Id, Reference> {
    //#region Read operations
    /** Retrieves all data from this repository as a [Flow]. */
    val items: Flow<List<T>>

    /** Retrieves the given document's data given the [id] as a [Flow]. */
    operator fun get(id: Id): Flow<T?>
    //#endregion

    //#region Create operations
    /** Adds the given [item] to the data source. The resulting added document is returned. */
    suspend fun add(item: T): Reference
    //#endregion

    //#region Delete operations
    /** Removes the given [item] from the data source. */
    suspend fun remove(item: T)

    /** Removes the given item represented by the [id] from the data source. */
    suspend fun removeById(id: Id)
    //#endregion

    //#region Update operations
    /** Updates the given item represented by the [id] from the data source with the new [data]. */
    suspend fun update(id: Id, data: Map<String, Any?>)
    //#endregion
}
