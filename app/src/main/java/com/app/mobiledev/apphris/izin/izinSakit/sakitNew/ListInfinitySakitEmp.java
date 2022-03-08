package com.app.mobiledev.apphris.izin.izinSakit.sakitNew;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.PaginationListener;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListInfinitySakitEmp extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Spinner dropdown;
    RecyclerView recyler_izin_sakit;
    private List<modelIzinSakitNew> modelIzinSakitNews;
    private String token;
    private String TAG = "LISIzinSakitApprove";
    private ImageView img_back;
    private SessionManager msession;
    private RadioGroup rbFilter;
    private LinearLayout lin_transparant;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tx_approve;

    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    LinearLayoutManager layoutManager;
    adapterIzinSakitEmp adapterIzinSakitEmp;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_infinity_sakit_emp);

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinDDown);
//create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyler_izin_sakit = findViewById(R.id.recyler_izin_sakit_emp);
        rbFilter = findViewById(R.id.rbFilter);
        img_back = findViewById(R.id.img_back);
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

        adapterIzinSakitEmp = new adapterIzinSakitEmp(ListInfinitySakitEmp.this, new ArrayList<>());
        recyler_izin_sakit.setAdapter(adapterIzinSakitEmp);
        paginationCall();

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

    }

    private void paginationCall() {
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
                Log.d("cek_url_all", "run: " + api.URL_IzinSakit + "?limit=" + itemCount + "&offset=" + offset);
                getRiwayatSakitAll(itemCount, offset, items);
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
        AndroidNetworking.get("http://192.168.50.24/all/hris_ci_3/api/izinsakit?limit=" + page + "&offset=" + offset + "&status=")
                .addHeaders("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjEyMzQ1Njc4OTAxMjM0NTYiLCJreXBhc3N3b3JkIjoiMTIzNDU2NyIsImt5amFiYXRhbiI6IkhSMTQ3IiwiamFiYXRhbiI6Im51bGwiLCJpYXQiOjE2NDY3MjEwOTYsImV4cCI6MTY0NjczOTA5Nn0.6TR4nCkdd_R17muJ8w40qeH2u24XCpp2qxibvKpHgQQ"/*+token*/)
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
                                if (!message.equals("null")) {

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

                                        items.add(model);
                                        //modelIzinSakits.add(model);
                                        //modelIzinSakitNews.add(model);

                                        Log.d("TAG_INDIKASI", "onResponse: " + data.getString("indikasi_sakit"));

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

                                } else {
                                    adapterIzinSakitEmp.removeLoading();
                                }

                            } else {

                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);

                                recyler_izin_sakit.setVisibility(View.VISIBLE);
                                adapterIzinSakitEmp.removeLoading();

                            }
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
                        Log.d("EROOR_RIWYAT_IZIN_SAKIT", "onError: " + anError.getErrorDetail());

                    }
                });


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