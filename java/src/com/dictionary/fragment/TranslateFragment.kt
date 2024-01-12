package com.dictionary.fragment

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicFragmentTranslatorBinding
import com.core.base.BaseFragment
import com.core.data.model.translate.TranslateReq
import com.core.extensions.copyTextToClipboard
import com.core.extensions.empty
import com.core.extensions.hide
import com.core.extensions.hideKeyboard
import com.core.extensions.show
import com.core.utils.setOnSingleClickListener
import com.dictionary.activity.DetailActivity
import com.dictionary.navigator.TranslateNavigator
import com.dictionary.utils.TextQueryListenerManager
import com.dictionary.viewmodel.TranslateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.net.URLEncoder
import java.util.Locale

@AndroidEntryPoint
class TranslateFragment :
    BaseFragment<DicFragmentTranslatorBinding>(DicFragmentTranslatorBinding::inflate),
    TranslateNavigator {


    private val REQUEST_CODE_STT = 1
    private val translateView: TranslateViewModel by viewModels()
    private var mTextToSpeech: TextToSpeech? = null
    private var mMediaPlayer: MediaPlayer? = null


    private val startMicReading =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleSpeechRecognitionResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        translateView.setNavigator(this)
    }

    override fun initUserInterface(view: View?) {

        viewDataBinding.apply {
            translatedView.hide()
            hLeftLanguage.text = preferenceManager.getPrefFromLangText()
            hRightLanguage.text = preferenceManager.getPrefToLangText()
            btnHistory.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(
                    DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.historyViewFragment
                )
                bundle.putBoolean(DetailActivity.SET_FAVOURITE_VIEW_TYPE, false)
                findNavController().navigate(R.id.action_fragmentHome_to_detailScreen, bundle)
            }

            btnFavourite.setOnSingleClickListener {
                val bundle = Bundle()
                bundle.putInt(
                    DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.historyViewFragment
                )
                bundle.putBoolean(DetailActivity.SET_FAVOURITE_VIEW_TYPE, true)
                findNavController().navigate(R.id.action_fragmentHome_to_detailScreen, bundle)

            }
            btnMic.setOnSingleClickListener {
                startMicReading()
            }

            hSplitLanguages.setOnSingleClickListener {
                translateView.transformLanguage()
            }

            edTranslate.addTextChangedListener(TextQueryListenerManager(
                activity?.lifecycle
            ) { queryText ->
                queryText?.let {
                    btnTranslate.isEnabled = it.isNotEmpty()
                    if (it.isEmpty()) btnDelete.hide() else btnDelete.show()
                }
            })

            btnDelete.setOnSingleClickListener {
                edTranslate.text.clear()
            }

            btnTranslate.setOnSingleClickListener {
                activity?.hideKeyboard()
                val word = edTranslate.text.toString().trim()
                if (word.isNotEmpty()) {
                    translatedView.show()
                    hTranslationLayout.txtTranslation.text = String.empty
                    hTranslationLayout.txtWord.text = word
                    hTranslationLayout.txtFromLang.text = preferenceManager.getPrefFromLangText()
                    hTranslationLayout.txtToLang.text = preferenceManager.getPrefToLangText()
                    val req = TranslateReq(
                        preferenceManager.getPrefToLangCode(),
                        preferenceManager.getPrefFromLangCode(),
                        word
                    )
                    translateView.getTranslation(req)
                } else {
                    showToast("Please type some text for translate")
                }
            }

            hTranslationLayout.hClose.setOnSingleClickListener {
                translatedView.hide()
            }

            hTranslationLayout.commonActionViews.apply {
                btnCopy.setOnSingleClickListener {
                    context?.copyTextToClipboard(hTranslationLayout.txtTranslation.text.toString())
                }
                btnCommonSpeaker.setOnSingleClickListener {
                    mTextToSpeech?.speak(
                        hTranslationLayout.txtTranslation.text.toString(),
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                }
                btnCommonZoom.setOnSingleClickListener{
                   translateView.getLastRecord{
                        val bundle = Bundle()
                        bundle.putSerializable(DetailActivity.SET_ENTITY_MODEL, it)
                        bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.zoomViewFragment)
                        findNavController().navigate(R.id.action_fragmentHome_to_detailScreen, bundle)

                   }
                }
            }

        }


    }


    private fun startMicReading() {
        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, preferenceManager.getPrefFromLangCode())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
        }

        try {
            startMicReading.launch(sttIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            showToast("Your device does not support STT.")
        }
    }

    private fun handleSpeechRecognitionResult(result: ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            when (result.resultCode) {
                REQUEST_CODE_STT -> {
                    result.data?.let { data ->
                        val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        if (!result.isNullOrEmpty()) {
                            val recognizedText = result[0]
                            translateView.handleSpeechText(recognizedText)
                        }
                    }
                }
            }
        }
    }

    override fun onTextSpeechReceived(text: String) {
        viewDataBinding.edTranslate.setText(text)
    }

    override fun onLanguageChanged(from: String, to: String) {
        viewDataBinding.hLeftLanguage.text = from
        viewDataBinding.hRightLanguage.text = to

    }

    override fun setProgressVisibility(visibility: Int) {
        viewDataBinding.apply {
            hTranslationLayout.txtLoadingBar.visibility = visibility
        }
        super.setProgressVisibility(visibility)
    }

    override fun onTranslatedTextReceived(text: String) {
        if (text.isNotEmpty()) {
            viewDataBinding.hTranslationLayout.txtTranslation.text = text
        } else {
            showToast("Translation not found. Try again later")
        }
    }

    private fun hInitializeMedia(locale: Locale?, languageCode: String?, sentence: String?) {
        if (mTextToSpeech == null) {
            if (languageCode != null) {
                sentence?.let { initializeTts(locale, languageCode, it) }
            }
        } else {
            val result: Int? = mTextToSpeech?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                val i = 0
                if (i == 0) {
                    if (sentence != null) {
                        languageCode?.let { getAudio(sentence, it) }
                    }
                } else {
                    showToast("No Internet Available")
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sentence?.let { ttsGreater21(it) }
                } else {
                    sentence?.let { ttsUnder20(it) }
                }
            }
        }
    }

    private fun initializeTts(
        locale: Locale?, languageCode: String,
        sentence: String
    ) {
        mTextToSpeech = TextToSpeech(requireContext()) { status: Int ->
            if (status == TextToSpeech.SUCCESS) {
                locale?.let { speakData(it, languageCode, sentence) }
            } else {
                mTextToSpeech = null
                Toast.makeText(requireContext(), "Some thing went wrong.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun ttsUnder20(text: String) {
        if (mTextToSpeech != null) {
            val map = HashMap<String, String>()
            map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "MessageId"
            mTextToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, map)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun ttsGreater21(text: String) {
        if (mTextToSpeech != null) {
            val utteranceId = this.hashCode().toString() + ""
            mTextToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }
    }

    private fun speakData(locale: Locale, languageCode: String, sentence: String) {
        mMediaPlayer = MediaPlayer()
        if (mMediaPlayer != null) {
            if (mMediaPlayer?.isPlaying == true) {
                mMediaPlayer?.stop()
                mMediaPlayer?.release()
                mMediaPlayer = null
                hInitializeMedia(locale, languageCode, sentence)
            } else {
                hInitializeMedia(locale, languageCode, sentence)
            }
        } else {
            hInitializeMedia(locale, languageCode, sentence)
        }
    }

    private fun getAudio(sentence: String, lang: String) {
        try {

            var url: String = ""
            val encodedData = URLEncoder.encode(sentence, "UTF-8")
            url = url.replace("**", lang)
            url = url.replace("##", encodedData)
            if (mMediaPlayer != null) {
                mMediaPlayer = if (mMediaPlayer?.isPlaying == true) {
                    mMediaPlayer?.stop()
                    mMediaPlayer?.release()
                    null
                } else {
                    mMediaPlayer?.release()
                    null
                }
            }
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer?.setDataSource(url)
            mMediaPlayer?.prepareAsync()
            mMediaPlayer?.setOnPreparedListener {
                if (mMediaPlayer?.isPlaying == false) {
                    mMediaPlayer?.start()
                }
            }
            mMediaPlayer?.setOnErrorListener { _, _, _ ->
                showToast("Audio Coming Soon please wait")
                true
            }
            mMediaPlayer?.setOnCompletionListener {
                mMediaPlayer?.stop()
                mMediaPlayer?.release()
            }
        } catch (ie: IOException) {
            ie.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}