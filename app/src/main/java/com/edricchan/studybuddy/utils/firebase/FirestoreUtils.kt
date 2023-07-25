package com.edricchan.studybuddy.utils.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

typealias QueryMapper = (CollectionReference) -> Query
