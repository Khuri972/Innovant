package com.innovent.erp.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by craftbox-2 on 22-Feb-17.
 */

public class LocationService extends Service {
    public static final String BROADCAST_ACTION = "LOCATION.UPADATE";
    private static final long TWO_MINUTES = 1000 * 60 * 1;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    public double current_long;
    public double current_lat;

    long minTime = 60 * 1000; // Minimum time interval for update in seconds, i.e. 60 seconds.
    long minDistance = 10;

    Intent intent;
    MyPreferences myPreferences;
    DBHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        myPreferences = new MyPreferences(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);
    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            // Check whether the new location fix is more or less accurate
            int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;

            // Check if the old and new location are from the same provider
            boolean isFromSameProvider = isSameProvider(location.getProvider(),
                    currentBestLocation.getProvider());

            // Determine location quality using a combination of timeliness and accuracy
            if (isMoreAccurate) {
                return true;
            } else if (isNewer && !isLessAccurate) {
                return true;
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true;
            }
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }


        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(final Location loc) {
            //Toaster.show(LocationService.this,"service call",false,Toaster.DANGER);
            try {
                try {
                    if (!myPreferences.getPreferences(MyPreferences.Execuvive_out).equals("")) {
                        if (LogoutTime(myPreferences.getPreferences(MyPreferences.Execuvive_out))) {
                            //  true
                            myPreferences.setPreferences(MyPreferences.UDATE, "");
                            stopService(new Intent(LocationService.this, LocationService.class));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (myPreferences.getPreferences(MyPreferences.id).equals("")) {
                    stopService(new Intent(LocationService.this, LocationService.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isBetterLocation(loc, previousBestLocation)) { // isBetterLocation(loc, previousBestLocation)
                db = new DBHelper(LocationService.this);
                loc.getLatitude();
                loc.getLongitude();
                previousBestLocation = loc;
                current_lat = loc.getLatitude();
                current_long = loc.getLongitude();

                intent.putExtra("lat", loc.getLatitude());
                intent.putExtra("longi", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());

                if (GlobalElements.isConnectingToInternet(LocationService.this)) {
                    db.AddSalesTracking("" + GlobalElements.getDateFrom_YYYY_MM_DD(), "" + current_lat, "" + current_long, "tracking");
                } else {
                    db.AddSalesTracking("" + GlobalElements.getDateFrom_YYYY_MM_DD(), "" + current_lat, "" + current_long, "tracking");
                }
                sendBroadcast(intent);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    public static boolean LogoutTime(String start_time) {
        try {
            // current time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String ctime = dateFormat.format(date);
            Date startime = dateFormat.parse(start_time);
            Date current_time = dateFormat.parse(ctime);
            if (current_time.after(startime)) {
                System.out.println("Yes");
                return true;
            } else {
                System.out.println("No");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}