package net.ticherhaz.karangancemerlangspm.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import net.ticherhaz.karangancemerlangspm.Model.Karangan;
import net.ticherhaz.karangancemerlangspm.R;

public class Others {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void messageInternetMessage(Context context) {
        Toast.makeText(context, "Sila pastikan internet anda stabil", Toast.LENGTH_SHORT).show();
    }

    public void setStatus(String status, TextView textViewStatus) {
        switch (status) {
            case "Biasa":
                textViewStatus.setText(status);
                textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_neutral, 0, 0, 0);
                break;
            case "Sedih":
                textViewStatus.setText(status);
                textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_sad, 0, 0, 0);
                break;
            case "Gembira":
                textViewStatus.setText(status);
                textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_happy, 0, 0, 0);
                break;
        }
    }

    public void lastVisitedKarangan(DatabaseReference databaseReference, String userUid, Karangan model) {
        databaseReference.child("userAlpha").child(userUid).child("lastVisitedKarangan").setValue(model.getTajukPenuh());
    }
}
