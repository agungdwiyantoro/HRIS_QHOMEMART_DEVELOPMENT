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

import id.zelory.compressor.Compressor;

public class formIzinSakit extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText edit_tgl_sakit, edit_tgl_selesai_sakit, edit_IndikasiPenyakit, edit_catatan;
    private ImageView image_surat_izin_sakit, ivTgl;
    private TextView tx_image_name, txClose, tvGetDate;
    private TextInputLayout tx_input_indikasi, tx_input_tgl_sakit, tx_input_tgl_selesai_sakit;
    private Dialog dialogResign;
    private LinearLayout lin_transparant, llUploadSkd, llViewCalendar, llViewGetDates;
    private CheckBox cbSkdView;

    private Uri resultUri;
    private File chosedfile;
    private Bitmap image_bmap;
    private Button btn_ajukan, btnDate, btnCancelDate;
    private String kyano, token, nameImage = "", indikasiSakit = "", mulaiSakit = "", selasaiSakit = "", catatan = "", skd = "0";
    private Toast toast;

    ArrayList<String> list = new ArrayList<String>();

    SessionManager session;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_sakit);
        edit_tgl_sakit = findViewById(R.id.edit_tgl_sakit);
        //edit_tgl_selesai_sakit=findViewById(R.id.edit_tgl_selesai_sakit);
        edit_catatan = findViewById(R.id.edit_catatan);
        tx_input_indikasi = findViewById(R.id.tx_input_indikasi);
        tx_input_tgl_sakit = findViewById(R.id.tx_input_tgl_sakit);
        lin_transparant = findViewById(R.id.lin_transparant);
        llUploadSkd = findViewById(R.id.llUploadSkd);
        //tx_input_tgl_selesai_sakit=findViewById(R.id.tx_input_tgl_selesai_sakit);
        edit_IndikasiPenyakit = findViewById(R.id.edit_IndikasiPenyakit);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        image_surat_izin_sakit = findViewById(R.id.image_surat_izin_sakit);
        ivTgl = findViewById(R.id.ivTgl);
        tx_image_name = findViewById(R.id.tx_image_name);
        btn_ajukan = findViewById(R.id.btn_ajukan);
        //edit_tgl_sakit.setInputType(InputType.TYPE_NULL);
        //edit_tgl_selesai_sakit.setInputType(InputType.TYPE_NULL);
        session = new SessionManager(formIzinSakit.this);
        helper.verifyStoragePermissions(formIzinSakit.this);
        kyano = session.getIdUser();
        token = session.getToken();

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
                chosedfile = new File(getRealPathFormURI(imageUri, formIzinSakit.this));
                resultUri = imageUri;
                image_surat_izin_sakit.setImageURI(resultUri);

                long fileSizeInBytes = chosedfile.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;

                Log.d("TAG_IMG_SIZE", "onActivityResult: "+fileSizeInBytes +" | "+fileSizeInKB+" | "+fileSizeInMB);

                if (chosedfile != null) {
                    if (fileSizeInMB < 1) {
                        Toast.makeText(formIzinSakit.this, "Ukuran foto adalah "+fileSizeInKB+" Kb", toast.LENGTH_SHORT).show();
                        uploadIzinSakit(chosedfile, token);

                        Log.d("TAG1", "onActivityResult: "+chosedfile.toString());
                    } else {

                        File compressedImageFile = new Compressor(this).compressToFile(chosedfile);

                        long fileSizeInBytesNew = compressedImageFile.length();
                        long fileSizeInKBNew = fileSizeInBytesNew / 1024;

                        Log.d("TAG2", "onActivityResult: Real: "+fileSizeInBytesNew+" | New: "+fileSizeInKBNew);

                        uploadIzinSakit(compressedImageFile, token);

                        Toast.makeText(formIzinSakit.this, "Sekarang ukuran foto adalah "+fileSizeInKBNew+" Kb", toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.d("TAKE_GALERI", "onActivityResult: " + e);
        }


    }

    public void uploadIzinSakit(File file, String token) {
        AndroidNetworking.upload(api.URL_uploadIzinSakit)
                .addHeaders("Authorization", "Bearer " + token)
                //.addHeaders("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjA2NTIyMDA1MTMwNTEyOTYiLCJreXBhc3N3b3JkIjoiMTIzNDU2NyIsImlhdCI6MTY0NTQ5NzEwMywiZXhwIjoxNjQ1NTE1MTAzfQ.WXlzgJadd0c1yZtgWp-CD5rgtmcsgO1ecgYOTUVI07Y")
                .addMultipartFile("lampiran_file", file)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        tx_image_name.setVisibility(View.VISIBLE);
                        nameImage = tx_image_name.getText().toString();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("HASIL_RESPONSE", "onResponse: " + response);
                            String status = response.getString("status");

                            if (status.equals("200")) {
                                JSONObject object = response.getJSONObject("message");
                                nameImage = object.getString("file_name");
                                tx_image_name.setVisibility(View.VISIBLE);
                                tx_image_name.setText("" + nameImage);
                                Toast.makeText(formIzinSakit.this, "Upload SKD berhasil", toast.LENGTH_SHORT).show();

                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                                Toast.makeText(formIzinSakit.this, "" + pesan, toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_UPLOAD", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UPLOAD", "onError: " + anError);
                    }
                });
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

    private void insertIzinSakit(String indikasiSakit, String catatan, String namaFile, String selectDate, String option) {
        AndroidNetworking.post(api.URL_IzinSakit)
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("indikasi_sakit", indikasiSakit)
                /*.addBodyParameter("mulai_sakit", mulaiSakit)
                .addBodyParameter("selesai_sakit", selesaiSakit)*/
                .addBodyParameter("catatan", catatan)
                .addBodyParameter("nama_file", namaFile)
                .addBodyParameter("select_date", selectDate)
                .addBodyParameter("option", option)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lin_transparant.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_INSERT", "onResponse: " + response);
                            if (status.equals("201")) {
                                String message = response.getString("message");
                                notifDialogSukses();
                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");
                                Toast.makeText(formIzinSakit.this, "" + pesan, toast.LENGTH_SHORT).show();
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
                        Log.d("EROOR_insert", "onError: " + anError);
                    }
                });

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
        }*/ else if (nameImage.isEmpty() || chosedfile == null) {
            Toast.makeText(formIzinSakit.this, "foto izin sakit belum disii", toast.LENGTH_SHORT).show();
        } else {
            //mProgressDialog.show();
            tx_input_indikasi.setErrorEnabled(false);
            tx_input_tgl_sakit.setErrorEnabled(false);
            //tx_input_tgl_selesai_sakit.setErrorEnabled(false);
            lin_transparant.setVisibility(View.VISIBLE);
            Log.d("TAGTAG_PARAMETER", "cekInputFormInsert: "+indikasiSakit+" | "+ catatan +" | "+ nameImage +" | "+ edit_tgl_sakit.getText().toString() +" | "+ skd);
            insertIzinSakit(indikasiSakit, catatan, nameImage, edit_tgl_sakit.getText().toString().trim() , skd);
            //auth_user(id_user, password);
        }
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
}
