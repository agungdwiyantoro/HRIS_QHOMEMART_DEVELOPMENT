package com.app.mobiledev.apphris;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.IntentSender;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.api.set_ip;
import com.app.mobiledev.apphris.cek_gps.GpsUtils;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.profile.profil;
import com.app.mobiledev.apphris.service.services_notif;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class main_fragment extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener  {
    private FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;
    Fragment fragment = null;
    Toolbar toolbar_abs;
    private SessionManager sessionmanager;
    private boolean isGPS = false;
    private String nama,kyano,nik;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private Socket socket;
    private   int index=0;
    private String idtraining="",SERVICE_NOTIF="";
    static set_ip ip = new set_ip();
    private String TAG="UPDATE_PLAY_STORE";
    private LinearLayout lytoolbar;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE=100;
    private Dialog dialogResign;
    private TextView txJudul,txInfo,txClose;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);

        sessionmanager = new SessionManager(main_fragment.this);

        //call FCM configuration
        helper.ConfigFCM();

        //Untuk mendapatkan token
        String kyano = sessionmanager.getIdUser();
        String password = sessionmanager.getPass();
        Log.d("TAG_UP", "onCreate: "+kyano+password);
        getToken(kyano, password);

        mAppUpdateManager= AppUpdateManagerFactory.create(this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                //helper.snackBar(lytoolbar,"CEK_VERSION= A="+UpdateAvailability.UPDATE_AVAILABLE+" B="+result.updateAvailability());
                if(result.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE
                        &&result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,main_fragment.this,
                                RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        helper.snackBar(lytoolbar,"CEK_VERSION= "+e);
                    }
                }



            }
        });
        //mAppUpdateManager.registerListener(installStateUpdatedListener);
        lytoolbar=findViewById(R.id.lytoolbar);

        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getNamaLEngkap();
        nik=sessionmanager.getNik();
        idtraining=sessionmanager.getIdtraning();
        toolbar_abs = findViewById(R.id.toolbar_abs);
        setSupportActionBar(toolbar_abs);
        toolbar_abs.setTitle(""+nama);
        setSupportActionBar(toolbar_abs);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        cekGps();
        Intent background = new Intent(this, services_notif.class);
        stopService(background);
        startService(background);
        getInfoTraining(bottomNavigationView);

        try {
            socket = IO.socket(ip.ip_notif);
            socket.connect();
            socket.on("notif_training",notif);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.d("NODE_JS_ERROR", "call: "+e);
        }

        Intent intent= getIntent();
        Bundle b = intent.getExtras();
        try {
            if(b!=null) { SERVICE_NOTIF =(String) b.get("service_notif"); }
            else{ SERVICE_NOTIF="";}

        }catch (NullPointerException e){
            Log.d("NULL", "onCreate: "+e);
        }

        //loadFragment(new Fragment_home());
        if (savedInstanceState == null) {
            fragment = new fragment_home();
            callFragment(fragment);
        }
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;

            }
        });

        helper.requestPermissionsGps(main_fragment.this);
        requestReadPhoneStatePermission();
        getInformasiKaryawan(kyano);

    }

    /*private InstallStateUpdatedListener installStateUpdatedListener=new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState state) {
                if(state.installStatus()==InstallStatus.DOWNLOADED){
                    shoCompleteUpdate();
                }
        }
    };

    private void shoCompleteUpdate() {
        Snackbar snackbar =Snackbar.make(lytoolbar,"New App is ready..!!",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppUpdateManager.completeUpdate();

            }
        });
        snackbar.show();

    }*/

    public void getToken(String kyano, String pass) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(api.URL_getTokenAlamat)
                .addBodyParameter("kyano",kyano/*"0000000000000000"*/)
                .addBodyParameter("password",pass)
                .addHeaders("Content-Type","application/json")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG_TOKEN_RESPONSE", "onResponse: "+response);
                        try {
                            //int status = Integer.parseInt(response.getString("status"));
                            String token = response.getString("token");
                            String waktu = response.getString("waktu");
                            Log.d("TAG_TOKEN", "onResponse: "+token+" "+waktu);
                            sessionmanager.createToken(token);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("JSONException", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.d("NumberFormatException", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERROR_TOKEN", "onError: " + anError);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==RC_APP_UPDATE&&resultCode!=RESULT_OK){
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        //if(mAppUpdateManager!=null)mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    //notif in navigation bar
    public void showBadge(Context context, final BottomNavigationView bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.custom_badge_layout, bottomNavigationView, false);
        TextView text = badge.findViewById(R.id.notifications_badge);
        text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.profil:
                Intent intent2 = new Intent(main_fragment.this, profil.class);
                startActivity(intent2);
                finish();

                return true;

            case R.id.logout:
                sessionmanager.logout();
                Intent intent3 = new Intent(main_fragment.this, login.class);
                startActivity(intent3);
                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //inflater.inflate(R.menu.menu_sorting, menu);
        return true;
    }

    private void callFragment(android.app.Fragment fragment) {
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        try {
            int id = menuItem.getItemId();

            if (id == R.id.home) {
                fragment = new fragment_home();
                callFragment(fragment);
            }

            else if (id == R.id.report) {
                fragment = new fragment_report();
                callFragment(fragment);
            }

            else if (id == R.id.pinjaman) {
                Bundle bundle = new Bundle();
                bundle.putString("index_notif", ""+index);
                fragment = new fragment_info();
                fragment.setArguments(bundle);
                callFragment(fragment);
            }


            return true;

        }catch (RuntimeException e){
            Log.d("EROOR", "onNavigationItemSelected: "+e);
        }

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void cekGps(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.i("About GPS", "GPS is Enabled in your devide");
            } else {
                new SweetAlertDialog(main_fragment.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Informasi")
                        .setContentText("aktifkan Gps anda terlebih dahulu")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        }).show();


            }
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(main_fragment.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Konfirmasi keluar")
                .setContentText("Yakin keluar aplikasi?")
                .setConfirmText("Ya, keluar")
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }).show();
    }

    public void showMsg(String title, String msg, int type) {
        new SweetAlertDialog(main_fragment.this, type)
                .setTitleText(title)
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    private void cek_jam(final String id_user) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api.URL_get_cekWaktu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");

                    if(success){
                        int jam= jsonObject.getInt("data");
                        if(jam>=8){
                            sessionmanager.logout();
                            Intent intent3 = new Intent(main_fragment.this, login.class);
                            startActivity(intent3);
                            finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("cek_login", "onResponse: "+e);
                    helper.showMsg(main_fragment.this, "Peringatan2", "" +helper.PESAN_SERVER, helper.ERROR_TYPE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DATA_JSONEXCEPION", "onResponse: "+error);
                      //  helper.showMsg(main_fragment.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("users", id_user);
                params.put("key", api.key);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void resetMacAddress() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api.URL_resetMacAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");

                    if(success){
                        Log.d("DATA_BOOLEAN", "onResponse: "+success);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("cek_login", "onResponse: "+e);
                    helper.showMsg(main_fragment.this, "Peringatan2", "" +helper.PESAN_SERVER, helper.ERROR_TYPE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DATA_JSONEXCEPION", "onResponse: "+error);
                        helper.showMsg(main_fragment.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("users",nik);
                params.put("key", api.key);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(main_fragment.this)
                    .setTitle("Permission Request")
                    .setMessage("")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(main_fragment.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    private void getInfoTraining(BottomNavigationView
            bottomNavigationView) {
            try {
                if(!SERVICE_NOTIF.equals("")&&SERVICE_NOTIF!=null){
                index++;
                    if(!idtraining.equals("")){
                        showBadge(this,bottomNavigationView , R.id.pinjaman, ""+index);
                    }
                }
            }catch (NullPointerException e){
                Log.d("NULL_POINTER", "getInfoTraining: "+e);
            }




    }

    private Emitter.Listener  notif = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String kyano="";
                String response = data.getString("pengirim");
                JSONObject Object = new JSONObject(response);
                String no_training=Object.getString("kode");
                String materi=Object.getString("materi");
                JSONArray jsonArray = Object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectData = jsonArray.getJSONObject(i);
                    kyano= objectData.getString("kyano");
                    if(kyano.equals(kyano)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                index++;
                                    showBadge(main_fragment.this,bottomNavigationView, R.id.pinjaman, ""+index);
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("NODE_JS_ERROR", "call: "+e);
            }
        }
    };

    private void getInformasiKaryawan(final String kyano){
        AndroidNetworking.post(api.URL_informasiKaryawan)
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
                                    String kyano=data.getString("kyano");
                                    String nik=data.getString("nik");
                                    String kynm=data.getString("kynm");
                                    String kyjk=data.getString("kyjk");
                                    String kyagama=data.getString("kyagama");
                                    String kytgllahir=data.getString("kytgllhr");
                                    String kystatus_kerja=data.getString("kystatus_kerja");
                                    String kyalamat=data.getString("kyalamat");
                                    String kyhp=data.getString("kyhp");
                                    String jbnama=data.getString("jbnama");
                                    String dvnama=data.getString("dvnama");
                                    String jbano=data.getString("jbano");
                                    String dvano=data.getString("dvano");
                                    String npwp=data.getString("npwp");
                                    String tgl_masuk=data.getString("kytglmasuk");
                                    String kyemail=data.getString("kyemail");
                                    String kyalamat_skrang=data.getString("kyalamat_skrang");
                                    String hashtag=data.getString("hashtag");
                                    String kyjenis=data.getString("kyjenis");
                                    String kytptlhr=data.getString("kytptlhr");
                                    Log.d("CEK_TGL_LAHIRKU", "loadprofile: "+kytgllahir);
                                    sessionmanager.Sessionprofile(nik,kynm,kyjenis,hashtag,kyjk,kyagama,kytptlhr,kytgllahir,
                                            kystatus_kerja,kyalamat,kyhp,jbano,dvano,npwp,tgl_masuk,kyemail,kyalamat_skrang);
                                }
                            }else{
                                notifDialogResign();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.getLokasi(main_fragment.this, sessionmanager.getIdUser());
       mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
              //  helper.snackBar(lytoolbar,"CEK_VERSION= A="+UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS+" B="+result.updateAvailability());
                if(result.updateAvailability()==UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                        &&result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE,main_fragment.this,
                                RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        helper.snackBar(lytoolbar,"CEK_VERSION= "+e);
                    }
                }



            }
        });
    }


    private void notifDialogResign()  {
        dialogResign = new Dialog(main_fragment.this);
        dialogResign.setContentView(R.layout.dialogkaryawanresign);
        dialogResign.setCancelable(true);

        dialogResign.setCanceledOnTouchOutside(false);
        txJudul = (TextView) dialogResign.findViewById(R.id.txJudul);
        txInfo = (TextView) dialogResign.findViewById(R.id.txInfo);
        txClose = (TextView) dialogResign.findViewById(R.id.txClose);
        txClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResign.dismiss();
                sessionmanager.logout();
                Intent intent3 = new Intent(main_fragment.this, login.class);
                startActivity(intent3);
                finish();
            }
        });



        dialogResign.show();
    }

}
