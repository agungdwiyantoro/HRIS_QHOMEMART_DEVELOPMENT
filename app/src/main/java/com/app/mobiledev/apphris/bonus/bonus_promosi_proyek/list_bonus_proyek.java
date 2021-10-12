package com.app.mobiledev.apphris.bonus.bonus_promosi_proyek;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class list_bonus_proyek extends AppCompatActivity {

    private RecyclerView rcBonusProyek;
    private TextView tgl1;
    private List<model_bonus_proyek> model_bonus_proyeks;
    private CoordinatorLayout coor_list_bonus;
    private SessionManager sessionmanager;
    private String kyano;
    private FloatingActionButton tambah_bonus_proyek;
    private Toolbar toolbar_list_bonus_proyek;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ImageButton btnCalender1,btnCari;
    private EditText edCari;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bonus_proyek);
        model_bonus_proyeks = new ArrayList<>();
        coor_list_bonus=findViewById(R.id.coor_list_bonus);
        tambah_bonus_proyek=findViewById(R.id.tambah_bonus_proyek);
        sessionmanager = new SessionManager(list_bonus_proyek.this);
        kyano = sessionmanager.getIdUser();
        rcBonusProyek=findViewById(R.id.rcBonusProyek);
        toolbar_list_bonus_proyek=findViewById(R.id.toolbar_list_bonus_proyek);
        btnCalender1=findViewById(R.id.btnCalender1);
        btnCari=findViewById(R.id.btnCari);
        edCari=findViewById(R.id.edCari);

        toolbar_list_bonus_proyek.setTitle("List Kunjungan Customer");
        tgl1=findViewById(R.id.tgl1);
        setSupportActionBar(toolbar_list_bonus_proyek);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dateFormatter = new SimpleDateFormat("yyyy-MM", Locale.US);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tgl1.setText(getDateNow());

        toolbar_list_bonus_proyek.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tambah_bonus_proyek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(list_bonus_proyek.this, form_bonus_proyek.class);
                intent.putExtra("kjnkd","");
                intent.putExtra("jenis_cust","");
                intent.putExtra("status","");
                intent.putExtra("ktg_proyek","");
                intent.putExtra("status_proyek","");
                startActivity(intent);
                finish();
            }
        });
        btnCalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog1();
            }
        });


        edCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String cari =edCari.getText().toString();
                    if(tgl1.equals(null)||tgl1.equals("")){
                        helper.snackBar(coor_list_bonus,"bulan belum dipilih");
                    }else{
                        String tahun=tgl1.getText().toString().substring(0, 4);
                        String bulan=tgl1.getText().toString().substring(5);
                        getBonusProyek(cari,bulan,tahun);
                    }

                }catch (NullPointerException e){
                    helper.snackBar(coor_list_bonus,"bulan belum dipilih");
                }

            }
        });
       setRiwayatBonusNow();
    }




    private void getBonusProyek(final String cari,final String bulan, final String tahun){
        AndroidNetworking.post(api.URL_getTrkunjungan)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("bulan", bulan)
                .addBodyParameter("tahun", tahun)
                .addBodyParameter("cari", cari)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        model_bonus_proyeks.clear();
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    model_bonus_proyek model = new model_bonus_proyek();
                                    model.setCust(data.getString("customer"));
                                    model.setNama_proyek(data.getString("nmProyek"));
                                    model.setTgl(data.getString("waktu"));
                                    model.setKode_kunjungan(data.getString("kode_kunjungan"));
                                    model.setStatus(data.getString("status"));
                                    model.setJenis(data.getString("jenis_cust"));
                                    model.setStatus_proyek(data.getString("status_proyek"));
                                    model_bonus_proyeks.add(model);
                                }
                                adapter_list_bonus_proyek mAdapter;
                                mAdapter = new adapter_list_bonus_proyek(model_bonus_proyeks, list_bonus_proyek.this);
                                rcBonusProyek.setLayoutManager(new LinearLayoutManager(list_bonus_proyek.this));
                                rcBonusProyek.setItemAnimator(new DefaultItemAnimator());
                                rcBonusProyek.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                            } else{
                                helper.snackBar(coor_list_bonus,"data belum ada...!!!");

                            }
                    } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORTRKUNJUNGAN1", "onResponse: "+e);
                            helper.snackBar(coor_list_bonus,""+helper.PESAN_SERVER);
                        }catch (NullPointerException e){
                            Log.d("JSONERORTRKUNJUNGAN2", "onResponse: "+e);
                            helper.snackBar(coor_list_bonus,""+helper.PESAN_SERVER);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                    }
                });

    }


    private void showDateDialog1(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(list_bonus_proyek.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tgl1.setText(""+dateFormatter.format(newDate.getTime()));
                if(tgl1.equals(null)||tgl1.equals("")){
                    helper.showMsg(list_bonus_proyek.this,"informasi","bulan belum dipilih",helper.ERROR_TYPE);
                }else{
                    String tahun=tgl1.getText().toString().substring(0, 4);
                    String bulan=tgl1.getText().toString().substring(5);
                    getBonusProyek("",bulan,tahun);


                }

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
        getBonusProyek("",bulan,tahun);
    }
}
