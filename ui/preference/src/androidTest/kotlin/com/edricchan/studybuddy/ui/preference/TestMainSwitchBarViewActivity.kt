package com.edricchan.studybuddy.ui.preference

import androidx.activity.ComponentActivity
import com.edricchan.studybuddy.ui.preference.mainswitch.MainSwitchBarView
import com.edricchan.studybuddy.ui.preference.test.R

class TestMainSwitchBarViewActivity : ComponentActivity(R.layout.main_switch_bar_test) {
    val switchBar: MainSwitchBarView get() = findViewById(R.id.main_switch_bar_test)
}
