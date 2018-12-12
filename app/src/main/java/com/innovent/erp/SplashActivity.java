package com.innovent.erp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.innovent.erp.custom.EnglishNumberToWords;
import com.innovent.erp.custom.GPSTracker;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RuntimePermissionsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.File;
import java.io.IOException;

public class SplashActivity extends RuntimePermissionsActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    MyPreferences myPreferences;
    DBHelper db;
    GPSTracker gps;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myPreferences = new MyPreferences(SplashActivity.this);
        db = new DBHelper(SplashActivity.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.super.requestAppPermissions(new
                                String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.RECEIVE_SMS,
                                android.Manifest.permission.READ_PHONE_STATE
                        }, R.string.runtime_permissions_txt
                        , 20);
            }
        }, 500);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        try {
            if (requestCode == 20) {

                try {
                    db.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    TelephonyManager tele = (TelephonyManager) getApplicationContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String imei = tele.getDeviceId();
                    myPreferences.setPreferences(MyPreferences.imei, imei);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "" + GlobalElements.directory);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "" + GlobalElements.directory_document);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                gps = new GPSTracker(SplashActivity.this);
                if (gps.canGetLocation()) {

                    if (GlobalElements.isConnectingToInternet(SplashActivity.this)) {
                        if (myPreferences.getPreferences(MyPreferences.id).equals("")) {
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        GlobalElements.showDialog(SplashActivity.this);
                    }
                } else {
                    mGoogleApiClient.connect();
                    locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(30 * 1000);
                    locationRequest.setFastestInterval(5 * 1000);

                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(
                                    mGoogleApiClient,
                                    builder.build()
                            );

                    result.setResultCallback(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("", "");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("", "");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("", "");
        gps = new GPSTracker(SplashActivity.this);
        gps.showSettingsAlert();
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                try {
                    gps = new GPSTracker(SplashActivity.this);
                    if (gps.canGetLocation()) {
                        if (GlobalElements.isConnectingToInternet(SplashActivity.this)) {
                            if (myPreferences.getPreferences(MyPreferences.id).equals("")) {
                                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } else {
                            GlobalElements.showDialog(SplashActivity.this);
                        }
                    } else {
                        gps.showSettingsAlert();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                finish();
            }
        }
    }
}
