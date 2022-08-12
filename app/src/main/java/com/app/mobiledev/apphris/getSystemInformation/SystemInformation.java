package com.app.mobiledev.apphris.getSystemInformation;

import android.os.Build;

import com.app.mobiledev.apphris.R;

public class SystemInformation {

    private String serial,
    model,
    id,
    manufacturer,
    brand,
    type,
    user,
    base,
    incremental,
    sdk,
    board,
    host,
    fingerprint,
    versioncode;

    public SystemInformation(String serial, String model, String id, String manufacturer, String brand, String type, String user, String base, String incremental, String sdk, String board, String host, String fingerprint, String versioncode) {
        this.serial = serial;
        this.model = model;
        this.id = id;
        this.manufacturer = manufacturer;
        this.brand = brand;
        this.type = type;
        this.user = user;
        this.base = base;
        this.incremental = incremental;
        this.sdk = sdk;
        this.board = board;
        this.host = host;
        this.fingerprint = fingerprint;
        this.versioncode = versioncode;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getIncremental() {
        return incremental;
    }

    public void setIncremental(String incremental) {
        this.incremental = incremental;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    /*
    private String getHardwareAndSoftwareInfo() {

        return getString(R.string.serial) + " " + Build.SERIAL + "\n" +
                getString(R.string.model) + " " + Build.MODEL + "\n" +
                getString(R.string.id) + " " + Build.ID + "\n" +
                getString(R.string.manufacturer) + " " + Build.MANUFACTURER + "\n" +
                getString(R.string.brand) + " " + Build.BRAND + "\n" +
                getString(R.string.type) + " " + Build.TYPE + "\n" +
                getString(R.string.user) + " " + Build.USER + "\n" +
                getString(R.string.base) + " " + Build.VERSION_CODES.BASE + "\n" +
                getString(R.string.incremental) + " " + Build.VERSION.INCREMENTAL + "\n" +
                getString(R.string.sdk) + " " + Build.VERSION.SDK_INT + "\n" +
                getString(R.string.board) + " " + Build.BOARD + "\n" +
                getString(R.string.host) + " " + Build.HOST + "\n" +
                getString(R.string.fingerprint) + " " + Build.FINGERPRINT + "\n" +
                getString(R.string.versioncode) + " " + Build.VERSION.RELEASE;
    }
}

     */
}
