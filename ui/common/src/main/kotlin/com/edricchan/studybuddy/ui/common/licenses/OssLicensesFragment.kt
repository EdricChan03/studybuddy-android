package com.edricchan.studybuddy.ui.common.licenses

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext

@Deprecated("Use the LibrariesContainer composable instead")
class OssLicensesFragment : ComposableFragment() {
    @Composable
    override fun Content(modifier: Modifier) {
        val context = LocalContext.current
        val libs = remember { Libs.Builder().withContext(context).build() }

        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            LicensesContainer(modifier = modifier, libs = libs)
        }
    }
}
