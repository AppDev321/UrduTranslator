package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemMoreBinding
import com.core.base.BaseViewHolder
import com.dictionary.model.SettingItem

class SettingsViewHolder(
    private var binding: DicItemMoreBinding,
) : BaseViewHolder<SettingItem>(binding.root) {

    override fun bindItem(item: SettingItem) {
        binding.txtName.text = item.name
        binding.imgIcon.setImageResource(item.img)

    }
}