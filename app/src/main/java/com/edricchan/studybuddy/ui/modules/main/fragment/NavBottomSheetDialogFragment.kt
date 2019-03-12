package com.edricchan.studybuddy.ui.modules.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.edricchan.studybuddy.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class NavBottomSheetDialogFragment : BottomSheetDialogFragment() {
	private var mNavigationViewListener: NavigationView.OnNavigationItemSelectedListener? = null

	/**
	 * Sets the listener for the [NavigationView]
	 *
	 * @param listener The listener
	 */
	fun setNavigationItemSelectedListener(listener: NavigationView.OnNavigationItemSelectedListener) {
		this.mNavigationViewListener = listener
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.frag_bottomappbar_bottomsheet, container, false)
		val navigationView = view.findViewById<NavigationView>(R.id.navigationView)
		if (mNavigationViewListener != null) {
			navigationView.setNavigationItemSelectedListener(mNavigationViewListener)
		}
		return view
	}
}
