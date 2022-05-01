package com.edricchan.studybuddy.ui.modules.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.utils.ThemeUtils
import com.edricchan.studybuddy.utils.UiUtils
import com.edricchan.studybuddy.utils.themeUtils

/**
 * Base activity that all activities should implement to handle theming behaviour.
 */
abstract class BaseActivity : AppCompatActivity {
    constructor() : super()
    @Deprecated("View Binding should be used where preferable")
    constructor(@LayoutRes resId: Int) : super(resId)

    private lateinit var themeUtil: ThemeUtils
    private lateinit var uiUtil: UiUtils

    private var themeIsUsingDynamicColor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply app theming
        uiUtil = UiUtils(this)
        uiUtil.setAppTheme()

        // Apply dynamic theming
        themeUtil = themeUtils
        themeUtil.applyTheme()

        // Update dynamic colour
        themeIsUsingDynamicColor = themeUtil.isUsingDynamicColor
    }

    override fun onResume() {
        super.onResume()
        // Apply theming if dynamic colour preference is updated
        if (themeIsUsingDynamicColor != themeUtil.isUsingDynamicColor) {
            uiUtil.setAppTheme()
            themeUtil.applyTheme()
            themeIsUsingDynamicColor = themeUtil.isUsingDynamicColor
            recreate()
        }
    }
}
