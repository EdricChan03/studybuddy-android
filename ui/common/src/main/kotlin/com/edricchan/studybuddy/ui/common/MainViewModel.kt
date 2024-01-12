package com.edricchan.studybuddy.ui.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _snackBarData = MutableSharedFlow<SnackBarData>()

    /** The [SnackBarData] to be shown, if any. */
    val snackBarData = _snackBarData.asSharedFlow()

    /**
     * Shows a snack-bar in the main UI with the specified parameters.
     * @param messageRes The message to be shown, as a string resource.
     * @param duration How long the snack-bar should be shown for.
     * @param action The snack-bar's action data if any.
     */
    suspend fun showSnackBar(
        @StringRes
        messageRes: Int,
        duration: SnackBarData.Duration,
        action: SnackBarData.Action? = null
    ) {
        _snackBarData.emit(
            SnackBarData(
                message = context.getString(messageRes),
                duration = duration,
                action = action
            )
        )
    }

    // TODO: Remove logic when migrated to Compose
    @SuppressLint("StaticFieldLeak") // TODO: Fix Context leak
    lateinit var fab: FloatingActionButton
        private set

    @SuppressLint("StaticFieldLeak") // TODO: Fix Context leak
    lateinit var bottomAppBar: BottomAppBar
        private set

    fun init(
        fab: FloatingActionButton,
        bottomAppBar: BottomAppBar
    ) {
        this.fab = fab
        this.bottomAppBar = bottomAppBar
    }
}
