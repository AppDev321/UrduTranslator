package com.dictionary.fragment.painter.emoji.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.inputmethod.latin.databinding.DicItemEmojiBinding
import com.core.base.BaseRecyclerAdapter
import com.core.base.BaseViewHolder
import com.core.interfaces.ItemClickListener

class EmojiAdapter(
    itemClickListener: ItemClickListener? = null
) : BaseRecyclerAdapter<String>(itemClickListener) {

    override fun createBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val view =
            DicItemEmojiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmojiViewHolder(view)
    }
}