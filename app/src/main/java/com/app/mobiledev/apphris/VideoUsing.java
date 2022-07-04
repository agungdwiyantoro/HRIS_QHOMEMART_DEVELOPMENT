package com.app.mobiledev.apphris;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.mobiledev.apphris.sesion.SessionManager;

import java.net.URLEncoder;

public class VideoUsing extends AppCompatActivity {

    //add fiture video
    public VideoView video;
    private String no_admin;
    public String video_url = "http://hris.qhomedata.id/video/video_tutor.mp4";
    ProgressDialog pd;
    TextView txHelp;
    SessionManager msession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_using);

        video = findViewById(R.id.video);

        progressDialog();

        Uri uri = Uri.parse(video_url);
        video.setMediaController(new MediaController(this));
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
        msession =new SessionManager(this);
        no_admin=msession.getNo_hp_admin();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                pd.dismiss();
            }
        });

        txHelp = findViewById(R.id.btnBantuan);
        txHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openWhatsApp(no_admin,"(NIK)-(Nama Lengkap) : ");
            }
        });
    }

    private void progressDialog() {
        pd = new ProgressDialog(VideoUsing.this);
        pd.setMessage("Memuat video...");
        pd.show();
    }

    private void openWhatsApp(String numero,String mensaje){

        try{
            PackageManager packageManager = VideoUsing.this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Toast.makeText(VideoUsing.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            Toast.makeText(VideoUsing.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
        }

    }
}
