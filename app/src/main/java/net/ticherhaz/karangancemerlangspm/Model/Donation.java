package net.ticherhaz.karangancemerlangspm.Model;

public class Donation {

    private String userUid;
    private String productUid;
    private String date;
    private String amount;

    public Donation(String userUid, String productUid, String date, String amount) {
        this.userUid = userUid;
        this.productUid = productUid;
        this.date = date;
        this.amount = amount;
    }

    public Donation() {
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getProductUid() {
        return productUid;
    }

    public void setProductUid(String productUid) {
        this.productUid = productUid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
