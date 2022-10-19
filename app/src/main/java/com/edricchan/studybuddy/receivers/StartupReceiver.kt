package com.edricchan.studybuddy.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.utils.enqueueUniqueCheckForUpdatesWorker

class StartupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Device has booted. Setting up check for updates worker...")
            context.enqueueUniqueCheckForUpdatesWorker()
        }
    }
}
