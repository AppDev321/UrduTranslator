package com.dictionary.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.android.inputmethod.latin.databinding.DicItemDictionaryBinding
import com.core.base.BaseRecyclerAdapter
import com.core.database.entity.DictionaryEntity
import com.core.extensions.empty
import com.core.interfaces.ItemClickListener
import com.dictionary.viewholder.DictionaryViewHolder

class DictionaryAdapter(
    itemClickListener: ItemClickListener

) :
    BaseRecyclerAdapter<DictionaryEntity>(itemClickListener), Filterable {
    var searchText: String = String.empty
    var originList = listOf<DictionaryEntity>()
    private var isEnglishDictionaryData: Boolean = true
    override fun createBaseViewHolder(parent: ViewGroup, viewType: Int) =
        DictionaryViewHolder(
            DicItemDictionaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), isEnglishDictionaryData
        )

    @SuppressLint("NotifyDataSetChanged")
    override fun setItems(items: ArrayList<DictionaryEntity>) {
        if (dataItems.isNotEmpty())
            dataItems.clear()
        dataItems.addAll(items)
        notifyDataSetChanged()
    }

    fun languageChanged(isEng: Boolean) {
        isEnglishDictionaryData = isEng
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (originList.isNotEmpty())
                    p1?.values?.let {
                        (it as? ArrayList<DictionaryEntity>)?.let { callMessages ->
                            setItems(callMessages)
                            //listenerDictionary.onSearchFilterResult(callMessages.size)
                        }
                    }
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchText = constraint.toString()
                val results = FilterResults()
                results.values =
                    getFilteredResults(constraint.toString().lowercase())
                return results
            }
        }
    }

    private fun getFilteredResults(constraint: String): List<DictionaryEntity> {
        val results: MutableList<DictionaryEntity> = arrayListOf()
        originList.forEach {
            if (it.meaning?.lowercase()!!.contains(constraint)) {
                results.add(it)
            }
        }
        return results
    }

}