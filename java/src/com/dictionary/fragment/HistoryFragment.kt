package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.databinding.DicHistoryFragmentBinding
import com.core.base.BaseFragment
import com.core.extensions.hide
import com.core.extensions.show
import com.dictionary.adapter.HistoryAdapter
import com.dictionary.model.HistoryClickEvent
import com.dictionary.navigator.HistoryNavigator
import com.dictionary.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HistoryFragment :
    BaseFragment<DicHistoryFragmentBinding>(DicHistoryFragmentBinding::inflate), HistoryNavigator {
    companion object {
        const val isFav = "isFav"
    }

    private val historyViewModel: HistoryViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter
    private var clickEvent: (HistoryClickEvent) -> Unit = {
        when (it) {
            is HistoryClickEvent.ExpandClick -> {
            }

            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyViewModel.setNavigator(this)

    }

    override fun initUserInterface(view: View?) {
        val isFavouriteDataView = false
           // arguments?.let { HistoryFragmentArgs.fromBundle(it).isFav } ?: false

        historyViewModel.getDataList(isFavouriteDataView)

        historyAdapter = HistoryAdapter(clickEvent)
        viewDataBinding.recHistory.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = historyAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            historyViewModel.historyDataListObserver.collect {
                if (it.isEmpty()) {
                    viewDataBinding.txtNoData.show()
                } else {
                    viewDataBinding.txtNoData.hide()
                }
                historyAdapter.setItems(ArrayList(it))
            }
        }
    }

    override fun setProgressVisibility(visibility: Int) {
        viewDataBinding.viewLoading.visibility = visibility
        super.setProgressVisibility(visibility)
    }

}