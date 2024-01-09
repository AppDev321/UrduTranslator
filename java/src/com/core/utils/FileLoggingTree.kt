package com.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

/*
* Created by Ergyun Syuleyman on 10.06.23.
*/

class FileLoggingTree(private val context: Context,
                      private val logsFolder: String,
                      private val filterByTags: Boolean = false,
                      private val logLevel: Int = Log.VERBOSE) : Timber.DebugTree() {
    private val fileName: String
    private var logFile: File? = null
    private var initialized = false
    private var fileOutputStream: OutputStream? = null
    private val allowedTagsFilter: HashSet<String>

    init {
        val fileNameTimeStamp =
            SimpleDateFormat("MM_dd_HH_mm_ss", Locale.getDefault()).format(Date())
        fileName = "app_log_$fileNameTimeStamp.log"
        allowedTagsFilter = if (filterByTags) {
            hashSetOf(CALL_SIGNALING, CONF_SIGNALING, SIGNALINGS, JITSI_CONF)
        } else {
            hashSetOf()
        }
        init()
    }

    companion object {
        private val TAG: String = FileLoggingTree::class.java.getSimpleName()
        const val CALL_SIGNALING = "CALL_SIGNALING"
        const val CONF_SIGNALING = "CONF_SIGNALING"
        const val SIGNALINGS = "SIGNALINGS"
        const val JITSI_CONF = "JitsiMeetSDK"
    }

    @SuppressLint("LogNotTimber")
    @Synchronized
    fun init(): Boolean {
        if (initialized) return true
        try {
            logFile = File(logsFolder + File.separator
                    + fileName)
            Log.d(
                TAG,
                "FileLoggingTree:fileName = $fileName"
            )
            if (!logFile!!.exists()) {
                logFile!!.createNewFile()
            }
            fileOutputStream = FileOutputStream(logFile, true)
            Log.d(TAG, "FileLoggingTree:log file full path = " + logFile!!.absolutePath)
            initialized = true
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Error while creating logging file : $e"
            )
            initialized = false
        }
        return initialized
    }

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return if (priority < logLevel) false else super.isLoggable(tag, priority)
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < logLevel) return
        if (!init()) return
        if (filterByTags) {
            // filter by Tag & priority >= ERROR
            if (!allowedTagsFilter.contains(tag)
                //&& (tag != JITSI_CONF || priority < Log.ERROR)
            )
                return
        }
        try {
            if (logFile!!.exists()) {
                val logTimeStamp =
                    SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(
                        Date()
                    )
                fileOutputStream?.write(
                    """${getLogChar(priority)} $logTimeStamp $tag : $message
    """.toByteArray()
                )
                fileOutputStream?.flush()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error while logging into file : $e")
        }
    }

    private fun getLogChar(priority: Int): String {
        return when (priority) {
            Log.ASSERT -> "A"
            Log.DEBUG -> "D"
            Log.ERROR -> "E"
            Log.INFO -> "I"
            Log.VERBOSE -> "V"
            Log.WARN -> "W"
            else -> ""
        }
    }
}