package com.dictionary.adapter

import com.core.base.BaseRecyclerWithAsyncListDiffAdapter
import com.core.database.entity.ConversationEntity
import com.dictionary.viewholder.ConversationViewHolder.Companion.LEFT_VIEW
import com.dictionary.viewholder.ConversationViewHolder.Companion.RIGHT_VIEW


abstract class ConversationCommonAdapter :
    BaseRecyclerWithAsyncListDiffAdapter<ConversationEntity>() {


    override fun getItemViewType(position: Int): Int {
        val conversationEntity = differ.currentList[position]
       return if (conversationEntity.isRightSide == true)  RIGHT_VIEW else LEFT_VIEW
    }


    override fun checkItemsTheSame(
        oldItem: ConversationEntity,
        newItem: ConversationEntity
    ): Boolean {
        return false
    }

    override fun checkContentsTheSame(
        oldItem: ConversationEntity,
        newItem: ConversationEntity
    ): Boolean {
        return false
    }

    override fun getPayload(oldItem: ConversationEntity, newItem: ConversationEntity): Any? {
        return oldItem
    }
}
