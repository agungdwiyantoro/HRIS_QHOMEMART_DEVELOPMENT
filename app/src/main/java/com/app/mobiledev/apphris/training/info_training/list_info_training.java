package com.app.mobiledev.apphris.training.info_training;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class list_info_training extends AppCompatActivity {
    private SessionManager sessionmanager;
    private ProgressDialog mProgressDialog;
    private RecyclerView rcInfoTraining;
    private  String kyano,nama;
    private Toolbar mToolbar;
    private List<model_training> modelTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_info_training);
        sessionmanager = new SessionManager(list_info_training.this);
        kyano=sessionmanager.getIdUser();
        nama=sessionmanager.getNamaLEngkap();
        mToolbar = findViewById(R.id.toolbar_list_training);
        modelTraining = new ArrayList<>();
        rcInfoTraining=findViewById(R.id.rcInfoTraining);
        listInfoTraining();
    }



    private void listInfoTraining(){
        AndroidNetworking.post(api.URL_getInfoTraining)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    model_training model = new model_training();

                                    model.setTnano(data.getString("tnano"));
                                    model.setTntgl(data.getString("tntgl"));
                                    model.setMateri(data.getString("materi"));
                                    model.setHasil(data.getString("hasil"));
                                    model.setTrainer(data.getString("trainer"));
                                    model.setSelesai(data.getString("selesai"));
                                    model.setPeserta(data.getString("peserta"));
                                    model.setKyano(data.getString("kyano"));
                                    Log.d("TEST_MATERI", "onBindViewHolder: "+data.getString("materi"));
                                    modelTraining.add(model);
                                }
                                adapterInfoTraining mAdapter;
                                mAdapter = new adapterInfoTraining(modelTraining, list_info_training.this);
                                mAdapter.notifyDataSetChanged();
                                rcInfoTraining.setLayoutManager(new LinearLayoutManager(list_info_training.this));
                                rcInfoTraining.setItemAnimator(new DefaultItemAnimator());
                                rcInfoTraining.setAdapter(mAdapter);

                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORTRAIN", "onResponse: "+e);
                            helper.showMsg(list_info_training.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }catch (NullPointerException e){
                            Log.d("JSONERORTRAIN", "onResponse: "+e);
                        }catch (NumberFormatException e){
                            Log.d("JSONERORTRAIN", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(list_info_training.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_TRAIN", "onError: "+anError);
                        mProgressDialog.dismiss();


                    }
                });
    }

    @Override
    public void onBackPressed() {
      finish();
    }
}
