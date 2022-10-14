package com.edricchan.studybuddy.ui.modules.help

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.databinding.ActivityHelpBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.base.BaseActivity
import com.edricchan.studybuddy.ui.modules.help.adapter.HelpArticleAdapter
import com.edricchan.studybuddy.utils.UiUtils
import com.edricchan.studybuddy.utils.WebUtils
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@WebDeepLink(["/help"])
@AppDeepLink(["/help"])
@AndroidEntryPoint
class HelpActivity : BaseActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivityHelpBinding
    private lateinit var adapter: HelpArticleAdapter
    private lateinit var webUtils: WebUtils

    private val viewModel: HelpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityHelpBinding.inflate(layoutInflater).also { setContentView(it.root) }

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener { loadFeaturedList() }
            setColorSchemeColors(MaterialColors.getColor(this, R.attr.colorPrimary))
        }

        webUtils = WebUtils.getInstance(this)

        initRecyclerView()
        initAdapter()
        loadFeaturedList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_help, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val webUtils = WebUtils.getInstance(this)
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_refresh -> {
                binding.swipeRefreshLayout.isRefreshing = true
                loadFeaturedList()
                true
            }

            R.id.action_send_feedback -> {
                webUtils.launchUri(Constants.uriSendFeedback)
                true
            }

            R.id.action_version -> {
                UiUtils.getInstance(this).showVersionDialog()
                true
            }

            R.id.action_licenses -> {
                startActivity<OssLicensesMenuActivity>()
                true
            }

            R.id.action_source_code -> {
                webUtils.launchUri(Constants.uriSrcCode)
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
                article.uri?.let { webUtils.launchUri(it) }
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
