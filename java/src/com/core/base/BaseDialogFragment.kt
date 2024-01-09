package com.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.core.BaseApplication
import com.core.extensions.TAG
import com.core.extensions.empty
import com.core.utils.AppLogger
import com.core.utils.DialogManager
import com.core.utils.Inflate

import javax.inject.Inject

abstract class BaseDialogFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) :
    DialogFragment() {

    //region VARIABLES

    @Inject
    lateinit var dialogManager: DialogManager

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val viewDataBinding: VB
        get() = _binding as VB


    //endregion

    //region LIFECYCLE


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(layoutInflater)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserInterface(view)
    }

    //endregion

    //region UTIL
    protected abstract fun initUserInterface(view: View?)

    open fun displayAlert(
        title: Int = 0,
        messageResourceId: Int = 0,
        message: String = String.empty
    ) {
        try {
            dialogManager.singleButtonDialog(
                context = requireContext(),
                title = BaseApplication.instance.getString(title),
                message = if (messageResourceId == 0) message else BaseApplication.instance.getString(
                    messageResourceId
                ),
                cancelable = true
            )
        } catch (e: IllegalStateException) {
            AppLogger.e(TAG, "Fragment $this not attached to an activity.")
        }
    }
}