package com.test.movieapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import java.io.IOException

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

suspend fun <T> fetchWithFallback(
    apiCall: suspend () -> T,
    localCall: suspend () -> T
): T {
    return try {
        apiCall()
    } catch (ex: IOException) {
        localCall()
    } catch (ex: Exception) {
        Log.e("MovieInteractor", "Error fetching from API: ${ex.message}", ex)
        throw ex
    }
}