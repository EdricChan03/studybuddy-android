package com.edricchan.studybuddy.data.repo.firestore

import com.edricchan.studybuddy.data.common.HasId
import com.google.firebase.firestore.CollectionReference
import kotlin.reflect.KClass

/**
 * Implementation of [FirestoreDataSource] which does not require a `Batch` to be specified.
 * @see FirestoreDataSource
 */
open class DefaultFirestoreDataSource<T : HasId>(
    collectionRef: CollectionReference,
    klass: KClass<T>
) : FirestoreDataSource<T, FirestoreDataSource.FirestoreCrudBatch<T>>(
    collectionRef = collectionRef,
    klass = klass,
    batchFactory = {
        FirestoreCrudBatch(collectionRef = it)
    }
)
