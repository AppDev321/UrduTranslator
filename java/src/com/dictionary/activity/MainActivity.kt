package com.dictionary.activity

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicActivityMainBinding
import com.core.base.BaseActivity
import com.core.extensions.hide
import com.core.extensions.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<DicActivityMainBinding>(DicActivityMainBinding::inflate) {


    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun initUserInterface() {

        setSupportActionBar(viewDataBinding.toolbar)
        viewDataBinding.toolbar.title = getString(R.string.dic_app_name)

        val navView = viewDataBinding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
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