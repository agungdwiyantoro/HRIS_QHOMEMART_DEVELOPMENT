package com.app.mobiledev.apphris.approve.adminIzinSakitHead;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.approve.adminIzinSakitHead.adapterIzinSakitApprove.adapterIzinSakitApproveHeadAll;
import com.app.mobiledev.apphris.helperPackage.PaginationListener;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListIzinSakitApproveHead extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyler_izin_sakit;
    private List<modelIzinSakit> modelIzinSakits;
    private String token;
    private String TAG="LISIzinSakitApprove";
    private ImageView img_back;
    private SessionManager msession;
    private RadioGroup rbFilter;
    private LinearLayout lin_transparant;
    private SwipeRefreshLayout swipeRefresh;
    private TextView  tx_approve;


    private Boolean all=true;


    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    LinearLayoutManager layoutManager;
    adapterIzinSakitApproveHeadAll mAdapterALL;
    private ShimmerFrameLayout mShimmerViewContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pengajuan_approve);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyler_izin_sakit = findViewById(R.id.recyler_izin_sakit);
        rbFilter=findViewById(R.id.rbFilter);
        img_back = findViewById(R.id.img_back);
        lin_transparant=findViewById(R.id.lin_transparant);
        swipeRefresh=findViewById(R.id.swipeRefresh);
        tx_approve=findViewById(R.id.tx_approve);
        swipeRefresh.setOnRefreshListener(this);
        msession = new SessionManager(ListIzinSakitApproveHead.this);
        modelIzinSakits = new ArrayList<>();
        token = msession.getToken();
        layoutManager = new LinearLayoutManager(this);
        recyler_izin_sakit.setLayoutManager(layoutManager);
        mAdapterALL = new adapterIzinSakitApproveHeadAll(new ArrayList<>(), ListIzinSakitApproveHead.this);
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_sakit.setVisibility(View.GONE);
        recyler_izin_sakit.setAdapter(mAdapterALL);
        paginationCall();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tx_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterALL.removeLoading();
            }
        });

        rbFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId==R.id.news){
                    all=false;
                    recyler_izin_sakit.setLayoutManager(layoutManager);
                    mAdapterALL = new adapterIzinSakitApproveHeadAll(new ArrayList<>(), ListIzinSakitApproveHead.this);
                    itemCount = 0;
                    currentPage = PAGE_START;
                    isLastPage = false;
                    mAdapterALL.clear();
                    mShimmerViewContainer.startShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    recyler_izin_sakit.setVisibility(View.GONE);
                    recyler_izin_sakit.setAdapter(mAdapterALL);
                    paginationCall();

                } else {
                    all=true;
                    recyler_izin_sakit.setLayoutManager(layoutManager);
                    mAdapterALL = new adapterIzinSakitApproveHeadAll(new ArrayList<>(), ListIzinSakitApproveHead.this);
                    itemCount = 0;
                    currentPage = PAGE_START;
                    isLastPage = false;
                    mAdapterALL.clear();
                    mShimmerViewContainer.startShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    recyler_izin_sakit.setVisibility(View.GONE);
                    recyler_izin_sakit.setAdapter(mAdapterALL);
                    paginationCall();
                }
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
    }







    private void paginationCall() {
        final ArrayList<modelIzinSakit> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < 10; i++) {
                    itemCount++;
                }

                Log.d("jml_count", "run: "+all);
                int offset=0;
                if(itemCount>10){
                    offset=(itemCount-totalPage)+1;
                }
                recyler_izin_sakit.setHasFixedSize(true);
                Log.d("cek_url_all", "run: "+api.URL_IzinSakit_approve_head+"?limit="+itemCount+"&offset="+offset);
                getRiwayatSakitAll(itemCount,offset,items);
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
        // Stopping Shimmer Effect's animation after data is loaded to ListView

        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapterALL.clear();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyler_izin_sakit.setVisibility(View.GONE);
        recyler_izin_sakit.setAdapter(mAdapterALL);
        paginationCall();
    }


    private void getRiwayatSakitAll(int page,int offset,ArrayList items) {
        if(all==false){
            AndroidNetworking.get(api.URL_IzinSakit_approve_head+"?limit="+page+"&offset="+offset+"&status=0")
                    .addHeaders("Authorization", "Bearer "+token)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //mAdapter.clear();
                                String status = response.getString("status");
                                String message = response.getString("message");
                                Log.d("HASL_RESPONSE_NEW", "onResponse: " + status);
                                if (status.equals("200")) {
                                    if(!message.equals("null")){

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

                                        if (currentPage != PAGE_START) mAdapterALL.removeLoading();
                                        mAdapterALL.addItems(items);
                                        swipeRefresh.setRefreshing(false);
                                        Log.d("CUURENT_PAGE", "onResponse: "+items.size());

//                                        if((items.size()+1<totalPage)&&currentPage==1){
//                                            mAdapterALL.removeLoading();
//                                        }else{
//                                            if (currentPage < totalPage) {
//                                                mAdapterALL.addLoading();
//                                            }else{
//                                                isLastPage = true;
//                                            }
//                                        }

                                        if (currentPage < totalPage) {
                                            mAdapterALL.addLoading();
                                        }else{
                                            isLastPage = true;
                                        }
                                        isLoading = false;

                                    }else{
                                        mAdapterALL.removeLoading();
                                    }

                                } else {
                                    JSONObject object = response.getJSONObject("message");
                                    String pesan = object.getString("lampiran_file");
                                    mShimmerViewContainer.stopShimmerAnimation();
                                    mShimmerViewContainer.setVisibility(View.GONE);

                                    recyler_izin_sakit.setVisibility(View.VISIBLE);
                                    mAdapterALL.removeLoading();

                                    Toast.makeText(ListIzinSakitApproveHead.this,""+pesan,Toast.LENGTH_SHORT).show();
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
        }else{
            AndroidNetworking.get(api.URL_IzinSakit_approve_head+"?limit="+page+"&offset="+offset)
                    .addHeaders("Authorization", "Bearer "+token)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //mAdapter.clear();
                                String status = response.getString("status");
                                String message = response.getString("message");
                                Log.d("HASL_RESPONSE_NEW", "onResponse: " + status);
                                if (status.equals("200")) {
                                    if(!message.equals("null")){

                                        JSONArray jsonArray = response.getJSONArray("message");

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject data = jsonArray.getJSONObject(i);
                                            if(!data.getString("approve_head").equals("null")){
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
                                            }




                                        }

                                        if (currentPage != PAGE_START) mAdapterALL.removeLoading();
                                        mAdapterALL.addItems(items);
                                        swipeRefresh.setRefreshing(false);
                                        Log.d("CEK_ITEMS", "onResponse: items "+items.size()+1+" remove"+totalPage);

//                                        if((items.size()+1<totalPage)&&currentPage==1){
//                                            mAdapterALL.removeLoading();
//                                        }else{
//                                            if (currentPage < totalPage) {
//                                                mAdapterALL.addLoading();
//                                            }else{
//                                                isLastPage = true;
//                                            }
//                                        }

                                        if (currentPage < totalPage) {
                                            mAdapterALL.addLoading();
                                        }else{
                                            isLastPage = true;
                                        }
                                        isLoading = false;



                                    }else{
                                        mAdapterALL.removeLoading();
                                    }

                                } else {
                                    JSONObject object = response.getJSONObject("message");
                                    String pesan = object.getString("lampiran_file");
                                    mShimmerViewContainer.stopShimmerAnimation();
                                    mShimmerViewContainer.setVisibility(View.GONE);

                                    recyler_izin_sakit.setVisibility(View.VISIBLE);
                                    mAdapterALL.removeLoading();

                                    Toast.makeText(ListIzinSakitApproveHead.this,""+pesan,Toast.LENGTH_SHORT).show();
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