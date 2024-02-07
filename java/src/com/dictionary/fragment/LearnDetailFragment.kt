package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.databinding.DicMoreFragmentBinding
import com.core.base.BaseFragment
import com.core.database.entity.LearnEntity
import com.core.extensions.copyTextToClipboard
import com.core.extensions.safeGet
import com.core.interfaces.ItemClickListener
import com.dictionary.adapter.LearnDetailAdapter
import com.dictionary.events.LearnClickEvent
import com.dictionary.navigator.LearnNavigator
import com.dictionary.viewmodel.LearnViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LearnDetailFragment :
    BaseFragment<DicMoreFragmentBinding>(DicMoreFragmentBinding::inflate),
    ItemClickListener, LearnNavigator, (LearnClickEvent) -> Unit {
    companion object {
        const val ITEM_POS = "item_pos"
        const val ITEM_NAME = "item_name"
    }

    private val learnViewModel: LearnViewModel by viewModels()

    private lateinit var learnDetailAdapter: LearnDetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        learnViewModel.setNavigator(this)
        val itemPos = arguments?.getInt(ITEM_POS) ?:0

        arguments?.getString(ITEM_NAME)?.let {
            if(itemPos == 3 || itemPos == 4){
                learnViewModel.getDetailListByCategory(it)
            }
            else {
                learnViewModel.getDetailListBySubCategory(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        activity.let {
            arguments?.getString(ITEM_NAME)?.let { name ->
                it as AppCompatActivity
                it.supportActionBar?.title = name
            }
        }

    }

    override fun initUserInterface(view: View?) {
        learnDetailAdapter = LearnDetailAdapter(this)
        viewDataBinding.rec.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = learnDetailAdapter
        }

    }


    override fun displayLearnDetailByCategory(item: List<LearnEntity>) {
        super.displayLearnDetailByCategory(item)
        learnDetailAdapter.setItems(items = ArrayList(item))
    }

    override fun invoke(p1: LearnClickEvent) {

        when (p1) {
            is LearnClickEvent.CopyClick -> {
                if (p1.isEngClick)
                    context?.copyTextToClipboard(p1.data.englishWords.safeGet())
                else
                    context?.copyTextToClipboard(p1.data.urduWords.safeGet())

            }

            is LearnClickEvent.SpeakerClick -> {
                if (p1.isEngClick)
                    setTextToSpeak(
                        p1.data.englishWords.toString(),
                        Locale("en")
                    )
                else
                    setTextToSpeak(
                        p1.data.urduWords.toString(),
                        Locale("ur")
                    )
            }

            else -> {}
        }
    }

}