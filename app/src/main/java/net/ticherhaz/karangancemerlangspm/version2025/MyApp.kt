package net.ticherhaz.karangancemerlangspm.version2025

import android.app.Application
import com.huawei.agconnect.AGCRoutePolicy
import dagger.hilt.android.HiltAndroidApp
import net.ticherhaz.karangancemerlangspm.huawei.model.CloudDBZoneWrapper
import net.ticherhaz.karangancemerlangspm.version2025.tools.QuickSave
import net.ticherhaz.tarikhmasa.TarikhMasa

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        QuickSave.Companion.initialize(applicationContext)
        initTarikhMasa()
        initHuaweiServices()
    }

    private fun initHuaweiServices() {
        try {
            CloudDBZoneWrapper.Companion.initAGConnectCloudDB(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initTarikhMasa() {
        TarikhMasa.AndroidThreeTenBP(this)
    }

    companion object {
        var regionRoutePolicy: AGCRoutePolicy = AGCRoutePolicy.SINGAPORE
    }
}