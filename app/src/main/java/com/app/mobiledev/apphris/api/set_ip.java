package com.app.mobiledev.apphris.api;

public class set_ip {
    //private String ip_utama="http://192.168.50.22/svn_all/hris/";
    //private String ip_utama="http://192.168.8.74/svn_all/hris/";
    private String ip_utama="http://hris.qhomedata.id/";
    //private String ip_utama="http://qclean.co.id/";
    //private String ip_server="http://192.168.50.22/svn_all/hris/api/";
   // private String ip_server="http://hris.qhomedata.id/api/";

   //link api
   private String ip_server=ip_utama+"api/";

   //lampiran memo pdf
   private String link_lampiran_memo=ip_utama+"upload/";

    // NODE JS NOTIF
    public String ip_notif="http://qhomemart.com:3000";
    //private String ip_server="http://10.11.1.174/all/hris/api/";
    //URL VIDEO MEMO
    private String ip_memo_video=ip_utama+"video/";

    //URL FOTO IZIN
    private String ip_foto_izin=ip_utama+"foto_izin/";

    public String getIp() {
        return ip_server;
    }
    public String getLink_lampiran_memo(){
        return link_lampiran_memo;
    }
    public String getIp_foto_profil() {
        return ip_utama;
    }
    public String getIp_notif() {
        return ip_notif;
    }
    public String getIp_image() {
        return ip_utama;
    }
    public String getIp_memo_video(){
        return  ip_memo_video;
    }
    public String getIpFoto_izin(){
        return ip_foto_izin;
    }


}