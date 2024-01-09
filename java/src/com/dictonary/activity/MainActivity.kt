package com.dictonary.activity

import com.android.inputmethod.latin.databinding.DicActivityMainBinding
import com.core.base.BaseActivity
import com.core.extensions.hide
import com.core.extensions.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<DicActivityMainBinding>(DicActivityMainBinding::inflate) {
    override fun initUserInterface() {

    }

    override fun onNetworkConnected() {
        viewDataBinding.hNoInterNetConnection.hide()
        super.onNetworkConnected()
    }

    override fun onNetworkDisconnected() {
        viewDataBinding.hNoInterNetConnection.show()
        super.onNetworkDisconnected()
    }
}