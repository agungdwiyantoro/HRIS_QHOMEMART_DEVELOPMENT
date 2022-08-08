package com.app.mobiledev.apphris.approve;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.approveCutiNew.ListInfinityCutiApprove;
import com.app.mobiledev.apphris.approve.approveDinasMt.ApproveDinasMT;
import com.app.mobiledev.apphris.approve.approveOffTerlambat.ApproveOffTerlambat;
import com.app.mobiledev.apphris.approve.approveSakitNew.ListInfinitySakitApprove;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class menu_approve extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    CardView cvSakitApprove, cvMtDinasApprove, cvCutiApprove, cvOffTerlambat;
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

        cvSakitApprove = findViewById(R.id.cvSakitApprove);
        cvCutiApprove = findViewById(R.id.cvCutiApprove);
        cvMtDinasApprove = findViewById(R.id.cvMtDinasApprove);
        cvOffTerlambat = findViewById(R.id.cvOffTerlambat);
        cvMtDinasApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(menu_approve.this, "Masih dalam Pengembangan", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(menu_approve.this, ApproveDinasMT.class);
                intent.putExtra("kyJabatan", "EXECUTIV");
                startActivity(intent);*/
                checkMenu("dinasMt");

            }
        });

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

        if (hak_akses.equals("EXECUTIV,DIRECTUR,HRD")) {
            llSpinner.setVisibility(View.VISIBLE);
        }

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

        cvSakitApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMenu("sakit");
            }
        });

        cvCutiApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMenu("cuti");
            }
        });

        cvOffTerlambat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMenu("OffTerlambat");
            }
        });

    }

    void checkMenu(String menu){
        if (hak_akses.equals("EXECUTIV,DIRECTUR,HRD")) {
            switch (spinResult) {
                case "HRD":
                    checkJabatanHRDEXEC(spinResult, menu);
                    break;
                case "EXECUTIV":
                    checkJabatanHRDEXEC(spinResult, menu);
                    break;
                case "DIRECTUR":
                    checkJabatanHRDEXEC(spinResult, menu);
                    break;
            }
        } else {
            checkJabatan(menu);
        }
    }


    @Override
    public void onRefresh() {

    }

    private void checkJabatan(String menu) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                api.URL_Akses, null,
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
                                    if (menu.equals("sakit")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                        intent.putExtra("kyJabatan", "HEAD");
                                        startActivity(intent);
                                    } else if (menu.equals("cuti")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                                        intent.putExtra("kyJabatan", "HEAD");
                                        startActivity(intent);
                                    } else if (menu.equals("dinasMt")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveDinasMT.class);
                                        intent.putExtra("kyJabatan", "HEAD");
                                        startActivity(intent);
                                    }

                                    else if (menu.equals("OffTerlambat")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveOffTerlambat.class);
                                        intent.putExtra("kyJabatan", "HEAD");
                                        startActivity(intent);
                                    }


                                } else if(hak_akses.equals("EXECUTIV")){
                                    if (menu.equals("sakit")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV");
                                        startActivity(intent);
                                    } else if (menu.equals("cuti")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV");
                                        startActivity(intent);
                                    } else if (menu.equals("dinasMt")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveDinasMT.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV");
                                        startActivity(intent);
                                    }

                                    else if (menu.equals("OffTerlambat")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveOffTerlambat.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV");
                                        startActivity(intent);
                                    }
                                } else if(hak_akses.equals("DIRECTUR")){
                                    if (menu.equals("sakit")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                        intent.putExtra("kyJabatan", "DIRECTUR");
                                        startActivity(intent);
                                    } else if (menu.equals("cuti")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                                        intent.putExtra("kyJabatan", "DIRECTUR");
                                        startActivity(intent);
                                    } else if (menu.equals("dinasMt")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveDinasMT.class);
                                        intent.putExtra("kyJabatan", "DIRECTUR");
                                        startActivity(intent);
                                    }

                                    else if (menu.equals("OffTerlambat")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveOffTerlambat.class);
                                        intent.putExtra("kyJabatan", "DIRECTUR");
                                        startActivity(intent);
                                    }
                                } else if(hak_akses.equals("HRD")){
                                    if (menu.equals("sakit")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                        intent.putExtra("kyJabatan", "HRD");
                                        startActivity(intent);
                                    } else if (menu.equals("cuti")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                                        intent.putExtra("kyJabatan", "HRD");
                                        startActivity(intent);
                                    } else if (menu.equals("dinasMt")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveDinasMT.class);
                                        intent.putExtra("kyJabatan", "HRD");
                                        startActivity(intent);
                                    }

                                    else if (menu.equals("OffTerlambat")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveOffTerlambat.class);
                                        intent.putExtra("kyJabatan", "HRD");
                                        startActivity(intent);
                                    }
                                } else if(hak_akses.equals("EXECUTIV,DIRECTUR,HRD")){
                                    if (menu.equals("sakit")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV,DIRECTUR,HRD");
                                        startActivity(intent);
                                    } else if (menu.equals("cuti")) {
                                        Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV,DIRECTUR,HRD");
                                        startActivity(intent);
                                    } else if (menu.equals("dinasMt")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveDinasMT.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV,DIRECTUR,HRD");
                                        startActivity(intent);
                                    }

                                    else if (menu.equals("OffTerlambat")) {
                                        Intent intent = new Intent(menu_approve.this, ApproveOffTerlambat.class);
                                        intent.putExtra("kyJabatan", "EXECUTIV,DIRECTUR,HRD");
                                        startActivity(intent);
                                    }
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

    private void checkJabatanHRDEXEC(String _spinResult, String menu) {

        if(_spinResult.equals("HRD")){
            if (menu.equals("sakit")) {
                Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                intent.putExtra("kyJabatan", "HRD");
                startActivity(intent);
            } else if (menu.equals("cuti")) {
                Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                intent.putExtra("kyJabatan", "HRD");
                startActivity(intent);
            }
        } else if(_spinResult.equals("EXECUTIV")){
            if (menu.equals("sakit")) {
                Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                intent.putExtra("kyJabatan", "EXECUTIV");
                startActivity(intent);
            } else if (menu.equals("cuti")) {
                Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                intent.putExtra("kyJabatan", "EXECUTIV");
                startActivity(intent);
            }
        } else if(_spinResult.equals("DIRECTUR")){
            if (menu.equals("sakit")) {
                Intent intent = new Intent(menu_approve.this, ListInfinitySakitApprove.class);
                intent.putExtra("kyJabatan", "DIRECTUR");
                startActivity(intent);
            } else if (menu.equals("cuti")) {
                Intent intent = new Intent(menu_approve.this, ListInfinityCutiApprove.class);
                intent.putExtra("kyJabatan", "DIRECTUR");
                startActivity(intent);
            }
        }

    }
}