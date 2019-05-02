package com.edricchan.studybuddy.ui.modules.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.modules.help.HelpActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.SettingsFragment
import com.edricchan.studybuddy.utils.Constants
import com.edricchan.studybuddy.utils.SharedUtils

class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
	private var preferences: SharedPreferences? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		preferences = PreferenceManager.getDefaultSharedPreferences(this)
		if (savedInstanceState == null) {
			SharedUtils.replaceFragment(this, SettingsFragment(), android.R.id.content, false)
		} else {
			title = savedInstanceState.getCharSequence(TITLE_TAG)
		}
		supportFragmentManager.addOnBackStackChangedListener {
			if (supportFragmentManager.backStackEntryCount == 0) {
				setTitle(R.string.title_activity_settings)
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_settings, menu)
		return true
	}

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

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		// Save current activity title so we can set it again after a configuration change
		outState.putCharSequence(TITLE_TAG, title)
	}

	override fun onSupportNavigateUp(): Boolean {
		if (supportFragmentManager.popBackStackImmediate()) {
			return true
		}
		return super.onSupportNavigateUp()
	}

	override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
		// Instantiate the new Fragment
		val args = pref.extras
		val fragment = supportFragmentManager.fragmentFactory.instantiate(
				classLoader,
				pref.fragment,
				args)
				.apply {
					arguments = args
					setTargetFragment(caller, 0)
				}
		title = pref.title
		// Replace the existing Fragment with the new Fragment
		SharedUtils.replaceFragment(this, fragment, android.R.id.content, true)
		return true
	}

	companion object {
		private val TAG = SharedUtils.getTag(this::class.java)
		// The title tag used for saving the title state
		private const val TITLE_TAG = "settingsActivityTitle"
	}
}
