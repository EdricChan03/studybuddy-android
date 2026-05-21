package com.edricchan.studybuddy.ui.common.licenses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.ui.common.licenses.vm.LicensesViewModel
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.m3.LicenseDialog
import com.mikepenz.aboutlibraries.ui.compose.m3.LicenseDialogBody
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors

@Composable
fun OssLicensesListScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    windowInsets: WindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom),
    viewModel: LicensesViewModel
) {
    val filteredLibraries by viewModel.libraries.collectAsStateWithLifecycle(initialValue = viewModel.allLibraries)
    val selectedLicenses by viewModel.selectedLicenses.collectAsStateWithLifecycle()
    val selectedLibrary by viewModel.selectedLibrary.collectAsStateWithLifecycle()

    OssLicensesListScreen(
        modifier = modifier.consumeWindowInsets(windowInsets),
        contentPadding = contentPadding + windowInsets.asPaddingValues(),
        filteredLibraries = filteredLibraries,
        licenseCounts = viewModel.licenseCounts,
        selectedLicenses = selectedLicenses,
        onSelectLicenses = viewModel::setSelectedLicenses,
        searchBarTextFieldState = viewModel.searchBarState,
        shownLibrary = selectedLibrary,
        onRequestShowLibrary = viewModel::setSelectedLibrary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OssLicensesListScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    filteredLibraries: List<Library>,
    licenseCounts: Map<License, Int>,
    lazyListState: LazyListState = rememberLazyListState(),
    searchBarTextFieldState: TextFieldState,
    selectedLicenses: Set<License>,
    onSelectLicenses: (Set<License>) -> Unit,
    shownLibrary: Library?,
    onRequestShowLibrary: (Library?) -> Unit
) {
    Column(
        modifier = modifier.padding(contentPadding),
    ) {
        LicensesFilterOptions(
            searchTextFieldState = searchBarTextFieldState,
            licenseCounts = licenseCounts,
            selectedLicenses = selectedLicenses,
            onSelectLicenses = onSelectLicenses
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            items(
                items = filteredLibraries,
                key = { it.uniqueId }
            ) {
                LibraryItem(
                    modifier = Modifier.animateItem(),
                    lib = it,
                    onClick = {
                        onRequestShowLibrary(it)
                    }
                )
            }
            if (filteredLibraries.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FilteredLicensesEmptyState()
                    }
                }
            }
        }
    }

    shownLibrary?.let {
        LicenseDialog(
            library = it,
            body = { lib, modifier ->
                LicenseDialogBody(
                    library = lib,
                    modifier = modifier,
                    colors = LibraryDefaults.libraryColors()
                )
            },
            onDismiss = {
                onRequestShowLibrary(null)
            }
        )
    }
}
