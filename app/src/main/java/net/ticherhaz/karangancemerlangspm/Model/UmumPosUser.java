package net.ticherhaz.karangancemerlangspm.Model;

public class UmumPosUser {

    private String umumPosUid, umumUid, registeredUid;
    private long umumPos;

    public UmumPosUser() {
    }

    public UmumPosUser(String umumPosUid, String umumUid, String registeredUid, long umumPos) {
        this.umumPosUid = umumPosUid;
        this.umumUid = umumUid;
        this.registeredUid = registeredUid;
        this.umumPos = umumPos;
    }

    public String getUmumPosUid() {
        return umumPosUid;
    }

    public void setUmumPosUid(String umumPosUid) {
        this.umumPosUid = umumPosUid;
    }

    public String getUmumUid() {
        return umumUid;
    }

    public void setUmumUid(String umumUid) {
        this.umumUid = umumUid;
    }

    public String getRegisteredUid() {
        return registeredUid;
    }

    public void setRegisteredUid(String registeredUid) {
        this.registeredUid = registeredUid;
    }

    public long getUmumPos() {
        return umumPos;
    }

    public void setUmumPos(long umumPos) {
        this.umumPos = umumPos;
    }
}
