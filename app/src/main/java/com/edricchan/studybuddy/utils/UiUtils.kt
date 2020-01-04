package com.edricchan.studybuddy.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.ui.modules.main.MainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Utilities for the UI (user interface).
 * @param context The context to be used for the class.
 */
class UiUtils(val context: Context) {
	/**
	 * The [FloatingActionButton] set on the [BottomAppBar].
	 */
	val bottomAppBarFab: FloatingActionButton? = if (context is MainActivity) {
		context.findViewById(R.id.fab)
	} else {
		null
	}

	/**
	 * The [BottomAppBar] of the [MainActivity] layout.
	 */
	val bottomAppBar: BottomAppBar? = if (context is MainActivity) {
		context.findViewById(R.id.bottomAppBar)
	} else {
		null
	}

	/**
	 * Utility method to set the app's theme.
	 * Note that the activity should be recreated after this function is called in order for the theme
	 * to be applied.
	 */
	fun setAppTheme() {
		when (PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.prefDarkTheme,
				Constants.prefDarkThemeAutoBatterySaver)) {
			// Note: The old values of the preference will still be supported
			// TODO: Completely remove support for old values
			"1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			// Note: Support for setting night mode based on time is deprecated
			"2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME)
			"3" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			// New values
			Constants.prefDarkThemeAlways -> AppCompatDelegate
					.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			// No longer supported
			/* Constants.prefDarkThemeAuto, Constants.prefDarkThemeAutoTime -> AppCompatDelegate
					.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME) */
			Constants.prefDarkThemeAutoBatterySaver -> AppCompatDelegate
					.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
			Constants.prefDarkThemeFollowSystem -> AppCompatDelegate
					.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
			Constants.prefDarkThemeNever -> AppCompatDelegate
					.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			else -> Log.w(TAG, "A valid string integer (1, 2, or 3), or a valid option" +
					" (\"always\", \"automatic_battery_saver\"," +
					"\"follow_system\" or \"never\") was not used.")
		}
	}


	/**
	 * Shows a version dialog.
	 */
	fun showVersionDialog() {
		val versionDialogView = LayoutInflater.from(context).inflate(R.layout.version_dialog, null)
		val appIconImageView = versionDialogView.findViewById<ImageView>(R.id.appIconImageView)
		val appNameTextView = versionDialogView.findViewById<TextView>(R.id.appNameTextView)
		val appVersionTextView = versionDialogView.findViewById<TextView>(R.id.appVersionTextView)
		try {
			appIconImageView.setImageDrawable(context.packageManager.getApplicationIcon(BuildConfig.APPLICATION_ID))
		} catch (e: PackageManager.NameNotFoundException) {
			Log.e(TAG, "An error occurred while attempting to retrieve the app's icon:", e)
		}

		appNameTextView.text = context.applicationInfo.loadLabel(context.packageManager)
		appVersionTextView.text = BuildConfig.VERSION_NAME
		val versionDialogBuilder = MaterialAlertDialogBuilder(context)
		versionDialogBuilder
				.setView(versionDialogView)
				.show()
	}

	companion object {
		/**
		 * Retrieves an instance of the class.
		 * @param context The context to be used for the class.
		 */
		fun getInstance(context: Context) = UiUtils(context)
	}
}
