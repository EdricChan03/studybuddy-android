package com.edricchan.studybuddy.ui.modules.tips.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.utils.SharedUtils

class TipsFragment : Fragment(R.layout.frag_tips) {
	private var mFragmentView: View? = null
	private val mTabsIntent: CustomTabsIntent? = null
	private var preferences: SharedPreferences? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
		preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mFragmentView = view
		view.findViewById<View>(R.id.tips_empty_state_cta)
				.setOnClickListener { SharedUtils.launchUri(requireContext(), Constants.uriSubmitTip) }
	}


	override fun onPrepareOptionsMenu(menu: Menu) {
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		// Clear the main activity's menu before inflating the fragment's menu
		menu.clear()
		inflater.inflate(R.menu.menu_frag_tips, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			/*R.id.action_submit_tip -> {
				SharedUtils.launchUri(context!!, Constants.uriSubmitTip)
				true
			}*/
			R.id.action_debug -> {
				startActivity<DebugActivity>()
				true
			}
			R.id.action_settings -> {
				startActivity<SettingsActivity>()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}
