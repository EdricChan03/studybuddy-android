package com.edricchan.studybuddy.constants.sharedprefs

import androidx.annotation.StringDef

/**
 * Constants to be used for shared preferences regarding the task fragment's options
 */
object TaskOptionsPrefConstants {
    /**
     * File to be used for storing the information about the task fragment's options
     */
    const val FILE_TASK_OPTIONS = "task_options"
    /**
     * Indicates the default preference for how to sort tasks
     *
     * This key is stored as a [String] representation of one of the values of [TaskSortValues]
     *
     * @see TaskSortValues
     * @see TaskSort
     */
    const val PREF_DEFAULT_SORT = "default_sort"

    /**
     * The former version of [PREF_DEFAULT_SORT]
     *
     * THis key is stored as a [String] representation of one of the values of [TaskSortValues]
     *
     * NOTE: This key is kept for compatibility purposes and may be removed in a future release
     *
     * @see PREF_DEFAULT_SORT
     * @see TaskSortValues
     * @see TaskSort
     */
    const val PREF_DEFAULT_SORT_OLD = "sortTasksBy"

    /**
     * Constants for the accepted values of the [PREF_DEFAULT_SORT] key's value
     */
    object TaskSortValues {
        /**
         * Indicates that tasks should be sorted based on the preference in Settings > Todos > Default sorting mode
         */
        const val UNSET = "unset"
        /**
         * Indicates that tasks should not be sorted
         */
        const val NONE = "none"
        /**
         * Indicates that tasks should be sorted by their titles descending
         */
        const val TITLE_DESC = "title_desc"
        /**
         * Indicates that tasks should be sorted by their titles ascending
         */
        const val TITLE_ASC = "title_asc"
        /**
         * Indicates that tasks should be sorted by their due dates from new to old
         */
        const val DUE_DATE_NEW_TO_OLD = "due_date_new_to_old"
        /**
         * Indicates that tasks should be sorted by their due dates from old to new
         */
        const val DUE_DATE_OLD_TO_NEW = "due_date_old_to_new"
    }

    /**
     * Annotation for indicating the accepted values of the [PREF_DEFAULT_SORT] preference
     */
    @StringDef(
        TaskOptionsPrefConstants.TaskSortValues.TITLE_DESC,
        TaskOptionsPrefConstants.TaskSortValues.TITLE_ASC,
        TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_NEW_TO_OLD,
        TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_OLD_TO_NEW
    )
    annotation class TaskSort
}