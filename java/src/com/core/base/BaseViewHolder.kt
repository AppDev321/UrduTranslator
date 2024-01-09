package com.core.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import com.core.interfaces.ItemClickListener
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<T>(private var bindingView: View) :
    RecyclerView.ViewHolder(bindingView),
    View.OnClickListener, LayoutContainer, View.OnLongClickListener {

    override val containerView = itemView

    private var itemClickListener: ItemClickListener? = null

    abstract fun bindItem(item: T)

    fun setItemClickListener(itemClickListener: ItemClickListener?) {
        itemClickListener?.let {
            this.itemClickListener = itemClickListener
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        itemClickListener?.let {
            this.itemClickListener = itemClickListener
            itemView.setOnClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
            itemClickListener?.onItemClick(
                position = absoluteAdapterPosition,
                view = itemView
            )
        }
    }

    override fun onLongClick(v: View?): Boolean {
        if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
            itemClickListener?.onItemLongClick(
                position = absoluteAdapterPosition,
                view = itemView
            )
            return true
        }
        return false
    }


}