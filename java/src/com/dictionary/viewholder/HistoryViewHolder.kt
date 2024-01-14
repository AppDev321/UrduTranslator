package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemHistoryBinding
import com.core.database.entity.HistoryEntity
import com.dictionary.events.HistoryClickEvent

class HistoryViewHolder(
    private var item: DicItemHistoryBinding,
    event: (HistoryClickEvent)-> Unit
) : CommonViewHolder(item.root,event) {

    override fun bindItem(data: HistoryEntity) {
        super.bindItem(data)
        item.hToLang.text = data.toLang
        item.hFromLang.text = data.fromLang
        item.hWord.text = data.textForTranslation
        item.hMeaning.text = data.translatedText
    }
}