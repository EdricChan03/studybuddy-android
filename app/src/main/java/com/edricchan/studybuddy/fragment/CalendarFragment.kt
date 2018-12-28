package com.edricchan.studybuddy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.SharedHelper

class CalendarFragment : Fragment() {
	private var fragmentView: View? = null
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.frag_calendar, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		fragmentView = view
	}

	companion object {
		/**
		 * The Android tag for use with [android.util.Log]
		 */
		private val TAG = SharedHelper.getTag(this::class.java)
	}
}
