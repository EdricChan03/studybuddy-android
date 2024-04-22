package com.edricchan.studybuddy.ui.modules.debug

import android.os.Bundle
import android.view.MenuItem
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityFragHostBinding
import com.edricchan.studybuddy.exts.androidx.fragment.replaceFragment
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.DebugSettingsFragment

/**
 * Created by edricchan on 14/3/18.
 */

class DebugActivity : BaseActivity() {
    private lateinit var binding: ActivityFragHostBinding

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragHostBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar)
        }.also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragment(R.id.fragmentHostContainer, DebugSettingsFragment(), false)
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
