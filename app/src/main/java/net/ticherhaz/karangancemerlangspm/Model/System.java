package net.ticherhaz.karangancemerlangspm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class System implements Parcelable {

    public static final Creator<System> CREATOR = new Creator<System>() {
        @Override
        public System createFromParcel(Parcel in) {
            return new System(in);
        }

        @Override
        public System[] newArray(int size) {
            return new System[size];
        }
    };
    private boolean mod;
    private int versi;

    private System(Parcel in) {
        mod = in.readByte() != 0;
        versi = in.readInt();
    }

    public System() {
        //Don't delete constructor
    }

    public boolean isMod() {
        return mod;
    }

    public int getVersi() {
        return versi;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mod ? 1 : 0));
        dest.writeInt(versi);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
