package com.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.android.inputmethod.latin.R
import com.core.extensions.empty
import com.core.module.IODispatcher
import com.core.module.MainDispatcher
import com.core.utils.DialogManager
import com.core.utils.Inflate
import com.core.utils.ResourceHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) :
    BottomSheetDialogFragment() {


    //region VARIABLES

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var resourceHelper: ResourceHelper

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val viewDataBinding: VB
        get() = _binding as VB


    @Inject
    @IODispatcher
    lateinit var defaultDispatcher: CoroutineDispatcher

    @Inject
    @MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    //endregion


    //region LIFECYCLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserInterface(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //endregion


    //region UTIL

    protected abstract fun initUserInterface(view: View?)

    open fun displayAlert(
        title: Int = 0,
        messageResourceId: Int = 0,
        message: String = String.empty
    ) {
        lifecycleScope.launch(mainDispatcher) {
            dialogManager.singleButtonDialog(
                context = requireContext(),
                title = if (title == 0) String.empty else resourceHelper.getString(title),
                message = if (messageResourceId == 0) message else resourceHelper.getString(
                    messageResourceId
                ),
                cancelable = true
            )
        }
    }


    //endregion
}