package com.edricchan.studybuddy.features.about.ui.sections

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.exts.android.metadata.appLabel
import com.edricchan.studybuddy.ui.widgets.compose.AppIcon
import com.edricchan.studybuddy.core.resources.R as CoreResR

@Composable
private fun AboutAppTitleHeader(
    modifier: Modifier = Modifier,
    appLabel: String,
    appIcon: @Composable () -> Unit
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    appIcon()

    Text(
        modifier = Modifier.semantics { heading() },
        text = appLabel,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
internal fun AboutAppTitleHeader(
    modifier: Modifier = Modifier,
    appIconModifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    val isPreview = LocalInspectionMode.current
    val previewAppLabel = stringResource(CoreResR.string.app_name)
    val label = remember(context, isPreview) {
        if (isPreview) previewAppLabel else context.appLabel.toString()
    }

    AboutAppTitleHeader(
        modifier = modifier,
        appLabel = label,
        appIcon = {
            AppIcon(modifier = appIconModifier)
        }
    )
}
