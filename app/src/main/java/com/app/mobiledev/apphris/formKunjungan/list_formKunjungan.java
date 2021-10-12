package com.app.mobiledev.apphris.formKunjungan;
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
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

public class list_formKunjungan extends AppCompatActivity {

    private FloatingActionButton tambah_form;
    private List<modelFromKunjungan> modelpromages;
    private RecyclerView rcKunjungan;
    private TextView tgl1;
    private SimpleDateFormat dateFormatter;
    private String TAG="proManage";
    private DatePickerDialog datePickerDialog;
    private ImageButton btnCalender1;
    private Toolbar toolbar_abs;
    private ProgressBar loading_spinner;
    private String kyano;
    private SessionManager sessionmanager;
    private CoordinatorLayout lmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_manage);
        modelpromages = new ArrayList<>();
        rcKunjungan=findViewById(R.id.rcKunjungan);
        tgl1=findViewById(R.id.tgl1);
        btnCalender1=findViewById(R.id.btnCalender1);
        tambah_form=findViewById(R.id.tambah_form);
        toolbar_abs=findViewById(R.id.toolbar_abs);
        toolbar_abs.setTitle("List Kunjungan");
        lmenu=findViewById(R.id.lmenu);
        loading_spinner=findViewById(R.id.loading_spinner);
        loading_spinner.setVisibility(View.VISIBLE);
        dateFormatter = new SimpleDateFormat("yyyy-MM", Locale.US);
        sessionmanager = new SessionManager(list_formKunjungan.this);
        kyano=sessionmanager.getIdUser();
        setSupportActionBar(toolbar_abs);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setRiwayatKunjungan();
        toolbar_abs.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tambah_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(list_formKunjungan.this, form_kunjungan.class);
                intent.putExtra("idform","");
                intent.putExtra("nmproyek","");
                intent.putExtra("alamtproyek","");
                intent.putExtra("ownerProyek","");
                intent.putExtra("penanggungJwb","");
                intent.putExtra("noTelp","");
                intent.putExtra("thpProyek","");
                intent.putExtra("jam","");
                intent.putExtra("tgl","");
                intent.putExtra("lokasi","");
                intent.putExtra("jenis","");
                intent.putExtra("kyano","");
                intent.putExtra("image","");
                intent.putExtra("status","");
                v.getContext().startActivity(intent);
                finish();
            }
        });
        btnCalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog1();
            }
        });

    }

    private void riwayatProManage(final String kyano,final String bulan, final String tahun){
        AndroidNetworking.post(api.URL_getHistoriFormKunjungan)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("bulan", bulan)
                .addBodyParameter("tahun", tahun)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelpromages.clear();
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelFromKunjungan model = new modelFromKunjungan();
                                    model.setIdForm(data.getString("idform"));
                                    model.setAlamtProyek(data.getString("alamtProyek"));
                                    model.setImage(data.getString("image"));
                                    model.setJam(data.getString("jam"));
                                    model.setJenis(data.getString("jenis"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setLokasi(data.getString("lokasi"));
                                    model.setNmProyek(data.getString("nmProyek"));
                                    model.setNoTelp(data.getString("noTelp"));
                                    model.setOwnerProyek(data.getString("ownerProyek"));
                                    model.setpJawab(data.getString("penanggungJwb"));
                                    model.setTgl(data.getString("tgl"));
                                    model.setStatus(data.getString("status"));
                                    model.setThpProyek(data.getString("thpProyek"));
                                    modelpromages.add(model);

                                }

                                adapterFromKunjungan mAdapter;
                                mAdapter = new adapterFromKunjungan(modelpromages, list_formKunjungan.this);
                                mAdapter.notifyDataSetChanged();
                                rcKunjungan.setLayoutManager(new LinearLayoutManager(list_formKunjungan.this));
                                rcKunjungan.setItemAnimator(new DefaultItemAnimator());
                                rcKunjungan.setAdapter(mAdapter);



                            }else{
                                Log.d("DATA_BOOLEAN_pro", "onResponse: "+success);
                            }
                            loading_spinner.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG+"_JASON", "onResponse: "+e);
                            loading_spinner.setVisibility(View.GONE);
                        }catch (NullPointerException e){
                            Log.d(TAG+"_NULL", "onResponse: "+e);
                            loading_spinner.setVisibility(View.GONE);
                        }catch (NumberFormatException e){
                            Log.d(TAG+"_EXCEPTION", "onResponse: "+e);
                            loading_spinner.setVisibility(View.GONE);
                        }

                    }
                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(list_formKunjungan.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION_riwayat", "onError: "+anError);
                        loading_spinner.setVisibility(View.GONE);
                    }
                });
    }

    private void showDateDialog1(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(list_formKunjungan.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tgl1.setText(""+dateFormatter.format(newDate.getTime()));
                String tahun=tgl1.getText().toString().substring(0, 4);
                String bulan=tgl1.getText().toString().substring(5);
                loading_spinner.setVisibility(View.VISIBLE);
                riwayatProManage(kyano,bulan,tahun);

                Log.d("SET_TGL1", "onDateSet: "+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private void setRiwayatKunjungan(){
        tgl1.setText(getDateNow());
        String tahun=tgl1.getText().toString().substring(0, 4);
        String bulan=tgl1.getText().toString().substring(5);
        riwayatProManage(kyano,bulan,tahun);
    }

    public String getDateNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        String formattedDate = df.format(c);
        return  formattedDate;
    }


}
