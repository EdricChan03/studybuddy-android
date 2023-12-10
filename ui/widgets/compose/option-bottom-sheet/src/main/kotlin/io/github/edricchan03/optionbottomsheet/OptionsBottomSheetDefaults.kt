package io.github.edricchan03.optionbottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Stable
object OptionsBottomSheetDefaults {
    @Composable
    fun Header(modifier: Modifier = Modifier, title: String) {
        Text(
            modifier = modifier.padding(start = 20.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
