package com.dictionary.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicActivityMainBinding
import com.core.base.BaseActivity
import com.core.extensions.TAG
import com.core.extensions.hide
import com.core.extensions.show
import com.core.utils.AppLogger
import com.dictionary.viewmodel.DictionaryViewModel
import com.dictionary.viewmodel.TranslateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<DicActivityMainBinding>(DicActivityMainBinding::inflate) {
    private val dictionaryViewModel: DictionaryViewModel by viewModels()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        AppLogger.e(TAG,"intent come")
        dictionaryViewModel.getQuizOfTheDay()
        dictionaryViewModel.getWordOfTheDay()

    }
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