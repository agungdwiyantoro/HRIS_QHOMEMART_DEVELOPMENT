package com.app.mobiledev.apphris.approve;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mobiledev.apphris.Model.modelIzinSakitNew;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.approve.adminIzinSakitHead.ListIzinSakitApproveHead;
import com.app.mobiledev.apphris.approve.approveSakitNew.ListInfinitySakitApprove;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class menu_approve extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    CardView cvSakitApproveHead, cvDinasApproveHead, cvTinggalTugasApproveHead, cvCutiApproveHead,
            cvSakitApproveExec, cvDinasApproveExec, cvTinggalTugasApproveExec, cvCutiApproveExec,
            cvSakitApproveDirect, cvDinasApproveDirect, cvTinggalTugasApproveDirect, cvCutiApproveDirect,
            cvSakitApproveHRD, cvDinasApproveHRD, cvTinggalTugasApproveHRD, cvCutiApproveHRD;
    private SessionManager session;
    private String token, spinSelected, spinResult, noJabatan, appHead, appExec, appDirect, appHRD;
    private SessionManager msession;
    private Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_approve);

        msession = new SessionManager(menu_approve.this);

        noJabatan = msession.getNoJabatan();

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
                        spinResult = "EXECUTIVE";
                        onRefresh();
                        break;
                    case "Direktur":
                        spinResult = "DIRECTOR";
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

                if (noJabatan.equals("HR181")) {
                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                    intent.putExtra("kyJabatan", "HEAD");
                    startActivity(intent);
                } else if(noJabatan.equals("HR177")){
                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                    intent.putExtra("kyJabatan", "HEAD");
                    startActivity(intent);
                } else if(noJabatan.equals("HR177")){
                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                    intent.putExtra("kyJabatan", "HEAD");
                    startActivity(intent);
                } else if(noJabatan.equals("HR177")){
                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                    intent.putExtra("kyJabatan", "HEAD");
                    startActivity(intent);
                }  else if(noJabatan.equals("HR177")){
                    Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                    intent.putExtra("kyJabatan", "HEAD");
                    startActivity(intent);
                }
            }
        });

        /*cvSakitApproveHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (noJabatan) {
                    case "HR181": // System Development Manager

                        Intent intent = new Intent(menu_approve.this, ListIzinSakitApproveHead.class);
                        intent.putExtra("kyJabatan", noJabatan);
                        startActivity(intent);

                        break;
                    case "HR003": // FINANCE MANAGER



                        break;
                    case "HR177": // Sekretaris Direktur



                        break;
                    case "HR058": // KEPALA URUSAN PAJAK

                        break;
                    case "HR007": // EXECUTIVE MANAGER KEUANGAN

                        break;
                    case "HR245": // Executive HR Manager

                        break;
                    default:
                }

            }
        });*/

    }


    @Override
    public void onRefresh() {

    }

    private void checkJabatan() {
        JsonObjectRequest req = new JsonObjectRequest(
                "http://192.168.50.24/all/hris_ci_3/api/akses", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.d("TAG_TAG_STATUS", "run: " + status);

                            if (status.equals("200")) {

                            } else if (status.equals("404")) {

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error_Volley: ", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjAwMTEwODAxMDMxMzA0NzkiLCJreXBhc3N3b3JkIjoiMTIzNDU2NyIsImt5amFiYXRhbiI6IkhSMTgxIiwia3lkaXZpc2kiOiJIUjAwNCIsImphYmF0YW4iOiJIRUFEIiwiaWF0IjoxNjQ3NDk5OTE0LCJleHAiOjE2NDc1MTc5MTR9.M6JISgefWIi42iK4I3BQkFx3KTEw1RHozi7pXp717vg"/*+token*/);
                return headers;
            }
        };

        Volley.newRequestQueue(menu_approve.this).add(req);
    }

}