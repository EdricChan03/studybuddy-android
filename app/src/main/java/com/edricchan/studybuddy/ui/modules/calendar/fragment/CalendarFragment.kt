package com.edricchan.studybuddy.ui.modules.calendar.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.edricchan.studybuddy.R

class CalendarFragment : Fragment(R.layout.frag_calendar) {
	private var fragmentView: View? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		fragmentView = view
	}
}
