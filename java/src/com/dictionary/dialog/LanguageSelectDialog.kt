package com.dictionary.dialog

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicSelectLanguageBottomSheetBinding
import com.core.base.BaseBottomSheetDialogFragment
import com.core.interfaces.ItemClickListener
import com.dictionary.adapter.LanguageAdapter
import com.dictionary.events.LanguageChangeEvent
import com.dictionary.model.LanguageModel
import com.dictionary.model.LanguagesList
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class LanguageSelectDialog :
    BaseBottomSheetDialogFragment<DicSelectLanguageBottomSheetBinding>(
        DicSelectLanguageBottomSheetBinding::inflate
    ), ItemClickListener {

    companion object {
        const val KEY_LANGUAGE_SIDE = "isLeftLanguage"
    }

    private var isLeftSideLanguage = true
    private lateinit var languageAdapter: LanguageAdapter
    private var languageList: ArrayList<LanguageModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLeftSideLanguage = arguments?.getBoolean(KEY_LANGUAGE_SIDE, false) ?: false
    }

    override fun initUserInterface(view: View?) {
        languageAdapter = LanguageAdapter(this)
        with(viewDataBinding) {
            recLang.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = languageAdapter
                layoutAnimation = AnimationUtils.loadLayoutAnimation(
                    context,
                    R.anim.layout_animation
                )
            }
        }

        languageList = ArrayList(LanguagesList.getAllLanguagesList().filter {
            it.isTextToSpeechSupporting && it.isSpeechToTextSupporting
        })

        languageAdapter.setItems(languageList)
    }

    override fun onItemClick(position: Int, view: View) {
        super.onItemClick(position, view)
        if (languageList.isNotEmpty())
            EventBus.getDefault()
                .post(LanguageChangeEvent(isLeftSideLanguage, languageList[position]))

        findNavController().popBackStack()
    }

}