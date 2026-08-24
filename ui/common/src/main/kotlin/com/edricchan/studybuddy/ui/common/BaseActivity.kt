package com.edricchan.studybuddy.ui.common

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.ui.theming.applyDarkTheme
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import com.edricchan.studybuddy.ui.theming.common.AppearancePreferences
import com.edricchan.studybuddy.ui.theming.common.night.isDarkThemeEnabled
import com.edricchan.studybuddy.ui.theming.common.night.shouldApplyDarkTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Base activity that all activities should implement to handle theming behaviour.
 *
 * Note that classes inheriting from this base class **must** be annotated with
 * [dagger.hilt.android.AndroidEntryPoint]
 */
abstract class BaseActivity : AppCompatActivity() {
    /** [AppearancePreferences] object for this activity. */
    @Inject
    lateinit var appearancePreferences: AppearancePreferences

    /**
     * Whether the content should be displayed edge-to-edge.
     *
     * Defaults to `false`; subclasses must explicitly opt in to this behaviour.
     */
    open val isEdgeToEdgeEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply edge-to-edge
        if (isEdgeToEdgeEnabled) {
            WindowCompat.enableEdgeToEdge(window)
        }

        // Apply app theming
        lifecycleScope.launch {
            appearancePreferences.darkModeFlow.flowWithLifecycle(lifecycle)
                .collectLatest {
                    Log.d(TAG, "onCreate: Current dark mode setting: $it")
                    val isDarkTheme = shouldApplyDarkTheme(
                        it,
                        isDarkThemeEnabled
                    )
                    applyDarkTheme(mode = it)
                    if (isEdgeToEdgeEnabled) WindowCompat.getInsetsController(
                        window,
                        window.decorView
                    ).isAppearanceLightStatusBars = !isDarkTheme
                }
        }
        lifecycleScope.launch {
            appearancePreferences.isMonetTheme.flowWithLifecycle(lifecycle)
                .collectLatest {
                    Log.d(TAG, "onCreate: Is Monet theme: $it")
                    applyDynamicTheme(shouldApply = it)
                }
        }
    }

    private companion object {
        private const val TAG = "MainActivity"
    }
}
