package com.app.mobiledev.apphris.memo;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.notifikasi.MainApplication;
import com.app.mobiledev.apphris.pdfHelper.PDFHelper;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.concurrent.Callable;

public class DetailLampiranMemo extends AppCompatActivity {

    private PDFView pdf_view;
    private String fileName = "", fileHal = "";
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    DownloadManager downloadManager;
    private FloatingActionButton fabDownload;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lampiran_memo);
        pdf_view=findViewById(R.id.pdfView);
        fileName = getIntent().getExtras().getString("file_lampiran");
        fileHal = getIntent().getExtras().getString("file_hal");
        fabDownload = findViewById(R.id.fabDownloadMemo);
        mToolbar = findViewById(R.id.toolbar_abs);
        mToolbar.setTitle(fileHal);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());
        mProgressDialog = new ProgressDialog(DetailLampiranMemo.this);
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.show();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new PDFHelper(this, fileName, api.URL_Link_lampiran_memo, new Callable() {
            @Override
            public Void call() {

                showPDF();
                return null;
            }
        }, new Callable<Void>() {
            @Override
            public Void call() {
                showError();
                //mprogressdialog.dismiss();
                return null;
            }
        });

        fabDownload.setOnClickListener(v -> {
            downloadMemo(fileHal, fileName);

        });

    }


    public void showPDF(){
        //Getting the saved PDF
        File file = new File(this.getExternalFilesDir("pdfs") + File.separator + fileName);
        //Loading the PDF
        pdf_view.fromFile(file)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

        mProgressDialog.dismiss();
    }

    public void showError(){
        Toast.makeText(this, "Error downloading ", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Deleting the PDF that was saved
        new File(this.getExternalFilesDir("pdfs")
                + File.separator + fileName).delete();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void downloadMemo(String fileHal, String fileName) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(api.URL_Link_lampiran_memo+fileName);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, fileHal+".pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        Log.d("TAG_DOWN_REF", "onClick: " + reference + downloadManager + request);
        showProgressDownload(reference);

    }

    private void showProgressDownload(Long reference) {
        final ProgressDialog progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setTitle("Mengunduh Memo");

        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", (dialog, whichButton) -> {
        });
        progressBarDialog.setProgress(0);
        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean downloading = true;

                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(reference);
                    //filter by id which you have receieved when reqesting download from download manager
                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            progressBarDialog.setProgress((int) dl_progress);

                        }
                    });

                    // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                    cursor.close();
                }

            }
        }).start();
        progressBarDialog.show();
    }

}
