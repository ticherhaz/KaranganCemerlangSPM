package net.ticherhaz.karangancemerlangspm.util;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogCustom {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Activity activity) {
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
