package net.ticherhaz.karangancemerlangspm.Model;

public class OnlineStatus {

    private String username;
    private String sekolah;
    private long reputation;

    public OnlineStatus() {
    }

    public OnlineStatus(String username, String sekolah, long reputation) {
        this.username = username;
        this.sekolah = sekolah;
        this.reputation = reputation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSekolah() {
        return sekolah;
    }

    public void setSekolah(String sekolah) {
        this.sekolah = sekolah;
    }

    public long getReputation() {
        return reputation;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }
}
