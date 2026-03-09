package com.edricchan.studybuddy.domain.common.paging

import com.edricchan.studybuddy.domain.common.sorting.OrderSpec
import kotlinx.coroutines.CoroutineScope

/**
 * Interface holding shared pagination options.
 * @param OS Desired subclass [OrderSpec] generic for [orderByFields].
 */
interface CommonPagingConfig<OS : OrderSpec<*>> {
    /** Desired [CoroutineScope] to cache the paging data in (see [androidx.paging.cachedIn] for more info). */
    val cachedCoroutineScope: CoroutineScope

    /** Number of items to be shown per page. */
    val pageSize: Int

    /** The set of [OS] configurations to order the resulting data by, */
    val orderByFields: Set<OS>
}
