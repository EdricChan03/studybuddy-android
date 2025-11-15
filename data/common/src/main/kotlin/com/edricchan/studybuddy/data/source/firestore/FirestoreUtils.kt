package com.edricchan.studybuddy.data.source.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlin.reflect.KClass

internal fun <T : Any> QuerySnapshot.toObjects(klass: KClass<T>): List<T> = toObjects(klass.java)
internal fun <T : Any> DocumentSnapshot.toObject(klass: KClass<T>) = toObject(klass.java)
