package com.edricchan.studybuddy.ui.modules.debug

import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity

import com.edricchan.studybuddy.ui.modules.settings.fragment.DebugSettingsFragment
import com.edricchan.studybuddy.utils.SharedUtils

/**
 * Created by edricchan on 14/3/18.
 */

class DebugActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        SharedUtils.replaceFragment(this, DebugSettingsFragment(), android.R.id.content, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
