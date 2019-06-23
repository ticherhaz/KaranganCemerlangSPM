package net.ticherhaz.karangancemerlangspm.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import net.ticherhaz.karangancemerlangspm.Model.Karangan;
import net.ticherhaz.karangancemerlangspm.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

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

    //Method to get IP Address
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        } // for now eat exceptions
        return "";
    }

    @SuppressLint("HardwareIds")
    public static String getMACAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1)
                        hex = "0".concat(hex);
                    res1.append(hex.concat(":"));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ignored) {
        }
        return "";
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
        databaseReference.child("userFirst").child(userUid).child("lastVisitedKarangan").setValue(model.getTajukPenuh());
    }
}
