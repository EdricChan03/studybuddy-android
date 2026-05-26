package com.edricchan.studybuddy.domain.common.sorting

import com.google.firebase.firestore.Query

/** Desired sorting direction for a given field. */
enum class SortDirection {
    /** Sort the resulting data in descending order, i.e. largest to smallest. */
    Descending,

    /** Sort the resulting data in ascending order, i.e. smallest to largest. */
    Ascending
}

/** Converts the receiver [SortDirection] to its Firebase [Query.Direction] equivalent. */
fun SortDirection.toFirestoreDirection(): Query.Direction = when (this) {
    SortDirection.Descending -> Query.Direction.DESCENDING
    SortDirection.Ascending -> Query.Direction.ASCENDING
}
