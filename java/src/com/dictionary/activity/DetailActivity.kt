package com.dictionary.activity

import android.content.Intent
import android.icu.util.MeasureUnit.ITEM
import android.os.Bundle
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavInflater
import androidx.navigation.fragment.NavHostFragment
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicDetailActivityBinding
import com.core.base.BaseActivity
import com.core.database.entity.DictionaryEntity
import com.core.database.entity.HistoryEntity
import com.core.utils.Utils.serializable
import com.dictionary.fragment.DictionaryDetailFragment
import com.dictionary.fragment.HistoryFragment
import com.dictionary.fragment.LearnSubFragment
import com.dictionary.fragment.LearnSubFragment.Companion.ITEM_NAME
import com.dictionary.fragment.LearnSubFragment.Companion.ITEM_POS
import com.dictionary.fragment.ZoomFragment
import com.dictionary.fragment.painter.UrduEditorFragment
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
        const val SET_ENTITY_MODEL = "entity_model"
        const val IMAGEPATH = ""
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
                val isFavouriteView = intent.extras?.getBoolean(SET_FAVOURITE_VIEW_TYPE)
                val argument = NavArgument.Builder().setDefaultValue(isFavouriteView).build()
                navGraph.addArgument(HistoryFragment.isFav, argument)
                R.id.historyViewFragment
            }
            R.id.action_dic_to_detail_dic -> {
                val dictionaryEntity = intent.extras?.serializable(SET_ENTITY_MODEL) as DictionaryEntity?
                val isEnglishView = intent.extras?.getBoolean(SET_FAVOURITE_VIEW_TYPE) as Boolean

                val argument = NavArgument.Builder().setDefaultValue(dictionaryEntity).build()
                val argument2 = NavArgument.Builder().setDefaultValue(isEnglishView).build()
                navGraph.addArgument(DictionaryDetailFragment.dictionaryEntityArgs, argument)
                navGraph.addArgument(DictionaryDetailFragment.dictionaryViewEntityArgs, argument2)
                R.id.dictionaryDetailViewFragment
            }
            R.id.action_dic_to_quiz -> {
                R.id.quizFragment
            }

            R.id.action_dic_to_word -> {
                R.id.wordFragment
            }
           R.id.action_more_to_editor ->{
               val imagePath = intent.extras?.getString(IMAGEPATH)
               val argument = NavArgument.Builder().setDefaultValue(imagePath).build()
               navGraph.addArgument(UrduEditorFragment.IMAGEPATH, argument)
                R.id.editorFragment
            }

            R.id.action_learn_to_detail ->
            {
                val id = intent.extras?.getInt(ITEM_POS)
                val name = intent.extras?.getString(ITEM_NAME)
                val idArgument = NavArgument.Builder().setDefaultValue(id).build()
                val nameArgument = NavArgument.Builder().setDefaultValue(name).build()
                navGraph.addArgument(ITEM_POS, idArgument)
                navGraph.addArgument(ITEM_NAME,nameArgument)
                R.id.learnSubFragment
            }
            else -> {
                val historyEntity = intent.extras?.serializable(SET_ENTITY_MODEL) as HistoryEntity?
                val argument = NavArgument.Builder().setDefaultValue(historyEntity).build()
                navGraph.addArgument(ZoomFragment.ARG_ZOOM, argument)
                R.id.zoomViewFragment
            }

        }

        if (fromOnCreate) {
            navGraph.setStartDestination(startDestinationID)
            navController.graph = navGraph
        } else {
            navController.navigate(startDestinationID)
        }
    }

    override fun initUserInterface() {
        setSupportActionBar(viewDataBinding.toolbar)
        viewDataBinding.toolbar.apply {
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }


}