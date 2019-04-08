package net.ticherhaz.karangancemerlangspm.Model;

public class ActivityStatusChanges {

    private String mode;
    private String date;

    public ActivityStatusChanges() {
    }

    public ActivityStatusChanges(String mode, String date) {
        this.mode = mode;
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
