package com.edricchan.studybuddy.domain.common.sorting

/**
 * Specification for a specific field to order a list of data by.
 * @property field Desired field to order the data by.
 * @property direction Desired sorting direction.
 */
open class OrderSpec<F>(
    open val field: F,
    open val direction: SortDirection = SortDirection.Descending
)

/** Desired sorting direction for a given field. */
enum class SortDirection {
    /** Sort the resulting data in descending order, i.e. largest to smallest. */
    Descending,

    /** Sort the resulting data in ascending order, i.e. smallest to largest. */
    Ascending
}
