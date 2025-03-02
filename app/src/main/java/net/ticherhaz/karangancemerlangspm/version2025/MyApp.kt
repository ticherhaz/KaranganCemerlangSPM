package net.ticherhaz.karangancemerlangspm.version2025

import android.app.Application
import net.ticherhaz.karangancemerlangspm.version2025.tools.QuickSave

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        QuickSave.init(applicationContext)
    }
}