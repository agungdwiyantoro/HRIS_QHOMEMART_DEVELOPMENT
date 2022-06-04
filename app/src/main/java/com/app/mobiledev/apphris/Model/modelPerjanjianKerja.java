package com.app.mobiledev.apphris.Model;

public class modelPerjanjianKerja {
    private String mkstatuskerja;
    private String file;
    private String mkano;
    private String mktglDari;
    private String judulKontrak;
    private String mktglSampai;

    public void setMkstatuskerja(String mkstatuskerja){
        this.mkstatuskerja = mkstatuskerja;
    }

    public String getMkstatuskerja(){
        return mkstatuskerja;
    }

    public void setFile(String  file){
        this.file = file;
    }

    public String  getFile(){
        return file;
    }

    public void setMkano(String mkano){
        this.mkano = mkano;
    }

    public String getMkano(){
        return mkano;
    }

    public void setMktglDari(String mktglDari){
        this.mktglDari = mktglDari;
    }

    public String getMktglDari(){
        return mktglDari;
    }

    public void setJudulKontrak(String  judulKontrak){
        this.judulKontrak = judulKontrak;
    }

    public String  getJudulKontrak(){
        return judulKontrak;
    }

    public void setMktglSampai(String mktglSampai){
        this.mktglSampai = mktglSampai;
    }

    public String getMktglSampai(){
        return mktglSampai;
    }
}
