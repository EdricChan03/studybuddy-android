package com.edricchan.studybuddy.ui.modules.main.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToAccountInfo
import com.edricchan.studybuddy.databinding.FragBottomappbarBottomsheetBinding
import com.edricchan.studybuddy.databinding.FragBottomappbarBottomsheetHeaderBinding
import com.edricchan.studybuddy.exts.android.getParcelableCompat
import com.edricchan.studybuddy.exts.androidx.viewbinding.viewInflateBinding
import com.edricchan.studybuddy.ui.insets.enableEdgeToEdge
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.edricchan.studybuddy.features.auth.R as AuthR

class NavBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var navController: NavController

    /** Listener for the [NavigationView] */
    var navigationViewListener: (MenuItem) -> Boolean = { false }

    /** Whether there is a logged in user */
    var isLoggedIn: Boolean = false
    var displayName: String? = null
    var email: String? = null
    var photoUrl: Uri? = null
    var navigationViewCheckedItemId: Int? = null

    private val binding by viewInflateBinding(FragBottomappbarBottomsheetBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.navigationView.apply {
            setNavigationItemSelectedListener(navigationViewListener)
            navigationViewCheckedItemId?.let { setCheckedItem(it) }
        }

        savedInstanceState?.apply {
            isLoggedIn = getBoolean(IS_LOGGED_IN_TAG)
            displayName = getString(USER_NAME_TAG)
            email = getString(USER_EMAIL_TAG)
            photoUrl = getParcelableCompat(USER_PHOTO_URL_TAG)
        }

        val headerView = binding.navigationView.getHeaderView(0)
        val headerBinding = FragBottomappbarBottomsheetHeaderBinding.bind(headerView)

        headerBinding.apply {
            root.setOnClickListener {
                navController.navigateToAccountInfo()
            }

            if (isLoggedIn) {
                displayName?.let { userName.text = it }
                email?.let { userEmail.text = it }
                photoUrl?.let(userAvatar::load)
            } else {
                userName.setText(AuthR.string.account_user_name_default)
                userEmail.setText(AuthR.string.account_user_email_default)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(
            bundleOf(
                IS_LOGGED_IN_TAG to isLoggedIn,
                USER_NAME_TAG to displayName,
                USER_EMAIL_TAG to email,
                USER_PHOTO_URL_TAG to photoUrl
            )
        )
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.enableEdgeToEdge()
        }

    companion object {
        private const val IS_LOGGED_IN_TAG = "isLoggedIn"
        private const val USER_NAME_TAG = "userName"
        private const val USER_EMAIL_TAG = "userEmail"
        private const val USER_PHOTO_URL_TAG = "userPhotoUrl"
    }
}

/**
 * Shows a [navigation bottom sheet dialog][NavBottomSheetDialogFragment].
 * @param tag The tag for the fragment.
 * @param init Options to be passed to the dialog fragment.
 * @return The created fragment.
 */
inline fun FragmentActivity.showNavBottomSheet(
    tag: String? = null,
    init: NavBottomSheetDialogFragment.() -> Unit
) = NavBottomSheetDialogFragment().apply(init).apply { show(supportFragmentManager, tag) }

/**
 * Shows a [navigation bottom sheet dialog][NavBottomSheetDialogFragment].
 * @param fragmentManager The [fragment manager][FragmentManager] to use.
 * @param tag The tag for the fragment.
 * @param init Options to be passed to the dialog fragment.
 * @return The created fragment.
 */
inline fun Fragment.showNavBottomSheet(
    fragmentManager: FragmentManager = parentFragmentManager,
    tag: String? = null,
    init: NavBottomSheetDialogFragment.() -> Unit
) = NavBottomSheetDialogFragment().apply(init).apply { show(fragmentManager, tag) }
