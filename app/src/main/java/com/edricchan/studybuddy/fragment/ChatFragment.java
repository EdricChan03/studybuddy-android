package com.edricchan.studybuddy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.utils.SharedUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChatFragment extends Fragment {
	private View fragmentView;
	/**
	 * The Android tag for use with {@link android.util.Log}
	 */
	private static final String TAG = SharedUtils.Companion.getTag(ChatFragment.class);

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_chat, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentView = view;
	}
}
