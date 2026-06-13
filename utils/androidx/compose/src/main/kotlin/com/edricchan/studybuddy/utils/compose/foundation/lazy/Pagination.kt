package com.edricchan.studybuddy.utils.compose.foundation.lazy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Displays a [LoadingIndicator] for the specified receiver [LazyItemScope]. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LazyItemScope.PaginationLoadingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillParentMaxWidth()
            .animateItem(),
        horizontalArrangement = Arrangement.Center
    ) {
        LoadingIndicator()
    }
}
