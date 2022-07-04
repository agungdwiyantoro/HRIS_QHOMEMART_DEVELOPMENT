package com.app.mobiledev.apphris.training.soal_training;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.DataSoalSQLite;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;
public class Latihan extends AppCompatActivity  {
    private List<ModelLatihan> mlLatihan;
    private TextView txHitSoal, txJmlSoal, txSoal, txA, txB, txC, txD,soal_temp,jawaban_temp;
    private Integer jml=0;
    private List<String> idSoal;
    private List<String> jawaban;
    private Button btnN, btnP;
    private DataSoalSQLite db;
    private LinearLayout cardLat_A,cardLat_B,cardLat_C,cardLat_D;
    private Integer i=0, currentPage=0;
    private CoordinatorLayout coordinator;
    private SessionManager sessionmanager;
    private String kyano;
    public String idsoal_temp="",jbnm_tmp="";
    private String idSoal_now="";
    private Boolean setJawaban=false;
    char[]  soal_array;
    char[] jawaban_array;
    private String getSetJawaban_a="",getSetJawaban_b="",getSetJawaban_c="",getSetJawaban_d="",getSetJawaban_uraian="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latihan);
        mlLatihan = new ArrayList<>();
        setTitle("Latihan");
        txHitSoal = findViewById(R.id.txHitSoal);
        txJmlSoal = findViewById(R.id.txJmlSoal);
        txSoal = findViewById(R.id.txSoal);
        idSoal= new ArrayList<>();
        jawaban= new ArrayList<>();
        db = new DataSoalSQLite(Latihan.this);
        txA = findViewById(R.id.txA);
        txB = findViewById(R.id.txB);
        txC = findViewById(R.id.txC);
        txD = findViewById(R.id.txD);
        btnN = findViewById(R.id.btn_next);
        btnP = findViewById(R.id.btn_prev);
        cardLat_A=findViewById(R.id.cardLat_A);
        cardLat_B=findViewById(R.id.cardLat_B);
        cardLat_C=findViewById(R.id.cardLat_C);
        cardLat_D=findViewById(R.id.cardLat_D);
        soal_temp=findViewById(R.id.soal_temp);
        jawaban_temp=findViewById(R.id.jawaban_temp);
        coordinator=findViewById(R.id.coordinator);
        sessionmanager = new SessionManager(Latihan.this);
        kyano=sessionmanager.getIdUser();
        Log.d("CEK_KYANO", "onCreate: "+kyano);
        deleteTrsoal();
        deleteTrjawaban();
        getSoal();
        Log.d("JAWABAN", "onResponse: "+jbnm_tmp);
        btnN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLat_A.setBackgroundResource(R.color.colorWhite);
                cardLat_B.setBackgroundResource(R.color.colorWhite);
                cardLat_C.setBackgroundResource(R.color.colorWhite);
                cardLat_D.setBackgroundResource(R.color.colorWhite);
                getDataJawabanSQL(idSoal_now);


               if(setJawaban){
                   if(currentPage<=(jml-1)){
                       setJawaban("belum selesai",idSoal_now,getSetJawaban_uraian,getSetJawaban_a,getSetJawaban_b,getSetJawaban_c,getSetJawaban_d);
                       simpanJawaban(idSoal_now);
                       currentPage++;
                       getDataSoalSQL(currentPage);
                   }
                   if(currentPage==(jml)){
                       btnN.setText("simpan");
                       simpanJwbTempo();
                   }

               }else{
                   helper.snackBar(coordinator,"Anda belum memilih jawaban.....!!!");
               }
                setJawaban=false;
            }

        });
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getJawaban();
                if(currentPage>=0){
                    currentPage--;
                    getDataSoalSQL(currentPage);
                    getDataJawabanSQL(idSoal_now);
                    setJawaban=true;
                }
            }
        });
        Log.d("CEK_IDSOAL_TEMP", "onCreate: "+idsoal_temp+" "+jbnm_tmp);




        txA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLat_A.setBackgroundResource(R.color.blue);
                cardLat_B.setBackgroundResource(R.color.colorWhite);
                cardLat_C.setBackgroundResource(R.color.colorWhite);
                cardLat_D.setBackgroundResource(R.color.colorWhite);
                setPilihan("A","","","","");

            }
        });

        txB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLat_B.setBackgroundResource(R.color.blue);
                cardLat_A.setBackgroundResource(R.color.colorWhite);
                cardLat_C.setBackgroundResource(R.color.colorWhite);
                cardLat_D.setBackgroundResource(R.color.colorWhite);
                setPilihan("","B","","","");

            }
        });

        txC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLat_C.setBackgroundResource(R.color.blue);
                cardLat_A.setBackgroundResource(R.color.colorWhite);
                cardLat_B.setBackgroundResource(R.color.colorWhite);
                cardLat_D.setBackgroundResource(R.color.colorWhite);
                setPilihan("","","C","","");

            }
        });

        txD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLat_D.setBackgroundResource(R.color.blue);
                cardLat_A.setBackgroundResource(R.color.colorWhite);
                cardLat_B.setBackgroundResource(R.color.colorWhite);
                cardLat_C.setBackgroundResource(R.color.colorWhite);
                setPilihan("","","","D","");

            }
        });





    }


    @Override
    public void onBackPressed() {
        new SweetAlertDialog(Latihan.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Konfirmasi keluar")
                .setContentText("Yakin keluar Latihan?")
                .setConfirmText("Ya")
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                }).show();
    }




    public void getDataJawabanSQL(String idsoal){
        SQLiteDatabase ReadData = db.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("select * from trjawaban where id_soal='" + idsoal + "'", null);
        cursor.moveToFirst();
        String[] str = new String[15];
        mlLatihan.clear();

        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);
            Log.d("DATA_CEK_CEK", "getDataSoalSQL: "+cursor.getString(1));
            if(cursor.getString(1).equals(""+idsoal)){
                if(cursor.getString(2).equals("A")||cursor.getString(2).equals("a")){
                    cardLat_A.setBackgroundResource(R.color.blue);
                    cardLat_B.setBackgroundResource(R.color.colorWhite);
                    cardLat_C.setBackgroundResource(R.color.colorWhite);
                    cardLat_D.setBackgroundResource(R.color.colorWhite);
                    setPilihan("A","","","","");
                }
                else if(cursor.getString(2).equals("B")||cursor.getString(2).equals("b")){
                    cardLat_B.setBackgroundResource(R.color.blue);
                    cardLat_A.setBackgroundResource(R.color.colorWhite);
                    cardLat_C.setBackgroundResource(R.color.colorWhite);
                    cardLat_D.setBackgroundResource(R.color.colorWhite);
                    setPilihan("","B","","","");

                }
                else if(cursor.getString(2).equals("C")||cursor.getString(2).equals("c")){
                    cardLat_C.setBackgroundResource(R.color.blue);
                    cardLat_A.setBackgroundResource(R.color.colorWhite);
                    cardLat_B.setBackgroundResource(R.color.colorWhite);
                    cardLat_D.setBackgroundResource(R.color.colorWhite);
                    setPilihan("","","C","","");

                }
                else{
                    cardLat_D.setBackgroundResource(R.color.blue);
                    cardLat_A.setBackgroundResource(R.color.colorWhite);
                    cardLat_B.setBackgroundResource(R.color.colorWhite);
                    cardLat_C.setBackgroundResource(R.color.colorWhite);
                    setPilihan("","","","D","");

                }


            }


        }

        try {
            for (int ii = 0; ii <= idSoal.size(); ii++) {
                Log.d("TEST_ID_SOAL", "onCreate: " + idSoal.get(ii));
            }
            Log.d("DATA_GETSOAL:", "getDataSoalSQL: "+cursor.getString(2));

        } catch (IndexOutOfBoundsException e) {
            Log.d("Errror", "onCreate: " + e);
        }


    }

    public void  getDataSoalSQL(int idsoal) {
        SQLiteDatabase ReadData = db.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("select * from trsoal where id_auto='" + idsoal + "'", null);
        cursor.moveToFirst();
        String[] str = new String[15];
        mlLatihan.clear();

        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);
            Log.d("DATA_CEK_CEK", "getDataSoalSQL: "+cursor.getString(1));
            txHitSoal.setText((idsoal+1)+"/"+jml);
            txSoal.setText(""+cursor.getString(2));
            txA.setText("A."+cursor.getString(3));
            txB.setText("B."+cursor.getString(4));
            txC.setText("C."+cursor.getString(5));
            txD.setText("D."+cursor.getString(6));
            idSoal_now=cursor.getString(1);


        }

        try {
            for (int ii = 0; ii <= idSoal.size(); ii++) {
                Log.d("TEST_ID_SOAL", "onCreate: " + idSoal.get(ii));
            }
            Log.d("DATA_GETSOAL:", "getDataSoalSQL: "+cursor.getString(2));

        } catch (IndexOutOfBoundsException e) {
            Log.d("Errror", "onCreate: " + e);
        }

    }


    private void getSoal(){
        AndroidNetworking.post(api.URL_getsoal)
                .addBodyParameter("key", api.key)
                .addBodyParameter("no_training", "HR004")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            jml = Integer.parseInt(response.getString("jml"));
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                soal_array=new char[jsonArray.length()];
                                jawaban_array=new char[jsonArray.length()];
                                mlLatihan.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    ModelLatihan model = new ModelLatihan();
                                    model.setId_soal(data.getString("id_soal"));
                                    model.setSoal(data.getString("soal"));
                                    model.setA(data.getString("a"));
                                    model.setB(data.getString("b"));
                                    model.setC(data.getString("c"));
                                    model.setD(data.getString("d"));
                                    model.setKunci(data.getString("kunci"));
                                    model.setTanggal(data.getString("tanggal"));
                                    model.setAktif(data.getString("aktif"));
                                    soal_array[i] =data.getString("id_soal").charAt(0);
                                    jawaban_array[i] ="0".charAt(0);
                                    simpan(model,i);
                                }
                                getDataSoalSQL(0);


                            } else {
                                helper.showMsg(Latihan.this,"Informasi","anda belum dapat soal",helper.WARNING_TYPE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(Latihan.this, "Peringatan", helper.PESAN_SERVER, helper.ERROR_TYPE);

                        }catch (NumberFormatException e){
                            Log.d("NUMBER_FORMAT", "onResponse: "+e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(Latihan.this, "Peringatan",  helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        //mProgressDialog.dismiss();

                    }
                });

    }
    public void simpan(ModelLatihan model,int index) {
        DataSoalSQLite mydatabase = new DataSoalSQLite(Latihan.this);
        SQLiteDatabase db = mydatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_auto", index);
        contentValues.put("id_soal", model.getId_soal());
        contentValues.put("soal", model.getSoal());
        contentValues.put("a", model.getA());
        contentValues.put("b", model.getB());
        contentValues.put("c",model.getC());
        contentValues.put("d",model.getD());
        contentValues.put("kunci", model.getKunci());
        contentValues.put("tanggal", model.getTanggal());
        contentValues.put("aktif",model.getAktif());

        Log.d("SOAL_COBA", "simpan: "+model.getSoal());
        long result = db.insert("trsoal", null, contentValues);
        if (result != -1) {
            Log.d("simpan_sqlite", "simpan: sukses");
        } else {
            Log.d("simpan_sqlite", "simpan: gagal");
        }
    }


    public void simpanJawaban(final String idsoal){
        DataSoalSQLite mydatabase = new DataSoalSQLite(Latihan.this);
        SQLiteDatabase db = mydatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_soal", idsoal);
        if(!getSetJawaban_a.equals("") && getSetJawaban_b.equals("") && getSetJawaban_c.equals("") && getSetJawaban_d.equals("")){
            contentValues.put("jawaban", getSetJawaban_a);
        }
        else if(!getSetJawaban_b.equals("") && getSetJawaban_a.equals("") && getSetJawaban_c.equals("") && getSetJawaban_d.equals("")){
            contentValues.put("jawaban", getSetJawaban_b);
        }
        else if(!getSetJawaban_c.equals("") && getSetJawaban_a.equals("") && getSetJawaban_b.equals("") && getSetJawaban_d.equals("")){
            contentValues.put("jawaban", getSetJawaban_c);
        }else{
            contentValues.put("jawaban", getSetJawaban_d);
        }

        long result = db.insert("trjawaban", null, contentValues);
        if (result != -1) {
            Log.d("simpan_sqlite", "simpan: sukses");
        } else {
            Log.d("simpan_sqlite", "simpan: gagal");
        }

    }

    private void deleteTrsoal(){
        DataSoalSQLite getDatabase = new DataSoalSQLite(Latihan.this);
        String deleteQuery = "DELETE FROM trsoal";
        SQLiteDatabase DeleteData = getDatabase.getWritableDatabase();
        DeleteData.execSQL(deleteQuery);
        DeleteData.close();
    }

    private void deleteTrjawaban(){
        DataSoalSQLite getDatabase = new DataSoalSQLite(Latihan.this);
        String deleteQuery = "DELETE FROM trjawaban";
        SQLiteDatabase DeleteData = getDatabase.getWritableDatabase();
        DeleteData.execSQL(deleteQuery);
        DeleteData.close();
    }

    private void setJawaban(final String status, final String idsoal, final String jbnm_uraian,String jbnm_a,final String jbnm_b,final String jbnm_c,final String jbnm_d){
        AndroidNetworking.post(api.URL_setJawabTemp)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("idsoal", idsoal)
                .addBodyParameter("status", status)
                .addBodyParameter("jbnm_a", jbnm_a)
                .addBodyParameter("jbnm_b", jbnm_b)
                .addBodyParameter("jbnm_c", jbnm_c)
                .addBodyParameter("jbnm_d", jbnm_d)
                .addBodyParameter("jbnm_uraian", jbnm_uraian)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String ket = response.getString("ket");
                            if (success) {
                                idsoal_temp=response.getString("soal");
                                jbnm_tmp=response.getString("jawaban");
                                Log.d("cek_sukses", "onResponse: "+success+" "+ket );


                            } else {
                                Log.d("cek_sukses", "onResponse: "+success+" "+ket );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_JWBAN", "onResponse: "+e);
                            helper.showMsg(Latihan.this, "Peringatan",  helper.PESAN_SERVER, helper.ERROR_TYPE);

                        }catch (NumberFormatException e){
                            Log.d("JSON_JWBAN", "onResponse: "+e);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(Latihan.this, "Peringatan", helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("JSON_JWBAN", "onError: "+anError);
                        //mProgressDialog.dismiss();

                    }
                });
    }


    private void simpanJwbTempo(){
        AndroidNetworking.post(api.URL_simpanJwbTempo)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String ket = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(Latihan.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText(""+ket)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                helper.showMsg(Latihan.this,"informasi",""+ket);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_JWBAN", "onResponse: "+e);
                            helper.showMsg(Latihan.this, "Peringatan",  helper.PESAN_SERVER, helper.ERROR_TYPE);

                        }catch (NumberFormatException e){
                            Log.d("JSON_JWBAN", "onResponse: "+e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(Latihan.this, "Peringatan",  helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("JSON_JWBAN", "onError: "+anError);
                    }
                });
            }

    public void setPilihan(String a, String b, String c, String d,String uraian){
        getSetJawaban_a=a;
        getSetJawaban_b=b;
        getSetJawaban_c=c;
        getSetJawaban_d=d;
        getSetJawaban_uraian=uraian;
        setJawaban=true;
    }







}
