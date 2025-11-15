package com.edricchan.studybuddy.data.repo.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.data.repo.crud.Countable
import com.edricchan.studybuddy.data.repo.crud.DataSource
import com.edricchan.studybuddy.data.repo.crud.HasBatchOperations
import com.edricchan.studybuddy.data.repo.crud.HasQueryOperations
import com.google.firebase.Firebase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

/**
 * [DataSource] which uses Firebase Firestore.
 * @property collectionRef [CollectionReference] to retrieve the data from.
 * @property klass [KClass] representation of the POJO class to be used for [DataSource].
 * @property batchFactory Lambda used to instantiate a [Batch] for [createBatch].
 * @see DataSource
 * @see HasQueryOperations
 */
open class FirestoreDataSource<T : HasId, Batch : FirestoreDataSource.FirestoreCrudBatch<T>>(
    private val collectionRef: CollectionReference,
    private val klass: KClass<T>,
    private val batchFactory: (CollectionReference) -> Batch
) : DataSource<T, String, DocumentReference>,
    HasQueryOperations<T, QueryMapper>,
    HasBatchOperations<Batch>,
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

    override suspend fun getSnapshot(id: String): T? = getRef(id).get().await().toObject(klass)

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

    override suspend fun createBatch(): Batch = batchFactory(collectionRef)

    /**
     * [HasBatchOperations.CrudBatch] which implements Firestore's [WriteBatch].
     * @property firestoreBatch The [WriteBatch] to use.
     * @property collectionRef Collection reference to be used for the [WriteBatch].
     */
    open class FirestoreCrudBatch<T : HasId>(
        protected val firestoreBatch: WriteBatch = Firebase.firestore.batch(),
        protected val collectionRef: CollectionReference
    ) : HasBatchOperations.CrudBatch<T, String> {
        override fun set(id: String, data: T) {
            firestoreBatch[collectionRef.document(id)] = data
        }

        override fun update(id: String, data: Map<String, Any?>) {
            firestoreBatch.update(collectionRef.document(id), data)
        }

        override fun delete(id: String) {
            firestoreBatch.delete(collectionRef.document(id))
        }

        override suspend fun commit() {
            firestoreBatch.commit().await()
        }
    }
}
