package net.ticherhaz.karangancemerlangspm.model;

public class Jenis {

    private String jenisUid;
    private String title;
    private String description;

    public Jenis(String jenisUid, String title, String description) {
        this.jenisUid = jenisUid;
        this.title = title;
        this.description = description;
    }

    public Jenis() {
    }

    public String getJenisUid() {
        return jenisUid;
    }

    public void setJenisUid(String jenisUid) {
        this.jenisUid = jenisUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
