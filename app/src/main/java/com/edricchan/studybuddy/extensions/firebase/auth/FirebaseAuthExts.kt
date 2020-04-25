package com.edricchan.studybuddy.extensions.firebase.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Retrieves the user's Firestore document
 * @param fs An instance of [FirebaseFirestore]
 * @return The user's Firestore document
 */
fun FirebaseUser?.getUserDocument(fs: FirebaseFirestore) =
    fs.document("users/${this?.uid}")

/**
 * Retrieves the user's Firestore document.
 */
val FirebaseUser?.userDocument: DocumentReference
    get() = this.getUserDocument(Firebase.firestore)