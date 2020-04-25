package com.edricchan.studybuddy.ui.modules.task.utils

import com.edricchan.studybuddy.interfaces.TodoItem
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Utility class for the task module.
 * @param user The currently logged-in [user][FirebaseUser].
 * @param fs An instance of [FirebaseFirestore].
 */
class TodoUtils(
    private val user: FirebaseUser?,
    private val fs: FirebaseFirestore
) {
    private val taskCollectionPath = "users/${user?.uid}/todos"

    /**
     * A [collection reference][com.google.firebase.firestore.CollectionReference] to the user's
     * task collection.
     */
    val taskCollectionRef = fs.collection(taskCollectionPath)

    /**
     * A [Task] object that maps to the user's task items.
     */
    val taskCollectionQuerySnapshot = taskCollectionRef.get()

    /**
     * Adds a new task to the [Firestore collection of tasks][taskCollectionRef].
     * @param item The task item to add
     * @return The result.
     */
    fun addTask(item: TodoItem): Task<DocumentReference> {
        return taskCollectionRef.add(item)
    }

    /**
     * Retrieves the document which is uniquely identified by its [docId].
     * @param docId The document ID.
     * @return A document reference of the specified document.
     */
    fun getTask(docId: String): DocumentReference {
        return taskCollectionRef.document(docId)
    }

    /**
     * Removes a task from the Firebase Firestore database
     * @param docId The document's ID
     * @return The result of the deletion
     */
    fun removeTask(docId: String): Task<Void> {
        return taskCollectionRef.document(docId).delete()
    }

    companion object {
        /**
         * Creates a new instance of the utility class.
         * @param user The currently logged-in [user][FirebaseUser].
         * @param fs An instance of [FirebaseFirestore].
         */
        fun getInstance(user: FirebaseUser, fs: FirebaseFirestore) = TodoUtils(user, fs)

        /**
         * Creates a new instance of the utility class.
         * @param auth An instance of [FirebaseAuth].
         * @param fs An instance of [FirebaseFirestore].
         */
        fun getInstance(auth: FirebaseAuth, fs: FirebaseFirestore) =
            TodoUtils(auth.currentUser, fs)
    }
}