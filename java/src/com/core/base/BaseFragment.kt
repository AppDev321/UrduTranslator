package com.core.base

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.android.inputmethod.latin.R
import com.core.extensions.showShortToast
import com.core.interfaces.MenuItemClickListener
import com.core.module.IODispatcher
import com.core.module.MainDispatcher
import com.core.module.UnconfinedDispatcher
import com.core.utils.DialogManager
import com.core.utils.Inflate
import com.core.utils.PreferenceManager
import com.core.utils.ResourceHelper
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var _binding: VB? = null

    @Suppress("UNCHECKED_CAST")
    protected val viewDataBinding: VB
        get() = _binding!!


    @Inject
    lateinit var resourceHelper: ResourceHelper

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    @IODispatcher
    lateinit var defaultDispatcher: CoroutineDispatcher

    @Inject
    @IODispatcher
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    @MainDispatcher
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    @UnconfinedDispatcher
    lateinit var unconfinedDispatcher: CoroutineDispatcher

    private var originalNavigationIcon: Drawable? = null


    var requestingContactPermissionDialogAlreadyVisible = false

    protected abstract fun initUserInterface(view: View?)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun showPermissionSettingsDialog() {
        if (requireActivity().isFinishing.not() && requireActivity().isDestroyed.not()) {
            dialogManager.twoButtonDialog(
                context = requireContext(),
                title = resourceHelper.getString(R.string.permission),
                message = resourceHelper.getString(R.string.grant_permission_from_settings),
                spannedMessage = false,
                positiveButtonText = resourceHelper.getString(R.string.open_settings),
                alertDialogListener = object : DialogManager.AlertDialogListener {
                    override fun onPositiveButtonClicked() {
                        activity?.let {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:" + it.packageName)
                            startActivity(intent)
                        }
                    }

                    override fun onDialogDismissed() {
                        super.onDialogDismissed()

                    }
                }
            )
        }
    }

    fun showToast(msg: String) {
        activity?.showShortToast(msg)
    }

    fun setFragmentMenu(requiredMenu: Int, menuItemListener: MenuItemClickListener) {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(requiredMenu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                menuItemListener.setMenuItemListener(menuItem)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()

    }


}