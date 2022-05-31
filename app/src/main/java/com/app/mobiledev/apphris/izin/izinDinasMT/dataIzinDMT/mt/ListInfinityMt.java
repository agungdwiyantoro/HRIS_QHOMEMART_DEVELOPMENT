package com.app.mobiledev.apphris.izin.izinDinasMT.dataIzinDMT.mt;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.app.mobiledev.apphris.Model.modelIzinSecVer;
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
import java.util.Objects;

public class ListInfinityMt extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Spinner dropdown;
    RecyclerView recyler_izin_sakit;
    private List<modelIzinSecVer> modelIzinSecVers;
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
    com.app.mobiledev.apphris.izin.izinDinasMT.dataIzinDMT.mt.adapterSecIzinMt adapterSecIzinMt;
    private ShimmerFrameLayout mShimmerViewContainer;

    MonthYearPickerDialogFragment dialogFragment;
    int yearSelected, monthSelected, daySelected;
    long minDate, maxDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_sec_ver_list_mt, container, false);

        inc_backPage = rootView.findViewById(R.id.inc_backPage);

        //get the spinner from the xml.
        dropdown = rootView.findViewById(R.id.spinDDown);
//create a list of items for the spinner.
        String[] items = new String[]{"Menunggu", "Diterima", "Ditolak"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        tvDate = rootView.findViewById(R.id.tvDate);
        ivMonthFilter = rootView.findViewById(R.id.ivMonthFilter);

        emptyHistory = rootView.findViewById(R.id.includeEmptyHistory);

        mShimmerViewContainer = rootView.findViewById(R.id.shimmer_view_container);
        recyler_izin_sakit = rootView.findViewById(R.id.recyler_izin_sakit_emp);
        inc_backPage = rootView.findViewById(R.id.inc_backPage);
        lin_transparant = rootView.findViewById(R.id.lin_transparant);
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        tx_approve = rootView.findViewById(R.id.tx_approve);
        swipeRefresh.setOnRefreshListener(this);
        msession = new SessionManager(Objects.requireNonNull(getActivity()));
        modelIzinSecVers = new ArrayList<>();
        token = msession.getToken();
        layoutManager = new LinearLayoutManager(Objects.requireNonNull(getActivity()));
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

        adapterSecIzinMt = new adapterSecIzinMt(Objects.requireNonNull(getActivity()), new ArrayList<>());
        recyler_izin_sakit.setAdapter(adapterSecIzinMt);
        getMonth();
        paginationCall();

        ivMonthFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getFragmentManager(), null);
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
                        spinResult = "";
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

        return rootView;
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

        if (spinResult.equals("")) {
            dateMonthDate = "";
            spinResult = "";
        } else {
            //dateMonthDate = yearSelected + "-" + bil/* + "-01"*/;
        }

        dateMonthString = yearSelected + "-" + bil + "-01";

        String monthYear = helper.formateDateFromstring("yyyy-MM-dd", "MMMM yyyy", dateMonthString);

        tvDate.setText(monthYear);

        Log.d("TAG_TAG_MY", "getMonthOfYear: " + dateMonthDate + " | "+ dateMonthString);
    }

    private void paginationCall() {
        emptyHistory.setVisibility(View.GONE);

        final ArrayList<modelIzinSecVer> items = new ArrayList<>();
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
        adapterSecIzinMt.clear();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_sakit.setVisibility(View.GONE);
        recyler_izin_sakit.setAdapter(adapterSecIzinMt);



        paginationCall();
    }

    private void getData(int page, int offset, ArrayList items) {



        JsonObjectRequest req = new JsonObjectRequest(api.URL_IzinSakit+"?offset=" + offset
                +"&first_date="+ dateMonthDate
                +"&limit=" + page
                + "&status="+spinResult, null,
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
                                        modelIzinSecVer model = new modelIzinSecVer();

                                        model.setName(data.getString("name"));

                                        model.setId(data.getString("id"));
                                        model.setKyano(data.getString("kyano"));
                                        model.setIndikasiSakit(data.getString("indikasi_sakit"));
                                        model.setMulaiSakitTanggal(data.getString("mulai_sakit_tanggal"));
                                        model.setSelesaiSakitTanggal(data.getString("selesai_sakit_tanggal"));
                                        model.setSelectDate(data.getString("select_date"));
                                        model.setJmlIzin(data.getString("jml_izin"));
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
                                        model.setComment(data.getString("comment"));
                                        model.setHeadName(data.getString("head_name"));
                                        model.setHrdName(data.getString("hrd_name"));
                                        model.setExecutiv(data.getString("executiv"));
                                        model.setDir(data.getString("dir"));
                                        model.setStatus(data.getString("status"));

                                        items.add(model);
                                        //modelIzinSakits.add(model);
                                        //modelIzinSecVers.add(model);

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
                                adapterSecIzinMt.removeLoading();
                            adapterSecIzinMt.addItems(items);
                            swipeRefresh.setRefreshing(false);
                            Log.d("CUURENT_PAGE", "onResponse: " + items.size());

                            if (currentPage < totalPage) {
                                //adapterSecIzinMt.addLoading();
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
                headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjA2NTIyMDA1MTMwNTEyOTYiLCJreXBhc3N3b3JkIjoiMTIzNDU2NyIsImt5amFiYXRhbiI6IkhSMTQ3Iiwia3lkaXZpc2kiOiJIUjAwNCIsImt5YmFnaWFuIjoiQkcwNDYiLCJqYWJhdGFuIjoibnVsbCIsImlhdCI6MTY1MzYzNTAzMywiZXhwIjoxNjUzNjUzMDMzfQ.bHik5pw7mXSZfECVifGJ4oUYAj0rrZT9Woefl1QIyzU"/*+token*/);
                return headers;
            }
        };

        //ApplicationController.getInstance().addToRequestQueue(req);
        Volley.newRequestQueue(Objects.requireNonNull(getActivity())).add(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }

}