package com.edricchan.studybuddy;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.edricchan.studybuddy.interfaces.HelpArticle;
import com.edricchan.studybuddy.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HelpActivity extends AppCompatActivity {

	private List<HelpArticle> helpArticleList;
	private ListView featuredListView;
	private CustomTabsIntent tabsIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_help);
		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
		featuredListView = findViewById(R.id.helpFeaturedListView);
		CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
		builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
				.addDefaultShareMenuItem()
				.setShowTitle(true);
		tabsIntent = builder.build();
		addFeaturedItems();
		initialiseAdapter();
		setupClickHandler();
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

				return true;
			case R.id.action_source_code:
				tabsIntent.launchUrl(this, DataUtil.uriSrcCode);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Adds featured items
	 */
	private void addFeaturedItems() {
		helpArticleList = new ArrayList<>();
		helpArticleList.add(new HelpArticle("About Study Buddy", "https://github.com/Chan4077/StudyBuddy"));
		helpArticleList.add(new HelpArticle("Getting Started", "https://github.com/Chan4077/StudyBuddy"));
		helpArticleList.add(new HelpArticle("About the new todo dialog", "https://github.com/Chan4077/StudyBuddy"));
		helpArticleList.add(new HelpArticle("Markdown support", "https://github.com/Chan4077/StudyBuddy"));
		helpArticleList.add(new HelpArticle("Experimenting with experiments", "https://github.com/Chan4077/StudyBuddy"));
		helpArticleList.add(new HelpArticle("Configuring settings", "https://github.com/Chan4077/StudyBuddy"));
		helpArticleList.add(new HelpArticle("How to contribute to the project (for developers)", "https://github.com/Chan4077/StudyBuddy"));
	}

	/**
	 * Initialises the adapter for the listview
	 * TODO: Use a RecyclerView instead of the now deprecated ListView
	 */
	private void initialiseAdapter() {
		HelpFeaturedAdapter adapter = new HelpFeaturedAdapter(helpArticleList, HelpActivity.this);
		featuredListView.setAdapter(adapter);
	}

	/**
	 * Sets a click handler on an item of the listview
	 */
	private void setupClickHandler() {
		featuredListView.setOnItemClickListener((parent, view, position, id) -> {
			featuredListView.setSelection(position);
			featuredListView.setPressed(true);
//				Log.d(SharedHelper.getTag(HelpActivity.class), "Selected: " + helpArticleList.get(position).helpUrl);
			tabsIntent.launchUrl(this, helpArticleList.get(position).getArticleUri());
		});
	}

}
