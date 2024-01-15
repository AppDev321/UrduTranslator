package com.dictionary.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.databinding.DicMoreFragmentBinding
import com.android.inputmethod.latin.setup.SetupActivity
import com.core.base.BaseFragment
import com.core.interfaces.ItemClickListener
import com.dictionary.activity.MainActivity
import com.dictionary.adapter.SettingAdapter
import com.dictionary.model.SettingsDataFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment:BaseFragment<DicMoreFragmentBinding>(DicMoreFragmentBinding::inflate),
    ItemClickListener {

    private lateinit var settingAdapter :SettingAdapter

    @Inject
    lateinit var settingsDataFactory: SettingsDataFactory

    override fun initUserInterface(view: View?) {
        settingAdapter = SettingAdapter(this)
        viewDataBinding.rec.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = settingAdapter
        }

      settingAdapter.setItems( settingsDataFactory.getMoreFragmentData())
    }

    override fun onItemClick(position: Int, view: View) {
        super.onItemClick(position, view)

        if(position == 0)
        {
            activity?.let{
                it as MainActivity
                startActivity(Intent(it,SetupActivity::class.java))
            }
        }


    }
}