package net.ticherhaz.karangancemerlangspm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Jenis implements Parcelable {

    public static final Creator<Jenis> CREATOR = new Creator<Jenis>() {
        @Override
        public Jenis createFromParcel(Parcel in) {
            return new Jenis(in);
        }

        @Override
        public Jenis[] newArray(int size) {
            return new Jenis[size];
        }
    };
    private String jenisUid;
    private String title;
    private String description;

    public Jenis(String jenisUid, String title, String description) {
        this.jenisUid = jenisUid;
        this.title = title;
        this.description = description;
    }

    public Jenis() {
    }

    private Jenis(Parcel in) {
        jenisUid = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public String getJenisUid() {
        return jenisUid;
    }

    public void setJenisUid(String jenisUid) {
        this.jenisUid = jenisUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jenisUid);
        dest.writeString(title);
        dest.writeString(description);
    }
}
