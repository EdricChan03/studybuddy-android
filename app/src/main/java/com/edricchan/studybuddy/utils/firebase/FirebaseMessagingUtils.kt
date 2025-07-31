package com.edricchan.studybuddy.utils.firebase

import com.edricchan.studybuddy.interfaces.NotificationRequest
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * A newer implementation of the former `sendNotificationToUser` method.
 *
 * This implementation reinforces that notifications are sent as requests to Cloud Firestore
 * (which gets saved as a document under the `notificationRequests` collection) and are
 * automatically sent to the associated topic or username.
 *
 * @param fs An instance of [FirebaseFirestore].
 * @return A reference of the task
 */
fun NotificationRequest.sendToFirestore(
    fs: FirebaseFirestore = Firebase.firestore
) = fs.collection("notificationRequests").add(this)

/**
 * A newer implementation of the former `sendNotificationToUser` method.
 *
 * This implementation reinforces that notifications are sent as requests to Cloud Firestore
 * (which gets saved as a document under the `notificationRequests` collection) and are
 * automatically sent to the associated topic or username.
 *
 * This variant allows for Kotlin Coroutines to be used.
 *
 * @param fs An instance of [FirebaseFirestore].
 * @return The created document reference
 */
suspend fun NotificationRequest.sendToFirestoreAsync(
    fs: FirebaseFirestore = Firebase.firestore
) =
    sendToFirestore(fs).await()
