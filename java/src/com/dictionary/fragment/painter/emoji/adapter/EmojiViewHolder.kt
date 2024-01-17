package com.dictionary.fragment.painter.emoji.adapter

import com.android.inputmethod.latin.databinding.DicItemEmojiBinding
import com.core.base.BaseViewHolder


class EmojiViewHolder(private val recyclerItemEmojiBinding: DicItemEmojiBinding) :
        BaseViewHolder<String>(recyclerItemEmojiBinding.root) {

        override fun bindItem(item: String) {
            recyclerItemEmojiBinding.textViewEmoji.text = item
        }
    }