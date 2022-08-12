package com.app.mobiledev.apphris.izin.izinOffTerlambat.terlambat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.mobiledev.apphris.Model.modelJenisTelat;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinDinasMT.dataIzinDMT.dinas.map_place_picker_izin_lokasi;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class formIzinTerlambat extends AppCompatActivity implements View.OnClickListener{

    private SimpleDateFormat dateFormatter,dateFormatterView;
    private DatePickerDialog datePickerDialog;
    private EditText etJenisTerlambat, etTanggal, etJamKedatangan, etAlasan;
    private TextInputLayout til_jenis_terlambat, til_input_tanggal, til_jam_kedatangan, til_alasan, til_lampiran_terlambat;
    private CheckBox cbLampirkanFoto;
    private ImageView ivTgl, ivClock;
    private TimePicker timePicker;
    private Calendar calendar;

    private ImageView ivLampiran, imStatus, ivClockS, ivLocation;
    private TextView tx_image_name, txClose, tvGetDate, tvNamaEmp, tvDivisiEmp, tvJabatanEmp, tvOnProgress, tvSetLatLong;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llUploadLamp, llViewCalendar, llViewGetDates;

    private Uri resultUri;
    private File chosedfile, compressedImageFile;
    private Bitmap image_bmap;
    private Button btn_ajukan, btnDate, btnCancelDate, dialogBtnSubmit, dialogBtnCancel, btnTime;
    private String
            kyano, token, nameImage = "", keperluanDns = "", tglDns = "", mulaiDns = "",
            selesaiDns = "", catatan = "", lamp = "1", namaEmp, divisiEmp, jabatanEmp,
            dateSend, add0HourM, add0MinM, add0HourS, add0MinS, latLong, location, trans;
    private Toast toast;

    ArrayList<String> list = new ArrayList<String>();

    private SessionManager session;
    private ProgressBar pbUpload;

    //map zone

    private map_place_picker_izin_lokasi dialog_map_lokasi;

    private double lat=0, lon=0, lat_lok_awal=0, lang_lok_awal=0, lat_lok_tujuan=0, lang_lok_tujuan=0;

    private Spinner spJenisTerlambat;
    private ArrayList<String> jenisTelatList;

    private static String checkLampiran;

    private List<modelJenisTelat> jenisTelat;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_terlambat);

        init();
        calendar = Calendar.getInstance();

        getNamaDanJabatan();
        getJenisTerlambat();

        llViewCalendar = findViewById(R.id.llViewCalendar);
        llViewGetDates = findViewById(R.id.llViewGetDates);

        tvGetDate = findViewById(R.id.tvGetDate);

        /*
         * END OPEN MULTIPLE CALENDAR
         * */

        String[] arraySpinner = new String[] {
                "1", "2", "3", "4", "5", "6", "7"
        };



        imStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dateFormatterView = new SimpleDateFormat("dd MMM yyyy", Locale.US);




        btn_ajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekInputFormInsert();
            }
        });

        cbLampirkanFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbLampirkanFoto.isChecked()){
                    checkLampiran = "1";
                    llUploadLamp.setVisibility(View.VISIBLE);
                    return;
                }


                checkLampiran = "0";
                llUploadLamp.setVisibility(View.GONE);
            }
        });

/*

        ivClockM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setVisibility(View.VISIBLE);
                btnTimeM.setVisibility(View.VISIBLE);
            }
        });

        ivClockS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setVisibility(View.VISIBLE);
                btnTimeS.setVisibility(View.VISIBLE);
            }
        });


        btnTimeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setVisibility(View.GONE);
                btnTimeM.setVisibility(View.GONE);
                setTimeM(v);
            }
        });

        btnTimeS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.setVisibility(View.GONE);
                btnTimeS.setVisibility(View.GONE);
                setTimeS(v);
            }
        });

        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog_map_lokasi();
            }
        });

    }


    public void setLocationSelected(String location, String _latLong) {
        //to retrive and set location from map dialogfragment selected location
        //edit_daerah.setText(location);
        tvSetLatLong.setText(_latLong);

        Log.d("TAG_GET_LATLONG", "onCreate: "+tvSetLatLong.getText());
    }

    private void setTimeM(View view) {
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        if (hour < 10) {
            add0HourM = "0"+hour;
        } else {
            add0HourM = ""+hour;
        }

        if (min < 10) {
            add0MinM = "0"+min;
        } else {
            add0MinM = ""+min;
        }

        //edit_mulai.setText(add0HourM+":"+add0MinM);
    }

    private void setTimeS(View view) {
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        if (hour < 10) {
            add0HourS = "0"+hour;
        } else {
            add0HourS = ""+hour;
        }

        if (min < 10) {
            add0MinS = "0"+min;
        } else {
            add0MinS = ""+min;
        }

        //edit_selesai.setText(add0HourS+":"+add0MinS);

         */
    }

    private void opendialog_map_lokasi() {
        if (!dialog_map_lokasi.isAdded()){
            dialog_map_lokasi.ctx = formIzinTerlambat.this;

            dialog_map_lokasi.show(getSupportFragmentManager(), null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                final Uri imageUri = data.getData();
                chosedfile = new File(Objects.requireNonNull(getRealPathFormURI(imageUri, formIzinTerlambat.this)));
                resultUri = imageUri;
                ivLampiran.setImageURI(resultUri);

                long fileSizeInBytes = chosedfile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Log.d("TAG_IMG_SIZE", "onActivityResult: " + fileSizeInBytes + " | " + fileSizeInKB + " | " + fileSizeInMB);

                if (chosedfile != null) {
                    if (fileSizeInMB < 1) {
                        Toast.makeText(formIzinTerlambat.this, "Ukuran foto adalah " + fileSizeInKB + " Kb", toast.LENGTH_SHORT).show();

                        Log.d("TAG1", "onActivityResult: " + chosedfile.toString());

                        compressedImageFile = chosedfile;

                    } else {

                        compressedImageFile =
                                new Compressor(this)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .compressToFile(chosedfile);

                        long fileSizeInBytesNew = compressedImageFile.length();
                        long fileSizeInKBNew = fileSizeInBytesNew / 1024;

                        Log.d("TAG2", "onActivityResult: Real: " + fileSizeInBytesNew + " | New: " + fileSizeInKBNew);

                        Toast.makeText(formIzinTerlambat.this, "Sekarang ukuran foto adalah " + fileSizeInKBNew + " Kb", toast.LENGTH_SHORT).show();
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
        if(spJenisTerlambat==null){
            spJenisTerlambat.requestFocus();
            til_jenis_terlambat.setError(getString(R.string.til_jenis_terlambat));
        }

        if(etTanggal.getText().toString().isEmpty()){
            etTanggal.requestFocus();
            til_input_tanggal.setError(getString(R.string.til_tanggal_terlambat));
            return;
        }
        if(etJamKedatangan.getText().toString().isEmpty()){
            etJamKedatangan.requestFocus();
            til_jam_kedatangan.setError(getString(R.string.til_jam_kedatangan_terlambat));
            return;
        }
        if(cbLampirkanFoto.isChecked()&&(chosedfile == null && lamp.equals("1"))){
            til_lampiran_terlambat.requestFocus();
            til_lampiran_terlambat.setError("Foto lampiran masih kosong");
            return;
        }
        if(etAlasan.getText().toString().isEmpty()){
            etAlasan.requestFocus();
            til_alasan.setError(getString(R.string.til_alasan_terlambat));
            return;
        }

        dialog_confirm();
    }

    private void notifDialogSukses() {
        dialogResign = new Dialog(formIzinTerlambat.this);
        dialogResign.setContentView(R.layout.dialog_sukses);
        dialogResign.setCancelable(true);
        dialogResign.setCanceledOnTouchOutside(false);
        txClose = dialogResign.findViewById(R.id.txClose);
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
        dialogConfirm = new Dialog(formIzinTerlambat.this);
        dialogConfirm.setContentView(R.layout.dialog_confirm);

        dialogBtnSubmit = dialogConfirm.findViewById(R.id.btnSubmit);
        dialogBtnCancel = dialogConfirm.findViewById(R.id.btnCancel);

        dialogConfirm.setCancelable(true);
        dialogConfirm.setCanceledOnTouchOutside(false);

        dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: " + keperluanDns + " | " + catatan + " | " + dateSend + " | " + mulaiDns + " | " + selesaiDns + " | " + chosedfile + " | " + lamp);
                Log.d("XHXHX", "J " + jenisTelat.get(0).getJenis());
                insertIzinTerlambat(dateSend, etJamKedatangan.getText().toString(), etAlasan.getText().toString(), compressedImageFile,  checkLampiran, String.valueOf(spJenisTerlambat.getSelectedItemPosition()));
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



    private void insertIzinTerlambat(String tgl_telat, String jam_sampai, String alasan, File lampiran, String option, String jenis_telat) {
        Log.d("TAG_INPUT_CEK", "insertIzinDns: " + tgl_telat + jam_sampai);
        AndroidNetworking.upload(api.URL_getIzinOff)
                //AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izinoff")
                .addHeaders("Authorization", "Bearer " + token)
                .addMultipartParameter("jenis", "TELAT")
                .addMultipartParameter("tgl_telat", tgl_telat)
                .addMultipartParameter("jam_sampai", jam_sampai)
                .addMultipartParameter("alasan", alasan)
                .addMultipartFile("lampiran", lampiran)
                .addMultipartParameter("option", option)
                .addMultipartParameter("jenis_telat", jenis_telat)
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

                        if (lampiran != null) {
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
                            Log.d("RESULT_RESPONSE_INSERT", "onResponse: " + response);
                            String message = response.getString("message");
                            if (status.equals("200")) {
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinTerlambat.this, R.string.data_telah_terkirim, toast.LENGTH_SHORT).show();
                            } else if (status.equals("201")) {
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinTerlambat.this, "" + message, toast.LENGTH_SHORT).show();
                            } else {
                                message = response.getString("message");
                                Toast.makeText(formIzinTerlambat.this, "" + message, toast.LENGTH_SHORT).show();
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
                        if(anError.getErrorCode() == 409){
                            Toast.makeText(formIzinTerlambat.this, "Anda sudah mengajukan izin terlambat pada tanggal yang sama", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(formIzinTerlambat.this, anError.getErrorCode()+" : Pengajuan Gagal Dilakukan", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_tanggal:
                showDateDialog();

                break;

            case R.id.iv_clock:
                timePicker.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_ajukan:
                cekInputFormInsert();
                break;

            case R.id.btnTime:
                setTime();
                break;

            case R.id.iv_upload_lampiran:
                tx_image_name.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateDialog() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);

            etTanggal.setText(dateFormatterView.format(newDate.getTime()));

            dateFormatterView = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateSend = dateFormatterView.format(newDate.getTime());

            Log.d("TAG_DATE_SELECTED", "onDateSet: " + dateSend);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void getNamaDanJabatan(){
        session = new SessionManager(formIzinTerlambat.this);
        helper.verifyStoragePermissions(formIzinTerlambat.this);
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
    }

    private void init(){
        spJenisTerlambat = findViewById(R.id.sp_jenis_terlambat);
        etTanggal = findViewById(R.id.et_tanggal);
        ivTgl = findViewById(R.id.iv_tanggal);
        etJamKedatangan = findViewById(R.id.et_jam_kedatangan);
        ivClock = findViewById(R.id.iv_clock);
        cbLampirkanFoto = findViewById(R.id.cb_lampirkan_foto);
        etAlasan = findViewById(R.id.et_alasan);
        btn_ajukan = findViewById(R.id.btn_ajukan);
        timePicker = findViewById(R.id.timePicker);
        btnTime = findViewById(R.id.btnTime);

        til_jenis_terlambat = findViewById(R.id.til_jenis_terlambat);
        til_input_tanggal = findViewById(R.id.til_input_tanggal);
        til_jam_kedatangan = findViewById(R.id.til_jam_kedatangan);
        til_lampiran_terlambat = findViewById(R.id.til_lampiran_terlambat);
        til_alasan = findViewById(R.id.til_alasan);




        imStatus = findViewById(R.id.imStatus);



        lin_transparant = findViewById(R.id.lin_transparant);
        llUploadLamp = findViewById(R.id.llUploadLamp);

        ivLampiran = findViewById(R.id.iv_upload_lampiran);

        tx_image_name = findViewById(R.id.tx_image_name);


       // btnTimeS = findViewById(R.id.btnTimeS);
        pbUpload = findViewById(R.id.pbUpload);

        tvOnProgress = findViewById(R.id.tvOnProgress);
        llViewCalendar = findViewById(R.id.llViewCalendar);
        llViewGetDates = findViewById(R.id.llViewGetDates);

        tvGetDate = findViewById(R.id.tvGetDate);

        btnDate = findViewById(R.id.btnGetDates);
        btnCancelDate = findViewById(R.id.btnCancelDate);

        tvSetLatLong = findViewById(R.id.tvSetLatLong);







        ivTgl.setOnClickListener(this);
        ivClock.setOnClickListener(this);
        btn_ajukan.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        cbLampirkanFoto.setOnClickListener(this);
        ivLampiran.setOnClickListener(this);
    }

    private void setTime() {
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        if (hour < 10) {
            add0HourM = "0"+hour;
        } else {
            add0HourM = ""+hour;
        }

        if (min < 10) {
            add0MinM = "0"+min;
        } else {
            add0MinM = ""+min;
        }

        etJamKedatangan.setText(add0HourM+":"+add0MinM);
        timePicker.setVisibility(View.GONE);
    }

    private void showCalendar(){
        timePicker.setIs24HourView(true);
        calendar = Calendar.getInstance();


        Calendar nextMonth = Calendar.getInstance();

        nextMonth.add(Calendar.MONTH, +1);

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

                Toast.makeText(formIzinTerlambat.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.add(clearFormatDate);

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateUnselected(Date date) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                Toast.makeText(formIzinTerlambat.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.remove(clearFormatDate);
            }
        });

    }


    private void getJenisTerlambat() {
        AndroidNetworking.get(api.URL_IzinTelat_jenis)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            jenisTelatList = new ArrayList<>();
                            jenisTelat = new ArrayList<>();
                            //JSONObject response = new JSONObject();
                            if (response.getString("status").equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    jenisTelatList.add(i, data.getString("jenis") );
                                    jenisTelat.add(i, new modelJenisTelat(data.getString("id_terlambat"), data.getString("jenis"), data.getString("keterangan")));
                                }
                            }
                            spJenisTerlambat.setAdapter(new ArrayAdapter<String>(formIzinTerlambat.this, R.layout.spinner_list, jenisTelatList));
                            spJenisTerlambat.setSelection(0);
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
/*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_list, jenisTelatList);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        spJenisTerlambat.setAdapter(adapter);

 */
    }
}