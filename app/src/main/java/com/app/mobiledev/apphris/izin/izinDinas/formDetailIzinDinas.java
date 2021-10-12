package com.app.mobiledev.apphris.izin.izinDinas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.api.set_ip;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.mindorks.paracamera.Camera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class formDetailIzinDinas extends AppCompatActivity implements OnMapReadyCallback {
    private String kyano;
    private TextView txNik;
    private TextView txNama;
    private TextView txDivisi;
    private TextView txNoDivisi;
    private TextView tx_lokasi;
    private CoordinatorLayout form_project;
    private EditText edJam;
    private EditText edTgl;
    private ImageView image_detIzin;
    private SessionManager mSesion;
    private String nojabatan;
    private String nodivisi;
    private String waktu;
    private String tdano="";
    private String jbano="";
    private String lang="";
    private Camera camera;
    private Bitmap imageFotoIzin;
    private String encodedimage="";
    private Button btnSimpan;
    private String image,langs="0.0",lats="0.0",status,tgl;

    private Fragment map_view;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private Boolean mLocationPermissionGranted=false;
    private LocationCallback locationCallback;
    private SweetAlertDialog mProgressDialog;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34;
    private double lat=0;
    private double lon=0;
    private  Location mLastLocation;
    private set_ip mSet_api;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_detail_izin_dinas);
        mSesion=new SessionManager(formDetailIzinDinas.this);
        kyano=mSesion.getIdUser();


        mSet_api=new set_ip();
        txNik=findViewById(R.id.txNik);
        image_detIzin=findViewById(R.id.image_detIzin);
        txNama=findViewById(R.id.txNama);
        txDivisi=findViewById(R.id.txDivisi);
        form_project=findViewById(R.id.form_project);
        btnSimpan=findViewById(R.id.btnSimpan);
        txNoDivisi=findViewById(R.id.txNoDivisi);
        edTgl=findViewById(R.id.edTgl);
        edJam=findViewById(R.id.edJam);
        tx_lokasi=findViewById(R.id.tx_lokasi);
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        mProgressDialog.setCancelable(true);
        getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    try {
                        mLastLocation = location;
                        LatLng mylokasi = new LatLng(location.getLatitude(), location.getLongitude());
                        getLokasi(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylokasi));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    } catch (Exception e) {
                        Log.d("LOCATION_CALL_BACK", "onLocationResult: " + e);
                    }

                }
            }
        };


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(imageFotoIzin==null){
                    helper.showMsg(formDetailIzinDinas.this,"Informasi","foto belum diiisi");
                    mProgressDialog.dismiss();
                }else{
                    mProgressDialog.show();
                    String kyanos=kyano;
                    String dvano=nodivisi;
                    String jbano=nojabatan;
                    String tanggal=edTgl.getText().toString()+" "+edJam.getText().toString();
                    insertDetDinas(tdano,kyano,dvano,jbano,String.valueOf(lon),String.valueOf(lat),tanggal);
                }

            }
        });
        image_detIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .setImageHeight(1000)
                        .build(formDetailIzinDinas.this);
                try {
                    camera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getInformasiKaryawan(kyano);
        getDateTime();
        getExtraDetailDinas();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = camera.getCameraBitmap();
                if (bitmap != null ) {
                    image_detIzin.setImageBitmap(bitmap);
                    imageFotoIzin=bitmap;
                    imageFotoIzin=Bitmap.createScaledBitmap(imageFotoIzin, 500, 500, false);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageFotoIzin.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                    encodedimage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                }
                else { Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show(); }
            }
        }catch (Exception e){
            Log.d("TAKE_CAMERA", "onActivityResult: "+e);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
                Log.d("ENABLE_LOCATION", "onMapReady: ");
            }else{
                new SweetAlertDialog(formDetailIzinDinas.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Versi Android anda Tidak cocok")
                        .setContentText("Yakin keluar aplikasi?")
                        .setConfirmText("Ya, keluar")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                                moveTaskToBack(true);
                            }
                        }).show();}

        }

    }


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
                                    txNik.setText(""+nik);
                                    txNama.setText(""+kynm);
                                    txDivisi.setText(""+dvnama);
                                    nojabatan=jbano;
                                    nodivisi=dvano;
                                    mProgressDialog.dismiss();
                                }
                            }else{
                                mProgressDialog.dismiss();
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(formDetailIzinDinas.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(formDetailIzinDinas.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    public void getLokasi(double llat,double llon) {
        try {
            if(llat == 0){
                Log.d("CEK_LOKASI", "getLokasi: "+llat);
            }else {
                lat = llat;
                lon = llon;
                Log.d("LOKASI", "getLokasi: "+llat);
                Geocoder geocoder = new Geocoder(formDetailIzinDinas.this, Locale.getDefault());
                String result = null;
                List<Address > addressList = geocoder.getFromLocation(lat, lon, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)); //.append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    Log.d("getLOKASI", "getLokasi: "+address.getAddressLine(0));
                    tx_lokasi.setText(""+address.getAddressLine(0));
                    result = sb.toString();
                }



            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }catch (NullPointerException e){
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }catch (NumberFormatException e){
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }

    }


    private  void getDateTime(){
        AndroidNetworking.post(api.URL_getTgl_time)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                String tgll =response.getString("tanggal");
                                String waktu =response.getString("waktu");
                                edJam.setText(waktu);
                                edTgl.setText(tgll);


                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            Log.d("EROOR_JSON", "onError: " + e);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UPDATE", "onError: " + anError);
                    }
                });

    }

    private  void insertDetDinas(String tdano,String kyano,String dvano,String jbano,String lang,String lat,String tanggal){
        AndroidNetworking.post(api.URL_uploadDetailDinas)
                .addBodyParameter("key", api.key)
                .addBodyParameter("tdano", tdano)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("dvano", dvano)
                .addBodyParameter("jbano", jbano)
                .addBodyParameter("lang", lang)
                .addBodyParameter("lat", lat)
                .addBodyParameter( "img", "data:image/png;base64,"+encodedimage)
                .addBodyParameter("tanggal", tanggal)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                new SweetAlertDialog(formDetailIzinDinas.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("detail izin dinas berhasil disimpan")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .show();

                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            Log.d("EROOR_JSON", "onError: " + e);

                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UPDATE", "onError: " + anError);
                        mProgressDialog.dismiss();
                    }
                });


    }

    private void getId_izin_dinas(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            tdano= "";
            btnSimpan.setVisibility(View.VISIBLE);

        } else {
            tdano= bundle.getString("tdano");
            btnSimpan.setVisibility(View.GONE);
        }
    }

    private void getExtraDetailDinas(){
        Bundle bundle = getIntent().getExtras();
        tdano= bundle.getString("tdano");



        if(bundle == null || bundle.getString("image").equals("")) {
            btnSimpan.setVisibility(View.VISIBLE);
        }
        else {
                image= bundle.getString("image");
                langs= bundle.getString("lang");
                lats= bundle.getString("lat");
                tgl= bundle.getString("tgl");
                double latts= helper.ParseCekDouble(lats);
                double langgs=helper.ParseCekDouble(langs);
                Log.d("CEK_LOKASI_EXTRA", "getExtraDetailDinas: "+langs+" "+lats+"\n url"+mSet_api.getIpFoto_izin()+""+image);
                edTgl.setText(helper.split_date_time("date",tgl));
                edJam.setText(helper.split_date_time("time",tgl));
                getLokasi(latts,langgs);
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);
                Glide.with(formDetailIzinDinas.this).load(mSet_api.getIpFoto_izin()+""+image).thumbnail(Glide.with(formDetailIzinDinas.this).load(R.drawable.loading)).apply(requestOptions).into(image_detIzin);
                btnSimpan.setVisibility(View.GONE);
        }
        mProgressDialog.dismiss();
    }

}