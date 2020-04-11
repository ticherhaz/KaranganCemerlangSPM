package net.ticherhaz.karangancemerlangspm.model;

public class DownloadKarangan {

    private String downloadKaranganUid, karanganUid, orderUid, skuName, signature, originalJson, developerPayload, onPurchasedDateUTC;
    private int purchaseState;
    private long onPurchasedDate;

    public DownloadKarangan() {
    }

    public DownloadKarangan(String downloadKaranganUid, String karanganUid, String orderUid, String skuName, String signature, String originalJson, String developerPayload, String onPurchasedDateUTC, int purchaseState, long onPurchasedDate) {
        this.downloadKaranganUid = downloadKaranganUid;
        this.karanganUid = karanganUid;
        this.orderUid = orderUid;
        this.skuName = skuName;
        this.signature = signature;
        this.originalJson = originalJson;
        this.developerPayload = developerPayload;
        this.onPurchasedDateUTC = onPurchasedDateUTC;
        this.purchaseState = purchaseState;
        this.onPurchasedDate = onPurchasedDate;
    }

    public String getDownloadKaranganUid() {
        return downloadKaranganUid;
    }

    public void setDownloadKaranganUid(String downloadKaranganUid) {
        this.downloadKaranganUid = downloadKaranganUid;
    }

    public String getKaranganUid() {
        return karanganUid;
    }

    public void setKaranganUid(String karanganUid) {
        this.karanganUid = karanganUid;
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

    public String getOnPurchasedDateUTC() {
        return onPurchasedDateUTC;
    }

    public void setOnPurchasedDateUTC(String onPurchasedDateUTC) {
        this.onPurchasedDateUTC = onPurchasedDateUTC;
    }

    public int getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(int purchaseState) {
        this.purchaseState = purchaseState;
    }

    public long getOnPurchasedDate() {
        return onPurchasedDate;
    }

    public void setOnPurchasedDate(long onPurchasedDate) {
        this.onPurchasedDate = onPurchasedDate;
    }
}
