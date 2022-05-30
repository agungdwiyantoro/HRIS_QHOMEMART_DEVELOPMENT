package com.app.mobiledev.apphris;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.riwayat_absen.adapterRiwayatAbsenNew;
import com.app.mobiledev.apphris.riwayat_absen.modelRiwayatAbsen;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class fragment_riwayat_presensi_new extends Fragment {
    public fragment_riwayat_presensi_new() {
    }

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText tgl1;
    private EditText tgl2;
    private ImageButton btnCalender1;
    private ImageButton btnCalender2;
    private List<modelRiwayatAbsen> modelRiwayatAbsens;
    private SessionManager sessionmanager;
    private String kyano = "";
    private View rootView;
    private RecyclerView rcRiwayatAbsen;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_fragment_riwayat_presensi_new, container, false);
        btnCalender1 = rootView.findViewById(R.id.btnCalender1);
        btnCalender2 = rootView.findViewById(R.id.btnCalender2);
        tgl1 = rootView.findViewById(R.id.tgl1);
        tgl2 = rootView.findViewById(R.id.tgl2);

        tgl1.setInputType(InputType.TYPE_NULL);
        tgl2.setInputType(InputType.TYPE_NULL);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        sessionmanager = new SessionManager(getActivity());
        kyano = sessionmanager.getIdUser();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        AndroidNetworking.initialize(getActivity());
        modelRiwayatAbsens = new ArrayList<>();
        rcRiwayatAbsen = rootView.findViewById(R.id.rcRiwayatAbsen);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.show();
        getRiwayatabsenNow();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        // getMenu();
                    }
                }, 2000);
            }
        });


        btnCalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog1();
            }
        });

        btnCalender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog2();
            }
        });
        return rootView;
    }


    private void showDateDialog1() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tgl1.setText("" + dateFormatter.format(newDate.getTime()));
                if (tgl1.equals(null) || tgl1.equals("")) {
                    helper.showMsg(getActivity(), "informasi", "bulan selesai belum dipilih", helper.ERROR_TYPE);
                } else {
                    String tgl_mulai = tgl1.getText().toString();
                    String tgl_selesai = tgl2.getText().toString();
                    mProgressDialog.show();
                    riwayatAbsen(kyano, tgl_mulai, tgl_selesai);

                }
                mProgressDialog.dismiss();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showDateDialog2() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tgl2.setText("" + dateFormatter.format(newDate.getTime()));
                if (tgl2.equals(null) || tgl2.equals("")) {
                    helper.showMsg(getActivity(), "informasi", "bulan selesai belum dipilih", helper.ERROR_TYPE);
                } else {
                    String tgl_mulai = tgl1.getText().toString();
                    String tgl_selesai = tgl2.getText().toString();
                    mProgressDialog.show();
                    riwayatAbsen(kyano, tgl_mulai, tgl_selesai);
                }

                Log.d("SET_TGL2", "onDateSet: " + dateFormatter.format(newDate.getTime()));
                mProgressDialog.dismiss();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void riwayatAbsen(final String kyano, final String tgl_mulai, final String tgl_selesai) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        AndroidNetworking.initialize(getActivity(), okHttpClient);
        AndroidNetworking.post(api.URL_getRiwayatAbsen)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("tgl_mulai", tgl_mulai.replaceAll("-", ""))
                .addBodyParameter("tgl_selesai", tgl_selesai.replaceAll("-", ""))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelRiwayatAbsens.clear();
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelRiwayatAbsen model = new modelRiwayatAbsen();
                                    String kyano = data.getString("kyano");
                                    String kynm = data.getString("kynm");
                                    String kydivisi = data.getString("kydivisi");
                                    String FINGER = data.getString("FINGER");
                                    String tgl = data.getString("tgl");
                                    String masuk = data.getString("masuk");
                                    String pulang = data.getString("pulang");
                                    String mulai_istirahat = data.getString("selesai_istirahat");
                                    String selesai_istirahat = data.getString("mulai_istirahat");
                                    String masuk_lembur = data.getString("masuk_lembur");
                                    String keluar_lembur = data.getString("keluar_lembur");
                                    String status = data.getString("status");
                                    model.setKyano(kyano);
                                    model.setKynm(kynm);
                                    model.setTgl(tgl);
                                    model.setKydivisi(kydivisi);
                                    model.setFINGER(FINGER);
                                    model.setMasuk(masuk);
                                    model.setPulang(pulang);
                                    model.setMulai_istirahat(mulai_istirahat);
                                    model.setKembali_istirahat(selesai_istirahat);
                                    model.setMasuk_lembur(masuk_lembur);
                                    model.setKeluar_lembur(keluar_lembur);
                                    model.setStatus(status);
                                    modelRiwayatAbsens.add(model);
                                }
                                adapterRiwayatAbsenNew mAdapter;
                                mAdapter = new adapterRiwayatAbsenNew(modelRiwayatAbsens, getActivity());
                                mAdapter.notifyDataSetChanged();
                                rcRiwayatAbsen.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rcRiwayatAbsen.setItemAnimator(new DefaultItemAnimator());
                                rcRiwayatAbsen.setAdapter(mAdapter);
                            } else {
                                Log.d("DATA_BOOLEAN", "onResponse: " + success);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: " + e);
                            helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        } catch (NullPointerException e) {
                            Log.d("JSONERORABSEN", "onResponse: " + e);
                        } catch (NumberFormatException e) {
                            Log.d("JSONERORABSEN", "onResponse: " + e);
                        }

                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: " + anError);
                        mProgressDialog.dismiss();
                    }
                });
    }


    public String getMonthNow() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getDateNow() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }


    public void getRiwayatabsenNow() {
        int bulan = Integer.parseInt(getMonthNow());
        Log.d("CEK_BULAN", "getLastDateNow: " + bulan);
        Calendar gc = new GregorianCalendar();
        gc.set(Calendar.MONTH, bulan - 1);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = gc.getTime();
        gc.add(Calendar.MONTH, 1);
        gc.add(Calendar.DAY_OF_MONTH, -1);
        Date monthEnd = gc.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tgl1.setText(format.format(monthStart));
        tgl2.setText(getDateNow());
        riwayatAbsen(kyano, format.format(monthStart), getDateNow());
    }


}
