package com.edricchan.studybuddy.ui.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.core.net.toUri
import androidx.fragment.compose.content
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.utils.web.launchUri

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
     * * An overridden [LocalUriHandler] is supplied with the ability to open a Chrome Custom
     * Tab if desired by the user.
     * @param modifier [Modifier] to be passed to the layout.
     */
    @Composable
    protected abstract fun Content(modifier: Modifier)

    private val uriHandler: UriHandler = object : UriHandler {
        override fun openUri(uri: String) {
            requireContext().launchUri(uri.toUri())
        }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        val nestedScrollInterop = rememberNestedScrollInteropConnection()
        StudyBuddyTheme {
            CompositionLocalProvider(LocalUriHandler provides uriHandler) {
                Content(modifier = Modifier.nestedScroll(nestedScrollInterop))
            }
        }
    }
}
