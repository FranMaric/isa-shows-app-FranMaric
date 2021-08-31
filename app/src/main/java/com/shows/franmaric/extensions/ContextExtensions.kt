package com.shows.franmaric.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun Context.hasInternetConnection(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}