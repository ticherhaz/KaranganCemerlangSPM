package net.ticherhaz.karangancemerlangspm.version2025.tools

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showNotificationRationaleDialog(
        context: Context,
        requestPermissionLauncher: ActivityResultLauncher<String>
    ) {
        AlertDialog.Builder(context)
            .setTitle("Notification Permission Needed")
            .setMessage("Our app needs permission to send you notifications for updates and reminders.")
            .setPositiveButton("OK") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("No thanks") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}