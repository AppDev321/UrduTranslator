package com.dictionary.fragment.painter.emoji

import android.graphics.Color
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicDialogMediaAddEmojiBinding

import com.core.base.BaseBottomSheetDialogFragment
import com.core.extensions.empty
import com.core.interfaces.ItemClickListener
import com.dictionary.fragment.painter.emoji.adapter.EmojiAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MediaEmojiEditorBSFragment(private val emojiSelectionListener: ((emojiUnicode: String) -> Unit)? = null) :
    BaseBottomSheetDialogFragment<DicDialogMediaAddEmojiBinding>(DicDialogMediaAddEmojiBinding::inflate) {


    var emojisList: ArrayList<String> = arrayListOf()

    override fun initUserInterface(view: View?) {

        (view?.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)

        lifecycleScope.launch {
            emojisList = withContext(defaultDispatcher) {
                getEmojis().let {
                    return@let it
                }
            }
            withContext(mainDispatcher) {
                setupRecycleView()
            }
        }

    }

    private fun setupRecycleView() {
        viewDataBinding.recyclerViewEmoji.apply {
            layoutManager = GridLayoutManager(activity, 5)
            adapter = EmojiAdapter(onItemClickListener).apply {
                setItems(emojisList)
            }
            setHasFixedSize(true)
        }
    }

    private val onItemClickListener = object : ItemClickListener {
        override fun onItemClick(position: Int, view: View) {
            emojiSelectionListener?.invoke(emojisList[position])
            dismiss()
        }

        override fun onItemLongClick(position: Int, view: View) {}
    }

    private fun getEmojis(): ArrayList<String> {
        val convertedEmojiList = arrayListOf<String>()
        resources.getStringArray(R.array.photo_editor_emoji).forEach {
            convertedEmojiList.add(convertEmoji(it))
        }
        return convertedEmojiList
    }

    private fun convertEmoji(emoji: String): String {
        return try {
            val convertEmojiToInt = emoji.substring(2).toInt(16)
            String(Character.toChars(convertEmojiToInt))
        } catch (e: NumberFormatException) {
            String.empty
        }
    }

    //endregion
}