package com.app.mobiledev.apphris.profile;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.main_fragment;
import com.app.mobiledev.apphris.profile.PerjanjianKerja.PerjanjianKerja;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.vision.face.FaceDetector;
import com.mindorks.paracamera.Camera;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class profil extends AppCompatActivity {

    private TextView txNama, txNik, txDivisi, txHastag, txJabatan, txUpdate;
    private EditText pass;
    private Camera camera;
    private Button btnSimpan, btnPass;
    private FaceDetector detector;
    private CheckBox checkBox;
    private SessionManager sessionmanager;
    private String kyano,namas,namaLengkap,cekStaff,password, hastag, nik, divisi, jabatan;
    private Toolbar mToolbar;
    private  TextView txtSampleDesc;
    private ProgressDialog mProgressDialog;
    private TextInputLayout tlNama;
    //private CircleImageView foto_profil;
    private Uri resultUri;
    private Bitmap image_bmap;
    int currentIndex = 0;
    private int[] imageArray;
    private Uri imageUri;
    private String url_foto="";
    private String encodedimage="";
    private Bitmap editedBitmap;
    private Drawable drawable;
    private int face_count=0;

    TextInputLayout tilUbahPass;

    private CardView cvUpdateDataDiri, cvPerjanjianKerja, cvUbahPassword;
    private ImageView foto_profil;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_new);
        mToolbar = findViewById(R.id.toolbar_abs);
        foto_profil=findViewById(R.id.foto_profil);
        //txNik = findViewById(R.id.txtNik);
        txNama = findViewById(R.id.txtNamaFull);
        txDivisi = findViewById(R.id.txtDivisi);
        txJabatan = findViewById(R.id.txtJabatan);
        txHastag = findViewById(R.id.txtHastag);
        txUpdate = findViewById(R.id.txtUpdateDataDiri);

        cvUpdateDataDiri = findViewById(R.id.cvUpdateDataDiri);
        cvUbahPassword = findViewById(R.id.cvUbahPassword);
        cvPerjanjianKerja = findViewById(R.id.cvPerjanjianKerja);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Profil");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        AndroidNetworking.initialize(getApplicationContext());


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(profil.this, main_fragment.class);
                startActivity(intent2);
                finish();
            }
        });

        sessionmanager = new SessionManager(profil.this);
        kyano       = sessionmanager.getIdUser();
        nik         = sessionmanager.getNik();
        namas       = sessionmanager.getUsername();
        password    = sessionmanager.getPass();
        namaLengkap = sessionmanager.getNamaLEngkap();
        cekStaff    = sessionmanager.getCekStaff();
        hastag      = sessionmanager.getHashtag();

        getInformasiKaryawan(kyano);

        //txNik.setText(nik);
        txNama.setText(namaLengkap);
        txDivisi.setText(cekStaff);
        txHastag.setText("#"+hastag);
        txHastag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) profil.this.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(txHastag.getText());
                Toast.makeText(profil.this, "Hastag berhasil dicopy", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("DATA_DIRI", "onCreate: "+password+" USER : "+namaLengkap+" NIK : "+cekStaff+" HASTAG : "+hastag);
        mProgressDialog.show();

        getlImageProfil(kyano);

        foto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(profil.this, UbahFoto.class);
                startActivity(intent2);
                finish();
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1);
                /*camera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .setImageHeight(1000)
                        .build(profil.this);
                try {
                    camera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });

        //tilUbahPass = findViewById(R.id.tilUbahPass);
        cvUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(profil.this, UbahPass.class);
                startActivity(intent2);
                finish();
            }
        });

        cvUpdateDataDiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profil.this, UpdateDataDiri.class);
                startActivity(intent);
            }
        });

        cvPerjanjianKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profil.this, PerjanjianKerja.class);
                startActivity(intent);
                //Toast.makeText(profil.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(profil.this, main_fragment.class);
        startActivity(intent2);
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
                               url_foto=data;
                                RequestOptions requestOptions = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true);
                                Glide.with(profil.this).load(api.get_url_foto_profil(kyano,url_foto)).apply(requestOptions).into(foto_profil);
                            } else {
                                Log.d("", "onResponse: "+data);
                            }

                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(profil.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(profil.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();
                    }
                });
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

                                    if (dvnama.equals("BUMI BERLIAN MEGA INDONESIA")) {
                                        txDivisi.setText("BBMI");
                                    } else {
                                        txDivisi.setText(dvnama);
                                    }

                                    txJabatan.setText(jbnama);

                                }
                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(profil.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(profil.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);

                    }
                });

    }
}
