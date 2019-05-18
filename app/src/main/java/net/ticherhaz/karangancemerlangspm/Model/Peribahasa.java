package net.ticherhaz.karangancemerlangspm.Model;

public class Peribahasa {

    private String uid;
    private String title;
    private String description;
    private String date;
    private String dateUTC;

    public Peribahasa() {
    }

    public Peribahasa(String uid, String title, String description, String date, String dateUTC) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.date = date;
        this.dateUTC = dateUTC;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateUTC() {
        return dateUTC;
    }

    public void setDateUTC(String dateUTC) {
        this.dateUTC = dateUTC;
    }
}
