package net.ticherhaz.karangancemerlangspm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String uid;
    private String lastVisitedKarangan;
    private String mostVisitedKarangan;
    private String phoneModel;
    private String ipAddress;
    private String lastSeen;

    public User() {
    }

    protected User(Parcel in) {
        uid = in.readString();
        lastVisitedKarangan = in.readString();
        mostVisitedKarangan = in.readString();
        phoneModel = in.readString();
        ipAddress = in.readString();
        lastSeen = in.readString();
    }

    public User(String uid, String lastVisitedKarangan, String mostVisitedKarangan, String phoneModel, String ipAddress, String lastSeen) {
        this.uid = uid;
        this.lastVisitedKarangan = lastVisitedKarangan;
        this.mostVisitedKarangan = mostVisitedKarangan;
        this.phoneModel = phoneModel;
        this.ipAddress = ipAddress;
        this.lastSeen = lastSeen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastVisitedKarangan() {
        return lastVisitedKarangan;
    }

    public void setLastVisitedKarangan(String lastVisitedKarangan) {
        this.lastVisitedKarangan = lastVisitedKarangan;
    }

    public String getMostVisitedKarangan() {
        return mostVisitedKarangan;
    }

    public void setMostVisitedKarangan(String mostVisitedKarangan) {
        this.mostVisitedKarangan = mostVisitedKarangan;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(lastVisitedKarangan);
        dest.writeString(mostVisitedKarangan);
        dest.writeString(phoneModel);
        dest.writeString(ipAddress);
        dest.writeString(lastSeen);
    }
}
