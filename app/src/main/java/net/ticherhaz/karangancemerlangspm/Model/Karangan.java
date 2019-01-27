package net.ticherhaz.karangancemerlangspm.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Karangan implements Parcelable {
    public static final Creator<Karangan> CREATOR = new Creator<Karangan>() {
        @Override
        public Karangan createFromParcel(Parcel in) {
            return new Karangan(in);
        }

        @Override
        public Karangan[] newArray(int size) {
            return new Karangan[size];
        }
    };
    //Main Variables
    //So, we have to divide the title at very specific because
    //we want to search them.
    //Example: Membaca Amalan Mulia
    //So it will be like this, tajuk1 = "Membaca", tajuk2 = "Amalan", tajuk3 = "Mulia"
    //And the rest will become empty
    private String uid;
    private String tajukPenuh;
    private String tajuk1;
    private String tajuk2;
    private String tajuk3;
    private String tajuk4;
    private String tajuk5;
    private String tajuk6;
    private String tajuk7;
    private String tajuk8;
    private String tajuk9;
    private String deskripsiPenuh;
    private String deskripsi1;
    private String deskripsi2;
    private String deskripsi3;
    private String deskripsi4;
    private String deskripsi5;
    private String deskripsi6;
    private String deskripsi7;
    private String deskripsi8;
    private String deskripsi9;
    private String tarikh;
    private String karangan;
    private int vote;    //When user press thumb up button
    private int mostVisited;    //Every time user click the title
    private String userLastVisitedDate; //When user press title, the record of last visited updated
    //UpperCase Variables
    private String tajukPenuhUpperCase;
    private String tajuk1UpperCase;
    private String tajuk2UpperCase;
    private String tajuk3UpperCase;
    private String tajuk4UpperCase;
    private String tajuk5UpperCase;
    private String tajuk6UpperCase;
    private String tajuk7UpperCase;
    private String tajuk8UpperCase;
    private String tajuk9UpperCase;
    private String deskripsiPenuhUpperCase;
    private String deskripsi1UpperCase;
    private String deskripsi2UpperCase;
    private String deskripsi3UpperCase;
    private String deskripsi4UpperCase;
    private String deskripsi5UpperCase;
    private String deskripsi6UpperCase;
    private String deskripsi7UpperCase;
    private String deskripsi8UpperCase;
    private String deskripsi9UpperCase;

    public Karangan() {
    }

    public Karangan(String uid, String tajukPenuh, String tajuk1, String tajuk2, String tajuk3, String tajuk4, String tajuk5, String tajuk6, String tajuk7, String tajuk8, String tajuk9, String deskripsiPenuh, String deskripsi1, String deskripsi2, String deskripsi3, String deskripsi4, String deskripsi5, String deskripsi6, String deskripsi7, String deskripsi8, String deskripsi9, String tarikh, String karangan, int vote, int mostVisited, String userLastVisitedDate, String tajukPenuhUpperCase, String tajuk1UpperCase, String tajuk2UpperCase, String tajuk3UpperCase, String tajuk4UpperCase, String tajuk5UpperCase, String tajuk6UpperCase, String tajuk7UpperCase, String tajuk8UpperCase, String tajuk9UpperCase, String deskripsiPenuhUpperCase, String deskripsi1UpperCase, String deskripsi2UpperCase, String deskripsi3UpperCase, String deskripsi4UpperCase, String deskripsi5UpperCase, String deskripsi6UpperCase, String deskripsi7UpperCase, String deskripsi8UpperCase, String deskripsi9UpperCase) {
        this.uid = uid;
        this.tajukPenuh = tajukPenuh;
        this.tajuk1 = tajuk1;
        this.tajuk2 = tajuk2;
        this.tajuk3 = tajuk3;
        this.tajuk4 = tajuk4;
        this.tajuk5 = tajuk5;
        this.tajuk6 = tajuk6;
        this.tajuk7 = tajuk7;
        this.tajuk8 = tajuk8;
        this.tajuk9 = tajuk9;
        this.deskripsiPenuh = deskripsiPenuh;
        this.deskripsi1 = deskripsi1;
        this.deskripsi2 = deskripsi2;
        this.deskripsi3 = deskripsi3;
        this.deskripsi4 = deskripsi4;
        this.deskripsi5 = deskripsi5;
        this.deskripsi6 = deskripsi6;
        this.deskripsi7 = deskripsi7;
        this.deskripsi8 = deskripsi8;
        this.deskripsi9 = deskripsi9;
        this.tarikh = tarikh;
        this.karangan = karangan;
        this.vote = vote;
        this.mostVisited = mostVisited;
        this.userLastVisitedDate = userLastVisitedDate;
        this.tajukPenuhUpperCase = tajukPenuhUpperCase;
        this.tajuk1UpperCase = tajuk1UpperCase;
        this.tajuk2UpperCase = tajuk2UpperCase;
        this.tajuk3UpperCase = tajuk3UpperCase;
        this.tajuk4UpperCase = tajuk4UpperCase;
        this.tajuk5UpperCase = tajuk5UpperCase;
        this.tajuk6UpperCase = tajuk6UpperCase;
        this.tajuk7UpperCase = tajuk7UpperCase;
        this.tajuk8UpperCase = tajuk8UpperCase;
        this.tajuk9UpperCase = tajuk9UpperCase;
        this.deskripsiPenuhUpperCase = deskripsiPenuhUpperCase;
        this.deskripsi1UpperCase = deskripsi1UpperCase;
        this.deskripsi2UpperCase = deskripsi2UpperCase;
        this.deskripsi3UpperCase = deskripsi3UpperCase;
        this.deskripsi4UpperCase = deskripsi4UpperCase;
        this.deskripsi5UpperCase = deskripsi5UpperCase;
        this.deskripsi6UpperCase = deskripsi6UpperCase;
        this.deskripsi7UpperCase = deskripsi7UpperCase;
        this.deskripsi8UpperCase = deskripsi8UpperCase;
        this.deskripsi9UpperCase = deskripsi9UpperCase;
    }

    protected Karangan(Parcel in) {
        uid = in.readString();
        tajukPenuh = in.readString();
        tajuk1 = in.readString();
        tajuk2 = in.readString();
        tajuk3 = in.readString();
        tajuk4 = in.readString();
        tajuk5 = in.readString();
        tajuk6 = in.readString();
        tajuk7 = in.readString();
        tajuk8 = in.readString();
        tajuk9 = in.readString();
        deskripsiPenuh = in.readString();
        deskripsi1 = in.readString();
        deskripsi2 = in.readString();
        deskripsi3 = in.readString();
        deskripsi4 = in.readString();
        deskripsi5 = in.readString();
        deskripsi6 = in.readString();
        deskripsi7 = in.readString();
        deskripsi8 = in.readString();
        deskripsi9 = in.readString();
        tarikh = in.readString();
        karangan = in.readString();
        vote = in.readInt();
        mostVisited = in.readInt();
        userLastVisitedDate = in.readString();
        tajukPenuhUpperCase = in.readString();
        tajuk1UpperCase = in.readString();
        tajuk2UpperCase = in.readString();
        tajuk3UpperCase = in.readString();
        tajuk4UpperCase = in.readString();
        tajuk5UpperCase = in.readString();
        tajuk6UpperCase = in.readString();
        tajuk7UpperCase = in.readString();
        tajuk8UpperCase = in.readString();
        tajuk9UpperCase = in.readString();
        deskripsiPenuhUpperCase = in.readString();
        deskripsi1UpperCase = in.readString();
        deskripsi2UpperCase = in.readString();
        deskripsi3UpperCase = in.readString();
        deskripsi4UpperCase = in.readString();
        deskripsi5UpperCase = in.readString();
        deskripsi6UpperCase = in.readString();
        deskripsi7UpperCase = in.readString();
        deskripsi8UpperCase = in.readString();
        deskripsi9UpperCase = in.readString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTajukPenuh() {
        return tajukPenuh;
    }

    public void setTajukPenuh(String tajukPenuh) {
        this.tajukPenuh = tajukPenuh;
    }

    public String getTajuk1() {
        return tajuk1;
    }

    public void setTajuk1(String tajuk1) {
        this.tajuk1 = tajuk1;
    }

    public String getTajuk2() {
        return tajuk2;
    }

    public void setTajuk2(String tajuk2) {
        this.tajuk2 = tajuk2;
    }

    public String getTajuk3() {
        return tajuk3;
    }

    public void setTajuk3(String tajuk3) {
        this.tajuk3 = tajuk3;
    }

    public String getTajuk4() {
        return tajuk4;
    }

    public void setTajuk4(String tajuk4) {
        this.tajuk4 = tajuk4;
    }

    public String getTajuk5() {
        return tajuk5;
    }

    public void setTajuk5(String tajuk5) {
        this.tajuk5 = tajuk5;
    }

    public String getTajuk6() {
        return tajuk6;
    }

    public void setTajuk6(String tajuk6) {
        this.tajuk6 = tajuk6;
    }

    public String getTajuk7() {
        return tajuk7;
    }

    public void setTajuk7(String tajuk7) {
        this.tajuk7 = tajuk7;
    }

    public String getTajuk8() {
        return tajuk8;
    }

    public void setTajuk8(String tajuk8) {
        this.tajuk8 = tajuk8;
    }

    public String getTajuk9() {
        return tajuk9;
    }

    public void setTajuk9(String tajuk9) {
        this.tajuk9 = tajuk9;
    }

    public String getDeskripsiPenuh() {
        return deskripsiPenuh;
    }

    public void setDeskripsiPenuh(String deskripsiPenuh) {
        this.deskripsiPenuh = deskripsiPenuh;
    }

    public String getDeskripsi1() {
        return deskripsi1;
    }

    public void setDeskripsi1(String deskripsi1) {
        this.deskripsi1 = deskripsi1;
    }

    public String getDeskripsi2() {
        return deskripsi2;
    }

    public void setDeskripsi2(String deskripsi2) {
        this.deskripsi2 = deskripsi2;
    }

    public String getDeskripsi3() {
        return deskripsi3;
    }

    public void setDeskripsi3(String deskripsi3) {
        this.deskripsi3 = deskripsi3;
    }

    public String getDeskripsi4() {
        return deskripsi4;
    }

    public void setDeskripsi4(String deskripsi4) {
        this.deskripsi4 = deskripsi4;
    }

    public String getDeskripsi5() {
        return deskripsi5;
    }

    public void setDeskripsi5(String deskripsi5) {
        this.deskripsi5 = deskripsi5;
    }

    public String getDeskripsi6() {
        return deskripsi6;
    }

    public void setDeskripsi6(String deskripsi6) {
        this.deskripsi6 = deskripsi6;
    }

    public String getDeskripsi7() {
        return deskripsi7;
    }

    public void setDeskripsi7(String deskripsi7) {
        this.deskripsi7 = deskripsi7;
    }

    public String getDeskripsi8() {
        return deskripsi8;
    }

    public void setDeskripsi8(String deskripsi8) {
        this.deskripsi8 = deskripsi8;
    }

    public String getDeskripsi9() {
        return deskripsi9;
    }

    public void setDeskripsi9(String deskripsi9) {
        this.deskripsi9 = deskripsi9;
    }

    public String getTarikh() {
        return tarikh;
    }

    public void setTarikh(String tarikh) {
        this.tarikh = tarikh;
    }

    public String getKarangan() {
        return karangan;
    }

    public void setKarangan(String karangan) {
        this.karangan = karangan;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getMostVisited() {
        return mostVisited;
    }

    public void setMostVisited(int mostVisited) {
        this.mostVisited = mostVisited;
    }

    public String getUserLastVisitedDate() {
        return userLastVisitedDate;
    }

    public void setUserLastVisitedDate(String userLastVisitedDate) {
        this.userLastVisitedDate = userLastVisitedDate;
    }

    public String getTajukPenuhUpperCase() {
        return tajukPenuhUpperCase;
    }

    public void setTajukPenuhUpperCase(String tajukPenuhUpperCase) {
        this.tajukPenuhUpperCase = tajukPenuhUpperCase;
    }

    public String getTajuk1UpperCase() {
        return tajuk1UpperCase;
    }

    public void setTajuk1UpperCase(String tajuk1UpperCase) {
        this.tajuk1UpperCase = tajuk1UpperCase;
    }

    public String getTajuk2UpperCase() {
        return tajuk2UpperCase;
    }

    public void setTajuk2UpperCase(String tajuk2UpperCase) {
        this.tajuk2UpperCase = tajuk2UpperCase;
    }

    public String getTajuk3UpperCase() {
        return tajuk3UpperCase;
    }

    public void setTajuk3UpperCase(String tajuk3UpperCase) {
        this.tajuk3UpperCase = tajuk3UpperCase;
    }

    public String getTajuk4UpperCase() {
        return tajuk4UpperCase;
    }

    public void setTajuk4UpperCase(String tajuk4UpperCase) {
        this.tajuk4UpperCase = tajuk4UpperCase;
    }

    public String getTajuk5UpperCase() {
        return tajuk5UpperCase;
    }

    public void setTajuk5UpperCase(String tajuk5UpperCase) {
        this.tajuk5UpperCase = tajuk5UpperCase;
    }

    public String getTajuk6UpperCase() {
        return tajuk6UpperCase;
    }

    public void setTajuk6UpperCase(String tajuk6UpperCase) {
        this.tajuk6UpperCase = tajuk6UpperCase;
    }

    public String getTajuk7UpperCase() {
        return tajuk7UpperCase;
    }

    public void setTajuk7UpperCase(String tajuk7UpperCase) {
        this.tajuk7UpperCase = tajuk7UpperCase;
    }

    public String getTajuk8UpperCase() {
        return tajuk8UpperCase;
    }

    public void setTajuk8UpperCase(String tajuk8UpperCase) {
        this.tajuk8UpperCase = tajuk8UpperCase;
    }

    public String getTajuk9UpperCase() {
        return tajuk9UpperCase;
    }

    public void setTajuk9UpperCase(String tajuk9UpperCase) {
        this.tajuk9UpperCase = tajuk9UpperCase;
    }

    public String getDeskripsiPenuhUpperCase() {
        return deskripsiPenuhUpperCase;
    }

    public void setDeskripsiPenuhUpperCase(String deskripsiPenuhUpperCase) {
        this.deskripsiPenuhUpperCase = deskripsiPenuhUpperCase;
    }

    public String getDeskripsi1UpperCase() {
        return deskripsi1UpperCase;
    }

    public void setDeskripsi1UpperCase(String deskripsi1UpperCase) {
        this.deskripsi1UpperCase = deskripsi1UpperCase;
    }

    public String getDeskripsi2UpperCase() {
        return deskripsi2UpperCase;
    }

    public void setDeskripsi2UpperCase(String deskripsi2UpperCase) {
        this.deskripsi2UpperCase = deskripsi2UpperCase;
    }

    public String getDeskripsi3UpperCase() {
        return deskripsi3UpperCase;
    }

    public void setDeskripsi3UpperCase(String deskripsi3UpperCase) {
        this.deskripsi3UpperCase = deskripsi3UpperCase;
    }

    public String getDeskripsi4UpperCase() {
        return deskripsi4UpperCase;
    }

    public void setDeskripsi4UpperCase(String deskripsi4UpperCase) {
        this.deskripsi4UpperCase = deskripsi4UpperCase;
    }

    public String getDeskripsi5UpperCase() {
        return deskripsi5UpperCase;
    }

    public void setDeskripsi5UpperCase(String deskripsi5UpperCase) {
        this.deskripsi5UpperCase = deskripsi5UpperCase;
    }

    public String getDeskripsi6UpperCase() {
        return deskripsi6UpperCase;
    }

    public void setDeskripsi6UpperCase(String deskripsi6UpperCase) {
        this.deskripsi6UpperCase = deskripsi6UpperCase;
    }

    public String getDeskripsi7UpperCase() {
        return deskripsi7UpperCase;
    }

    public void setDeskripsi7UpperCase(String deskripsi7UpperCase) {
        this.deskripsi7UpperCase = deskripsi7UpperCase;
    }

    public String getDeskripsi8UpperCase() {
        return deskripsi8UpperCase;
    }

    public void setDeskripsi8UpperCase(String deskripsi8UpperCase) {
        this.deskripsi8UpperCase = deskripsi8UpperCase;
    }

    public String getDeskripsi9UpperCase() {
        return deskripsi9UpperCase;
    }

    public void setDeskripsi9UpperCase(String deskripsi9UpperCase) {
        this.deskripsi9UpperCase = deskripsi9UpperCase;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(tajukPenuh);
        dest.writeString(tajuk1);
        dest.writeString(tajuk2);
        dest.writeString(tajuk3);
        dest.writeString(tajuk4);
        dest.writeString(tajuk5);
        dest.writeString(tajuk6);
        dest.writeString(tajuk7);
        dest.writeString(tajuk8);
        dest.writeString(tajuk9);
        dest.writeString(deskripsiPenuh);
        dest.writeString(deskripsi1);
        dest.writeString(deskripsi2);
        dest.writeString(deskripsi3);
        dest.writeString(deskripsi4);
        dest.writeString(deskripsi5);
        dest.writeString(deskripsi6);
        dest.writeString(deskripsi7);
        dest.writeString(deskripsi8);
        dest.writeString(deskripsi9);
        dest.writeString(tarikh);
        dest.writeString(karangan);
        dest.writeInt(vote);
        dest.writeInt(mostVisited);
        dest.writeString(userLastVisitedDate);
        dest.writeString(tajukPenuhUpperCase);
        dest.writeString(tajuk1UpperCase);
        dest.writeString(tajuk2UpperCase);
        dest.writeString(tajuk3UpperCase);
        dest.writeString(tajuk4UpperCase);
        dest.writeString(tajuk5UpperCase);
        dest.writeString(tajuk6UpperCase);
        dest.writeString(tajuk7UpperCase);
        dest.writeString(tajuk8UpperCase);
        dest.writeString(tajuk9UpperCase);
        dest.writeString(deskripsiPenuhUpperCase);
        dest.writeString(deskripsi1UpperCase);
        dest.writeString(deskripsi2UpperCase);
        dest.writeString(deskripsi3UpperCase);
        dest.writeString(deskripsi4UpperCase);
        dest.writeString(deskripsi5UpperCase);
        dest.writeString(deskripsi6UpperCase);
        dest.writeString(deskripsi7UpperCase);
        dest.writeString(deskripsi8UpperCase);
        dest.writeString(deskripsi9UpperCase);
    }
}
