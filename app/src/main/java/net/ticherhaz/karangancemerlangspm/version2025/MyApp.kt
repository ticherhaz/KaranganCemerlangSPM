package net.ticherhaz.karangancemerlangspm.version2025

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import net.ticherhaz.karangancemerlangspm.version2025.tools.QuickSave

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        QuickSave.initialize(applicationContext)
    }
}