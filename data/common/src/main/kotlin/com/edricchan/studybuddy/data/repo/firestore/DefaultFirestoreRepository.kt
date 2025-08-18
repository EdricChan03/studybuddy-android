package com.edricchan.studybuddy.data.repo.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.google.firebase.firestore.CollectionReference
import kotlin.reflect.KClass

/**
 * Implementation of [FirestoreRepository] which does not require a `Batch` to be specified.
 * @see FirestoreRepository
 */
open class DefaultFirestoreRepository<T : HasId>(
    collectionRef: CollectionReference,
    klass: KClass<T>
) : FirestoreRepository<T, FirestoreRepository.FirestoreCrudBatch<T>>(
    collectionRef = collectionRef,
    klass = klass,
    batchFactory = {
        FirestoreCrudBatch(collectionRef = it)
    }
)
