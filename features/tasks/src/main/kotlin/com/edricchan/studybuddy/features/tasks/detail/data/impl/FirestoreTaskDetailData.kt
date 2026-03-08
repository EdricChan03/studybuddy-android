package com.edricchan.studybuddy.features.tasks.detail.data.impl

import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.detail.data.TaskDetailData
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FirestoreTaskDetailData @AssistedInject constructor(
    @Assisted private val selectedTaskId: String,
    @Assisted private val coroutineScope: CoroutineScope,
    repo: TaskRepository
) : TaskDetailData {
    @AssistedFactory
    fun interface Factory {
        fun create(selectedTaskId: String, coroutineScope: CoroutineScope): FirestoreTaskDetailData
    }

    override val currentTaskId: String = selectedTaskId

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
}
