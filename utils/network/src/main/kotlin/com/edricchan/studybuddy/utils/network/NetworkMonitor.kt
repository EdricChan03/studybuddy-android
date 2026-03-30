package com.edricchan.studybuddy.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED
import android.net.NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Adapted from
// https://github.com/f-droid/fdroidclient/blob/de69003c5d3b19d89c079d207d2388c1f25ca1ba/app/src/main/kotlin/org/fdroid/download/NetworkMonitor.kt

/**
 * Class that monitors the [requested network][networkRequest] for changes in its
 * [network state][NetworkState].
 *
 * The current network [StateFlow] can be accessed via the [networkState].
 */
class NetworkMonitor(
    context: Context,
    private val networkRequest: NetworkRequest
) : ConnectivityManager.NetworkCallback(), DefaultLifecycleObserver {

    private val connectivityManager =
        requireNotNull(context.getSystemService<ConnectivityManager>())

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        connectivityManager.unregisterNetworkCallback(this)
    }

    private val _networkState =
        MutableStateFlow(
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.let(::NetworkState)
                ?: NetworkState(isOnline = false, isMetered = false)
        )
    val networkState = _networkState.asStateFlow()

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        _networkState.update { NetworkState(networkCapabilities = networkCapabilities) }
    }

    override fun onLost(network: Network) {
        _networkState.update { NetworkState(isOnline = false, isMetered = false) }
    }
}

data class NetworkState(val isOnline: Boolean, val isMetered: Boolean) {
    constructor(
        networkCapabilities: NetworkCapabilities
    ) : this(
        isOnline = networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET),
        isMetered = !networkCapabilities.hasCapability(NET_CAPABILITY_NOT_METERED),
    )
}

/**
 * Gets a [StateFlow] of the network's [NetworkState] that matches the given [request].
 * @param owner [LifecycleOwner] to automatically register/unregister the underlying
 * [ConnectivityManager.NetworkCallback] under.
 * @param request [NetworkRequest] to filter the [ConnectivityManager.registerNetworkCallback]
 * call by.
 */
fun Context.observeNetworkState(
    owner: LifecycleOwner,
    request: NetworkRequest
): StateFlow<NetworkState> {
    val monitor = NetworkMonitor(this, request)

    owner.lifecycle.addObserver(monitor)
    return monitor.networkState
}

/**
 * Gets a [StateFlow] of a network's [NetworkState] that has the given [requestInit]
 * options.
 * @param owner [LifecycleOwner] to automatically register/unregister the underlying
 * [ConnectivityManager.NetworkCallback] under.
 * @param requestInit Options to be passed to [networkRequest], to filter the
 * [ConnectivityManager.registerNetworkCallback] call by.
 */
fun Context.observeNetworkState(
    owner: LifecycleOwner,
    requestInit: NetworkRequest.Builder.() -> Unit
): StateFlow<NetworkState> =
    observeNetworkState(owner = owner, request = networkRequest(requestInit))

/**
 * Gets a [StateFlow] of a network's [NetworkState] that has the
 * [NET_CAPABILITY_INTERNET] and [NET_CAPABILITY_NOT_RESTRICTED] capabilities.
 * @param owner [LifecycleOwner] to automatically register/unregister the underlying
 * [ConnectivityManager.NetworkCallback] under.
 */
fun Context.observeNetworkState(
    owner: LifecycleOwner
): StateFlow<NetworkState> = observeNetworkState(owner = owner) {
    addCapability(NET_CAPABILITY_INTERNET)
    addCapability(NET_CAPABILITY_NOT_RESTRICTED)
}

/**
 * Gets a [StateFlow] of a network's [NetworkState] that has the
 * [NET_CAPABILITY_INTERNET], and [NET_CAPABILITY_NOT_METERED] and
 * [NET_CAPABILITY_NOT_RESTRICTED] capabilities.
 * @param owner [LifecycleOwner] to automatically register/unregister the underlying
 * [ConnectivityManager.NetworkCallback] under.
 * @see observeNetworkState
 */
fun Context.observeUnmeteredNetworkState(
    owner: LifecycleOwner
): StateFlow<NetworkState> = observeNetworkState(owner = owner) {
    addCapability(NET_CAPABILITY_INTERNET)
    addCapability(NET_CAPABILITY_NOT_METERED)
    addCapability(NET_CAPABILITY_NOT_RESTRICTED)
}
