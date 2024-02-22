package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemDictionaryBinding
import com.core.base.BaseViewHolder
import com.core.database.entity.DictionaryEntity

class DictionaryViewHolder(
    private var binding: DicItemDictionaryBinding,
    private var isEnglishDictionaryData :Boolean
) : BaseViewHolder<DictionaryEntity>(binding.root) {
    override fun bindItem(item: DictionaryEntity) {
        binding.txtDictionaryWord.text = if(isEnglishDictionaryData)
            item.meaning
        else
        item.word
    }
}