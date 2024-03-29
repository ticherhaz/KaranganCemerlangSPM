package net.ticherhaz.karangancemerlangspm.model;

public class UserFirst {

    private String userUid;
    private String phoneBrand;
    private String phoneModel;
    private String ipAddress;
    private String onCreatedDate;
    private String onLoginDate;
    private String onIpAddress;

    public UserFirst() {
    }

    public UserFirst(String userUid, String phoneBrand, String phoneModel, String ipAddress, String onCreatedDate) {
        this.userUid = userUid;
        this.phoneBrand = phoneBrand;
        this.phoneModel = phoneModel;
        this.ipAddress = ipAddress;
        this.onCreatedDate = onCreatedDate;
    }

    public UserFirst(String onLoginDate, String onIpAddress) {
        this.onLoginDate = onLoginDate;
        this.onIpAddress = onIpAddress;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
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

    public String getOnCreatedDate() {
        return onCreatedDate;
    }

    public void setOnCreatedDate(String onCreatedDate) {
        this.onCreatedDate = onCreatedDate;
    }

    public String getOnLoginDate() {
        return onLoginDate;
    }

    public void setOnLoginDate(String onLoginDate) {
        this.onLoginDate = onLoginDate;
    }

    public String getOnIpAddress() {
        return onIpAddress;
    }

    public void setOnIpAddress(String onIpAddress) {
        this.onIpAddress = onIpAddress;
    }
}
