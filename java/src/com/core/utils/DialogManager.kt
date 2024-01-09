package com.core.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.text.InputFilter
import android.util.TypedValue
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updatePaddingRelative
import com.android.inputmethod.latin.R

import com.core.extensions.TAG
import com.core.extensions.empty
import com.core.extensions.hide
import com.core.extensions.show
import com.core.extensions.showShortToast
import com.core.utils.Utils.getHtmlSpannedText
import java.util.*
import javax.inject.Inject


class DialogManager @Inject constructor() {

    /**
     * Create and displays alert dialog with single button
     * * @param context - Calling view/activity context
     * @param title - Dialog title
     * @param message - Dialog message
     * @param positiveButtonText - text for the dialog button
     * @param alertDialogListener - listener  , if callback requested
     *
     * */
    fun singleButtonDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = context.getString(R.string.ok),
        alertDialogListener: AlertDialogListener? = null,
        cancelable: Boolean,
    ): AlertDialog? {
        try {
            val builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
            builder.setTitle(title)
                .setMessage(getHtmlSpannedText(message))
                .setCancelable(cancelable)
                .setPositiveButton(positiveButtonText) { dialogInterface, _ ->
                    alertDialogListener?.onPositiveButtonClicked()
                    dialogInterface?.dismiss()
                }.setOnDismissListener {
                    alertDialogListener?.onDialogDismissed()
                }

            val alertDialog = builder.create()
            alertDialog.show()
            applyThemeColorOnDialogButtons(alertDialog)
            return alertDialog
        } catch (e: IllegalStateException) {
            AppLogger.e(TAG, "singleButtonDialog " + e.localizedMessage)
        }
        return null
    }

    /**
     * Create and displays alert dialog with two button (Action and cancel )
     * @param context - Calling view/activity context
     * @param title - Dialog title
     * @param message - Dialog message
     * @param positiveButtonText - text for the positive dialog button
     * @param negativeButtonText - text for the negative dialog button
     * @param alertDialogListener - listener  , if callback requested
     * @param cancelable - boolean  , dialog cancellable or not
     *
     * */
    fun twoButtonDialog(
        context: Context, title: String, message: String,
        positiveButtonText: String = context.getString(R.string.ok),
        negativeButtonText: String = context.getString(R.string.cancel),
        alertDialogListener: AlertDialogListener? = null,
        cancelable: Boolean = true,
    ): AlertDialog {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
        builder.setCancelable(cancelable)
        builder.setPositiveButton(positiveButtonText) { dialogInterface, _ ->
            alertDialogListener?.onPositiveButtonClicked()
            dialogInterface?.dismiss()
        }.setNegativeButton(negativeButtonText) { dialogInterface, _ ->
            alertDialogListener?.onNegativeButtonClicked()
            dialogInterface.dismiss()
        }
        builder.setTitle(title)
        builder.setMessage(message)
        val alertDialog = builder.create()
        alertDialog.setOnDismissListener { alertDialogListener?.onDialogDismissed() }
        alertDialog.show()
        applyThemeColorOnDialogButtons(alertDialog)
        return alertDialog
    }

    private fun getDialogInputField(
        context: Context,
        inputType: Int? = null,
        inputLength: Int
    ): EditText {
        return EditText(context).also {
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            val drawable = it.background.mutate()
           val lineColor= ResourcesCompat.getColor(it.resources, R.color.black, null)
            if (Utils.androidQAndAbove) {
                drawable.colorFilter = BlendModeColorFilter(lineColor, BlendMode.SRC_ATOP)
            } else {
                drawable.setColorFilter(lineColor, PorterDuff.Mode.SRC_ATOP)
            }
            it.background = drawable
            val horizontalSpacing =
                context.resources.getDimension(R.dimen.dialog_input_padding).toInt()
            lp.setMargins(horizontalSpacing, horizontalSpacing, horizontalSpacing, 0)
            it.layoutParams = lp
            if (inputType != null)
                it.inputType = inputType

            it.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(R.dimen.dialog_input_text_size)
            )
            val padding = context.resources.getDimension(R.dimen.new_dimen_8_dp).toInt()
            it.updatePaddingRelative(start = padding, top = 0, end = padding)
            it.filters = arrayOf(InputFilter.LengthFilter(inputLength))
        }
    }

    /**
     * Create and displays alert dialog with password input and two button (Action and cancel )
     * @param context - Calling view/activity context
     * @param title - Dialog title
     * @param positiveButtonText - text for the positive dialog button
     * @param negativeButtonText - text for the negative dialog button
     * @param alertDialogListener - listener  , if callback requested
     * @param cancelable - boolean  , dialog cancellable or not
     * @param inputType - Int  , type of the input, i.e. password, number, tet etc. default=text
     *
     * */
    fun twoButtonInputDialog(
        context: Context, title: String,
        positiveButtonText: String = context.getString(R.string.ok),
        negativeButtonText: String = context.getString(R.string.cancel).uppercase(),
        alertDialogListener: InputAlertDialogListener? = null,
        cancelable: Boolean = true,
        isFieldMandatory: Boolean = false,
        inputType: Int? = null,
        maxLength: Int,
    ): AlertDialog {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
        val inputText = getDialogInputField(context, inputType, maxLength)

        val bodyParentLayout = FrameLayout(context)
        bodyParentLayout.addView(inputText)

        builder.apply {
            setView(bodyParentLayout)
            setCancelable(cancelable)
            setPositiveButton(positiveButtonText) { dialogInterface, _ ->
                alertDialogListener?.onPositiveButtonClicked(inputText.text.toString())
                dialogInterface?.dismiss()

            }.setNegativeButton(negativeButtonText) { dialogInterface, _ ->
                alertDialogListener?.onNegativeButtonClicked()
                dialogInterface?.dismiss()
            }
            setTitle(title)
        }

        val alertDialog = builder.create()
        alertDialog.setOnDismissListener { alertDialogListener?.onDialogDismissed() }
        if (isFieldMandatory) {
            alertDialog.setOnShowListener {
                val button = (alertDialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                button.setOnClickListener {
                    if (inputText.text.isNotEmpty()) {
                        alertDialogListener?.onPositiveButtonClicked(inputText.text.toString())
                        alertDialog.dismiss()
                    } else {
                        context.showShortToast(R.string.please_enter_description)
                    }
                }
            }
        }
        alertDialog.show()
        applyThemeColorOnDialogButtons(alertDialog)
        return alertDialog
    }


    /**
     * Create and displays alert dialog with two button (Action and cancel )
     * @param context - Calling view/activity context
     * @param title - Dialog title
     * @param message - Dialog message
     * @param positiveButtonText - text for the positive dialog button
     * @param negativeButtonText - text for the negative dialog button
     * @param alertDialogListener - listener  , if callback requested
     *
     * */

    fun twoButtonDialog(
        context: Context,
        title: String,
        message: String,
        spannedMessage: Boolean = false,
        spannedTitle: Boolean = false,
        positiveButtonText: String = context.getString(R.string.ok),
        negativeButtonText: String = context.getString(R.string.cancel),
        alertDialogListener: AlertDialogListener? = null,
    ) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
        builder
            .setPositiveButton(positiveButtonText) { dialogInterface, _ ->
                alertDialogListener?.onPositiveButtonClicked()
                dialogInterface?.dismiss()
            }
            .setNegativeButton(negativeButtonText) { dialogInterface, _ ->
                alertDialogListener?.onNegativeButtonClicked()
                dialogInterface.dismiss()
            }

        if (spannedTitle) {
            builder.setTitle(getHtmlSpannedText(title))
        } else {
            builder.setTitle(title)
        }

        if (spannedMessage) {
            builder.setMessage(getHtmlSpannedText(message))
        } else {
            builder.setMessage(message)
        }

        val alertDialog = builder.create()


        alertDialog.setOnDismissListener { alertDialogListener?.onDialogDismissed() }
        alertDialog.show()

        applyThemeColorOnDialogButtons(alertDialog)
    }

    /**
     * Create and displays alert dialog with two button (Action and cancel )
     * @param context - Calling view/activity context
     * @param title - Dialog title
     * @param message - Dialog message
     * @param positiveButtonText - text for the positive dialog button
     * @param negativeButtonText - text for the negative dialog button
     * @param alertDialogListener - listener  , if callback requested
     *
     * */

    fun changeNumberCustomDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = context.getString(R.string.ok),
        negativeButtonText: Int = 0,
        alertDialogListener: AlertDialogListener? = null,
    ) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
        builder.setPositiveButton(positiveButtonText) { dialogInterface, _ ->
            alertDialogListener?.onPositiveButtonClicked()
            dialogInterface?.dismiss()
        }

        if (negativeButtonText != 0)
            builder.setNegativeButton(
                context.getString(negativeButtonText)
                    .uppercase(Locale.ROOT)
            ) { dialogInterface, _ ->
                alertDialogListener?.onNegativeButtonClicked()
                dialogInterface.dismiss()
            }


        val textView = TextView(context)
        textView.text = title
        textView.textSize = 16f
        textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        textView.setPadding(70, 70, 70, 0)
        builder.setCustomTitle(textView)
        builder.setMessage(getHtmlSpannedText(message))
        val alertDialog = builder.create()
        alertDialog.setOnDismissListener { alertDialogListener?.onDialogDismissed() }
        alertDialog.show()
        (alertDialog.findViewById(android.R.id.message) as TextView).gravity =
            Gravity.CENTER_HORIZONTAL
    }

    /**
     * Create and displays alert dialog with two button (Action and cancel )
     * @param context - Calling view/activity context
     * @param title - Dialog title
     * @param message - Dialog message
     * @param positiveButtonText - text for the positive dialog button
     * @param negativeButtonText - text for the negative dialog button
     * @param alertDialogListener - listener  , if callback requested
     *
     * */
    fun threeButtonDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        neutralButtonText: String,
        showPositiveButton: Boolean,
        alertDialogListener: AlertDialogListener? = null,
    ): AlertDialog {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
        builder.setTitle(title)
            .setNeutralButton(neutralButtonText) { dialogInterface, _ ->
                alertDialogListener?.onNeutralButtonClicked()
                dialogInterface?.dismiss()
            }
            .setNegativeButton(negativeButtonText) { dialogInterface, _ ->
                alertDialogListener?.onNegativeButtonClicked()
                dialogInterface.dismiss()
            }

        if (showPositiveButton) {
            builder.setPositiveButton(positiveButtonText) { dialogInterface, _ ->
                alertDialogListener?.onPositiveButtonClicked()
                dialogInterface.dismiss()
            }
        }
        builder.setMessage(message)
        val alertDialog = builder.create()
        alertDialog.setOnDismissListener { alertDialogListener?.onDialogDismissed() }
        alertDialog.show()
        applyThemeColorOnDialogButtons(alertDialog)
        return alertDialog
    }

    fun twoListItemDialog(
        context: Context?,
        title: String,
        firstItem: String,
        secondItem: String,
        checkedItem: Int,
        alertDialogListener: AlertDialogItemClickListener? = null,
    ) {
        val alertDialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.
            RadioButtonsTheme)
        val items = arrayOf(firstItem, secondItem)
        alertDialogBuilder.setSingleChoiceItems(items, checkedItem) { dialogInterface, i ->
            alertDialogListener?.onItemClicked(i)
            dialogInterface?.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setTitle(title)
        val window = alertDialog.window
        val wlp = window?.attributes
        wlp!!.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp

        alertDialog.show()
    }

    fun singleChoiceListItemDialog(
        context: Context?,
        type: String,
        title: String,
        items: Array<String>,
        checkedItem: Int,
        alertDialogListener: TypeAlertDialogItemClickListener? = null,
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.RadioButtonsTheme)
        alertDialogBuilder.setTitle(title)
        //val items = arrayOf(firstItem, secondItem, thirdItem)
        alertDialogBuilder.setSingleChoiceItems(items, checkedItem) { dialogInterface, i ->
            alertDialogListener?.onItemTypeClicked(i, type)
            dialogInterface?.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()

        val window = alertDialog.window
        val wlp = window?.attributes
        wlp!!.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp

        alertDialog.show()
    }

    fun twoItemDialog(
        context: Context?,
        firstItem: String,
        secondItem: String,
        alertDialogListener: AlertDialogItemClickListener? = null,
    ) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
        val items = arrayOf(firstItem, secondItem)
        alertDialogBuilder.setItems(items) { dialogInterface, i ->
            alertDialogListener?.onItemClicked(i)
            dialogInterface?.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun multiItemDialog(
        context: Context?,
        groupUserName: String?,
        info: String,
        voiceCall: String,
        videoCall: String,
        sendMessage: String,
        makeGroupAdmin: String,
        dismissGroupAdmin: String,
        removeUser: String,
        removeBroadcastUser: String,
        isFromBroadcast: Boolean,
        broadcastRecipientCount: Int,
        isAdmin: Boolean,
        isUserAdmin: Boolean,
        alertDialogListener: AlertDialogItemClickListener? = null,
    ) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme)
        val items: Array<String>
        if (isFromBroadcast) {
            items =
                 arrayOf(
                    info,
                    voiceCall,
                    videoCall,
                    sendMessage,
                )
        } else if (isAdmin) {
            items = arrayOf(
                info,
                voiceCall,
                videoCall,
                sendMessage,
                (if (isUserAdmin) dismissGroupAdmin else makeGroupAdmin),
                removeUser
            )
        } else {
            items = arrayOf(
                info,
                voiceCall,
                videoCall,
                sendMessage
            )
        }
        alertDialogBuilder.setItems(items) { dialogInterface, i ->
            alertDialogListener?.onItemClicked(i)
            dialogInterface?.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()

        val textView = TextView(context)
        textView.apply {
            text = groupUserName
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
            setTextColor(context?.getColor(R.color.black)!!)
            textSize = 14f
            setPadding(0, 10, 0, 0)
        }

        alertDialog.setCustomTitle(textView)
        alertDialog.show()
        applyThemeColorOnDialogButtons(alertDialog)
    }

    fun listItemDialog(
        context: Context?,
        title: String? = String.empty,
        @ArrayRes listItems: Int,
        alertDialogListener: AlertDialogItemClickListener? = null,
        showDivider: Boolean = true
    ) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setItems(listItems) { dialogInterface, i ->
            alertDialogListener?.onItemClicked(i)
            dialogInterface?.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setTitle(title)
        val dialogListView = alertDialog.listView
        if (showDivider) {
          //  dialogListView.divider = context?.getDrawable(R.drawable.divider_black)
            dialogListView.dividerHeight = 2
        }
        for (i in 0 until dialogListView.count) {
            val textView: TextView =
                dialogListView.adapter.getView(i, null, dialogListView) as TextView
            textView.gravity = Gravity.CENTER
        }
        val window = alertDialog.window
        val wlp = window?.attributes
        wlp!!.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        alertDialog.show()
        applyThemeColorOnDialogButtons(alertDialog)
    }

    fun listItemDialogWithoutDivider(
        context: Context?,
        title: String? = String.empty,
        listItems: Array<String>,
        alertDialogListener: AlertDialogItemClickListener? = null,
    ) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setItems(listItems) { dialogInterface, i ->
            alertDialogListener?.onItemClicked(i)
            dialogInterface?.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setTitle(title)
        val dialogListView = alertDialog.listView
        for (i in 0 until dialogListView.count) {
            val textView: TextView =
                dialogListView.adapter.getView(i, null, dialogListView) as TextView
            textView.gravity = Gravity.CENTER
        }
        val window = alertDialog.window
        val wlp = window?.attributes
        wlp!!.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp

        alertDialog.show()
        applyThemeColorOnDialogButtons(alertDialog)
    }

    fun customViewListItemDialog(
        context: Context?,
        title: String? = String.empty,
        listItems: Array<String>,
        alertDialogListener: AlertDialogItemClickListener? = null,
        selectedItemPosition: Int,
    ): AlertDialog {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setSingleChoiceItems(
            listItems,
            selectedItemPosition
        ) { dialogInterface, i ->
            alertDialogListener?.onItemClicked(i)
            dialogInterface?.dismiss()
        }
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.setTitle(title)
        val dialogListView = alertDialog.listView
        for (i in 0 until dialogListView.count) {
            val textView: TextView =
                dialogListView.adapter.getView(i, null, dialogListView) as TextView
            textView.gravity = Gravity.BOTTOM
        }
        val window = alertDialog.window
        val wlp = window?.attributes
        wlp!!.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        return alertDialog
    }

    /**
     * To show the dialog with check box
     */

    fun showMultiChoiceItemsDialog(
        context: Context?,
        dialogTitle: String,
        optionsArray: Array<String>,
        checkedOptionsArray: BooleanArray,
        alertDialogListener: AlertDialogListenerWithCheckBox? = null,
    ) {
        context?.run {
            val builder = AlertDialog.Builder(this, R.style.styleMultiChoiceDialog)
            builder.setTitle(dialogTitle)
            builder.setMultiChoiceItems(
                optionsArray,
                checkedOptionsArray
            ) { _, which, isChecked ->
                checkedOptionsArray[which] = isChecked
            }

            builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                alertDialogListener?.onPositiveButtonClicked(checkedOptionsArray)
                dialog?.dismiss()
            }
            builder.setNegativeButton(getString(R.string.cancel).uppercase(), null)
            val dialog = builder.create()
            dialog?.window?.apply {
                dialog.show()
                val width = getScreenWidth() - resources.getDimension(R.dimen.dimen_35_dp).toInt()
                val height = ViewGroup.LayoutParams.WRAP_CONTENT
                setLayout(width, height)
            }
        }
    }

    fun showCustomViewAlert(
        context: Context,
        extraButtonStringResource: Int = 0,
        positiveButtonStringResource: Int,
        alertTopMainImageResource: Int,
        titleStringResource: String,
        descriptionStringResource: String = String.empty,
        isHeadingAtCenter: Boolean = false,
        onExtraButtonClick: () -> Unit = {},
        onPositiveButtonClick: () -> Unit = {},
    ) {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val dialog: View = layoutInflater.inflate(R.layout.custom_dialog_view, null)
        val customDialog = AlertDialog.Builder(context).create()
        customDialog.setView(dialog)

        val image = dialog.findViewById<AppCompatImageView>(R.id.appCompatImageViewMainIcon)
        val title = dialog.findViewById<AppCompatTextView>(R.id.appCompatTextViewDialogTitle)
        val description =
            dialog.findViewById<AppCompatTextView>(R.id.appCompatTextViewDialogDescription)
        val firstButton = dialog.findViewById<AppCompatTextView>(R.id.buttonDialogFirst)
        val secondButton = dialog.findViewById<AppCompatTextView>(R.id.buttonDialogSecond)

        if (extraButtonStringResource != 0) {
            firstButton.setText(extraButtonStringResource)
            firstButton.show()
        } else {
            firstButton.hide()
            secondButton.background = null
        }

        title.text = titleStringResource
        if (isHeadingAtCenter) {
            changeConstraintForHeading(dialog)
        }

        if (descriptionStringResource.isEmpty())
            description.hide()
        else
            description.text = descriptionStringResource
        secondButton.setText(positiveButtonStringResource)
        image.setImageResource(alertTopMainImageResource)

        firstButton.setOnClickListener {
            onExtraButtonClick()
            customDialog.dismiss()
        }

        secondButton.setOnClickListener {
            onPositiveButtonClick()
            customDialog.dismiss()
        }

        customDialog.show()
    }
    fun showCustomDialogAlert(
        context: Context,
        extraButtonStringResource: Int = 0,
        positiveButtonStringResource: Int,
        alertTopMainImageResource: Int =0,
        titleStringResource: String,
        descriptionStringResource: String = String.empty,
        isTopImageShow: Boolean = true,
        spannedMessage: Boolean = false,
        spannedTitle: Boolean = false,
        onExtraButtonClick: () -> Unit = {},
        onPositiveButtonClick: () -> Unit = {},
    ) {
        val customDialog = AlertDialog.Builder(context, R.style.AlertDialogBaseTheme).create()
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val dialog: View = layoutInflater.inflate(R.layout.custom_dialog_view, null)
        customDialog.setView(dialog)

        val image = dialog.findViewById<AppCompatImageView>(R.id.appCompatImageViewMainIcon)
        val title = dialog.findViewById<AppCompatTextView>(R.id.appCompatTextViewDialogTitle)
        val description =
            dialog.findViewById<AppCompatTextView>(R.id.appCompatTextViewDialogDescription)
        val firstButton = dialog.findViewById<AppCompatTextView>(R.id.buttonDialogFirst)
        val secondButton = dialog.findViewById<AppCompatTextView>(R.id.buttonDialogSecond)

        if (extraButtonStringResource != 0) {
            firstButton.setText(extraButtonStringResource)
            firstButton.show()
        } else {
            firstButton.hide()
            secondButton.background = null
        }
        if (isTopImageShow)
            image.show()
        else
            image.hide()


        title.text = if (spannedTitle) {
            getHtmlSpannedText(titleStringResource)
        } else {
            titleStringResource
        }

        if (descriptionStringResource.isEmpty())
            description.hide()
        else {
            description.text = if (spannedMessage) {
                getHtmlSpannedText(descriptionStringResource)
            } else {
                descriptionStringResource
            }
        }

        secondButton.setText(positiveButtonStringResource)
        image.setImageResource(alertTopMainImageResource)

        firstButton.setOnClickListener {
            onExtraButtonClick()
            customDialog.dismiss()
        }

        secondButton.setOnClickListener {
            onPositiveButtonClick()
            customDialog.dismiss()
        }

        customDialog.show()
    }



    fun showDialogWithCustomDialogLayout(
        context: Context,
        view: View?,
        title: String = String.empty,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveButtonClick: () -> Unit?,
        onNegativeButtonClick: (() -> Unit)?=null,
    ) {
        AlertDialog.Builder(context, R.style.AlertDialogBaseTheme).apply {
            if (title.isNotEmpty()) setTitle(title)

            if (view != null) setView(view)

            setPositiveButton(
                positiveButtonText
            ) { _: DialogInterface?, _: Int ->
                onPositiveButtonClick.invoke()
            }.setNegativeButton(
                negativeButtonText,
                if (onNegativeButtonClick != null) { _: DialogInterface?, _: Int ->
                    onNegativeButtonClick.invoke()
                } else null)

            create().apply {
                show()
                applyThemeColorOnDialogButtons(this)
            }
        }
    }

    fun showSingleChoiceDialog(
        context: Context,
        customTextViewTitle: TextView? = null,
        title: String = String.empty,
        positiveButtonText: String,
        negativeButtonText: String,
        isCancelable: Boolean = true,
        isCancelableFromTouchOutSide: Boolean = true,
        listItems: Array<String?>,
        lastSelectedPosition: Int,
        onPositiveButtonClick: () -> Unit?,
        onNegativeButtonClick: (() -> Unit)? = null,
        onDismissListener: (() -> Unit)? = null,
        onSingleChoiceItemClick: (position: Int) -> Unit
    ) {
        AlertDialog.Builder(context, R.style.AlertDialogBaseTheme).apply {

            if (title.isNotEmpty()) setTitle(title)
            if (customTextViewTitle != null) setCustomTitle(customTextViewTitle)

            setCancelable(isCancelable).setPositiveButton(
                positiveButtonText
            ) { _: DialogInterface?, _: Int ->
                onPositiveButtonClick.invoke()
            }.setNegativeButton(
                negativeButtonText,
                if (onNegativeButtonClick != null) { dialog: DialogInterface?, _: Int ->
                    onNegativeButtonClick.invoke()
                    dialog?.cancel()
                } else null).setSingleChoiceItems(
                listItems,
                lastSelectedPosition
            ) { _: DialogInterface, position: Int ->
                onSingleChoiceItemClick.invoke(position)
            }

            if (onDismissListener != null) {
                setOnDismissListener {
                    onDismissListener.invoke()
                }
            }

            create().apply {
                setCanceledOnTouchOutside(isCancelableFromTouchOutSide)
                show()
                applyThemeColorOnDialogButtons(this)
            }
        }
    }


    private fun changeConstraintForHeading(dialogView: View) {
        val constraintLayout: ConstraintLayout = dialogView.findViewById(R.id.customViewContainer)
        val constraintSet = ConstraintSet()

        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.appCompatTextViewDialogTitle,
            ConstraintSet.END,
            R.id.customViewContainer,
            ConstraintSet.END,
            0
        )

        constraintSet.applyTo(constraintLayout)
    }

    /*
    * applyThemeColorOnDialogButtons custom method to apply color on dialog
    * buttons.
    */
    private fun applyThemeColorOnDialogButtons(
        alertDialog: AlertDialog,
        @ColorRes textColor: Int = R.color.dialog_box_text_and_control_color
    ) {
        alertDialog.apply {
            val color = alertDialog.context.getColor(textColor)
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color)
            getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(color)
        }
    }

    /**
     * Custom alert dialog interface to process/handle click
     * **/
    interface AlertDialogListener {

        fun onPositiveButtonClicked() {
            // Implementation not required
        }

        fun onNegativeButtonClicked() {
            // Implementation not required
        }

        fun onNeutralButtonClicked() {
            // Implementation not required
        }

        fun onDialogDismissed() {
            // Implementation not required
        }
    }

    interface AlertDialogItemClickListener {
        fun onItemClicked(which: Int) {
            // Implementation not required
        }

        fun onDialogDismissed() {}
    }

    interface AlertDialogListenerWithCheckBox {
        fun onPositiveButtonClicked(isCallLogChecked: Boolean = false) {
            // Implementation not required
        }

        fun onPositiveButtonClicked(checkedOptionsArray: BooleanArray) {
            // Implementation not required
        }
    }

    interface TypeAlertDialogItemClickListener {
        fun onItemTypeClicked(which: Int, type: String) {
            // Implementation not required
        }
    }

    interface InputAlertDialogListener {
        fun onPositiveButtonClicked(inputText: String) {
            // Implementation not required
        }

        fun onNegativeButtonClicked() {
            // Implementation not required
        }

        fun onDialogDismissed() {
            // Implementation not required
        }
    }
}