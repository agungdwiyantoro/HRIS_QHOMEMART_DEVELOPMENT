package com.app.mobiledev.apphris.izin.izinCuti;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class formIzinCuti extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText etTglCuti, etKetCuti, etLamaCuti;
    private ImageView ivTgl, imStatus;
    private TextView tx_image_name, txClose, tvGetDate, tvNamaEmp,tvDivisiEmp, tvJabatanEmp, tvSisaCuti, tvPeriode, tvHakCuti;
    private TextInputLayout tilTglCuti, tilLamaCuti;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llViewCalendar, llViewGetDates;

    private Button btn_ajukan, btnDate, btnCancelDate, dialogBtnSubmit, dialogBtnCancel;
    private String kyano, token, lamaCuti = "", ketCuti = "", tglCuti = "", kuotaCuti = "", hakCuti = "", namaEmp, divisiEmp, jabatanEmp, spinJenisSelected, spinDelegasiSelected, spinResultJenis, spinResultDelegasi;
    private Toast toast;
    Spinner spinJenis, spinDelegasi;

    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> jenisList, delegasiList;

    SessionManager session;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_cuti);

        /*
        * GET DATA SHAREPREF START
        * */

        session = new SessionManager(formIzinCuti.this);
        helper.verifyStoragePermissions(formIzinCuti.this);
        kyano = session.getIdUser();
        token = /*session.getToken()*/"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjA1MjMxOTA0MjIxMDc5NSIsImt5cGFzc3dvcmQiOiIxMjM0NTY3Iiwia3lqYWJhdGFuIjoiSFIxNDciLCJreWRpdmlzaSI6IkhSMDA0Iiwia3liYWdpYW4iOiJCRzA0NiIsImphYmF0YW4iOiJudWxsIiwiaWF0IjoxNjUyMTY3MzgzLCJleHAiOjE2NTIxODUzODN9.Vax2borGc18EggAO4UzYvzS55PM7fFqQdks6zc1Iyts";

        tvNamaEmp = findViewById(R.id.tvNamaEmp);
        tvDivisiEmp = findViewById(R.id.tvDivisiEmp);
        tvJabatanEmp = findViewById(R.id.tvJabatanEmp);

        namaEmp = session.getNamaLEngkap();
        divisiEmp = session.getDivisi();
        jabatanEmp = session.getJabatan();

        tvNamaEmp.setText(namaEmp);
        tvDivisiEmp.setText(divisiEmp);
        tvJabatanEmp.setText(jabatanEmp);

        /*
         * GET DATA SHAREPREF END
         * */

        tvHakCuti = findViewById(R.id.tvHakCuti);
        tvSisaCuti = findViewById(R.id.tvSisaCuti);
        tvPeriode = findViewById(R.id.tvPeriode);

        etTglCuti = findViewById(R.id.etTglCuti);
        etLamaCuti = findViewById(R.id.etLamaCuti);
        etKetCuti = findViewById(R.id.etKetCuti);

        tilLamaCuti = findViewById(R.id.tilLamaCuti);
        tilTglCuti = findViewById(R.id.tilTglCuti);

        lin_transparant = findViewById(R.id.lin_transparant);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        ivTgl = findViewById(R.id.ivTgl);
        btn_ajukan = findViewById(R.id.btn_ajukan);

        //get the spinner from the xml.
        spinJenis = findViewById(R.id.spinJenisCuti);
        spinDelegasi = findViewById(R.id.spinDelegasi);

        String[] itemJenis = new String[]{"Cuti Tahunan", "Cuti Pernikahan", "Cuti Istri Melahirkan"};
        String[] itemDelegasi = new String[]{"Mas'ud", "Anwar", "Septi"};

        ArrayAdapter<String> adapterJenis = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemJenis);
        adapterJenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterDelegasi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemDelegasi);
        adapterDelegasi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinJenis.setAdapter(adapterJenis);
        //spinDelegasi.setAdapter(adapterDelegasi);

        /*
         * START OPEN MULTIPLE CALENDAR
         * */

        llViewCalendar = findViewById(R.id.llViewCalendar);
        llViewGetDates = findViewById(R.id.llViewGetDates);

        tvGetDate = findViewById(R.id.tvGetDate);

        btnDate = findViewById(R.id.btnGetDates);
        btnCancelDate = findViewById(R.id.btnCancelDate);

        Calendar nextMonth = Calendar.getInstance();

        nextMonth.add(Calendar.MONTH, +2);

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextMonth.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSelected(Date date) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                Toast.makeText(formIzinCuti.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.add(clearFormatDate);

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateUnselected(Date date) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                Toast.makeText(formIzinCuti.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.remove(clearFormatDate);
            }
        });

        btnCancelDate.setOnClickListener(v -> {

            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
        });

        btnDate.setOnClickListener(v -> {
            etTglCuti.setText(list.toString().replace("[", "").replace("]", ""));
            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
            Log.d("TAG_DATE_ARRAY", "onCreate: " + list.toString().replace("[", "").replace("]", ""));
            Log.d("TAG_LIST_LENGTH", "onCreate: "+list.size());
            etLamaCuti.setText(String.valueOf(list.size()));
        });

        /*
         * END OPEN MULTIPLE CALENDAR
         * */

        Calendar calendarYear = Calendar.getInstance();
        int year = calendarYear.get(Calendar.YEAR);
        getKuotaCuti(year);
        tvPeriode.setText(""+year);

        getJenisCuti();
        getDelegasi();
        getHakCuti();

        spinJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinJenisSelected = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                /*switch (spinJenisSelected) {
                    case "Menunggu":
                        spinResultJenis = "";
                        break;
                    case "Diterima":
                        spinResultJenis = "1";
                        break;
                    case "Ditolak":
                        spinResultJenis = "0";
                        break;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDelegasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinDelegasiSelected = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                /*switch (spinDelegasiSelected) {
                    case "Menunggu":
                        spinResultDelegasi = "";
                        break;
                    case "Diterima":
                        spinResultDelegasi = "1";
                        break;
                    case "Ditolak":
                        spinResultDelegasi = "0";
                        break;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ivTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar.setVisibility(View.VISIBLE);
                llViewCalendar.setVisibility(View.VISIBLE);
                llViewGetDates.setVisibility(View.VISIBLE);
            }
        });

        btn_ajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekInputFormInsert();
            }
        });

    }

    private void getJenisCuti() {
        AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/jenis_cuti")
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            jenisList = new ArrayList<>();
                            //JSONObject response = new JSONObject();
                            if (response.getString("status").equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("message");
                                for (int i = 1; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String seri = data.getString("nmcuti");
                                    if (seri.length()<40) {
                                        jenisList.add(i+"."+seri);
                                    } else {
                                        jenisList.add(i+"."+seri+"...");
                                    }
                                    Log.d("LIST_JENIS", "onResponse: "+seri);
                                }
                            }
                            spinJenis.setAdapter(new ArrayAdapter<String>(formIzinCuti.this, android.R.layout.simple_spinner_dropdown_item, jenisList));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONExceptionSeri", "onResponse: " + e);
                            //showMsg(UpdateStockOpname.this, "pesan", "" + e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getDelegasi() {
        AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/list_delegasi")
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            delegasiList = new ArrayList<>();
                            //JSONObject response = new JSONObject();
                            if (response.getString("status").equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String seri = data.getString("kynm");
                                    delegasiList.add(seri);
                                    Log.d("LIST_DELEGASI", "onResponse: "+seri);
                                }
                            }
                            spinDelegasi.setAdapter(new ArrayAdapter<String>(formIzinCuti.this, android.R.layout.simple_spinner_dropdown_item, delegasiList));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONExceptionSeri", "onResponse: " + e);
                            //showMsg(UpdateStockOpname.this, "pesan", "" + e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getKuotaCuti(int periode) {
        AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/kuota_cuti?periode="+periode)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG_GET_KUOTA", "onResponse: "+response);
                        try {
                            JSONObject object = response.getJSONObject("message");
                            kuotaCuti = object.getString("sisa_cuti");
                            tvSisaCuti.setText(kuotaCuti);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void getHakCuti() {
        AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/kuota_cuti")
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG_GET_KUOTA", "onResponse: "+response);
                        try {
                            JSONObject object = response.getJSONObject("message");
                            hakCuti = object.getString("hak_cuti");
                            tvHakCuti.setText(hakCuti+" Hari");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private void cekInputFormInsert() {

        tglCuti = etTglCuti.getText().toString();
        lamaCuti = etLamaCuti.getText().toString();

        if (tglCuti.isEmpty()) {
            tilTglCuti.setError("Tanggal Cuti masih kosong");
            tilTglCuti.requestFocus();
        } else {
            dialog_confirm();
        }

    }

    private void notifDialogSukses() {
        dialogResign = new Dialog(formIzinCuti.this);
        dialogResign.setContentView(R.layout.dialog_sukses);
        dialogResign.setCancelable(true);
        dialogResign.setCanceledOnTouchOutside(false);
        txClose = (TextView) dialogResign.findViewById(R.id.txClose);
        txClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogResign.dismiss();
                finish();
            }
        });
        dialogResign.show();
    }

    private void dialog_confirm() {
        dialogConfirm = new Dialog(formIzinCuti.this);
        dialogConfirm.setContentView(R.layout.dialog_confirm);

        dialogBtnSubmit = dialogConfirm.findViewById(R.id.btnSubmit);
        dialogBtnCancel = dialogConfirm.findViewById(R.id.btnCancel);

        dialogConfirm.setCancelable(true);
        dialogConfirm.setCanceledOnTouchOutside(false);

        dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: "+spinJenisSelected.substring(0,1)+" | "+ tglCuti+" | "+lamaCuti+" | "+ spinDelegasiSelected +" | "+tvPeriode.getText().toString()+" | "+etKetCuti.getText().toString());
                insertCuti(spinJenisSelected.substring(0,1),tglCuti,spinDelegasiSelected,etKetCuti.getText().toString(),tvPeriode.getText().toString());

            }
        });

        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm.dismiss();

            }
        });

        dialogConfirm.show();
    }

    /*private void insertCuti(String id_jenis, String select_cuti, String delegasi_to, String alasan, String periode) {
        Log.d("TAG_INPUT_CEK", "insertIzinSakit: "+ id_jenis + select_cuti + delegasi_to + alasan + periode);
        AndroidNetworking.post("http://192.168.50.24/all/hris_ci_3/api/izinsakit")
                .addHeaders("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjA2NTIyMDA1MTMwNTEyOTYiLCJreXBhc3N3b3JkIjoiMTIzNDU2NyIsImt5amFiYXRhbiI6IkhSMTQ3Iiwia3lkaXZpc2kiOiJIUjAwNCIsImt5YmFnaWFuIjoiQkcwNDYiLCJqYWJhdGFuIjoibnVsbCIsImlhdCI6MTY1MjE0NzEwMywiZXhwIjoxNjUyMTY1MTAzfQ.DHPQJxio_iNxD3uOoIWXDR3z7l6-e87LSqzOSeUKu5s"+ token)
                .addHeaders("Content-Type", "application/json")
                .addQueryParameter("id_jenis", id_jenis)
                .addQueryParameter("select_cuti", select_cuti)
                .addQueryParameter("delegasi_to", delegasi_to)
                .addQueryParameter("alasan", alasan)
                .addQueryParameter("periode", periode)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //Log.d("TAG_UPLOAD_PROGRESS", "onProgress: "+bytesUploaded);
                        Log.d("TAG_UPLOAD_TOTAL", "onProgress: "+totalBytes);
                        lin_transparant.setVisibility(View.VISIBLE);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lin_transparant.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            Log.d("RESULT_RESPONSE_INSERT", "onResponse: " + response);
                            String message = response.getString("message");
                            if (status.equals("200")) {
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinCuti.this, "" + message, toast.LENGTH_SHORT).show();
                            } else if(status.equals("201")){
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinCuti.this, "" + message, toast.LENGTH_SHORT).show();
                            } else {
                                message = response.getString("message");
                                Toast.makeText(formIzinCuti.this, "" + message, toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            lin_transparant.setVisibility(View.GONE);
                            Log.d("JSON_insert", "onResponse: " + e);
                        }
                    }



                    @Override
                    public void onError(ANError anError) {
                        lin_transparant.setVisibility(View.GONE);
                        Log.d("ERROR_INSERT_CODE", "onError: " + anError.getErrorCode());
                        Log.d("ERROR_INSERT_DETAIL", "onError: " + anError.getErrorDetail());
                        Log.d("ERROR_INSERT_RESPONSE", "onError: " + anError.getResponse());
                    }
                });

    }*/

    private void insertCuti(String id_jenis, String select_cuti, String delegasi_to, String alasan, String periode) {
        Log.d("TAG_INPUT_CEK", "insertIzinCuti: "+ id_jenis + " | " + select_cuti + " | " + delegasi_to + " | " + alasan + " | " + periode);
        AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izincuti")
                .addHeaders("Authorization", "Bearer " + token)
                //.addHeaders("Content-Type", "application/json")
                //.addHeaders("Content-Type", "multipart/form-data")
                .addMultipartParameter("id_jenis", /*"1"*/id_jenis)
                .addMultipartParameter("select_cuti", /*"2022-06-17, 2022-06-16"*/select_cuti)
                .addMultipartParameter("delegasi_to", /*"ANWARUL MUSLIMIN"*/delegasi_to)
                .addMultipartParameter("alasan", /*"test alasan"*/alasan)
                .addMultipartParameter("periode", /*"2022"*/periode)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lin_transparant.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            Log.d("RESULT_RESPONSE_INSERT", "onResponse: " + response.toString());
                            String message = response.getString("message");
                            if (status.equals("200")) {
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinCuti.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                message = response.getString("message");
                                Toast.makeText(formIzinCuti.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            lin_transparant.setVisibility(View.GONE);
                            Log.d("JSON_insert", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        lin_transparant.setVisibility(View.GONE);
                        Log.d("ERROR_INSERT_CODE", "onError: " + anError.getErrorCode());
                        Log.d("ERROR_INSERT_DETAIL", "onError: " + anError.getErrorDetail());
                        Log.d("ERROR_INSERT_RESPONSE", "onError: " + anError.getResponse());
                    }
                });

    }

}
