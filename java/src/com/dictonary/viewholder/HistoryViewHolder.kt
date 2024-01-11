package com.dictonary.viewholder

import com.android.inputmethod.latin.databinding.DicItemHistoryBinding
import com.core.base.BaseViewHolder
import com.core.database.entity.HistoryEntity
import com.dictonary.model.HistoryClickEvent

class HistoryViewHolder(
    private var item: DicItemHistoryBinding,
    private val event: (HistoryClickEvent)-> Unit
) : BaseViewHolder<HistoryEntity>(item.root) {

    override fun bindItem(data: HistoryEntity) {

        item.hFromLang.text = if(data.isUrdu == true) "Urdu" else "English"
        item.hToLang.text = if(data.isUrdu == true) "English" else "Urdu"
        item.hWord.text = data.textForTranslation
        item.hMeaning.text = data.translatedText

        item.hZoom.setOnClickListener{
            event.invoke(HistoryClickEvent.ExpandClick(data))
        }

    }
}