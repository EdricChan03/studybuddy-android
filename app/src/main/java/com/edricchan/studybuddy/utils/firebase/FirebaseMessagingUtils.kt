package com.edricchan.studybuddy.utils.firebase

import com.edricchan.studybuddy.interfaces.NotificationRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseMessagingUtils {
	companion object {
		/**
		 * A newer implementation of the former `sendNotificationToUser` method.
		 *
		 * This implementation reinforces that notifications are sent as requests to Cloud Firestore
		 * (which gets saved as a document under the `notificationRequests` collection) and are
		 * automatically sent to the associated topic or username.
		 *
		 * This implementation also uses only 1 parameter to save on the amount of characters
		 * required to call the former method.
		 *
		 * @param request The notification request to send to Cloud Firestore
		 * @return A reference of the task
		 */
		fun sendNotificationRequest(request: NotificationRequest): Task<DocumentReference> {
			val fs = Firebase.firestore
			return fs.collection("notificationRequests").add(request)
		}
	}
}