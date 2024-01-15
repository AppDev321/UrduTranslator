package com.dictionary.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.databinding.DicConversationFragmentBinding
import com.core.base.BaseFragment
import com.core.data.model.translate.TranslateReq
import com.core.database.entity.ConversationEntity
import com.core.database.entity.HistoryEntity
import com.core.extensions.copyTextToClipboard
import com.core.utils.setOnSingleClickListener
import com.dictionary.adapter.ConversationAdapter
import com.dictionary.events.ConversationClickEvent
import com.dictionary.navigator.ConversationNavigator
import com.dictionary.navigator.TranslateNavigator
import com.dictionary.viewmodel.ConversationViewModel
import com.dictionary.viewmodel.TranslateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class ConversationFragment :
    BaseFragment<DicConversationFragmentBinding>(DicConversationFragmentBinding::inflate),
    ConversationNavigator, TranslateNavigator {


    private val REQUEST_CODE_STT = -1
    private val translateView: TranslateViewModel by viewModels()
    private val conversationViewModel: ConversationViewModel by viewModels()
    private lateinit var conversationAdapter: ConversationAdapter


    private val startMicReading =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleSpeechRecognitionResult(result)
        }
    private var clickEvent: (ConversationClickEvent) -> Unit = {
        when (it) {
            is ConversationClickEvent.CopyClick -> {
                context?.copyTextToClipboard(it.data.outputText.toString())

            }

            is ConversationClickEvent.SpeakerClick -> {
                setTextToSpeak(
                    it.data.outputText.toString(),
                    Locale(it.data.outputLangCode.toString())
                )
            }

            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conversationViewModel.setNavigator(this)
        translateView.setNavigator(this)
    }

    override fun initUserInterface(view: View?) {
        conversationAdapter = ConversationAdapter(clickEvent)

        viewDataBinding.recConversation.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = conversationAdapter
        }

        conversationViewModel.getConversationList()
        conversationAdapter.publishListToAdapter(arrayListOf())
        viewDataBinding.apply {

            hLeftLanguage.text = preferenceManager.getPrefFromLangText()
            hRightLanguage.text = preferenceManager.getPrefToLangText()

            hSplitLanguages.setOnSingleClickListener {
                translateView.transformLanguage()
            }
            micRightSide.setOnSingleClickListener {
                translateView.isRightSideConversationMicOpen = true
                startMicReading(preferenceManager.getPrefToLangCode())
            }
            micLeftSide.setOnSingleClickListener {
                translateView.isRightSideConversationMicOpen = false
                startMicReading(preferenceManager.getPrefFromLangCode())
            }
        }

    }

    override fun displayConversationList(item: List<ConversationEntity>) {
        conversationAdapter.publishListToAdapter(item)
        val targetPosition = conversationAdapter.itemCount - 1
        if (targetPosition >= 0) {
            viewDataBinding.recConversation.scrollToPosition(targetPosition)
        }

    }

    override fun addConversation(item: ConversationEntity) {
        conversationAdapter.publishListToAdapter(item)
        val targetPosition = 0//conversationAdapter.itemCount - 1
        viewDataBinding.recConversation.apply {
            post{
                val lastChild: View? =
                    layoutManager?.findViewByPosition((layoutManager?.itemCount ?: 0) - 1)
                if (lastChild != null) {
                    layoutManager?.scrollToPosition(targetPosition)
                }
            }

        }
    }

    private fun startMicReading(lang: String) {
        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang)
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
        val req = if (translateView.isRightSideConversationMicOpen)
            TranslateReq(
                preferenceManager.getPrefFromLangCode(),
                preferenceManager.getPrefToLangCode(),
                text
            )
        else
            TranslateReq(
                preferenceManager.getPrefToLangCode(),
                preferenceManager.getPrefFromLangCode(),
                text
            )
        translateView.getTranslation(req, isConversationData = true)
    }

    override fun onLanguageChanged(from: String, to: String) {
        viewDataBinding.hLeftLanguage.text = from
        viewDataBinding.hRightLanguage.text = to
    }

    override fun onTranslatedTextReceived(data: HistoryEntity) {
        val model = ConversationEntity(
            inputText = data.textForTranslation,
            outputText = data.translatedText,
            inputLangCode = data.fromCode,
            outputLangCode = data.toCode,
            isRightSide = translateView.isRightSideConversationMicOpen
        )
        conversationViewModel.addConversationData(model)
    }
}