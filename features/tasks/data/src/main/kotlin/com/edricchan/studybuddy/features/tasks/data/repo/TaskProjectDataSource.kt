package com.edricchan.studybuddy.features.tasks.data.repo

import com.edricchan.studybuddy.data.source.firestore.DefaultFlowableFirestoreDataSource
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class TaskProjectDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>
) : DefaultFlowableFirestoreDataSource<TodoProject>(
    collectionRefFlow = userFlow
        .mapNotNull { user ->
            user?.let { firestore.collection("/users/${it.uid}/todoProjects") }
        },
    klass = TodoProject::class
)
