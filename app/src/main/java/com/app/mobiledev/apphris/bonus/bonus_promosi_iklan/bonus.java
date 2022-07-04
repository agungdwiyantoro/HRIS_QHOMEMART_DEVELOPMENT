package com.app.mobiledev.apphris.bonus.bonus_promosi_iklan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class bonus extends AppCompatActivity {
    private List<model_bonus> modelBonus;
    private RecyclerView rcBonus;
    private ProgressDialog mProgressDialog;
    private SessionManager sessionmanager;
    private String kyano;
    private EditText tgl1;
    private ImageButton btnCalender1;
    private Button btnOpen;
    private AlertDialog.Builder dialog;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private FloatingActionButton tambah;
    private CoordinatorLayout lmenu;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        rcBonus=findViewById(R.id.rcBonus);
        tgl1=findViewById(R.id.tgl1);
        tambah=findViewById(R.id.tambah_bonus);
        btnOpen=findViewById(R.id.btnOpen);
        lmenu=findViewById(R.id.lmenu);
        btnCalender1=findViewById(R.id.btnCalender1);
        modelBonus = new ArrayList<>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        dialog = new AlertDialog.Builder(bonus.this);
        sessionmanager = new SessionManager(bonus.this);
        kyano=sessionmanager.getIdUser();
        dateFormatter = new SimpleDateFormat("yyyy-MM", Locale.US);
        tgl1.setInputType(InputType.TYPE_NULL);
        setRiwayatBonusNow();

        mToolbar = findViewById(R.id.toolbar_abs);
        mToolbar.setTitle("Bonusan");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mProgressDialog.show();
                    if(tgl1.equals(null)||tgl1.equals("")){
                        helper.showMsg(bonus.this,"informasi","bulan belum dipilih",helper.ERROR_TYPE);
                    }else{
                        String tahun=tgl1.getText().toString().substring(0, 4);
                        String bulan=tgl1.getText().toString().substring(5);
                        getBonus(bulan,tahun);


                    }

                }catch (NullPointerException e){
                    helper.showMsg(bonus.this,"informasi2","bulan belum dipilih",helper.ERROR_TYPE);
                    mProgressDialog.dismiss();
                }
            }
        });


        btnCalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog1();
            }
        });



        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bonus.this, add_bonus.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void getBonus(final String bulan,final String tahun){
        AndroidNetworking.post(api.URL_getBonus)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("bulan", bulan)
                .addBodyParameter("tahun", tahun)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelBonus.clear();
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    model_bonus model = new model_bonus();
                                    String id_hastag=data.getString("id_hastag");
                                    String kyano=data.getString("kyano");
                                    String image=data.getString("image");
                                    String ket=data.getString("ket");
                                    String tgl=data.getString("tgl");
                                    String status=data.getString("status");

                                    model.setId_hastag(id_hastag);
                                    model.setKyano(kyano);
                                    model.setImage(image);
                                    model.setTgl(tgl);
                                    model.setKet(ket);
                                    model.setStatus(status);
                                    modelBonus.add(model);


                                }

                                adapterBonus mAdapter;
                                mAdapter = new adapterBonus(modelBonus, bonus.this);
                                rcBonus.setLayoutManager(new LinearLayoutManager(bonus.this));
                                rcBonus.setItemAnimator(new DefaultItemAnimator());
                                rcBonus.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();




                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                                helper.snackBar(lmenu,"riwayat bonus tidak ditemukan");

                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(bonus.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }catch (NullPointerException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(bonus.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });
    }

    private void showDateDialog1(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(bonus.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tgl1.setText(""+dateFormatter.format(newDate.getTime()));
                Log.d("SET_TGL1", "onDateSet: "+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public String getDateNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String formattedDate = df.format(c);
        return  formattedDate;
    }


    private void setRiwayatBonusNow(){
        tgl1.setText(getDateNow());
        String tahun=tgl1.getText().toString().substring(0, 4);
        String bulan=tgl1.getText().toString().substring(5);
        getBonus(bulan,tahun);
    }


}
