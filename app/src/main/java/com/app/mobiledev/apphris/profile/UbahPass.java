package com.app.mobiledev.apphris.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.login;
import com.app.mobiledev.apphris.main_fragment;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.google.android.gms.vision.face.FaceDetector;
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class UbahPass extends AppCompatActivity {

    private TextView nama;
    private EditText pass;
    private Camera camera;
    private Button btnSimpan;
    private FaceDetector detector;
    private CheckBox checkBox;
    private SessionManager sessionmanager;
    private String kyano,namas,namaLengkap,cekStaff,password;
    private Toolbar mToolbar;
    private  TextView txtSampleDesc;
    private ProgressDialog mProgressDialog;
    private TextInputLayout tlNama;
    private CircleImageView foto_profil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_pass);

        nama=findViewById(R.id.nama);
        pass=findViewById(R.id.pass);
        btnSimpan=findViewById(R.id.btnSimpan);
        checkBox=findViewById(R.id.ck_pass);
        mToolbar = findViewById(R.id.toolbar_abs);
        tlNama=findViewById(R.id.tlNama);

        mToolbar = findViewById(R.id.toolbarUbahFoto);
        mToolbar.setTitle("Ubah Password");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        AndroidNetworking.initialize(getApplicationContext());

        sessionmanager = new SessionManager(UbahPass.this);
        kyano=sessionmanager.getIdUser();
        namas=sessionmanager.getUsername();
        password=sessionmanager.getPass();
        namaLengkap=sessionmanager.getNamaLEngkap();
        cekStaff=sessionmanager.getCekStaff();

        Log.d("DATA_DIRI", "onCreate: "+password+" USER"+namaLengkap);
        mProgressDialog.show();
        nama.setText(""+namaLengkap);
        getPassword(kyano);
        checkBox.setChecked(false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=pass.getText().toString();
                if(password.equals("")){
                    tlNama.setError("password harus diisi..!!");
                }else{
                    insertPassword(kyano,password);
                }
                mProgressDialog.dismiss();

            }
        });
    }

    private void getPassword(final String kyano){
        AndroidNetworking.post(api.URL_getPassword)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String passwrd = response.getString("status");
                            pass.setText(""+passwrd);
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(UbahPass.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(UbahPass.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UbahPass.this, main_fragment.class);
        startActivity(intent);
        finish();
    }

    private void insertPassword(final String kyano,String pass){
        AndroidNetworking.post(api.URL_updatePassword)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("pass", pass)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String data = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(UbahPass.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText(""+data)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sessionmanager.logout();
                                                Intent intent = new Intent(UbahPass.this, login.class);
                                                startActivity(intent);
                                                Toast.makeText(UbahPass.this, "Password anda berhasil diubah", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(UbahPass.this, "Silakan login kembali", Toast.LENGTH_SHORT).show();
                                                sessionmanager.logout();
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                helper.showMsg(UbahPass.this,"Informasi",""+data,helper.WARNING_TYPE);
                            }

                            getPassword(kyano);
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(UbahPass.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(UbahPass.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();
                    }
                });
    }


}