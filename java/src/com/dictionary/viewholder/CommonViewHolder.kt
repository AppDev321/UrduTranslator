package com.dictionary.viewholder

import android.view.View
import android.widget.ImageView
import com.android.inputmethod.latin.R
import com.core.base.BaseViewHolderWithAsyncPayLoadChange
import com.core.database.entity.HistoryEntity
import com.core.utils.setOnSingleClickListener
import com.dictionary.model.HistoryClickEvent

open class CommonViewHolder(extendedView: View, var event: (HistoryClickEvent) -> Unit?) :
    BaseViewHolderWithAsyncPayLoadChange<HistoryEntity>(extendedView) {

    override fun bindItem(item: HistoryEntity) {
        super.bindItem(item)
        manageBaseAction(item)
    }

    private fun manageBaseAction(item: HistoryEntity) {

        val btnCopy = itemView.findViewById<ImageView>(R.id.btnCommonCopy)
        val btnFav = itemView.findViewById<ImageView>(R.id.btnCommonFav)
        val btnZoom = itemView.findViewById<ImageView>(R.id.btnCommonZoom)
        val btnSpeaker = itemView.findViewById<ImageView>(R.id.btnCommonSpeaker)

        if (item.isFav == true) {
            btnFav.setImageResource(R.drawable.ic_fav)
        } else {
            btnFav.setImageResource(R.drawable.ic_fav_uncheck)
        }

        btnCopy.setOnSingleClickListener {
            event.invoke(HistoryClickEvent.CopyClick(item))
        }

        btnSpeaker.setOnSingleClickListener {
            event.invoke(HistoryClickEvent.SpeakerClick(item))

        }
        btnFav.setOnSingleClickListener {
            event.invoke(HistoryClickEvent.FavClick(item))
        }

        btnZoom.setOnSingleClickListener {
            event.invoke(HistoryClickEvent.ZoomClick(item))
        }

    }

    override fun payloadChanged(change: Any, item: HistoryEntity) {

    }

}