package com.dictonary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.inputmethod.latin.databinding.DicItemHistoryBinding
import com.core.base.BaseRecyclerAdapter
import com.core.database.entity.HistoryEntity
import com.dictonary.model.HistoryClickEvent
import com.dictonary.viewholder.HistoryViewHolder

class HistoryAdapter(
    private val event: (HistoryClickEvent)-> Unit
) :
    BaseRecyclerAdapter<HistoryEntity>(null) {

    override fun createBaseViewHolder(parent: ViewGroup, viewType: Int) =
        HistoryViewHolder(
            DicItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),event
        )
}