package com.edricchan.studybuddy.features.auth.recovery.ui.compat

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import com.edricchan.studybuddy.features.auth.recovery.RecoveryViewModel
import com.edricchan.studybuddy.features.auth.recovery.ui.RecoveryScreen
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryFragment : ComposableFragment() {
    private val viewModel by viewModels<RecoveryViewModel>()

    @Composable
    override fun Content(modifier: Modifier) {
        RecoveryScreen(
            modifier = modifier,
            viewModel = viewModel
        )
    }
}
