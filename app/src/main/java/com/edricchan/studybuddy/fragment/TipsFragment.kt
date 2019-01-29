package com.edricchan.studybuddy.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.DebugActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.SettingsActivity
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.utils.Constants

class TipsFragment : Fragment() {
	private var mFragmentView: View? = null
	private val mTabsIntent: CustomTabsIntent? = null
	private var preferences: SharedPreferences? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
		preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.frag_tips, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mFragmentView = view
		view.findViewById<View>(R.id.tips_empty_state_cta)
				.setOnClickListener { v -> SharedUtils.launchUri(context!!, Constants.uriSubmitTip, preferences!!.getBoolean(Constants.prefUseCustomTabs, true)) }
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
		when (item.itemId) {
			R.id.action_submit_tip -> {
				SharedUtils.launchUri(context!!, Constants.uriSubmitTip, preferences!!.getBoolean(Constants.prefUseCustomTabs, true))
				return true
			}
			R.id.action_debug -> {
				val debugIntent = Intent(activity, DebugActivity::class.java)
				startActivity(debugIntent)
				return true
			}
			R.id.action_settings -> {
				val settingsIntent = Intent(activity, SettingsActivity::class.java)
				startActivity(settingsIntent)
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	companion object {
		/**
		 * The Android tag for use with [android.util.Log]
		 */
		private val TAG = SharedUtils.getTag(TipsFragment::class.java)
	}
}
