package net.ticherhaz.karangancemerlangspm.version2025.tools

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuickSave private constructor(context: Context) {

    companion object {
        private const val SECRET_SHARED_PREFS = "karangancemerlangspm.quick_save"

        @Volatile
        private var instance: QuickSave? = null

        fun initialize(context: Context) {
            synchronized(this) {
                if (instance == null) {
                    instance = QuickSave(context.applicationContext)
                }
            }
        }

        fun getInstance(): QuickSave {
            return instance ?: throw IllegalStateException(
                "QuickSave must be initialized. Call QuickSave.initialize(context) first."
            )
        }
    }

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    val encryptedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            SECRET_SHARED_PREFS,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun save(key: String, value: Any) {
        encryptedPreferences.edit {
            when (value) {
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    inline fun <reified T> get(key: String, defaultValue: T): T {
        return when (T::class) {
            Int::class -> encryptedPreferences.getInt(key, defaultValue as Int) as T
            Boolean::class -> encryptedPreferences.getBoolean(key, defaultValue as Boolean) as T
            Float::class -> encryptedPreferences.getFloat(key, defaultValue as Float) as T
            Long::class -> encryptedPreferences.getLong(key, defaultValue as Long) as T
            String::class -> encryptedPreferences.getString(key, defaultValue as? String) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    fun <T> saveObject(key: String, `object`: T) {
        try {
            val objectString = Gson().toJson(`object`)
            save(key, objectString)
        } catch (e: Exception) {
            Log.e("QuickSave", "Error saving object for key: $key", e)
        }
    }

    inline fun <reified T> getObject(key: String): T? {
        return try {
            val objectString: String? = get(key, null as String?)
            objectString?.let {
                val type = object : TypeToken<T>() {}.type
                Gson().fromJson(it, type)
            }
        } catch (e: Exception) {
            Log.e("QuickSave", "Error retrieving object for key: $key", e)
            null
        }
    }

    fun <T> saveObjectsList(key: String, objectList: List<T>) {
        try {
            val objectString = Gson().toJson(objectList)
            save(key, objectString)
        } catch (e: Exception) {
            Log.e("QuickSave", "Error saving objects list", e)
        }
    }

    inline fun <reified T> getObjectsList(key: String): List<T>? {
        return try {
            val objectString: String? = get(key, null as String?)
            objectString?.let {
                Gson().fromJson(it, object : TypeToken<List<T>>() {}.type)
            }
        } catch (e: Exception) {
            Log.e("QuickSave", "Error retrieving objects list", e)
            null
        }
    }

    fun clearSession() {
        encryptedPreferences.edit { clear() }
    }

    fun deleteValue(key: String): Boolean {
        return if (encryptedPreferences.contains(key)) {
            encryptedPreferences.edit { remove(key) }
            true
        } else {
            false
        }
    }

    fun isKeyExists(key: String): Boolean {
        return encryptedPreferences.contains(key)
    }
}