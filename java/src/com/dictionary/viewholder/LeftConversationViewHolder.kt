package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemLeftConversationBinding
import com.core.database.entity.ConversationEntity
import com.dictionary.events.ConversationClickEvent

class LeftConversationViewHolder(
    private var viewBinding: DicItemLeftConversationBinding,
    event: (ConversationClickEvent) -> Unit?
) : ConversationViewHolder(viewBinding.root, event) {

    override fun bindItem(item: ConversationEntity) {
        super.bindItem(item)
    }
}