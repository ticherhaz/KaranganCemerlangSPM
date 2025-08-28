package net.ticherhaz.karangancemerlangspm.huawei.model

import android.app.Activity
import android.util.Log
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.SignInResult

/**
 * Handle login events
 */
class LoginHelper(private val mActivity: Activity) {
    private val mLoginCallbacks: MutableList<OnLoginEventCallBack> = ArrayList()
    fun login() {
        val auth = AGConnectAuth.getInstance()
        auth.signInAnonymously().addOnSuccessListener(mActivity) { signInResult: SignInResult ->
            Log.i(TAG, "addOnSuccessListener: " + signInResult.user.displayName)
            for (loginEventCallBack in mLoginCallbacks) {
                loginEventCallBack.onLogin(true, signInResult)
            }
        }.addOnFailureListener(mActivity) { e: Exception ->
            Log.w(TAG, "Sign in for agc failed: " + e.message)
            for (loginEventCallBack in mLoginCallbacks) {
                loginEventCallBack.onLogOut(false)
            }
        }
    }

    fun logOut() {
        val auth = AGConnectAuth.getInstance()
        auth.signOut()
    }

    fun addLoginCallBack(loginEventCallBack: OnLoginEventCallBack) {
        if (!mLoginCallbacks.contains(loginEventCallBack)) {
            mLoginCallbacks.add(loginEventCallBack)
        }
    }

    interface OnLoginEventCallBack {
        fun onLogin(showLoginUserInfo: Boolean, signInResult: SignInResult?)
        fun onLogOut(showLoginUserInfo: Boolean)
    }

    companion object {
        private const val TAG = "LoginHelper"
    }
}