package com.app.mobiledev.apphris;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.memo.AdapterMemo;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.cek_gps.GpsUtils;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.memo.ModelMemo;
import com.app.mobiledev.apphris.memo.listMemo.memo_list;
import com.app.mobiledev.apphris.profile.UpdateDataDiri;
import com.app.mobiledev.apphris.profile.profil;
import com.app.mobiledev.apphris.service_location.LocationUpdate;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

public class fragment_home extends Fragment {

    public fragment_home(){ }
    private boolean isGPS = false;
    private SessionManager sessionmanager;
    private String kyano,namauser,nama,hashtag;
    private TextView txtusername;
    private TextView txBantuan;
    private ImageView absensi_masuk,absensi_keluar;
    private LinearLayout mulai_istirahat,selesai_istirahat,mulai_lembur,selesai_lembur;
    private double lat=0;
    private double lon=0;
    private ProgressDialog mProgressDialog;
    private TextView abs_masuk,abs_keluar;
    private View rootView;
    private TextView txinformasi;
    private TextView txthastag;
    private LinearLayout lmenu;
    private CircleImageView ic_user_account;
    private TextView tx_lihat_selengkapnya, txtNanti, txtUpdate,info_update,txInfo,txtClose;
    private Boolean cek_memo=false;
    private Boolean cek_info_update=false;
    //Memo
    private List<ModelMemo> mlistMemo;
    private RecyclerView rvMemo;


    private boolean status_notif = true;

    Dialog dialogHubungan;
    Dialog dialoginfoUpdate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Cek Update Data Diri Notif
        //notifUpdateData();

        rootView= inflater.inflate(R.layout.activity_fragment_activity, container, false);
        sessionmanager=new SessionManager(getActivity());
        kyano=sessionmanager.getIdUser();
        hashtag=sessionmanager.getHashtag();
        abs_masuk=rootView.findViewById(R.id.abs_masuk);
        abs_keluar=rootView.findViewById(R.id.abs_pulang);
        txthastag=rootView.findViewById(R.id.txthastag);
        txBantuan=rootView.findViewById(R.id.btnBantuan);
        lmenu=rootView.findViewById(R.id.lmenu);
        txthastag.setText("#"+hashtag);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading ...");
        txinformasi=rootView.findViewById(R.id.txinformasi);
        namauser=sessionmanager.getUsername();
        nama=sessionmanager.getNamaLEngkap();
        ic_user_account=rootView.findViewById(R.id.ic_user_account);
        txtusername=rootView.findViewById(R.id.txtusername);
        txtusername.setText(nama);
        absensi_masuk=rootView.findViewById(R.id.absensiMasuk);
        absensi_keluar=rootView.findViewById(R.id.absensiKeluar);
        mulai_lembur=rootView.findViewById(R.id.mulaiLembur);
        selesai_lembur=rootView.findViewById(R.id.selesaiLembur);
        mulai_istirahat=rootView.findViewById(R.id.mulaiIstirahat);
        selesai_istirahat=rootView.findViewById(R.id.selesaiIstirahat);
        tx_lihat_selengkapnya= rootView.findViewById(R.id.tx_lihat_selengkapnya);
        helper.getLokasi(getActivity(),sessionmanager.getIdUser());
        tx_lihat_selengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cek_memo){
                    startActivity(new Intent(getActivity(), memo_list.class));
                }else{
                    helper.snackBar(lmenu,"memo belum tersedia......");
                }
            }
        });
        //cekLokasiFakeGPS();
        getlImageProfil(kyano);
        getMemo();
        getabsen(kyano);
        cek_info_update=sessionmanager.getUpdatePlayStore();
        if(cek_info_update==true){
            getMsetProg(getActivity(),"info_update_android");
        }



        //memo
        mlistMemo = new ArrayList<>();
        rvMemo = rootView.findViewById(R.id.rvMemo);
        //getMemo();

        AndroidNetworking.initialize(getActivity());
        String locationProviders = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

       ic_user_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), profil.class);
                startActivity(intent2);
                getActivity().finish();
            }
        });
        txBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nowa_admin=sessionmanager.getNo_hp_admin();
                openWhatsApp(nowa_admin,""+sessionmanager.getNik()+" "+sessionmanager.getNamaLEngkap()+":  ");

            }
        });


        absensi_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                cek_absen("absensi masuk",kyano);
                Log.d("CEK_TGL_ABSENSI_MASUK", "onClick: "+ sessionmanager.getTglPresensiMasuk());

               // ((absensi_masuk) getActivity()).setOnButtonListener(this);
            }
        });

        absensi_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                cek_absen("absensi pulang",kyano);

            }
        });

        mulai_istirahat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();

                cek_absen("mulai istirahat",kyano);


            }
        });
        selesai_istirahat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                cek_absen("selesai istirahat",kyano);



            }
        });

        mulai_lembur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                cek_absen("mulai lembur",kyano);

            }
        });

        selesai_lembur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                cek_absen("selesai lembur",kyano);

            }
        });

        new GpsUtils(getActivity()).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;

            }
        });
        helper.requestPermissionsGps(getActivity());
        notifUpdateData();

        return rootView;
    };

    public void cekLokasiFakeGPS(){
    try {
        LocationUpdate gt = new LocationUpdate(getActivity());
        Location l = gt.getLocation();
        if(l.isFromMockProvider()){
            SweetAlertDialog dialog=new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            dialog.setTitleText("Informasi");
            dialog.setContentText("terdeteksi aplikasi fake gps \n aplikasi akan berhenti");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    getActivity().finish();
                    getActivity().moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            dialog.show();


        }

    }catch (NullPointerException e){
        Log.d("NULL", "cekLokasi: "+e);
    }

}

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getabsen(final String kyano){
        AndroidNetworking.post(api.URL_getInformasiAbsen)
                .addBodyParameter( "mcaddress", helper.getDeviceIMEI(getActivity()))
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    if(data.getString("abin").equals("null")){
                                        abs_masuk.setText("00:00");
                                    }else{
                                        abs_masuk.setText(data.getString("abin"));
                                    }

                                    if(data.getString("about").equals("null")){
                                        abs_keluar.setText("00:00");
                                    }else{
                                        abs_keluar.setText(data.getString("about"));
                                    }


                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTI", "onError: "+anError);


                    }
                });

    }

    private  void cek_absen(final String status, final String kyano) {
        AndroidNetworking.post(api.URL_chekingAbsensi)
                .addBodyParameter("status", status)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Boolean success = response.getBoolean("status");
                            String data = response.getString("ket");
                            if (success) {
                                if(status.equals("absensi masuk")){
                                    startActivity(new Intent(getActivity(), absensi_masuk.class));

                                }
                                if(status.equals("absensi pulang")){
                                    startActivity(new Intent(getActivity(), absensi_keluar.class));


                                }
                                if(status.equals("mulai istirahat")){
                                    startActivity(new Intent(getActivity(), mulai_istirahat.class));

                                }
                                if(status.equals("selesai istirahat")){
                                    startActivity(new Intent(getActivity(), selesai_istirahat.class));

                                }
                                if(status.equals("mulai lembur")){
                                    startActivity(new Intent(getActivity(), mulai_lembur.class));


                                }
                                if(status.equals("selesai lembur")){
                                    startActivity(new Intent(getActivity(), selesai_lembur.class));
                                }

                            } else {
                                helper.showMsg(getActivity(), "Informasi", "" + data, helper.ERROR_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: " + e);
                            helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: " + anError);
                        mProgressDialog.dismiss();

                    }
                });

    }

    private void getInformasi(){
        AndroidNetworking.post(api.URL_getNominalUM)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String cekStaff1=data.getString("setano");
                                    if(cekStaff1.equals("informasi")){
                                    txinformasi.setText(""+data.getString("setchar"));
                                    }
                                }

                            }else{
                                helper.showMsg(getActivity(),"Informasi","Data tidak ditemukan",helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            mProgressDialog.dismiss();
                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });

        }

    private void openWhatsApp(String numero,String mensaje){
        try{
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
        }

    }

    private void getlImageProfil(final String kyano){
        AndroidNetworking.post(api.URL_getfoto_profil)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String data = response.getString("data");
                            if (success) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true);
                                Glide.with(getActivity()).load(api.get_url_foto_profil(kyano,data)).thumbnail(Glide.with(getActivity()).load(R.drawable.loading)).apply(requestOptions).into(ic_user_account);
                            } else {
                                Log.d("ERROR_foto_profil", "onResponse: ");
                            }

                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            mProgressDialog.dismiss();
                        }catch (NullPointerException e){
                            Log.d("NULLPOINTER", "onResponse: "+e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.DialogKoneksi(getActivity());
                        Log.d("EROOR_FOTO_PROFIL", "onError: "+anError);
                        mProgressDialog.dismiss();
                    }
                });
    }

    private void getMemo(){
        AndroidNetworking.post(api.URL_getMemo)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Boolean success = response.getBoolean("success");


                            if (success) {
                                mlistMemo.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    ModelMemo model = new ModelMemo();
                                    model.setMmANo(data.getString("MmANo"));
                                    model.setJns(data.getString("jns"));
                                    model.setMo(data.getString("no"));
                                    model.setTgl(data.getString("tgl"));
                                    model.setHal(data.getString("hal"));
                                    model.setKpd(data.getString("kepada"));
                                    model.setDari(data.getString("dari"));
                                    model.setIsi(data.getString("isi"));
                                    model.setPdf(data.getString("pdf"));
                                    model.setVideo(data.getString("video"));
                                    cek_memo=true;

                                    if(data.getString("jns").equals("Notulen Meeting")){
                                        model.setWkt(data.getString("wkt"));
                                        model.setTempat(data.getString("tempat"));
                                    }
                                    model.setMemo_baru(data.getString("memo_baru"));
                                    mlistMemo.add(model);
                                }
                                AdapterMemo mAdapter = new AdapterMemo(mlistMemo, getActivity());
                                mAdapter.notifyDataSetChanged();
                                rvMemo.setVisibility(View.VISIBLE);
                                rvMemo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
                                rvMemo.setItemAnimator(new DefaultItemAnimator());
                                rvMemo.setAdapter(mAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONEEVENTT", "onResponse: "+e);

                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EVENT", "onError: "+anError);

                    }
                });
    }




    @Override
    public void onResume() {
        super.onResume();
        getlImageProfil(kyano);
        getMemo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getabsen(kyano);
        }
    }

    private void notifUpdateData() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
                 AndroidNetworking.initialize(getActivity(), okHttpClient);
                 AndroidNetworking.post(api.URL_cekDataKK)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean status = response.getBoolean("status");
                            String keterangan = response.getString("ket");
                            if (status) {
                                notifDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_Exception", "onResponse: " + e);
                           // helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                            //progressDialog.dismiss();
                        } catch (NullPointerException e) {
                            Log.d("JSON_NullPointer", "onResponse: " + e);
                        } catch (NumberFormatException e) {
                            Log.d("JSONFormatException", "onResponse: " + e);
                        }

                        //progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        //helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("ANERROR_EXCEPTION", "onError: " + anError);
                        //progressDialog.dismiss();
                    }
                });
    }

    private void notifDialog() {
        dialogHubungan = new Dialog(getActivity());
        dialogHubungan.setContentView(R.layout.dialog_notify_update);
        //inflater = getLayoutInflater();

        //dialogViewHubungan = inflater.inflate(R.layout.form_tambah_keluarga, null);

        dialogHubungan.setCancelable(true);
        /*dialogHubungan.setC*/

        //dialogHubungan.setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
        dialogHubungan.setTitle("Update data diri");

        txtNanti = (TextView) dialogHubungan.findViewById(R.id.txtNanti);
        txtUpdate = (TextView) dialogHubungan.findViewById(R.id.txtUpdate);

        txtNanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHubungan.dismiss();
            }
        });

        txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateDataDiri.class));
            }
        });

        dialogHubungan.show();
    }


    private void notifDialogUpdate(String pesan_update) throws PackageManager.NameNotFoundException {
        dialoginfoUpdate = new Dialog(getActivity());
        dialoginfoUpdate.setContentView(R.layout.dialog_infoupdate);
        dialoginfoUpdate.setCancelable(true);
        dialoginfoUpdate.setCanceledOnTouchOutside(false);
        dialoginfoUpdate.setTitle("info update");
        info_update = (TextView) dialoginfoUpdate.findViewById(R.id.info_update);
        txInfo = (TextView) dialoginfoUpdate.findViewById(R.id.txInfo);
        txtClose = (TextView) dialoginfoUpdate.findViewById(R.id.txtClose);
        txInfo.setText(""+pesan_update);

        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        String version = pInfo.versionName;
        info_update.setText("Info Update "+version);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoginfoUpdate.dismiss();
                sessionmanager.createStatusUpdatePlayStore(false);
            }
        });



        dialoginfoUpdate.show();
    }



    public  void getMsetProg(final Context ctx, String setProg){
        AndroidNetworking.post(api.URL_getmsetprog)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SessionManager msession=new SessionManager(ctx);
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String setano=data.getString("setano");
                                    if(setano.equals(setProg)){
                                        Log.d("JSONSETPROG", "onResponse: "+data.getString("setchar"));
                                        notifDialogUpdate(""+data.getString("setchar"));
                                    }

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONSETPROG", "onResponse: "+e);

                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("JSONSETPROG", "onError: "+anError);



                    }
                });
    }
}
