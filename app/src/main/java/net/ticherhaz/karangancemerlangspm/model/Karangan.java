package net.ticherhaz.karangancemerlangspm.model;

public class Karangan {

    //Main Variables
    //So, we have to divide the title at very specific because
    //we want to search them.
    //Example: Membaca Amalan Mulia
    //So it will be like this, tajuk1 = "Membaca", tajuk2 = "Amalan", tajuk3 = "Mulia"
    //And the rest will become empty
    private String uid;
    private String tajukPenuh;
    private String deskripsiPenuh;
    private String tarikh;
    private String karangan;
    private int vote;    //When user press thumb up button
    private int mostVisited;    //Every time user click the title
    private String userLastVisitedDate; //When user press title, the record of last visited updated
    //UpperCase Variables
    private String tajukPenuhUpperCase;
    private String karanganTag;
    private String karanganJenis;

    public Karangan() {
        //Don't delete constructor
    }

    public Karangan(String uid, String tajukPenuh, String deskripsiPenuh, String tarikh, String karangan, int vote, int mostVisited, String userLastVisitedDate, String tajukPenuhUpperCase, String karanganTag, String karanganJenis) {
        this.uid = uid;
        this.tajukPenuh = tajukPenuh;
        this.deskripsiPenuh = deskripsiPenuh;
        this.tarikh = tarikh;
        this.karangan = karangan;
        this.vote = vote;
        this.mostVisited = mostVisited;
        this.userLastVisitedDate = userLastVisitedDate;
        this.tajukPenuhUpperCase = tajukPenuhUpperCase;
        this.karanganTag = karanganTag;
        this.karanganJenis = karanganJenis;
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

    public String getDeskripsiPenuh() {
        return deskripsiPenuh;
    }

    public void setDeskripsiPenuh(String deskripsiPenuh) {
        this.deskripsiPenuh = deskripsiPenuh;
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

    public String getKaranganTag() {
        return karanganTag;
    }

    public void setKaranganTag(String karanganTag) {
        this.karanganTag = karanganTag;
    }

    public String getKaranganJenis() {
        return karanganJenis;
    }

    public void setKaranganJenis(String karanganJenis) {
        this.karanganJenis = karanganJenis;
    }
}
