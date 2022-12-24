package com.edricchan.studybuddy.ui.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.ui.theming.applyDarkTheme
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import com.edricchan.studybuddy.ui.theming.prefDynamicTheme

/**
 * Base activity that all activities should implement to handle theming behaviour.
 */
abstract class BaseActivity : AppCompatActivity {
    constructor() : super()

    private var themeIsUsingDynamicColor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply app theming
        applyDarkTheme()

        // Apply dynamic theming
        applyDynamicTheme()

        // Update dynamic colour
        themeIsUsingDynamicColor = prefDynamicTheme
    }

    override fun onResume() {
        super.onResume()
        // Apply theming if dynamic colour preference is updated
        if (themeIsUsingDynamicColor != prefDynamicTheme) {
            applyDarkTheme()
            applyDynamicTheme()
            themeIsUsingDynamicColor = prefDynamicTheme
            recreate()
        }
    }
}
