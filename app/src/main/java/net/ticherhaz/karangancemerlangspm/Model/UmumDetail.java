package net.ticherhaz.karangancemerlangspm.Model;

public class UmumDetail {

    private String umumDetailUid;
    private String masaDibalasOleh;
    private String profileUrl;
    private String username;
    private String userTitle;
    private String sekolah;
    private String onAccountCreatedDate;
    private String postCreatedDate;
    private String gender;
    private long pos;
    private long reputation;
    private String deskripsi;

    public UmumDetail() {
    }

    public UmumDetail(String umumDetailUid, String masaDibalasOleh, String profileUrl, String username, String userTitle, String sekolah, String onAccountCreatedDate, String postCreatedDate, String gender, long pos, long reputation, String deskripsi) {
        this.umumDetailUid = umumDetailUid;
        this.masaDibalasOleh = masaDibalasOleh;
        this.profileUrl = profileUrl;
        this.username = username;
        this.userTitle = userTitle;
        this.sekolah = sekolah;
        this.onAccountCreatedDate = onAccountCreatedDate;
        this.postCreatedDate = postCreatedDate;
        this.gender = gender;
        this.pos = pos;
        this.reputation = reputation;
        this.deskripsi = deskripsi;
    }

    public String getUmumDetailUid() {
        return umumDetailUid;
    }

    public void setUmumDetailUid(String umumDetailUid) {
        this.umumDetailUid = umumDetailUid;
    }

    public String getMasaDibalasOleh() {
        return masaDibalasOleh;
    }

    public void setMasaDibalasOleh(String masaDibalasOleh) {
        this.masaDibalasOleh = masaDibalasOleh;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getSekolah() {
        return sekolah;
    }

    public void setSekolah(String sekolah) {
        this.sekolah = sekolah;
    }

    public String getOnAccountCreatedDate() {
        return onAccountCreatedDate;
    }

    public void setOnAccountCreatedDate(String onAccountCreatedDate) {
        this.onAccountCreatedDate = onAccountCreatedDate;
    }

    public String getPostCreatedDate() {
        return postCreatedDate;
    }

    public void setPostCreatedDate(String postCreatedDate) {
        this.postCreatedDate = postCreatedDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
