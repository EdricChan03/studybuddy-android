package com.edricchan.studybuddy.domain.common.paging

import com.edricchan.studybuddy.domain.common.sorting.SortDirection
import kotlinx.coroutines.CoroutineScope

/**
 * Interface holding shared pagination options.
 * @param Field Field class to be used as the `key` generic type for [orderByFields].
 */
interface CommonPagingConfig<Field> {
    /** Desired [CoroutineScope] to cache the paging data in (see [androidx.paging.cachedIn] for more info). */
    val cachedCoroutineScope: CoroutineScope

    /** Number of items to be shown per page. */
    val pageSize: Int

    /** The set of ordering configurations to order the resulting data by, */
    // LinkedHashMap is used instead of a Map or Set (or LinkedHashSet) as it preserves the
    // insertion order and has the benefits of a Map as well
    val orderByFields: LinkedHashMap<Field, SortDirection>
}
