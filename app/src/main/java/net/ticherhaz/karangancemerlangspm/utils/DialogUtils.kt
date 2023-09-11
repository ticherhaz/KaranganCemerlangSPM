package net.ticherhaz.karangancemerlangspm.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import net.ticherhaz.karangancemerlangspm.databinding.DialogInfoPermissionsBinding

object DialogUtils {

    fun showDialogInfoPermissions(
        activity: Activity,
        iDialogInfoPermissions: IDialogInfoPermissions
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = DialogInfoPermissionsBinding.inflate(activity.layoutInflater)
        val root = view.root
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        view.btnCancel.setOnClickListener {
            dialog.dismiss()
            iDialogInfoPermissions.onBtnCancelClicked()
        }

        view.btnContinue.setOnClickListener {
            dialog.dismiss()
            iDialogInfoPermissions.onBtnContinueClicked()
        }

        dialog.show()
        dialog.window!!.attributes = lp
    }

    interface IDialogInfoPermissions {
        fun onBtnCancelClicked()
        fun onBtnContinueClicked()
    }
}