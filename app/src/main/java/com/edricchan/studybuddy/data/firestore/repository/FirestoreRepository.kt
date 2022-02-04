package com.edricchan.studybuddy.data.firestore.repository

import com.edricchan.studybuddy.data.firestore.CollectionToQueryMapper
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

/**
 * Generic repository interface that business logic related to retrieving
 * data from Firebase Firestore should implement.
 *
 * ## Overview
 *
 * This repository interface provides commonly-used methods for Firestore
 * operations including:
 * - [get] to retrieve an entity given the ID,
 * - [exists] to check if an entity exists given the ID,
 * - [create] to create an entity,
 * - [update] to update an entity,
 * - [delete] to delete an entity given the ID,
 * - and methods for observing a list of entities or a given entity
 * ([observeAll] and [observe] respectively).
 *
 * @param Entity The entity to be used to represent the Firestore data.
 * @param EntityKey The data type to be used for the [Entity]'s
 * unique ID.
 */
interface FirestoreRepository<Entity, EntityKey> {
    /**
     * Checks if the specified [document ID][id] exists.
     * @param id The document ID to retrieve
     */
    suspend fun exists(id: EntityKey): Boolean

    /**
     * Retrieves the specified entity.
     * If no such entity actually exists at the specified [id], `null` is returned.
     * @param id The entity's document ID
     */
    suspend fun get(id: EntityKey): Entity?

    /**
     * Observes the specified entity as a [flow][Flow].
     * @param id The entity's document ID
     */
    fun observe(id: EntityKey): Flow<Entity?>

    /**
     * Retrieves the list of entities.
     * Optionally, a [query] can be passed which allows for
     * [ordering and limiting data](https://firebase.google.com/docs/firestore/query-data/order-limit-data).
     * @param query The query used to filter the data by. By default, no queries are set.
     */
    suspend fun list(query: CollectionToQueryMapper? = null): List<Entity>

    /**
     * Observes the list of entities as a [flow][Flow].
     * Optionally, a [query] can be passed which allows for
     * [ordering and limiting data](https://firebase.google.com/docs/firestore/query-data/order-limit-data).
     * @param query The query used to filter the data by. By default, no queries are set.
     */
    fun observeAll(query: CollectionToQueryMapper? = null): Flow<List<Entity>>

    /**
     * Creates the specified entity and returns the [document reference][DocumentReference]
     * for the created entity.
     * @param entity The new entity
     */
    suspend fun create(entity: Entity): DocumentReference

    /**
     * Updates the specified entity with the new data.
     * @param entity The entity to update
     */
    suspend fun update(entity: Entity)

    /**
     * Updates the specified entity with the new data.
     * @param id The entity's ID to update
     * @param entityMap A map of document fields and their new values
     */
    suspend fun update(id: EntityKey, entityMap: Map<String, Any?>)

    /**
     * Deletes the specified entity [id].
     * @param id The entity's ID to delete
     */
    suspend fun delete(id: EntityKey)
}
