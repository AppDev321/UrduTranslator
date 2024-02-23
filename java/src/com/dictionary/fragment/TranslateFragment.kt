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
import androidx.navigation.fragment.findNavController
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicFragmentTranslatorBinding
import com.core.base.BaseFragment
import com.core.data.model.translate.TranslateReq
import com.core.database.entity.HistoryEntity
import com.core.extensions.copyTextToClipboard
import com.core.extensions.empty
import com.core.extensions.hide
import com.core.extensions.hideKeyboard
import com.core.extensions.hideKeyboardFromStart
import com.core.extensions.safeGet
import com.core.extensions.show
import com.core.utils.setOnSingleClickListener
import com.dictionary.activity.DetailActivity
import com.dictionary.dialog.LanguageSelectDialog.Companion.KEY_LANGUAGE_SIDE
import com.dictionary.events.FavouriteUpdate
import com.dictionary.events.LanguageChangeEvent
import com.dictionary.navigator.HistoryNavigator
import com.dictionary.navigator.TranslateNavigator
import com.dictionary.utils.TextQueryListenerManager
import com.dictionary.viewmodel.HistoryViewModel
import com.dictionary.viewmodel.TranslateViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Locale

@AndroidEntryPoint
class TranslateFragment :
    BaseFragment<DicFragmentTranslatorBinding>(DicFragmentTranslatorBinding::inflate),
    TranslateNavigator, HistoryNavigator {


    private val REQUEST_CODE_STT = -1
    private val translateView: TranslateViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()


    private val startMicReading =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleSpeechRecognitionResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        translateView.setNavigator(this)
        historyViewModel.setNavigator(this)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun initUserInterface(view: View?) {
        hideKeyboardFromStart()
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

            hLeftLanguage.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(KEY_LANGUAGE_SIDE, true)
             //   findNavController().navigate(R.id.action_move_to_language_dialog, bundle)
                bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_move_to_language_select)
                findNavController().navigate(R.id.action_move_to_language_select,bundle)
            }


            hRightLanguage.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean(KEY_LANGUAGE_SIDE, false)
               // findNavController().navigate(R.id.action_move_to_language_dialog, bundle)
                bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_move_to_language_select)
                findNavController().navigate(R.id.action_move_to_language_select,bundle)
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
                edTranslate.text?.clear()
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
                stopTextToSpeech()
            }

            hTranslationLayout.commonActionViews.apply {
                btnCommonFav.setOnSingleClickListener {
                    translateView.getLastRecord {
                        historyViewModel.performFavAction(it)
                    }
                }
                btnPaste.setOnSingleClickListener {
                    pasteCopiedText { text ->
                        edTranslate.setText(text)
                    }
                }
                btnCommonSpeaker.setOnSingleClickListener {
                    setTextToSpeak(
                        hTranslationLayout.txtTranslation.text.toString(),
                        Locale(preferenceManager.getPrefToLangCode())
                    )
                }
                btnCommonZoom.setOnSingleClickListener {
                    translateView.getLastRecord {
                        val bundle = Bundle()
                        bundle.putSerializable(DetailActivity.SET_ENTITY_MODEL, it)
                        bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.zoomViewFragment)
                        findNavController().navigate(
                            R.id.action_fragmentHome_to_detailScreen,
                            bundle
                        )

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

    override fun favouriteItemUpdated(item: HistoryEntity) {
        if (item.isFav == true) {
            viewDataBinding.hTranslationLayout.commonActionViews.btnCommonFav.setImageResource(R.drawable.ic_fav)
        } else {
            viewDataBinding.hTranslationLayout.commonActionViews.btnCommonFav.setImageResource(R.drawable.ic_fav_uncheck)

        }
    }

    override fun setProgressVisibility(visibility: Int) {
        viewDataBinding.apply {
            hTranslationLayout.txtLoadingBar.visibility = visibility
        }

    }

    override fun onTranslatedTextReceived(data: HistoryEntity) {
        if (data.translatedText.toString().isNotEmpty()) {
            viewDataBinding.hTranslationLayout.txtTranslation.text = data.translatedText
        } else {
            showToast("Translation not found. Try again later")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFavouriteUpdate(event: FavouriteUpdate) {
        favouriteItemUpdated(event.item)
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

}