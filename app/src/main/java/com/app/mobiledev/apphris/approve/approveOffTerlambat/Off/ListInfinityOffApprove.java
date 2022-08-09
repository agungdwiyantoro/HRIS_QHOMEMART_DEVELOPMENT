package com.app.mobiledev.apphris.approve.approveOffTerlambat.Off;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.mobiledev.apphris.Model.modelIzinOff;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.PaginationListener;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ListInfinityOffApprove extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = ListInfinityOffApprove.class.getName();

    Spinner dropdown;
    RecyclerView recyler_izin_sakit;
    private String token, dateMonthDate = "", dateMonthString = "", spinSelected, spinResult = "", access;
    private SessionManager msession;
    private LinearLayout lin_transparant;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tx_approve, tvDate, tvTitleInfinityList, tvMessage;
    ImageView ivMonthFilter;
    View emptyHistory, inc_backPage;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private final int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    LinearLayoutManager layoutManager;
    adapterIzinOffApprove adapterIzinOffApprove;
    private ShimmerFrameLayout mShimmerViewContainer;

    MonthYearPickerDialogFragment dialogFragment;
    int yearSelected, monthSelected, daySelected;
    long minDate, maxDate;

    FloatingActionButton fabAddMt;
    private TextView tvTidakAdaData;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_infinity_off_approve, container, false);

        inc_backPage = rootView.findViewById(R.id.inc_backPage);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        access = intent.getStringExtra("kyJabatan");
        Log.d(TAG, "kyJabatan " + access);

        dropdown = rootView.findViewById(R.id.spinDDown);
        String[] items;
        if (access.equals("HRD")) {
            Log.d(TAG, "onCreate: " + access);

            items = new String[]{"Proses", "Diterima", "Ditolak"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);

        } else {

            items = new String[]{"Menunggu", "Proses", "Diterima", "Ditolak"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);
        }

        tvDate = rootView.findViewById(R.id.tvDate);
        ivMonthFilter = rootView.findViewById(R.id.ivMonthFilter);

        emptyHistory = rootView.findViewById(R.id.includeEmptyHistory);

        mShimmerViewContainer = rootView.findViewById(R.id.shimmer_view_container);
        recyler_izin_sakit = rootView.findViewById(R.id.recyler_izin_sakit_emp);
        inc_backPage = rootView.findViewById(R.id.inc_backPage);
        lin_transparant = rootView.findViewById(R.id.lin_transparant);
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        tx_approve = rootView.findViewById(R.id.tx_approve);
        fabAddMt = rootView.findViewById(R.id.fabAddDns);
        tvTidakAdaData = rootView.findViewById(R.id.tv_tidak_ada_data_izin);
        coordinatorLayout = rootView.findViewById(R.id.coordinator_layout);

        swipeRefresh.setOnRefreshListener(this);
        msession = new SessionManager(Objects.requireNonNull(getActivity()));
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
        calendar.set(yearSelected, monthSelected - 1, 1); // Set minimum date to show in dialog
        minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        calendar.clear();
        calendar.set(yearSelected, monthSelected, daySelected); // Set maximum date to show in dialog
        maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

        dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected, minDate, maxDate, "Tanggal Izin");

        adapterIzinOffApprove = new adapterIzinOffApprove(Objects.requireNonNull(getActivity()), new ArrayList<>(), access, spinResult);
        recyler_izin_sakit.setAdapter(adapterIzinOffApprove);
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
                        adapterIzinOffApprove = new adapterIzinOffApprove(Objects.requireNonNull(getActivity()), new ArrayList<>(), access, spinResult);
                        onRefresh();
                        break;
                    case "Proses":
                        spinResult = "2";
                        adapterIzinOffApprove = new adapterIzinOffApprove(Objects.requireNonNull(getActivity()), new ArrayList<>(), access, spinResult);
                        onRefresh();
                        break;
                    case "Diterima":
                        spinResult = "1";
                        adapterIzinOffApprove = new adapterIzinOffApprove(Objects.requireNonNull(getActivity()), new ArrayList<>(), access, spinResult);
                        onRefresh();
                        break;
                    case "Ditolak":
                        spinResult = "0";
                        adapterIzinOffApprove = new adapterIzinOffApprove(Objects.requireNonNull(getActivity()), new ArrayList<>(), access, spinResult);
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
                Log.d(TAG, "getMonthOfYear: " + monthAdd);
            } else {
                bil = "" + monthAdd;
            }

            if (spinResult.equals("")) {
                dateMonthDate = year + "-" + bil;
                spinResult = "";
            } else {
                dateMonthDate = year + "-" + bil/* + "-01"*/;
            }

            dateMonthString = year + "-" + bil + "-01";

            String monthYear = helper.formateDateFromstring("yyyy-MM-dd", "MMMM yyyy", dateMonthString);

            tvDate.setText(monthYear);

            Log.d(TAG, "getMonthOfYear: " + dateMonthDate + " | " + dateMonthString);

            onRefresh();
        });
    }

    private void getMonth() {

        int monthAdd = monthSelected + 1;
        String bil;

        if (monthAdd < 10) {
            bil = "0" + monthAdd;
            Log.d(TAG, "getMonthOfYear: " + monthAdd);
        } else {
            bil = "" + monthAdd;
        }

        if (spinResult.equals("")) {
            dateMonthDate = "";
            spinResult = "";
        } else {
            dateMonthDate = yearSelected + "-" + bil/* + "-01"*/;
        }

        dateMonthString = yearSelected + "-" + bil + "-01";

        String monthYear = helper.formateDateFromstring("yyyy-MM-dd", "MMMM yyyy", dateMonthString);

        tvDate.setText(monthYear);

        Log.d(TAG, "getMonthOfYear: " + dateMonthDate + " | " + dateMonthString);
    }

    private void paginationCall() {
        emptyHistory.setVisibility(View.GONE);

        final ArrayList<modelIzinOff> items = new ArrayList<>();
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
                if(offset>9){
                    return;
                }
                getData(spinResult, access, itemCount, offset, dateMonthDate, items);
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
// Stopping Shimmer Effect's animation after data is loaded to ListView

        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapterIzinOffApprove.clear();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_sakit.setVisibility(View.GONE);
        recyler_izin_sakit.setAdapter(adapterIzinOffApprove);


        paginationCall();
    }

    private void getData(String spinResult, String access, int page, int offset, String date, ArrayList items) {
        //JsonObjectRequest req = new JsonObjectRequest("http://192.168.50.24/all/hris_ci_3/api/approve_OT"
        JsonObjectRequest req = new JsonObjectRequest(api.URL_IzinOffTerlambat_approve
                + "?jenis=OFF"
                + "&status=" + spinResult
                + "&hak_akses=" + access
                + "&limit=" + page
                + "&offset=" + offset
                + "&first_date=" + date,
                //JsonObjectRequest req = new JsonObjectRequest("http://192.168.50.24/all/hris_ci_3/api/approvemt?jenis=MT&status=&hak_akses=HEAD&limit=30&offset=0&first_date=",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");
                            Log.d(TAG, "run: " + status);
                            Log.d(TAG, "run: " + message);
                            switch (status) {
                                case "200":
                                    JSONArray jsonArray = response.getJSONArray("message");
                                    Log.d(TAG, "run: " + jsonArray);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        modelIzinOff model = new modelIzinOff();

                                        model.setId_off(data.getString("id_off"));
                                        model.setName(data.getString("name"));
                                        model.setDvnama(data.getString("dvnama"));
                                        model.setKyano(data.getString("kyano"));
                                        model.setDvano(data.getString("dvano"));
                                        model.setJbano(data.getString("jbano"));
                                        model.setLama_off(data.getString("lama_off"));
                                        model.setMulai_off(data.getString("mulai_off"));
                                        model.setSelesai_off(data.getString("selesai_off"));
                                        model.setSelect_off(data.getString("select_off"));
                                        model.setAlasan(data.getString("alasan"));
                                        model.setCreated_at(data.getString("created_at"));
                                        model.setUpdated_at(data.getString("updated_at"));
                                        model.setLampiran(data.getString("lampiran"));
                                        model.setApprove_head(data.getString("approve_head"));
                                        model.setApprove_hrd(data.getString("approve_hrd"));
                                        model.setApprove_executiv(data.getString("approve_executiv"));
                                        model.setApprove_directur(data.getString("approve_directur"));
                                        model.setHead_kyano(data.getString("head_kyano"));
                                        model.setHrd_kyano(data.getString("hrd_kyano"));
                                        model.setExecutiv_kyano(data.getString("executiv_kyano"));
                                        model.setDirectur_kyano(data.getString("directur_kyano"));
                                        model.setHead_approve_date(data.getString("executiv_approve_date"));
                                        model.setHrd_approve_date(data.getString("directur_approve_date"));
                                        model.setExecutiv_approve_date(data.getString("head_name"));
                                        model.setDirectur_approve_date(data.getString("hrd_name"));
                                        model.setComment(data.getString("executiv"));
                                        model.setInput_from(data.getString("input_from"));
                                        model.setHead_name(data.getString("head_name"));
                                        model.setHrd_name(data.getString("hrd_name"));
                                        model.setExecutiv(data.getString("executiv"));
                                        model.setDir(data.getString("dir"));

                                        model.setComment(data.getString("comment"));
                                        model.setStatus_approve(data.getString("status_approve"));

                                        items.add(model);
                                        //modelIzinSakits.add(model);
                                        //modelIzinMtNews.add(model);

                                        Log.d(TAG, "onResponse: " + data.getString("id_off"));
                                        emptyHistory.setVisibility(View.GONE);
                                    }

                                    break;
                                /*case "201":
                                    emptyHistory.setVisibility(View.VISIBLE);
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    recyler_izin_sakit.setVisibility(View.GONE);
                                    break;*/
                            }

                            if (currentPage != PAGE_START)
                                adapterIzinOffApprove.removeLoading();
                            adapterIzinOffApprove.addItems(items);
                            swipeRefresh.setRefreshing(false);
                            Log.d(TAG, "onResponse: " + items.size());

                            if (currentPage < totalPage) {
                                //adapterIzinMtApprove.addLoading();
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
                            Log.d(TAG, "onResponse: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.toString());
                Log.d(TAG, "ahoy " + error.getMessage());
                if (error.getMessage()==null) {
                    Log.d(TAG, "ahoy2 " + error.getMessage());
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
                headers.put("Authorization", "Bearer " + token);
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

    @Override
    public void onStart() {
        super.onStart();
        paginationCall();
    }
}