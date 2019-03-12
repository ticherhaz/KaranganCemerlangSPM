package net.ticherhaz.karangancemerlangspm.Model;

public class System {

    private boolean mod;
    private int versi;

    public System() {
        //Don't delete constructor
    }

    public System(boolean mod, int versi) {
        this.mod = mod;
        this.versi = versi;
    }

    public boolean isMod() {
        return mod;
    }

    public void setMod(boolean mod) {
        this.mod = mod;
    }

    public int getVersi() {
        return versi;
    }

    public void setVersi(int versi) {
        this.versi = versi;
    }
}
