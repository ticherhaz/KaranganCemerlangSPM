package net.ticherhaz.karangancemerlangspm.huawei

import android.app.Application
import com.huawei.agconnect.AGCRoutePolicy
import net.ticherhaz.karangancemerlangspm.huawei.model.CloudDBZoneWrapper

class CloudDBQuickStartApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CloudDBZoneWrapper.initAGConnectCloudDB(this)
    }

    companion object {
        var regionRoutePolicy: AGCRoutePolicy = AGCRoutePolicy.SINGAPORE
    }
}