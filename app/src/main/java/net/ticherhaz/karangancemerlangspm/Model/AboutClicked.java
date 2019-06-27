package net.ticherhaz.karangancemerlangspm.Model;

public class AboutClicked {

    private String userUid;
    private String uid;
    private String date;

    public AboutClicked(String userUid, String uid, String date) {
        this.userUid = userUid;
        this.uid = uid;
        this.date = date;
    }

    public AboutClicked() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
