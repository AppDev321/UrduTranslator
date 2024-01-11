package com.dictonary.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.databinding.DicHistoryFragmentBinding
import com.core.base.BaseFragment
import com.dictonary.adapter.HistoryAdapter
import com.dictonary.model.HistoryClickEvent
import com.dictonary.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HistoryFragment :
    BaseFragment<DicHistoryFragmentBinding>(DicHistoryFragmentBinding::inflate) {

    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter
    private var clickEvent: (HistoryClickEvent) -> Unit = {
        when (it) {
            is HistoryClickEvent.ExpandClick -> {
            }
            else -> {}
        }
    }

    override fun initUserInterface(view: View?) {
        historyAdapter = HistoryAdapter(clickEvent)

        viewDataBinding.recHistory.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = historyAdapter
        }
        historyViewModel.getHistoryDataList()
        viewLifecycleOwner.lifecycleScope.launch {
            historyViewModel.historyDataListObserver.collect {
                historyAdapter.setItems(ArrayList(it))
            }
        }
    }
}