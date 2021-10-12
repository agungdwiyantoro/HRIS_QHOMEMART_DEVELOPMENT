package com.app.mobiledev.apphris.training.soal_training;

public class ModelLatihan {

    private String id_soal;
    private String soal;
    private String a;
    private String b;
    private String c;
    private String d;
    private String kunci;
    private String tanggal;
    private String aktif;


    public ModelLatihan() {
    }

    public ModelLatihan(String id_soal, String soal, String a, String b, String c, String d, String kunci, String tanggal, String aktif) {
        this.id_soal = id_soal;
        this.soal = soal;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.kunci = kunci;
        this.tanggal = tanggal;
        this.aktif = aktif;
    }

    public String getId_soal() {
        return id_soal;
    }

    public void setId_soal(String id_soal) {
        this.id_soal = id_soal;
    }

    public String getSoal() {
        return soal;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getKunci() {
        return kunci;
    }

    public void setKunci(String kunci) {
        this.kunci = kunci;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAktif() {
        return aktif;
    }

    public void setAktif(String aktif) {
        this.aktif = aktif;
    }
}
