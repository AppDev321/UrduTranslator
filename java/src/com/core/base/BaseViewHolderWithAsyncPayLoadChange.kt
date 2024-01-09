package com.core.base

import android.view.View

abstract class BaseViewHolderWithAsyncPayLoadChange<T>(bindingView: View) :
    BaseViewHolder<T>(bindingView) {

    abstract fun payloadChanged(change: Any, item: T)

    var bindPosition: Int = layoutPosition

    override fun bindItem(item: T) {
        bindPosition = layoutPosition
    }

}