package com.app.mobiledev.apphris.test.SecurityVerifikasi;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.app.mobiledev.apphris.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQR extends AppCompatActivity {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mScannerView.setResultHandler((ZXingScannerView.ResultHandler) ScanQR.this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mScannerView.stopCamera();
    }

    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();

        mScannerView.resumeCameraPreview((ZXingScannerView.ResultHandler) ScanQR.this);
    }
}

class openSCanner {

}