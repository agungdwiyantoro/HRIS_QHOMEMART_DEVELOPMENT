package com.app.mobiledev.apphris.izin.izinDinasMT.dataIzinDMT.mt;

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
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class formIzinMt extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter, dateFormatterView;
    private EditText edit_tgl_mt, edit_mulai, edit_selesai, edit_keperluan, edit_catatan;
    private ImageView image_surat_izin_mt, ivTgl, imStatus, ivClockM, ivClockS;
    private TextView tx_image_name, txClose, tvNamaEmp, tvDivisiEmp, tvJabatanEmp, tvOnProgress;
    private TextInputLayout tx_input_keperluan, tx_input_tgl_mt, tx_input_mulai_mt, tx_input_selesai_mt;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llUploadLamp;
    private CheckBox cbLamp;

    private Uri resultUri;
    private File chosedfile;
    private Bitmap image_bmap;
    private Button btn_ajukan, dialogBtnSubmit, dialogBtnCancel, btnTimeM, btnTimeS;
    private String kyano, token, nameImage = "", keperluanMt = "", tglMt = "", mulaiMt = "", selesaiMt = "", catatan = "", lamp = "0", namaEmp, divisiEmp, jabatanEmp, dateSend, format, add0HourM, add0MinM, add0HourS, add0MinS;
    private Toast toast;

    private TimePicker timePicker;
    private Calendar calendar;

    private SessionManager session;
    private ProgressBar pbUpload;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_mt);

        imStatus = findViewById(R.id.imStatus);

        edit_tgl_mt = findViewById(R.id.edit_tgl_mt);
        edit_catatan = findViewById(R.id.edit_catatan);
        edit_keperluan = findViewById(R.id.edit_keperluan);
        edit_mulai = findViewById(R.id.edit_mulai);
        edit_selesai = findViewById(R.id.edit_selesai);
        ivClockM = findViewById(R.id.ivClockM);
        ivClockS = findViewById(R.id.ivClockS);

        tx_input_keperluan = findViewById(R.id.tx_input_keperluan);
        tx_input_tgl_mt = findViewById(R.id.tx_input_tgl_mt);
        tx_input_mulai_mt = findViewById(R.id.tx_input_mulai);
        tx_input_selesai_mt = findViewById(R.id.tx_input_selesai);

        lin_transparant = findViewById(R.id.lin_transparant);
        llUploadLamp = findViewById(R.id.llUploadLamp);

        image_surat_izin_mt = findViewById(R.id.image_surat_izin_mt);
        ivTgl = findViewById(R.id.ivTgl);
        tx_image_name = findViewById(R.id.tx_image_name);
        btn_ajukan = findViewById(R.id.btn_ajukan);
        btnTimeM = findViewById(R.id.btnTimeM);
        btnTimeS = findViewById(R.id.btnTimeS);

        session = new SessionManager(formIzinMt.this);
        helper.verifyStoragePermissions(formIzinMt.this);
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

        imStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cbLamp = findViewById(R.id.cbLamp);
        cbLamp.setOnClickListener(v -> {
            if (cbLamp.isChecked()) {
                lamp = "1";
                llUploadLamp.setVisibility(View.VISIBLE);
            } else {
                llUploadLamp.setVisibility(View.GONE);
            }
        });

        dateFormatterView = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        ivTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });


        image_surat_izin_mt.setOnClickListener(new View.OnClickListener() {
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

    }

    public void setTimeM(View view) {
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

    public void setTimeS(View view) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                final Uri imageUri = data.getData();
                chosedfile = new File(Objects.requireNonNull(getRealPathFormURI(imageUri, formIzinMt.this)));
                resultUri = imageUri;
                image_surat_izin_mt.setImageURI(resultUri);

                long fileSizeInBytes = chosedfile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Log.d("TAG_IMG_SIZE", "onActivityResult: " + fileSizeInBytes + " | " + fileSizeInKB + " | " + fileSizeInMB);

                if (chosedfile != null) {
                    if (fileSizeInMB < 1) {
                        Toast.makeText(formIzinMt.this, "Ukuran foto adalah " + fileSizeInKB + " Kb", toast.LENGTH_SHORT).show();

                        Log.d("TAG1", "onActivityResult: " + chosedfile.toString());
                    } else {

                        File compressedImageFile =
                                new Compressor(this)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .compressToFile(chosedfile);

                        long fileSizeInBytesNew = compressedImageFile.length();
                        long fileSizeInKBNew = fileSizeInBytesNew / 1024;

                        Log.d("TAG2", "onActivityResult: Real: " + fileSizeInBytesNew + " | New: " + fileSizeInKBNew);

                        Toast.makeText(formIzinMt.this, "Sekarang ukuran foto adalah " + fileSizeInKBNew + " Kb", toast.LENGTH_SHORT).show();
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
        keperluanMt = edit_keperluan.getText().toString().trim();
        tglMt = edit_tgl_mt.getText().toString();
        mulaiMt = edit_mulai.getText().toString();
        selesaiMt = edit_selesai.getText().toString();
        //catatan = edit_catatan.getText().toString().trim();
        nameImage = tx_image_name.getText().toString();

        if (keperluanMt.isEmpty()) {
            tx_input_keperluan.setError("Keperluan masih kosong");
            tx_input_keperluan.requestFocus();
        } else if (tglMt.isEmpty()) {
            tx_input_tgl_mt.setError("Tanggal masih kosong");
            tx_input_tgl_mt.requestFocus();
            tx_input_keperluan.setErrorEnabled(false);
        } else if (mulaiMt.isEmpty()) {
            tx_input_mulai_mt.setError("Waktu Mulai masih Kosong");
            tx_input_mulai_mt.requestFocus();
            tx_input_tgl_mt.setErrorEnabled(false);
        } else if (selesaiMt.isEmpty()) {
            tx_input_selesai_mt.setError("Waktu Selesai masih kosong");
            tx_input_selesai_mt.requestFocus();
            tx_input_mulai_mt.setErrorEnabled(false);
        } else if (chosedfile == null && cbLamp.isChecked()) {
            Toast.makeText(formIzinMt.this, "Foto Lampiran masih kosong", toast.LENGTH_SHORT).show();
        } else {
            tx_input_selesai_mt.setErrorEnabled(false);

            dialog_confirm();

        }
    }

    private void notifDialogSukses() {
        dialogResign = new Dialog(formIzinMt.this);
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
        dialogConfirm = new Dialog(formIzinMt.this);
        dialogConfirm.setContentView(R.layout.dialog_confirm);

        dialogBtnSubmit = dialogConfirm.findViewById(R.id.btnSubmit);
        dialogBtnCancel = dialogConfirm.findViewById(R.id.btnCancel);

        dialogConfirm.setCancelable(true);
        dialogConfirm.setCanceledOnTouchOutside(false);

        dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: " + keperluanMt + " | " + catatan + " | " + dateSend + " | " + mulaiMt + " | " + selesaiMt + " | " + chosedfile + " | " + lamp);
                insertIzinMt(keperluanMt, catatan, dateSend, mulaiMt, selesaiMt, chosedfile, lamp);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateDialog() {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {

            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);

            edit_tgl_mt.setText(dateFormatterView.format(newDate.getTime()));

            dateFormatterView = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateSend = dateFormatterView.format(newDate.getTime());

            Log.d("TAG_DATE_SELECTED", "onDateSet: " + dateSend);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void insertIzinMt(String _keperluan, String _catatan, String _selectDate, String _mulai, String _selesai, File _file, String _option) {
        Log.d("TAG_INPUT_CEK", "insertIzinMt: " + _keperluan + _catatan + _selectDate + _mulai + _selesai + _file + _option);
        AndroidNetworking.upload(api.URL_IzinMt)
                //AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izinmt")
                .addHeaders("Authorization", "Bearer " + token)
                .addMultipartParameter("tgl", _selectDate)
                .addMultipartParameter("jam", _mulai)
                .addMultipartParameter("sampai", _selesai)
                .addMultipartParameter("kepentingan", _keperluan)
                .addMultipartParameter("catatan", _catatan)
                .addMultipartFile("lampiran_file", _file)
                .addMultipartParameter("option", _option)
                .addMultipartParameter("jenis", "MT")
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
                                Toast.makeText(formIzinMt.this, "" + message, toast.LENGTH_SHORT).show();
                            } else if (status.equals("201")) {
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinMt.this, "" + message, toast.LENGTH_SHORT).show();
                            } else {
                                message = response.getString("message");
                                Toast.makeText(formIzinMt.this, "" + message, toast.LENGTH_SHORT).show();
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