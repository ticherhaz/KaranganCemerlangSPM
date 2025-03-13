package net.ticherhaz.karangancemerlangspm.version2025.tools

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

object Tools {
    private const val TAG = "Tools"

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun simpleLog(message: String) {
        Log.d("???", message)
    }

    fun Context.goPlayStore() {
        val appPackageName = packageName // Use the context's package name
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where the Play Store is not installed
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
                startActivity(intent)
            } catch (e: Exception) {
                //Handle the error
                Log.e(TAG, "Error opening Play Store", e)
                showToast("Error opening Play Store")
            }
        }
    }

    fun Context.launchKcspmLiteApp() {
        val appPackageName =
            "net.ticherhaz.karangancemerlangspmlite" // Can also use context.packageName if needed
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    fun getPath(context: Context, uri: Uri): String? {
        //Check the API level
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            getPathAboveApi19(context, uri)
        } else {
            getPathBelowApi19(context, uri)
        }
    }

    private fun getPathBelowApi19(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting path: ", e)
            null
        } finally {
            cursor?.close()
        }
    }

    private fun getPathAboveApi19(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver

        // Check if the URI is a content URI
        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    return if (index != -1) {
                        it.getString(index)
                    } else {
                        uri.path
                    }
                }
            }
        } else if (ContentResolver.SCHEME_FILE == uri.scheme) {
            return uri.path
        }
        return null
    }
}