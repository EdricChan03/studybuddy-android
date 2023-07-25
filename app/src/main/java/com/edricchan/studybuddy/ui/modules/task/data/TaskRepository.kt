package com.edricchan.studybuddy.ui.modules.task.data

import com.edricchan.studybuddy.interfaces.TodoItem
import com.edricchan.studybuddy.utils.firebase.QueryMapper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class TaskRepository(
    firestore: FirebaseFirestore,
    user: FirebaseUser,
) {
    private val userId = user.uid
    private val taskCollectionRef = firestore.collection("/users/$userId/todos")

    /** Retrieves the user's list of tasks. */
    suspend fun getTasks() = taskCollectionRef.get().await().toObjects<TodoItem>()

    /**
     * Retrieves the user's list of tasks.
     *
     * This method returns a [com.google.android.gms.tasks.Task] for backwards compatibility.
     */
    @Deprecated("This method is kept for backwards compatibility. Use getTasks instead")
    fun getTasksCompat() = taskCollectionRef.get()

    /** Retrieves the user's list of tasks as a [kotlinx.coroutines.flow.Flow] of updates. */
    val tasksFlow = taskCollectionRef.dataObjects<TodoItem>()

    /** Retrieves the user's list of tasks given the specified [query]. */
    suspend fun queryTasks(query: QueryMapper) =
        query(taskCollectionRef).get().await().toObjects<TodoItem>()

    /**
     * Retrieves the user's list of tasks given the specified [query] as a
     * [kotlinx.coroutines.flow.Flow].
     */
    fun observeQueryTasks(query: QueryMapper) =
        query(taskCollectionRef).dataObjects<TodoItem>()

    /** Retrieves the task given its [id]. */
    suspend fun getTask(id: String) =
        taskCollectionRef.document(id).get().await().toObject<TodoItem>()

    /** Retrieves the task given its [id] as a [com.google.firebase.firestore.DocumentReference] */
    fun getTaskDocument(id: String) = taskCollectionRef.document(id)

    /** Retrieves the task given its [id] as a [kotlinx.coroutines.flow.Flow]. */
    fun observeTask(id: String) = taskCollectionRef.document(id).dataObjects<TodoItem>()

    /** Adds the specified [task]. */
    suspend fun addTask(task: TodoItem) = taskCollectionRef.add(task).await()

    /**
     * Adds the specified [task].
     *
     * This method returns a [com.google.android.gms.tasks.Task] for backwards compatibility.
     */
    @Deprecated("This method is kept for backwards compatibility. Use addTask instead")
    fun addTaskCompat(task: TodoItem) = taskCollectionRef.add(task)

    /** Removes the specified [task]. */
    suspend fun removeTask(task: TodoItem) = removeTask(task.id)

    /** Removes the specified task given its [id]. */
    suspend fun removeTask(id: String) {
        getTaskDocument(id).delete().await()
    }

    /**
     * Removes the specified task given its [id].
     *
     * This method returns a [com.google.android.gms.tasks.Task] for backwards compatibility.
     */
    @Deprecated("This method is kept for backwards compatibility. Use removeTask instead")
    fun removeTaskCompat(id: String) = getTaskDocument(id).delete()

    /** Update the task (given its [id]) with the specified [data]. */
    suspend fun updateTask(id: String, data: Map<String, Any>) {
        getTaskDocument(id).update(data).await()
    }
}
