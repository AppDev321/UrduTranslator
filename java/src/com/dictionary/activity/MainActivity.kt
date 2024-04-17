package com.dictionary.activity

import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicActivityMainBinding
import com.core.base.BaseActivity
import com.core.extensions.TAG
import com.core.extensions.hide
import com.core.extensions.show
import com.core.utils.AppLogger
import com.core.utils.DialogManager
import com.core.utils.PermissionUtilsNew
import com.core.utils.PreferenceManager
import com.dictionary.viewmodel.DictionaryViewModel
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<DicActivityMainBinding>(DicActivityMainBinding::inflate),
    NavigationView.OnNavigationItemSelectedListener {
    private val dictionaryViewModel: DictionaryViewModel by viewModels()

    @Inject
    lateinit var dialogManager: DialogManager
    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        dictionaryViewModel.getQuizOfTheDay()
        dictionaryViewModel.getWordOfTheDay()

        if (!PermissionUtilsNew.hasNotificationPermission()) {
            Dexter.withContext(this)
                .withPermissions(arrayListOf(PermissionUtilsNew.getNotificationPermission()))
                .withListener(object : MultiplePermissionsListener {

                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        p0?.let {
                            if (it.isAnyPermissionPermanentlyDenied) {
                                PermissionUtilsNew.showPermissionSettingsDialog(this@MainActivity)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        permissionToken: PermissionToken?
                    ) {
                        permissionToken?.continuePermissionRequest()
                    }
                })
                .check()
        }
    }

    /* override fun onSupportNavigateUp(): Boolean {
         val navHostFragment =
             supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment
         val navController = navHostFragment.navController

         return navController.navigateUp() || super.onSupportNavigateUp()
     }*/

    override fun initUserInterface() {

        setSupportActionBar(viewDataBinding.toolbar)
        viewDataBinding.toolbar.title =
            getString(R.string.dic_app_name)

        val navView = viewDataBinding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)


        viewDataBinding.navView.setNavigationItemSelectedListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            val mDrawerLayout = viewDataBinding.drawerLayout
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.END)
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return false
    }

    override fun onNetworkConnected() {
        viewDataBinding.hNoInterNetConnection.hide()
        super.onNetworkConnected()
    }

    override fun onNetworkDisconnected() {
        viewDataBinding.hNoInterNetConnection.show()
        super.onNetworkDisconnected()
    }

    override fun onBackPressed() {
        val mDrawerLayout = viewDataBinding.drawerLayout
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.notification -> {

                dialogManager.singleChoiceListItemDialog(
                    this,
                    "",
                    "Notification",
                    arrayOf("On","Off"),
                    if (preferenceManager.getNotificationPolicy()) 0 else 1 ,
                    alertDialogListener= object: DialogManager.TypeAlertDialogItemClickListener{
                        override fun onItemTypeClicked(which: Int, type: String) {
                            preferenceManager.setNotificationPolicy(which == 0)
                        }
                    }
                )
                val mDrawerLayout = viewDataBinding.drawerLayout
                mDrawerLayout.closeDrawers()
            }

            else -> return false
        }

        return true
    }


}