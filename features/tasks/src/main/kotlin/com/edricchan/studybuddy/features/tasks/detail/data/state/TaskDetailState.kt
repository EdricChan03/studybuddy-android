package com.edricchan.studybuddy.features.tasks.detail.data.state

import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem

sealed interface TaskDetailState {
    data object Loading : TaskDetailState
    data object NoData : TaskDetailState
    data class Success(val item: TaskItem) : TaskDetailState
    data class Error(val error: Throwable) : TaskDetailState
}
