package com.app.mobiledev.apphris.izin.izinSakit.sakitNew;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.Model.modelIzinSakitNew;
import com.app.mobiledev.apphris.R;
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

public class ListInfinitySakitEmp extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Spinner dropdown;
    RecyclerView recyler_izin_sakit;
    private List<modelIzinSakitNew> modelIzinSakitNews;
    private String token, dateMonthDate="", dateMonthString = "", spinSelected, spinResult="null";
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
    adapterIzinSakitEmp adapterIzinSakitEmp;
    private ShimmerFrameLayout mShimmerViewContainer;

    MonthYearPickerDialogFragment dialogFragment;
    int yearSelected, monthSelected, daySelected;
    long minDate, maxDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_infinity_sakit_emp);

        inc_backPage = findViewById(R.id.inc_backPage);

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinDDown);
//create a list of items for the spinner.
        String[] items = new String[]{"Menunggu", "Diterima", "Ditolak"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        tvTitleInfinityList = findViewById(R.id.tvTitleInfinityList);
        tvTitleInfinityList.setText("Daftar Izin Sakit");
        tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setText("Pengajuan izin sakit kosong");

        tvDate = findViewById(R.id.tvDate);
        ivMonthFilter = findViewById(R.id.ivMonthFilter);

        emptyHistory = findViewById(R.id.includeEmptyHistory);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyler_izin_sakit = findViewById(R.id.recyler_izin_sakit_emp);
        inc_backPage = findViewById(R.id.inc_backPage);
        lin_transparant = findViewById(R.id.lin_transparant);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tx_approve = findViewById(R.id.tx_approve);
        swipeRefresh.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) ListInfinitySakitEmp.this);
        msession = new SessionManager(ListInfinitySakitEmp.this);
        modelIzinSakitNews = new ArrayList<>();
        token = msession.getToken();
        layoutManager = new LinearLayoutManager(this);
        recyler_izin_sakit.setLayoutManager(layoutManager);

        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_sakit.setVisibility(View.GONE);

        //Set default values
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        daySelected = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.clear();
        calendar.set(yearSelected, monthSelected-1, 1); // Set minimum date to show in dialog
        minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        calendar.clear();
        calendar.set(yearSelected, monthSelected, daySelected); // Set maximum date to show in dialog
        maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected, minDate, maxDate, "Tanggal Izin");

        adapterIzinSakitEmp = new adapterIzinSakitEmp(ListInfinitySakitEmp.this, new ArrayList<>());
        recyler_izin_sakit.setAdapter(adapterIzinSakitEmp);
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

        recyler_izin_sakit.addOnScrollListener(new PaginationListener(layoutManager) {
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
                        spinResult = "null";
                        onRefresh();
                        break;
                    case "Diterima":
                        spinResult = "1";
                        onRefresh();
                        break;
                    case "Ditolak":
                        spinResult = "0";
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

            dateMonthDate = year + "-" + bil/* + "-01"*/;
            dateMonthString = year + "-" + bil + "-01";

            String monthYear = helper.formateDateFromstring("yyyy-MM-dd", "MMMM yyyy", dateMonthString);

            tvDate.setText(monthYear);

            Log.d("TAG_TAG_MY", "getMonthOfYear: " + dateMonthDate + " | "+ dateMonthString);

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

        dateMonthDate = yearSelected + "-" + bil/* + "-01"*/;
        dateMonthString = yearSelected + "-" + bil + "-01";

        String monthYear = helper.formateDateFromstring("yyyy-MM-dd", "MMMM yyyy", dateMonthString);

        tvDate.setText(monthYear);

        Log.d("TAG_TAG_MY", "getMonthOfYear: " + dateMonthDate + " | "+ dateMonthString);
    }

    private void paginationCall() {
        emptyHistory.setVisibility(View.GONE);

        final ArrayList<modelIzinSakitNew> items = new ArrayList<>();
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
                recyler_izin_sakit.setHasFixedSize(true);
                Log.d("cek_url_all", "run: http://192.168.50.24/all/hris_ci_3/api/izinsakit?offset=" + offset +"&first_date="+ dateMonthDate +"&limit=" + itemCount + "&status="+spinResult);
                //getRiwayatSakitAll(itemCount, offset, items);
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
        adapterIzinSakitEmp.clear();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_sakit.setVisibility(View.GONE);
        recyler_izin_sakit.setAdapter(adapterIzinSakitEmp);



        paginationCall();
    }

    private void getRiwayatSakitAll(int page, int offset, ArrayList items) {
        //AndroidNetworking.get(api.URL_IzinSakit_approve_head+"?limit="+page+"&offset="+offset+"&status=")
        //AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/izinsakit?limit=" + page + "&offset=" + offset + "&status=")
        Log.d("TAG_PARAM", "getRiwayatSakitAll: http://192.168.50.24/all/hris_ci_3/api/izinsakit?offset=" + offset +"&first_date="+ dateMonthDate +"&limit=" + page + "&status="+spinResult);
        AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/izinsakit?offset=" + offset +"&first_date="+ dateMonthDate +"&limit=" + page + "&status="+spinResult)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Log.d("TAG_TAG", "run: " + message);

                            if (status.equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("message");
                                Log.d("TAG_TAG", "run: " + jsonArray);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelIzinSakitNew model = new modelIzinSakitNew();

                                    model.setName(data.getString("name"));
                                    model.setId(data.getString("id"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setIndikasiSakit(data.getString("indikasi_sakit"));
                                    model.setMulaiSakitTanggal(data.getString("mulai_sakit_tanggal"));
                                    model.setSelesaiSakitTanggal(data.getString("selesai_sakit_tanggal"));
                                    model.setSelectDate(data.getString("select_date"));
                                    model.setCatatan(data.getString("catatan"));
                                    model.setLampiranFile(data.getString("lampiran_file"));

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

                                    model.setHeadName(data.getString("head_name"));
                                    model.setHrdName(data.getString("hrd_name"));

                                    model.setCatatanHrd(data.getString("catatan_hrd"));
                                    model.setStatus(data.getString("status"));

                                    items.add(model);
                                    //modelIzinSakits.add(model);
                                    //modelIzinSakitNews.add(model);

                                    Log.d("TAG_INDIKASI", "onResponse: " + data.getString("indikasi_sakit"));

                                }
                            } else if(status.equals("201")) {
                                Toast.makeText(ListInfinitySakitEmp.this, "Riwayat Izin Sakit Kosong", Toast.LENGTH_LONG).show();
                                emptyHistory.setVisibility(View.VISIBLE);
                            }

                            if (currentPage != PAGE_START)
                                adapterIzinSakitEmp.removeLoading();
                            adapterIzinSakitEmp.addItems(items);
                            swipeRefresh.setRefreshing(false);
                            Log.d("CUURENT_PAGE", "onResponse: " + items.size());

                            if (currentPage < totalPage) {
                                //adapterIzinSakitEmp.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;

                            // Stopping Shimmer Effect's animation after data is loaded to ListView
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            recyler_izin_sakit.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ON_ERROR", "onError: "+anError.getErrorBody());
                    }




                });


    }

    private void getData(int page, int offset, ArrayList items) {
        JsonObjectRequest req = new JsonObjectRequest("http://192.168.50.24/all/hris_ci_3/api/izinsakit?offset=" + offset +"&first_date="+ dateMonthDate +"&limit=" + page + "&status="+spinResult, null,
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
                                        modelIzinSakitNew model = new modelIzinSakitNew();

                                        model.setName(data.getString("name"));
                                        model.setId(data.getString("id"));
                                        model.setKyano(data.getString("kyano"));
                                        model.setIndikasiSakit(data.getString("indikasi_sakit"));
                                        model.setMulaiSakitTanggal(data.getString("mulai_sakit_tanggal"));
                                        model.setSelesaiSakitTanggal(data.getString("selesai_sakit_tanggal"));
                                        model.setSelectDate(data.getString("select_date"));
                                        model.setCatatan(data.getString("catatan"));
                                        model.setLampiranFile(data.getString("lampiran_file"));

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

                                        model.setHeadName(data.getString("head_name"));
                                        model.setHrdName(data.getString("hrd_name"));

                                        model.setCatatanHrd(data.getString("catatan_hrd"));
                                        model.setStatus(data.getString("status"));

                                        items.add(model);
                                        //modelIzinSakits.add(model);
                                        //modelIzinSakitNews.add(model);

                                        Log.d("TAG_INDIKASI", "onResponse: " + data.getString("indikasi_sakit"));
                                        emptyHistory.setVisibility(View.GONE);
                                    }

                                    break;
                                /*case "201":
                                    emptyHistory.setVisibility(View.VISIBLE);
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    recyler_izin_sakit.setVisibility(View.GONE);
                                    onRestart();
                                    break;*/
                                case "404":
                                    emptyHistory.setVisibility(View.VISIBLE);
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    recyler_izin_sakit.setVisibility(View.GONE);
                                    break;
                            }

                            if (currentPage != PAGE_START)
                                adapterIzinSakitEmp.removeLoading();
                            adapterIzinSakitEmp.addItems(items);
                            swipeRefresh.setRefreshing(false);
                            Log.d("CUURENT_PAGE", "onResponse: " + items.size());

                            if (currentPage < totalPage) {
                                //adapterIzinSakitEmp.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;

                            // Stopping Shimmer Effect's animation after data is loaded to ListView
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            recyler_izin_sakit.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("ERROR_VOLLEY_EMP: ", error.toString());

                if (error.toString().equals("")) {
                    emptyHistory.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.GONE);
                    recyler_izin_sakit.setVisibility(View.GONE);
                    //refresh dibawah digunakan untuk
                    // menanggulangi error
                    // BasicNetwork.performRequest: Unexpected response code 404
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        //ApplicationController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(ListInfinitySakitEmp.this).add(req);
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
}