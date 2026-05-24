package com.edricchan.studybuddy.core.settings.tasks.source

import com.edricchan.studybuddy.core.settings.tasks.model.TaskConfirmationOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskDefaultDataOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskFilterOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskSwipeActionOptions
import kotlinx.coroutines.flow.Flow

interface TasksSettingsDataSource {
    val filterOptions: Flow<TaskFilterOptions>
    suspend fun setFilterOptions(
        transform: suspend (TaskFilterOptions) -> TaskFilterOptions
    )

    suspend fun setFilterOptions(options: TaskFilterOptions) {
        setFilterOptions { options }
    }

    val swipeActionOptions: Flow<TaskSwipeActionOptions>
    suspend fun setSwipeActionOptions(transform: suspend (TaskSwipeActionOptions) -> TaskSwipeActionOptions)
    suspend fun setSwipeActionOptions(options: TaskSwipeActionOptions) {
        setSwipeActionOptions { options }
    }

    val confirmOptions: Flow<TaskConfirmationOptions>
    suspend fun setConfirmOptions(transform: suspend (TaskConfirmationOptions) -> TaskConfirmationOptions)
    suspend fun setConfirmOptions(options: TaskConfirmationOptions) {
        setConfirmOptions { options }
    }

    val defaultNewDataOptions: Flow<TaskDefaultDataOptions>
    suspend fun setDefaultNewDataOptions(transform: suspend (TaskDefaultDataOptions) -> TaskDefaultDataOptions)
    suspend fun setDefaultNewDataOptions(options: TaskDefaultDataOptions) {
        setDefaultNewDataOptions { options }
    }
}
