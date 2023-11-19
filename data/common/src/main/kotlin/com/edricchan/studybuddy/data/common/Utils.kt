package com.edricchan.studybuddy.data.common

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

/** Mapping function used to convert the [CollectionReference] parameter to a [Query]. */
typealias QueryMapper = (CollectionReference) -> Query
