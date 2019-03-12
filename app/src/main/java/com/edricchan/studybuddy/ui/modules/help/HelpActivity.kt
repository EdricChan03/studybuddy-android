package com.edricchan.studybuddy.ui.modules.help

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.interfaces.HelpArticle
import com.edricchan.studybuddy.interfaces.HelpArticles
import com.edricchan.studybuddy.ui.adapter.HelpArticleAdapter
import com.edricchan.studybuddy.utils.Constants
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.gson.Gson
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL

class HelpActivity : AppCompatActivity() {

	private var featuredRecyclerView: RecyclerView? = null
	private var constraintLayout: ConstraintLayout? = null
	private var progressBarLayout: LinearLayout? = null
	private var swipeRefreshLayout: SwipeRefreshLayout? = null
	private var preferences: SharedPreferences? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		setContentView(R.layout.activity_help)
		preferences = PreferenceManager.getDefaultSharedPreferences(this)
		featuredRecyclerView = findViewById(R.id.helpFeaturedRecyclerView)
		constraintLayout = findViewById(R.id.constraintLayout)
		progressBarLayout = findViewById(R.id.progressBar)
		swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
		swipeRefreshLayout!!.setOnRefreshListener { this.loadFeaturedList() }
		swipeRefreshLayout!!.setColorSchemeResources(R.color.colorPrimary)
		initRecyclerView()
		loadFeaturedList()
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_help, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
			R.id.action_refresh -> {
				swipeRefreshLayout!!.isRefreshing = true
				loadFeaturedList()
				return true
			}
			R.id.action_send_feedback -> {
				SharedUtils.launchUri(this, Constants.uriSendFeedback, preferences!!.getBoolean(Constants.prefUseCustomTabs, true))
				return true
			}
			R.id.action_version -> {
				SharedUtils.showVersionDialog(this)
				return true
			}
			R.id.action_licenses -> {
				val licensesIntent = Intent(this, OssLicensesMenuActivity::class.java)
				startActivity(licensesIntent)
				return true
			}
			R.id.action_source_code -> {
				SharedUtils.launchUri(this, Constants.uriSrcCode, preferences!!.getBoolean(Constants.prefUseCustomTabs, true))
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	/**
	 * Initialises the recycler view by calling the needed methods
	 */
	private fun initRecyclerView() {
		featuredRecyclerView!!.setHasFixedSize(true)
		featuredRecyclerView!!.itemAnimator = DefaultItemAnimator()
		featuredRecyclerView!!.layoutManager = LinearLayoutManager(this)
		featuredRecyclerView!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
	}

	/**
	 * Loads the featured items from the JSON file
	 */
	private fun loadFeaturedList() {
		try {
			progressBarLayout!!.visibility = View.VISIBLE
			featuredRecyclerView!!.visibility = View.GONE
			GetHelpArticlesAsyncTask(this).execute(URL(Constants.urlHelpFeatured))
		} catch (e: Exception) {
			Log.e(TAG, "An error occurred while attempting to parse the JSON:", e)
		}

	}

	private class GetHelpArticlesAsyncTask internal constructor(activity: HelpActivity) : AsyncTask<URL, Void, List<HelpArticle>>() {

		// Weak reference to the current activity
		// See this great post on StackOverflow for more info (to prevent memory leaks): https://stackoverflow.com/a/46166223
		private val activityRef: WeakReference<HelpActivity> = WeakReference(activity)

		override fun doInBackground(vararg urls: URL): List<HelpArticle>? {
			var reader: InputStreamReader? = null
			try {
				reader = InputStreamReader(urls[0].openStream())
			} catch (e: Exception) {
				Log.e(TAG, "An error occurred while attempting to parse the JSON file:", e)
			}

			if (reader != null) {
				val (_, _, articles) = Gson().fromJson(reader, HelpArticles::class.java)
				if (articles != null) {
					return articles.asList()
				} else {
					return null
				}
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
				adapter.onItemClickListener = object : HelpArticleAdapter.OnItemClickListener {
					override fun onItemClick(article: HelpArticle, position: Int) {
						SharedUtils.launchUri(activity, article.getArticleUri(), activity.preferences!!.getBoolean(Constants.prefUseCustomTabs, true))
					}
				}
				activity.featuredRecyclerView!!.adapter = adapter
				activity.featuredRecyclerView!!.visibility = View.VISIBLE
				activity.swipeRefreshLayout!!.isRefreshing = false
				TransitionManager.beginDelayedTransition(activity.constraintLayout!!, Fade(Fade.IN))
				activity.progressBarLayout!!.visibility = View.GONE
			}
		}
	}

	companion object {
		private val TAG = SharedUtils.getTag(HelpActivity::class.java)
	}

}
