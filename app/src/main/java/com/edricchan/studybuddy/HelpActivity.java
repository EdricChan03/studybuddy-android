package com.edricchan.studybuddy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.edricchan.studybuddy.interfaces.HelpFeatured;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

	List<HelpFeatured> helpFeaturedList;
	androidx.recyclerview.widget.RecyclerView featuredRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		featuredRecyclerView = (androidx.recyclerview.widget.RecyclerView) findViewById(R.id.help_featured_recyclerview);
		featuredRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		addFeaturedItems();
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
		helpFeaturedList.add(new HelpFeatured("Article #1", "https://example.com"));
		helpFeaturedList.add(new HelpFeatured("Article #2", "https://google.com"));
		helpFeaturedList.add(new HelpFeatured("About Study Buddy", "https://github.com/Chan4077/StudyBuddy"));
	}

	/**
	 * Initialises the adapter for the recycler view
	 */
	private void initialiseAdapter() {
		HelpFeaturedAdapter adapter = new HelpFeaturedAdapter(helpFeaturedList);
		featuredRecyclerView.setAdapter(adapter);
	}

}
