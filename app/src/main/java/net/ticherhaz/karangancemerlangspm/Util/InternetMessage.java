package net.ticherhaz.karangancemerlangspm.Util;

import android.os.Parcel;
import android.os.Parcelable;

public class InternetMessage implements Parcelable {

    public static final Creator<InternetMessage> CREATOR = new Creator<InternetMessage>() {
        @Override
        public InternetMessage createFromParcel(Parcel in) {
            return new InternetMessage(in);
        }

        @Override
        public InternetMessage[] newArray(int size) {
            return new InternetMessage[size];
        }
    };
    private String message;

    public InternetMessage() {
    }

    public InternetMessage(String message) {
        this.message = message;
    }

    protected InternetMessage(Parcel in) {
        message = in.readString();
    }

    public String getMessage() {
        return "Sila pastikan internet anda stabil";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }
}
