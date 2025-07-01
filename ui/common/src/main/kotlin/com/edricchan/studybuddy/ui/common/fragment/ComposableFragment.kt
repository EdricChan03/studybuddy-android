package com.edricchan.studybuddy.ui.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.fragment.compose.content
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/**
 * [BaseFragment] sub-class for fragments which use Jetpack Compose for its
 * content, via the [Content] function.
 */
abstract class ComposableFragment : BaseFragment() {
    /**
     * Desired composable content for this fragment.
     *
     * * Its contents are surrounded with [StudyBuddyTheme], so wrapping the theme composable
     * around the content is not required.
     * * An [interop nested-scroll][rememberNestedScrollInteropConnection] is applied to its
     * contents via the [modifier] parameter using the [nestedScroll] modifier.
     * @param modifier [Modifier] to be passed to the layout.
     */
    @Composable
    abstract fun Content(modifier: Modifier = Modifier)

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val nestedScrollInterop = rememberNestedScrollInteropConnection()
        StudyBuddyTheme {
            Content(modifier = Modifier.nestedScroll(nestedScrollInterop))
        }
    }
}
