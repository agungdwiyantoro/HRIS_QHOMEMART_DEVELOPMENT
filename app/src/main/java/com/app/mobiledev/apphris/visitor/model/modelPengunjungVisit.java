package com.app.mobiledev.apphris.visitor.model;

public class modelPengunjungVisit {
    private String id;
    private String hari;
    private String tanggal;
    private String jumlah;

    public modelPengunjungVisit(String id, String hari, String tanggal, String jumlah) {
        this.id = id;
        this.hari = hari;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
    }

    public modelPengunjungVisit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
