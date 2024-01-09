package com.core.interfaces

import android.view.MenuItem
import android.view.View


interface ItemClickListener {
    fun onItemClick(position: Int, view: View) = Unit
    fun onItemLongClick(position: Int, view: View) = Unit
}

interface CheckChangeListenerItem {
    fun onCheckChangedListener(
        view: View,
        checkedState: Boolean
    )
}

interface MenuItemClickListener{
    fun setMenuItemListener(menuItem: MenuItem) = Unit
}


