package com.dictonary.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.android.inputmethod.latin.databinding.DicFragmentTranslatorBinding
import com.core.base.BaseFragment
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.dictonary.navigator.TranslateNavigator
import com.dictonary.utils.TextQueryListenerManager
import com.dictonary.viewmodel.TranslateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TranslateFragment:BaseFragment<DicFragmentTranslatorBinding>(DicFragmentTranslatorBinding::inflate),
    TranslateNavigator {
    private  val REQUEST_CODE_STT = 1
    private val translateView: TranslateViewModel by viewModels()
    private val startMicReading =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleSpeechRecognitionResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        translateView.setNavigator(this)
    }
    override fun initUserInterface(view: View?) {

        viewDataBinding.hLeftLanguage.text = preferenceManager.getPrefFromLangText()
        viewDataBinding.hRightLanguage.text = preferenceManager.getPrefToLangText()


        viewDataBinding.btnMic.setOnClickListener{
            startMicReading()
        }

        viewDataBinding.hSplitLanguages.setOnClickListener {
            translateView.transformLanguage(preferenceManager)
        }

        viewDataBinding.edTranslate.addTextChangedListener( TextQueryListenerManager(
            this.activity?.lifecycle
        ) { queryText ->
            queryText?.let {
                viewDataBinding.btnTranslate.isEnabled = it.isNotEmpty()
            }
        })


    }





    private fun startMicReading() {
        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
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
                        val result =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
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

}