package com.edricchan.studybuddy.features.help.compat

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.edricchan.studybuddy.core.compat.navigation.about.navigateToLicenses
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.help.HelpViewModel
import com.edricchan.studybuddy.features.help.R
import com.edricchan.studybuddy.features.help.adapter.HelpArticleAdapter
import com.edricchan.studybuddy.features.help.constants.uriSendFeedback
import com.edricchan.studybuddy.features.help.constants.uriSrcCode
import com.edricchan.studybuddy.features.help.databinding.FragHelpListBinding
import com.edricchan.studybuddy.features.help.showVersionDialog
import com.edricchan.studybuddy.features.help.ui.HelpArticlesState
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.edricchan.studybuddy.ui.theming.setDynamicColors
import com.edricchan.studybuddy.utils.web.launchUri
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HelpListFragment : ViewBindingFragment<FragHelpListBinding>(FragHelpListBinding::inflate) {
    private val viewModel: HelpViewModel by viewModels()
    private lateinit var adapter: HelpArticleAdapter

    override val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_help, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_refresh -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                    loadFeaturedList()
                    true
                }

                R.id.action_send_feedback -> {
                    requireContext().launchUri(uriSendFeedback)
                    true
                }

                R.id.action_version -> {
                    requireContext().showVersionDialog()
                    true
                }

                R.id.action_licenses -> {
                    navController.navigateToLicenses()
                    true
                }

                R.id.action_source_code -> {
                    requireContext().launchUri(uriSrcCode)
                    true
                }

                else -> false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener { loadFeaturedList() }
            setDynamicColors()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updatePadding(
                left = insets.left,
                right = insets.right
            )

            val contentInsets = windowInsets.getInsets(
                WindowInsetsCompat.Type.displayCutout()
            )
            binding.helpFeaturedRecyclerView.updatePadding(
                left = contentInsets.left,
                right = contentInsets.right
            )

            windowInsets
        }

        initRecyclerView()
        initAdapter()
        loadFeaturedList()
    }

    /**
     * Initialises the recycler view by calling the needed methods
     */
    private fun initRecyclerView() {
        binding.helpFeaturedRecyclerView.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    MaterialDividerItemDecoration.VERTICAL
                ).apply {
                    isLastItemDecorated = false
                }
            )
        }
    }

    private fun onStateChanged(state: HelpArticlesState) {
        when (state) {
            HelpArticlesState.Loading -> {
                with(binding) {
                    helpFeaturedRecyclerView.isVisible = false
                    progressBarLinearLayout.isVisible = true
                    tvEmptyStateLoading.isVisible = true
                    tvEmptyState.isVisible = false
                }
            }

            is HelpArticlesState.Success -> {
                with(binding) {
                    helpFeaturedRecyclerView.isVisible = true
                    progressBarLinearLayout.isVisible = false
                    adapter.submitList(state.articles)
                }
            }

            is HelpArticlesState.Error -> {
                with(binding) {
                    helpFeaturedRecyclerView.isVisible = false
                    progressBarLinearLayout.isVisible = true
                    tvEmptyStateLoading.isVisible = false
                    tvEmptyState.isVisible = true
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = HelpArticleAdapter().apply {
            setOnItemClickListener { article, _ ->
                requireContext().launchUri(article.uri)
            }
        }.also { binding.helpFeaturedRecyclerView.adapter = it }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.helpArticles.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect(::onStateChanged)
        }
    }

    /**
     * Loads the featured items from the JSON file
     */
    private fun loadFeaturedList() {
        try {
            binding.apply {
                progressBarLinearLayout.isVisible = true
                helpFeaturedRecyclerView.isVisible = false
            }
            // Load data
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.refreshHelpArticles()
            }.invokeOnCompletion {
                if (it != null && it !is CancellationException) {
                    // An error occurred
                    Log.e(TAG, "Could not retrieve help articles:", it)
                }
                binding.apply {
                    helpFeaturedRecyclerView.isVisible = true
                    swipeRefreshLayout.isRefreshing = false
                    TransitionManager.beginDelayedTransition(constraintLayout, Fade(Fade.IN))
                    progressBarLinearLayout.isVisible = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "An error occurred while attempting to parse the JSON:", e)
        }
    }
}
