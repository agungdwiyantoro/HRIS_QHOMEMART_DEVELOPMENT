package com.app.mobiledev.apphris.getSystemInformation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.mobiledev.apphris.R;

public class getSystemInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_system_information);

        Button uploadSystemInformation = findViewById(R.id.bt_upload_system_information);
        uploadSystemInformation.setOnClickListener(v -> new SendRequest().execute());
    }
}