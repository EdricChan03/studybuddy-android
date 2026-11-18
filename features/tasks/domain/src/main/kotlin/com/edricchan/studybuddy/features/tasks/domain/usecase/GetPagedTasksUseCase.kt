package com.edricchan.studybuddy.features.tasks.domain.usecase

import androidx.paging.PagingData
import com.edricchan.studybuddy.core.settings.tasks.repo.TasksSettingsRepository
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.toDomain
import com.edricchan.studybuddy.features.tasks.domain.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.domain.repo.TasksPaginationConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class GetPagedTasksUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val settingsRepo: TasksSettingsRepository
) {
    operator fun invoke(
        cachedCoroutineScope: CoroutineScope
    ): Flow<PagingData<TaskItem>> = settingsRepo.filterOptions.flatMapLatest {
        val ordering = it.orderByFields.mapKeysTo(LinkedHashMap()) { (field, dir) ->
            field.protoValue.toDomain()
        }

        // TODO: Hook up ordering support when we migrate off of Firestore. (Firestore requires a
        //   composite index to be added for *every* new sort-direction order that would be added
        //   to a query)
        repository.observeTasks(
            TasksPaginationConfig(
                cachedCoroutineScope = cachedCoroutineScope
            )
        )
    }
}
