package com.app.mobiledev.apphris.newIzin.izinSakit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.lang.UScript;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.app.mobiledev.apphris.login;
import com.app.mobiledev.apphris.main_fragment;
import com.app.mobiledev.apphris.profile.UpdateDataDiri;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class formIzinSakit extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText edit_tgl_sakit;
    private EditText edit_tgl_selesai_sakit;
    private ImageView image_surat_izin_sakit;
    private EditText edit_IndikasiPenyakit;
    private EditText edit_catatan;
    private TextView tx_image_name;
    private TextInputLayout tx_input_indikasi;
    private TextInputLayout tx_input_tgl_sakit;
    private TextInputLayout tx_input_tgl_selesai_sakit;
    private Dialog dialogResign;
    private TextView txClose;
    private LinearLayout lin_transparant;

    private Uri resultUri;
    private File chosedfile;
    private Bitmap image_bmap;
    private Button btn_ajukan;
    private String nameImage="";
    private String indikasiSakit="";
    private String mulaiSakit="";
    private String selasaiSakit="";
    private String catatan="";
    private Toast toast;


    SessionManager session;
    String kyano;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_izin_sakit);
        edit_tgl_sakit=findViewById(R.id.edit_tgl_sakit);
        edit_tgl_selesai_sakit=findViewById(R.id.edit_tgl_selesai_sakit);
        edit_catatan=findViewById(R.id.edit_catatan);
        tx_input_indikasi=findViewById(R.id.tx_input_indikasi);
        tx_input_tgl_sakit=findViewById(R.id.tx_input_tgl_sakit);
        lin_transparant=findViewById(R.id.lin_transparant);
        tx_input_tgl_selesai_sakit=findViewById(R.id.tx_input_tgl_selesai_sakit);
        edit_IndikasiPenyakit=findViewById(R.id.edit_IndikasiPenyakit);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        image_surat_izin_sakit=findViewById(R.id.image_surat_izin_sakit);
        tx_image_name=findViewById(R.id.tx_image_name);
        btn_ajukan=findViewById(R.id.btn_ajukan);
        edit_tgl_sakit.setInputType(InputType.TYPE_NULL);
        edit_tgl_selesai_sakit.setInputType(InputType.TYPE_NULL);
        session=new SessionManager(formIzinSakit.this);
        helper.verifyStoragePermissions(formIzinSakit.this);
        kyano=session.getIdUser();
        token=session.getToken();


        edit_tgl_sakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialogTglSakit();
            }
        });
        edit_tgl_selesai_sakit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialogTglSelesaiSakit();
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


    private void showDateDialogTglSakit(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(formIzinSakit.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edit_tgl_sakit.setText(""+dateFormatter.format(newDate.getTime()));


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showDateDialogTglSelesaiSakit(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(formIzinSakit.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edit_tgl_selesai_sakit.setText(""+dateFormatter.format(newDate.getTime()));


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == 1 && resultCode == Activity.RESULT_OK){
                final Uri imageUri = data.getData();
                chosedfile=new File(getRealPathFormURI(imageUri,formIzinSakit.this));
                resultUri = imageUri;
                image_surat_izin_sakit.setImageURI(resultUri);
                if(chosedfile!=null){
                    upluoadIzinSakit(chosedfile,token);
                }


              //  encodedimage= Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
            }
        }catch (Exception e){
            Log.d("TAKE_GALERI", "onActivityResult: "+e);
        }


    }



    public void upluoadIzinSakit(File file, String token) {
        AndroidNetworking.upload(api.URL_uploadIzinSakit)
                .addHeaders("Authorization", "Bearer "+token)
                .addMultipartFile("lampiran_file",file)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        tx_image_name.setVisibility(View.VISIBLE);
                        nameImage=tx_image_name.getText().toString();
                        if(nameImage.equals("")||nameImage.isEmpty()){
                            Toast.makeText(formIzinSakit.this,"foto kurang dari 1 mb",toast.LENGTH_SHORT).show();
                            //break;


                        }else{
                            Toast.makeText(formIzinSakit.this,"upluoad: "+bytesUploaded+" total"+totalBytes,toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("HASL_RESPONSE", "onResponse: "+response);
                            String status = response.getString("status");

                            if(status.equals("200")){
                                JSONObject object = response.getJSONObject("message");
                                nameImage = object.getString("file_name");
                                tx_image_name.setVisibility(View.VISIBLE);
                                tx_image_name.setText(""+nameImage);
                                Toast.makeText(formIzinSakit.this,"berhasil upluoad",toast.LENGTH_SHORT).show();

                            }else{
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                                Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
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


    private  String getRealPathFormURI(Uri contenturi,Activity context){
        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor =context.managedQuery(contenturi,projection,null,null,null);
        if(cursor==null)
            return null;
        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        if(cursor.moveToFirst()){
            String s=cursor.getString(column_index);
            return s;
        }

        return  null;
    }


    private void insertIzinSakit(String indikasiSakit,String mulaiSakit,String selesaiSakit,String catatan,String namaFile){
        AndroidNetworking.post(api.URL_IzinSakit)
                .addHeaders("Authorization", "Bearer "+token)
                .addBodyParameter("indikasi_sakit",indikasiSakit)
                .addBodyParameter("mulai_sakit",mulaiSakit)
                .addBodyParameter("selesai_sakit",selesaiSakit)
                .addBodyParameter("catatan",catatan)
                .addBodyParameter("nama_file",namaFile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lin_transparant.setVisibility(View.GONE);
                        try {
                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_INSERT", "onResponse: "+response);
                            if(status.equals("201")){
                                String message=response.getString("message");
                                notifDialogSukses();
                            }else{
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");
                                Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
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


    private void cekInputFormInsert(){
        indikasiSakit = edit_IndikasiPenyakit.getText().toString().trim();
        mulaiSakit = edit_tgl_sakit.getText().toString().trim();
        selasaiSakit = edit_tgl_selesai_sakit.getText().toString().trim();
        catatan = edit_catatan.getText().toString().trim();
        nameImage=tx_image_name.getText().toString();

        if (indikasiSakit.isEmpty()) {
            tx_input_indikasi.setError("indikasi sakit harus diisi");
            tx_input_indikasi.requestFocus();
        } else if (mulaiSakit.isEmpty()) {
            tx_input_tgl_sakit.setError("tanggal mulai sakit harus diisi");
            tx_input_tgl_sakit.requestFocus();
        }else if(selasaiSakit.isEmpty()){
            tx_input_tgl_selesai_sakit.setError("tanggal mulai sakit harus diisi");
            tx_input_tgl_selesai_sakit.requestFocus();
        }else if(nameImage.isEmpty() || chosedfile==null){
            Toast.makeText(formIzinSakit.this,"foto izin sakit belum disii",toast.LENGTH_SHORT).show();
        }
        else{
            //mProgressDialog.show();
            tx_input_indikasi.setErrorEnabled(false);
            tx_input_tgl_sakit.setErrorEnabled(false);
            tx_input_tgl_selesai_sakit.setErrorEnabled(false);
            lin_transparant.setVisibility(View.VISIBLE);
            insertIzinSakit(indikasiSakit,mulaiSakit,selasaiSakit,catatan,nameImage);
            //auth_user(id_user, password);
        }
    }


    private void notifDialogSukses()  {
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
