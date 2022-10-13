package com.edricchan.studybuddy.ui.modules.help

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.edricchan.studybuddy.interfaces.HelpArticle
import com.edricchan.studybuddy.interfaces.HelpArticles
import com.edricchan.studybuddy.ui.modules.base.BaseActivity
import com.edricchan.studybuddy.ui.modules.help.adapter.HelpArticleAdapter
import com.edricchan.studybuddy.utils.UiUtils
import com.edricchan.studybuddy.utils.WebUtils
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.lang.ref.WeakReference
import java.net.URL

@WebDeepLink(["/help"])
@AppDeepLink(["/help"])
class HelpActivity : BaseActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityHelpBinding.inflate(layoutInflater).also { setContentView(it.root) }

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener { loadFeaturedList() }
            setColorSchemeColors(MaterialColors.getColor(this, R.attr.colorPrimary))
        }
        initRecyclerView()
        loadFeaturedList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_help, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val webUtils = WebUtils.getInstance(this)
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.action_refresh -> {
                binding.swipeRefreshLayout.isRefreshing = true
                loadFeaturedList()
                return true
            }

            R.id.action_send_feedback -> {
                webUtils.launchUri(Constants.uriSendFeedback)
                return true
            }

            R.id.action_version -> {
                UiUtils.getInstance(this).showVersionDialog()
                return true
            }

            R.id.action_licenses -> {
                startActivity<OssLicensesMenuActivity>()
                return true
            }

            R.id.action_source_code -> {
                webUtils.launchUri(Constants.uriSrcCode)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
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

    /**
     * Loads the featured items from the JSON file
     */
    private fun loadFeaturedList() {
        try {
            binding.apply {
                progressBarLinearLayout.visibility = View.VISIBLE
                helpFeaturedRecyclerView.visibility = View.GONE
            }
            GetHelpArticlesAsyncTask(this).execute(URL(Constants.urlHelpFeatured))
        } catch (e: Exception) {
            Log.e(TAG, "An error occurred while attempting to parse the JSON:", e)
        }

    }

    // TODO: Switch to Kotlin Coroutines
    private class GetHelpArticlesAsyncTask(activity: HelpActivity) :
        AsyncTask<URL, Void, List<HelpArticle>>() {

        // Weak reference to the current activity
        // See this great post on StackOverflow for more info (to prevent memory leaks): https://stackoverflow.com/a/46166223
        private val activityRef: WeakReference<HelpActivity> = WeakReference(activity)

        @OptIn(ExperimentalSerializationApi::class)
        override fun doInBackground(vararg urls: URL): List<HelpArticle>? {
            return Json.decodeFromStream<HelpArticles>(urls[0].openStream()).articles
        }

        override fun onPostExecute(helpArticles: List<HelpArticle>?) {
            super.onPostExecute(helpArticles)

            if (helpArticles != null) {
                // Try to get a reference to the activity
                val activity = activityRef.get()
                if (activity == null || activity.isFinishing) return
                // Update the adapter
                val adapter = HelpArticleAdapter(helpArticles)
                adapter.setOnItemClickListener { article, _ ->
                    article.uri?.let { WebUtils.getInstance(activity).launchUri(it) }
                }
                activity.binding.apply {
                    helpFeaturedRecyclerView.adapter = adapter
                    helpFeaturedRecyclerView.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                    TransitionManager.beginDelayedTransition(constraintLayout, Fade(Fade.IN))
                    progressBarLinearLayout.visibility = View.GONE
                }
            }
        }
    }
}
