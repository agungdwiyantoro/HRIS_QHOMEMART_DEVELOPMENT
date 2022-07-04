package com.app.mobiledev.apphris.memo;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.set_ip;

public class DetailLampiranVideo extends AppCompatActivity {
    String lampiran_video;
    public VideoView video;
    ProgressDialog pd;
    static set_ip ip = new set_ip();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lampiran_video);
        lampiran_video   = getIntent().getExtras().getString("file_lampiran_video");


        video = findViewById(R.id.video);

        progressDialog();

        Uri uri = Uri.parse(ip.getIp_memo_video()+lampiran_video);
        video.setMediaController(new MediaController(this));
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                pd.dismiss();
            }
        });
    }

    private void progressDialog() {
        pd = new ProgressDialog(DetailLampiranVideo.this);
        pd.setMessage("Memuat video...");
        pd.show();
    }
}
