package net.ticherhaz.karangancemerlangspm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class HantarKarangan implements Parcelable {

    public static final Creator<HantarKarangan> CREATOR = new Creator<HantarKarangan>() {
        @Override
        public HantarKarangan createFromParcel(Parcel in) {
            return new HantarKarangan(in);
        }

        @Override
        public HantarKarangan[] newArray(int size) {
            return new HantarKarangan[size];
        }
    };
    private String hantarKaranganUid;
    private String userUid;
    private String name;
    private String sekolah;
    private String date;
    private String fileUid;

    public HantarKarangan(String hantarKaranganUid, String userUid, String name, String sekolah, String date, String fileUid) {
        this.hantarKaranganUid = hantarKaranganUid;
        this.userUid = userUid;
        this.name = name;
        this.sekolah = sekolah;
        this.date = date;
        this.fileUid = fileUid;
    }

    private HantarKarangan(Parcel in) {
        hantarKaranganUid = in.readString();
        userUid = in.readString();
        name = in.readString();
        sekolah = in.readString();
        date = in.readString();
        fileUid = in.readString();
    }

    public HantarKarangan() {
    }

    public String getHantarKaranganUid() {
        return hantarKaranganUid;
    }

    public void setHantarKaranganUid(String hantarKaranganUid) {
        this.hantarKaranganUid = hantarKaranganUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSekolah() {
        return sekolah;
    }

    public void setSekolah(String sekolah) {
        this.sekolah = sekolah;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFileUid() {
        return fileUid;
    }

    public void setFileUid(String fileUid) {
        this.fileUid = fileUid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hantarKaranganUid);
        dest.writeString(userUid);
        dest.writeString(name);
        dest.writeString(sekolah);
        dest.writeString(date);
        dest.writeString(fileUid);
    }
}
