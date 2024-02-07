package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicMoreFragmentBinding
import com.core.base.BaseFragment
import com.core.interfaces.ItemClickListener
import com.dictionary.adapter.LearnSubCategoryAdapter
import com.dictionary.model.SettingItem
import com.dictionary.navigator.LearnNavigator
import com.dictionary.viewmodel.LearnViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearnSubFragment :
    BaseFragment<DicMoreFragmentBinding>(DicMoreFragmentBinding::inflate),
    ItemClickListener, LearnNavigator {
    companion object {
        const val ITEM_POS = "item_pos"
        const val ITEM_NAME = "item_name"
    }

    private val learnViewModel: LearnViewModel by viewModels()

    private lateinit var settingAdapter: LearnSubCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        learnViewModel.setNavigator(this)


    }

    override fun onResume() {
        super.onResume()
        activity.let {
            arguments?.getString(ITEM_NAME)?.let {name->
                it as AppCompatActivity
                it.supportActionBar?.title = name
            }
        }

    }

    override fun initUserInterface(view: View?) {
        arguments?.getInt(ITEM_POS)?.let {
            learnViewModel.getLearnDetailByCategory(it)
        }
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

    override fun onItemClick(position: Int, view: View) {
        super.onItemClick(position, view)
        val data = settingAdapter.getItems()[position]
        val bundle = bundleOf(
            LearnDetailFragment.ITEM_NAME to data.name,
            LearnDetailFragment.ITEM_POS to arguments?.getInt(ITEM_POS)
        )
        findNavController().navigate(R.id.action_sub_to_detail, bundle)
    }

}