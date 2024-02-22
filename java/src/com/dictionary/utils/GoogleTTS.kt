package com.dictionary.utils

import android.media.AudioManager
import android.media.MediaPlayer
import com.core.extensions.TAG
import com.core.utils.AppLogger
import javax.inject.Inject

class GoogleTTS @Inject constructor(private val mediaPlayer: MediaPlayer) {
    fun playMediaFile(
        msg: String, translateType: String,
        onException: ((Exception) -> Unit)?,
        setOnErrorListener: (String) -> Unit,
    ) {
        mediaPlayer.apply {
            try {
                val url =
                    "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=${translateType}&q=$msg"
                reset()
                setDataSource(url)
                setOnPreparedListener {
                    start()
                }
                setOnCompletionListener {
                    stopMediaFile()
                }

                setOnErrorListener { mp, what, extra ->
                    stopMediaFile()
                    setOnErrorListener("Audio coming soon")
                    reset()
                    true
                }
                prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
                setOnErrorListener("Audio coming soon")
                stopMediaFile()
                // release()
            }
        }
    }

    fun stopMediaFile() {
        mediaPlayer.stop()
        AppLogger.e(TAG, "MediaPlayer stopped and released")

    }
}