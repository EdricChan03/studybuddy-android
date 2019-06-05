package com.edricchan.studybuddy.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManagerFix
import com.edricchan.studybuddy.utils.SharedUtils

class StartupReceiver : BroadcastReceiver() {
	private lateinit var sharedPreferences: SharedPreferences
	private val TAG = SharedUtils.getTag(this::class.java)
	override fun onReceive(context: Context, intent: Intent) {
		sharedPreferences = PreferenceManagerFix.getDefaultSharedPreferences(context)
		if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
			Log.d(TAG, "Device has booted. Setting up check for updates worker...")
			SharedUtils.enqueueCheckForUpdatesWorker(context)
		}
	}
}