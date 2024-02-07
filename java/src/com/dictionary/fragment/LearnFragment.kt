package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicMoreFragmentBinding
import com.core.base.BaseFragment
import com.core.interfaces.ItemClickListener
import com.dictionary.activity.DetailActivity
import com.dictionary.adapter.SettingAdapter
import com.dictionary.model.SettingsDataFactory
import com.dictionary.viewmodel.LearnViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LearnFragment :
    BaseFragment<DicMoreFragmentBinding>(DicMoreFragmentBinding::inflate),
    ItemClickListener {

    private val learnViewModel: LearnViewModel by viewModels()

    private lateinit var settingAdapter: SettingAdapter

    @Inject
    lateinit var settingsDataFactory: SettingsDataFactory

    override fun initUserInterface(view: View?) {
        settingAdapter = SettingAdapter(this)
        viewDataBinding.rec.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = settingAdapter
        }

        settingAdapter.setItems(settingsDataFactory.getLearningTypes())
    }

    override fun onItemClick(position: Int, view: View) {
        super.onItemClick(position, view)
        val data = settingsDataFactory.getLearningTypes()[position]
        val bundle = Bundle()
        bundle.putSerializable(LearnSubFragment.ITEM_POS, position)
        bundle.putSerializable(LearnSubFragment.ITEM_NAME, data.name)
        bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_learn_to_detail)
        findNavController().navigate(R.id.action_learn_to_detail, bundle)
    }

}