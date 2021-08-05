package com.shows.franmaric.extensions

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Context.hasInternetConnection(): Boolean {
    val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        return cm.activeNetworkInfo != null

    } else {
        for (network in cm.allNetworks) { // added in API 21 (Lollipop)
            val networkCapabilities: NetworkCapabilities? =
                cm.getNetworkCapabilities(network)
            return (networkCapabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)))
        }
    }
    return false
}