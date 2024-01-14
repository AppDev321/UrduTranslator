package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicZoomTranslationViewBinding
import com.core.base.BaseFragment
import com.core.database.entity.HistoryEntity
import com.core.extensions.copyTextToClipboard
import com.core.extensions.hide
import com.core.utils.Utils.serializable
import com.core.utils.setOnSingleClickListener
import com.dictionary.events.FavouriteUpdate
import com.dictionary.navigator.HistoryNavigator
import com.dictionary.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.util.Locale

@AndroidEntryPoint
class ZoomFragment :
    BaseFragment<DicZoomTranslationViewBinding>(DicZoomTranslationViewBinding::inflate),
    HistoryNavigator {

    companion object {
        const val ARG_ZOOM = "type"
    }

    private var historyModelEntity: HistoryEntity? = null
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyModelEntity = arguments?.serializable(ARG_ZOOM) as HistoryEntity?
        historyViewModel.setNavigator(this)
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            it as AppCompatActivity
            it.supportActionBar?.title = "Detail View"
        }

    }

    override fun initUserInterface(view: View?) {

        historyModelEntity?.let {
            val historyEntity = it
            viewDataBinding.apply {
                hEnd.btnCommonZoom.hide()
                hFromLang.text = it.fromLang
                hToLang.text = it.toLang
                hWord.text = it.textForTranslation
                hMeaning.text = it.translatedText

                favouriteItemUpdated(it)

                hEnd.apply {
                    btnCommonSpeaker.setOnSingleClickListener {
                        setTextToSpeak(
                            historyEntity.translatedText.toString(),
                            Locale(preferenceManager.getPrefToLangCode())
                        )
                    }
                    btnCommonCopy.setOnSingleClickListener {
                        context?.copyTextToClipboard(historyEntity.translatedText.toString())

                    }
                    btnCommonFav.setOnSingleClickListener {
                        historyViewModel.performFavAction(historyEntity)
                    }
                }


            }

        }

    }

    override fun favouriteItemUpdated(item: HistoryEntity) {
        if (item.isFav == true) {
            viewDataBinding.hEnd.btnCommonFav.setImageResource(R.drawable.ic_fav)
        } else {
            viewDataBinding.hEnd.btnCommonFav.setImageResource(R.drawable.ic_fav_uncheck)
        }
        EventBus.getDefault().post(FavouriteUpdate(item))
    }
}