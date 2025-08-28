package net.ticherhaz.karangancemerlangspm.huawei.model

import com.huawei.agconnect.AGCRoutePolicy
import net.ticherhaz.karangancemerlangspm.huawei.CloudDBQuickStartApplication

class StorageLocationHelper {
    private val mCallbacks: MutableList<StorageLocationChangeCallback> = ArrayList()

    fun changeLocation(routePolicy: AGCRoutePolicy?) {
        if (CloudDBQuickStartApplication.regionRoutePolicy === routePolicy) {
            return
        }
        if (routePolicy != null) {
            CloudDBQuickStartApplication.regionRoutePolicy = routePolicy
        }
        for (callback in mCallbacks) {
            callback.onStorageLocationChanged()
        }
    }

    fun addCallback(callback: StorageLocationChangeCallback) {
        mCallbacks.add(callback)
    }

    interface StorageLocationChangeCallback {
        fun onStorageLocationChanged()
    }
}