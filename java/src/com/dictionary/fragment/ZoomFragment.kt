package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import com.android.inputmethod.latin.databinding.DicZoomTranslationViewBinding
import com.core.base.BaseFragment
import com.core.database.entity.HistoryEntity
import com.core.utils.Utils.serializable

class ZoomFragment:BaseFragment<DicZoomTranslationViewBinding>(DicZoomTranslationViewBinding::inflate) {

    companion object{
        const val ARG_ZOOM ="type"
    }

    private  var historyModelEntity :HistoryEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyModelEntity =  arguments?.serializable(ARG_ZOOM) as HistoryEntity?

    }
    override fun initUserInterface(view: View?) {

        historyModelEntity?.let {
            viewDataBinding.apply {
               hFromLang.text = if(it.isUrdu == true) "Urdu" else "English"
               hToLang.text = if(it.isUrdu == true) "English" else "Urdu"
              hWord.text = it.textForTranslation
              hMeaning.text = it.translatedText
            }

        }

    }
}