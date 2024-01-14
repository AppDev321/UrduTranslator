package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicHistoryFragmentBinding
import com.core.base.BaseFragment
import com.core.database.entity.HistoryEntity
import com.core.extensions.copyTextToClipboard
import com.core.extensions.hide
import com.core.extensions.show
import com.dictionary.adapter.HistoryAdapter
import com.dictionary.events.HistoryClickEvent
import com.dictionary.navigator.HistoryNavigator
import com.dictionary.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


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
            is HistoryClickEvent.ZoomClick -> {
                val bundle = bundleOf(
                    ZoomFragment.ARG_ZOOM to it.data
                )
                findNavController().navigate(R.id.action_fragmentHome_to_zoomViewFragment, bundle)
            }

            is HistoryClickEvent.CopyClick -> {
                context?.copyTextToClipboard(it.data.translatedText ?: "")

            }

            is HistoryClickEvent.FavClick -> {
                historyViewModel.performFavAction(it.data)
            }
            is HistoryClickEvent.SpeakerClick -> {
                setTextToSpeak(it.data.translatedText.toString(), Locale(it.data.toCode.toString()))
            }
            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyViewModel.setNavigator(this)

    }


    override fun initUserInterface(view: View?) {
        val isFavouriteDataView =
            arguments?.let { HistoryFragmentArgs.fromBundle(it).isFav } ?: false

        activity?.let {
            it as AppCompatActivity
            it.supportActionBar?.title = if (isFavouriteDataView.not()) "History" else "Favourite"
        }

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

    override fun favouriteItemUpdated(item: HistoryEntity) {
        historyAdapter.getItems().map {
            if (it.id == item.id) {
                it.isFav = item.isFav
            }
        }
          historyAdapter.notifyDataSetChanged()

    }

}