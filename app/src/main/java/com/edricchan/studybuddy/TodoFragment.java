package com.edricchan.studybuddy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by edricchan on 15/3/18.
 */

public class TodoFragment extends Fragment {
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private ArrayList taskItems = new ArrayList<>();

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		// Handles swiping down to refresh logic
		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.recycler_swiperefresh);
		// Sets a refreshing listener
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mAdapter.notifyDataSetChanged();
				new android.os.Handler().postDelayed(
						new Runnable() {
							@Override
							public void run() {
								swipeRefreshLayout.setRefreshing(false);

							}
						},
						1000);
			}
		});
		mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_list);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(getContext());
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
		mAdapter = new StudyAdapter(getContext(), taskItems);
		mRecyclerView.setAdapter(mAdapter);
		return inflater.inflate(R.layout.content_main, container, false);
	}

	public TodoFragment newInstance() {
		return new TodoFragment();
	}
}
