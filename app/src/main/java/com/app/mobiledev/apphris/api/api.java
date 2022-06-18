package com.app.mobiledev.apphris.api;

public class api {
    //login
    static set_ip ip = new set_ip();
    public static final String key = "suksesmandiri96";
    public static final String APPS = "HRIS MOBILE";
    public static final String URL_foto = "http://hris.qhomedata.id/image_screnshoot/";
    //public static final String URL_foto = "http://qclean.co.id/image_screnshoot/";
    //public static final String URL_foto = "http://192.168.8.74/all/hris/image_screnshoot/";
    public static final String URL_foto_form_kunjungan = ip.getIp_image()+"image_mktProyek/";
    public static final String URL_foto_form_bonus_proyek =ip.getIp_image()+"image_bonus_proyek/";
    public static  final String URL_foto_izin=ip.getIp_image()+"foto_izin";
    public static final String URL_foto_profil = ip.getURL()+"upload/karyawan/";
    public static final String URL_login = ip.getIp() + "loginApps/";
    public static final String URL_absen = ip.getIp() + "uploadFoto3/"; // absensi_masuk, absensi_pulang, istirahat, lembur
    public static final String URL_generatePdf = ip.getIp() + "generatePdf/";
    public static final String URL_getInformasiAbsen = ip.getIp() + "getInformasiAbsen/";
    public static final String URL_cekSaldo = ip.getIp() + "cekSaldo/";
    public static final String URL_kasbonMingguan = ip.getIp() + "kasbonMingguan/";
    public static final String URL_chekingAbsensi = ip.getIp() + "chekingAbsensi_test/";// cek sdah presensi masuk atau belum
    public static final String URL_getSaldoMingguan = ip.getIp() + "getSaldoMingguan/";
    public static final String URL_pinjaman = ip.getIp() + "pinjaman/";
    public static final String URL_totalPinjaman = ip.getIp() + "totalPinjaman/";
    public static final String URL_informasiKaryawan = ip.getIp() + "informasiKaryawan2/";
    public static final String URL_pengajuanPinjaman = ip.getIp() + "pengajuanPinjaman/";
    public static final String URL_pengajuanKasbon = ip.getIp() + "pengajuanKasbon/";
    public static final String URL_totalBayarPinjaman = ip.getIp() + "totalBayarPinjaman/";
    public static final String URL_getNominalUM = ip.getIp() + "getNominalUM/";
    public static final String URL_getHistoryCuti = ip.getIp() + "getHistoryCuti/";
    public static final String URL_getCuti = ip.getIp() + "getCuti/";
    public static final String URL_getPeriode = ip.getIp() + "getPeriode/";
    public static final String URL_insertCuti= ip.getIp() + "insertCuti/";
    public static final String URL_updatePassword= ip.getIp() + "updatePassword/";
    public static final String URL_getPassword= ip.getIp() + "getPassword/";
    public static final String URL_getLamaCuti= ip.getIp() + "getLamaCuti/";
    public static final String URL_getRiwayatAbsen= ip.getIp() + "getRiwayatAbsen3/";
    public static final String URL_get_url_version= ip.getIp() + "get_url_version/";
    public static final String URL_get_cekWaktu= ip.getIp() + "cekWaktu/";
    public static final String URL_IzinMt= ip.getIp() + "izinmt/";
    public static final String URL_uploadIzin= ip.getIp() + "uploadIzin/";
    public static final String URL_getIzin= ip.getIp() + "getIzin/";
    public static final String URL_getBonus= ip.getIp() + "getBonus/";
    public static final String URL_insertBonus= ip.getIp() + "insertBonus/";
    public static final String URL_resetMacAddress= ip.getIp() + "resetMacAddress/";
    public static final String URL_insert_foto_profil= ip.getIp() + "insert_foto_profil/";
    public static  final String URL_getfoto_profil=ip.getIp()+"getfoto_profil";
    public static  final String URL_reset_password=ip.getIp()+"reset_password";
    public static  final String URL_getsoal=ip.getIp()+"getsoal";
    public static  final String URL_getInfoTraining=ip.getIp()+"getInfoTraining";
    public static  final String URL_insertFormKunjungan=ip.getIp()+"insertFormKunjungan";
    public static  final String URL_getHistoriFormKunjungan=ip.getIp()+"getHistoriFormKunjungan";
    public static  final String URL_getAksesMobile=ip.getIp()+"getAksesMobile";
    public static  final String URL_deleteFormKunjungan=ip.getIp()+"deleteFormKunjungan";
    public static  final String URL_getDateTime=ip.getIp()+"getWaktuFormKunjungan";
    public static  final String URL_setJawabTemp=ip.getIp()+"setJawabTemp";
    public static  final String URL_getJawabTemp=ip.getIp()+"getJawabTemp";
    public static  final String URL_simpanJwbTempo=ip.getIp()+"simpanJwbTempo";
    public static  final String URL_getinfosoal=ip.getIp()+"getinfosoal";
    public static  final String URL_cekSoal=ip.getIp()+"cekSoal";
    public static  final String URL_jenis_proyek=ip.getIp()+"jenis_proyek";
    public static  final String URL_jenis_cust=ip.getIp()+"jenis_cust";
    public static  final String URL_getmskaryawan=ip.getIp()+"getmskaryawan";
    public static  final String URL_insertTrkunjungan=ip.getIp()+"insertTrkunjungan";
    public static  final String URL_nourut_trkunjungan=ip.getIp()+"nourut_trkunjungan";
    public static  final String URL_getTrkunjungan=ip.getIp()+"getTrkunjungan";
    public static  final String URL_delete_trkunjungan=ip.getIp()+"delete_trkunjungan";
    public static  final String URL_getDetailTrkunjungan=ip.getIp()+"getDetailTrkunjungan";
    public static  final String URL_getDetailTeam=ip.getIp()+"getDetailTeam";
    public static  final String URL_updateTrkunjungan=ip.getIp()+"updateTrkunjungan";
    public static  final String URL_Link_lampiran_memo=ip.getLink_lampiran_memo();
    public static  final String URL_getMemo=ip.getIp()+"getMemo";
    public static  final String URL_detMemo=ip.getIp()+"detMemo";
    public static  final String URL_getlocation=ip.getIp()+"getLokasi";
    public static  final String URL_insertIzinDinas=ip.getIp()+"insertIzinDinas";
    public static  final String URL_getRiwayatDinas=ip.getIp()+"getRiwayatDinas";
    public static  final String URL_getRiewayatDetailDinas=ip.getIp()+"getRiewayatDetailDinas";
    public static  final String URL_getTgl_time=ip.getIp()+"getTgl_time";
    public static  final String URL_uploadDetailDinas=ip.getIp()+"uploadDetailDinas";
    public static  final String URL_deleteIzinDinas=ip.getIp()+"deleteIzinDinas";
    public static  final String URL_updateInsertTrDinas=ip.getIp()+"updateInsertTrDinas";
    public static  final String URL_getRiwayatIzin_mt=ip.getIp()+"getRiwayatIzin_mt";
    public static  final String URL_getRiwayatDinas_approve=ip.getIp()+"getRiwayatDinas_approve";
    public static  final String URL_approve_izinDinas=ip.getIp()+"approve_izinDinas";
    public static  final String URL_getRiwayatMt_approve=ip.getIp()+"getRiwayatMt_approve";
    public static  final String URL_deleteIzinMt=ip.getIp()+"deleteIzinMt";
    public static  final String URL_approve_izinMt=ip.getIp()+"approve_izinMt";
    public static  final String URL_getmsetprog=ip.getIp()+"getMsetProg";
    public static  final String URL_insertKK=ip.getIp()+"insert_KK";
    public static  final String URL_insertKK_new=ip.getIp()+"insert_KK_new";
    public static  final String URL_getHubungan_keluarga=ip.getIp()+"getHubungan_keluarga";
    public static  final String URL_insertHubunganKeluarga=ip.getIp()+"insertHubunganKeluarga";
    public static  final String URL_getStatus_keluarga=ip.getIp()+"getStatus_keluarga";
    public static  final String URL_cekDataKK=ip.getIp()+"cekDataKK";
    public static  final String URL_deleteHubungan_keluarga=ip.getIp()+"deleteHubungan_keluarga";
    public static  final String URL_up_ktps=ip.getIp()+"up_ktps";
    public static  final String URL_up_get_data_diri_temp=ip.getIp()+"get_data_diri_temp";
    public static  final String URL_getRiwayatSlipGaji=ip.getIp()+"getRiwayatSlipGaji";
    public static  final String URL_getSlipGaji=ip.getIp()+"getSlipGaji";
    public static  final String URL_saveTokenFcm=ip.getIp()+"saveTokenFcm";
    public static  final String URL_updateTokenFCM=ip.getIp()+"updateTokenFCM";
    public static  final String URL_getListKontrak=ip.getIp()+"kontrak_user";

    //data ip aws
    public  static  final String URL_uploadIzinSakit=ip.getIp()+"uploadimage";
    public  static  final String URL_IzinSakit=ip.getIp()+"izinsakit";
    public  static  final String URL_IzinCuti=ip.getIp()+"izincuti";
    public  static  final String URL_IzinDinasMT=ip.getIp()+"izinmt";
    public  static  final String URL_Akses=ip.getIp()+"akses";
    //public  static  final String URL_IzinSakit_approve_head=ip.getIp()+"approvehead";
    public  static  final String URL_IzinSakit_approve=ip.getIp()+"approvesakit";
    public  static  final String URL_IzinCuti_approve=ip.getIp()+"approvecuti";
    public  static  final String URL_IzinDinasMT_approve=ip.getIp()+"approvemt";
    //public  static  final String URL_IzinSakit_approve_hrd=ip.getIp()+"approvehrd";
    public  static  final String URL_foto_izinLampiran=ip.getURL()+"upload/karyawan/";
    public  static  final String URL_pdf_kontrak=ip.getURL()+"upload/karyawan/";
    public  static  final String URL_IzinCuti_jenis=ip.getIp()+"jenis_cuti";
    public  static  final String URL_IzinCuti_delegasi=ip.getIp()+"list_delegasi";
    public  static  final String URL_IzinCuti_kuotaTahunanPeriode=ip.getIp()+"kuota_cuti?periode=";
    public  static  final String URL_IzinCuti_hakCuti=ip.getIp()+"kuota_cuti";

    //Server api.qhome.id
    public static final String URL_API_QHOME_ID="http://api.qhome.id/hris/UnderMaintanceMobile";

    //Server hris ci3 token
    public static final String URL_API_TOKEN_HRIS=ip.getIp()+"auth";




    //Get Token Alamat
    public static final String URL_getTokenAlamat="http://192.168.8.74/svn_all/hris/api_coba/Auth/login";
    public static final String URL_getProvinsi = "http://192.168.8.74/svn_all/hris/wilayah/provinsi";
    public static final String URL_getKabKot = "http://192.168.8.74/svn_all/hris/wilayah/kabupaten?kode=";
    public static final String URL_getKecamatan = "http://192.168.8.74/svn_all/hris/wilayah/kecamatan?kode=";
    public static final String URL_getKelurahan = "http://192.168.8.74/svn_all/hris/wilayah/kelurahan?kode=";

public static  String get_url_foto_profil(String kyano,String url_foto){
    String url=api.URL_foto_profil+kyano+"/foto_profil/"+url_foto;
    return  url;
}











}