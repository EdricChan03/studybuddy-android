package com.edricchan.studybuddy.ui.modules.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityFragHostBinding
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.androidx.fragment.replaceFragment
import com.edricchan.studybuddy.features.help.HelpActivity
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.SettingsFragment
import com.edricchan.studybuddy.utils.web.launchUri

@WebDeepLink(["/settings"])
@AppDeepLink(["/settings"])
class SettingsActivity : BaseActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    private lateinit var binding: ActivityFragHostBinding

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragHostBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar.apply {
                setTitle(R.string.title_activity_settings)
            })
        }.also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_send_feedback -> {
                launchUri(Constants.uriSendFeedback)
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
            pref.fragment!!
        )
            .apply {
                arguments = args
                @Suppress("DEPRECATION") // See https://issuetracker.google.com/issues/212905758
                setTargetFragment(caller, 0)
            }
        title = pref.title
        // Replace the existing Fragment with the new Fragment
        setCurrentFragment(fragment)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        // FIXME: Animations are disabled here for now as the fade through behaviour looks a bit odd
        replaceFragment(R.id.fragmentHostContainer, fragment, addToBackStack, hasAnimations = false)
    }

    companion object {
        // The title tag used for saving the title state
        private const val TITLE_TAG = "settingsActivityTitle"
    }
}
