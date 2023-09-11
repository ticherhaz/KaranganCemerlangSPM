package net.ticherhaz.karangancemerlangspm.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogCustom {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context activity) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Memuatkan...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
