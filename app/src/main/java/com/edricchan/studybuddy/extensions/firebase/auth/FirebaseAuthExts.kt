package com.edricchan.studybuddy.extensions.firebase.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Retrieves the user's Firestore document
 * @param fs An instance of [FirebaseFirestore] (TIP: Use [FirebaseFirestore.getInstance] to retrieve an instance)
 * @return The user's Firestore document
 */
fun FirebaseUser?.getUserDocument(fs: FirebaseFirestore) = fs.document("users/${this?.uid}")