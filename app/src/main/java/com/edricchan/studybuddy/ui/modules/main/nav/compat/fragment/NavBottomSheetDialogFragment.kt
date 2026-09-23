package com.edricchan.studybuddy.ui.modules.main.nav.compat.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.edricchan.studybuddy.core.auth.model.User
import com.edricchan.studybuddy.exts.android.getParcelableCompat
import com.edricchan.studybuddy.features.auth.navigation.navigateToAccountInfo
import com.edricchan.studybuddy.ui.insets.enableEdgeToEdge
import com.edricchan.studybuddy.ui.modules.main.nav.NavItem
import com.edricchan.studybuddy.ui.modules.main.nav.NavModalBottomSheetContent
import com.edricchan.studybuddy.ui.modules.main.nav.vm.NavBottomSheetViewModel
import com.edricchan.studybuddy.ui.theming.common.AppearancePreferences
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import com.edricchan.studybuddy.features.auth.R as AuthR

@Deprecated("Use NavModalBottomSheetContent directly")
@AndroidEntryPoint
class NavBottomSheetDialogFragment : BottomSheetDialogFragment() {
    @Parcelize
    @Stable
    sealed interface UserData : Parcelable {
        val displayName: String?
        val email: String?
        val photoUrl: Uri?

        @Parcelize
        data object Anonymous : UserData {
            @IgnoredOnParcel
            override val displayName: String? = null

            @IgnoredOnParcel
            override val email: String? = null

            @IgnoredOnParcel
            override val photoUrl: Uri? = null
        }

        @Immutable
        @Parcelize
        data class LoggedIn(
            override val displayName: String? = null,
            override val email: String? = null,
            override val photoUrl: Uri? = null
        ) : UserData
    }

    @Inject
    lateinit var appearancePreferences: AppearancePreferences

    private val viewModel by viewModels<NavBottomSheetViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.window?.enableEdgeToEdge()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentLayout =
            (dialog as BottomSheetDialog).findViewById<ViewGroup>(R.id.design_bottom_sheet)
        parentLayout?.apply {
            // Clip the background such that the corners are rounded
            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val nestedScrollInterop = rememberNestedScrollInteropConnection()

        StudyBuddyTheme(
            appearancePreferences = appearancePreferences
        ) {
            val selected by viewModel.selectedItem.collectAsStateWithLifecycle()
            val userData by viewModel.userData.collectAsStateWithLifecycle()

            NavModalBottomSheetContent(
                modifier = Modifier.nestedScroll(nestedScrollInterop),
                headerModifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        onClickLabel = stringResource(AuthR.string.account_account_actions_button_title),
                        onClick = ::onHeaderClick
                    ),
                displayName = userData.displayName,
                email = userData.email,
                photoUrl = userData.photoUrl,
                selectedItem = selected,
                onItemClick = ::onNavItemClick
            )
        }
    }

    private fun onHeaderClick() {
        findNavController().navigateToAccountInfo()
        dismiss()
    }

    private fun onNavItemClick(item: NavItem) {
        viewModel.setSelectedItem(item)
        setFragmentResult(
            RESULT_KEY, FragmentResult(
                item
            ).toBundle()
        )
        dismiss()
    }

    /**
     * Data class used for this fragment's [setFragmentResult].
     * @property selectedItem The selected navigation item.
     */
    @Parcelize
    data class FragmentResult(
        val selectedItem: NavItem
    ) : Parcelable {
        fun toBundle() = Bundle().apply { putParcelable(RESULT_DATA_KEY, this@FragmentResult) }

        companion object {
            fun fromBundle(bundle: Bundle): FragmentResult? =
                bundle.getParcelableCompat(RESULT_DATA_KEY)
        }
    }


    companion object {
        internal const val TAG_USER_DATA = "nav-sheet:userData"
        internal const val TAG_SELECTED_NAV_ITEM = "nav-sheet:selectedNavItem"

        const val RESULT_KEY = "nav-sheet:result"
        const val RESULT_DATA_KEY = "nav-sheet:resultData"

        fun newInstance(
            selectedItem: NavItem?,
            userData: UserData
        ) = NavBottomSheetDialogFragment().apply {
            arguments = getArguments(selectedItem = selectedItem, userData = userData)
        }

        fun getArguments(
            selectedItem: NavItem?,
            userData: UserData
        ) = Bundle().apply {
            putSerializable(TAG_SELECTED_NAV_ITEM, selectedItem)
            putParcelable(TAG_USER_DATA, userData)
        }
    }
}

inline fun navBottomSheet(
    selectedItem: NavItem?,
    userData: NavBottomSheetDialogFragment.UserData
): NavBottomSheetDialogFragment =
    NavBottomSheetDialogFragment.newInstance(selectedItem = selectedItem, userData = userData)

inline fun FragmentManager.setNavSheetFragmentResultListeners(
    lifecycleOwner: LifecycleOwner,
    crossinline onSelectedItemChange: (NavItem) -> Unit
) {
    setFragmentResultListener(
        NavBottomSheetDialogFragment.RESULT_KEY,
        lifecycleOwner
    ) { _, bundle ->
        val result = NavBottomSheetDialogFragment.FragmentResult.fromBundle(bundle)
            ?: return@setFragmentResultListener

        onSelectedItemChange(result.selectedItem)
    }
}

/**
 * Shows a [navigation bottom sheet dialog][NavBottomSheetDialogFragment].
 * @param tag The tag for the fragment.
 * @param selectedItem The initially selected item.
 * @param userData The user data to be shown.
 * @param lifecycleOwner [LifecycleOwner] to be passed to [setNavSheetFragmentResultListeners].
 * @param onSelectedItemChange Invoked when a [NavItem] is selected.
 * @return The created fragment.
 */
inline fun FragmentManager.showNavBottomSheet(
    tag: String? = null,
    selectedItem: NavItem?,
    userData: NavBottomSheetDialogFragment.UserData,
    lifecycleOwner: LifecycleOwner,
    crossinline onSelectedItemChange: (NavItem) -> Unit
): NavBottomSheetDialogFragment = navBottomSheet(
    selectedItem = selectedItem, userData = userData
).apply {
    show(this@showNavBottomSheet, tag)
    setNavSheetFragmentResultListeners(
        lifecycleOwner = lifecycleOwner,
        onSelectedItemChange = onSelectedItemChange
    )
}

/**
 * Shows a [navigation bottom sheet dialog][NavBottomSheetDialogFragment].
 *
 * For real-time updates to the latest selected item, use
 * [NavBottomSheetViewModel.selectedItem].
 * @param tag The tag for the fragment.
 * @param selectedItem The initially selected item.
 * @param userData The user data to be shown.
 * @param lifecycleOwner [LifecycleOwner] to be passed to [setNavSheetFragmentResultListeners].
 * @param onSelectedItemChange Invoked when a [NavItem] is selected.
 * @return The created fragment.
 */
inline fun FragmentActivity.showNavBottomSheet(
    tag: String? = null,
    selectedItem: NavItem?,
    userData: NavBottomSheetDialogFragment.UserData,
    lifecycleOwner: LifecycleOwner = this,
    crossinline onSelectedItemChange: (NavItem) -> Unit = {}
): NavBottomSheetDialogFragment = supportFragmentManager.showNavBottomSheet(
    tag = tag,
    selectedItem = selectedItem,
    userData = userData,
    lifecycleOwner = lifecycleOwner, onSelectedItemChange = onSelectedItemChange
)

/**
 * Shows a [navigation bottom sheet dialog][NavBottomSheetDialogFragment].
 * @param fragmentManager The [fragment manager][FragmentManager] to use.
 * @param tag The tag for the fragment.
 * @param selectedItem The initially selected item.
 * @param userData The user data to be shown.
 * @param lifecycleOwner [LifecycleOwner] to be passed to [setNavSheetFragmentResultListeners].
 * @param onSelectedItemChange Invoked when a [NavItem] is selected.
 * @return The created fragment.
 */
inline fun Fragment.showNavBottomSheet(
    fragmentManager: FragmentManager = parentFragmentManager,
    tag: String? = null,
    selectedItem: NavItem?,
    userData: NavBottomSheetDialogFragment.UserData,
    lifecycleOwner: LifecycleOwner,
    crossinline onSelectedItemChange: (NavItem) -> Unit
) = fragmentManager.showNavBottomSheet(
    tag = tag,
    selectedItem = selectedItem,
    userData = userData,
    lifecycleOwner = lifecycleOwner,
    onSelectedItemChange = onSelectedItemChange
)

fun FirebaseUser?.asUserData(
    isLoggedIn: Boolean = this != null
): NavBottomSheetDialogFragment.UserData = if (isLoggedIn)
    NavBottomSheetDialogFragment.UserData.LoggedIn(
        displayName = this?.displayName,
        email = this?.email,
        photoUrl = this?.photoUrl
    ) else NavBottomSheetDialogFragment.UserData.Anonymous

fun User?.asUserData(
    isLoggedIn: Boolean = this != null
): NavBottomSheetDialogFragment.UserData = if (isLoggedIn)
    NavBottomSheetDialogFragment.UserData.LoggedIn(
        displayName = this?.displayName,
        email = this?.email,
        photoUrl = this?.photoUri
    ) else NavBottomSheetDialogFragment.UserData.Anonymous
