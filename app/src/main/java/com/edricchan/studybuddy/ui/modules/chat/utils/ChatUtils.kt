package com.edricchan.studybuddy.ui.modules.chat.utils

import com.edricchan.studybuddy.extensions.firebase.auth.getUserDocument
import com.edricchan.studybuddy.interfaces.chat.Chat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Utility class for the chat module.
 * @param user The currently logged-in user.
 * @param fs An instance of [FirebaseFirestore].
 */
class ChatUtils(
		val user: FirebaseUser?,
		val fs: FirebaseFirestore
) {
	private val chatCollectionPath = "chats"

	/**
	 * A reference to the chat collection.
	 */
	val chatCollectionRef = fs.collection(chatCollectionPath)

	/**
	 * A [Task] object that maps to the full list of available chats.
	 *
	 * Note: If you instead want to retrieve a list of public chats, consider using the
	 * [chatCollectionRef] variable and query accordingly.
	 */
	val chatCollectionAll = chatCollectionRef.get()

	/**
	 * A query of the list of public chats.
	 */
	val chatCollectionPublicQuery = chatCollectionRef.whereEqualTo("visibility", "public")

	/**
	 * A query of the list of chats that have been joined by the currently logged-in user.
	 */
	val chatCollectionJoinedQuery = chatCollectionRef.whereArrayContains("members",
			user.getUserDocument(fs))

	/**
	 * Adds a new chat group to the collection.
	 *
	 * @param item The chat group to add.
	 * @return The added chat group.
	 */
	fun addChatGroup(item: Chat): Task<DocumentReference> {
		return chatCollectionRef.add(item)
	}

	/**
	 * Destructively deletes the specified chat group (denoted by [docId]).
	 * Note: The UI should ideally indicate that this is a destructive action and/or validate
	 * that the current user has the privileges to delete the chat group.
	 * @param docId The ID of the chat group to be deleted.
	 */
	fun deleteChatGroup(docId: String): Task<Void> {
		return chatCollectionRef.document(docId).delete()
	}

	companion object {
		/**
		 * Creates a new instance of the utility class.
		 * @param user The currently logged-in user.
		 * @param fs An instance of [FirebaseFirestore].
		 */
		fun getInstance(user: FirebaseUser?, fs: FirebaseFirestore) = ChatUtils(user, fs)

		/**
		 * Creates a new instance of the utility class.
		 * @param auth An instance of [FirebaseAuth]
		 * @param fs An instance of [FirebaseFirestore].
		 */
		fun getInstance(auth: FirebaseAuth, fs: FirebaseFirestore) =
				ChatUtils(auth.currentUser, fs)
	}
}