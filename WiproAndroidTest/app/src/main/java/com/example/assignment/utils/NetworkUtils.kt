/*
 * NetworkUtil.java
 * Callisto 2.0
 *
 * Copyright © 2019 Eli Lilly and Company. All rights reserved.
 *
 */
package com.example.assignment.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * This { NetworkUtil} class is a utility class for network
 */
object NetworkUtils {
    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        return false
    }
}