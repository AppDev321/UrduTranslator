package com.dictionary.fragment

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicQuizFragmentBinding
import com.android.inputmethod.latin.databinding.DicWordFragmentBinding
import com.core.base.BaseFragment
import com.core.extensions.empty
import com.core.extensions.safeGet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordFragment : BaseFragment<DicWordFragmentBinding>(DicWordFragmentBinding::inflate) {

    override fun onResume() {
        super.onResume()
        activity?.let {
            it as AppCompatActivity
            it.supportActionBar?.title = "Word of the day"
        }
    }


    override fun initUserInterface(view: View?) {
        val wordModel = preferenceManager.getWordOfTheDay()
        wordModel?.let {
            viewDataBinding.apply {
                txtQuestion.text = wordModel.question.safeGet()
                txtMeaning.text = wordModel.meaning.safeGet()
            }
        }

    }
}