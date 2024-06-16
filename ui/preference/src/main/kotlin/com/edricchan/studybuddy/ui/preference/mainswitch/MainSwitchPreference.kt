/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edricchan.studybuddy.ui.preference.mainswitch

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.withStyledAttributes
import androidx.preference.PreferenceViewHolder
import androidx.preference.TwoStatePreference
import com.edricchan.studybuddy.ui.preference.R

/**
 * MainSwitchPreference is a Preference with a customized Switch.
 * This component is used as the main switch of the page
 * to enable or disable the preferences on the page.
 */
class MainSwitchPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : TwoStatePreference(context, attrs, defStyleAttr, defStyleRes), OnMainSwitchChangeListener {
    private lateinit var mainSwitchBar: MainSwitchBarCompat

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.isDividerAllowedAbove = false
        holder.isDividerAllowedBelow = false
        mainSwitchBar = holder.findViewById(R.id.m3_main_switch_bar) as MainSwitchBarCompat
        updateStatus(isChecked)
        // Set title on the switch bar
        mainSwitchBar.title = title
        mainSwitchBar.addOnSwitchChangeListener(this)
    }

    init {
        layoutResource = R.layout.pref_m3_main_switch_layout
        if (attrs != null) {
            context.withStyledAttributes(
                attrs,
                androidx.preference.R.styleable.Preference, 0 /*defStyleAttr*/,
                0 /*defStyleRes*/
            ) {
                title = getText(
                    androidx.preference.R.styleable.Preference_title
                ) ?: getText(androidx.preference.R.styleable.Preference_android_title)
            }
        }
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if (::mainSwitchBar.isInitialized && mainSwitchBar.isChecked != checked) {
            mainSwitchBar.isChecked = checked
        }
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        if (::mainSwitchBar.isInitialized) mainSwitchBar.title = title
    }

    override fun onSwitchChanged(switchView: SwitchCompat?, isChecked: Boolean) {
        super.setChecked(isChecked)
    }

    /**
     * Update the switch status of preference
     */
    fun updateStatus(checked: Boolean) {
        isChecked = checked
        mainSwitchBar.show()
    }

    /**
     * Adds a listener for switch changes
     */
    fun addOnSwitchChangeListener(listener: OnMainSwitchChangeListener) {
        mainSwitchBar.addOnSwitchChangeListener(listener)
    }

    /**
     * Remove a listener for switch changes
     */
    fun removeOnSwitchChangeListener(listener: OnMainSwitchChangeListener) {
        mainSwitchBar.removeOnSwitchChangeListener(listener)
    }
}
