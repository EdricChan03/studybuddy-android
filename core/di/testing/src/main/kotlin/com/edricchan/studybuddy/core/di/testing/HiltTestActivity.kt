package com.edricchan.studybuddy.core.di.testing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.ui.theming.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(
            intent.getIntExtra(
                THEME_EXTRAS_BUNDLE_KEY,
                R.style.Theme_App,
            )
        )

        super.onCreate(savedInstanceState)
    }

    companion object {
        const val THEME_EXTRAS_BUNDLE_KEY: String =
            "com.edricchan.studybuddy.core.di.testing.HiltTestActivity.THEME_EXTRAS"
    }
}
