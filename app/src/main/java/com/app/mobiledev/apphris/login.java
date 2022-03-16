package com.app.mobiledev.apphris;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {
    TextInputLayout validIDUser, validPassword;
    EditText txtIDUser, txtPassword;
    TextView lupa_password;
    Button btnLogin;
    String token;
    String id_user, password;
    private CheckBox checkBox;
    SessionManager sessionManager;
    private TextView txBantuan,tx_versi;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        validIDUser = findViewById(R.id.valid_user);
        txBantuan=findViewById(R.id.btnBantuan);
        lupa_password=findViewById(R.id.lupa_password);
        checkBox=findViewById(R.id.ck_pass);
        validPassword = findViewById(R.id.valid_pass);
        txtIDUser = findViewById(R.id.inUser);
        txtPassword = findViewById(R.id.inPass);
        btnLogin = findViewById(R.id.btnLogin);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        tx_versi=findViewById(R.id.tx_versi);
        checkBox.setChecked(false);
        token=helper.ConfigFCM();

        //call FCM configuration


        lupa_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, lupa_password.class);
                startActivity(i);

            }
        });
        txBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openWhatsApp("+6282324281046","(NIK)-(Nama Lengkap) : ");
                Intent i = new Intent(login.this, VideoUsing.class);
                startActivity(i);
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                { txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                { txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_user = txtIDUser.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                if (id_user.isEmpty()) {
                    validIDUser.setError("User harus diisi!");
                    validIDUser.requestFocus();
                } else if (password.isEmpty()) {
                    validPassword.setError("Password harus diisi!");
                    validPassword.requestFocus();
                } else {
                    //mProgressDialog.show();
                    validIDUser.setErrorEnabled(false);
                    validPassword.setErrorEnabled(false);
                    auth_user(id_user, password);
                }
            }
        });
        getVersi();
    }


    private void auth_user(final String id_user, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api.URL_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        String email="";
                        String password="";
                        String noUser="";
                        String nik="";
                        String cekStaff="";
                        String namaLengkap="";
                        String hashtag="";
                        String kydivisi="";
                        String divisi="";
                        String jabatan="";
                        String kyjabatan="";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            email = data.getString("kyemail");
                            noUser = data.getString("kyano");
                            password = data.getString("kypassword");
                            nik = data.getString("nik");
                            cekStaff = data.getString("kyjenis");
                            namaLengkap = data.getString("kynm");
                            hashtag = data.getString("hashtag");
                            kydivisi = data.getString("kydivisi");
                            divisi = data.getString("divisi");
                            kyjabatan = data.getString("kyjabatan");
                            jabatan = data.getString("jabatan");
                            Log.d("STAFF_cek_nik", "onResponse: "+nik);

                        }

                        sessionManager.createSession(
                                noUser, email,password,
                                nik,cekStaff,namaLengkap,
                                hashtag, kydivisi, divisi,
                                kyjabatan,jabatan);
                        Toast.makeText(login.this, "Selamat datang " + namaLengkap, Toast.LENGTH_SHORT).show();
                        Intent utm = new Intent(login.this, main_fragment.class);
                        utm.putExtra("username",email);
                        startActivity(utm);
                        finish();
                    } else {
                        String data = jsonObject.getString("data");
                        helper.showMsg(login.this, "Peringatan", ""+data, helper.ERROR_TYPE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("DATA_JSONEXCEPION", "onResponse: "+e);

                    helper.showMsg(login.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();
                        Log.d("DATA_JSONEXCEPION", "onResponse: "+error);
                        helper.showMsg(login.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                    }
                }) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("CEK_LOGIN", "getParams: "+id_user+" cek :"+password+" macaddress: "+helper.getDeviceIMEI(login.this));
                params.put("users", id_user);
                params.put("pass", password);
                params.put("token_fcm", token);
                params.put("mc_address", helper.getDeviceId(login.this));
                params.put("key", api.key);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





    @Override
    public void onBackPressed() {
        new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Konfirmasi keluar")
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


    private void openWhatsApp(String numero,String mensaje){

        try{
            PackageManager packageManager = login.this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Toast.makeText(login.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            Toast.makeText(login.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
        }

    }


    private void getVersi(){
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tx_versi.setText("Copyright@IT PT Bangunan Jaya Mandiri \n (Qhomemart) V.0320"+"."+version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
