package com.edricchan.studybuddy.ui.modules.task.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination

class TaskDetailViewModel(savedState: SavedStateHandle) : ViewModel() {
    private val routeData = savedState.toRoute<CompatDestination.Task.View>()

    val taskId = routeData.taskId
}
