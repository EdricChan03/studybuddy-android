package com.edricchan.studybuddy.ui.common.licenses

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import com.edricchan.studybuddy.ui.common.R
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.edricchan.studybuddy.ui.common.licenses.vm.LicensesViewModel
import com.edricchan.studybuddy.utils.androidx.core.menuProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Deprecated("Use the OssLicensesListScreen screen composable instead")
class OssLicensesFragment : ComposableFragment() {
    private val viewModel by viewModels<LicensesViewModel>()

    override val menuProvider = menuProvider(
        R.menu.menu_oss_licenses
    ) {
        if (it.itemId == R.id.action_reset_filters) {
            viewModel.clearFilters()
            return@menuProvider true
        }
        false
    }

    @Composable
    override fun Content(modifier: Modifier) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            OssLicensesListScreen(modifier = modifier, viewModel = viewModel)
        }
    }
}
