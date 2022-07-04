package com.app.mobiledev.apphris.service_location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import android.util.Log;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class push_location extends Activity   {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private Context mCtx;





    private void requestPermissions(Context mctx) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) mctx,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(push_location.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions((Activity) mctx,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }






    public void getLokasi(Context mctx,String userId) {
        LocationUpdate gt = new LocationUpdate(mctx);

        Location l = gt.getLocation();
        if( l == null){
            Log.d("CEK_LOKASI", "getLokasi: "+l);
        }else {
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            Geocoder geocoder = new Geocoder(mctx, Locale.getDefault());
            String result = null;
            try {
                List <Address> addressList = geocoder.getFromLocation(lat, lon, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)); //.append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    Log.d("getLOKASI", "getLokasi: "+address.getAddressLine(0));
                    //lokasi.setText(""+address.getAddressLine(0));
                    result = sb.toString();

                }
            } catch (IOException e) {
                Log.e("Location Address Loader", "Unable connect to Geocoder", e);

            }


        }

    }





}
