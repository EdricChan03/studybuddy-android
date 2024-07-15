package com.edricchan.studybuddy.features.help

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.help.adapter.HelpArticleAdapter
import com.edricchan.studybuddy.features.help.constants.uriSendFeedback
import com.edricchan.studybuddy.features.help.constants.uriSrcCode
import com.edricchan.studybuddy.features.help.databinding.ActivityHelpBinding
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.common.licenses.OssLicensesCompatActivity
import com.edricchan.studybuddy.ui.theming.setDynamicColors
import com.edricchan.studybuddy.utils.web.launchUri
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@WebDeepLink(["/help"])
@AppDeepLink(["/help"])
@AndroidEntryPoint
class HelpActivity : BaseActivity() {
    private lateinit var binding: ActivityHelpBinding
    private lateinit var adapter: HelpArticleAdapter

    private val viewModel: HelpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityHelpBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener { loadFeaturedList() }
            setDynamicColors()
        }

        initRecyclerView()
        initAdapter()
        loadFeaturedList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_help, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_refresh -> {
                binding.swipeRefreshLayout.isRefreshing = true
                loadFeaturedList()
                true
            }

            R.id.action_send_feedback -> {
                launchUri(uriSendFeedback)
                true
            }

            R.id.action_version -> {
                showVersionDialog()
                true
            }

            R.id.action_licenses -> {
                startActivity(Intent(this, OssLicensesCompatActivity::class.java))
                true
            }

            R.id.action_source_code -> {
                launchUri(uriSrcCode)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Initialises the recycler view by calling the needed methods
     */
    private fun initRecyclerView() {
        binding.helpFeaturedRecyclerView.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this@HelpActivity)
            addItemDecoration(
                MaterialDividerItemDecoration(
                    this@HelpActivity,
                    MaterialDividerItemDecoration.VERTICAL
                ).apply {
                    isLastItemDecorated = false
                }
            )
        }
    }

    private fun initAdapter() {
        adapter = HelpArticleAdapter().apply {
            setOnItemClickListener { article, _ ->
                launchUri(article.uri)
            }
        }.also { binding.helpFeaturedRecyclerView.adapter = it }

        lifecycleScope.launch {
            viewModel.helpArticles.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    adapter.submitList(it)
                }
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
            lifecycleScope.launch {
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
