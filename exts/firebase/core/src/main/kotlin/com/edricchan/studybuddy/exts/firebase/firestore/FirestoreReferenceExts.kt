package com.edricchan.studybuddy.exts.firebase.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

/**
 * Reads the document referenced by this [DocumentReference].
 *
 * By default, `get()` attempts to provide up-to-date data when possible
 * by waiting for data from the server, but it may return cached data
 * or fail if you are offline and the server cannot be reached.
 * This behavior can be altered via the [source] parameter.
 *
 * This method is equivalent to calling `get(source).await()`.
 * @param source A value to configure the get behavior.
 * @return The contents of the Document at this [DocumentReference]
 * @see DocumentReference.get
 */
suspend fun DocumentReference.getAsync(
    source: Source = Source.DEFAULT
): DocumentSnapshot = get(source).await()

/**
 * Reads the document referenced by this [DocumentReference].
 *
 * By default, `get()` attempts to provide up-to-date data when possible
 * by waiting for data from the server, but it may return cached data
 * or fail if you are offline and the server cannot be reached.
 * This behavior can be altered via the [source] parameter.
 *
 * This method is equivalent to calling `get(source).await()`.
 * @param source A value to configure the get behavior.
 * @return The contents of the Document at this [DocumentReference]
 * @see DocumentReference.get
 */
suspend fun CollectionReference.getAsync(
    source: Source = Source.DEFAULT
): QuerySnapshot = get(source).await()
