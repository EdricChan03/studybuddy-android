package com.edricchan.studybuddy.features.tasks.data.repo

import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepository @Inject constructor(
    firestore: FirebaseFirestore,
    userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>,
) {
    private val userId = userFlow.mapNotNull { it?.uid }
    private val taskCollectionRef = userId.map { firestore.collection("/users/$it/todos") }

    private suspend fun getCollectionRef() = taskCollectionRef.first()

    /** Retrieves the user's list of tasks. */
    suspend fun getTasks() = getCollectionRef().get().await().toObjects<TodoItem>()

    /**
     * Retrieves the user's list of tasks.
     *
     * This method returns a [com.google.android.gms.tasks.Task] for backwards compatibility.
     */
    @Deprecated("This method is kept for backwards compatibility. Use getTasks instead")
    fun getTasksCompat() = runBlocking(Dispatchers.Default) { getCollectionRef() }

    /** Retrieves the user's list of tasks as a [kotlinx.coroutines.flow.Flow] of updates. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksFlow = taskCollectionRef.flatMapLatest { it.dataObjects<TodoItem>() }

    /** Retrieves the user's list of tasks given the specified [query]. */
    suspend fun queryTasks(query: QueryMapper) =
        query(getCollectionRef()).get().await().toObjects<TodoItem>()

    /**
     * Retrieves the user's list of tasks given the specified [query] as a
     * [kotlinx.coroutines.flow.Flow].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeQueryTasks(query: QueryMapper) =
        taskCollectionRef.flatMapLatest { query(it).dataObjects<TodoItem>() }

    /** Retrieves the task given its [id]. */
    suspend fun getTask(id: String) =
        getCollectionRef().document(id).get().await().toObject<TodoItem>()

    /** Retrieves the task given its [id] as a [com.google.firebase.firestore.DocumentReference] */
    suspend fun getTaskDocument(id: String) = getCollectionRef().document(id)

    /** Retrieves the task given its [id] as a [com.google.firebase.firestore.DocumentReference] */
    @Deprecated("This method is kept for backwards compatibility. Use getTaskDocument instead")
    fun getTaskDocumentCompat(id: String) = runBlocking(Dispatchers.Default) { getTaskDocument(id) }

    /** Retrieves the task given its [id] as a [kotlinx.coroutines.flow.Flow]. */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeTask(id: String) = taskCollectionRef.flatMapLatest {
        it.document(id).dataObjects<TodoItem>()
    }

    /** Adds the specified [task]. */
    suspend fun addTask(task: TodoItem) = getCollectionRef().add(task).await()

    /**
     * Adds the specified [task].
     *
     * This method returns a [com.google.android.gms.tasks.Task] for backwards compatibility.
     */
    @Deprecated("This method is kept for backwards compatibility. Use addTask instead")
    fun addTaskCompat(task: TodoItem) =
        runBlocking(Dispatchers.Default) { getCollectionRef().add(task) }

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
    fun removeTaskCompat(id: String) =
        runBlocking(Dispatchers.Default) { getTaskDocument(id).delete() }

    /** Update the task (given its [id]) with the specified [data]. */
    suspend fun updateTask(id: String, data: Map<String, Any>) {
        getTaskDocument(id).update(data).await()
    }

    /** Updates the list of tasks (given by [ids]) with the specified [data]. */
    suspend fun updateTasks(ids: Set<String>, data: Map<String, Any>) {
        val collectionRef = getCollectionRef()
        val docs = ids.map(collectionRef::document)

        firestore.runBatch { batch ->
            docs.forEach {
                batch.update(it, data)
            }
        }.await()
    }

    /** Updates the task given its [id] with the [dataAction] to be passed to [buildMap]. */
    suspend fun updateTask(id: String, dataAction: MutableMap<String, Any>.() -> Unit) =
        updateTask(id, buildMap(dataAction))
}

suspend fun TaskRepository.update(
    id: String,
    data: Map<TodoItem.Field, Any>
) {
    updateTask(id, data.mapKeys { it.key.fieldName })
}

suspend fun TaskRepository.update(
    id: String,
    dataAction: MutableMap<TodoItem.Field, Any>.() -> Unit
) {
    updateTask(id, buildMap(dataAction).mapKeys { it.key.fieldName })
}

/** Toggles the [item]'s [completion status][TodoItem.done]. */
suspend fun TaskRepository.toggleCompleted(item: TodoItem) =
    updateTask(item.id, mapOf(TodoItem.Field.IsDone.fieldName to !(item.done ?: false)))

/** Toggles the [item]'s [archival status][TodoItem.archived]. */
suspend fun TaskRepository.toggleArchived(item: TodoItem) =
    updateTask(item.id, mapOf(TodoItem.Field.IsArchived.fieldName to !(item.archived ?: false)))
