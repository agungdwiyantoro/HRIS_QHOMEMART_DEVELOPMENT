package com.app.mobiledev.apphris.formKunjungan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
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
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;

public class form_kunjungan extends AppCompatActivity implements OnMapReadyCallback {
    private TextView tvLokasi;
    private Camera camera;
    private GoogleMap mMap;
    private String TAG = "FORM_PROJECT", kyano = "";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34;
    private Boolean mLocationPermissionGranted = false;
    private LocationRequest mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private String mlatitude, mlongtitude;
    private LocationCallback locationCallback;
    private Location mLastLocation;
    private String encodedimage = "";
    private double lat = 0;
    private double lon = 0;
    private SessionManager sessionmanager;
    private Bitmap imageFoto;
    private EditText edNama, edOwner, edJawab, edTelp, edTahapan, edJam, edTgl, edAlamat;
    private Spinner spJenis;
    private ImageView image;
    private Button btnSimpan;
    private CoordinatorLayout form_project;
    private Toolbar toolbar_fromProject;
    private String idform;
    private String nmproyek;
    private String alamtproyek;
    private String ownerProyek;
    private String penanggungJwb;
    private String noTelp;
    private String thpProyek;
    private String jam;
    private String tgl;
    private String lokasi;
    private String jenis;
    private String gambar;
    private String status;
    private String[] jp = new String[]{"Lama", "Baru"};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_poject);
        //disable focus edittext
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tvLokasi = findViewById(R.id.tvLokasi);
        edNama = findViewById(R.id.edNama);
        edOwner = findViewById(R.id.edOwner);
        edJawab = findViewById(R.id.edJawab);
        edTelp = findViewById(R.id.edTelp);
        edTahapan = findViewById(R.id.edTahapan);
        edJam = findViewById(R.id.edJam);
        //make disable edJam
        edJam.setEnabled(false);
        edTgl = findViewById(R.id.edTgl);
        //make disable edTgl
        edTgl.setEnabled(false);
        edAlamat = findViewById(R.id.edAlamat);
        //make disable edJam
        edAlamat.setEnabled(false);
        spJenis = findViewById(R.id.spinJenis);
        image = findViewById(R.id.image);
        btnSimpan = findViewById(R.id.btnSimpan);
        toolbar_fromProject = findViewById(R.id.toolbar_fromProject);
        form_project = findViewById(R.id.form_project);
        toolbar_fromProject.setTitle("Form Kunjungan");
        setSupportActionBar(toolbar_fromProject);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionmanager = new SessionManager(form_kunjungan.this);
        kyano = sessionmanager.getIdUser();


        toolbar_fromProject.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .setImageHeight(1000)
                        .build(form_kunjungan.this);
                try {
                    camera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(form_kunjungan.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("konfirmasi")
                        .setContentText("anda yakin untuk menyimpan data ini?")
                        .setConfirmText("simpan")
                        .setCancelText("batal")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                cekValidasi();
                            }
                        }).show();

            }
        });

        spitipe();
        getDateTime();
        getintent();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = camera.getCameraBitmap();
                if (bitmap != null) {
                    image.setImageBitmap(bitmap);
                    imageFoto = bitmap;
                    imageFoto = Bitmap.createScaledBitmap(imageFoto, 500, 500, false);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageFoto.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                    encodedimage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);


                } else {
                    Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.d("TAKE_CAMERA", "onActivityResult: " + e);
        }

    }


    private void simpanForm(final String namaProyek, final String alamat, final String owner, final String no_telp, final String thpProyek, final String jam, final String tanggal, final String lokasi, final String jenis, final String tanggung_jwb,final String idform) {
                AndroidNetworking.post(api.URL_insertFormKunjungan)
                .addBodyParameter("img", "data:image/PNG;base64,"+encodedimage)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("lokasi", lokasi)
                .addBodyParameter("namaProyek", namaProyek)
                .addBodyParameter("alamat", alamat)
                .addBodyParameter("owner", owner)
                .addBodyParameter("no_telp", no_telp)
                .addBodyParameter("thpProyek", thpProyek)
                .addBodyParameter("jam", jam)
                .addBodyParameter("tanggung_jwb", tanggung_jwb)
                .addBodyParameter("jenis", jenis)
                .addBodyParameter("tgl", tanggal)
                .addBodyParameter("id_form", idform)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String data = response.getString("ket");
                            Log.d(TAG, "onActivityResult: "+encodedimage);
                            if (success) {
                                new SweetAlertDialog(form_kunjungan.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("" + data)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();


                            } else {
                                helper.showMsg(form_kunjungan.this, "Informasi", "" + data, helper.WARNING_TYPE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: " + e);
                            helper.showMsg(form_kunjungan.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(form_kunjungan.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: " + anError);

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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
                Log.d("ENABLE_LOCATION", "onMapReady: ");
            } else {
                new SweetAlertDialog(form_kunjungan.this, SweetAlertDialog.WARNING_TYPE)
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
                        }).show();
            }

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

    public void getLokasi(double llat, double llon) {
        try {
            String lok_lat = "";
            String lok_lon = "";
            if (llat == 0) {
                Log.d("CEK_LOKASI", "getLokasi: " + llat);
            } else {
                if(idform.equals("")){
                    lat = llat;
                    lon = llon;
                }else{
                    String[] split_lokasi = lokasi.split(",");
                     lok_lat = split_lokasi[0];
                    lok_lon = split_lokasi[1];
                    Log.d(TAG, "getLokasi lok_lon: "+lok_lon);
                }

                Log.d("LOKASI", "getLokasi: " + llat);
                Geocoder geocoder = new Geocoder(form_kunjungan.this, Locale.getDefault());
                String result = null;
                List<Address> addressList=null;

                if(idform.equals("")){
                     addressList = geocoder.getFromLocation(lat, lon, 1);
                }else{
                   addressList = geocoder.getFromLocation(Double.parseDouble(lok_lat), Double.parseDouble(lok_lon), 1);
                }


                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)); //.append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    Log.d("getLOKASI", "getLokasi: " + address.getAddressLine(0));
                    //tvLokasi.setText(""+address.getAddressLine(0));
                    edAlamat.setText("" + address.getAddressLine(0));
                    result = sb.toString();
                }


            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        } catch (NullPointerException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }

    }

    private void cekValidasi() {
        try {
            String nmProyek = edNama.getText().toString();
            String alamat = edAlamat.getText().toString();
            String owner = edOwner.getText().toString();
            String nmJawab = edJawab.getText().toString();
            String telp = edTelp.getText().toString();
            String tahap = edTahapan.getText().toString();
            String jam = edJam.getText().toString();
            String tgl_convert = edTgl.getText().toString();
            String tgl = convertDateBack(tgl_convert);
            Log.d("TAG_BACK", "cekCONVERTBACK: "+tgl);
            //String jenis= "lama";
            String jenis = spJenis.getSelectedItem().toString();
            if (nmProyek.equals("")) {
                helper.snackBar(form_project, "nama proyek belum diisi.......!!");
            } else if (alamat.equals("")) {
                helper.snackBar(form_project, "alamat proyek belum diisi......!!");
            } else if (owner.equals("")) {
                helper.snackBar(form_project, "owner proyek belum diisi......!!");
            } else if (nmJawab.equals("")) {
                helper.snackBar(form_project, "penanggung jawab belum diisi......!!");
            } else if (telp.equals("")) {
                helper.snackBar(form_project, "no telephone belum diisi......!!");
            } else if (tahap.equals("")) {
                helper.snackBar(form_project, "tahapan proyek belum diisi......!!");
            } else if(jam.equals("")){
                helper.snackBar(form_project,"jam proyek belum diisi......!!");
            } else if(tgl.equals("")){
                helper.snackBar(form_project,"tanggal proyek belum diisi......!!");
            } else if(encodedimage.equals("")){
                helper.snackBar(form_project,"Foto Belum diisi......!!");
            }else {
                if(!idform.equals("")){
                    //update
                    simpanForm(nmProyek, alamat, owner, telp, tahap, jam, tgl, "" + lat + "," + lon, jenis, nmJawab,idform);
                    Log.d(TAG, "cekValidasi,update: "+nmProyek);
                }else{
                    simpanForm(nmProyek, alamat, owner, telp, tahap, jam, tgl, "" + lat + "," + lon, jenis, nmJawab,"");
                    Log.d(TAG, "cekValidasi,simpan: "+idform);
                }

                Log.d("TAG_SIMPAN", "cekValidasi: "+nmProyek+alamat+owner+telp+tahap+jam+"TANGGAL = "+tgl+" =="+lat+lon+jenis+nmJawab);
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "cekValidasi: " + e);
        }

    }

    private void spitipe() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, jp);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spJenis.setAdapter(spinnerArrayAdapter);
    }

    private void getDateTime() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
                 AndroidNetworking.post(api.URL_getDateTime)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String waktu = response.getString("waktu");
                            String j = waktu.substring(11, 16);
                            String t = waktu.substring(0, 10);
                            edJam.setText(j);
                            //edTgl.setText(t);

                            String date = t;
                            convertDate(date);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);


                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Log.d("JSONPROduk", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_PRODUK", "onError: " + anError);

                    }
                });
    }

    private void convertDate(String date) {
        String strDate = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(strDate);
            SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd-MM-yyyy");
            String finalDateString = sdfnewformat.format(convertedDate);
            Log.d("TAG_DD", "onResponse: " + finalDateString);
            edTgl.setText(finalDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String convertDateBack(String date) {
        String strDate = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(strDate);
            SimpleDateFormat sdfnewformat = new SimpleDateFormat("yyyy-MM-dd");
            String finalDateString = sdfnewformat.format(convertedDate);
            Log.d("TAG_DD", "onResponse: " + finalDateString);
            date = finalDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    private void getintent() {
        idform=getIntent().getExtras().getString("idform");
        nmproyek=getIntent().getExtras().getString("nmproyek");
        alamtproyek=getIntent().getExtras().getString("alamtproyek");
        ownerProyek=getIntent().getExtras().getString("ownerproyek");
        penanggungJwb=getIntent().getExtras().getString("penanggungjwb");
        noTelp=getIntent().getExtras().getString("notelp");
        thpProyek=getIntent().getExtras().getString("thpproyek");
        jam=getIntent().getExtras().getString("jam");
        tgl=getIntent().getExtras().getString("tgl");
        jam=getIntent().getExtras().getString("jam");
        lokasi=getIntent().getExtras().getString("lokasi");
        jenis=getIntent().getExtras().getString("jenis");
        gambar=getIntent().getExtras().getString("image");

        if(!idform.equals("")){
            edNama.setText(""+nmproyek);
            edOwner.setText(""+ownerProyek);
            edJawab.setText(""+penanggungJwb);
            edTelp.setText(""+noTelp);
            edTahapan.setText(""+thpProyek);
            edJam.setText(""+jam);
            edTgl.setText(""+tgl);
            btnSimpan.setText("Update");
            if(jenis.equals("lama")){
                spJenis.setSelection(0);
            }else{
                spJenis.setSelection(1);
            }
            Log.d(TAG, "getintent: "+lokasi);
        }


    }

}
