package com.dictonary.fragment

import android.os.Bundle
import android.view.View
import com.android.inputmethod.latin.databinding.DicZoomTranslationViewBinding
import com.core.base.BaseFragment
import com.core.database.entity.HistoryEntity

class ZoomFragment:BaseFragment<DicZoomTranslationViewBinding>(DicZoomTranslationViewBinding::inflate) {

    companion object{
        const val ARG_ZOOM ="zoom_object"
    }

    private lateinit var historyModelEntity :HistoryEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val historyModelEntity = arguments?.getSerializable(ARG_ZOOM)

    }
    override fun initUserInterface(view: View?) {
    }
}