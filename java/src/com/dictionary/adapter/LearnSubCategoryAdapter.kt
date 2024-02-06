package com.dictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.inputmethod.latin.databinding.DicItemMoreBinding
import com.core.base.BaseRecyclerAdapter
import com.core.interfaces.ItemClickListener
import com.dictionary.model.SettingItem
import com.dictionary.viewholder.LearnSubViewHolder
import com.dictionary.viewholder.SettingsViewHolder

class LearnSubCategoryAdapter(
    itemClickListener: ItemClickListener
) :
    BaseRecyclerAdapter<SettingItem>(itemClickListener) {


    override fun createBaseViewHolder(parent: ViewGroup, viewType: Int) =
        LearnSubViewHolder(
            DicItemMoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


}