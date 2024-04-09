package com.dictionary.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.inputmethod.latin.databinding.DicDictionaryDetailBinding
import com.core.base.BaseFragment
import com.core.database.entity.DictionaryEntity
import com.core.extensions.copyTextToClipboard
import com.core.extensions.hide
import com.core.extensions.safeGet
import com.core.extensions.sendShareIntent
import com.core.extensions.show
import com.core.utils.Utils.serializable
import com.core.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class DictionaryDetailFragment :
    BaseFragment<DicDictionaryDetailBinding>(DicDictionaryDetailBinding::inflate) {

        companion object {
        const val dictionaryEntityArgs = "dictionaryEntityArgs"
            const val dictionaryViewEntityArgs = "dictionaryViewEntityArgs"
    }

    private var entity: DictionaryEntity? = null
    private var isEnglishViewType = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entity = arguments?.serializable(dictionaryEntityArgs) as DictionaryEntity?
        isEnglishViewType = arguments?.getBoolean(dictionaryViewEntityArgs) as Boolean
        activity?.let {
            it as AppCompatActivity
            it.supportActionBar?.title = "Word " +
                    "Detail"
        }
    }

    override fun initUserInterface(view: View?) {
        entity?.let {
            viewDataBinding.apply {
                if(isEnglishViewType) {
                    txtEng.text = it.meaning
                    txtUrdu.text = it.word
                    txtEng.setOnSingleClickListener{view->
                        setTextToSpeak(it.meaning.safeGet(), Locale("en"))
                    }
                    txtUrdu.setOnSingleClickListener{view->
                        setTextToSpeak(it.word.safeGet(), Locale("ur"))
                    }
                }
                else {
                    txtUrdu.text = it.meaning
                    txtEng.text = it.word
                    txtUrdu.setOnSingleClickListener{view->
                        setTextToSpeak(it.meaning.safeGet(), Locale("en"))
                    }
                    txtEng.setOnSingleClickListener{view->
                        setTextToSpeak(it.word.safeGet(), Locale("ur"))
                    }
                }

                txtCopy.setOnSingleClickListener{view->
                    context?.copyTextToClipboard(it.meaning.safeGet() +"\n"+it.word.safeGet())
                }

                txtShare.setOnSingleClickListener{view->
                    context?.sendShareIntent(it.meaning.safeGet() +"\n"+it.word.safeGet())
                }
            }
            
            
        }

    }
}