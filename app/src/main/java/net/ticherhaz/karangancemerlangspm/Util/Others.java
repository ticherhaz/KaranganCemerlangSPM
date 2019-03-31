package net.ticherhaz.karangancemerlangspm.Util;

import android.widget.TextView;

import net.ticherhaz.karangancemerlangspm.R;

public class Others {

    public void setStatus(String status, TextView textViewStatus) {
        if (status.equals("Biasa")) {
            textViewStatus.setText(status);
            textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_neutral, 0, 0, 0);
        } else if (status.equals("Sedih")) {
            textViewStatus.setText(status);
            textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_sad, 0, 0, 0);
        } else if (status.equals("Gembira")) {
            textViewStatus.setText(status);
            textViewStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_status_happy, 0, 0, 0);
        }
    }
}
