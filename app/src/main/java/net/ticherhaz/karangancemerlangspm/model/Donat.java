package net.ticherhaz.karangancemerlangspm.model;

public class Donat {

    private String donatUid, purchaseName, purchaseToken, purchaseSignature, orderId, devPayload, json, date;

    public Donat() {
    }

    public Donat(String donatUid, String purchaseName, String purchaseToken, String purchaseSignature, String orderId, String devPayload, String json, String date) {
        this.donatUid = donatUid;
        this.purchaseName = purchaseName;
        this.purchaseToken = purchaseToken;
        this.purchaseSignature = purchaseSignature;
        this.orderId = orderId;
        this.devPayload = devPayload;
        this.json = json;
        this.date = date;
    }

    public String getDonatUid() {
        return donatUid;
    }

    public void setDonatUid(String donatUid) {
        this.donatUid = donatUid;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public String getPurchaseSignature() {
        return purchaseSignature;
    }

    public void setPurchaseSignature(String purchaseSignature) {
        this.purchaseSignature = purchaseSignature;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDevPayload() {
        return devPayload;
    }

    public void setDevPayload(String devPayload) {
        this.devPayload = devPayload;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
