package com.app.mobiledev.apphris.memo.listMemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.memo.ModelMemo;
import com.app.mobiledev.apphris.memo.adapterMemoList;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class memo_list extends AppCompatActivity {
    private List<ModelMemo> mlistMemo;
    private RecyclerView rvMemo;
    private EditText edCari;
    private Toolbar mToolbar;
    private SessionManager sessionmanager;
    private String kyano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        rvMemo=findViewById(R.id.rvMemo);
        edCari=findViewById(R.id.edCari);
        mToolbar = findViewById(R.id.toolbar_memo_list);
        mToolbar.setTitle("List Memo");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());
        sessionmanager = new SessionManager(memo_list.this);
        kyano=sessionmanager.getIdUser();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        edCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cari=edCari.getText().toString();
                getMemo(cari);

            }
        });
        mlistMemo = new ArrayList<>();
        getMemo("");
    }


    private void getMemo(String cari){
        AndroidNetworking.post(api.URL_getMemo)
                .addBodyParameter("key", api.key)
                .addBodyParameter("cari", cari)
                .addBodyParameter("kyano", kyano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Boolean success = response.getBoolean("success");
                            Log.d("cek_memo_liost", "onResponse: "+response.getBoolean("success"));
                            if (success) {
                                mlistMemo.clear();
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    ModelMemo model = new ModelMemo();
                                    model.setMmANo(data.getString("MmANo"));
                                    model.setJns(data.getString("jns"));
                                    model.setMo(data.getString("no"));
                                    model.setTgl(data.getString("tgl"));
                                    model.setHal(data.getString("hal"));
                                    model.setKpd(data.getString("kepada"));
                                    model.setDari(data.getString("dari"));
                                    model.setIsi(data.getString("isi"));
                                    model.setPdf(data.getString("pdf"));
                                    model.setVideo(data.getString("video"));
                                    Log.d("cek_video", "onClick: "+data.getString("video"));
                                    model.setMemo_baru(data.getString("memo_baru"));
                                    if(data.getString("jns").equals("Notulen Meeting")){
                                        model.setWkt(data.getString("wkt"));
                                        model.setTempat(data.getString("tempat"));
                                    }

                                    mlistMemo.add(model);
                                }
                                adapterMemoList mAdapter = new adapterMemoList(mlistMemo, memo_list.this);
                                mAdapter.notifyDataSetChanged();
                                rvMemo.setVisibility(View.VISIBLE);
                                rvMemo.setLayoutManager(new LinearLayoutManager(memo_list.this, LinearLayoutManager.VERTICAL,false));
                                rvMemo.setItemAnimator(new DefaultItemAnimator());
                                rvMemo.setAdapter(mAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONEEVENTT", "onResponse: "+e);
                            helper.showMsg(memo_list.this, "Peringatan", ""+ helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EVENT", "onError: "+anError);
                        helper.showMsg(memo_list.this, "Peringatan", ""+ helper.PESAN_SERVER, helper.ERROR_TYPE);
                    }
                });

    }
}
