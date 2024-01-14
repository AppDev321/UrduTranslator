package com.dictionary.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.inputmethod.latin.R
import com.core.base.BaseViewHolderWithAsyncPayLoadChange
import com.core.database.entity.ConversationEntity
import com.core.extensions.hide
import com.core.extensions.show
import com.core.utils.setOnSingleClickListener
import com.dictionary.events.ConversationClickEvent


open class ConversationViewHolder(
    extendedView: View,
    var event: (ConversationClickEvent) -> Unit?
) :
    BaseViewHolderWithAsyncPayLoadChange<ConversationEntity>(extendedView) {

    companion object {
        const val LEFT_VIEW = 0
        const val RIGHT_VIEW = 1
    }

    override fun bindItem(item: ConversationEntity) {
        super.bindItem(item)
        manageBaseAction(item)
    }

    private fun manageBaseAction(item: ConversationEntity) {

        val txtInput = itemView.findViewById<TextView>(R.id.txtInput)
        val txtOutput = itemView.findViewById<TextView>(R.id.txtOutput)


        val containerAction = itemView.findViewById<LinearLayout>(R.id.viewAction)
        val btnSpeaker = itemView.findViewById<ImageView>(R.id.imgSpeaker)
        val btnCopy = itemView.findViewById<ImageView>(R.id.imgCopy)

        containerAction.hide()

        txtInput.text = item.inputText
        txtOutput.text = item.outputText

        txtOutput.setOnClickListener {
            if (containerAction.visibility == View.GONE) {
                containerAction.show()
            } else {
                containerAction.hide()
            }
        }
        btnSpeaker.setOnSingleClickListener {
            event.invoke(ConversationClickEvent.SpeakerClick(item))
        }
        btnCopy.setOnSingleClickListener {
            event.invoke(ConversationClickEvent.CopyClick(item))
        }
    }

    override fun payloadChanged(change: Any, item: ConversationEntity) {
    }

}