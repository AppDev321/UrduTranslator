package com.dictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.inputmethod.latin.databinding.DicItemHistoryBinding
import com.android.inputmethod.latin.databinding.DicItemLearnDetailBinding
import com.core.base.BaseRecyclerAdapter
import com.core.database.entity.HistoryEntity
import com.core.database.entity.LearnEntity
import com.dictionary.events.HistoryClickEvent
import com.dictionary.events.LearnClickEvent
import com.dictionary.viewholder.HistoryViewHolder
import com.dictionary.viewholder.LearnDetailViewHolder

class LearnDetailAdapter(
    private val event: (LearnClickEvent)-> Unit
) :
    BaseRecyclerAdapter<LearnEntity>(null) {

    override fun createBaseViewHolder(parent: ViewGroup, viewType: Int) =
        LearnDetailViewHolder(
            DicItemLearnDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),event
        )
}