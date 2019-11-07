package com.edricchan.studybuddy.ui.modules.task.utils

import com.edricchan.studybuddy.interfaces.TaskItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Utility class for the task module.
 * @param user The currently logged-in user.
 * @param fs An instance of [FirebaseFirestore].
 */
class TaskUtils(
		val user: FirebaseUser?,
		val fs: FirebaseFirestore
) {
	private val taskCollectionPath = "users/${user?.uid}/todos"

	/**
	 * A reference to the user's task collection.
	 */
	val taskCollectionRef = fs.collection(taskCollectionPath)

	/**
	 * A [Task] object that maps to the user's task items.
	 */
	val taskCollectionQuerySnapshot = taskCollectionRef.get()

	/**
	 * Adds a new task to the Firebase Firestore database
	 *
	 * @param item The task item to add
	 * @param user The currently authenticated user
	 * @param fs   An instance of [FirebaseFirestore]
	 * @return The result.
	 */
	fun addTask(item: TaskItem): Task<DocumentReference> {
		return taskCollectionRef.add(item)
	}

	/**
	 * Removes a task from the Firebase Firestore database
	 *
	 * @param docId The document's ID
	 * @return The result of the deletion
	 */
	fun removeTask(docId: String): Task<Void> {
		return taskCollectionRef.document(docId).delete()
	}

	companion object {
		/**
		 * Creates a new instance of the utility class.
		 * @param user The currently logged-in user.
		 * @param fs An instance of [FirebaseFirestore].
		 */
		fun getInstance(user: FirebaseUser, fs: FirebaseFirestore) = TaskUtils(user, fs)

		/**
		 * Creates a new instance of the utility class.
		 * @param auth An instance of [FirebaseAuth]
		 * @param fs An instance of [FirebaseFirestore].
		 */
		fun getInstance(auth: FirebaseAuth, fs: FirebaseFirestore) =
				TaskUtils(auth.currentUser, fs)
	}
}