package net.ticherhaz.karangancemerlangspm.model;

public class HubungiKamiMessage {

    private String hkUid, chatUid, registeredUid, date, message, receiverUid;

    public HubungiKamiMessage() {
    }

    public HubungiKamiMessage(String hkUid, String chatUid, String registeredUid, String date, String message, String receiverUid) {
        this.hkUid = hkUid;
        this.chatUid = chatUid;
        this.registeredUid = registeredUid;
        this.date = date;
        this.message = message;
        this.receiverUid = receiverUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getChatUid() {
        return chatUid;
    }

    public void setChatUid(String chatUid) {
        this.chatUid = chatUid;
    }

    public String getHkUid() {
        return hkUid;
    }

    public void setHkUid(String hkUid) {
        this.hkUid = hkUid;
    }

    public String getRegisteredUid() {
        return registeredUid;
    }

    public void setRegisteredUid(String registeredUid) {
        this.registeredUid = registeredUid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
