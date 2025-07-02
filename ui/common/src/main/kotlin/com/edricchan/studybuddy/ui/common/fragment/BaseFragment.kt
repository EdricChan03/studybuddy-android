package com.edricchan.studybuddy.ui.common.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.Discouraged
import androidx.annotation.StringRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.edricchan.studybuddy.ui.common.MainViewModel
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fab.FabConfig
import com.edricchan.studybuddy.utils.web.launchUri
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A [Fragment] class which sub-classes should implement for common behaviours
 * such as:
 * * [adding a `MenuProvider`][menuProvider]
 * * [retrieving the `NavController`][navController]
 * * [configuring the host activity's `FloatingActionButton`][fabConfig]
 * * [showing a snack-bar][showSnackBar]
 */
abstract class BaseFragment : Fragment() {
    /** The [MainViewModel] as scoped to the host activity. */
    protected val mainViewModel by activityViewModels<MainViewModel>()

    /** The [MenuProvider] to be used for this fragment. */
    protected open val menuProvider: MenuProvider? = null

    /** The [NavController] of the parent [androidx.navigation.fragment.NavHostFragment]. */
    protected val navController: NavController by lazy(::findNavController)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuProvider?.let { activity?.addMenuProvider(it, viewLifecycleOwner) }
    }

    /** The [FabConfig] to use for this fragment. If set to `null`, no FAB will be shown. */
    open val fabConfig: FabConfig? = null

    /**
     * Shows a snack-bar in the host activity.
     * @param messageRes String resource ID of the message to be shown.
     * @param duration How long the snack-bar should be shown.
     * @param action The snack-bar's action, if any.
     * @return The resulting [Job] of the [launch]ed coroutine scope.
     * @see MainViewModel.showSnackBar
     */
    protected fun showSnackBar(
        @StringRes messageRes: Int,
        duration: SnackBarData.Duration = SnackBarData.Duration.Short,
        action: SnackBarData.Action? = null
    ): Job = viewLifecycleOwner.lifecycleScope.launch {
        mainViewModel.showSnackBar(
            messageRes = messageRes,
            duration = duration,
            action = action
        )
    }

    /**
     * Shows a snack-bar in the host activity.
     * @param message The message to be shown as a [String].
     * @param duration How long the snack-bar should be shown.
     * @param action The snack-bar's action, if any.
     * @return The resulting [Job] of the [launch]ed coroutine scope.
     * @see MainViewModel.showSnackBar
     */
    @Discouraged("Use the overload which takes a string resource for the message")
    protected fun showSnackBar(
        message: String,
        duration: SnackBarData.Duration = SnackBarData.Duration.Short,
        action: SnackBarData.Action? = null
    ): Job = viewLifecycleOwner.lifecycleScope.launch {
        mainViewModel.showSnackBar(
            message = message,
            duration = duration,
            action = action
        )
    }

    /**
     * Launches the desired [uri] in a Chrome Custom Tab or opens an [android.content.Intent]
     * with [android.content.Intent.ACTION_VIEW] otherwise.
     * @see com.edricchan.studybuddy.utils.web.launchUri
     */
    protected fun launchUri(uri: Uri) {
        requireContext().launchUri(uri)
    }
}
