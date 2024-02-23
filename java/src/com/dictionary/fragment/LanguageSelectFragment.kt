package com.dictionary.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicMoreFragmentBinding
import com.android.inputmethod.latin.databinding.DicSelectLanguageBottomSheetBinding
import com.android.inputmethod.latin.setup.SetupActivity
import com.core.base.BaseFragment
import com.core.database.entity.HistoryEntity
import com.core.extensions.showShortToast
import com.core.interfaces.ItemClickListener
import com.core.utils.setOnSingleClickListener
import com.dictionary.activity.DetailActivity
import com.dictionary.activity.MainActivity
import com.dictionary.adapter.DictionaryAdapter
import com.dictionary.adapter.LanguageAdapter
import com.dictionary.adapter.SettingAdapter
import com.dictionary.dialog.ImageChooserDialog
import com.dictionary.dialog.ImageChooserDialog.Companion.PROFILE_PHOTO_SELECTION_OPTION
import com.dictionary.events.LanguageChangeEvent
import com.dictionary.model.LanguageModel
import com.dictionary.model.LanguagesList
import com.dictionary.model.SettingsDataFactory
import com.dictionary.navigator.TranslateNavigator
import com.dictionary.utils.CHOOSER
import com.dictionary.utils.TextQueryListenerManager
import com.dictionary.utils.getCameraOutputUri
import com.dictionary.viewmodel.TranslateViewModel
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class LanguageSelectFragment : BaseFragment<DicSelectLanguageBottomSheetBinding>(DicSelectLanguageBottomSheetBinding::inflate),
    ItemClickListener {


    companion object {
        const val KEY_LANGUAGE_SIDE = "isLeftLanguage"
    }

    private var isLeftSideLanguage = true
    private lateinit var languageAdapter: LanguageAdapter
    private var languageList: ArrayList<LanguageModel> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLeftSideLanguage = arguments?.getBoolean(KEY_LANGUAGE_SIDE, false) ?: false
        activity?.let {
            it as AppCompatActivity
            it.supportActionBar?.title = if(isLeftSideLanguage)
                "Translate From"
                 else
                "Translate To"
        }
    }

    override fun initUserInterface(view: View?) {

        languageAdapter = LanguageAdapter(this)
        with(viewDataBinding) {
            recLang.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = languageAdapter
                layoutAnimation = AnimationUtils.loadLayoutAnimation(
                    context,
                    R.anim.layout_animation
                )
            }
        }

        viewDataBinding.edSearchBar.addTextChangedListener(
            TextQueryListenerManager(
                this.activity?.lifecycle
            ) { queryText ->
                queryText?.let {
                    languageAdapter.filter.filter(it)
                }
            }
        )


        languageList = ArrayList(LanguagesList.getAllLanguagesList().filter {
            it.isTextToSpeechSupporting && it.isSpeechToTextSupporting
        })

        languageAdapter.setItems(languageList)
        languageAdapter.originList = languageAdapter.getItems()

    }

    override fun onItemClick(position: Int, view: View) {
        super.onItemClick(position, view)

        if (languageList.isNotEmpty())
            EventBus.getDefault()
                .post(LanguageChangeEvent(isLeftSideLanguage, languageAdapter.getItems()[position]))

        activity?.onBackPressed()

    }



}