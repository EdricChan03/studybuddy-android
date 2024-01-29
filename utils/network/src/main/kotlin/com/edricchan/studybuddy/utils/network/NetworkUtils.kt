package com.edricchan.studybuddy.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService

enum class NetworkType(
    private val transportType: Int,
    private val compatType: Int
) {
    WiFi(NetworkCapabilities.TRANSPORT_WIFI, ConnectivityManager.TYPE_WIFI),
    Cellular(NetworkCapabilities.TRANSPORT_CELLULAR, ConnectivityManager.TYPE_MOBILE),
    Ethernet(NetworkCapabilities.TRANSPORT_ETHERNET, ConnectivityManager.TYPE_ETHERNET);

    fun isCurrentType(manager: ConnectivityManager): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork ?: return false
            val actNw = manager.getNetworkCapabilities(network) ?: return false
            actNw.hasTransport(transportType)
        } else {
            @Suppress("DEPRECATION") // Deprecated in API level 29
            manager.activeNetworkInfo?.type == compatType
        }
    }
}

/**
 * Checks if an internet connection is available, using the relevant [types].
 * @param types The [NetworkType]s to check.
 * @return True if **any** of the specified [types] returns `true` for
 * [NetworkType.isCurrentType].
 */
fun Context.isNetworkAvailable(types: Set<NetworkType> = NetworkType.entries.toSet()): Boolean {
    val manager = getSystemService<ConnectivityManager>() ?: return false

    return types.any { it.isCurrentType(manager) }
}

/**
 * Checks if the current internet connection is on a metered network.
 * @see ConnectivityManager.isActiveNetworkMetered
 */
val Context.isMeteredNetwork
    get() = getSystemService<ConnectivityManager>()?.isActiveNetworkMetered ?: false

/**
 * Checks if the current network has a connection.
 * @see android.net.NetworkInfo.isConnected
 * @see ConnectivityManager.getActiveNetworkInfo
 */
val Context.isNetworkConnected
    get() = getSystemService<ConnectivityManager>()?.activeNetworkInfo?.isConnected ?: false
