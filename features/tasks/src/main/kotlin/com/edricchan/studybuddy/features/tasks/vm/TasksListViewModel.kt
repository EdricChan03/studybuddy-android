package com.edricchan.studybuddy.features.tasks.vm

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.domain.common.sorting.SortDirection
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants.TodoSortValues
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.repo.toggleCompleted
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
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
}
