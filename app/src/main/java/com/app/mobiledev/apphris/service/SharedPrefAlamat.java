package com.app.mobiledev.apphris.service;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefAlamat {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "ALAMAT";

    public static final String PROVINSI="PROVINSI";
    public static final String KOTA="KOTA";
    public static final String KECAMATAN="KECAMATAN";
    public static final String KELURAHAN="KELURAHAN";
    public static final String ALAMAT_LENGKAP="ALAMAT_LENGKAP";

    public static String provinsi;
    public static String kota;
    public static String kecamatan;
    public static String kelurahan;
    public static String alamat_lengkap;

    public SharedPrefAlamat(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void putProvinsi(String provinsi){
        editor.putString(PROVINSI, provinsi);
        editor.apply();
    }

    public String getProvinsi(){
        provinsi = sharedPreferences.getString(PROVINSI,"");
        return provinsi;
    }

    public void putKota(String kota){
        editor.putString(KOTA, kota);
        editor.apply();
    }

    public String getKota(){
        kota = sharedPreferences.getString(KOTA,"");
        return kota;
    }

    public void putKecamatan(String kecamatan){
        editor.putString(KECAMATAN, kecamatan);
        editor.apply();
    }

    public String getKecamatan(){
        kecamatan = sharedPreferences.getString(KECAMATAN,"");
        return kecamatan;
    }

    public void putKelurahan(String kelurahan){
        editor.putString(KELURAHAN, kelurahan);
        editor.apply();
    }

    public String getKelurahan(){
        kelurahan = sharedPreferences.getString(KELURAHAN,"");
        return kelurahan;
    }

    public void putAlamatLengkap(String alamat_lengkap){
        editor.putString(ALAMAT_LENGKAP, alamat_lengkap);
        editor.apply();
    }

    public String getAlamat_lengkap(){
        alamat_lengkap = sharedPreferences.getString(ALAMAT_LENGKAP,"");
        return alamat_lengkap;
    }

}
