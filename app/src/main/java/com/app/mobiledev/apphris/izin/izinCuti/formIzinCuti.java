package com.app.mobiledev.apphris.izin.izinCuti;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.google.common.base.CharMatcher;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class formIzinCuti extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText etTglCuti, etKetCuti, etLamaCuti;
    private ImageView ivTgl, imStatus;
    private TextView tx_image_name, txClose, tvGetDate, tvNamaEmp,tvDivisiEmp, tvJabatanEmp, tvSisaCuti, tvPeriode, tvHakCuti;
    private TextInputLayout tilTglCuti, tilLamaCuti, tilKetCuti;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llViewCalendar, llViewGetDates, linearLayout;

    private Button btn_ajukan, btnDate, btnCancelDate, dialogBtnSubmit, dialogBtnCancel;
    private String kyano, token, lamaCuti = "", ketCuti = "", tglCuti = "", kuotaCuti = "", hakCuti = "", namaEmp, divisiEmp, jabatanEmp, spinJenisSelected, spinDelegasiSelected, spinResultJenis, spinResultDelegasi;
    private Toast toast;
    Spinner spinJenis, spinDelegasi;
    Snackbar snackbar;

    String jenis1, jenis2, jenis3, jenis4, jenis5, jenis6, jenis7, jenis8, jenis9, jenis10;

    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> jenisList, delegasiList;

    CalendarPickerView calendar;

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

        linearLayout = findViewById(R.id.linearLayout);

        session = new SessionManager(formIzinCuti.this);
        helper.verifyStoragePermissions(formIzinCuti.this);
        kyano = session.getIdUser();
        token = session.getToken();

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
        tilKetCuti = findViewById(R.id.tilKetCuti);
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

        nextMonth.add(Calendar.MONTH, +4);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
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

                //Toast.makeText(formIzinCuti.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.add(clearFormatDate);

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateUnselected(Date date) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                //Toast.makeText(formIzinCuti.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.remove(clearFormatDate);
            }
        });

        btnCancelDate.setOnClickListener(v -> {

            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
        });

        btnDate.setOnClickListener(v -> {

            Log.d("TAG_SPIN_SELECTED", "onCreate: "+spinJenisSelected);

            if (spinJenisSelected.substring(0,2).equals("1.")) {
                if (list.size() > Integer.parseInt(jenis1)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }

            } else if (spinJenisSelected.substring(0,2).equals("2.")) {
                if (list.size() > Integer.parseInt(jenis2)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("3.")) {
                if (list.size() > Integer.parseInt(jenis3)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("4.")) {
                if (list.size() > Integer.parseInt(jenis4)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("5.")) {
                if (list.size() > Integer.parseInt(jenis5)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("6.")) {
                if (list.size() > Integer.parseInt(jenis6)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("7.")) {
                if (list.size() > Integer.parseInt(jenis7)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("8.")) {
                if (list.size() > Integer.parseInt(jenis8)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("9.")) {
                if (list.size() > Integer.parseInt(jenis9)) {
                    showToast("Tanggal melebihi batas cuti");
                } else {
                    getAfterCheckDateSelected();
                }
            } else if (spinJenisSelected.substring(0,2).equals("10")) {
                if (list.size() > Integer.parseInt(kuotaCuti)) {
                    showToast("Tanggal melebihi batas Cuti Tahunan");
                } else {
                    getAfterCheckDateSelected();
                }
            }

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

    private void getAfterCheckDateSelected() {
        etTglCuti.setText(list.toString().replace("[", "").replace("]", ""));
        calendar.setVisibility(View.GONE);
        llViewGetDates.setVisibility(View.GONE);
        Log.d("TAG_DATE_ARRAY", "onCreate: " + list.toString().replace("[", "").replace("]", ""));
        Log.d("TAG_LIST_LENGTH", "onCreate: "+list.size());
        etLamaCuti.setText(String.valueOf(list.size()));
    }

    private void showToast(String text) {
        Toast.makeText(formIzinCuti.this, text, Toast.LENGTH_SHORT).show();
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
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String seri = data.getString("nmcuti");

                                    JSONObject dataJenis1 = jsonArray.getJSONObject(0);
                                    jenis1 = dataJenis1.getString("hrcuti");
                                    JSONObject dataJenis2 = jsonArray.getJSONObject(1);
                                    jenis2 = dataJenis2.getString("hrcuti");
                                    JSONObject dataJenis3 = jsonArray.getJSONObject(2);
                                    jenis3 = dataJenis3.getString("hrcuti");
                                    JSONObject dataJenis4 = jsonArray.getJSONObject(3);
                                    jenis4 = dataJenis4.getString("hrcuti");
                                    JSONObject dataJenis5 = jsonArray.getJSONObject(4);
                                    jenis5 = dataJenis5.getString("hrcuti");
                                    JSONObject dataJenis6 = jsonArray.getJSONObject(5);
                                    jenis6 = dataJenis6.getString("hrcuti");
                                    JSONObject dataJenis7 = jsonArray.getJSONObject(6);
                                    jenis7 = dataJenis7.getString("hrcuti");
                                    JSONObject dataJenis8 = jsonArray.getJSONObject(7);
                                    jenis8 = dataJenis8.getString("hrcuti");
                                    JSONObject dataJenis9 = jsonArray.getJSONObject(8);
                                    jenis9 = dataJenis9.getString("hrcuti");
                                    JSONObject dataJenis10 = jsonArray.getJSONObject(9);
                                    jenis10 = dataJenis10.getString("hrcuti");


                                    if (seri.length()<40) {
                                        jenisList.add((i+1)+"."+seri);
                                    } else {
                                        jenisList.add((i+1)+"."+seri+"...");
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
                            tvSisaCuti.setText(kuotaCuti+" Hari");
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
        ketCuti = etKetCuti.getText().toString();

        if (tglCuti.isEmpty()) {
            tilTglCuti.setError("Tanggal Cuti masih kosong");
            tilTglCuti.requestFocus();
        } else if (ketCuti.isEmpty()) {
            tilKetCuti.setError("Keterangan Cuti masih kosong");
            tilKetCuti.requestFocus();
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
                Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: "+spinJenisSelected.substring(0,2)+" | "+ tglCuti+" | "+lamaCuti+" | "+ spinDelegasiSelected +" | "+tvPeriode.getText().toString()+" | "+etKetCuti.getText().toString());
                insertCuti(spinJenisSelected.substring(0,2),tglCuti,spinDelegasiSelected,etKetCuti.getText().toString(),tvPeriode.getText().toString());

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
        String extractNumber = CharMatcher.inRange('0', '9').retainFrom(id_jenis); // 123
        AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izincuti")
                .addHeaders("Authorization", "Bearer " + token)
                //.addHeaders("Content-Type", "application/json")
                //.addHeaders("Content-Type", "multipart/form-data")
                .addMultipartParameter("id_jenis", extractNumber/*"1"*/)
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
