package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemLanguageBinding
import com.core.base.BaseViewHolder
import com.dictionary.model.LanguageModel

class LanguageViewHolder(
    private var binding: DicItemLanguageBinding,
) : BaseViewHolder<LanguageModel>(binding.root) {

    override fun bindItem(item: LanguageModel) {
        binding.txtLangName.text = item.language
    }
}