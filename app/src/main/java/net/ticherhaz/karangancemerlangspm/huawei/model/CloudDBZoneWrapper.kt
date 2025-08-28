package net.ticherhaz.karangancemerlangspm.huawei.model

import android.content.Context
import android.util.Log
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot
import com.huawei.agconnect.cloud.database.ListenerHandler
import com.huawei.agconnect.cloud.database.OnSnapshotListener
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import net.ticherhaz.karangancemerlangspm.huawei.CloudDBQuickStartApplication
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Proxying implementation of CloudDBZone.
 */
class CloudDBZoneWrapper {
    private var mCloudDB: AGConnectCloudDB = AGConnectCloudDB.getInstance()
    private var mCloudDBZone: CloudDBZone? = null
    private var mRegister: ListenerHandler? = null
    private var mConfig: CloudDBZoneConfig? = null
    private var mUiCallBack = UiCallBack.DEFAULT

    /**
     * Mark max id of book info. id is the primary key of [BookInfo], so we must provide an value for it
     * when upserting to database.
     */
    private var mBookIndex = 0
    private val mReadWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    /**
     * Monitor data change from database. Update book info list if data have changed
     */
    private val mSnapshotListener = OnSnapshotListener<Student> { cloudDBZoneSnapshot, e ->
        if (e != null) {
            Log.w(TAG, "onSnapshot: " + e.message)
            return@OnSnapshotListener
        }
        val snapshotObjects = cloudDBZoneSnapshot.snapshotObjects
        val bookInfoList: MutableList<Student> = ArrayList()
        try {
            if (snapshotObjects != null) {
                while (snapshotObjects.hasNext()) {
                    val bookInfo = snapshotObjects.next()
                    bookInfoList.add(bookInfo)
                    updateBookIndex(bookInfo)
                }
            }
            mUiCallBack.onSubscribe(bookInfoList)
        } catch (snapshotException: AGConnectCloudDBException) {
            Log.w(TAG, "onSnapshot:(getObject) " + snapshotException.message)
        } finally {
            cloudDBZoneSnapshot.release()
        }
    }

    fun setStorageLocation(context: Context?) {
        if (mCloudDBZone != null) {
            closeCloudDBZone()
        }
        val builder = AGConnectOptionsBuilder()
            .setRoutePolicy(CloudDBQuickStartApplication.regionRoutePolicy)
        val instance = AGConnectInstance.buildInstance(builder.build(context))
        mCloudDB = AGConnectCloudDB.getInstance(instance, AGConnectAuth.getInstance())
    }

    /**
     * Call AGConnectCloudDB.createObjectType to init schema
     */
    fun createObjectType() {
        try {
            mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())
            Log.i(TAG, "createObjectType: success")
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "createObjectType: " + e.message)
        }
    }

    /**
     * Call AGConnectCloudDB.openCloudDBZone to open a cloudDBZone.
     * We set it with cloud cache mode, and data can be store in local storage
     */
    fun openCloudDBZone() {
        mConfig = CloudDBZoneConfig(
            "Master",
            CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
        )
        mConfig!!.persistenceEnabled = true
        try {
            mCloudDBZone = mCloudDB.openCloudDBZone(mConfig!!, true)
            Log.w(TAG, "openCloudDBZone: success")
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "openCloudDBZone: " + e.message)
        }
    }

    /**
     * Call AGConnectCloudDB.openCloudDBZone2 to open a cloudDBZone.
     * We set it with cloud cache mode, and data can be store in local storage.
     * AGConnectCloudDB.openCloudDBZone2 is an asynchronous method, we can add
     * OnSuccessListener/OnFailureListener to receive the result for opening cloudDBZone
     */
    fun openCloudDBZoneV2() {
        mConfig = CloudDBZoneConfig(
            "Master",
            CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
        )
        mConfig!!.persistenceEnabled = true
        val task = mCloudDB.openCloudDBZone2(mConfig!!, true)
        task.addOnSuccessListener {
            Log.i(TAG, "Open cloudDBZone success")
            mCloudDBZone = it
            addSubscription()
        }.addOnFailureListener {
            Log.w(TAG, "Open cloudDBZone failed for " + it.message)
        }
    }

    /**
     * Call AGConnectCloudDB.closeCloudDBZone
     */
    fun closeCloudDBZone() {
        try {
            mRegister?.remove()
            mCloudDB.closeCloudDBZone(mCloudDBZone)
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "closeCloudDBZone: " + e.message)
        }
    }

    /**
     * Call AGConnectCloudDB.deleteCloudDBZone
     */
    fun deleteCloudDBZone() {
        try {
            mCloudDB.deleteCloudDBZone(mConfig!!.cloudDBZoneName)
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "deleteCloudDBZone: " + e.message)
        }
    }

    /**
     * Add a callback to update book info list
     *
     * @param uiCallBack callback to update book list
     */
    fun addCallBacks(uiCallBack: UiCallBack) {
        mUiCallBack = uiCallBack
    }

    /**
     * Add mSnapshotListener to monitor data changes from storage
     */
    private fun addSubscription() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        try {
            val snapshotQuery = CloudDBZoneQuery.where(Student::class.java)
                .equalTo("tempX", "")
            mRegister = mCloudDBZone!!.subscribeSnapshot(
                snapshotQuery,
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY,
                mSnapshotListener
            )
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "subscribeSnapshot: " + e.message)
        }
    }

    /**
     * Query all books in storage from cloud side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
     */
    fun queryAllBooks() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val queryTask = mCloudDBZone!!.executeQuery(
            CloudDBZoneQuery.where(Student::class.java),
            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        )
        queryTask.addOnSuccessListener { snapshot -> processQueryResult(snapshot) }
            .addOnFailureListener {
                mUiCallBack.updateUiOnError("Query book list from cloud failed")
            }
    }

    /**
     * Query books with condition
     *
     * @param query query condition
     */
    fun queryBooks(query: CloudDBZoneQuery<Student>) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }

        val queryTask = mCloudDBZone!!.executeQuery(
            query,
            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        )
        queryTask.addOnSuccessListener { snapshot -> processQueryResult(snapshot) }
            .addOnFailureListener { mUiCallBack.updateUiOnError("Query failed") }
    }

    private fun processQueryResult(snapshot: CloudDBZoneSnapshot<Student>) {
        val bookInfoCursor = snapshot.snapshotObjects
        val bookInfoList: MutableList<Student> = ArrayList()
        try {
            while (bookInfoCursor.hasNext()) {
                val bookInfo = bookInfoCursor.next()
                bookInfoList.add(bookInfo)
            }
        } catch (e: AGConnectCloudDBException) {
            Log.w(TAG, "processQueryResult: " + e.message)
        } finally {
            snapshot.release()
        }
        mUiCallBack.onAddOrQuery(bookInfoList)
    }

    /**
     * Upsert bookinfo
     *
     * @param bookInfo bookinfo added or modified from local
     */
    fun upsertBookInfos(bookInfo: Student?) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "upsertBookInfos: CloudDBZone is null, try re-open it")
            return
        }
        Log.i(TAG, "upsertBookInfos: start")
        val upsertTask = mCloudDBZone!!.executeUpsert(bookInfo!!)
        Log.w(TAG, "upsertBookInfos: start 2")
        upsertTask.addOnSuccessListener { cloudDBZoneResult ->
            Log.i(TAG, "Upsert $cloudDBZoneResult records")
        }.addOnFailureListener {
            Log.i(TAG, "Upsert failed: " + it.message)
            mUiCallBack.updateUiOnError("Insert book info failed")
        }
    }

    /**
     * Delete bookinfo
     *
     * @param bookInfoList books selected by user
     */
    fun deleteBookInfos(bookInfoList: List<Student?>?) {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val deleteTask = mCloudDBZone!!.executeDelete(bookInfoList!!)
        if (deleteTask.exception != null) {
            mUiCallBack.updateUiOnError("Delete book info failed")
            return
        }
        mUiCallBack.onDelete(bookInfoList)
    }

    private fun updateBookIndex(bookInfo: Student) {
        try {
            mReadWriteLock.writeLock().lock()
            if (mBookIndex < bookInfo.studentId!!.toInt()) {
                mBookIndex = bookInfo.studentId!!.toInt()
            }
        } finally {
            mReadWriteLock.writeLock().unlock()
        }
    }

    /**
     * Get max id of bookinfos
     *
     * @return max book info id
     */
    val bookIndex: Int
        get() = try {
            mReadWriteLock.readLock().lock()
            mBookIndex
        } finally {
            mReadWriteLock.readLock().unlock()
        }

    /**
     * Call back to update ui in HomePageFragment
     */
    interface UiCallBack {
        fun onAddOrQuery(bookInfoList: List<Student>)
        fun onSubscribe(bookInfoList: List<Student>?)
        fun onDelete(bookInfoList: List<Student?>?)
        fun updateUiOnError(errorMessage: String?)

        companion object {
            val DEFAULT: UiCallBack = object : UiCallBack {
                override fun onAddOrQuery(bookInfoList: List<Student>) {
                    Log.i(TAG, "Using default onAddOrQuery")
                }

                override fun onSubscribe(bookInfoList: List<Student>?) {
                    Log.i(TAG, "Using default onSubscribe")
                }

                override fun onDelete(bookInfoList: List<Student?>?) {
                    Log.i(TAG, "Using default onDelete")
                }

                override fun updateUiOnError(errorMessage: String?) {
                    Log.i(TAG, "Using default updateUiOnError")
                }
            }
        }
    }

    companion object {
        private const val TAG = "CloudDBZoneWrapper"

        /**
         * Init AGConnectCloudDB in Application
         *
         * @param context application context
         */
        fun initAGConnectCloudDB(context: Context?) {
            AGConnectCloudDB.initialize(context!!)
        }
    }
}