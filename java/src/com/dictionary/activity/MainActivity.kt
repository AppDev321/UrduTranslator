package com.dictionary.activity

import android.content.Intent
import android.view.Gravity
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.inputmethod.latin.databinding.DicActivityMainBinding
import com.core.base.BaseActivity
import com.core.extensions.hide
import com.core.extensions.show
import com.dictionary.viewmodel.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<DicActivityMainBinding>(DicActivityMainBinding::inflate) {
    private val dictionaryViewModel: DictionaryViewModel by viewModels()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        dictionaryViewModel.getQuizOfTheDay()
        dictionaryViewModel.getWordOfTheDay()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(com.android.inputmethod.latin.R.id.activity_main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun initUserInterface() {

        setSupportActionBar(viewDataBinding.toolbar)
        viewDataBinding.toolbar.title =
            getString(com.android.inputmethod.latin.R.string.dic_app_name)

        val navView = viewDataBinding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(com.android.inputmethod.latin.R.id.activity_main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val mDrawerLayout = viewDataBinding.drawerLayout
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT)
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT)
            }
        } else {
            return true
        }
        return super.onOptionsItemSelected(item)
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