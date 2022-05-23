package com.app.mobiledev.apphris.approve.approveCutiNew;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mobiledev.apphris.Model.modelIzinCutiNew;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.PaginationListener;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListInfinityCutiApprove extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Spinner dropdown;
    RecyclerView recyler_izin_cuti;
    private List<modelIzinCutiNew> modelIzinCutiNews;
    private String token, dateMonthDate = "", dateMonthString = "", spinSelected, spinResult = "", access = "";
    private SessionManager msession;
    private LinearLayout lin_transparant;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tx_approve, tvDate, tvTitleInfinityList, tvMessage;
    ImageView ivMonthFilter;
    View emptyHistory, inc_backPage;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    LinearLayoutManager layoutManager;
    adapterIzinCutiApprove adapterIzinCutiApprove;
    private ShimmerFrameLayout mShimmerViewContainer;

    MonthYearPickerDialogFragment dialogFragment;
    int yearSelected, monthSelected, daySelected;
    long minDate, maxDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_infinity_cuti_emp);

        Intent intent = getIntent();
        access = intent.getStringExtra("kyJabatan");

        inc_backPage = findViewById(R.id.inc_backPage);

        dropdown = findViewById(R.id.spinDDown);
        String[] items;
        if (access.equals("HRD")) {
            Log.d("TAG_JABJAB", "onCreate: "+access);

            items = new String[]{"Proses", "Diterima", "Ditolak"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);

        } else {

            items = new String[]{"Menunggu", "Proses", "Diterima", "Ditolak"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);
        }

        tvTitleInfinityList = findViewById(R.id.tvTitleInfinityList);
        tvTitleInfinityList.setText("Daftar Pengajuan Izin Cuti");
        tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText("Pengajuan izin cuti kosong");

        tvDate = findViewById(R.id.tvDate);
        ivMonthFilter = findViewById(R.id.ivMonthFilter);

        emptyHistory = findViewById(R.id.includeEmptyHistory);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyler_izin_cuti = findViewById(R.id.recyler_izin_cuti_emp);
        inc_backPage = findViewById(R.id.inc_backPage);
        lin_transparant = findViewById(R.id.lin_transparant);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tx_approve = findViewById(R.id.tx_approve);
        swipeRefresh.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) ListInfinityCutiApprove.this);
        msession = new SessionManager(ListInfinityCutiApprove.this);
        modelIzinCutiNews = new ArrayList<>();
        token = msession.getToken();
        layoutManager = new LinearLayoutManager(this);
        recyler_izin_cuti.setLayoutManager(layoutManager);

        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_cuti.setVisibility(View.GONE);

        //Set default values
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        daySelected = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.clear();
        calendar.set(yearSelected, monthSelected - 1, 1); // Set minimum date to show in dialog
        minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        calendar.clear();
        calendar.set(yearSelected, monthSelected, daySelected); // Set maximum date to show in dialog
        maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected, minDate, maxDate, "Tanggal Izin");

        adapterIzinCutiApprove = new adapterIzinCutiApprove(ListInfinityCutiApprove.this, new ArrayList<>(), access, spinResult);
        recyler_izin_cuti.setAdapter(adapterIzinCutiApprove);
        getMonth();
        paginationCall();

        inc_backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivMonthFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), null);
                getMonthOfYear();
            }
        });

        recyler_izin_cuti.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                paginationCall();
            }

            @Override
            public boolean isLastPage() {

                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinSelected = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                switch (spinSelected) {
                    case "Menunggu":
                        spinResult = "";
                        adapterIzinCutiApprove = new adapterIzinCutiApprove(ListInfinityCutiApprove.this, new ArrayList<>(), access, spinResult);
                        onRefresh();
                        break;
                    case "Proses":
                        spinResult = "2";
                        adapterIzinCutiApprove = new adapterIzinCutiApprove(ListInfinityCutiApprove.this, new ArrayList<>(), access, spinResult);
                        onRefresh();
                        break;
                    case "Diterima":
                        spinResult = "1";
                        adapterIzinCutiApprove = new adapterIzinCutiApprove(ListInfinityCutiApprove.this, new ArrayList<>(), access, spinResult);
                        onRefresh();
                        break;
                    case "Ditolak":
                        spinResult = "0";
                        adapterIzinCutiApprove = new adapterIzinCutiApprove(ListInfinityCutiApprove.this, new ArrayList<>(), access, spinResult);
                        onRefresh();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getMonthOfYear() {
        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            int monthAdd = monthOfYear + 1;
            String bil;

            if (monthAdd < 10) {
                bil = "0" + monthAdd;
                Log.d("TAG_TEST+1", "getMonthOfYear: " + monthAdd);
            } else {
                bil = "" + monthAdd;
            }

            if (spinResult.equals("")) {
                dateMonthDate = "";
                spinResult = "";
            } else {
                dateMonthDate = year + "-" + bil/* + "-01"*/;
            }

            dateMonthString = year + "-" + bil + "-01";

            String monthYear = helper.formateDateFromstring("yyyy-MM-dd", "MMMM yyyy", dateMonthString);

            tvDate.setText(monthYear);

            Log.d("TAG_TAG_MY", "getMonthOfYear: " + dateMonthDate + " | " + dateMonthString);

            onRefresh();
        });
    }

    private void getMonth() {

        int monthAdd = monthSelected + 1;
        String bil;

        if (monthAdd < 10) {
            bil = "0" + monthAdd;
            Log.d("TAG_TEST+1", "getMonthOfYear: " + monthAdd);
        } else {
            bil = "" + monthAdd;
        }

        if (spinResult.equals("")) {
            dateMonthDate = "";
            spinResult = "";
        } else {
            //dateMonthDate = yearSelected + "-" + bil/* + "-01"*/;
        }

        dateMonthString = yearSelected + "-" + bil + "-01";

        String monthYear = helper.formateDateFromstring("yyyy-MM-dd", "MMMM yyyy", dateMonthString);

        tvDate.setText(monthYear);

        Log.d("TAG_TAG_MY", "getMonthOfYear: " + dateMonthDate + " | " + dateMonthString);
    }

    private void paginationCall() {
        emptyHistory.setVisibility(View.GONE);

        final ArrayList<modelIzinCutiNew> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < 10; i++) {
                    itemCount++;
                }

                int offset = 0;
                if (itemCount > 10) {
                    offset = (itemCount - totalPage);
                }
                recyler_izin_cuti.setHasFixedSize(true);

                //getRiwayatCutiAll(itemCount, offset, items);
                getData(itemCount, offset, items);
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
        // Stopping Shimmer Effect's animation after data is loaded to ListView

        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapterIzinCutiApprove.clear();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_cuti.setVisibility(View.GONE);
        recyler_izin_cuti.setAdapter(adapterIzinCutiApprove);

        paginationCall();
    }

    private void getData(int limit, int offset, ArrayList items) {

        JsonObjectRequest req = new JsonObjectRequest(
                /*"http://192.168.50.24/all/hris_ci_3/api/approvecuti?" +
                        "offset=" + offset +
                        "&hak_akses="+ access +
                        "&first_date="+ dateMonthDate +
                        "&limit=" + page +
                        "&status="+spinResult, null,*/
                api.URL_IzinCuti_approve +
                        "?status=" + spinResult +
                        "&hak_akses=" + access +
                        "&limit=" + limit +
                        "&offset=" + offset +
                        "&first_date=" + dateMonthDate, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Log.d("TAG_TAG_STATUS", "run: " + status);
                            Log.d("TAG_TAG_MESSAGE", "run: " + message);

                            switch (status) {
                                case "200":
                                    JSONArray jsonArray = response.getJSONArray("message");
                                    Log.d("TAG_TAG", "run: " + jsonArray);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        modelIzinCutiNew model = new modelIzinCutiNew();

                                        model.setKynm(data.getString("name"));
                                        model.setCtano(data.getString("ctano"));
                                        model.setKyano(data.getString("kyano"));
                                        model.setNmcuti(data.getString("nmcuti"));
                                        model.setDvnama(data.getString("dvnama"));
                                        model.setCtmulai(data.getString("ctmulai"));
                                        model.setCtselesai(data.getString("ctselesai"));
                                        model.setSelectCuti(data.getString("select_cuti"));
                                        model.setCtlama(data.getString("ctlama"));
                                        model.setCtalasan(data.getString("ctalasan"));
                                        model.setCtperiode(data.getString("ctperiode"));
                                        model.setCttelp(data.getString("cttelp"));
                                        model.setCtsisahak(data.getString("ctsisahak"));
                                        model.setCtjenisId(data.getString("ctjenis_id"));
                                        model.setCtjenis(data.getString("ctjenis"));

                                        model.setCreatedAt(data.getString("created_at"));
                                        model.setUpdatedAt(data.getString("updated_at"));
                                        model.setApproveHead(data.getString("approve_head"));
                                        model.setApproveHrd(data.getString("approve_hrd"));
                                        model.setApproveExecutiv(data.getString("approve_executiv"));
                                        model.setApproveDirectur(data.getString("approve_directur"));
                                        model.setExecutivKyano(data.getString("executiv_kyano"));
                                        model.setDirecturKyano(data.getString("directur_kyano"));
                                        model.setHrdKyano(data.getString("hrd_kyano"));
                                        model.setHeadApproveDate(data.getString("head_approve_date"));
                                        model.setHrdApproveDate(data.getString("hrd_approve_date"));
                                        model.setExecutivApproveDate(data.getString("executiv_approve_date"));
                                        model.setDirecturApproveDate(data.getString("directur_approve_date"));
                                        model.setComment(data.getString("comment"));
                                        model.setHeadName(data.getString("head_name"));
                                        model.setHrdName(data.getString("hrd_name"));
                                        model.setExecutiv(data.getString("executiv"));
                                        model.setDir(data.getString("dir"));
                                        model.setStatus(data.getString("status"));
                                        model.setStatusApprove(data.getString("status_approve"));
                                        model.setKet(data.getString("ket"));

                                        items.add(model);
                                        //modelIzinCutis.add(model);
                                        //modelIzinCutiNews.add(model);

                                        Log.d("TAG_INDIKASI", "onResponse: " + data.getString("nmcuti"));
                                        emptyHistory.setVisibility(View.GONE);
                                    }

                                    break;
                                case "201":
                                    /*emptyHistory.setVisibility(View.VISIBLE);
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    recyler_izin_cuti.setVisibility(View.GONE);*/

                                    break;
                                case "404":
                                    emptyHistory.setVisibility(View.VISIBLE);
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    recyler_izin_cuti.setVisibility(View.GONE);
                                    break;
                            }

                            if (currentPage != PAGE_START)
                                adapterIzinCutiApprove.removeLoading();
                            adapterIzinCutiApprove.addItems(items);
                            swipeRefresh.setRefreshing(false);
                            Log.d("CUURENT_PAGE", "onResponse: " + items.size());

                            if (currentPage < totalPage) {
                                //adapterIzinCutiEmp.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;

                            // Stopping Shimmer Effect's animation after data is loaded to ListView
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            recyler_izin_cuti.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_CUTI", "onResponse: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ERROR_VOLLEY_APP: ", error.networkResponse);

                if (error.toString().equals("404")) {
                    emptyHistory.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.GONE);
                    recyler_izin_cuti.setVisibility(View.GONE);
                    //refresh dibawah digunakan untuk
                    // menanggulangi error
                    // BasicNetwork.performRequest: Unexpected response code 404
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        //ApplicationController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(ListInfinityCutiApprove.this).add(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}