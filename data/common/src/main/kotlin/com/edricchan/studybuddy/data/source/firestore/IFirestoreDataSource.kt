package com.edricchan.studybuddy.data.source.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.data.source.crud.Countable
import com.edricchan.studybuddy.data.source.crud.DataSource
import com.edricchan.studybuddy.data.source.crud.HasBatchOperations
import com.edricchan.studybuddy.data.source.crud.HasBatchOperations.CrudBatch
import com.edricchan.studybuddy.data.source.crud.HasQueryOperations
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

/**
 * Generic Firestore-backed [DataSource].
 *
 * Classes which implement this interface have the ability for
 * [additional querying operations][HasQueryOperations],
 * [batch editing operations][HasBatchOperations] and [counting operations][Countable].
 * @see FirestoreDataSource
 * @see FlowableFirestoreDataSource
 */
sealed interface IFirestoreDataSource<T : HasId, Batch : FirestoreDataSource.FirestoreCrudBatch<T>> :
    DataSource<T, String, DocumentReference>,
    HasQueryOperations<T, QueryMapper>,
    HasBatchOperations<Batch>,
    Countable<Long> {
    /** Retrieves the source [CollectionReference] for this data-source. */
    suspend fun getCollectionRef(): CollectionReference
}

/**
 * Generic Firestore-backed [DataSource] with a default [CrudBatch] type.
 * @see IFirestoreDataSource
 */
sealed interface IDefaultFirestoreDataSource<T : HasId> :
    IFirestoreDataSource<T, FirestoreDataSource.FirestoreCrudBatch<T>>
