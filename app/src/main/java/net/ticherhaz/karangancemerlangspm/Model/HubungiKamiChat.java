package net.ticherhaz.karangancemerlangspm.Model;

public class HubungiKamiChat {

    private String chatUid, senderUid, title, lastUpdated;

    public HubungiKamiChat() {
    }

    public HubungiKamiChat(String chatUid, String senderUid, String title, String lastUpdated) {
        this.chatUid = chatUid;
        this.senderUid = senderUid;
        this.title = title;
        this.lastUpdated = lastUpdated;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getChatUid() {
        return chatUid;
    }

    public void setChatUid(String chatUid) {
        this.chatUid = chatUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
