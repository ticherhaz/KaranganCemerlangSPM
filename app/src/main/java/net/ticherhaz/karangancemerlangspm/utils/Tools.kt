package net.ticherhaz.karangancemerlangspm.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Base64
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


object Tools {
    fun showToast(context: Context?, message: String?) {
        val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun View.setOnClickCustom(debounceTime: Long = 1200L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    true
                } else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createImageName(): String {
        val offsetDateTime =
            OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
        return "IMG_$offsetDateTime"
    }

    fun fullNameFixer(fullName: String?): String {
        return fullName?.replace("\\s+".toRegex(), " ") ?: ""
    }

    /**
     * @param delayMillis   example 3000 (3 seconds)
     * @param delayListener cannot be null, after finished the delay, what will happen
     */
    fun createDelay(delayMillis: Long, delayListener: DelayListener) {
        val l = Looper.myLooper()
        if (l != null) {
            Handler(l).postDelayed({ delayListener.onFinished() }, delayMillis)
        }
    }


    fun formattedDateFromCalendar(calendarDate: Date): String? {
        val newDateFormat = "yyyy-MM-dd HH:mm:ss"
        val format = SimpleDateFormat(newDateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = calendarDate.time
        return format.format(calendar.time)
    }

    fun fixPhoneNumber(phoneNumber: String): String {
        return when {
            phoneNumber.startsWith("0") -> {
                "6$phoneNumber"
            }

            phoneNumber.startsWith("1") -> {
                "60$phoneNumber"
            }

            else -> {
                phoneNumber
            }
        }
    }

    fun RecyclerView.smoothSnapToPosition(
        position: Int,
        snapMode: Int = LinearSmoothScroller.SNAP_TO_START
    ) {
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int = snapMode
            override fun getHorizontalSnapPreference(): Int = snapMode
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    fun RecyclerView.immediateSnapToPosition() {
        isVisible = false
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scrollToPosition(0)
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                isVisible = true
            }
        })
    }

    // Vibrates the device for 100 milliseconds.
    @RequiresApi(Build.VERSION_CODES.M)
    private fun vibrateDevice(activity: Activity) {
        val vibrator = activity.getSystemService(Vibrator::class.java)
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= 26) {
                it.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(100)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun simpleCopyText(activity: Activity, view: View, key: String, value: String) {
        vibrateDevice(activity)
        val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(key, value)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(view, "Copied $key", Snackbar.LENGTH_LONG).show()
    }

    fun getStringImage(imageBitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    fun getBitmapImage(imageString: String): Bitmap {
        val imageBytes = Base64.decode(imageString, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun interface DelayListener {
        fun onFinished()
    }

    interface ShowDialogCallback {
        fun onButtonClosedClicked()
        fun onOutsideClicked()
    }

    interface ShowPopupConfirmationCallback {
        fun onConfirmClicked()
        fun onCancelClicked()
    }
}