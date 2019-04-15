package net.ticherhaz.karangancemerlangspm.Model;

public class UserAlpha {

    private String userUid;
    private String phoneBrand;
    private String phoneModel;
    private String ipAddress;
    private String onCreatedDate;
    private String onLoginDate;
    private String onIpAddress;
    private String dayMonthYearCreated;

    public UserAlpha() {
    }

    public UserAlpha(String userUid, String phoneBrand, String phoneModel, String ipAddress, String onCreatedDate, String dayMonthYearCreated) {
        this.userUid = userUid;
        this.phoneBrand = phoneBrand;
        this.phoneModel = phoneModel;
        this.ipAddress = ipAddress;
        this.onCreatedDate = onCreatedDate;
        this.dayMonthYearCreated = dayMonthYearCreated;
    }

    public UserAlpha(String onLoginDate, String onIpAddress) {
        this.onLoginDate = onLoginDate;
        this.onIpAddress = onIpAddress;
    }

    public String getDayMonthYearCreated() {
        return dayMonthYearCreated;
    }

    public void setDayMonthYearCreated(String dayMonthYearCreated) {
        this.dayMonthYearCreated = dayMonthYearCreated;
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
