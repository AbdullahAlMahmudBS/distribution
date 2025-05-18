package com.incepta.core.utils

import android.util.Log

/**
 * Created by Abdullah on 18/5/25.
 */

/**
 * This will be a singleton class that will be used to log the events in the app.
 * Can be access by using Logger.error("Error message") or Logger.info("Info message")
 */

object Logger {

    private const val TAG = "Logger"
    private const val ERROR = "ERROR"
    private const val INFO = "INFO"
    private const val DEBUG = "DEBUG"
    private const val WARNING = "WARNING"

    fun error(message: String) {
        Log.e(ERROR, message)
    }

    fun info(message: String) {
        Log.i(INFO, message)
    }

    fun debug(message: String) {
        Log.d(DEBUG, message)
    }

    fun warning(message: String) {
        Log.w(WARNING, message)
    }

    fun log(message: String) {
        Log.d(TAG, message)
    }



}