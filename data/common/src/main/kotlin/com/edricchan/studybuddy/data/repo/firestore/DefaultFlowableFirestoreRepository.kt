package com.edricchan.studybuddy.data.repo.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.edricchan.studybuddy.data.repo.firestore.FirestoreRepository.FirestoreCrudBatch
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * Variant of [FlowableFirestoreRepository] with a default `Batch` being used.
 * @see FlowableFirestoreRepository
 */
open class DefaultFlowableFirestoreRepository<T : HasId>(
    collectionRefFlow: Flow<CollectionReference>,
    klass: KClass<T>
) : FlowableFirestoreRepository<T, FirestoreCrudBatch<T>>(
    collectionRefFlow = collectionRefFlow,
    klass = klass,
    batchFactory = {
        FirestoreCrudBatch(collectionRef = it)
    }
)
