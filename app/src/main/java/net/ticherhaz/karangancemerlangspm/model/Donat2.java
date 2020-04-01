package net.ticherhaz.karangancemerlangspm.model;

public class Donat2 {

    private String userPurchasedCoinsMoreUid, userUid, orderUid, skuName, signature, originalJson, developerPayload, onPurchasedDateUTC;
    private int purchaseState;
    private long onPurchasedDate;

    public Donat2() {
    }

    public Donat2(String userPurchasedCoinsMoreUid, String userUid, String orderUid, String skuName, String signature, String originalJson, String developerPayload, String onPurchasedDateUTC, int purchaseState, long onPurchasedDate) {
        this.userPurchasedCoinsMoreUid = userPurchasedCoinsMoreUid;
        this.userUid = userUid;
        this.orderUid = orderUid;
        this.skuName = skuName;
        this.signature = signature;
        this.originalJson = originalJson;
        this.developerPayload = developerPayload;
        this.onPurchasedDateUTC = onPurchasedDateUTC;
        this.purchaseState = purchaseState;
        this.onPurchasedDate = onPurchasedDate;
    }

    public String getOrderUid() {
        return orderUid;
    }

    public void setOrderUid(String orderUid) {
        this.orderUid = orderUid;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOriginalJson() {
        return originalJson;
    }

    public void setOriginalJson(String originalJson) {
        this.originalJson = originalJson;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }

    public int getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(int purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getUserPurchasedCoinsMoreUid() {
        return userPurchasedCoinsMoreUid;
    }

    public void setUserPurchasedCoinsMoreUid(String userPurchasedCoinsMoreUid) {
        this.userPurchasedCoinsMoreUid = userPurchasedCoinsMoreUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getOnPurchasedDateUTC() {
        return onPurchasedDateUTC;
    }

    public void setOnPurchasedDateUTC(String onPurchasedDateUTC) {
        this.onPurchasedDateUTC = onPurchasedDateUTC;
    }

    public long getOnPurchasedDate() {
        return onPurchasedDate;
    }

    public void setOnPurchasedDate(long onPurchasedDate) {
        this.onPurchasedDate = onPurchasedDate;
    }
}
