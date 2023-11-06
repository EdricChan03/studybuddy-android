package com.edricchan.studybuddy.ui.common.licenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.edricchan.studybuddy.ui.common.R
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import com.edricchan.studybuddy.ui.widgets.compose.BackIconButton
import io.github.edricchan03.aboutlibraries.ui.compose.m3.LibrariesContainer

@Deprecated(
    "For interop for existing views-based UIs; " +
        "consider using LibrariesContainer directly if possible"
)
class OssLicensesCompatActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            StudyBuddyTheme {
                Scaffold(
                    topBar = {
                        LargeTopAppBar(
                            title = { Text(text = stringResource(R.string.activity_license_title)) },
                            navigationIcon = {
                                BackIconButton(onClick = ::finish)
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) {
                    LibrariesContainer(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        contentPadding = it
                    )
                }
            }
        }
    }
}
