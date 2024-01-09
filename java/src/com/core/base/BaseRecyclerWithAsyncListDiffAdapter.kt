package com.core.base

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.core.interfaces.ItemClickListener

abstract class BaseRecyclerWithAsyncListDiffAdapter<T>(
    private val itemClickListener: ItemClickListener? = null,
) : RecyclerView.Adapter<BaseViewHolderWithAsyncPayLoadChange<T>>() {

    protected var messageDiffUtils = object : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any): Boolean {
            return checkItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItem: T & Any, newItem:T & Any): Boolean {
            return checkContentsTheSame(oldItem, newItem)
        }

        override fun getChangePayload(oldItem: T & Any, newItem: T & Any): Any? {
            return getPayload(oldItem, newItem)
        }
    }
    protected lateinit var differ: AsyncListDiffer<T>

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolderWithAsyncPayLoadChange<T> {
        val viewHolder = createBaseViewHolder(parent = parent, viewType = viewType)

        viewHolder.setItemClickListener(itemClickListener)
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: BaseViewHolderWithAsyncPayLoadChange<T>,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            payloads.forEach {
                holder.payloadChanged(it, differ.currentList[position])
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolderWithAsyncPayLoadChange<T>, position: Int) {
        if (position < 0 || position >= differ.currentList.size)
            return
        if (differ.currentList[position] != null)
            holder.bindItem(item = differ.currentList[position])
    }


    protected abstract fun createBaseViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolderWithAsyncPayLoadChange<T>

    fun getItemsAtPosition(position: Int): T? = differ.currentList.getOrNull(position)

    protected abstract fun checkItemsTheSame(oldItem: T, newItem: T): Boolean
    protected abstract fun checkContentsTheSame(oldItem: T, newItem: T): Boolean
    protected abstract fun getPayload(oldItem: T, newItem: T): Any?
    protected abstract fun initializeDiffer()


}