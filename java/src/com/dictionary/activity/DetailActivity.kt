package com.dictionary.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavInflater
import androidx.navigation.fragment.NavHostFragment
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicDetailActivityBinding
import com.core.base.BaseActivity
import com.dictionary.fragment.HistoryFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<DicDetailActivityBinding>(DicDetailActivityBinding::inflate) {

    private lateinit var navGraph: NavGraph
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var graphInflater: NavInflater

    companion object {
        const val DEFAULT_NAV_HOST_KEY = "nav_host_id"
        const val SET_FAVOURITE_VIEW_TYPE = "view_type"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_detail_nav_host_fragment) as NavHostFragment
        graphInflater = navHostFragment.navController.navInflater
        setNavGraphAndNavController()
        updateNavigationDestination(intent)
    }

    private fun setNavGraphAndNavController() {
        if (this::graphInflater.isInitialized && this::navHostFragment.isInitialized) {
            navGraph = graphInflater.inflate(R.navigation.detail_activity_navigation)
            navController = navHostFragment.navController
        }
    }

    private fun updateNavigationDestination(intent: Intent, fromOnCreate: Boolean = true) {
        val startDestinationID = when (intent.getIntExtra(DEFAULT_NAV_HOST_KEY, 0)) {
            R.id.historyViewFragment -> {
                R.id.historyViewFragment
                val isFavouriteView = intent.extras?.getBoolean(SET_FAVOURITE_VIEW_TYPE)
                val argument = NavArgument.Builder().setDefaultValue(isFavouriteView).build()
                navGraph.addArgument(HistoryFragment.isFav, argument)
                R.id.historyViewFragment
            }

            else -> R.id.zoomViewFragment
        }

        if (fromOnCreate) {
            navGraph.setStartDestination(startDestinationID)
            navController.graph = navGraph
        } else {
            navController.navigate(startDestinationID)
        }
    }

    override fun initUserInterface() {

    }
}