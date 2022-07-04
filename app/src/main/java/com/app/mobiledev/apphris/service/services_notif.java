package com.app.mobiledev.apphris.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.set_ip;
import com.app.mobiledev.apphris.main_fragment;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class services_notif extends Service {
    private boolean isRunning;
    private Thread backgroundThread;
    private Socket socket;
    private String iduser="";
    public static final String CHANNEL_ID = "com.app.mobiledev.apphris.ANDROID";
    private String nik="";
    private String url="";
    private NotificationManager mNotificationManager;
    Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    long timestamp;
    static set_ip ip = new set_ip();
    private SessionManager sessionmanager;
    public static final int NOTIF_ID = 56;
    public services_notif() {

    }

    @Override
    public void onCreate() {
        this.isRunning = false;
        Log.d("start_service", "onCreate: ");
        sessionmanager = new SessionManager(getApplicationContext());
        sessionmanager.createIdTraining("");
        this.backgroundThread = new Thread(myTask);

        try {
            if(!sessionmanager.getIdUser().equals("")){
                iduser=sessionmanager.getIdUser();
                nik=sessionmanager.getNik();
            }

        }catch (NullPointerException e){
            Log.d("NULLPOINTER", "onCreate: "+e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            // do something in here
            Log.d("NODE_JS_CEK", "call: ");
            try {
                socket = IO.socket(ip.ip_notif);
                socket.connect();
                socket.emit("join", "android");
                socket.on("notif_training",notif);
                socket.on("notif_memo",notif_memo);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.d("NODE_JS_ERROR_MYTASK", "call: "+e);
            }

            //TODO - how to handle socket events here?
            //How do I do something like mSocket.on(Socket.EVENT_CONNECT,onConnect); here?
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( !this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }

        return START_STICKY;
    }
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void push_notif(String title, String detail){
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.hris);
        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getApplicationContext().getPackageName() + "/raw/hris");
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent open_form = new Intent(getApplicationContext(), main_fragment.class);
        open_form.putExtra("service_notif", "TRUE");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, open_form, 0);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("");
        bigText.setBigContentTitle("HRIS MOBILE");
        bigText.setSummaryText("");
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(""+title);
        mBuilder.setContentText(""+detail);
        mBuilder.setSound(alarmsound);
        mBuilder.setOngoing(true);
        mBuilder.setAutoCancel(true);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        mBuilder.setStyle(bigText);
        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "HRIS_MOBILE";
            NotificationChannel channel = new NotificationChannel(channelId , ""+title, NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(alarmsound,audioAttributes);
            channel.setDescription(""+detail);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mNotificationManager.cancel("NOTIF", NOTIF_ID);
            mBuilder.setChannelId(channelId);
            startForeground(NOTIF_ID, mBuilder.build());

        }else{
            startService(new Intent(getApplicationContext(), services_notif.class));
            startForeground(NOTIF_ID, mBuilder.build());
        }


    }
    private void createNotification(String val,String materi) {
        // Construct pending intent to serve as action for notification item
        Intent intent = new Intent(this, main_fragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // Create notification
        String longText = ""+val;

        Notification noti =
                new NotificationCompat.Builder(this, DemoApplication.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setSound(alarmsound)
                        .setContentTitle("HRIS MOBILE")
                        .setContentText(""+materi)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
                        .setContentIntent(pIntent)

                        .build();

        // Hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIF_ID, noti);
        Log.d("NOTIF_CREATE", "createNotification: "+ noti);


    }

    private Emitter.Listener notif = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String kyano="";
                String response = data.getString("pengirim");
                JSONObject Object = new JSONObject(response);
                String no_training=Object.getString("kode");
                String materi=Object.getString("materi");
                Log.d("NODE_JS_ERROR_MEMO", "call: "+response+" SEND_TO"+materi);
                JSONArray jsonArray = Object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectData = jsonArray.getJSONObject(i);
                    kyano= objectData.getString("kyano");
                    if(kyano.equals(iduser)){
                        push_notif("Info Training: ",""+materi);
                        sessionmanager.createIdTraining(no_training);
                        //createNotification("Info Training",materi);
                    }

                }





//                if(getnik.equals(nik)){
//                    push_notif(""+getnik,""+getpesan);
//                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("NODE_JS_ERROR_TRAINING", "call: "+e.getMessage());
            }

        }
    };

    private Emitter.Listener notif_memo = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String kyano="";
                String hal="";
                String response = data.getString("pengirim");
                JSONObject Object = new JSONObject(response);
                String jenis_kirim=Object.getString("sendto");

                JSONArray jsonArray = Object.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectData = jsonArray.getJSONObject(i);
                    if(iduser.equals(objectData.getString("kyano"))){
                        kyano= objectData.getString("kyano");
                    }
                    hal=objectData.getString("hal");
                }

               //Log.d("NODE_JS_MEMO_1", "kyano_session: "+iduser+" kyano_form_node:"+kyano);
                if(jenis_kirim.equals("ALL")){
                    push_notif("Memo Mobile : ",""+hal);
                }else if(jenis_kirim.equals("ONE")){
                    if(kyano.equals(iduser)){
                        push_notif("Memo Mobile : ",""+hal);
                        Log.d("NODE_JS_MEMO_2", "call: "+response+" SEND_TO"+jenis_kirim);
                    }
                }








            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("NODE_JS_ERROR_MEMO", "call: "+e.getMessage());
            }

        }
    };





}
