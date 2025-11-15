package com.edricchan.studybuddy.data.repo.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.repo.firestore.FirestoreDataSource.FirestoreCrudBatch
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * Variant of [FlowableFirestoreDataSource] with a default `Batch` being used.
 * @see FlowableFirestoreDataSource
 */
open class DefaultFlowableFirestoreDataSource<T : HasId>(
    collectionRefFlow: Flow<CollectionReference>,
    klass: KClass<T>
) : FlowableFirestoreDataSource<T, FirestoreCrudBatch<T>>(
    collectionRefFlow = collectionRefFlow,
    klass = klass,
    batchFactory = {
        FirestoreCrudBatch(collectionRef = it)
    }
)
