package com.edricchan.studybuddy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.edricchan.studybuddy.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class NavBottomSheetDialogFragment extends BottomSheetDialogFragment {
	private NavigationView.OnNavigationItemSelectedListener mNavigationViewListener;

	/**
	 * Sets the listener for the {@link NavigationView}
	 *
	 * @param listener The listener
	 */
	public void setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener listener) {
		this.mNavigationViewListener = listener;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_bottomappbar_bottomsheet, container, false);
		NavigationView navigationView = view.findViewById(R.id.navigationView);
		if (mNavigationViewListener != null) {
			navigationView.setNavigationItemSelectedListener(mNavigationViewListener);
		}
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
