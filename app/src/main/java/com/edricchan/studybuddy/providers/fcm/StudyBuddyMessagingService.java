package com.edricchan.studybuddy.providers.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class StudyBuddyMessagingService extends FirebaseMessagingService {
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		if (remoteMessage != null) {
			if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {

			}
			System.out.println("Firebase Messaging (Data): " + remoteMessage.getData().toString());
			System.out.println("Firebase Messaging (Notification): " + remoteMessage.getNotification().toString());
		} else {
			System.out.println("Oops! No remote message! :(");
		}
	}
}
