package com.app.mobiledev.apphris.riwayat_absen;

public class modelRiwayatAbsen {

    private String kyano;
    private String kynm;
    private String kydivisi;
    private String FINGER;
    private String masuk;
    private String pulang;
    private String mulai_istirahat;
    private String selesai_istirahat;
    private String masuk_lembur;
    private String keluar_lembur;
    private String tgl="";

    public String getTgl() {
        return tgl;
    }
    public void setTgl(String tgl) {
        this.tgl = tgl;
    }
    public String getKynm() {
        return kynm;
    }
    public void setKynm(String kynm) {
        this.kynm = kynm;
    }
    public String getKyano() {
        return kyano;
    }
    public void setKyano(String kyano) {
        this.kyano = kyano;
    }
    public String getKydivisi() {
        return kydivisi;
    }
    public void setKydivisi(String kydivisi) {
        this.kydivisi = kydivisi;
    }
    public String getFINGER() {
        return FINGER;
    }
    public void setFINGER(String FINGER) {
        this.FINGER = FINGER;
    }
    public String getMasuk() {
        return masuk;
    }
    public void setMasuk(String masuk) {
        this.masuk = masuk;
    }
    public String getPulang() {
        return pulang;
    }
    public void setPulang(String pulang) {
        this.pulang = pulang;
    }
    public String getMulai_istirahat() {
        return mulai_istirahat;
    }
    public void setMulai_istirahat(String mulai_istirahat) {
        this.mulai_istirahat = mulai_istirahat;
    }
    public String getSelesai_istirahat() {
        return selesai_istirahat;
    }
    public void setKembali_istirahat(String selesai_istirahat) {
        this.selesai_istirahat = selesai_istirahat;
    }
    public String getMasuk_lembur() {
        return masuk_lembur;
    }
    public void setMasuk_lembur(String masuk_lembur) {
        this.masuk_lembur = masuk_lembur;
    }
    public String getKeluar_lembur() {
        return keluar_lembur;
    }
    public void setKeluar_lembur(String keluar_lembur) {
        this.keluar_lembur = keluar_lembur;
    }

}
