package com.edricchan.studybuddy.ui.modules.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.edricchan.studybuddy.core.compat.navigation.navigateToDebugModalBottomSheet
import com.edricchan.studybuddy.core.compat.navigation.navigateToFeatureFlagsList
import com.edricchan.studybuddy.ui.modules.debug.compose.DebugScreen
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Deprecated("Use DebugScreen instead")
@AndroidEntryPoint
class DebugFragment : Fragment() {
    @Inject
    lateinit var userFlow: Flow<@JvmSuppressWildcards FirebaseUser?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val nestedScrollInterop = rememberNestedScrollInteropConnection()

        StudyBuddyTheme {
            DebugScreen(
                modifier = Modifier.nestedScroll(nestedScrollInterop),
                contentPadding = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
                    .asPaddingValues() + PaddingValues(16.dp),
                onNavigateToDebugModalBottomSheet = findNavController()::navigateToDebugModalBottomSheet,
                onNavigateToFeatureFlagsList = findNavController()::navigateToFeatureFlagsList,
                userFlow = userFlow
            )
        }
    }
}
