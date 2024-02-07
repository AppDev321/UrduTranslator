package com.dictionary.viewholder

import com.android.inputmethod.latin.databinding.DicItemLearnDetailBinding
import com.core.base.BaseViewHolder
import com.core.database.entity.LearnEntity
import com.core.extensions.hide
import com.core.utils.setOnSingleClickListener
import com.dictionary.events.HistoryClickEvent
import com.dictionary.events.LearnClickEvent

class LearnDetailViewHolder(
    private var binding: DicItemLearnDetailBinding,
   var event: (LearnClickEvent) -> Unit
) : BaseViewHolder<LearnEntity>(binding.root) {


    override fun bindItem(item: LearnEntity) {
        binding.txtUrdu.text = item.urduWords
        binding.txtEng.text = item.englishWords

        binding.engViewItems.apply {
            btnCommonFav.hide()
            btnCommonZoom.hide()
            btnCommonCopy.setOnSingleClickListener{
                event.invoke(LearnClickEvent.CopyClick(item))
            }
            btnCommonSpeaker.setOnSingleClickListener{
                event.invoke(LearnClickEvent.SpeakerClick(item))
            }
        }

        binding.urduViewItems.apply {
            btnCommonFav.hide()
            btnCommonZoom.hide()
            btnCommonCopy.setOnSingleClickListener{
                event.invoke(LearnClickEvent.CopyClick(item,false))
            }
            btnCommonSpeaker.setOnSingleClickListener{
                event.invoke(LearnClickEvent.SpeakerClick(item,false))
            }
        }
    }

}