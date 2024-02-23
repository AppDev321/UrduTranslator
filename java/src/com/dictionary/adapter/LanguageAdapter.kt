package com.dictionary.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.android.inputmethod.latin.databinding.DicItemLanguageBinding
import com.core.base.BaseRecyclerAdapter
import com.core.database.entity.DictionaryEntity
import com.core.extensions.empty
import com.core.interfaces.ItemClickListener
import com.dictionary.model.LanguageModel
import com.dictionary.viewholder.LanguageViewHolder

class LanguageAdapter(
    itemClickListener: ItemClickListener
) :
    BaseRecyclerAdapter<LanguageModel>(itemClickListener) , Filterable {
    var searchText: String = String.empty
    var originList = listOf<LanguageModel>()

    override fun createBaseViewHolder(parent: ViewGroup, viewType: Int) =
        LanguageViewHolder(
            DicItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    @SuppressLint("NotifyDataSetChanged")
    override fun setItems(items: ArrayList<LanguageModel>) {
        if (dataItems.isNotEmpty())
            dataItems.clear()
        dataItems.addAll(items)
        notifyDataSetChanged()
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                if (originList.isNotEmpty())
                    p1?.values?.let {
                        (it as? ArrayList<LanguageModel>)?.let { callMessages ->
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

    private fun getFilteredResults(constraint: String): List<LanguageModel> {
        val results: MutableList<LanguageModel> = arrayListOf()
        originList.forEach {
            if (it.language?.lowercase()!!.contains(constraint)) {
                results.add(it)
            }
        }
        return results
    }

}