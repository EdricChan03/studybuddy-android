package com.edricchan.studybuddy.ui.modules.debug

import android.os.Bundle
import android.view.MenuItem
import com.edricchan.studybuddy.extensions.replaceFragment
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.DebugSettingsFragment

/**
 * Created by edricchan on 14/3/18.
 */

class DebugActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragment(android.R.id.content, DebugSettingsFragment(), false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
