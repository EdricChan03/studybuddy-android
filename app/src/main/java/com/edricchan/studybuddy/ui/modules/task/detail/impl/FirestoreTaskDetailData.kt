package com.edricchan.studybuddy.ui.modules.task.detail.impl

import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.data.repo.TaskProjectRepository
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.ui.modules.task.detail.TaskDetailData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class FirestoreTaskDetailData @AssistedInject constructor(
    @Assisted private val selectedTaskId: String,
    @Assisted private val coroutineScope: CoroutineScope,
    private val repo: TaskRepository,
    private val projectRepo: TaskProjectRepository
) : TaskDetailData {
    @AssistedFactory
    fun interface Factory {
        fun create(selectedTaskId: String, coroutineScope: CoroutineScope): FirestoreTaskDetailData
    }

    override val currentTaskId: String = selectedTaskId

    override val currTaskFlow: StateFlow<TodoItem?> = repo.observeTask(selectedTaskId)
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val currTaskProjectFlow: StateFlow<TodoProject?> = currTaskFlow
        .flatMapConcat { item ->
            // Return a flow with null if the ID is null
            item?.project?.id?.let(projectRepo::get) ?: flowOf(null)
        }
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)
}
