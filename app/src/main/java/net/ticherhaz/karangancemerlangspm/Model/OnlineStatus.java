package net.ticherhaz.karangancemerlangspm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class OnlineStatus implements Parcelable {

    public static final Creator<OnlineStatus> CREATOR = new Creator<OnlineStatus>() {
        @Override
        public OnlineStatus createFromParcel(Parcel in) {
            return new OnlineStatus(in);
        }

        @Override
        public OnlineStatus[] newArray(int size) {
            return new OnlineStatus[size];
        }
    };
    private String username;
    private String sekolah;
    private long reputation;

    public OnlineStatus() {
    }

    public OnlineStatus(String username, String sekolah, long reputation) {
        this.username = username;
        this.sekolah = sekolah;
        this.reputation = reputation;
    }

    protected OnlineStatus(Parcel in) {
        username = in.readString();
        sekolah = in.readString();
        reputation = in.readLong();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSekolah() {
        return sekolah;
    }

    public void setSekolah(String sekolah) {
        this.sekolah = sekolah;
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(sekolah);
        dest.writeLong(reputation);
    }
}
