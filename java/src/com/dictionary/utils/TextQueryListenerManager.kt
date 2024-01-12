package com.dictionary.utils

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class TextQueryListenerManager(
    lifecycle: Lifecycle?,
    private val onDebounceQueryRequested: (String?) -> Unit
) : TextWatcher {

    private var mDebouncePeriod: Long = 250
    private val mCoroutineScope = lifecycle?.coroutineScope
    private var mSearchJob: Job? = null
    override fun afterTextChanged(text: Editable?) {
        val newText = text.toString()
        mSearchJob?.cancel()
        mSearchJob = mCoroutineScope?.launch {
            newText.let {
                delay(mDebouncePeriod)
                onDebounceQueryRequested(newText)
            }
        }
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(newText: CharSequence?, start: Int, before: Int, count: Int) = Unit
}