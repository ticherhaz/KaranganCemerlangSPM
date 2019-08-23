package net.ticherhaz.karangancemerlangspm.model;

public class UmumDetail {

    private String umumDetailUid;
    private String registeredUid;
    private String postCreatedDate;
    private String deskripsi;

    public UmumDetail() {
    }

    public UmumDetail(String umumDetailUid, String registeredUid, String postCreatedDate, String deskripsi) {
        this.umumDetailUid = umumDetailUid;
        this.registeredUid = registeredUid;
        this.postCreatedDate = postCreatedDate;
        this.deskripsi = deskripsi;
    }

    public String getUmumDetailUid() {
        return umumDetailUid;
    }

    public void setUmumDetailUid(String umumDetailUid) {
        this.umumDetailUid = umumDetailUid;
    }

    public String getRegisteredUid() {
        return registeredUid;
    }

    public void setRegisteredUid(String registeredUid) {
        this.registeredUid = registeredUid;
    }

    public String getPostCreatedDate() {
        return postCreatedDate;
    }

    public void setPostCreatedDate(String postCreatedDate) {
        this.postCreatedDate = postCreatedDate;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
