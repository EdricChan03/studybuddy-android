package com.edricchan.studybuddy.ui.modules.calendar.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.ContentView
import androidx.fragment.app.Fragment
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.SharedUtils

@ContentView(R.layout.frag_calendar)
class CalendarFragment : Fragment() {
	private var fragmentView: View? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		fragmentView = view
	}

	companion object {
		/**
		 * The Android tag for use with [android.util.Log]
		 */
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}
