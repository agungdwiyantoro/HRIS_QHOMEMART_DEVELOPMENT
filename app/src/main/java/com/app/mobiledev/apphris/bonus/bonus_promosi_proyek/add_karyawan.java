package com.app.mobiledev.apphris.bonus.bonus_promosi_proyek;

import android.app.Dialog;
import android.content.DialogInterface;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class add_karyawan extends DialogFragment {

    private RecyclerView rcKaryawan;
    private List<model_add_karywan> model_add_karyawans;
    private static CoordinatorLayout coor_add_karyawan;
    private EditText edCari;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private void refresh_data() {
        ((form_bonus_proyek)this.getActivity()).getDataKaryawanSQL();
    }

    public static void pesanBerhasil(){
        helper.snackBar(coor_add_karyawan,"karyawan berhasil ditambahkan..!!");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_add_karyawan, container, false);
        rcKaryawan = view.findViewById(R.id.rcKaryawan);
        model_add_karyawans = new ArrayList<>();
        coor_add_karyawan = view.findViewById(R.id.coor_add_karyawan);
        edCari = view.findViewById(R.id.edCari);
        edCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cari = edCari.getText().toString();
                try {
                    if (!cari.equals(null)) {
                        getBonus("" + cari);
                    } else {
                        helper.snackBar(coor_add_karyawan, "data yang dicari belum diisi...!!");
                    }

                } catch (NullPointerException e) {
                    helper.snackBar(coor_add_karyawan, "data yang dicari tidak ada..!!");
                }

            }
        });

        getBonus("");
        return view;
    }




    private void getBonus(final String cari){
        AndroidNetworking.post(api.URL_getmskaryawan)
                .addBodyParameter("key", api.key)
                .addBodyParameter("cari", cari)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        model_add_karyawans.clear();
                        try {
                            Boolean success = response.getBoolean("status");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    model_add_karywan model = new model_add_karywan();
                                    String kyano=data.getString("kyano");
                                    String kynm=data.getString("kynm");
                                    String dvnama=data.getString("dvnama");
                                    model.setKynm(kynm);
                                    model.setKyano(kyano);
                                    model.setDvnama(dvnama);
                                    model_add_karyawans.add(model);
                                }
                                adapter_add_karyawan mAdapter;
                                mAdapter = new adapter_add_karyawan(model_add_karyawans, getActivity(),"");
                                rcKaryawan.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rcKaryawan.setItemAnimator(new DefaultItemAnimator());
                                rcKaryawan.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();



                            }else{
                                String pesan = response.getString("data");
                                helper.snackBar(coor_add_karyawan,""+pesan);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.snackBar(coor_add_karyawan,""+ helper.PESAN_SERVER);
                        }catch (NullPointerException e){
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.snackBar(coor_add_karyawan,""+ helper.PESAN_SERVER);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        helper.snackBar(coor_add_karyawan,""+ helper.PESAN_KONEKSI);
                    }
                });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        refresh_data();
    }



}
