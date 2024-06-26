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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicConversationFragmentBinding
import com.core.base.BaseFragment
import com.core.data.model.translate.TranslateReq
import com.core.database.entity.ConversationEntity
import com.core.database.entity.HistoryEntity
import com.core.extensions.copyTextToClipboard
import com.core.extensions.safeGet
import com.core.extensions.sendShareIntent
import com.core.utils.setOnSingleClickListener
import com.dictionary.activity.DetailActivity
import com.dictionary.adapter.ConversationAdapter
import com.dictionary.dialog.LanguageSelectDialog
import com.dictionary.events.ConversationClickEvent
import com.dictionary.events.LanguageChangeEvent
import com.dictionary.navigator.ConversationNavigator
import com.dictionary.navigator.TranslateNavigator
import com.dictionary.viewmodel.ConversationViewModel
import com.dictionary.viewmodel.TranslateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
                context?.copyTextToClipboard(it.data.outputText.safeGet())

            }

            is ConversationClickEvent.SpeakerClick -> {
                setTextToSpeak(
                    it.data.outputText.safeGet(),
                    Locale(it.data.outputLangCode.safeGet())
                )
            }

            is ConversationClickEvent.ShareClick -> {
                context?.sendShareIntent(it.data.outputText.safeGet())
            }

            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        conversationViewModel.setNavigator(this)
        translateView.setNavigator(this)
        EventBus.getDefault().register(this)

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

            hLeftLanguage.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(LanguageSelectDialog.KEY_LANGUAGE_SIDE, true)
                //findNavController().navigate(R.id.action_move_to_language_dialog, bundle)
                bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_move_to_language_select)
                findNavController().navigate(R.id.action_move_to_language_select,bundle)
            }


            hRightLanguage.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(LanguageSelectDialog.KEY_LANGUAGE_SIDE, false)
              //  findNavController().navigate(R.id.action_move_to_language_dialog, bundle)
                bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_move_to_language_select)
                findNavController().navigate(R.id.action_move_to_language_select,bundle)
            }

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
        lifecycleScope.launch(Dispatchers.Default) {
            delay(500)
            withContext(Dispatchers.Main){
                scrollListToBottom()
            }
        }
    }

   private fun scrollListToBottom()
    {
        val targetPosition = conversationAdapter.itemCount
        viewDataBinding.recConversation.apply {
            post {
                smoothScrollToPosition(targetPosition)
            }
        }
    }

    override fun addConversation(item: ConversationEntity) {
        conversationAdapter.publishListToAdapter(item)
        scrollListToBottom()
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
        setTextToSpeak(
            model.outputText.toString(),
            Locale(model.outputLangCode.toString())
        )
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLanguageChangeEvent(event: LanguageChangeEvent) {
        viewDataBinding.apply {
            event.langModel.let {
                if (event.isLeftSide) {
                    hLeftLanguage.text = it.language
                    preferenceManager.setPrefFromLangCode(it.code.safeGet())
                    preferenceManager.setPrefFromLangText(it.language.safeGet())
                } else {
                    hRightLanguage.text = event.langModel.language
                    preferenceManager.setPrefToLangCode(it.code.safeGet())
                    preferenceManager.setPrefToLangText(it.language.safeGet())
                }
            }
        }
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}