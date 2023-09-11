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
import coil.load
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.FragBottomappbarBottomsheetBinding
import com.edricchan.studybuddy.databinding.FragBottomappbarBottomsheetHeaderBinding
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.exts.android.getParcelableCompat
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class NavBottomSheetDialogFragment : BottomSheetDialogFragment() {
    /** Listener for the [NavigationView] */
    var navigationViewListener: (MenuItem) -> Boolean = { false }

    /** Whether there is a logged in user */
    var isLoggedIn: Boolean = false
    var displayName: String? = null
    var email: String? = null
    var photoUrl: Uri? = null
    var navigationViewCheckedItemId: Int? = null

    private var _binding: FragBottomappbarBottomsheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragBottomappbarBottomsheetBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.apply {
            setNavigationItemSelectedListener(navigationViewListener)
            if (navigationViewCheckedItemId != null) {
                setCheckedItem(navigationViewCheckedItemId!!)
            }
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
                startActivity<AccountActivity>()
            }

            if (isLoggedIn) {
                if (displayName != null) userName.text = displayName
                if (email != null) userEmail.text = email
                if (photoUrl != null) userAvatar.load(photoUrl)
            } else {
                userName.setText(R.string.account_user_name_default)
                userEmail.setText(R.string.account_user_email_default)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
