package com.edricchan.studybuddy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.edricchan.studybuddy.BuildConfig;
import com.edricchan.studybuddy.DebugActivity;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SettingsActivity;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.utils.DataUtil;

public class TipsFragment extends Fragment {
	private View mFragmentView;
	private CustomTabsIntent mTabsIntent;
	/**
	 * The Android tag for use with {@link android.util.Log}
	 */
	private static final String TAG = SharedHelper.getTag(TipsFragment.class);


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_tips, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mFragmentView = view;
		CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
		builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
				.addDefaultShareMenuItem()
				.setShowTitle(true);
		mTabsIntent = builder.build();
		view.findViewById(R.id.tips_empty_state_cta)
				.setOnClickListener(v -> mTabsIntent.launchUrl(getContext(), DataUtil.uriSubmitTip));
	}


	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug);
		}
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		// Clear the main activity's menu before inflating the fragment's menu
		menu.clear();
		inflater.inflate(R.menu.menu_frag_tips, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_submit_tip:
				mTabsIntent.launchUrl(getContext(), DataUtil.uriSubmitTip);
				return true;
			case R.id.action_debug:
				Intent debugIntent = new Intent(getActivity(), DebugActivity.class);
				startActivity(debugIntent);
				return true;
			case R.id.action_settings:
				Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
				startActivity(settingsIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
