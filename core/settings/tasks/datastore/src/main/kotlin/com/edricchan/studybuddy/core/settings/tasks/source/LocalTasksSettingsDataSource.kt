package com.edricchan.studybuddy.core.settings.tasks.source

import androidx.datastore.core.DataStore
import com.edricchan.studybuddy.core.settings.tasks.model.TaskConfirmationOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskDefaultDataOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskFilterOptions
import com.edricchan.studybuddy.core.settings.tasks.model.TaskSwipeActionOptions
import com.edricchan.studybuddy.core.settings.tasks.model.toDomain
import com.edricchan.studybuddy.core.settings.tasks.model.toProto
import com.edricchan.studybuddy.core.settings.tasks.proto.TasksSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalTasksSettingsDataSource @Inject constructor(
    private val dataStore: DataStore<@JvmSuppressWildcards TasksSettings>
) : TasksSettingsDataSource {
    override val filterOptions: Flow<TaskFilterOptions> = dataStore.data.map {
        it.filter_options?.toDomain() ?: TaskFilterOptions()
    }

    override suspend fun setFilterOptions(transform: suspend (TaskFilterOptions) -> TaskFilterOptions) {
        dataStore.updateData { settings ->
            val filterOptions = settings.filter_options?.toDomain() ?: TaskFilterOptions()
            settings.copy(
                filter_options = transform(filterOptions).toProto()
            )
        }
    }

    override suspend fun setFilterOptions(options: TaskFilterOptions) {
        dataStore.updateData {
            it.copy(
                filter_options = options.toProto()
            )
        }
    }

    override val swipeActionOptions: Flow<TaskSwipeActionOptions> = dataStore.data.map {
        it.swipe_action_options?.toDomain() ?: TaskSwipeActionOptions()
    }

    override suspend fun setSwipeActionOptions(transform: suspend (TaskSwipeActionOptions) -> TaskSwipeActionOptions) {
        dataStore.updateData { settings ->
            val swipeActionOptions =
                settings.swipe_action_options?.toDomain() ?: TaskSwipeActionOptions()
            settings.copy(
                swipe_action_options = transform(swipeActionOptions).toProto()
            )
        }
    }

    override suspend fun setSwipeActionOptions(options: TaskSwipeActionOptions) {
        dataStore.updateData {
            it.copy(
                swipe_action_options = options.toProto()
            )
        }
    }

    override val confirmOptions: Flow<TaskConfirmationOptions> = dataStore.data.map {
        it.confirm_options?.toDomain() ?: TaskConfirmationOptions()
    }

    override suspend fun setConfirmOptions(transform: suspend (TaskConfirmationOptions) -> TaskConfirmationOptions) {
        dataStore.updateData { settings ->
            val confirmOptions = settings.confirm_options?.toDomain() ?: TaskConfirmationOptions()
            settings.copy(
                confirm_options = transform(confirmOptions).toProto()
            )
        }
    }

    override suspend fun setConfirmOptions(options: TaskConfirmationOptions) {
        dataStore.updateData {
            it.copy(
                confirm_options = options.toProto()
            )
        }
    }

    override val defaultNewDataOptions: Flow<TaskDefaultDataOptions> = dataStore.data.map {
        it.default_new_task_data?.toDomain() ?: TaskDefaultDataOptions()
    }

    override suspend fun setDefaultNewDataOptions(transform: suspend (TaskDefaultDataOptions) -> TaskDefaultDataOptions) {
        dataStore.updateData { settings ->
            val defaultNewData =
                settings.default_new_task_data?.toDomain() ?: TaskDefaultDataOptions()
            settings.copy(
                default_new_task_data = transform(defaultNewData).toProto()
            )
        }
    }

    override suspend fun setDefaultNewDataOptions(options: TaskDefaultDataOptions) {
        dataStore.updateData {
            it.copy(
                default_new_task_data = options.toProto()
            )
        }
    }
}
