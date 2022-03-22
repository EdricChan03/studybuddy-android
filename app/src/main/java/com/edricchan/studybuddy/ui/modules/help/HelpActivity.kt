package com.edricchan.studybuddy.ui.modules.help

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.interfaces.HelpArticle
import com.edricchan.studybuddy.interfaces.HelpArticles
import com.edricchan.studybuddy.ui.modules.help.adapter.HelpArticleAdapter
import com.edricchan.studybuddy.utils.UiUtils
import com.edricchan.studybuddy.utils.WebUtils
import com.edricchan.studybuddy.utils.moshi.adapters.UriJsonAdapter
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.android.synthetic.main.activity_help.*
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL

@WebDeepLink(["/help"])
@AppDeepLink(["/help"])
class HelpActivity : AppCompatActivity(R.layout.activity_help) {
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        swipeRefreshLayout.setOnRefreshListener { this.loadFeaturedList() }
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
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
                swipeRefreshLayout.isRefreshing = true
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
        helpFeaturedRecyclerView.setHasFixedSize(true)
        helpFeaturedRecyclerView.itemAnimator = DefaultItemAnimator()
        helpFeaturedRecyclerView.layoutManager = LinearLayoutManager(this)
        helpFeaturedRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    /**
     * Loads the featured items from the JSON file
     */
    private fun loadFeaturedList() {
        try {
            progressBarLinearLayout.visibility = View.VISIBLE
            helpFeaturedRecyclerView.visibility = View.GONE
            GetHelpArticlesAsyncTask(this).execute(URL(Constants.urlHelpFeatured))
        } catch (e: Exception) {
            Log.e(TAG, "An error occurred while attempting to parse the JSON:", e)
        }

    }

    private class GetHelpArticlesAsyncTask internal constructor(activity: HelpActivity) :
        AsyncTask<URL, Void, List<HelpArticle>>() {

        // Weak reference to the current activity
        // See this great post on StackOverflow for more info (to prevent memory leaks): https://stackoverflow.com/a/46166223
        private val activityRef: WeakReference<HelpActivity> = WeakReference(activity)

        @OptIn(ExperimentalStdlibApi::class)
        override fun doInBackground(vararg urls: URL): List<HelpArticle>? {
            var reader: InputStreamReader? = null
            try {
                reader = InputStreamReader(urls[0].openStream())
            } catch (e: Exception) {
                Log.e(TAG, "An error occurred while attempting to parse the JSON file:", e)
            }

            reader?.use {
                return Moshi.Builder()
                    .add(UriJsonAdapter)
                    .build()
                    .adapter<HelpArticles>()
                    .fromJson(it.readText())
                    ?.articles
            }
            return null
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
                    WebUtils.getInstance(activity).launchUri(article.uri)
                }
                activity.helpFeaturedRecyclerView.adapter = adapter
                activity.helpFeaturedRecyclerView.visibility = View.VISIBLE
                activity.swipeRefreshLayout.isRefreshing = false
                TransitionManager.beginDelayedTransition(activity.constraintLayout!!, Fade(Fade.IN))
                activity.progressBarLinearLayout.visibility = View.GONE
            }
        }
    }
}
