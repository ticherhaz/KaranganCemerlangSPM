package net.ticherhaz.karangancemerlangspm.Model;

public class Umum {

    private String umumUid;
    private String tajuk;
    private String deskripsi;
    private long viewed;
    private long jumlahBalas;
    private long kedudukan;
    private String dimulaiOleh;
    private long masaDimulaiOleh;
    private String dibalasOleh;
    private long masaDibalasOleh;
    private String onCreatedDate;
    private String activityUmumLogUid;
    private String activityKedudukanLogUid;
    private String type;
    private String lastVisitedUser;

    public Umum(String umumUid, String tajuk, String deskripsi, long viewed, long jumlahBalas, long kedudukan, String dimulaiOleh, long masaDimulaiOleh, String dibalasOleh, long masaDibalasOleh, String onCreatedDate, String activityUmumLogUid, String activityKedudukanLogUid, String type, String lastVisitedUser) {
        this.umumUid = umumUid;
        this.tajuk = tajuk;
        this.deskripsi = deskripsi;
        this.viewed = viewed;
        this.jumlahBalas = jumlahBalas;
        this.kedudukan = kedudukan;
        this.dimulaiOleh = dimulaiOleh;
        this.masaDimulaiOleh = masaDimulaiOleh;
        this.dibalasOleh = dibalasOleh;
        this.masaDibalasOleh = masaDibalasOleh;
        this.onCreatedDate = onCreatedDate;
        this.activityUmumLogUid = activityUmumLogUid;
        this.activityKedudukanLogUid = activityKedudukanLogUid;
        this.type = type;
        this.lastVisitedUser = lastVisitedUser;
    }

    public Umum() {
    }

    public String getUmumUid() {
        return umumUid;
    }

    public void setUmumUid(String umumUid) {
        this.umumUid = umumUid;
    }

    public String getTajuk() {
        return tajuk;
    }

    public void setTajuk(String tajuk) {
        this.tajuk = tajuk;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public long getViewed() {
        return viewed;
    }

    public void setViewed(long viewed) {
        this.viewed = viewed;
    }

    public long getJumlahBalas() {
        return jumlahBalas;
    }

    public void setJumlahBalas(long jumlahBalas) {
        this.jumlahBalas = jumlahBalas;
    }

    public long getKedudukan() {
        return kedudukan;
    }

    public void setKedudukan(long kedudukan) {
        this.kedudukan = kedudukan;
    }

    public String getDimulaiOleh() {
        return dimulaiOleh;
    }

    public void setDimulaiOleh(String dimulaiOleh) {
        this.dimulaiOleh = dimulaiOleh;
    }

    public long getMasaDimulaiOleh() {
        return masaDimulaiOleh;
    }

    public void setMasaDimulaiOleh(long masaDimulaiOleh) {
        this.masaDimulaiOleh = masaDimulaiOleh;
    }

    public String getDibalasOleh() {
        return dibalasOleh;
    }

    public void setDibalasOleh(String dibalasOleh) {
        this.dibalasOleh = dibalasOleh;
    }

    public long getMasaDibalasOleh() {
        return masaDibalasOleh;
    }

    public void setMasaDibalasOleh(long masaDibalasOleh) {
        this.masaDibalasOleh = masaDibalasOleh;
    }

    public String getOnCreatedDate() {
        return onCreatedDate;
    }

    public void setOnCreatedDate(String onCreatedDate) {
        this.onCreatedDate = onCreatedDate;
    }

    public String getActivityUmumLogUid() {
        return activityUmumLogUid;
    }

    public void setActivityUmumLogUid(String activityUmumLogUid) {
        this.activityUmumLogUid = activityUmumLogUid;
    }

    public String getActivityKedudukanLogUid() {
        return activityKedudukanLogUid;
    }

    public void setActivityKedudukanLogUid(String activityKedudukanLogUid) {
        this.activityKedudukanLogUid = activityKedudukanLogUid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastVisitedUser() {
        return lastVisitedUser;
    }

    public void setLastVisitedUser(String lastVisitedUser) {
        this.lastVisitedUser = lastVisitedUser;
    }
}
