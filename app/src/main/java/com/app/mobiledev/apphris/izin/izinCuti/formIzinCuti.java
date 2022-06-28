package com.app.mobiledev.apphris.izin.izinCuti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinDinasMT.dataIzinDMT.mt.formIzinMt;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.google.common.base.CharMatcher;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class formIzinCuti extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText etTglCuti, etKetCuti, etLamaCuti;
    private ImageView ivTgl, imStatus, image_surat_izin_cuti;
    private TextView tx_image_name, txClose, tvGetDate, tvNamaEmp,tvDivisiEmp, tvJabatanEmp, tvSisaCuti, tvPeriode, tvHakCuti, tvOnProgress;
    private TextInputLayout tilTglCuti, tilLamaCuti, tilKetCuti, tilDelegasiAutoComp;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llViewCalendar, llViewGetDates, linearLayout, llUploadLamp;

    private Button btn_ajukan, btnDate, btnCancelDate, dialogBtnSubmit, dialogBtnCancel;
    private String kyano, token, lamaCuti = "", ketCuti = "", tglCuti = "", kuotaCuti = "", hakCuti = "", namaEmp, divisiEmp, jabatanEmp, spinJenisSelected, spinDelegasiSelected, spinResultJenis, spinResultDelegasi, splitKuotaCuti, lamp = "1";
    private Toast toast;
    Spinner spinJenis, spinDelegasi;
    Snackbar snackbar;

    String jenis1, jenis2, jenis3, jenis4, jenis5, jenis6, jenis7, jenis8, jenis9, jenis10;

    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> jenisList, delegasiList;

    CalendarPickerView calendar;

    SessionManager session;

    AutoCompleteTextView actv;

    private Uri resultUri;
    private File chosedfile, compressedImageFile;

    private ProgressBar pbUpload;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_cuti);

        /*
        * GET DATA SHAREPREF START
        * */

        imStatus = findViewById(R.id.imStatus);
        imStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        llUploadLamp = findViewById(R.id.llUploadLamp);
        tx_image_name = findViewById(R.id.tx_image_name);
        image_surat_izin_cuti = findViewById(R.id.image_surat_izin_cuti);

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
        tilDelegasiAutoComp = findViewById(R.id.tilDelegasiAutoComp);

        lin_transparant = findViewById(R.id.lin_transparant);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        ivTgl = findViewById(R.id.ivTgl);
        btn_ajukan = findViewById(R.id.btn_ajukan);

        //get the spinner from the xml.
        spinJenis = findViewById(R.id.spinJenisCuti);
        spinDelegasi = findViewById(R.id.spinDelegasi);

        pbUpload = findViewById(R.id.pbUpload);
        tvOnProgress = findViewById(R.id.tvOnProgress);

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

                if (kuotaCuti.contains(",")) {
                    splitKuotaCuti = kuotaCuti.split(",")[0];
                    Log.d("TAG_TEST_CONVERT", "onCreate: "+splitKuotaCuti);

                    if (list.size() > Integer.parseInt(splitKuotaCuti)) {
                        showToast("Tanggal melebihi batas Cuti Tahunan");
                    } else {
                        getAfterCheckDateSelected();
                    }

                } else {
                    if (list.size() > Integer.parseInt(kuotaCuti)) {
                        showToast("Tanggal melebihi batas Cuti Tahunan");
                    } else {
                        getAfterCheckDateSelected();
                    }
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

                if (spinJenisSelected.contains("10")) {
                    llUploadLamp.setVisibility(View.GONE);
                    lamp = "0";
                } else {
                    llUploadLamp.setVisibility(View.VISIBLE);
                    lamp = "1";
                }

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

        image_surat_izin_cuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx_image_name.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
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

        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setTextColor(Color.BLACK);

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
        AndroidNetworking.get(api.URL_IzinCuti_jenis)
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
                            spinJenis.setSelection(9);
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
        AndroidNetworking.get(api.URL_IzinCuti_delegasi)
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
                            actv.setAdapter(new ArrayAdapter<String>(formIzinCuti.this, android.R.layout.simple_spinner_dropdown_item, delegasiList));

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
        AndroidNetworking.get(api.URL_IzinCuti_kuotaTahunanPeriode+periode)
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
                            tvSisaCuti.setText(kuotaCuti.split(",")[0]+" Hari");
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
        //AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/kuota_cuti")
        AndroidNetworking.get(api.URL_IzinCuti_hakCuti)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                final Uri imageUri = data.getData();
                chosedfile = new File(Objects.requireNonNull(getRealPathFormURI(imageUri, formIzinCuti.this)));
                resultUri = imageUri;
                image_surat_izin_cuti.setImageURI(resultUri);

                long fileSizeInBytes = chosedfile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Log.d("TAG_IMG_SIZE", "onActivityResult: " + fileSizeInBytes + " | " + fileSizeInKB + " | " + fileSizeInMB);

                if (chosedfile != null) {
                    if (fileSizeInMB < 1) {
                        Toast.makeText(formIzinCuti.this, "Ukuran foto adalah " + fileSizeInKB + " Kb", toast.LENGTH_SHORT).show();

                        Log.d("TAG1", "onActivityResult: " + chosedfile.toString());
                    } else {

                        compressedImageFile =
                                new Compressor(this)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .compressToFile(chosedfile);

                        long fileSizeInBytesNew = compressedImageFile.length();
                        long fileSizeInKBNew = fileSizeInBytesNew / 1024;

                        Log.d("TAG2", "onActivityResult: Real: " + fileSizeInBytesNew + " | New: " + fileSizeInKBNew);

                        Toast.makeText(formIzinCuti.this, "Sekarang ukuran foto adalah " + fileSizeInKBNew + " Kb", toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.d("TAKE_GALERI", "onActivityResult: " + e);
        }


    }

    private String getRealPathFormURI(Uri contenturi, Activity context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(contenturi, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            return s;
        }

        return null;
    }

    private void cekInputFormInsert() {

        tglCuti = etTglCuti.getText().toString();
        lamaCuti = etLamaCuti.getText().toString();
        ketCuti = etKetCuti.getText().toString();

        if (tglCuti.isEmpty()) {
            tilTglCuti.setError("Tanggal Cuti masih kosong");
            tilTglCuti.requestFocus();
        } else if(actv.getText().toString().isEmpty()) {
            tilDelegasiAutoComp.setError("Delegasi masih kosong");
            tilDelegasiAutoComp.requestFocus();
        } else if(actv.length() < 5 && !actv.getText().toString().contains(" ") ) {
            tilDelegasiAutoComp.setError("Delegasi Tidak Valid");
            tilDelegasiAutoComp.requestFocus();
        }else if (ketCuti.isEmpty()) {
            tilKetCuti.setError("Keterangan Cuti masih kosong");
            tilKetCuti.requestFocus();
        } else if (chosedfile == null && lamp.equals("1")) {
            Toast.makeText(formIzinCuti.this, "Foto Lampiran masih kosong", toast.LENGTH_SHORT).show();
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
                insertCuti(spinJenisSelected.substring(0,2),tglCuti,actv.getText().toString(),etKetCuti.getText().toString(),tvPeriode.getText().toString(), compressedImageFile, lamp);
                lin_transparant.setVisibility(View.VISIBLE);
                dialogConfirm.dismiss();
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

    private void insertCuti(String id_jenis, String select_cuti, String delegasi_to, String alasan, String periode, File file, String option) {
        Log.d("TAG_INPUT_CEK", "insertIzinCuti: "+ id_jenis + " | " + select_cuti + " | " + delegasi_to + " | " + alasan + " | " + periode);
        String extractNumber = CharMatcher.inRange('0', '9').retainFrom(id_jenis); // 123
        //AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izincuti")
        AndroidNetworking.upload(api.URL_IzinCuti)
                .addHeaders("Authorization", "Bearer " + token)
                //.addHeaders("Content-Type", "application/json")
                //.addHeaders("Content-Type", "multipart/form-data")
                .addHeaders("MediaType","text/plain")
                .addMultipartParameter("id_jenis", extractNumber/*"1"*/)
                .addMultipartParameter("select_cuti", /*"2022-06-17, 2022-06-16"*/select_cuti)
                .addMultipartParameter("delegasi_to", /*"ANWARUL MUSLIMIN"*/delegasi_to)
                .addMultipartParameter("alasan", /*"test alasan"*/alasan)
                .addMultipartParameter("periode", /*"2022"*/periode)
                .addMultipartFile("lampiran_file", file)
                .addMultipartParameter("option", option)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //Log.d("TAG_UPLOAD_PROGRESS", "onProgress: "+bytesUploaded);

                        int up = Integer.parseInt(String.valueOf(bytesUploaded));
                        int tot = Integer.parseInt(String.valueOf(totalBytes));
                        int progress = 100 * up / tot;

                        if (file != null) {
                            lin_transparant.setVisibility(View.VISIBLE);
                            tvOnProgress.setText(progress+"%");
                            pbUpload.setProgress(Integer.parseInt(String.valueOf(bytesUploaded)), true);
                        } else {
                            lin_transparant.setVisibility(View.VISIBLE);
                        }
                        Log.d("TAG_UPLOAD_TOTAL", "onProgress: "+bytesUploaded+" Total : "+totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lin_transparant.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            Log.d("RESULT_RESPONSE_INSERT", "onResponse: " + response.toString());
                            String message = response.getString("message");
                            if (status.equals("200")) {
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
