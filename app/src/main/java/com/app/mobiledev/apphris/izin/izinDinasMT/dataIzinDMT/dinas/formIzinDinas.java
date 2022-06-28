package com.app.mobiledev.apphris.izin.izinDinasMT.dataIzinDMT.dinas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class formIzinDinas extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText edit_tgl_dns, edit_mulai, edit_selesai, edit_keperluan, edit_catatan, edit_daerah, edit_transportasi;
    private ImageView image_surat_izin_dns, ivTgl, imStatus, ivClockM, ivClockS, ivLocation;
    private TextView tx_image_name, txClose, tvGetDate, tvNamaEmp, tvDivisiEmp, tvJabatanEmp, tvOnProgress, tvSetLatLong;
    private TextInputLayout tx_input_keperluan, tx_input_daerah, tx_input_transportasi, tx_input_tgl_dns, tx_input_mulai_dns, tx_input_selesai_dns;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llUploadLamp, llViewCalendar, llViewGetDates;

    private Uri resultUri;
    private File chosedfile, compressedImageFile;
    private Bitmap image_bmap;
    private Button btn_ajukan, btnDate, btnCancelDate, dialogBtnSubmit, dialogBtnCancel, btnTimeM, btnTimeS;
    private String
            kyano, token, nameImage = "", keperluanDns = "", tglDns = "", mulaiDns = "",
            selesaiDns = "", catatan = "", lamp = "1", namaEmp, divisiEmp, jabatanEmp,
            dateSend, add0HourM, add0MinM, add0HourS, add0MinS, latLong, location, trans;
    private Toast toast;

    ArrayList<String> list = new ArrayList<String>();

    private TimePicker timePicker;
    private Calendar calendar;

    private SessionManager session;
    private ProgressBar pbUpload;

    //map zone

    private map_place_picker_izin_lokasi dialog_map_lokasi;

    private double lat=0, lon=0, lat_lok_awal=0, lang_lok_awal=0, lat_lok_tujuan=0, lang_lok_tujuan=0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_dns);

        imStatus = findViewById(R.id.imStatus);

        edit_tgl_dns = findViewById(R.id.edit_tgl_dns);
        edit_catatan = findViewById(R.id.edit_catatan);
        edit_keperluan = findViewById(R.id.edit_keperluan);
        edit_daerah = findViewById(R.id.edit_daerah);
        edit_transportasi = findViewById(R.id.edit_transportasi);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        edit_mulai = findViewById(R.id.edit_mulai);
        edit_selesai = findViewById(R.id.edit_selesai);
        ivClockM = findViewById(R.id.ivClockM);
        ivClockS = findViewById(R.id.ivClockS);
        ivLocation = findViewById(R.id.ivLocation);

        tx_input_keperluan = findViewById(R.id.tx_input_keperluan);
        tx_input_daerah = findViewById(R.id.tx_input_daerah);
        tx_input_tgl_dns = findViewById(R.id.tx_input_tgl_dns);
        tx_input_mulai_dns = findViewById(R.id.tx_input_mulai);
        tx_input_selesai_dns = findViewById(R.id.tx_input_selesai);

        lin_transparant = findViewById(R.id.lin_transparant);
        llUploadLamp = findViewById(R.id.llUploadLamp);

        image_surat_izin_dns = findViewById(R.id.image_surat_izin_dns);
        ivTgl = findViewById(R.id.ivTgl);
        tx_image_name = findViewById(R.id.tx_image_name);
        btn_ajukan = findViewById(R.id.btn_ajukan);
        btnTimeM = findViewById(R.id.btnTimeM);
        btnTimeS = findViewById(R.id.btnTimeS);

        session = new SessionManager(formIzinDinas.this);
        helper.verifyStoragePermissions(formIzinDinas.this);
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

        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        pbUpload = findViewById(R.id.pbUpload);
        tvOnProgress = findViewById(R.id.tvOnProgress);

        dialog_map_lokasi = new map_place_picker_izin_lokasi();
        tvSetLatLong = findViewById(R.id.tvSetLatLong);

        Date date = new Date();  // to get the date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //String formattedDate = df.format(date.getTime());
        edit_tgl_dns.setText(df.format(date.getTime()));

        /*
         * START OPEN MULTIPLE CALENDAR
         * */

        llViewCalendar = findViewById(R.id.llViewCalendar);
        llViewGetDates = findViewById(R.id.llViewGetDates);

        tvGetDate = findViewById(R.id.tvGetDate);

        btnDate = findViewById(R.id.btnGetDates);
        btnCancelDate = findViewById(R.id.btnCancelDate);

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

                android.icu.text.SimpleDateFormat dateFormat = new android.icu.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                Toast.makeText(formIzinDinas.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.add(clearFormatDate);

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateUnselected(Date date) {
                android.icu.text.SimpleDateFormat dateFormat = new android.icu.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                Toast.makeText(formIzinDinas.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.remove(clearFormatDate);
            }
        });

        btnCancelDate.setOnClickListener(v -> {

            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
        });

        btnDate.setOnClickListener(v -> {
            //tvDateSelected.setText(list.toString().replace("[", "").replace("]", ""));
            //tvDateSelected.setText(list.toString().replace("[","" ).replace("]",""));
            edit_tgl_dns.setText(list.toString().replace("[", "").replace("]", ""));
            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
            Log.d("TAG_DATE_ARRAY", "onCreate: " + list.toString().replace("[", "").replace("]", ""));
        });

        /*
         * END OPEN MULTIPLE CALENDAR
         * */

        imStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //dateFormatterView = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        ivTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDateDialog();
                calendar.setVisibility(View.VISIBLE);
                llViewCalendar.setVisibility(View.VISIBLE);
                llViewGetDates.setVisibility(View.VISIBLE);
            }
        });


        image_surat_izin_dns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx_image_name.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        btn_ajukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekInputFormInsert();
            }
        });

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
        edit_daerah.setText(location);
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

        edit_mulai.setText(add0HourM+":"+add0MinM);
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

        edit_selesai.setText(add0HourS+":"+add0MinS);
    }

    private void opendialog_map_lokasi() {
        if (!dialog_map_lokasi.isAdded()){
            dialog_map_lokasi.ctx = formIzinDinas.this;

            dialog_map_lokasi.show(getSupportFragmentManager(), null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                final Uri imageUri = data.getData();
                chosedfile = new File(Objects.requireNonNull(getRealPathFormURI(imageUri, formIzinDinas.this)));
                resultUri = imageUri;
                image_surat_izin_dns.setImageURI(resultUri);

                long fileSizeInBytes = chosedfile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Log.d("TAG_IMG_SIZE", "onActivityResult: " + fileSizeInBytes + " | " + fileSizeInKB + " | " + fileSizeInMB);

                if (chosedfile != null) {
                    if (fileSizeInMB < 1) {
                        Toast.makeText(formIzinDinas.this, "Ukuran foto adalah " + fileSizeInKB + " Kb", toast.LENGTH_SHORT).show();

                        Log.d("TAG1", "onActivityResult: " + chosedfile.toString());
                    } else {

                        compressedImageFile =
                                new Compressor(this)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .compressToFile(chosedfile);

                        long fileSizeInBytesNew = compressedImageFile.length();
                        long fileSizeInKBNew = fileSizeInBytesNew / 1024;

                        Log.d("TAG2", "onActivityResult: Real: " + fileSizeInBytesNew + " | New: " + fileSizeInKBNew);

                        Toast.makeText(formIzinDinas.this, "Sekarang ukuran foto adalah " + fileSizeInKBNew + " Kb", toast.LENGTH_SHORT).show();
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
        keperluanDns = edit_keperluan.getText().toString().trim();
        tglDns = edit_tgl_dns.getText().toString();
        mulaiDns = edit_mulai.getText().toString();
        selesaiDns = edit_selesai.getText().toString();
        catatan = edit_catatan.getText().toString().trim();
        nameImage = tx_image_name.getText().toString();

        location = edit_daerah.getText().toString();
        latLong = tvSetLatLong.getText().toString();
        trans = edit_transportasi.getText().toString();

        if (keperluanDns.isEmpty()) {
            tx_input_keperluan.setError("Kepentingan masih kosong");
            tx_input_keperluan.requestFocus();
        } else if (location.isEmpty()) {
            tx_input_daerah.setError("Daerah tujuan masih kosong");
            tx_input_daerah.requestFocus();
            tx_input_daerah.setErrorEnabled(false);
        } else if (trans.isEmpty()) {
            tx_input_transportasi.setError("Transportasi masih kosong");
            tx_input_transportasi.requestFocus();
            tx_input_transportasi.setErrorEnabled(false);
        } else if (tglDns.isEmpty()) {
            tx_input_tgl_dns.setError("Tanggal masih kosong");
            tx_input_tgl_dns.requestFocus();
            tx_input_keperluan.setErrorEnabled(false);
        } else if (mulaiDns.isEmpty()) {
            tx_input_mulai_dns.setError("Waktu Mulai masih Kosong");
            tx_input_mulai_dns.requestFocus();
            tx_input_tgl_dns.setErrorEnabled(false);
        } else if (selesaiDns.isEmpty()) {
            tx_input_selesai_dns.setError("Waktu Selesai masih kosong");
            tx_input_selesai_dns.requestFocus();
            tx_input_mulai_dns.setErrorEnabled(false);
        } else if (chosedfile == null) {
            Toast.makeText(formIzinDinas.this, "Foto Lampiran masih kosong", toast.LENGTH_SHORT).show();
        } else {

            dialog_confirm();

        }
    }

    private void notifDialogSukses() {
        dialogResign = new Dialog(formIzinDinas.this);
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
        dialogConfirm = new Dialog(formIzinDinas.this);
        dialogConfirm.setContentView(R.layout.dialog_confirm);

        dialogBtnSubmit = dialogConfirm.findViewById(R.id.btnSubmit);
        dialogBtnCancel = dialogConfirm.findViewById(R.id.btnCancel);

        dialogConfirm.setCancelable(true);
        dialogConfirm.setCanceledOnTouchOutside(false);

        dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: " + keperluanDns + " | " + catatan + " | " + dateSend + " | " + mulaiDns + " | " + selesaiDns + " | " + chosedfile + " | " + lamp);
                insertIzinDns(keperluanDns, catatan, tglDns, mulaiDns, selesaiDns, compressedImageFile, lamp, trans, location, latLong);
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

    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateDialog() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);

            edit_tgl_dns.setText(dateFormatterView.format(newDate.getTime()));

            dateFormatterView = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateSend = dateFormatterView.format(newDate.getTime());

            Log.d("TAG_DATE_SELECTED", "onDateSet: " + dateSend);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }*/

    private void insertIzinDns(String _keperluan, String _catatan, String _selectDate, String _mulai, String _selesai, File _file, String _option, String _trans, String _daerah, String _lat_long) {
        Log.d("TAG_INPUT_CEK", "insertIzinDns: " + _keperluan + _catatan + _selectDate + _mulai + _selesai + _file + _option + _trans + _daerah + _lat_long);
        AndroidNetworking.upload(api.URL_IzinMt)
                //AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izinmt")
                .addHeaders("Authorization", "Bearer " + token)
                .addMultipartParameter("tgl", _selectDate)
                .addMultipartParameter("jam", _mulai)
                .addMultipartParameter("sampai", _selesai)
                .addMultipartParameter("kepentingan", _keperluan)
                .addMultipartParameter("ket", _catatan)
                .addMultipartFile("lampiran_file", _file)
                .addMultipartParameter("option", "1")
                .addMultipartParameter("jenis", "DNS")
                .addMultipartParameter("trans", _trans)
                .addMultipartParameter("daerah", _daerah)
                .addMultipartParameter("lat_long", _lat_long)
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

                        if (_file != null) {
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
                                Toast.makeText(formIzinDinas.this, "" + message, toast.LENGTH_SHORT).show();
                            } else if (status.equals("201")) {
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinDinas.this, "" + message, toast.LENGTH_SHORT).show();
                            } else {
                                message = response.getString("message");
                                Toast.makeText(formIzinDinas.this, "" + message, toast.LENGTH_SHORT).show();
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
                        Toast.makeText(formIzinDinas.this, anError.getErrorCode()+" : Pengajuan Gagal Dilakukan", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}