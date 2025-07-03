package com.edricchan.studybuddy.ui.common.licenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.exts.androidx.compose.plus
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun LicensesContainer(
    modifier: Modifier = Modifier,
    libs: Libs,
    lazyListState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
        .asPaddingValues() + PaddingValues(16.dp)
) = LibrariesContainer(
    modifier = modifier,
    libraries = libs,
    libraryModifier = Modifier
        .clip(MaterialTheme.shapes.large)
        // We need to set the background modifier manually, see
        // https://github.com/mikepenz/AboutLibraries/issues/1206
        .background(MaterialTheme.colorScheme.surfaceContainer),
    dimensions = LibraryDefaults.libraryDimensions(
        itemSpacing = 8.dp
    ),
    lazyListState = lazyListState,
    contentPadding = contentPadding,
    showDescription = true,
    showFundingBadges = true
)
