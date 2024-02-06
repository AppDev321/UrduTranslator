package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.databinding.DicMoreFragmentBinding
import com.core.base.BaseFragment
import com.core.interfaces.ItemClickListener
import com.dictionary.activity.DetailActivity.Companion.OBJECTID
import com.dictionary.adapter.LearnSubCategoryAdapter
import com.dictionary.adapter.SettingAdapter
import com.dictionary.fragment.painter.UrduEditorFragment
import com.dictionary.model.SettingItem
import com.dictionary.model.SettingsDataFactory
import com.dictionary.navigator.LearnNavigator
import com.dictionary.viewmodel.LearnViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LearnSubFragment :
    BaseFragment<DicMoreFragmentBinding>(DicMoreFragmentBinding::inflate),
    ItemClickListener, LearnNavigator {

    private val learnViewModel : LearnViewModel by viewModels()

    private lateinit var settingAdapter: LearnSubCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        learnViewModel.setNavigator(this)
        arguments?.getInt(OBJECTID)?.let {
            learnViewModel.getLearnDetailByCategory(it)
        }

    }

    override fun initUserInterface(view: View?) {
        settingAdapter = LearnSubCategoryAdapter(this)
        viewDataBinding.rec.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = settingAdapter
        }

    }


    override fun displaySubCategoryList(item: List<SettingItem>) {
        super.displaySubCategoryList(item)
        settingAdapter.setItems(items = ArrayList(item))
    }

}