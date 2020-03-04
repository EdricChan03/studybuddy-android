package com.edricchan.studybuddy.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

/**
 * Utility class for network-related functionality.
 * @param context The context.
 */
class NetworkUtils(
    private val context: Context
) {
    /**
     * Whether the device is online.
     */
    fun isOnline(): Boolean {
        val cm = context.getSystemService<ConnectivityManager>()
        val netInfo = cm?.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    companion object {
        /**
         * Creates a new instance of the [NetworkUtils] class.
         */
        fun getInstance(context: Context) = NetworkUtils(context)
    }
}