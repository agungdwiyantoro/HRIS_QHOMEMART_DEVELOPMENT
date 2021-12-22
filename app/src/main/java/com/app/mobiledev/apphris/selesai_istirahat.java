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
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class selesai_istirahat extends AppCompatActivity implements OnMapReadyCallback {

    private Camera camera;
    private ImageView image;
    private Bitmap imageFoto;
    private Uri resultUri;
    private TextView tvLokasi;
    private double lat=0;
    private double lon=0;
    private TextView mstatus;
    private Button btnAbsen;
    private SessionManager sessionmanager;
    private String kyano;
    private SweetAlertDialog mProgressDialog;
    private String encodedImage;
    private LinearLayout lin_abs_selesai_istirahat;
    static final int REQUEST_IMAGE_CAPTURE = 100;
    private String alamat="";
    int i = 0;

    private FaceDetector detector;
    private int face_count=0;
    private Bitmap editedBitmap;

    //===maps//
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34;
    private Boolean mLocationPermissionGranted=false;
    private LocationRequest mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private String mlatitude,mlongtitude;
    private LocationCallback locationCallback;
    private  Location mLastLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_istirahat);
        image=findViewById(R.id.image);
        tvLokasi=findViewById(R.id.tvLokasi);
        //lin_abs_selesai_istirahat=findViewById(R.id.lin_abs_selesai_istirahat);
        mstatus=findViewById(R.id.status);
        btnAbsen=findViewById(R.id.btnAbsen);
        sessionmanager = new SessionManager(selesai_istirahat.this);
        kyano=sessionmanager.getIdUser();
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mProgressDialog.setTitleText("Loading");
        mProgressDialog.setCancelable(true);
        AndroidNetworking.initialize(getApplicationContext());
        detector = new FaceDetector.Builder(selesai_istirahat.this)
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
            @Override
            public void onClick(View v) {
                String lokasi=tvLokasi.getText().toString();
                try {
                    if(imageFoto==null){
                        helper.showMsg(selesai_istirahat.this,"Informasi","foto belum diiisi");
                        mProgressDialog.dismiss();

                    }else{
                        if(lokasi.equals("null")||lokasi.equals(null)||lokasi.equals("")){
                            helper.showMsg(selesai_istirahat.this,"","Lokasi anda belum  terdeteksi \n pastikan gps anda aktif\n coba restart aplikasi",helper.WARNING_TYPE);
                            mProgressDialog.dismiss();
                        }else{

                            new SweetAlertDialog(selesai_istirahat.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Konfirmasi")
                                    .setContentText("Yakin akan presensi\nselesai istirahat..?")
                                    .setConfirmText("Ya, Lanjutkan")
                                    .setCancelText("Tidak")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            i=0;
                                            i++;
                                            Handler handler = new Handler();
                                            Runnable r = new Runnable() {

                                                @Override
                                                public void run() {
                                                    i = 0;
                                                }
                                            };

                                            face_count=processImage();
                                            if(face_count==1){
                                                if (i == 1) {
                                                    // pushAbsen(status,kyano);
                                                    mProgressDialog.show();
                                                    absen("mulai istirahat",kyano,encodedImage);
                                                    sweetAlertDialog.dismiss();
                                                    handler.postDelayed(r, 250);
                                                } else if (i == 2) {
                                                    //Double click
                                                    i = 0;

                                                }
                                            }else{
                                                helper.showMsg(selesai_istirahat.this,"Informasi","foto wajah tidak terdeteksi atau terdeteksi lebih dari 1 satu orang");
                                                mProgressDialog.dismiss();
                                            }


                                        }
                                    }).show();



                        }
                    }

                }catch (NullPointerException e){
                    helper.showMsg(selesai_istirahat.this,"","Lokasi anda belum  terdeteksi \n pastikan gps anda aktif\n coba restart aplikasi",helper.WARNING_TYPE);
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
                        mLastLocation = location;
                        LatLng mylokasi = new LatLng(location.getLatitude(), location.getLongitude());
                        getLokasi(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylokasi));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


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
                imageFoto.compress(Bitmap.CompressFormat.PNG, 70, bytes);
                encodedImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

            }else{
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.d("TAKE_CAMERA", "onActivityResult: "+e);
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
                Geocoder geocoder = new Geocoder(selesai_istirahat.this, Locale.getDefault());
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


    private void absen(final String status,final String kyano,final String encodeimage){
        AndroidNetworking.post(api.URL_absen)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter( "img", "data:image/png;base64,"+encodeimage)
                .addBodyParameter("status",status)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("tgl_mobile",sessionmanager.getTglPresensiMasuk())
                .addBodyParameter("lokasi", lat+","+lon)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("abbreakin_alamat", alamat)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Boolean success = response.getBoolean("status");
                            if (success) {
                                String data = response.getString("ket");
                                new SweetAlertDialog(selesai_istirahat.this, SweetAlertDialog.SUCCESS_TYPE)
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
                                String data = response.getString("ket");
                                helper.showMsg(selesai_istirahat.this,"Informasi",""+helper.PESAN_SERVER,helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(selesai_istirahat.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(selesai_istirahat.this, "Peringatan", "ERROR SERVER SELESAI ISTIRAHAT" + anError, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();

                    }
                });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
                Log.d("ENABLE_LOCATION", "onMapReady: ");
            }else{
                new SweetAlertDialog(selesai_istirahat.this, SweetAlertDialog.WARNING_TYPE)
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
                }
            }
        }
        updateLocationUI();
    }
    @Override
    public void onBackPressed() {
        finish();

    }

    private Integer processImage(){
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

    private void takeFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
        }
    }
}
