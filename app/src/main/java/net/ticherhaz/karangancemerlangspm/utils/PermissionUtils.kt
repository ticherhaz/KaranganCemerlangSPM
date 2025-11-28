package net.ticherhaz.karangancemerlangspm.utils

import android.app.Activity
import android.os.Build
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.PermissionLists
import com.hjq.permissions.permission.base.IPermission

object PermissionUtils {

    fun storagePermissions(activity: Activity, iStoragePermission: IStoragePermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (!XXPermissions.isGrantedPermissions(
                    activity,
                    mutableListOf(
                        PermissionLists.getReadMediaImagesPermission(),
                        PermissionLists.getReadMediaVideoPermission(),
                        PermissionLists.getReadMediaAudioPermission(),
                        PermissionLists.getCameraPermission(),
                        PermissionLists.getPostNotificationsPermission()
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
                                .permission(PermissionLists.getWriteExternalStoragePermission())
                                .permission(PermissionLists.getPostNotificationsPermission())
                                .permission(PermissionLists.getCameraPermission())
                                .request(object : OnPermissionCallback {
                                    override fun onResult(
                                        grantedList: List<IPermission?>,
                                        deniedList: List<IPermission?>
                                    ) {

                                        val allGranted = deniedList.isEmpty()
                                        if (!allGranted) {
                                            Tools.showToast(
                                                activity,
                                                "Some permissions were obtained successfully, but some permissions were not granted normally"
                                            )

                                            iStoragePermission.onFailure()
                                            return
                                        }
                                        Tools.showToast(
                                            activity,
                                            "Acquired permissions successfully"
                                        )
                                        iStoragePermission.onSuccess()
                                    }
                                })
                        }
                    })

            } else {
                iStoragePermission.onSuccess()
            }

        } else {

            if (!XXPermissions.isGrantedPermissions(
                    activity,
                    listOf(
                        PermissionLists.getReadMediaImagesPermission(),
                        PermissionLists.getReadMediaVideoPermission(),
                        PermissionLists.getReadMediaAudioPermission(),
                        PermissionLists.getCameraPermission(),
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
                                .permission(PermissionLists.getReadMediaImagesPermission())
                                .permission(PermissionLists.getReadMediaVideoPermission())
                                .permission(PermissionLists.getReadMediaAudioPermission())
                                .permission(PermissionLists.getCameraPermission())
                                .request(object : OnPermissionCallback {
                                    override fun onResult(
                                        grantedList: List<IPermission?>,
                                        deniedList: List<IPermission?>
                                    ) {

                                        val allGranted = deniedList.isEmpty()
                                        if (!allGranted) {
                                            Tools.showToast(
                                                activity,
                                                "Some permissions were obtained successfully, but some permissions were not granted normally"
                                            )

                                            iStoragePermission.onFailure()
                                            return
                                        }
                                        Tools.showToast(
                                            activity,
                                            "Acquired permissions successfully"
                                        )
                                        iStoragePermission.onSuccess()
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