package com.core.utils

import android.content.Context
import android.util.Log
import com.android.inputmethod.latin.BuildConfig


import timber.log.Timber
import java.util.*

object AppLogger {

    private var enableTimberFileLogger_ = false

    val enableTimberFileLogger: Boolean
        get() = enableTimberFileLogger_

    fun initializeLogging(
        context: Context,
        logsFolder: String,
        enableTimberFileLogger: Boolean
    ) {
        enableTimberFileLogger_ = enableTimberFileLogger
        if (BuildConfig.DEBUG) {
            if (enableTimberFileLogger) {
                Timber.plant(
                    Timber.DebugTree(),
                    FileLoggingTree(context, logsFolder, logLevel = Log.DEBUG)
                )
            } else {
                Timber.plant(Timber.DebugTree())
            }
        } else if (enableTimberFileLogger) {
            Timber.plant(FileLoggingTree(context, logsFolder, logLevel = Log.DEBUG))
        }
    }

    fun d(
        tag: String,
        message: String,
        vararg params: Any
    ) {
        try {
            Timber.tag(tag).d(message, params)
        } catch (e: UnknownFormatConversionException) {
            Timber.e("I can not log this debug message due to UnknownFormatConversionException")
        } catch (e: FormatFlagsConversionMismatchException) {
            Timber.e("I can not log this debug message due to FormatFlagsConversionMismatchException")
        } catch (e: IllegalFormatConversionException) {
            Timber.e("I can not log this debug message due to IllegalFormatConversionException")
        } catch (e: MissingFormatArgumentException) {
            Timber.e("I can not log this debug message due to MissingFormatArgumentException")
        } catch (e: Exception) {
            Timber.e("I can not log this debug message due to generic exception: ${e.localizedMessage}")
        }
    }

    fun i(
        tag: String,
        message: String,
        vararg params: Any
    ) {
        try {
            Timber.tag(tag).i(message, params)
        } catch (e: UnknownFormatConversionException) {
            Timber.e("I can not log this info message due to UnknownFormatConversionException")
        } catch (e: FormatFlagsConversionMismatchException) {
            Timber.e("I can not log this info message due to FormatFlagsConversionMismatchException")
        } catch (e: IllegalFormatConversionException) {
            Timber.e("I can not log this info message due to IllegalFormatConversionException")
        } catch (e: MissingFormatArgumentException) {
            Timber.e("I can not log this debug message due to MissingFormatArgumentException")
        } catch (e: Exception) {
            Timber.e("I can not log this debug message due to generic exception: ${e.localizedMessage}")
        }
    }

    fun v(
        tag: String,
        message: String,
        vararg params: Any
    ) {
        try {
            Timber.tag(tag).v(message, params)
        } catch (e: UnknownFormatConversionException) {
            Timber.e("I can not log verbose message due to UnknownFormatConversionException")
        } catch (e: FormatFlagsConversionMismatchException) {
            Timber.e("I can not log verbose message due to FormatFlagsConversionMismatchException")
        } catch (e: IllegalFormatConversionException) {
            Timber.e("I can not log this verbose message due to IllegalFormatConversionException")
        } catch (e: MissingFormatArgumentException) {
            Timber.e("I can not log this verbose message due to MissingFormatArgumentException")
        } catch (e: Exception) {
            Timber.e("I can not log this verbose message due to generic exception: ${e.localizedMessage}")
        }
    }

    fun e(
        tag: String,
        message: String,
        vararg params: Any
    ) {
        try {
            Timber.tag(tag).e(message, params)
        } catch (e: UnknownFormatConversionException) {
            Timber.e("I can not log this error message due to UnknownFormatConversionException")
        } catch (e: FormatFlagsConversionMismatchException) {
            Timber.e("I can not log this error message due to FormatFlagsConversionMismatchException")
        } catch (e: IllegalFormatConversionException) {
            Timber.e("I can not log this error message due to IllegalFormatConversionException")
        } catch (e: MissingFormatArgumentException) {
            Timber.e("I can not log this error message due to MissingFormatArgumentException")
        } catch (e: Exception) {
            Timber.e("I can not log this error message due to generic exception: ${e.localizedMessage}")
        }
    }

    fun w(
        tag: String,
        message: String,
        vararg params: Any
    ) {
        try {
            Timber.tag(tag).w(message, params)
        } catch (e: UnknownFormatConversionException) {
            Timber.e("I can not log this WTF (What a terrible failure) message due to UnknownFormatConversionException")
        } catch (e: FormatFlagsConversionMismatchException) {
            Timber.e("I can not log this WTF message due to FormatFlagsConversionMismatchException")
        } catch (e: IllegalFormatConversionException) {
            Timber.e("I can not log this WTF message due to IllegalFormatConversionException")
        } catch (e: MissingFormatArgumentException) {
            Timber.e("I can not log this debug message due to MissingFormatArgumentException")
        } catch (e: Exception) {
            Timber.e("I can not log this error message due to generic exception: ${e.localizedMessage}")
        }
    }

}