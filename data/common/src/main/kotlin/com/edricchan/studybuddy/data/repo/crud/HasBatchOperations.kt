package com.edricchan.studybuddy.data.repo.crud

import com.edricchan.studybuddy.data.repo.crud.HasBatchOperations.CrudBatch
import com.google.firebase.firestore.WriteBatch

/**
 * Represents a data-source that supports batch CRUD operations.
 * @param Batch Class used to support batch CRUD operations. For example,
 * this would be the [WriteBatch] class from Firestore.
 * @see CrudBatch
 * @see DataSource
 */
interface HasBatchOperations<Batch : CrudBatch<*, *>> {
    /** Sub-classes should implement this method to create [Batch] instances. */
    suspend fun createBatch(): Batch

    /** Runs the specified [batchFn] operation. */
    suspend fun runBatch(batchFn: Batch.() -> Unit) {
        createBatch().apply(batchFn).commit()
    }

    /**
     * Interface which holds the possible batch operations that [HasBatchOperations.runBatch]
     * accepts.
     *
     * By default, the following operations are supported:
     * * **Create:** [set]
     * * **Update:** [update]
     * * **Delete:** [delete]
     *
     * [commit] should be called to persist the changes.
     */
    interface CrudBatch<T, Id> {
        // Create
        /**
         * Adds an set/overwrite operation to this [CrudBatch] with the specified arguments.
         * @param id The document's ID to be set.
         * @param data The data to set.
         */
        operator fun set(id: Id, data: T)

        /**
         * Adds an set/overwrite operation for every value in [ids] with the new [data].
         * @param ids The document IDs to be set.
         * @param data The data to set.
         */
        fun setAll(ids: Set<Id>, data: T) {
            ids.forEach { set(it, data) }
        }

        /**
         * Adds an set/overwrite operation for every value in [ids] with the new [data].
         * @param ids The document IDs to be set.
         * @param data The data to set.
         * @see setAll
         */
        fun setAll(ids: Set<Id>, data: (id: Id) -> T) {
            ids.forEach { set(it, data(it)) }
        }

        /**
         * Adds an set/overwrite operation for every value in [ids] with the new [data].
         * @param ids The document IDs to be set.
         * @param data The data to set, where the `index` and `id` are passed as arguments.
         * @see Iterable.forEachIndexed
         */
        fun setAllIndexed(ids: Set<Id>, data: (index: Int, id: Id) -> T) {
            ids.forEachIndexed { index, id -> set(id, data(index, id)) }
        }

        // Update
        /**
         * Adds an update operation to this [CrudBatch] with the specified arguments.
         * @param id The document's ID to be updated.
         * @param data The new data.
         */
        fun update(id: Id, data: Map<String, Any?>)

        /**
         * Adds an update operation to this [CrudBatch] with the specified arguments.
         *
         * This variant allows for the [buildMap] DSL to be used.
         * @param id The document's ID to be updated.
         * @param dataAction The new data.
         */
        fun update(id: Id, dataAction: MutableMap<String, Any?>.() -> Unit) {
            update(id, buildMap(dataAction))
        }

        // Delete
        /** Adds a delete operation to this [CrudBatch]. */
        fun delete(id: Id)

        /**
         * Adds a delete operation for every value in [ids].
         * @see delete
         */
        fun deleteAll(ids: Set<Id>) {
            ids.forEach { delete(it) }
        }

        /**
         * Adds a delete operation for every value in [ids] using [buildSet]
         * DSL syntax.
         * @see delete
         * @see deleteAll
         */
        fun deleteAll(idsAction: MutableSet<Id>.() -> Unit) {
            deleteAll(buildSet(idsAction))
        }

        /** Commits the batch operations. */
        suspend fun commit()
    }
}
