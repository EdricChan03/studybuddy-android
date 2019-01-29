package com.edricchan.studybuddy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.settings.*
import com.edricchan.studybuddy.utils.Constants
import com.edricchan.studybuddy.utils.SharedUtils

class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
	private var preferences: SharedPreferences? = null

	/**
	 * {@inheritDoc}
	 */
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		preferences = PreferenceManager.getDefaultSharedPreferences(this)
		if (savedInstanceState == null) {
			SharedUtils.replaceFragment(this, SettingsFragment(), android.R.id.content, false)
		}
	}

	/**
	 * {@inheritDoc}
	 */
	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_settings, menu)
		return true
	}

	/**
	 * {@inheritDoc}
	 */
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
			R.id.action_send_feedback -> {
				SharedUtils.launchUri(this, Constants.uriSendFeedback, preferences!!.getBoolean(Constants.prefUseCustomTabs, true))
				return true
			}
			R.id.action_help -> {
				val helpIntent = Intent(this, HelpActivity::class.java)
				startActivity(helpIntent)
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
		// Instantiate the new Fragment
		val args = pref.extras
		val fragment = supportFragmentManager.fragmentFactory.instantiate(
				classLoader,
				pref.fragment,
				args)
		if (fragment is SettingsFragment) {
			title = "Settings"
		} else if (fragment is AboutSettingsFragment) {
			title = "About"
		} else if (fragment is DebugSettingsFragment) {
			title = "Debug"
		} else if (fragment is GeneralSettingsFragment) {
			title = "General"
		} else if (fragment is SyncSettingsFragment) {
			title = "Sync"
		} else if (fragment is TodoSettingsFragment) {
			title = "Todos"
		}
		fragment.arguments = args
		fragment.setTargetFragment(caller, 0)
		// Replace the existing Fragment with the new Fragment
		SharedUtils.replaceFragment(this, fragment, android.R.id.content, true)
		return true
	}

	companion object {
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}
