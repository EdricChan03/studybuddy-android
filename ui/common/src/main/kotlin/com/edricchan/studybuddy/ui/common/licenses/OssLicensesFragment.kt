package com.edricchan.studybuddy.ui.common.licenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Deprecated("Use the LibrariesContainer composable instead")
class OssLicensesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val nestedScrollInterop = rememberNestedScrollInteropConnection()

        StudyBuddyTheme {
            Surface {
                LibrariesContainer(
                    modifier = Modifier.nestedScroll(nestedScrollInterop),
                    contentPadding = WindowInsets.navigationBars.asPaddingValues()
                )
            }
        }
    }
}
