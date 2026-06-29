package com.edricchan.studybuddy.features.tasks.constants.sharedprefs

import androidx.annotation.StringDef
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants.PREF_DEFAULT_SORT

/**
 * Constants to be used for shared preferences regarding the task fragment's options
 */
@Deprecated(
    "Task-related settings are now stored as a ProtoBuf message; see the " +
        "respective deprecations for more details"
)
object TodoOptionsPrefConstants {
    /**
     * File to be used for storing the information about the task fragment's options
     */
    @Deprecated(
        "Task-related settings are now stored as a ProtoBuf message; " +
            "use TasksSettingsDataSource to access the data"
    )
    const val FILE_TODO_OPTIONS = "todo_options"

    /**
     * Indicates the default preference for how to sort tasks
     *
     * This key is stored as a [String] representation of one of the values of [TodoSortValues]
     *
     * @see TodoSortValues
     * @see TodoSort
     */
    @Deprecated("This setting is now accessible via TasksFilterOptions.orderByFields")
    const val PREF_DEFAULT_SORT = "default_sort"

    /**
     * The former version of [PREF_DEFAULT_SORT]
     *
     * This key is stored as a [String] representation of one of the values of [TodoSortValues]
     *
     * NOTE: This key is kept for compatibility purposes and may be removed in a future release
     *
     * @see PREF_DEFAULT_SORT
     * @see TodoSortValues
     * @see TodoSort
     */
    @Deprecated(
        "This key is kept for compatibility purposes. " +
            "PREF_DEFAULT_SORT should be used where preferable",
        ReplaceWith(
            "TodoOptionsPrefConstants.PREF_DEFAULT_SORT",
            "com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants"
        )
    )
    const val PREF_DEFAULT_SORT_OLD = "sortTasksBy"

    /**
     * Constants for the accepted values of the [PREF_DEFAULT_SORT] key's value
     */
    @Deprecated(
        "Sorting-related settings are now stored as a map of TaskFields to their " +
            "sorting direction"
    )
    object TodoSortValues {
        /**
         * Indicates that tasks should be sorted based on the preference in Settings > Todos > Default sorting mode
         */
        @Deprecated(
            "The current sorting value should always be persisted globally, " +
                "so this serves no purpose"
        )
        const val UNSET = "unset"

        /**
         * Indicates that tasks should not be sorted
         */
        @Deprecated("Use `null` or an empty map where applicable when ordering")
        const val NONE = "none"

        /**
         * Indicates that tasks should be sorted by their titles descending
         */
        @Deprecated("Use `TaskField.Title` set to `SortDirection.Descending` when ordering")
        const val TITLE_DESC = "title_desc"

        /**
         * Indicates that tasks should be sorted by their titles ascending
         */
        @Deprecated("Use `TaskField.Title` set to `SortDirection.Ascending` when ordering")
        const val TITLE_ASC = "title_asc"

        /**
         * Indicates that tasks should be sorted by their due dates from new to old
         */
        @Deprecated("Use `TaskField.DueDate` set to `SortDirection.Descending` when ordering")
        const val DUE_DATE_NEW_TO_OLD = "due_date_new_to_old"

        /**
         * Indicates that tasks should be sorted by their due dates from old to new
         */
        @Deprecated("Use `TaskField.DueDate` set to `SortDirection.Ascending` when ordering")
        const val DUE_DATE_OLD_TO_NEW = "due_date_old_to_new"
    }

    /**
     * Annotation for indicating the accepted values of the [PREF_DEFAULT_SORT] preference
     */
    @StringDef(
        TodoSortValues.TITLE_DESC,
        TodoSortValues.TITLE_ASC,
        TodoSortValues.DUE_DATE_NEW_TO_OLD,
        TodoSortValues.DUE_DATE_OLD_TO_NEW
    )
    @Deprecated(
        "Sorting is now set as a map of `TaskField`s to `SortDirection`s rather " +
            "than a hard-coded preset string"
    )
    annotation class TodoSort
}
