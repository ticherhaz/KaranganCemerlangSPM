package net.ticherhaz.karangancemerlangspm.huawei.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Parse date to format as we need
 */
object DateUtils {
    /**
     * Format date to string in yyyy-MM-dd
     * @param date time input by caller
     * @return format string
     */
    fun formatDate(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.format(date.time)
    }

    /**
     * Parse date from an input string. Return current time if parsed failed
     * @param dateStr input date string
     * @return date from date string
     */
    fun parseDate(dateStr: String?): Date {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            return simpleDateFormat.parse(dateStr!!)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date(System.currentTimeMillis())
    }
}