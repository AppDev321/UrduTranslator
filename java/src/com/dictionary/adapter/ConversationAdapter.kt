package com.dictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.android.inputmethod.latin.databinding.DicItemLeftConversationBinding
import com.android.inputmethod.latin.databinding.DicItemRightConversationBinding
import com.core.base.BaseViewHolderWithAsyncPayLoadChange
import com.core.database.entity.ConversationEntity
import com.dictionary.events.ConversationClickEvent
import com.dictionary.viewholder.ConversationViewHolder.Companion.LEFT_VIEW
import com.dictionary.viewholder.LeftConversationViewHolder
import com.dictionary.viewholder.RightConversationViewHolder

class ConversationAdapter(
    private val event: (ConversationClickEvent) -> Unit
) : ConversationCommonAdapter() {

    init {
        initializeDiffer()
    }


    @Synchronized
    fun publishListToAdapter(list: List<ConversationEntity>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    fun publishListToAdapter(item: ConversationEntity) {
        val newList = ArrayList(differ.currentList)
        newList.add(item)
        differ.submitList(newList)
        notifyItemInserted(newList.size)
    }



    override fun initializeDiffer() {
        differ = AsyncListDiffer(this, messageDiffUtils)
    }

    override fun createBaseViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolderWithAsyncPayLoadChange<ConversationEntity> {
        return if (viewType == LEFT_VIEW)
            LeftConversationViewHolder(
                DicItemLeftConversationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), event
            )
        else
            RightConversationViewHolder(
                DicItemRightConversationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), event
            )
    }



}