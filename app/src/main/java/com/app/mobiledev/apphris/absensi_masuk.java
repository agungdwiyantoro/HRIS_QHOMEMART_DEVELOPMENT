package com.app.mobiledev.apphris;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.mindorks.paracamera.Camera;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;


import cn.pedant.SweetAlert.SweetAlertDialog;
public class absensi_masuk extends AppCompatActivity implements OnMapReadyCallback {
    private Camera camera;
    private ImageView image;
    private Bitmap imageFoto;
    private TextView tvPesan;
    private Uri resultUri;
    private TextView tvLokasi;
    private double lat=0;
    private double lon=0;
    private Button btnAbsen;

    private SessionManager sessionmanager;
    private String kyano;
    private SweetAlertDialog mProgressDialog;
    private String encodedimage="";
    private ConstraintLayout cons_absen_masuk;
    double radiusInMeters = 200.0;

    int i = 0;
    private FaceDetector detector;
    private int face_count=0;
    private Bitmap editedBitmap;
    private String outlokasi="";
    private double jarak=0;
    private float jarak_janti=0;
    private float jarak_janti_lestari=0;
    private float jarak_piyungan=0;
    private float jarak_berbah=0;
    private String alamat="";
    static final int REQUEST_IMAGE_CAPTURE = 100;


    //lokasi latitude dang longtitude
    double janti_lat=0;
    double janti_long=0;
    double janti_lestari_lat=0;
    double janti_lestari_long=0;
    double piyungan_lat=0;
    double piyungan_long=0;
    double berbah_lat=0;
    double berbah_long=0;



    //===maps//
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34;
    private Boolean mLocationPermissionGranted=false;
    private LocationRequest mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private String mlatitude,mlongtitude,presensi_jarak;
    private LocationCallback locationCallback;
    private  Location mLastLocation;
    private Marker geoFenceMarker;

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_masuk);
        image=findViewById(R.id.image);
        tvLokasi=findViewById(R.id.tvLokasi);
        btnAbsen=findViewById(R.id.btnAbsen);
        cons_absen_masuk=findViewById(R.id.cons_absen_masuk);
        sessionmanager = new SessionManager(absensi_masuk.this);
        kyano=sessionmanager.getIdUser();
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        presensi_jarak=sessionmanager.getPresensiJarakJauh();
        tvPesan=findViewById(R.id.tvPesan);
        mProgressDialog.setCancelable(true);
        if(sessionmanager.getJarak().equals("")||sessionmanager.getJarak().equals("0")){
            jarak=radiusInMeters;
        }else{
            jarak=Double.parseDouble(sessionmanager.getJarak());
        }
        Log.d("CEK_JARAK", "onCreate: "+jarak);

        AndroidNetworking.initialize(getApplicationContext());

        detector = new FaceDetector.Builder(absensi_masuk.this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeFoto();
            }
        });




        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                String lokasi=tvLokasi.getText().toString();
                try {
                    if(imageFoto==null){
                        helper.showMsg(absensi_masuk.this,"Informasi","foto belum diiisi");
                        mProgressDialog.dismiss();
                    }else{
                        if(lokasi.equals("null")||lokasi.equals(null)||lokasi.equals("")){
                            helper.showMsg(absensi_masuk.this,"","Lokasi anda belum  terdeteksi \n pastikan gps anda aktif\n coba restart aplikasi",helper.WARNING_TYPE);
                            mProgressDialog.dismiss();
                        }else{
                            i=0;
                            i++;
                            Handler handler = new Handler();
                            Runnable r = new Runnable() {
                                @Override
                                public void run() { i = 0; }
                            };
                            if (i == 1) {
                                face_count=processImage();
                                if(face_count==1){

                                    if((jarak_janti<=jarak)||(jarak_janti_lestari<=jarak)||(jarak_berbah<=jarak)||(jarak_piyungan<=jarak)){

                                        absen("absensi masuk",kyano,"N");
                                    }else {
                                        if(presensi_jarak.equals("1")){
                                            absen("absensi masuk",kyano,"Y");
                                        }else{
                                            helper.snackBar(cons_absen_masuk,"anda diluar area yang ditetapkan, silahkan hubungi HRD");
                                            mProgressDialog.dismiss();
                                        }
                                    }
                                }else{
                                    helper.showMsg(absensi_masuk.this,"Informasi","foto wajah tidak terdeteksi atau terdeteksi lebih dari 1 satu orang");
                                    mProgressDialog.dismiss();
                                }
                                handler.postDelayed(r, 250);
                            } else if (i == 2) {
                                i = 0;
                            }

                        }
                    }

                }catch (NullPointerException e){
                    helper.showMsg(absensi_masuk.this,"","Lokasi anda belum  terdeteksi \n pastikan gps anda aktif\n coba restart aplikasi",helper.WARNING_TYPE);
                    mProgressDialog.dismiss();
                }
            }
        });

        getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }


        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    try{
                        mMap.clear();
                        mLastLocation = location;
                        LatLng mylokasi = new LatLng(location.getLatitude(), location.getLongitude());
                        String lokasijanti=sessionmanager.getLokasiJanti();
                        String lokasijantilestari=sessionmanager.getLokasiJantiLestari();
                        String lokasipiyungan=sessionmanager.getLokasiPiyungan();
                        Log.d("LOKASI_PIYUNGAN", "onLocationResult: "+lokasipiyungan);
                        String lokasiberbah=sessionmanager.getLokasiBerbah();
                        String[] arr_lokasi_janti = lokasijanti.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);//split string ke araay
                            janti_lat=Double.parseDouble(arr_lokasi_janti[0]);
                            janti_long=Double.parseDouble(arr_lokasi_janti[1]);

                        String[] arr_lokasi_janti_lestari = lokasijantilestari.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                            janti_lestari_lat=Double.parseDouble(arr_lokasi_janti_lestari[0]);
                            janti_lestari_long=Double.parseDouble(arr_lokasi_janti_lestari[1]);



                        String[] arr_lokasi_piyungan = lokasipiyungan.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                        piyungan_lat=Double.parseDouble(arr_lokasi_piyungan[0]);
                        piyungan_long=Double.parseDouble(arr_lokasi_piyungan[1]);

                        String[] arr_lokasi_berbah = lokasiberbah.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                        berbah_lat=Double.parseDouble(arr_lokasi_berbah[0]);
                        berbah_long=Double.parseDouble(arr_lokasi_berbah[1]);

                        LatLng lokasi_janti = new LatLng( janti_lat,janti_long);
                        LatLng lokasi_janti_lestari = new LatLng( janti_lestari_lat,janti_lestari_long);
                        LatLng lokasi_piyungan = new LatLng( piyungan_lat,piyungan_long);
                        LatLng lokasi_berbah = new LatLng( berbah_lat,berbah_long);

                        getLokasi(location.getLatitude(), location.getLongitude());
                        int strokeColor = 0xffff0000; //red outline
                        int shadeColor = 0x44ff0000; //opaque red fill

                        CircleOptions circleOptions_janti = new CircleOptions().center(lokasi_janti).radius(jarak).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                        MarkerOptions markerOptions_janti = new MarkerOptions().position(lokasi_janti);

                        CircleOptions circleOptions_janti_lestari = new CircleOptions().center(lokasi_janti_lestari).radius(jarak).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                        MarkerOptions markerOptions_janti_lestari = new MarkerOptions().position(lokasi_janti_lestari);

                        CircleOptions circleOptions_piyungan = new CircleOptions().center(lokasi_piyungan).radius(jarak).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                        MarkerOptions markerOptions_piyungan = new MarkerOptions().position(lokasi_piyungan);

                        CircleOptions circleOptions_berbah = new CircleOptions().center(lokasi_berbah).radius(jarak).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                        MarkerOptions markerOptions_berbah = new MarkerOptions().position(lokasi_berbah);
                          mMap.moveCamera(CameraUpdateFactory.newLatLng(mylokasi));
                          mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                          mMap.addCircle(circleOptions_janti);
                          mMap.addMarker(markerOptions_janti);
                          mMap.addCircle(circleOptions_janti_lestari);
                          mMap.addMarker(markerOptions_janti_lestari);
                          mMap.addCircle(circleOptions_piyungan);
                          mMap.addMarker(markerOptions_piyungan);
                        mMap.addCircle(circleOptions_berbah);
                        mMap.addMarker(markerOptions_berbah);
                        jarak_janti=distanceBetween(mylokasi,lokasi_janti);
                        jarak_janti_lestari=distanceBetween(mylokasi,lokasi_janti_lestari);
                        jarak_piyungan=distanceBetween(mylokasi,lokasi_piyungan);
                        jarak_berbah=distanceBetween(mylokasi,lokasi_berbah);
                        presensi_jarak=sessionmanager.getPresensiJarakJauh();
                        cekPresensiJarakJauh();

                    }catch (Exception e){
                        Log.d("LOCATION_CALL_BACK", "onLocationResult: "+e);
                    }

                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode==100){
                Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(bitmap);
                imageFoto=bitmap;
                imageFoto=Bitmap.createScaledBitmap(imageFoto, 500, 500, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageFoto.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                encodedimage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

            }else{
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();;
        }

    }


    private void takeFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
        }
    }

    private float distance(){
        float jarak=0;
        float jarak2=0;
        try{
            Location startPoint=new Location("locationA");
            startPoint.setLatitude(helper.latitude_bj);
            startPoint.setLongitude(helper.longtitude_bj);

            Location endPoint=new Location("locationB");
            endPoint.setLatitude(lat);
            endPoint.setLongitude(lon);
            float results[]=new float[10];
            Location.distanceBetween(helper.latitude_bj,helper.longtitude_bj,lat,lon,results);
            jarak2=startPoint.distanceTo(endPoint);
            DecimalFormat df = new DecimalFormat("#");
            jarak=results[0];


        }catch (NumberFormatException e){
            Log.d("numberformat", "distance: "+e);
        }catch (NullPointerException e){
            Log.d("nullformat", "distance: "+e);
        }
        return jarak2;
    }


    private void cekPresensiJarakJauh(){
       // Log.d("CEK_Presensi_jarak", "cekPresensiJarakJauh: "+presensi_jarak);
        if((jarak_janti<=jarak)||(jarak_janti_lestari<=jarak)||(jarak_berbah<=jarak)||(jarak_piyungan<=jarak)){
            Log.d("CEK_cekPresensi2", "cekPresensiJarakJauh: "+presensi_jarak);
            btnAbsen.setTextColor(Color.parseColor("#FFFFFF"));
            btnAbsen.setBackgroundColor(Color.parseColor("#3B9C40"));
            tvPesan.setVisibility(View.GONE);
            btnAbsen.setEnabled(true);
            image.setEnabled(true);

        }else{
            if(presensi_jarak.equals("0")) {
                Log.d("cek_jarak_cek", "cekPresensiJarakJauh: "+presensi_jarak);
                btnAbsen.setTextColor(Color.parseColor("#FFFFFF"));
                btnAbsen.setBackgroundColor(Color.parseColor("#C1C0C7"));
                tvPesan.setText("Anda berada di luar area yang ditetapkan, mohon berada di dalam area untuk melakukan presensi");
                tvPesan.setTextColor(Color.parseColor("#D81B60"));
                tvPesan.setVisibility(View.VISIBLE);
                btnAbsen.setEnabled(false);
                image.setEnabled(false);
        }

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
            Geocoder geocoder = new Geocoder(absensi_masuk.this, Locale.getDefault());
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
                    alamat=address.getAddressLine(0);
                    tvLokasi.setText(""+address.getAddressLine(0));
                    result = sb.toString();
                }



        }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }catch (NullPointerException e){
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }

    }





    @Override
    public void onBackPressed() {
//        fragment_home fragment = (fragment_home) getFragmentManager().findFragmentById(R.id.lmenu);
//        fragment.<getabsen>();
        finish();

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void absen(final String status, final String kyano,String outlokasi){
        AndroidNetworking.post(api.URL_absen)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter( "img", "data:image/png;base64,"+encodedimage)
                .addBodyParameter("status",status)
                .addBodyParameter("out_lokasi",outlokasi)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("lokasi", lat+","+lon)
                .addBodyParameter("abin_alamat",alamat )
                .addBodyParameter("ip", helper.getDeviceIMEI(absensi_masuk.this))
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
                                String tgl_masuk= response.getString("tgl_masuk");
                                sessionmanager.createTglPresensiMasuk(tgl_masuk);
                                Log.d("session_tgl_masuk_m", "onResponse: "+tgl_masuk);
                                new SweetAlertDialog(absensi_masuk.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText(""+data)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                                sweetAlertDialog.dismiss();
                                            }
                                        })
                                        .show();


                            } else {

                                helper.showMsg(absensi_masuk.this,"Informasi",""+data,helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(absensi_masuk.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(absensi_masuk.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        Log.d("EROOR_EXCEPTION", "onError: status:"+status+" outlokasi"+outlokasi+" kyano"+kyano+" lat"+lat+" long"+lon+" alamat"+alamat+" imei"+helper.getDeviceIMEI(absensi_masuk.this));
                        mProgressDialog.dismiss();

                    }
                });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
               mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

                Log.d("ENABLE_LOCATION", "onMapReady: ");
            }else{
                new SweetAlertDialog(absensi_masuk.this, SweetAlertDialog.WARNING_TYPE)
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

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
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


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(absensi_masuk.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
        updateLocationUI();
    }
    private Integer processImage() {
        int jml_face=0;
        Bitmap bitmap = imageFoto;
        if (detector.isOperational() && bitmap != null) {
            editedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), bitmap.getConfig());
            float scale = getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setTextSize((int) (16 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);
            Canvas canvas = new Canvas(editedBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
            SparseArray<Face> faces = detector.detect(frame);

            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);
                canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);
                jml_face=index + 1;

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 8, paint);
                }
            }

            if (faces.size() == 0) {
            } else {
               image.setImageBitmap(editedBitmap);

            }
        } else {
            Log.d("", "processImage: ");
        }
        return jml_face;
    }

    private void markerForGeofence(LatLng latLng) {
        Log.i("CEK", "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if ( mMap!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = mMap.addMarker(markerOptions);
        }
    }


    private float distanceBetween(LatLng latLng1, LatLng latLng2)
    {
        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        Location loc2 = new Location(LocationManager.GPS_PROVIDER);
        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);
        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);
        return loc1.distanceTo(loc2);       //in meters
    }

    private Geofence createGeofence(LatLng latLng, float radius ) {
        Log.d("cek_GEOFANCING", "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }


    private GeofencingRequest createGeofenceRequest(Geofence geofence ) {
        Log.d("cek_fake_gps1", "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence(geofence)
                .build();
    }





}
