package com.app.mobiledev.apphris.profile.dataKeluarga;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class DataKeluarga extends AppCompatActivity {

    FloatingActionButton fab;
    LayoutInflater inflater;
    EditText etNama, etHubungan, etHp;
    TextView txt_hasil, txtSelesaiAdd;
    String nama, hubungan, noHp,kyano;

    TextInputLayout tIlNama, tIlSpHub, tIlNoHp;

    Spinner spinner;

    Dialog dialogHubungan;
    //View dialogViewHubungan;

    ArrayList<String> listHubungan;

    AdapterDataKeluarga mAdapter;

    Button btnTambah;

    Boolean statusTambah = false;

    private List<ModelDataKeluarga> modelDataKeluargas;
    private RecyclerView rvDataKeluarga;
    private SessionManager mSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_keluarga);
        fab = findViewById(R.id.fabTambahKeluarga);
        modelDataKeluargas = new ArrayList<>();
        rvDataKeluarga = findViewById(R.id.rvDataKeluarga);
        mSession=new SessionManager(this);
        kyano=mSession.getIdUser();
        spinner = findViewById(R.id.spHubungan);
        getDataKeluarga();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHubSpin();
            }
        });

    }

    public void getDataKeluarga() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        AndroidNetworking.initialize(DataKeluarga.this, okHttpClient);
        AndroidNetworking.post(api.URL_getHubungan_keluarga)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelDataKeluargas.clear();
                        try {
                            Boolean success = response.getBoolean("success");
                            if(success){
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    ModelDataKeluarga model = new ModelDataKeluarga();

                                    String id = data.getString("id");
                                    String nama = data.getString("nama");
                                    String hubungan = data.getString("hubungan");
                                    String noHp = data.getString("hp");

                                    Log.d("TAG_CEK_KEL", "onResponse: " + id +" || "+nama);

                                    model.setId(id);
                                    model.setNama(nama);
                                    model.setHubungan(hubungan);
                                    model.setNoHp(noHp);

                                    modelDataKeluargas.add(model);
                                }

                                mAdapter = new AdapterDataKeluarga(DataKeluarga.this, modelDataKeluargas);
                                mAdapter.notifyDataSetChanged();
                                rvDataKeluarga.setLayoutManager(new LinearLayoutManager(DataKeluarga.this));
                                rvDataKeluarga.setItemAnimator(new DefaultItemAnimator());
                                rvDataKeluarga.setAdapter(mAdapter);
                            }
                            else{
                                helper.messageToast(DataKeluarga.this,"data masih kosong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_Exception", "onResponse: " + e);
                            helper.showMsg(DataKeluarga.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
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
                        helper.showMsg(DataKeluarga.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("ANERROR_EXCEPTION", "onError: " + anError);
                        //progressDialog.dismiss();
                    }
                });
    }

    private boolean postDataKeluarga(String nama, String hubungan, String noHp) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        AndroidNetworking.initialize(DataKeluarga.this, okHttpClient);
        AndroidNetworking.post(api.URL_insertHubunganKeluarga)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("nama", /*"test"*/nama)
                .addBodyParameter("hubungan", /*"Ibu"*/hubungan)
                .addBodyParameter("hp", /*"614596541"*/noHp)
                .addBodyParameter("status", "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            boolean status = response.getBoolean("status");
                            String keterangan = response.getString("ket");
                            if (status) {
                                onResume();
                                getDataKeluarga();
                                statusTambah = true;
                                Toast.makeText(DataKeluarga.this,"1. "+keterangan, Toast.LENGTH_LONG).show();
                            } else {
                                statusTambah = false;
                                Toast.makeText(DataKeluarga.this,"2. "+keterangan, Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_Exception", "onResponse: " + e);
                            helper.showMsg(DataKeluarga.this, "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
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
                        helper.showMsg(DataKeluarga.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("ANERROR_EXCEPTION", "onError: " + anError);
                        //progressDialog.dismiss();
                    }
                });

        return statusTambah;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataKeluarga();
    }

    public void getHubSpin() {
        dialogHubungan = new Dialog(DataKeluarga.this);
        dialogHubungan.setContentView(R.layout.form_tambah_keluarga);
        //inflater = getLayoutInflater();

        //dialogViewHubungan = inflater.inflate(R.layout.form_tambah_keluarga, null);

        dialogHubungan.setCancelable(true);
        /*dialogHubungan.setC*/

        //dialogHubungan.setIcon(R.drawable.ic_baseline_supervised_user_circle_24);
        dialogHubungan.setTitle("Tambah Data Keluarga");

        tIlNama = (TextInputLayout) dialogHubungan.findViewById(R.id.tIlNamaAdd);
        tIlSpHub = (TextInputLayout) dialogHubungan.findViewById(R.id.tIlspHubungan);
        tIlNoHp = (TextInputLayout) dialogHubungan.findViewById(R.id.tIlNoHP);

        etNama = (EditText) dialogHubungan.findViewById(R.id.etNamaAdd);
        etHp = (EditText) dialogHubungan.findViewById(R.id.etNoHpAdd);

        txtSelesaiAdd = (TextView) dialogHubungan.findViewById(R.id.txtSelesaiAdd);

        btnTambah = (Button) dialogHubungan.findViewById(R.id.btnSubmit);

        txtSelesaiAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHubungan.dismiss();
            }
        });

        final StringRequest request = new StringRequest(Request.Method.POST, api.URL_getStatus_keluarga,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            listHubungan = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objectData = jsonArray.getJSONObject(i);

                                    String hubungan = objectData.getString("nama_hubungan");

                                    listHubungan.add(hubungan);
                                    Log.d("COMP_NAME", "iniData: " + hubungan);
                                }

                            }

                            spinner = dialogHubungan.findViewById(R.id.spHubungan);
                            spinner.setAdapter(new ArrayAdapter<String>(DataKeluarga.this, android.R.layout.simple_spinner_dropdown_item, listHubungan));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("COMP_NAME", "iniData: " + e);
                            helper.showMsg(DataKeluarga.this, "Peringatan", "Error login : Json " + e, helper.ERROR_TYPE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DataKeluarga.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("key", "suksesmandiri96");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_validation();

                nama = etNama.getText().toString();
                hubungan = spinner.getSelectedItem().toString();
                noHp = etHp.getText().toString();

                if (is_valid()) {

                    if (postDataKeluarga(nama, hubungan, noHp)) {
                        dialogHubungan.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }

                } else {
                    //AlertDialog dialog = (AlertDialog) getDialog();
                    //dialogViewHubungan.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }
            }
        });

        /*dialogHubungan.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogHubungan.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                reset_validation();

                nama = etNama.getText().toString();
                hubungan = spinner.getSelectedItem().toString();
                noHp = etHp.getText().toString();

                if (is_valid()) {
                    postDataKeluarga(nama, hubungan, noHp);
                } else {
                    //AlertDialog dialog = (AlertDialog) getDialog();
                    //dialogViewHubungan.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }


            }
        });*/

        dialogHubungan.show();

    }

    private boolean is_valid() {
        boolean valid = true;
        if (nama.isEmpty()) {
            tIlNama.setError("Nama belum diisi");
            tIlNama.requestFocus();
            dialogHubungan.setCancelable(false);
            valid = false;
            //Toast.makeText(DataKeluarga.this, "Nama belum diisi", Toast.LENGTH_SHORT).show();
        } else if (hubungan.equals("Pilih hubungan")){
            tIlSpHub.setError("Hubungan belum dipilih");
            tIlSpHub.requestFocus();
            dialogHubungan.setCancelable(false);
            valid = false;
            //Toast.makeText(DataKeluarga.this, "Hubungan belum dipilih", Toast.LENGTH_SHORT).show();
        }
        Log.d("CEK_nama_hub_hp", "onClick: " + nama +", "+ hubungan +", "+ noHp);
        return valid;
    }

    private void reset_validation() {
        tIlNama.setErrorEnabled(false);
        tIlSpHub.setErrorEnabled(false);
        //tIlNoHp.setErrorEnabled(false);

    }

}