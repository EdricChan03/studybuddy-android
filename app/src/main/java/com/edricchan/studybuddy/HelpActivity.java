package com.edricchan.studybuddy;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edricchan.studybuddy.interfaces.HelpFeatured;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

	ArrayList<HelpFeatured> helpFeaturedList;
	ListView featuredListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		featuredListView = (ListView) findViewById(R.id.helpFeaturedListView);
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
			case R.id.action_version:

				break;
			case R.id.action_licenses:

				break;
			case R.id.action_source_code:

				break;

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Method to add featured items
	 */
	private void addFeaturedItems() {
		helpFeaturedList = new ArrayList<>();
		helpFeaturedList.add(new HelpFeatured("About Study Buddy", "https://github.com/Chan4077/StudyBuddy"));
		helpFeaturedList.add(new HelpFeatured("Getting Started", "https://github.com/Chan4077/StudyBuddy"));
		helpFeaturedList.add(new HelpFeatured("About the new todo dialog", "https://github.com/Chan4077/StudyBuddy"));
		helpFeaturedList.add(new HelpFeatured("Markdown support", "https://github.com/Chan4077/StudyBuddy"));
		helpFeaturedList.add(new HelpFeatured("Experimenting with experiments", "https://github.com/Chan4077/StudyBuddy"));
		helpFeaturedList.add(new HelpFeatured("Configuring settings", "https://github.com/Chan4077/StudyBuddy"));
		helpFeaturedList.add(new HelpFeatured("How to contribute to the project (for developers)", "https://github.com/Chan4077/StudyBuddy"));
	}

	/**
	 * Initialises the adapter for the listview
	 */
	private void initialiseAdapter() {
		HelpFeaturedAdapter adapter = new HelpFeaturedAdapter(helpFeaturedList, HelpActivity.this);
		featuredListView.setAdapter(adapter);
	}
	/**
	 * Sets a click handler on an item of the listview
	 */
	private void setupClickHandler() {
		featuredListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				featuredListView.setSelection(position);
				featuredListView.setPressed(true);
//				Log.d(SharedHelper.getTag(HelpActivity.class), "Selected: " + helpFeaturedList.get(position).helpUrl);
				CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
				builder.setToolbarColor(ContextCompat.getColor(HelpActivity.this, R.color.colorPrimary));
				builder.addDefaultShareMenuItem();
				final CustomTabsIntent customTabsIntent = builder.build();
				customTabsIntent.launchUrl(HelpActivity.this, Uri.parse(helpFeaturedList.get(position).helpUrl));
			}
		});
	}

}
