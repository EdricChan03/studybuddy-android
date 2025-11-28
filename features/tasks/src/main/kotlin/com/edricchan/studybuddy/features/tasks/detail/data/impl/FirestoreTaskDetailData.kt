package com.edricchan.studybuddy.features.tasks.detail.data.impl

import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.data.repo.TaskProjectDataSource
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.detail.data.TaskDetailData
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FirestoreTaskDetailData @AssistedInject constructor(
    @Assisted private val selectedTaskId: String,
    @Assisted private val coroutineScope: CoroutineScope,
    repo: TaskRepository,
    private val projectSource: TaskProjectDataSource
) : TaskDetailData {
    @AssistedFactory
    fun interface Factory {
        fun create(selectedTaskId: String, coroutineScope: CoroutineScope): FirestoreTaskDetailData
    }

    override val currentTaskId: String = selectedTaskId

    @Deprecated(
        "There is an ambiguity between having no such task item " +
            "existing (represented by `null`) and the initial `null` value when the underlying " +
            "flow has yet to start emitting values. Use the currentTaskStateFlow property " +
            "instead which uses a sealed interface to differentiate between these 2 possible " +
            "states. Note that the state class uses the domain-specific TaskItem data class " +
            "which also provides access to the project-related information, if any."
    )
    override val currTaskFlow: StateFlow<TodoItem?> = repo.observeTask(selectedTaskId)
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

    override val currentTaskStateFlow: StateFlow<TaskDetailState> =
        repo.observeTaskById(selectedTaskId)
            .map {
                if (it == null) return@map TaskDetailState.NoData
                TaskDetailState.Success(it)
            }
            .catch { emit(TaskDetailState.Error(it)) }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = TaskDetailState.Loading
            )

    @Suppress("DEPRECATION")
    @Deprecated(
        "The project data is now available in the TaskItem domain class. " +
            "To get the current task data, use the currentTaskStateFlow property"
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    override val currTaskProjectFlow: StateFlow<TodoProject?> = currTaskFlow
        .flatMapConcat { item ->
            item?.project?.id?.let(projectSource::get) ?: flowOf(null)
        }
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)
}
