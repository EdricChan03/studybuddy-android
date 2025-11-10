@file:SuppressLint("StaticFieldLeak")

package com.edricchan.studybuddy.features.tasks.vm

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants.TodoSortValues
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.repo.toggleCompleted
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor(
    private val repository: TaskRepository,
    @param:ApplicationContext private val context: Context,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private val taskOptionsPrefs = context.getSharedPreferences(
        TodoOptionsPrefConstants.FILE_TODO_OPTIONS,
        Context.MODE_PRIVATE
    )

    // TODO: Remove when migrated to new UI for sorting preferences
    private val sortCompatMap = mapOf(
        TodoSortValues.NONE to null,
        TodoSortValues.TITLE_ASC to ("title" to Query.Direction.ASCENDING),
        TodoSortValues.TITLE_DESC to ("title" to Query.Direction.DESCENDING),
        TodoSortValues.DUE_DATE_NEW_TO_OLD to ("dueDate" to Query.Direction.DESCENDING),
        TodoSortValues.DUE_DATE_OLD_TO_NEW to ("dueDate" to Query.Direction.ASCENDING)
    )

    // A MutableStateFlow but without strict equality comparisons
    private val _query =
        MutableSharedFlow<QueryMapper?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /** The current Firestore query applied on the list of tasks. */
    val query = _query.asSharedFlow()

    /** Sets the new Firestore query to be applied on the list of tasks. */
    suspend fun setQuery(newQuery: QueryMapper?) {
        _query.emit(newQuery)
    }

    /** Resets the current Firestore query. */
    suspend fun resetQuery() {
        setQuery(null)
    }

    /** The (compat) Firestore query to use, in its [TodoSortValues] form. */
    val compatQuery = savedState.getStateFlow(
        KEY_COMPAT_QUERY,
        taskOptionsPrefs.getString(
            TodoOptionsPrefConstants.PREF_DEFAULT_SORT, TodoSortValues.NONE
        )
    )

    /** Updates the current Firestore query with the new sorting option to use. */
    @Deprecated("For backwards compatibility with existing shared prefs")
    suspend fun updateSort(value: String) {
        // Persist updated data
        taskOptionsPrefs.edit {
            putString(
                TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                value
            )
        }
        savedState[KEY_COMPAT_QUERY] = value

        // Update the query
        val sortBy = sortCompatMap[value]
        setQuery(
            sortBy?.let { (field, dir) ->
                { it.orderBy(field, dir) }
            } ?: run {
                Log.w(TAG, "Unrecognised sort option $value was specified")
                // Reset the query used
                null
            }
        )
    }

    /** The current list of tasks to be shown. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks = query
        .flatMapLatest {
            if (it != null) repository.observeQueryTasks(it) else repository.tasksFlow
        }

    /** Toggles and updates the specified [task][item]'s [done][TodoItem.done] status. */
    suspend fun toggleTaskDone(item: TodoItem) {
        repository.toggleCompleted(item)
    }

    /** Removes the specified [task]. */
    suspend fun removeTask(task: TodoItem) {
        repository.removeTask(task)
    }

    // UI
    /** Refreshes the current list of items. */
    suspend fun refresh() {
        // Re-emit the previous query. We assume that the replay cache only has
        // one value
        _query.emit(_query.replayCache.first())
    }

    init {
        // Set the initial query
        viewModelScope.launch {
            resetQuery()
        }
    }

    companion object {
        /**
         * Saved-state key used to store the current query.
         *
         * Its saved-state value represents the compat version as used in the
         * task settings UI.
         * @see TodoSortValues
         */
        const val KEY_COMPAT_QUERY = "tasks:compatQuery"
    }
}
