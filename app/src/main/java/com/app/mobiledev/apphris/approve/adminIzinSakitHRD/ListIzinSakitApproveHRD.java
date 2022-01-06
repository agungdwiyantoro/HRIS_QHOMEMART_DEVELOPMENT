package com.app.mobiledev.apphris.approve.adminIzinSakitHRD;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.adminIzinSakitHRD.adapterIzinSakitApprove.adapterIzinSakitApproveHRDAll;
import com.app.mobiledev.apphris.approve.adminIzinSakitHead.ListIzinSakitApproveHead;
import com.app.mobiledev.apphris.approve.adminIzinSakitHead.adapterIzinSakitApprove.adapterIzinSakitApproveHeadAll;
import com.app.mobiledev.apphris.helperPackage.PaginationListener;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListIzinSakitApproveHRD extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String token;
    private SessionManager mSession;
    private int currentPage = PAGE_START;
    private ShimmerFrameLayout mShimmerViewContainer;
     adapterIzinSakitApproveHRDAll mAdapterAll;
     LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefresh;

    private RecyclerView recyler_izin_sakit;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    private int itemCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_izin_sakit_approve_hrd);
        mSession=new SessionManager(ListIzinSakitApproveHRD.this);
        token=mSession.getToken();
        swipeRefresh=findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        recyler_izin_sakit=findViewById(R.id.recyler_izin_sakit);
        mShimmerViewContainer=findViewById(R.id. shimmer_view_container);
        mLayoutManager = new LinearLayoutManager(this);
        recyler_izin_sakit.setLayoutManager(mLayoutManager);
        mAdapterAll = new adapterIzinSakitApproveHRDAll(new ArrayList<>(), ListIzinSakitApproveHRD.this);
        paginationCall();

        recyler_izin_sakit.addOnScrollListener(new PaginationListener(mLayoutManager) {
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




    private void paginationCall(){
        final ArrayList<modelIzinSakit>items=new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    itemCount++;
                }

                int offset=0;
                if(itemCount>10){
                    offset=(itemCount-totalPage)+1;
                }
                recyler_izin_sakit.setAdapter(mAdapterAll);
                recyler_izin_sakit.setHasFixedSize(true);
                mShimmerViewContainer.startShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.VISIBLE);
                recyler_izin_sakit.setVisibility(View.GONE);
                Log.d("CEK_PARAM", "limit: "+itemCount+" offset "+offset);
                getRiwayatSakitAll(itemCount,offset,items);

            }
        },1500);
    }
    private void getRiwayatSakitAll(int page, int offset, ArrayList items) {
        AndroidNetworking.get(api.URL_IzinSakit_approve_hrd+"?limit="+page+"&offset="+offset)
                .addHeaders("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJreWFubyI6IjAzMDIxNzExMDc0NTA2OTUiLCJreXBhc3N3b3JkIjoiMTIzNDU2NyIsImlhdCI6MTY0MTM1Mjc2MywiZXhwIjoxNjQxMzcwNzYzfQ.V_w3JQ1lJt8_g-h4DVkx5mgQxUlSfDlVfNYqIWm7ZB8")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            //mAdapter.clear();
                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_HRD", "onResponse: " + status);
                            if (status.equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("message");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    modelIzinSakit model = new modelIzinSakit();
                                    model.setId(data.getString("id"));
                                    model.setKyano(data.getString("kyano"));
                                    model.setIndikasi_sakit(data.getString("indikasi_sakit"));
                                    model.setSelesai_sakit_tanggal(data.getString("mulai_sakit_tanggal"));
                                    model.setMulai_sakit_tanggal(data.getString("selesai_sakit_tanggal"));
                                    model.setCatatan(data.getString("catatan"));
                                    model.setCreated_at(data.getString("created_at"));
                                    model.setUpdated_at(data.getString("updated_at"));
                                    model.setApprove_head(data.getString("approve_head"));
                                    model.setApprove_hrd(data.getString("approve_hrd"));
                                    model.setLampiran_file(data.getString("lampiran_file"));
                                    model.setHead_kyano(data.getString("head_kyano"));
                                    model.setHrd_kyano(data.getString("hrd_kyano"));
                                    model.setHead_approve_date(data.getString("head_approve_date"));
                                    model.setHrd_approve_date(data.getString("hrd_approve_date"));
                                    model.setHead_name(data.getString("head_name"));
                                    model.setName(data.getString("name"));
                                    items.add(model);
                                    //modelIzinSakits.add(model);


                                }

                                if (currentPage != PAGE_START) mAdapterAll.removeLoading();
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);

                                recyler_izin_sakit.setVisibility(View.VISIBLE);

                                mAdapterAll.addItems(items);
                                swipeRefresh.setRefreshing(false);

                                // check weather is last page or not
                                if (currentPage < totalPage) {
                                    // items.clear();
                                    mAdapterAll.addLoading();
                                } else {
                                    isLastPage = true;
                                }
                                isLoading = false;
                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                                Toast.makeText(ListIzinSakitApproveHRD.this,""+pesan,Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_SAKIT_HRD", "onError: " + anError.getErrorDetail());

                    }
                });


    }

    @Override
    public void onRefresh() {
        // Stopping Shimmer Effect's animation after data is loaded to ListView
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        recyler_izin_sakit.setVisibility(View.GONE);

        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapterAll.clear();
        paginationCall();
    }

    @Override
    public  void onResume(){
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }
}