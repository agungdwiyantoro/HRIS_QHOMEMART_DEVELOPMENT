package com.app.mobiledev.apphris.izin.izinOffTerlambat.off;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.izin.izinDinasMT.dataIzinDMT.dinas.map_place_picker_izin_lokasi;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
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

public class formIzinOff extends AppCompatActivity {

    private SimpleDateFormat dateFormatter;
    private EditText et_tanggal_off, et_lama_off, et_alasan, edit_tgl_dns, edit_mulai, edit_selesai, edit_keperluan, edit_catatan, edit_daerah, edit_transportasi;
    private ImageView iv_tanggal_off, iv_upload_lampiran_off,image_surat_izin_dns, ivTgl, imStatus, ivClockM, ivClockS, ivLocation;
    private CheckBox cb_lampiran;
    private Button bt_ajukan;

    private TextView tx_image_name, txClose, tvGetDate, tvNamaEmp, tvDivisiEmp, tvJabatanEmp, tvOnProgress, tvSetLatLong;
    private TextInputLayout til_tanggal_off, til_alasan, til_iv_lampiran_off;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llUploadLamp, llViewCalendar, llViewGetDates;
    private CalendarPickerView calendar;

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
    //private Calendar calendar;

    private SessionManager session;
    private ProgressBar pbUpload;

    //map zone

    private map_place_picker_izin_lokasi dialog_map_lokasi;

    private double lat=0, lon=0, lat_lok_awal=0, lang_lok_awal=0, lat_lok_tujuan=0, lang_lok_tujuan=0;

    private static String checkLampiran;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_off);

        et_tanggal_off = findViewById(R.id.et_tanggal_off);
        et_lama_off = findViewById(R.id.et_lama_off);
        et_alasan = findViewById(R.id.et_alasan);
        iv_tanggal_off = findViewById(R.id.iv_tanggal_off);
        cb_lampiran = findViewById(R.id.cb_lampirkan_foto);
        btn_ajukan = findViewById(R.id.btn_ajukan);
        iv_upload_lampiran_off = findViewById(R.id.iv_lampiran_off);
        til_iv_lampiran_off = findViewById(R.id.til_iv_lampiran_off);
        til_tanggal_off = findViewById(R.id.til_tanggal_off);
        til_alasan = findViewById(R.id.til_alasan);

        imStatus = findViewById(R.id.imStatus);
        edit_tgl_dns = findViewById(R.id.edit_tgl_dns);
        edit_catatan = findViewById(R.id.edit_catatan);
        edit_keperluan = findViewById(R.id.edit_keperluan);
        edit_daerah = findViewById(R.id.edit_daerah);
        edit_transportasi = findViewById(R.id.edit_transportasi);
        edit_mulai = findViewById(R.id.edit_mulai);
        edit_selesai = findViewById(R.id.edit_selesai);
        ivClockM = findViewById(R.id.ivClockM);
        ivClockS = findViewById(R.id.ivClockS);
        ivLocation = findViewById(R.id.ivLocation);

        lin_transparant = findViewById(R.id.lin_transparant);
        llUploadLamp = findViewById(R.id.llUploadLamp);

        image_surat_izin_dns = findViewById(R.id.image_surat_izin_dns);
        ivTgl = findViewById(R.id.ivTgl);
        tx_image_name = findViewById(R.id.tx_image_name);

        btnTimeM = findViewById(R.id.btnTimeM);
        btnTimeS = findViewById(R.id.btnTimeS);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        session = new SessionManager(formIzinOff.this);
        helper.verifyStoragePermissions(formIzinOff.this);
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

        llViewCalendar = findViewById(R.id.llViewCalendar);
        llViewGetDates = findViewById(R.id.llViewGetDates);


        Calendar nextMonth = Calendar.getInstance();

        nextMonth.add(Calendar.MONTH, +4);

        calendar = findViewById(R.id.calendar_view);
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


        pbUpload = findViewById(R.id.pbUpload);
        tvOnProgress = findViewById(R.id.tvOnProgress);

        dialog_map_lokasi = new map_place_picker_izin_lokasi();
        tvSetLatLong = findViewById(R.id.tvSetLatLong);


        tvGetDate = findViewById(R.id.tvGetDate);

        btnDate = findViewById(R.id.btnGetDates);
        btnCancelDate = findViewById(R.id.btnCancelDate);

        cb_lampiran.setOnClickListener(v -> {
            if(cb_lampiran.isChecked()){
                checkLampiran = "1";
                llUploadLamp.setVisibility(View.VISIBLE);
            }
            else{
                checkLampiran = "0";
                llUploadLamp.setVisibility(View.GONE);
            }
        });

        btnCancelDate.setOnClickListener(v -> {

            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
        });

        btnDate.setOnClickListener(v -> {
            et_tanggal_off.setText(list.toString().replace("[", "").replace("]", ""));
            calendar.setVisibility(View.GONE);
            llViewGetDates.setVisibility(View.GONE);
            et_lama_off.setText(list.size() + " hari");
        });

        imStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_tanggal_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDateDialog();
                calendar.setVisibility(View.VISIBLE);
                llViewCalendar.setVisibility(View.VISIBLE);
                llViewGetDates.setVisibility(View.VISIBLE);
            }
        });

        iv_upload_lampiran_off.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                final Uri imageUri = data.getData();
                chosedfile = new File(Objects.requireNonNull(getRealPathFormURI(imageUri, formIzinOff.this)));
                resultUri = imageUri;
                iv_upload_lampiran_off.setImageURI(resultUri);

                long fileSizeInBytes = chosedfile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Log.d("TAG_IMG_SIZE", "onActivityResult: " + fileSizeInBytes + " | " + fileSizeInKB + " | " + fileSizeInMB);

                if (chosedfile != null) {
                    if (fileSizeInMB < 1) {
                        Toast.makeText(formIzinOff.this, "Ukuran foto adalah " + fileSizeInKB + " Kb", toast.LENGTH_SHORT).show();

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

                        Toast.makeText(formIzinOff.this, "Sekarang ukuran foto adalah " + fileSizeInKBNew + " Kb", toast.LENGTH_SHORT).show();
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
        if(et_tanggal_off.getText().toString().isEmpty()){
            et_tanggal_off.requestFocus();
            til_tanggal_off.setError(getString(R.string.til_tanggal_off_kosong));
            return;
        }
        if(cb_lampiran.isChecked()&&(chosedfile == null && lamp.equals("1"))){
            iv_upload_lampiran_off.requestFocus();
            til_iv_lampiran_off.setError(getString(R.string.til_lampiran_off_kosong));
            return;
        }
        if(et_alasan.getText().toString().isEmpty()){
            et_alasan.requestFocus();
            til_alasan.setError(getString(R.string.til_alasan_off_kosong));
            return;
        }

        dialog_confirm();
    }

    private void notifDialogSukses() {
        dialogResign = new Dialog(formIzinOff.this);
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
        dialogConfirm = new Dialog(formIzinOff.this);
        dialogConfirm.setContentView(R.layout.dialog_confirm);

        dialogBtnSubmit = dialogConfirm.findViewById(R.id.btnSubmit);
        dialogBtnCancel = dialogConfirm.findViewById(R.id.btnCancel);

        dialogConfirm.setCancelable(true);
        dialogConfirm.setCanceledOnTouchOutside(false);

        dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: " + keperluanDns + " | " + catatan + " | " + dateSend + " | " + mulaiDns + " | " + selesaiDns + " | " + chosedfile + " | " + lamp);
                insertIzinOff(et_tanggal_off.getText().toString(), et_alasan.getText().toString(), compressedImageFile,  checkLampiran);
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

    private void insertIzinOff(String _selectDate, String _catatan, File _file, String checkLampiran) {
        Log.d("TAG_INPUT_CEK", "insertIzinDns: " +  _selectDate + _catatan + _file);
        AndroidNetworking.upload(api.URL_getIzinOff)
                //AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izinoff")
                .addHeaders("Authorization", "Bearer " + token)
                .addMultipartParameter("select_off", _selectDate)
                .addMultipartParameter("alasan", _catatan)
                .addMultipartFile("lampiran", _file)
                .addMultipartParameter("option", checkLampiran)
                .addMultipartParameter("jenis", "OFF")
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
                                Toast.makeText(formIzinOff.this, R.string.data_telah_terkirim, toast.LENGTH_SHORT).show();
                            } else if (status.equals("201")) {
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinOff.this, "" + message, toast.LENGTH_SHORT).show();
                            } else {
                                message = response.getString("message");
                                Toast.makeText(formIzinOff.this, "" + message, toast.LENGTH_SHORT).show();
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

                        if(anError.getErrorCode()==409){
                            Toast.makeText(formIzinOff.this, "Anda sudah mengajukan izin off pada tanggal yang sama", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(formIzinOff.this, anError.getErrorCode()+" : Pengajuan Gagal Dilakukan", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}