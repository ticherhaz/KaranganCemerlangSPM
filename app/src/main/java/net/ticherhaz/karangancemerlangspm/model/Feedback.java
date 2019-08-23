package net.ticherhaz.karangancemerlangspm.model;

public class Feedback {
    private String uid;
    private String userUid;
    private String phoneModel;
    private String problemType;
    private String email;
    private String description;
    private String date;

    public Feedback() {
        //Don't delete constructor
    }

    public Feedback(String uid, String userUid, String phoneModel, String problemType, String email, String description, String date) {
        this.uid = uid;
        this.userUid = userUid;
        this.phoneModel = phoneModel;
        this.problemType = problemType;
        this.email = email;
        this.description = description;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}
