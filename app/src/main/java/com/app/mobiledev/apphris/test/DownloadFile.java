package com.app.mobiledev.apphris.test;

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
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.app.mobiledev.apphris.R;

public class DownloadFile extends AppCompatActivity {

    private static String file_url = "https://file-examples-com.github.io/uploads/2017/10/file-example_PDF_1MB.pdf";

    Button button;
    DownloadManager downloadManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);

        button = findViewById(R.id.btnDownload);

        button.setOnClickListener(v -> getDownloadMemo());

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getDownloadMemo() {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        //Uri uri = Uri.parse("http://www.africau.edu/images/default/sample.pdf");
        Uri uri = Uri.parse("https://file-examples-com.github.io/uploads/2017/10/file-example_PDF_1MB.pdf");

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "sample_pdf.pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        Long reference = downloadManager.enqueue(request);

        Log.d("TAG_DOWN_REF", "onClick: " + reference);

        showProgressDownload(reference);
    }

    private void showProgressDownload(Long reference) {
        final ProgressDialog progressBarDialog = new ProgressDialog(this);
        progressBarDialog.setTitle("Download App Data, Please Wait");

        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int whichButton) {
                // Toast.makeText(getBaseContext(),
                //       "OK clicked!", Toast.LENGTH_SHORT).show();
            }
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


        //show the dialog
        progressBarDialog.show();

    }

}