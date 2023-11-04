package com.edricchan.studybuddy.ui.modules.task.utils

import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.modules.task.data.TaskRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flowOf

/**
 * Utility class for the task module.
 * @param user The currently logged-in [user][FirebaseUser].
 * @param fs An instance of [FirebaseFirestore].
 */
@Deprecated("Use TaskRepository instead")
class TodoUtils(
    private val user: FirebaseUser?,
    private val fs: FirebaseFirestore
) {
    constructor(auth: FirebaseAuth, fs: FirebaseFirestore) : this(auth.currentUser, fs)

    private val repository =
        TaskRepository(userFlow = flowOf(user), firestore = fs)
    private val taskCollectionPath = "users/${user?.uid}/todos"

    /**
     * A [collection reference][com.google.firebase.firestore.CollectionReference] to the user's
     * task collection.
     */
    @Deprecated(
        "Use the appropriate TaskRepository methods for performing CRUD operations"
    )
    val taskCollectionRef = fs.collection(taskCollectionPath)

    /**
     * A [Task] object that maps to the user's task items.
     */
    @Deprecated(
        "Use TaskRepository#getTasksCompat instead",
        ReplaceWith(
            "TaskRepository(user, fs).getTasksCompat(item)",
            "com.edricchan.studybuddy.ui.modules.task.data.TaskRepository"
        )
    )
    val taskCollectionQuerySnapshot = taskCollectionRef.get()

    /**
     * Adds a new task to the [Firestore collection of tasks][taskCollectionRef].
     * @param item The task item to add
     * @return The result.
     */
    @Deprecated(
        "Use TaskRepository#addTask instead",
        ReplaceWith(
            "TaskRepository(user, fs).addTaskCompat(item)",
            "com.edricchan.studybuddy.ui.modules.task.data.TaskRepository"
        )
    )
    fun addTask(item: TodoItem) = repository.addTaskCompat(item)

    /**
     * Retrieves the document which is uniquely identified by its [docId].
     * @param docId The document ID.
     * @return A document reference of the specified document.
     */
    @Deprecated(
        "Use TaskRepository#getTaskDocument instead",
        ReplaceWith(
            "TaskRepository(user, fs).getTaskDocument(item)",
            "com.edricchan.studybuddy.ui.modules.task.data.TaskRepository"
        )
    )
    fun getTask(docId: String) = repository.getTaskDocumentCompat(docId)

    /**
     * Removes a task from the Firebase Firestore database
     * @param docId The document's ID
     * @return The result of the deletion
     */
    @Deprecated(
        "Use TaskRepository#removeTask instead",
        ReplaceWith(
            "TaskRepository(user, fs).removeTaskCompat(item)",
            "com.edricchan.studybuddy.ui.modules.task.data.TaskRepository"
        )
    )
    fun removeTask(docId: String) = repository.removeTaskCompat(docId)

    companion object {
        /**
         * Creates a new instance of the utility class.
         * @param user The currently logged-in [user][FirebaseUser].
         * @param fs An instance of [FirebaseFirestore].
         */
        @Deprecated(
            "Use the TodoUtils constructor directly",
            ReplaceWith(
                "TodoUtils(user, fs)"
            )
        )
        fun getInstance(user: FirebaseUser, fs: FirebaseFirestore) = TodoUtils(user, fs)

        /**
         * Creates a new instance of the utility class.
         * @param auth An instance of [FirebaseAuth].
         * @param fs An instance of [FirebaseFirestore].
         */
        @Deprecated(
            "Use the TodoUtils constructor directly",
            ReplaceWith(
                "TodoUtils(auth.currentUser, fs)"
            )
        )
        fun getInstance(auth: FirebaseAuth, fs: FirebaseFirestore) =
            TodoUtils(auth, fs)
    }
}
