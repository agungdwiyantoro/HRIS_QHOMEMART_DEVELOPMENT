package com.app.mobiledev.apphris.approve;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.approve.approveSakitNew.ListInfinitySakitApprove;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class menu_approve extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    CardView cvSakitApproveHead, cvDinasApproveHead, cvTinggalTugasApproveHead, cvCutiApproveHead,
            cvSakitApproveExec, cvDinasApproveExec, cvTinggalTugasApproveExec, cvCutiApproveExec,
            cvSakitApproveDirect, cvDinasApproveDirect, cvTinggalTugasApproveDirect, cvCutiApproveDirect,
            cvSakitApproveHRD, cvDinasApproveHRD, cvTinggalTugasApproveHRD, cvCutiApproveHRD;
    private SessionManager session;
    private String token, spinSelected, spinResult="HRD", noJabatan, appHead, appExec, appDirect, appHRD, hak_akses="";
    private SessionManager msession;
    private Spinner dropdown;
    private View incPageBack;
    private LinearLayout llSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_approve);

        msession = new SessionManager(menu_approve.this);
        token = msession.getToken();

        incPageBack = findViewById(R.id.incPageBack);

        hak_akses = getIntent().getExtras().getString("hak_akses");

        Log.d("TAG_BUNDLE", "onCreate: "+hak_akses);

        noJabatan = msession.getNoJabatan();

        llSpinner = findViewById(R.id.llSpinner);

        cvSakitApproveHead = findViewById(R.id.cvSakitApproveHead);
        cvCutiApproveHead = findViewById(R.id.cvCutiApproveHead);
        cvDinasApproveHead = findViewById(R.id.cvDinasApproveHead);
        cvTinggalTugasApproveHead = findViewById(R.id.cvTinggalTugasApproveHead);

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinDDown);
//create a list of items for the spinner.
        String[] items = new String[]{"HRD", "Eksekutif", "Direktur"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        if (hak_akses.equals("HRD,EXECUTIV")) {
            llSpinner.setVisibility(View.VISIBLE);
        }

        /*cvSakitApproveExec = findViewById(R.id.cvSakitApproveExec);
        cvCutiApproveExec = findViewById(R.id.cvCutiApproveExec);
        cvDinasApproveExec = findViewById(R.id.cvDinasApproveExec);
        cvTinggalTugasApproveExec = findViewById(R.id.cvTinggalTugasApproveExec);

        cvSakitApproveDirect = findViewById(R.id.cvSakitApproveDirect);
        cvCutiApproveDirect = findViewById(R.id.cvCutiApproveDirect);
        cvDinasApproveDirect = findViewById(R.id.cvDinasApproveDirect);
        cvTinggalTugasApproveDirect = findViewById(R.id.cvTinggalTugasApproveDirect);

        cvSakitApproveHRD = findViewById(R.id.cvSakitApproveHRD);
        cvCutiApproveHRD = findViewById(R.id.cvCutiApproveHRD);
        cvDinasApproveHRD = findViewById(R.id.cvDinasApproveHRD);
        cvTinggalTugasApproveHRD = findViewById(R.id.cvTinggalTugasApproveHRD);*/

        /*checkProduct();
        checkJabatan();*/

        incPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinSelected = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                switch (spinSelected) {
                    case "HRD":
                        spinResult = "HRD";
                        onRefresh();
                        break;
                    case "Eksekutif":
                        spinResult = "EXECUTIV";
                        onRefresh();
                        break;
                    case "Direktur":
                        spinResult = "DIRECTUR";
                        onRefresh();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cvSakitApproveHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hak_akses.equals("HRD,EXECUTIV")) {
                    switch (spinResult) {
                        case "HRD":
                            checkJabatanHRDEXEC(spinResult);
                            break;
                        case "EXECUTIV":
                            checkJabatanHRDEXEC(spinResult);
                            break;
                        case "DIRECTUR":
                            checkJabatanHRDEXEC(spinResult);
                            break;
                    }
                } else {
                    checkJabatan();
                }

            }
        });

    }


    @Override
    public void onRefresh() {

    }

    private void checkJabatan() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                "http://192.168.50.24/all/hris_ci_3/api/akses", null,
                //"http://hris.qhomedata.id/api/akses", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("TAG_RESPONSE_MENU", "checkJabatan: onMethod"+response.toString());

                            int status = response.getInt("status");

                            JSONObject message = response.getJSONObject("message");
                            String hak = message.getString("hak_akses");

                            Log.d("TAG_TAG_MSG_MENU", "run: " + status + message.toString()+ hak);

                            if (status == 200) {

                                hak_akses = hak;

                                if (hak_akses.equals("HEAD")) {
                                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                    intent.putExtra("kyJabatan", "HEAD");
                                    startActivity(intent);
                                } else if(hak_akses.equals("EXECUTIV")){
                                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                    intent.putExtra("kyJabatan", "EXECUTIV");
                                    startActivity(intent);
                                } else if(hak_akses.equals("DIRECTUR")){
                                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                    intent.putExtra("kyJabatan", "DIRECTUR");
                                    startActivity(intent);
                                } else if(hak_akses.equals("HRD")){
                                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                    intent.putExtra("kyJabatan", "HRD");
                                    startActivity(intent);
                                } else if(hak_akses.equals("HRD,EXECUTIV")){
                                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                    intent.putExtra("kyJabatan", "DIRECTUR");
                                    startActivity(intent);
                                }

                            } /*else if (status.equals("404")) {

                            }*/

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error_Volley_MENU_APP: ", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        Volley.newRequestQueue(menu_approve.this).add(req);
    }

    private void checkJabatanHRDEXEC(String _spinResult) {

        if(_spinResult.equals("HRD")){
            Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
            intent.putExtra("kyJabatan", "HRD");
            startActivity(intent);
        } else if(_spinResult.equals("EXECUTIV")){
            Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
            intent.putExtra("kyJabatan", "EXECUTIV");
            startActivity(intent);
        } else if(_spinResult.equals("DIRECTUR")){
            Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
            intent.putExtra("kyJabatan", "DIRECTUR");
            startActivity(intent);
        }

    }

}