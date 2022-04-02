package com.app.mobiledev.apphris.izin.izinSakit;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class formIzinSakit extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText edit_tgl_sakit, edit_tgl_selesai_sakit, edit_IndikasiPenyakit, edit_catatan;
    private ImageView image_surat_izin_sakit, ivTgl, imStatus;
    private TextView tx_image_name, txClose, tvGetDate, tvNamaEmp,tvDivisiEmp, tvJabatanEmp;
    private TextInputLayout tx_input_indikasi, tx_input_tgl_sakit, tx_input_tgl_selesai_sakit;
    private Dialog dialogResign, dialogConfirm;
    private LinearLayout lin_transparant, llUploadSkd, llViewCalendar, llViewGetDates;
    private CheckBox cbSkdView;

    private Uri resultUri;
    private File chosedfile;
    private Bitmap image_bmap;
    private Button btn_ajukan, btnDate, btnCancelDate, dialogBtnSubmit, dialogBtnCancel;
    private String kyano, token, nameImage = "", indikasiSakit = "", mulaiSakit = "", selasaiSakit = "", catatan = "", skd = "0", namaEmp, divisiEmp, jabatanEmp;
    private Toast toast;

    ArrayList<String> list = new ArrayList<String>();

    SessionManager session;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_sakit);

        imStatus = findViewById(R.id.imStatus);

        edit_tgl_sakit = findViewById(R.id.edit_tgl_sakit);
        edit_catatan = findViewById(R.id.edit_catatan);
        tx_input_indikasi = findViewById(R.id.tx_input_indikasi);
        tx_input_tgl_sakit = findViewById(R.id.tx_input_tgl_sakit);
        lin_transparant = findViewById(R.id.lin_transparant);
        llUploadSkd = findViewById(R.id.llUploadSkd);
        edit_IndikasiPenyakit = findViewById(R.id.edit_IndikasiPenyakit);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        image_surat_izin_sakit = findViewById(R.id.image_surat_izin_sakit);
        ivTgl = findViewById(R.id.ivTgl);
        tx_image_name = findViewById(R.id.tx_image_name);
        btn_ajukan = findViewById(R.id.btn_ajukan);

        session = new SessionManager(formIzinSakit.this);
        helper.verifyStoragePermissions(formIzinSakit.this);
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
         * START OPEN MULTIPLE CALENDAR
         * */

        llViewCalendar = findViewById(R.id.llViewCalendar);
        llViewGetDates = findViewById(R.id.llViewGetDates);

        tvGetDate = findViewById(R.id.tvGetDate);

        btnDate = findViewById(R.id.btnGetDates);
        btnCancelDate = findViewById(R.id.btnCancelDate);

        Calendar prevMonth = Calendar.getInstance();

        prevMonth.add(Calendar.MONTH, -1);

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(prevMonth.getTime(), today)
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSelected(Date date) {

                android.icu.text.SimpleDateFormat dateFormat = new android.icu.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                Toast.makeText(formIzinSakit.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

                Log.d("TAG_DATE_SELECTED", "onDateSelected: " + clearFormatDate);

                list.add(clearFormatDate);

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateUnselected(Date date) {
                android.icu.text.SimpleDateFormat dateFormat = new android.icu.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String clearFormatDate = dateFormat.format(date);
                System.out.println("Date :" + clearFormatDate);

                Toast.makeText(formIzinSakit.this, dateFormat.format(date), Toast.LENGTH_SHORT).show();

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
            edit_tgl_sakit.setText(list.toString().replace("[", "").replace("]", ""));
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

        cbSkdView = findViewById(R.id.cbSkd);
        cbSkdView.setOnClickListener(v -> {
            if (cbSkdView.isChecked()) {
                skd = "1";
                llUploadSkd.setVisibility(View.VISIBLE);
            } else {
                llUploadSkd.setVisibility(View.GONE);
            }
        });

        tx_input_tgl_sakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDateDialogTglSakit();
                calendar.setVisibility(View.VISIBLE);
                llViewCalendar.setVisibility(View.VISIBLE);
                llViewGetDates.setVisibility(View.VISIBLE);
            }
        });


        image_surat_izin_sakit.setOnClickListener(new View.OnClickListener() {
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
                chosedfile = new File(Objects.requireNonNull(getRealPathFormURI(imageUri, formIzinSakit.this)));
                resultUri = imageUri;
                image_surat_izin_sakit.setImageURI(resultUri);

                long fileSizeInBytes = chosedfile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Log.d("TAG_IMG_SIZE", "onActivityResult: "+fileSizeInBytes +" | "+fileSizeInKB+" | "+fileSizeInMB);

                if (chosedfile != null) {
                    if (fileSizeInMB < 1) {
                        Toast.makeText(formIzinSakit.this, "Ukuran foto adalah "+fileSizeInKB+" Kb", toast.LENGTH_SHORT).show();

                        Log.d("TAG1", "onActivityResult: "+chosedfile.toString());
                    } else {

                        File compressedImageFile =
                                new Compressor(this)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .compressToFile(chosedfile);

                        long fileSizeInBytesNew = compressedImageFile.length();
                        long fileSizeInKBNew = fileSizeInBytesNew / 1024;

                        Log.d("TAG2", "onActivityResult: Real: "+fileSizeInBytesNew+" | New: "+fileSizeInKBNew);

                        Toast.makeText(formIzinSakit.this, "Sekarang ukuran foto adalah "+fileSizeInKBNew+" Kb", toast.LENGTH_SHORT).show();
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
        indikasiSakit = edit_IndikasiPenyakit.getText().toString().trim();
        mulaiSakit = edit_tgl_sakit.getText().toString();
        //selasaiSakit = edit_tgl_selesai_sakit.getText().toString().trim();
        catatan = edit_catatan.getText().toString().trim();
        nameImage = tx_image_name.getText().toString();

        if (indikasiSakit.isEmpty()) {
            tx_input_indikasi.setError("indikasi sakit harus diisi");
            tx_input_indikasi.requestFocus();
        } else if (mulaiSakit.isEmpty()) {
            tx_input_tgl_sakit.setError("tanggal sakit harus diisi");
            tx_input_tgl_sakit.requestFocus();
        }/*else if(selasaiSakit.isEmpty()){
            tx_input_tgl_selesai_sakit.setError("tanggal mulai sakit harus diisi");
            tx_input_tgl_selesai_sakit.requestFocus();
        }*/ else if (chosedfile == null && cbSkdView.isChecked()) {
            Toast.makeText(formIzinSakit.this, "foto izin sakit belum disi", toast.LENGTH_SHORT).show();
        } else {
            //mProgressDialog.show();
            tx_input_indikasi.setErrorEnabled(false);
            tx_input_tgl_sakit.setErrorEnabled(false);
            //tx_input_tgl_selesai_sakit.setErrorEnabled(false);

            dialog_confirm();

        }
    }

    private void insertIzinSakit(String indikasiSakit, String catatan, String selectDate, File file, String option) {
        Log.d("TAG_INPUT_CEK", "insertIzinSakit: "+ indikasiSakit + catatan + selectDate + file + option);
        //AndroidNetworking.upload(api.URL_IzinSakit)
        AndroidNetworking.upload("http://192.168.50.24/all/hris_ci_3/api/izinsakit")
                .addHeaders("Authorization", "Bearer " + token)
                .addMultipartParameter("indikasi_sakit", indikasiSakit)
                .addMultipartParameter("catatan", catatan)
                .addMultipartParameter("select_date", selectDate)
                .addMultipartFile("lampiran_file", file)
                .addMultipartParameter("option", option)
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
                                Toast.makeText(formIzinSakit.this, "" + message, toast.LENGTH_SHORT).show();
                            } else if(status.equals("201")){
                                dialogConfirm.dismiss();
                                notifDialogSukses();
                                message = response.getString("message");
                                Toast.makeText(formIzinSakit.this, "" + message, toast.LENGTH_SHORT).show();
                            } else {
                                message = response.getString("message");
                                Toast.makeText(formIzinSakit.this, "" + message, toast.LENGTH_SHORT).show();
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

    private void notifDialogSukses() {
        dialogResign = new Dialog(formIzinSakit.this);
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
        dialogConfirm = new Dialog(formIzinSakit.this);
        dialogConfirm.setContentView(R.layout.dialog_confirm);

        dialogBtnSubmit = dialogConfirm.findViewById(R.id.btnSubmit);
        dialogBtnCancel = dialogConfirm.findViewById(R.id.btnCancel);

        dialogConfirm.setCancelable(true);
        dialogConfirm.setCanceledOnTouchOutside(false);

        dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: "+indikasiSakit+" | "+ catatan +" | "+ nameImage +" | "+ mulaiSakit +" | "+ skd);
                insertIzinSakit(indikasiSakit, catatan, mulaiSakit, chosedfile , skd);

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
}
