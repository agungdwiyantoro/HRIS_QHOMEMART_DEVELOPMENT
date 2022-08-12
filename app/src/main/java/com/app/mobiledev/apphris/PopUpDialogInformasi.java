package com.app.mobiledev.apphris;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;

import java.net.URLEncoder;
import com.app.mobiledev.apphris.sesion.SessionManager;


public class PopUpDialogInformasi {

    public Dialog popUpInformasi(Dialog dialog, Activity activity, Context context, SessionManager sessionManager){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_dialog_informasi);
        dialog.setCancelable(false);

        Button btHubungiHRD = dialog.findViewById(R.id.bt_hubungi_HRD);
        ImageView keluar = dialog.findViewById(R.id.bt_keluar);
        btHubungiHRD.setOnClickListener(v -> openWhatsApp("+6282324281046","(NIK)-(Nama Lengkap)", context));

        keluar.setOnClickListener(v -> {
            Toast.makeText(context, helper.getDeviceId(context), Toast.LENGTH_SHORT).show();
       //     sessionManager.logout();
       //     Intent intent3 = new Intent(activity, splashScreen.class);
       //     activity.startActivity(intent3);
        //    activity.finish();
        });

        return dialog;
    }

    private void openWhatsApp(String numero, String mensaje, Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            } else {
                Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ERROR WHATSAPP", e.toString());
            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
        }
    }
}
