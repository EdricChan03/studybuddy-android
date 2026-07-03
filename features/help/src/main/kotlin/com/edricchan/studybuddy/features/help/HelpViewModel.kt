package com.edricchan.studybuddy.features.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.features.help.data.HelpRepository
import com.edricchan.studybuddy.features.help.data.model.HelpArticle
import com.edricchan.studybuddy.features.help.ui.HelpArticlesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val repository: HelpRepository
) : ViewModel() {
    /** The current list of help articles. */
    val helpArticles: StateFlow<HelpArticlesState>
        field = MutableStateFlow<HelpArticlesState>(HelpArticlesState.Loading)

    init {
        viewModelScope.launch {
            refreshHelpArticles()
        }
    }

    /** Refreshes the list of help articles. */
    suspend fun refreshHelpArticles() {
        helpArticles.value = HelpArticlesState.Loading
        withContext(Dispatchers.IO) {
            helpArticles.value = try {
                HelpArticlesState.Success(
                    repository.fetchHelpArticles().filterNot { it.isHidden }
                )
            } catch (e: Exception) {
                HelpArticlesState.Error(e)
            }
        }
    }
}
