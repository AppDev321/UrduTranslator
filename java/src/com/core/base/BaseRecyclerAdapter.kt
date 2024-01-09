package com.core.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.core.interfaces.ItemClickListener

abstract class BaseRecyclerAdapter<T>(private val itemClickListener: ItemClickListener? = null) :
    RecyclerView.Adapter<BaseViewHolder<T>>() {

    protected var dataItems = ArrayList<T>()

    override fun getItemCount() = dataItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val viewHolder = createBaseViewHolder(parent = parent, viewType = viewType)

        viewHolder.setItemClickListener(itemClickListener)
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        if (position < 0 || position >= dataItems.size)
            return
        if (dataItems[position] != null) {
            holder.bindItem(dataItems[position])
            holder.setItemClickListener(itemClickListener)
        }
    }

    open fun setItems(items: ArrayList<T>) {
        this.dataItems = items
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<T> {
        val list = arrayListOf<T>()
        list.addAll(dataItems)
        return list
    }

    fun clearItems() {
        dataItems.clear()
        notifyDataSetChanged()
    }

    private fun removeItem(index: Int) {
        dataItems.removeAt(index)
        notifyItemRemoved(index)
    }


    open fun diffUtilRefresh(
        diffResult: DiffUtil.DiffResult,
        newList: ArrayList<T>
    ) {
        dataItems.clear()
        dataItems.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    protected abstract fun createBaseViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<T>
}