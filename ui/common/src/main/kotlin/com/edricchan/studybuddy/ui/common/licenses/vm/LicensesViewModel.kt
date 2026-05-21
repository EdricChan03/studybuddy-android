package com.edricchan.studybuddy.ui.common.licenses.vm

import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.util.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class LicensesViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val initialLibs = Libs.Builder().withContext(context).build()

    val allLibraries = initialLibs.libraries
    val licenses = initialLibs.licenses

    val licenseCounts = allLibraries
        .flatMap { it.licenses }
        .groupingBy { it }
        .eachCount()

    val searchBarState = TextFieldState()

    private val _selectedLicenses = MutableStateFlow(initialLibs.licenses)
    val selectedLicenses = _selectedLicenses.asStateFlow()

    fun setSelectedLicenses(licenses: Set<License>) {
        _selectedLicenses.value = licenses
    }

    private fun Library.matchesQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true) ||
            (description?.contains(query, ignoreCase = true) == true) ||
            licenses.any { name.contains(query, ignoreCase = true) } ||
            developers.any { name.contains(query, ignoreCase = true) } ||
            (organization?.name?.contains(query, ignoreCase = true) == true)
    }

    @OptIn(FlowPreview::class)
    val libraries: Flow<List<Library>> = combine(
        flow = selectedLicenses,
        flow2 = snapshotFlow { searchBarState.text.toString() }.debounce(100.milliseconds)
    ) { licenses, searchText ->
        if (licenses == initialLibs.licenses && searchText.isBlank())
            return@combine initialLibs.libraries

        if (licenses == initialLibs.licenses)
            return@combine initialLibs.libraries.filter { it.matchesQuery(searchText) }

        initialLibs.libraries.filter { lib ->
            lib.licenses.any { it in licenses } && lib.matchesQuery(searchText)
        }
    }

    private val _selectedLibrary = MutableStateFlow<Library?>(null)
    val selectedLibrary = _selectedLibrary.asStateFlow()

    fun setSelectedLibrary(library: Library?) {
        _selectedLibrary.value = library
    }

    fun clearFilters() {
        _selectedLicenses.value = licenses
        searchBarState.edit { delete(0, length) }
    }
}
