package com.edricchan.studybuddy.core.settings.tasks.repo

import com.edricchan.studybuddy.core.settings.tasks.model.DefaultTaskOrdering
import com.edricchan.studybuddy.core.settings.tasks.model.TaskConfirmationOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskDefaultDataOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskFilterOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskOrderMap
import com.edricchan.studybuddy.core.settings.tasks.model.TaskSwipeAction
import com.edricchan.studybuddy.core.settings.tasks.model.TaskSwipeActionOptions
import com.edricchan.studybuddy.core.settings.tasks.model.field.TaskField
import com.edricchan.studybuddy.core.settings.tasks.source.TasksSettingsDataSource
import com.edricchan.studybuddy.domain.common.sorting.SortDirection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TasksSettingsRepository @Inject constructor(
    private val dataSource: TasksSettingsDataSource
) {
    val filterOptions: Flow<TaskFilterOptions> by dataSource::filterOptions

    suspend fun setFilterOptions(options: TaskFilterOptions) {
        dataSource.setFilterOptions(options)
    }

    suspend fun updateFilterOptions(transform: suspend (TaskFilterOptions) -> TaskFilterOptions) {
        dataSource.setFilterOptions(transform)
    }

    suspend fun setFilterOptions(
        orderMap: TaskOrderMap = DefaultTaskOrdering,
        includeCompleted: Boolean = false,
        includeArchived: Boolean = false
    ) {
        setFilterOptions(
            TaskFilterOptions(
                orderByFields = orderMap,
                includeCompleted = includeCompleted,
                includeArchived = includeArchived
            )
        )
    }

    suspend fun setIncludeCompletedTasks(include: Boolean) {
        dataSource.setFilterOptions {
            it.copy(
                includeCompleted = include
            )
        }
    }

    suspend fun setIncludeArchivedTasks(include: Boolean) {
        dataSource.setFilterOptions {
            it.copy(
                includeArchived = include
            )
        }
    }

    suspend fun setOrdering(orderMap: TaskOrderMap) {
        dataSource.setFilterOptions {
            it.copy(
                orderByFields = orderMap
            )
        }
    }

    suspend fun updateOrdering(
        orderMapTransform: suspend (TaskOrderMap) -> TaskOrderMap
    ) {
        dataSource.setFilterOptions {
            it.copy(orderByFields = orderMapTransform(it.orderByFields))
        }
    }

    suspend fun resetOrdering() {
        setOrdering(DefaultTaskOrdering)
    }

    val swipeActionOptions: Flow<TaskSwipeActionOptions> by dataSource::swipeActionOptions
    suspend fun setSwipeActionOptions(options: TaskSwipeActionOptions) {
        dataSource.setSwipeActionOptions(options)
    }

    suspend fun updateSwipeActionOptions(
        transform: suspend (TaskSwipeActionOptions) -> TaskSwipeActionOptions
    ) {
        dataSource.setSwipeActionOptions(transform)
    }

    suspend fun setSwipeTowardsStartAction(action: TaskSwipeAction) {
        updateSwipeActionOptions { it.copy(towardsStart = action) }
    }

    suspend fun setSwipeTowardsEndAction(action: TaskSwipeAction) {
        updateSwipeActionOptions { it.copy(towardsEnd = action) }
    }

    suspend fun setSwipeActions(towardsStart: TaskSwipeAction, towardsEnd: TaskSwipeAction) {
        updateSwipeActionOptions { it.copy(towardsStart = towardsStart, towardsEnd = towardsEnd) }
    }

    val confirmOptions: Flow<TaskConfirmationOptions> by dataSource::confirmOptions
    suspend fun setConfirmOptions(options: TaskConfirmationOptions) {
        dataSource.setConfirmOptions(options)
    }

    suspend fun updateConfirmOptions(
        transform: suspend (TaskConfirmationOptions) -> TaskConfirmationOptions
    ) {
        dataSource.setConfirmOptions(transform)
    }

    suspend fun setConfirmOptions(
        onDelete: Boolean = true,
        onArchive: Boolean = false,
        onComplete: Boolean = false
    ) {
        setConfirmOptions(
            TaskConfirmationOptions(
                onDelete = onDelete, onArchive = onArchive, onComplete = onComplete
            )
        )
    }

    val defaultNewDataOptions: Flow<TaskDefaultDataOptions> by dataSource::defaultNewDataOptions
    suspend fun setDefaultNewDataOptions(options: TaskDefaultDataOptions) {
        dataSource.setDefaultNewDataOptions(options)
    }

    suspend fun updateDefaultNewDataOptions(
        transform: suspend (TaskDefaultDataOptions) -> TaskDefaultDataOptions
    ) {
        dataSource.setDefaultNewDataOptions(transform)
    }
}

suspend inline fun TasksSettingsRepository.setOrderingEntries(
    vararg fields: Pair<TaskField, SortDirection>
) {
    setOrdering(linkedMapOf(*fields))
}

suspend inline fun TasksSettingsRepository.removeOrderingEntry(field: TaskField) {
    updateOrdering { it.apply { remove(field) } }
}

suspend inline fun TasksSettingsRepository.addOrderingEntries(
    vararg fields: Pair<TaskField, SortDirection>
) {
    updateOrdering { it.apply { putAll(fields) } }
}

suspend inline fun TasksSettingsRepository.addOrderingEntry(
    field: TaskField,
    direction: SortDirection
) {
    updateOrdering { it.apply { put(field, direction) } }
}
