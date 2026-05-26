package com.edricchan.studybuddy.features.tasks.domain.repo.project

import com.edricchan.studybuddy.domain.common.paging.CommonPagingConfig
import com.edricchan.studybuddy.domain.common.sorting.SortDirection
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import kotlinx.coroutines.CoroutineScope

/**
 * Configuration options for [TaskProjectRepository.observeProjects].
 * @property cachedCoroutineScope Desired [CoroutineScope] to cache the paging data in
 * (see [androidx.paging.cachedIn] for more info).
 * @property pageSize Number of projects to be shown per page.
 * @property orderByFields The set of ordering configurations to order the resulting
 * data by, applied sequentially in-order.
 */
data class TaskProjectsPaginationConfig(
    override val cachedCoroutineScope: CoroutineScope,
    override val pageSize: Int = 30,
    override val orderByFields: LinkedHashMap<TaskProject.Field, SortDirection> = linkedMapOf(
        TaskProject.Field.CreatedAt to SortDirection.Descending
    )
) : CommonPagingConfig<TaskProject.Field>
