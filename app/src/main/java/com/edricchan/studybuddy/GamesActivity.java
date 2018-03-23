package com.edricchan.studybuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class GamesActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		RecyclerView recyclerView = findViewById(R.id.games_recycler_view);

	}
}
