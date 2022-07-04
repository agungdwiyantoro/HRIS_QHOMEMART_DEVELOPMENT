package com.app.mobiledev.apphris.profile.PerjanjianKerja;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.pdfHelper.PDFHelper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.concurrent.Callable;

public class DetPerjanjianKerja extends AppCompatActivity {

    private PDFView pdf_view;
    private String filename = "", fileHal = "", kyano;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    DownloadManager downloadManager;
    private FloatingActionButton fabDownload;

    SessionManager sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_perjanjian_kerja);

        pdf_view=findViewById(R.id.pdfView);

        sp = new SessionManager(this);
        kyano = sp.getIdUser();

        filename = getIntent().getExtras().getString("file");

        Log.d("TAG_FILENAME", "onCreate: "+filename);

        fabDownload = findViewById(R.id.fabDownloadKontrak);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());
        mProgressDialog = new ProgressDialog(DetPerjanjianKerja.this);
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.show();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (filename.isEmpty() || filename.equals("null")) {
            mProgressDialog.dismiss();
            Toast.makeText(this, "Dokumen Perjanjian Kerja masih kosong", Toast.LENGTH_LONG).show();
        } else {
            //http://hris.qhomedata.id/upload/karyawan/0718210504120192/lampiran/kontrak/20220604-KONTRAK-001_HR-PKWT_QSA_IV_2022-0718210504120192.pdf
            String link = api.URL_pdf_kontrak+kyano+"/lampiran/kontrak/";
            new PDFHelper(this, filename, link, new Callable() {
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
        }

        fabDownload.setOnClickListener(v -> {

            if (filename.isEmpty() || filename.equals("null")) {
                Toast.makeText(this, "Dokumen Perjanjian Kerja masih kosong", Toast.LENGTH_LONG).show();
            } else {
                downloadKontrak(fileHal, filename);
            }

        });

    }

    public void showPDF(){
        //Getting the saved PDF
        File file = new File(this.getExternalFilesDir("pdfs") + File.separator + filename);
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
                + File.separator + filename).delete();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void downloadKontrak(String fileHal, String filename) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(api.URL_pdf_kontrak+kyano+"/lampiran/kontrak/"+filename);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, fileHal+".pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        Log.d("TAG_DOWN_REF", "onClick: " + reference + downloadManager + request);
        showProgressDownload(reference);

    }

    private void showProgressDownload(Long reference) {
        final ProgressDialog progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setTitle("Mengunduh Dokumen Kontrak Kerja");

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