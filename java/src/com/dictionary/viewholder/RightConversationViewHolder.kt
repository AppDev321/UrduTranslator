package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemRightConversationBinding
import com.core.database.entity.ConversationEntity
import com.dictionary.events.ConversationClickEvent

class RightConversationViewHolder(
    private var viewBinding: DicItemRightConversationBinding,
    event: (ConversationClickEvent) -> Unit?
) : ConversationViewHolder(viewBinding.root, event) {

    override fun bindItem(item: ConversationEntity) {
        super.bindItem(item)
    }
}