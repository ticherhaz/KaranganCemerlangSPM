package net.ticherhaz.karangancemerlangspm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UsernameCheck implements Parcelable {

    public static final Creator<UsernameCheck> CREATOR = new Creator<UsernameCheck>() {
        @Override
        public UsernameCheck createFromParcel(Parcel in) {
            return new UsernameCheck(in);
        }

        @Override
        public UsernameCheck[] newArray(int size) {
            return new UsernameCheck[size];
        }
    };
    private String username;

    public UsernameCheck(String username) {
        this.username = username;
    }

    private UsernameCheck(Parcel in) {
        username = in.readString();
    }

    public UsernameCheck() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
    }
}
