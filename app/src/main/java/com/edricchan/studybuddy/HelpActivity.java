package com.edricchan.studybuddy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edricchan.studybuddy.adapter.HelpArticleAdapter;
import com.edricchan.studybuddy.interfaces.HelpArticle;
import com.edricchan.studybuddy.utils.DataUtil;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HelpActivity extends AppCompatActivity {

	private List<HelpArticle> helpArticleList;
	private RecyclerView featuredRecyclerView;
	private CustomTabsIntent tabsIntent;
	private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
	private static final String TAG = SharedHelper.getTag(HelpActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_help);
		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
		featuredRecyclerView = findViewById(R.id.helpFeaturedRecyclerView);
		CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
		builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
				.addDefaultShareMenuItem()
				.setShowTitle(true);
		tabsIntent = builder.build();
		initRecyclerView();
		parseFeaturedList();
		initialiseAdapter();
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
			case R.id.action_version:

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
	}

	/**
	 * Adds featured items
	 */
	private void parseFeaturedList() {
		try {
			URL url = new URL(DataUtil.urlHelpFeatured);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			HelpArticle[] helpArticles = new Gson().fromJson(reader, HelpArticle[].class);
			helpArticleList = Arrays.asList(helpArticles);
		} catch (Exception e) {
			Log.e(TAG, "An error occurred while attempting to parse the JSON:", e);
		}
	}

	/**
	 * Initialises the adapter for the listview
	 * TODO: Use a RecyclerView instead of the now deprecated ListView
	 */
	private void initialiseAdapter() {
		HelpArticleAdapter adapter = new HelpArticleAdapter(helpArticleList);
		adapter.setOnItemClickListener((article, position) -> SharedHelper.launchUri(this, article.getArticleUri(), preferences.getBoolean(DataUtil.prefUseCustomTabs, true)));
		featuredRecyclerView.setAdapter(adapter);
	}

}
