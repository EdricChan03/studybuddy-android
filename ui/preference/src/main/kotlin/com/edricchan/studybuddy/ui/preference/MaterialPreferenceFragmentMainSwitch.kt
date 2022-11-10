package com.edricchan.studybuddy.ui.preference

import android.content.Context
import android.os.Bundle
import com.edricchan.studybuddy.ui.preference.mainswitch.MainSwitchPreference

/**
 * A [androidx.preference.PreferenceFragmentCompat] that displays a main switch.
 *
 * **Implementation note:** Subclasses should call [addPreferencesFromResource] to add preferences
 * to the fragment, instead of [setPreferencesFromResource] such that the main switch is not
 * overridden.
 * Additionally, subclasses should also call the superclass' [onCreatePreferences] for the main
 * switch to be added.
 */
abstract class MaterialPreferenceFragmentMainSwitch : MaterialPreferenceFragment() {
    /**
     * Creates the main switch using the given [context].
     *
     * **Implementation note:** Subclasses should extend this method to create the main switch.
     * @param context The context to use.
     */
    abstract fun createMainSwitch(context: Context): MainSwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val context = preferenceManager.context
        preferenceScreen = preferenceManager.createPreferenceScreen(context).apply {
            addPreference(createMainSwitch(context))
        }
    }
}
