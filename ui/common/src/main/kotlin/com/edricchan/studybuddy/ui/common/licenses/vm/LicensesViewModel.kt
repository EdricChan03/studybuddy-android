package com.edricchan.studybuddy.ui.common.licenses.vm

import android.content.Context
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.util.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class LicensesViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val initialLibs = Libs.Builder().withContext(context).build()

    val allLibraries = initialLibs.libraries
    val licenses = initialLibs.licenses

    val searchBarState = TextFieldState()

    private fun Library.matchesQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true) ||
            (description?.contains(query, ignoreCase = true) == true) ||
            licenses.any { name.contains(query, ignoreCase = true) } ||
            developers.any { name.contains(query, ignoreCase = true) } ||
            (organization?.name?.contains(query, ignoreCase = true) == true)
    }

    @OptIn(FlowPreview::class)
    val libraries = snapshotFlow { searchBarState.text.toString() }
        .debounce(100.milliseconds)
        .map { searchText ->
            allLibraries.filter { it.matchesQuery(searchText) }
        }

    fun clearFilters() {
        searchBarState.edit { delete(0, length) }
    }
}
