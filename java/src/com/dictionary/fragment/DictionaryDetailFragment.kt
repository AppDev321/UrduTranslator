package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.inputmethod.latin.databinding.DicDictionaryDetailBinding
import com.core.base.BaseFragment
import com.core.database.entity.DictionaryEntity
import com.core.utils.Utils.serializable
import com.core.utils.setOnSingleClickListener
import java.util.Locale

class DictionaryDetailFragment :
    BaseFragment<DicDictionaryDetailBinding>(DicDictionaryDetailBinding::inflate) {

        companion object {
        const val dictionaryEntityArgs = "dictionaryEntityArgs"
    }

    private var entity: DictionaryEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entity = arguments?.serializable(dictionaryEntityArgs) as DictionaryEntity?
        activity?.let {
            it as AppCompatActivity
            it.supportActionBar?.title = "Detail"
        }
    }

    override fun initUserInterface(view: View?) {
        entity?.let {
            viewDataBinding.apply {
                txtEng.text = it.meaning
                txtUrdu.text = it.word
                txtEng.setOnSingleClickListener{view->
                    setTextToSpeak(it.meaning.toString(), Locale("en"))
                }
                txtUrdu.setOnSingleClickListener{view->
                    setTextToSpeak(it.word.toString(), Locale("ur"))
                }
            }
        }

    }
}