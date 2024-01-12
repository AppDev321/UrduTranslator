package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemDictionaryBinding
import com.core.base.BaseViewHolder
import com.core.database.entity.DictionaryEntity

class DictionaryViewHolder(
    private var binding: DicItemDictionaryBinding,
) : BaseViewHolder<DictionaryEntity>(binding.root) {
    override fun bindItem(item: DictionaryEntity) {

        binding.txtDictionaryWord.text = item.meaning

    }
}