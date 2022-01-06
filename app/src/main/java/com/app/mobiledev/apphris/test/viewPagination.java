package com.app.mobiledev.apphris.test;

import static com.app.mobiledev.apphris.helperPackage.PaginationListener.PAGE_START;

import android.os.Bundle;
import android.os.Handler;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;

import com.app.mobiledev.apphris.helperPackage.PaginationListener;
import com.app.mobiledev.apphris.izin.izinSakit.modelIzinSakit;
import com.app.mobiledev.apphris.sesion.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class viewPagination extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "CEK_PAGINATION";


    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    private SessionManager msession;
    private String token;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pagination);
        mRecyclerView=findViewById(R.id.recyclerView);
        swipeRefresh=findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new PostRecyclerAdapter(new ArrayList<>());
        msession = new SessionManager(viewPagination.this);
        token = msession.getToken();
        mRecyclerView.setAdapter(adapter);
        doApiCall();
        //getRiwayatSakitAll();

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        mRecyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                doApiCall();
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

    /**
     * do api call here to fetch data from server
     * In example i'm adding data manually
     */
    private void doApiCall() {
        final ArrayList<PostItem> items = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < 10; i++) {
                    itemCount++;
//                    PostItem postItem = new PostItem();
//                    postItem.setTitle(getString(R.string.text_title) + itemCount);
//                    postItem.setDescription(getString(R.string.text_description));
//
//
//                    items.add(postItem);
                }
                Log.d(TAG, "run: "+itemCount);
                 getRiwayatSakitAll(itemCount);
                // do this all stuff on Success of APIs response
                /**
                 * manage progress view
                 */

            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        doApiCall();
    }

    private void getRiwayatSakitAll(int page) {
        AndroidNetworking.get(api.URL_IzinSakit_approve_head+"?limit="+page)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final ArrayList<PostItem> items = new ArrayList<>();
                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_APPROVE", "onResponse: " + status);
                            if (status.equals("200")) {
                                JSONArray jsonArray = response.getJSONArray("message");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    PostItem postItem = new PostItem();
                                   // postItem.setTitle(getString(R.string.text_title) + itemCount);
                                    postItem.setDescription(data.getString("name"));
                                    Log.d(TAG, "run: "+data.getString("name"));
                                    items.add(postItem);
                                }
                                if (currentPage != PAGE_START) adapter.removeLoading();
                                adapter.addItems(items);
                                swipeRefresh.setRefreshing(false);

                                // check weather is last page or not
                                if (currentPage < totalPage) {
                                    adapter.addLoading();
                                } else {
                                    isLastPage = true;
                                }
                                isLoading = false;
                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                               // Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
                          }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_RIWYAT_IZIN_SAKIT", "onError: " + anError.getErrorDetail());
                        Log.d("EROOR_RIWYAT_IZIN_SAKIT", "onError: " + api.URL_IzinSakit_approve_head+"?limit="+page);

                    }
                });


    }

    private void getRiwayatSakitAll() {
        AndroidNetworking.get(api.URL_IzinSakit_approve_head)
                .addHeaders("Authorization", "Bearer "+token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            Log.d("HASL_RESPONSE_APPROVE", "onResponse: " + status);
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





                                }




                            } else {
                                JSONObject object = response.getJSONObject("message");
                                String pesan = object.getString("lampiran_file");

                                //  Toast.makeText(formIzinSakit.this,""+pesan,toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_RIWYAT_IZIN_SAKIT", "onResponse: " + e);

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_IZIN_SAKITSS", "onError: " + anError.getErrorDetail());

                    }
                });


    }

}
