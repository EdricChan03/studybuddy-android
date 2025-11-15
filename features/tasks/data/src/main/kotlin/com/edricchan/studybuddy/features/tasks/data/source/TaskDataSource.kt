package com.edricchan.studybuddy.features.tasks.data.source

import com.edricchan.studybuddy.data.source.firestore.DefaultFlowableFirestoreDataSource
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class TaskDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>,
) : DefaultFlowableFirestoreDataSource<TodoItem>(
    collectionRefFlow = userFlow
        .mapNotNull { user ->
            user?.let { firestore.collection("/users/${it.uid}/todos") }
        },
    klass = TodoItem::class
)
