package com.edricchan.studybuddy.ui.modules.debug

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.activity
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.databinding.ActivityFragNavHostBinding
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.settings.fragment.featureflags.FeatureFlagsSettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DebugActivity : BaseActivity() {
    private lateinit var binding: ActivityFragNavHostBinding

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragNavHostBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar)
        }.also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updatePadding(
                left = insets.left,
                right = insets.right
            )

            windowInsets
        }

        val navHost = binding.fragmentHostContainer.getFragment<NavHostFragment>()
        navHost.apply {
            navController.graph = navController.createGraph(CompatDestination.Debug) {
                // DebugFragment only has a feature flag and bottom sheet destination, so
                // we should be fine with just defining 3 destinations here
                fragment<DebugFragment, CompatDestination.Debug> {
                    label = getString(R.string.title_activity_debug)
                }
                fragment<FeatureFlagsSettingsFragment, CompatDestination.FeatureFlagsList> {
                    label = getString(R.string.debug_activity_feature_flags_title)
                }
                activity<CompatDestination.DebugModalBottomSheet> {
                    activityClass = DebugModalBottomSheetActivity::class
                }
            }
        }
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
