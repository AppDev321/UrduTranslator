package com.dictionary.fragment

import android.os.Bundle
import android.util.LayoutDirection
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicDictionaryFragmentBinding
import com.core.base.BaseFragment
import com.core.database.entity.DictionaryEntity
import com.core.interfaces.ItemClickListener
import com.core.utils.setOnSingleClickListener
import com.dictionary.activity.DetailActivity
import com.dictionary.adapter.DictionaryAdapter
import com.dictionary.navigator.DictionaryNavigator
import com.dictionary.utils.TextQueryListenerManager
import com.dictionary.viewmodel.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DictionaryFragment :
    BaseFragment<DicDictionaryFragmentBinding>(DicDictionaryFragmentBinding::inflate),
    ItemClickListener, DictionaryNavigator {


    private val dictionaryViewModel: DictionaryViewModel by viewModels()
    private lateinit var dictionaryAdapter: DictionaryAdapter
    private var dicDataList = arrayListOf<DictionaryEntity>()
    private var isEnglishDic = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dictionaryViewModel.setNavigator(this)
    }

    override fun initUserInterface(view: View?) {
        dictionaryAdapter = DictionaryAdapter(this)
        viewDataBinding.recDictionary.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = dictionaryAdapter
        }

        dictionaryViewModel.getDictionaryData()

        viewDataBinding.langToggle.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton: RadioButton = group.findViewById(checkedId)
            val selectedText: String = checkedRadioButton.text.toString()
            dictionaryAdapter =  DictionaryAdapter(this)
            viewDataBinding.recDictionary.adapter = dictionaryAdapter
            isEnglishDic = selectedText == "English"
            dictionaryAdapter.languageChanged(isEnglishDic)
            dictionaryAdapter.setItems(dicDataList)
            dictionaryAdapter.originList = dictionaryAdapter.getItems()
            viewDataBinding.edSearchBar.apply {
                if(isEnglishDic.not())
                {
                    this.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    this.hint = "تلاش کریں"
                }
                else{
                    this.layoutDirection = View.LAYOUT_DIRECTION_LTR
                    this.hint = "Search"
                }
            }


        }

        viewDataBinding.btnQuizOftheDay.setOnSingleClickListener {
            val bundle = Bundle()
            bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_dic_to_quiz)
            findNavController().navigate(R.id.action_dic_to_quiz, bundle)
        }

        viewDataBinding.btnWordOftheDay.setOnSingleClickListener {
            val bundle = Bundle()
            bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_dic_to_word)
            findNavController().navigate(R.id.action_dic_to_word, bundle)
        }

        viewDataBinding.edSearchBar.addTextChangedListener(
            TextQueryListenerManager(
                this.activity?.lifecycle
            ) { queryText ->
                queryText?.let {
                    dictionaryAdapter.filter.filter(it)
                    if (it.isEmpty()) {

                        //    setAdapter(countrySelectFragmentViewModel.getTotalCountryList())
                    } else {
                        //   countrySelectFragmentViewModel.searchCountry(queryText.trim())
                    }
                }
            }
        )


    }

    override fun setProgressVisibility(visibility: Int) {
        super.setProgressVisibility(visibility)
        if(isAdded) {
            viewDataBinding.apply {
                loadingProgressBar.visibility = visibility
            }
        }
    }

    override fun displayDictionaryDataList(item: List<DictionaryEntity>, isEnglish: Boolean) {
        dictionaryAdapter.clearItems()
        dictionaryAdapter.setItems(ArrayList(item))
        dictionaryAdapter.originList = dictionaryAdapter.getItems()
        dicDataList = dictionaryAdapter.getItems()
    }

    override fun onItemClick(position: Int, view: View) {
        super.onItemClick(position, view)
        val bundle = Bundle()
        bundle.putSerializable(
            DetailActivity.SET_ENTITY_MODEL,
            dictionaryAdapter.getItems()[position]
        )
        bundle.putBoolean(DetailActivity.SET_FAVOURITE_VIEW_TYPE,isEnglishDic)
        bundle.putInt(DetailActivity.DEFAULT_NAV_HOST_KEY, R.id.action_dic_to_detail_dic)
        findNavController().navigate(R.id.action_dic_to_detail_dic, bundle)
    }

}