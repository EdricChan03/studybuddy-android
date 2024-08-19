package com.edricchan.studybuddy.ui.common.fab

import androidx.activity.ComponentActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.ui.common.fab.FabController.FragmentEvent
import com.edricchan.studybuddy.ui.common.fragment.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

/**
 * Controls the specified [fab]'s data.
 *
 * Data should be added via the [onFragmentEnter] method, which triggers a [FragmentEvent.Enter],
 * or removed via the [onFragmentExit] method, triggering a [FragmentEvent.Exit].
 * @param scope [CoroutineScope] to set-up the binding logic.
 * @param fab The [FloatingActionButton] to be controlled.
 */
class FabController(
    private val scope: CoroutineScope,
    private val fab: FloatingActionButton
) {
    private val fragmentEvents = MutableSharedFlow<FragmentEvent>()

    init {
        scope.launch {
            fragmentEvents
                // We use a list of null FabDatas for fragments that do inherit the
                // base fragment class but still don't want a FAB to be shown
                .scan(emptyList<FabConfig?>()) { currentFabDataList, event ->
                    when (event) {
                        is FragmentEvent.Enter -> currentFabDataList + event.fabConfig
                        FragmentEvent.Exit -> currentFabDataList.dropLast(1)
                    }
                }
                .collect { fabDataList ->
                    val fabData = fabDataList.lastOrNull()
                    fab.apply {
                        if (fabData != null) {
                            show()
                            setImageResource(fabData.iconRes)
                            setOnClickListener { fabData.onClick() }
                        } else {
                            hide()
                        }
                    }
                }
        }
    }

    /** Call this method when a fragment is visible with the specified [FabConfig]. */
    suspend fun onFragmentEnter(fabConfig: FabConfig?) {
        fragmentEvents.emit(FragmentEvent.Enter(fabConfig))
    }

    /** Call this method when a fragment is no longer visible. */
    suspend fun onFragmentExit() {
        fragmentEvents.emit(FragmentEvent.Exit)
    }

    sealed interface FragmentEvent {
        /** A fragment is visible with the specified [fabConfig]. */
        data class Enter(val fabConfig: FabConfig?) : FragmentEvent

        /** The current fragment is no longer visible. */
        data object Exit : FragmentEvent
    }


    /**
     * Binds the [FabController] to the specified [fragment].
     *
     * ## Behaviour
     * * Fragments that are attached to the specified [fragment]'s [Fragment.getChildFragmentManager]
     * will result in [onFragmentEnter] being invoked _only_ if they inherit from [BaseFragment].
     * * When they are in the paused state, [onFragmentExit] will be called unless it's a
     * [DialogFragment] (and it's [being shown as a dialog][DialogFragment.getShowsDialog]).
     * @param fragment The [Fragment] to bind the [FabController] logic to.
     */
    fun bindToFragment(
        fragment: Fragment
    ) {
        fragment.childFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(manager: FragmentManager, fragment: Fragment) {
                super.onFragmentResumed(manager, fragment)

                scope.launch {
                    if (fragment is BaseFragment) {
                        onFragmentEnter(fragment.fabConfig)
                    }
                }
            }

            override fun onFragmentPaused(manager: FragmentManager, fragment: Fragment) {
                super.onFragmentPaused(manager, fragment)

                scope.launch {
                    // Skip dialog fragments or if the dialog fragment is shown
                    // as a dialog
                    if (fragment !is DialogFragment || !fragment.showsDialog) {
                        onFragmentExit()
                    }
                }
            }
        }, false)
    }

}

/**
 * Sets up the [FabController] for the receiver [ComponentActivity].
 * @param fab The [FloatingActionButton] to be controlled.
 * @param hostFragment The hosting [Fragment] to bind the lifecycle callbacks to.
 * @return The created [FabController].
 * @see FabController.bindToFragment
 */
fun ComponentActivity.setupFabController(
    fab: FloatingActionButton,
    hostFragment: Fragment
): FabController = FabController(
    fab = fab,
    scope = lifecycleScope
).apply {
    bindToFragment(hostFragment)
}
