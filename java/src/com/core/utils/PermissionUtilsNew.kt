package com.core.utils

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_PICTURE_IN_PICTURE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Process
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.android.inputmethod.latin.R
import com.core.BaseApplication

import com.core.utils.Utils.androidOreoAndAbove
import com.core.utils.Utils.androidQAndAbove
import com.core.utils.Utils.androidRAndAbove
import com.core.utils.Utils.androidSAndAbove
import com.core.utils.Utils.androidTIRAMISUAndAbove

object PermissionUtilsNew {

    /**
     *  Checks if app has been granted READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE permission
     *  @return true , if granted
     * **/
    fun hasStoragePermission(isReadMandatory: Boolean = false): Boolean {
        return if (androidTIRAMISUAndAbove) {
            if (isReadMandatory) {
                ContextCompat.checkSelfPermission(
                    BaseApplication.instance,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    BaseApplication.instance,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        } else if (androidRAndAbove) {
            ContextCompat.checkSelfPermission(
                BaseApplication.instance,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                BaseApplication.instance,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun hasAllFilesReadPermission(): Boolean {
        return if (androidTIRAMISUAndAbove) {
            ContextCompat.checkSelfPermission(
                BaseApplication.instance, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                BaseApplication.instance, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                BaseApplication.instance, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                BaseApplication.instance,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getFileStoragePermissions(): Array<String> {
        return if (androidTIRAMISUAndAbove) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else if (androidRAndAbove) arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        else arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun getMediaStoragePermissions(): Array<String> {
        return if (androidTIRAMISUAndAbove) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        } else if (androidRAndAbove) arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        else arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun getStoragePermissionString(): String {
        return if (androidRAndAbove) Manifest.permission.READ_EXTERNAL_STORAGE else
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    fun getReadStoragePermissionString(): String {
        return Manifest.permission.READ_EXTERNAL_STORAGE
    }

    fun getWriteStoragePermissionString(): String {
        return Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    /**
     * Checks if app has been granted Notifications permissions in
     * Android 13 and above
     **/
    fun hasNotificationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED)
    }

    /**
     *  Checks if app has been granted CAMERA permission
     *  @return true , if granted
     * **/
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     *  Checks if app has been granted AUDIO RECORDING permission
     *  @return true , if granted
     * **/
    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * Check if given grant result array has not granted permission
     * @param grantResults - array of results from permission result callback
     * @return false , if array has not granted result or grantResults is empty
     * */
    fun allGranted(grantResults: IntArray): Boolean {
        if (grantResults.isEmpty()) {
            return false
        }
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


    /**
     *returns list of permission required for call
     * @param callType - CallType - Audio or Video
     * @return list of permission required based on call type
     * **/
    fun getPermissionForCall(callType: Int): List<String> {
        val permissionList: MutableList<String> = ArrayList()

        permissionList.add(Manifest.permission.RECORD_AUDIO)
        permissionList.add(Manifest.permission.READ_PHONE_STATE)
        permissionList.add(Manifest.permission.CAMERA)


        return permissionList
    }



    fun getBluetoothPermissionList(): List<String> {
        val permissionList: MutableList<String> = ArrayList()
        permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
        permissionList.add(Manifest.permission.BLUETOOTH_SCAN)
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        return permissionList
    }

    /**
     * Checks if Manifest.permission.BLUETOOTH is granted
     * **/
    fun hasBluetoothPermission(): Boolean {
        return if (androidSAndAbove) {
            (BaseApplication.instance.checkPermission(
                Manifest.permission.BLUETOOTH_CONNECT,
                Process.myPid(),
                Process.myUid()
            ) == PackageManager.PERMISSION_GRANTED &&
                    BaseApplication.instance.checkPermission(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Process.myPid(),
                        Process.myUid()
                    ) == PackageManager.PERMISSION_GRANTED)
        } else {
            (BaseApplication.instance.checkPermission(
                Manifest.permission.BLUETOOTH,
                Process.myPid(),
                Process.myUid()
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    /**
     * Checks if Manifest.permission.READ_CONTACTS is granted
     * */
    fun hasContactPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Returns the list of permissions required for camera and storage
     */
    fun getPermissionListForVideoRecording(): List<String> {
        return if (androidTIRAMISUAndAbove) {
            listOf(Manifest.permission.CAMERA)
        } else {
            listOf(Manifest.permission.CAMERA, getStoragePermissionString())
        }
    }

    /**
     * Checks if Picture In Picture Mode is enabled or not using AppOpsManager
     * Since [OPSTR_PICTURE_IN_PICTURE] was added in api level 26 , hence we cannot
     * check if PIP is enabled or not below api level 26
     */
    fun isPIPSettingEnabled(): Boolean {
        return try {
            val appOpsManager =
                BaseApplication.instance.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager?
            appOpsManager?.let {
                when {
                    androidQAndAbove -> {
                        it.unsafeCheckOp(
                            OPSTR_PICTURE_IN_PICTURE,
                            Process.myUid(),
                            BaseApplication.instance.packageName
                        ) == AppOpsManager.MODE_ALLOWED
                    }

                    androidOreoAndAbove -> {
                        it.checkOpNoThrow(
                            OPSTR_PICTURE_IN_PICTURE,
                            Process.myUid(),
                            BaseApplication.instance.packageName
                        ) == AppOpsManager.MODE_ALLOWED
                    }

                    else -> false
                }
            } ?: false
        } catch (exp: Exception) {
            false
        }
    }

    /**
     * Shows dialog when rational permission tries exhausted and user needs to enable permission
     * from settings
     * **/
    fun showPermissionSettingsDialog(
        activity: Activity,
        listener: DialogManager.AlertDialogListener? = null
    ) {
        com.core.utils.DialogManager().twoButtonDialog(
            context = activity,
            title = activity.getString(R.string.permission),
            message = activity.getString(R.string.grant_permission_from_settings),
            spannedMessage = false,
            positiveButtonText = activity.getString(R.string.open_settings),
            alertDialogListener = object : DialogManager.AlertDialogListener {
                override fun onPositiveButtonClicked() {
                    activity.let {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:" + it.packageName)
                        activity.startActivity(intent)
                        listener?.onPositiveButtonClicked()
                    }
                }

                override fun onNegativeButtonClicked() {
                    super.onNegativeButtonClicked()
                    listener?.onNegativeButtonClicked()
                }
            }
        )
    }

    fun checkForVideoPermission(): Boolean {
        return hasCameraPermission() && checkForAudioPermission()
    }

    fun checkForAudioPermission(): Boolean {
        val phoneStatePermission = ContextCompat.checkSelfPermission(
            BaseApplication.instance,
            Manifest.permission.READ_PHONE_STATE
        )
        return hasAudioPermission() && phoneStatePermission == PackageManager.PERMISSION_GRANTED
    }
}