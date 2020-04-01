package net.ticherhaz.karangancemerlangspm.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.model.Karangan;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Others {

    private static Toast toast;
    private static ProgressDialog progressDialog;

    public static void ShowProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static void DismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void messageInternetMessage(Context context) {
        Toast.makeText(context, "Sila pastikan internet anda stabil", Toast.LENGTH_SHORT).show();
    }

    public static void ShowToast(Context context, final String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            toast = null;
        } else {
            toast = null;
        }
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

    public static void setStatus(String status, TextView textViewStatus) {
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

    public enum PasswordStrength {
        WEAK(0, Color.RED), MEDIUM(1, Color.argb(255, 220, 185, 0)), STRONG(2, Color.GREEN), VERY_STRONG(3, Color.BLUE);

        //--------REQUIREMENTS--------
        static int REQUIRED_LENGTH = 8;
        static int MAXIMUM_LENGTH = 15;
        static boolean REQUIRE_SPECIAL_CHARACTERS = true;
        static boolean REQUIRE_DIGITS = true;
        static boolean REQUIRE_LOWER_CASE = true;
        static boolean REQUIRE_UPPER_CASE = true;

        int resId;
        int color;

        PasswordStrength(int resId, int color) {
            this.resId = resId;
            this.color = color;
        }

        public static PasswordStrength calculateStrength(String password) {
            int currentScore = 0;
            boolean sawUpper = false;
            boolean sawLower = false;
            boolean sawDigit = false;
            boolean sawSpecial = false;

            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);

                if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                    currentScore += 1;
                    sawSpecial = true;
                } else {
                    if (!sawDigit && Character.isDigit(c)) {
                        currentScore += 1;
                        sawDigit = true;
                    } else {
                        if (!sawUpper || !sawLower) {
                            if (Character.isUpperCase(c))
                                sawUpper = true;
                            else
                                sawLower = true;
                            if (sawUpper && sawLower)
                                currentScore += 1;
                        }
                    }
                }
            }

            if (password.length() > REQUIRED_LENGTH) {
                if ((REQUIRE_SPECIAL_CHARACTERS && !sawSpecial) || (REQUIRE_UPPER_CASE && !sawUpper) || (REQUIRE_LOWER_CASE && !sawLower) || (REQUIRE_DIGITS && !sawDigit)) {
                    currentScore = 1;
                } else {
                    currentScore = 2;
                    if (password.length() > MAXIMUM_LENGTH) {
                        currentScore = 3;
                    }
                }
            } else {
                currentScore = 0;
            }

            switch (currentScore) {
                case 0:
                    return WEAK;
                case 1:
                    return MEDIUM;
                case 2:
                    return STRONG;
                case 3:
                    return VERY_STRONG;
                default:
            }

            return VERY_STRONG;
        }

        public int getValue() {
            return resId;
        }

        public int getColor() {
            return color;
        }
    }
}
