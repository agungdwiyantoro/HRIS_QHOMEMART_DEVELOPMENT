package com.app.mobiledev.apphris.Model;

import org.json.JSONArray;

import java.util.ArrayList;

public class modelJenisTelat {
    private String id_terlambat, jenis, keterangan;

    public modelJenisTelat(String id_terlambat, String jenis, String keterangan) {
        this.id_terlambat = id_terlambat;
        this.jenis = jenis;
        this.keterangan = keterangan;
    }

    public String getId_terlambat() {
        return id_terlambat;
    }

    public void setId_terlambat(String id_terlambat) {
        this.id_terlambat = id_terlambat;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void getAll (){
      //  return new JSONArray;
    }
}
