package com.edricchan.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;

import com.edricchan.studybuddy.adapter.HelpArticleAdapter;
import com.edricchan.studybuddy.interfaces.HelpArticle;
import com.edricchan.studybuddy.utils.DataUtil;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

	private RecyclerView featuredRecyclerView;
	private ConstraintLayout constraintLayout;
	private LinearLayout progressBarLayout;
	private SwipeRefreshLayout swipeRefreshLayout;
	private SharedPreferences preferences;
	private static final String TAG = SharedHelper.getTag(HelpActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_help);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		featuredRecyclerView = findViewById(R.id.helpFeaturedRecyclerView);
		constraintLayout = findViewById(R.id.constraintLayout);
		progressBarLayout = findViewById(R.id.progressBar);
		swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(this::loadFeaturedList);
		swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		initRecyclerView();
		loadFeaturedList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_help, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_refresh:
				swipeRefreshLayout.setRefreshing(true);
				loadFeaturedList();
				return true;
			case R.id.action_send_feedback:
				SharedHelper.launchUri(this, DataUtil.uriSendFeedback, preferences.getBoolean(DataUtil.prefUseCustomTabs, true));
				return true;
			case R.id.action_version:
				SharedHelper.showVersionDialog(this);
				return true;
			case R.id.action_licenses:
				Intent licensesIntent = new Intent(this, OssLicensesMenuActivity.class);
				startActivity(licensesIntent);
				return true;
			case R.id.action_source_code:
				SharedHelper.launchUri(this, DataUtil.uriSrcCode, preferences.getBoolean(DataUtil.prefUseCustomTabs, true));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Initialises the recycler view by calling the needed methods
	 */
	private void initRecyclerView() {
		featuredRecyclerView.setHasFixedSize(true);
		featuredRecyclerView.setItemAnimator(new DefaultItemAnimator());
		featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		featuredRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
	}

	/**
	 * Loads the featured items from the JSON file
	 */
	private void loadFeaturedList() {
		try {
			progressBarLayout.setVisibility(View.VISIBLE);
			featuredRecyclerView.setVisibility(View.GONE);
			new GetHelpArticlesAsyncTask(this).execute(new URL(DataUtil.urlHelpFeatured));
		} catch (Exception e) {
			Log.e(TAG, "An error occurred while attempting to parse the JSON:", e);
		}
	}

	private static class GetHelpArticlesAsyncTask extends AsyncTask<URL, Void, List<HelpArticle>> {

		// Weak reference to the current activity
		// See this great post on StackOverflow for more info (to prevent memory leaks): https://stackoverflow.com/a/46166223
		private WeakReference<HelpActivity> activityRef;

		GetHelpArticlesAsyncTask(HelpActivity activity) {
			this.activityRef = new WeakReference<>(activity);
		}

		@Override
		protected List<HelpArticle> doInBackground(URL... urls) {
			InputStreamReader reader = null;
			try {
				reader = new InputStreamReader(urls[0].openStream());
			} catch (Exception e) {
				Log.e(TAG, "An error occurred while attempting to parse the JSON file:", e);
			}
			if (reader != null) {
				HelpArticle[] helpArticles = new Gson().fromJson(reader, HelpArticle[].class);
				return Arrays.asList(helpArticles);
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<HelpArticle> helpArticles) {
			super.onPostExecute(helpArticles);

			if (helpArticles != null) {
				// Try to get a reference to the activity
				HelpActivity activity = activityRef.get();
				if (activity == null || activity.isFinishing()) return;
				// Update the adapter
				HelpArticleAdapter adapter = new HelpArticleAdapter(helpArticles);
				adapter.setOnItemClickListener((article, position) -> SharedHelper.launchUri(activity, article.getArticleUri(), activity.preferences.getBoolean(DataUtil.prefUseCustomTabs, true)));
				activity.featuredRecyclerView.setAdapter(adapter);
				activity.featuredRecyclerView.setVisibility(View.VISIBLE);
				activity.swipeRefreshLayout.setRefreshing(false);
				TransitionManager.beginDelayedTransition(activity.constraintLayout, new Fade(Fade.IN));
				activity.progressBarLayout.setVisibility(View.GONE);
			}
		}
	}

}
