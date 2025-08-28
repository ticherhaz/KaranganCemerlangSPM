package net.ticherhaz.karangancemerlangspm.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

object PermissionUtils {

    fun storagePermissions(activity: Activity, iStoragePermission: IStoragePermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (!XXPermissions.isGranted(
                    activity,
                    mutableListOf(
                        Permission.READ_MEDIA_IMAGES,
                        Permission.READ_MEDIA_VIDEO,
                        Permission.READ_MEDIA_AUDIO,
                        Permission.POST_NOTIFICATIONS,
                        Permission.CAMERA
                    )
                )
            ) {

                DialogUtils.showDialogInfoPermissions(
                    activity,
                    object : DialogUtils.IDialogInfoPermissions {
                        override fun onBtnCancelClicked() {
                            iStoragePermission.onFailure()
                        }

                        override fun onBtnContinueClicked() {

                            XXPermissions.with(activity)
                                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                                .permission(Permission.POST_NOTIFICATIONS)
                                .permission(Permission.CAMERA)
                                .request(object : OnPermissionCallback {
                                    override fun onGranted(
                                        permissions: MutableList<String>,
                                        allGranted: Boolean
                                    ) {
                                        if (!allGranted) {
                                            Tools.showToast(
                                                activity,
                                                "Some permissions were obtained successfully, but some permissions were not granted normally"
                                            )

                                            iStoragePermission.onSuccess()
                                            return
                                        }
                                        Tools.showToast(
                                            activity,
                                            "Acquired permissions successfully"
                                        )

                                        iStoragePermission.onSuccess()
                                    }

                                    override fun onDenied(
                                        permissions: MutableList<String>,
                                        doNotAskAgain: Boolean
                                    ) {
                                        if (doNotAskAgain) {

                                            // navigate user to app settings
                                            val alertDialog = AlertDialog.Builder(activity)
                                                .setTitle("Info")
                                                .setMessage("Authorization denied permanently, please grant permissions manually")
                                                .setPositiveButton("Proceed") { _: DialogInterface?, _: Int ->

                                                    // If it is permanently denied, jump to the application permission system settings page
                                                    XXPermissions.startPermissionActivity(
                                                        activity,
                                                        permissions
                                                    )


                                                }
                                                .create()
                                            alertDialog.show()

                                            iStoragePermission.onFailure()

                                        } else {
                                            Tools.showToast(
                                                activity,
                                                "Failed to get permissions"
                                            )

                                            iStoragePermission.onFailure()
                                        }
                                    }
                                })
                        }
                    })

            } else {
                iStoragePermission.onSuccess()
            }

        } else {

            if (!XXPermissions.isGranted(
                    activity,
                    listOf(
                        Permission.READ_MEDIA_IMAGES,
                        Permission.READ_MEDIA_VIDEO,
                        Permission.READ_MEDIA_AUDIO,
                        Permission.CAMERA
                    )
                )
            ) {

                DialogUtils.showDialogInfoPermissions(
                    activity,
                    object : DialogUtils.IDialogInfoPermissions {
                        override fun onBtnCancelClicked() {
                            iStoragePermission.onFailure()
                        }

                        override fun onBtnContinueClicked() {

                            XXPermissions.with(activity)
                                .permission(Permission.READ_MEDIA_IMAGES)
                                .permission(Permission.READ_MEDIA_VIDEO)
                                .permission(Permission.READ_MEDIA_AUDIO)
                                .permission(Permission.CAMERA)
                                .request(object : OnPermissionCallback {
                                    override fun onGranted(
                                        permissions: MutableList<String>,
                                        allGranted: Boolean
                                    ) {
                                        if (!allGranted) {
                                            Tools.showToast(
                                                activity,
                                                "Some permissions were obtained successfully, but some permissions were not granted normally"
                                            )
                                            iStoragePermission.onSuccess()
                                            return
                                        }
                                        Tools.showToast(
                                            activity,
                                            "Acquired permissions successfully"
                                        )
                                        iStoragePermission.onSuccess()
                                    }

                                    override fun onDenied(
                                        permissions: MutableList<String>,
                                        doNotAskAgain: Boolean
                                    ) {
                                        if (doNotAskAgain) {

                                            // navigate user to app settings
                                            val alertDialog = AlertDialog.Builder(activity)
                                                .setTitle("Info")
                                                .setMessage("Authorization denied permanently, please grant permissions manually")
                                                .setPositiveButton("Proceed") { _: DialogInterface?, _: Int ->

                                                    // If it is permanently denied, jump to the application permission system settings page
                                                    XXPermissions.startPermissionActivity(
                                                        activity,
                                                        permissions
                                                    )


                                                }
                                                .create()
                                            alertDialog.show()

                                            iStoragePermission.onFailure()

                                        } else {
                                            Tools.showToast(
                                                activity,
                                                "Failed to get permissions"
                                            )

                                            iStoragePermission.onFailure()
                                        }
                                    }
                                })
                        }
                    })

            } else {
                iStoragePermission.onSuccess()
            }
        }
    }

    interface IStoragePermission {
        fun onSuccess()
        fun onFailure()
    }
}