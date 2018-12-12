package com.innovent.erp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.custom.GPSTracker;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by om on 09-Jun-17.
 */

public class HourAlarmReceiver extends BroadcastReceiver {

    DBHelper db;
    MyPreferences myPreferences;
    JSONArray sales_tracking_array = new JSONArray();
    Context context;
    GPSTracker gps;
    public double latitude;
    public double longitude;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        db = new DBHelper(context);
        myPreferences = new MyPreferences(context);

        int i = 300;//= Integer.parseInt(text.getText().toString());
        intent = new Intent(context, HourAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);

        if (GlobalElements.isConnectingToInternet(context)) {
            //Toaster.show(context, "sync_hour", true, Toaster.DANGER);
            if (!myPreferences.getPreferences(MyPreferences.id).equals("")) {
                gps = new GPSTracker(context);
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat cu_date_yyyy_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String current_date_yyyy_time = cu_date_yyyy_time.format(c1.getTime());
                    db.AddSalesTracking(current_date_yyyy_time, "" + latitude, "" + longitude, "sync");
                }
                try {
                    sales_tracking_array = new JSONArray();
                    sales_tracking_array = db.GetSalesTracking();
                    if (sales_tracking_array != null) {
                        UpdateOrder();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void UpdateOrder() {
        try {
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.salesTrackingUpdate(myPreferences.getPreferences(MyPreferences.id), sales_tracking_array);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            db.DeleteTable(DBHelper.SALESEXECUTIVE_TRACKING);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.print("error" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
