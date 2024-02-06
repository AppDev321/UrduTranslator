package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemMoreBinding
import com.core.base.BaseViewHolder
import com.core.extensions.hide
import com.dictionary.model.SettingItem

class LearnSubViewHolder(
    private var binding: DicItemMoreBinding,
) : BaseViewHolder<SettingItem>(binding.root) {
    override fun bindItem(item: SettingItem) {
        binding.txtName.text = item.name
        binding.imgIcon.hide()
    }
}