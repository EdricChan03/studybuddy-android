package com.edricchan.studybuddy.data.repo.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.data.repo.crud.CrudRepository
import com.edricchan.studybuddy.data.repo.crud.HasQueryOperations
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

/**
 * [CrudRepository] which uses Firebase Firestore as its underlying data-source.
 * @property collectionRef [CollectionReference] to retrieve the data from.
 * @property klass [KClass] representation of the POJO class to be used for [CrudRepository].
 * @see CrudRepository
 * @see HasQueryOperations
 */
open class FirestoreRepository<T : HasId>(
    private val collectionRef: CollectionReference,
    private val klass: KClass<T>
) : CrudRepository<T, String, DocumentReference>,
    HasQueryOperations<T, QueryMapper>,
    Countable<Long> {
    /**
     * Retrieves a document from the [collectionRef].
     * @see CollectionReference.document
     */
    fun document(path: String) = collectionRef.document(path)

    override val items = collectionRef.snapshots().map { it.toObjects(klass) }

    override fun findAll(query: QueryMapper) =
        query(collectionRef).snapshots().map { it.toObjects(klass) }

    override fun get(id: String) =
        document(id).snapshots().map { it.toObject(klass) }

    override suspend fun getRef(id: String): DocumentReference = collectionRef.document(id)

    override suspend fun count(): Long =
        collectionRef.count()[AggregateSource.SERVER].await().count

    override suspend fun add(item: T): DocumentReference =
        collectionRef.add(item).await()

    override suspend fun remove(item: T) = removeById(item.id)

    override suspend fun removeById(id: String) {
        document(id).delete().await()
    }

    override suspend fun update(id: String, data: Map<String, Any?>) {
        document(id).update(data).await()
    }
}
