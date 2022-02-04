package com.edricchan.studybuddy.data.firestore

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

/** Function to map the given [CollectionReference] to a [Query]. */
typealias CollectionToQueryMapper = (CollectionReference) -> Query
