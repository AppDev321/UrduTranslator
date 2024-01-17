package com.dictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.inputmethod.latin.databinding.DicItemLanguageBinding
import com.core.base.BaseRecyclerAdapter
import com.core.interfaces.ItemClickListener
import com.dictionary.model.LanguageModel
import com.dictionary.viewholder.LanguageViewHolder

class LanguageAdapter(
    itemClickListener: ItemClickListener
) :
    BaseRecyclerAdapter<LanguageModel>(itemClickListener) {


    override fun createBaseViewHolder(parent: ViewGroup, viewType: Int) =
        LanguageViewHolder(
            DicItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


}