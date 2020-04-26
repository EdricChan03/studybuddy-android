package com.edricchan.studybuddy.ui.modules.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.help.HelpActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.SettingsFragment
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.utils.WebUtils

@WebDeepLink(["/settings"])
@AppDeepLink(["/settings"])
class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (savedInstanceState == null) {
            setCurrentFragment(SettingsFragment(), /* addToBackStack = */ false)
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
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_send_feedback -> {
                WebUtils.getInstance(this).launchUri(Constants.uriSendFeedback)
                true
            }
            R.id.action_help -> {
                startActivity<HelpActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                pref.fragment
            )
            .apply {
                arguments = args
                setTargetFragment(caller, 0)
            }
        title = pref.title
        // Replace the existing Fragment with the new Fragment
        setCurrentFragment(fragment)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        SharedUtils.replaceFragment(this, fragment, android.R.id.content, addToBackStack)
    }

    companion object {
        // The title tag used for saving the title state
        private const val TITLE_TAG = "settingsActivityTitle"
    }
}
