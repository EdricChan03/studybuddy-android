package com.edricchan.studybuddy.data.repo.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.data.repo.crud.Countable
import com.edricchan.studybuddy.data.repo.crud.CrudRepository
import com.edricchan.studybuddy.data.repo.crud.HasQueryOperations
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

/**
 * [CrudRepository] which uses Firebase Firestore as its underlying data-source.
 *
 * This variant allows for a [Flow] of the [CollectionReference] to be used.
 * For its non-[Flow] equivalent, use [FirestoreRepository] instead.
 * @property collectionRefFlow [CollectionReference] to retrieve the data from as a
 * [Flow].
 * @property klass [KClass] representation of the POJO class to be used for [CrudRepository].
 * @see CrudRepository
 * @see HasQueryOperations
 * @see FirestoreRepository
 */
open class FlowableFirestoreRepository<T : HasId>(
    private val collectionRefFlow: Flow<CollectionReference>,
    private val klass: KClass<T>
) : CrudRepository<T, String, DocumentReference>,
    HasQueryOperations<T, QueryMapper>,
    Countable<Long> {
    private suspend fun getCollectionRef() = collectionRefFlow.first()

    /**
     * Retrieves a document from the [collectionRefFlow] as a [Flow].
     * @see CollectionReference.document
     */
    fun documentFlow(path: String) = collectionRefFlow.map { it.document(path) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val items =
        collectionRefFlow.flatMapLatest { ref -> ref.snapshots().map { it.toObjects(klass) } }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findAll(query: QueryMapper) =
        collectionRefFlow.flatMapConcat { ref ->
            query(ref).snapshots().map { it.toObjects(klass) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun get(id: String) =
        documentFlow(id).flatMapConcat { ref -> ref.snapshots().map { it.toObject(klass) } }

    override suspend fun getRef(id: String): DocumentReference = getCollectionRef().document(id)

    override suspend fun count(): Long =
        getCollectionRef().count()[AggregateSource.SERVER].await().count

    override suspend fun add(item: T): DocumentReference = getCollectionRef().add(item).await()

    override suspend fun remove(item: T) = removeById(item.id)

    override suspend fun removeById(id: String) {
        getRef(id).delete().await()
    }

    override suspend fun update(id: String, data: Map<String, Any?>) {
        getRef(id).update(data).await()
    }
}
