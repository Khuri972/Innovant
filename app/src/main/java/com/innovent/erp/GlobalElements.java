package com.innovent.erp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


import com.innovent.erp.music.Logger;
import com.innovent.erp.model.ViewpagerModel;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CRAFT BOX on 10/6/2017.
 */

public class GlobalElements extends Application {

    public static String directory = "/Cdms";
    public static String directory_document = "Cdms/Document";
    public static int ll_count = 20;
    public static String fileprovider_path = "com.cdms.fileprovider";
    public static String path = "";
    public static boolean isChatActive = false;

    /* todo task filter variable */
    public static String taskTitle = "";
    public static String taskAssignedById = "";
    public static String taskAssignedTo = "";
    public static String taskStatus = "";
    public static String toDate = "";
    public static String fromeDate = "";
    public static String actionShowHide = "0";
    public static String task_type = "";
    public static String admintype = "1";

    /* todo contact filter */
    public static String city = "";
    public static String zone = "";
    public static String tagids = "";
    public static String labelids = "";

    /* todo courier */
    public static String courierType = "receiver";

    public static ArrayList<ViewpagerModel> viewpagerModels = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (info != null) {
                if (info.isConnected()) {
                    return true;
                } else {
                    NetworkInfo info1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (info1.isConnected()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static void showDialog(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Set Dialog Title
        alertDialog.setTitle("Internet Connection");
        // Set Dialog Message
        alertDialog.setMessage("Please check your internet connection ..");
        // Set OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Show Alert Message
        alertDialog.show();
    }

    public static boolean getVersionCheck() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


    public static boolean beforeDate(String fromDate, String ToDate, String formatOfDate) {
        boolean success = false;
        SimpleDateFormat sdf = new SimpleDateFormat(formatOfDate);
        try {
            if (sdf.parse(fromDate).before(sdf.parse(ToDate)) || sdf.parse(fromDate).compareTo(sdf.parse(ToDate)) == 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return success;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static String DecimalFormat(String value) {
        DecimalFormat doubleFormat = new DecimalFormat("#.##");
        doubleFormat.setMinimumFractionDigits(2);
        value = doubleFormat.format(Double.parseDouble(value));
        return value;
    }

    public static String DecimalFormat(String value, int dot) {
        DecimalFormat doubleFormat = new DecimalFormat("#.##");
        doubleFormat.setMinimumFractionDigits(dot);
        value = doubleFormat.format(Double.parseDouble(value));
        return value;
    }

    public static String getCurrentdate() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat cu_date = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = cu_date.format(newCalendar.getTime());
        return current_date;
    }

    public static String FirstRemoveZero(String text) {
        text = text.replaceFirst("^0-*", "");
        return text;
    }

    public static String rights(String rights_type, String rights_name) {
        try {
            JSONObject result = new JSONObject(rights_type);
            return result.getString(rights_name);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getRemoveLastComma(String text) {
        String refine_txt = null;
        try {
            if (text.equals("")) {
                return "";
            } else {
                refine_txt = text.substring(0, text.lastIndexOf(","));
                return refine_txt;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return refine_txt = "";
        }
    }

    public static String getTime() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat cu_date_time = new SimpleDateFormat("HH:mm a");
        String current_time = cu_date_time.format(newCalendar.getTime());
        return current_time;
    }

    public static boolean compareDate(String fromDate, String formatOfDate) {
        boolean success = false;
        try {
            if (fromDate.equals("")) {
                return false;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                //Date date1 = sdf.parse("31-03-2016");
                Date date1 = sdf.parse("" + fromDate);
                Date date2 = sdf.parse("" + sdf.format(new Date()));
                if (date1.equals(date2)) {
                    success = true;
                } else {
                    success = false;
                }
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
            return success;
        }
        return success;
    }

    public static String checkDateFormate(String dateFormate) {

        if (dateFormate.matches("\\d{4}-\\d{2}-\\d{2}")) {

        }

        return "";
    }

    public static boolean compareDateAfter(String fromDate) {
        boolean success = false;
        try {
            if (fromDate.equals("")) {
                return false;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //Date date1 = sdf.parse("31-03-2016");
                Date date1 = sdf.parse("" + fromDate);
                Date date2 = sdf.parse("" + sdf.format(new Date()));
                if (date1.before(date2)) {
                    success = true;
                } else {
                    success = false;
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return success;
        }
        return success;
    }

    public static String printDifference(String sDate, String eDate) {
        //milliseconds

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date startDate = null, endDate = null;
        try {
            startDate = simpleDateFormat.parse("" + sDate);
            endDate = simpleDateFormat.parse("" + eDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return "" + elapsedDays + " " + elapsedHours + " " + elapsedMinutes + " " + elapsedSeconds;

    }

    public static GradientDrawable RoundedButtion_gray_redies_10(Context context) {
        float radii = context.getResources().getDimension(R.dimen.toast_redius);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{radii, radii, radii, radii, radii, radii, radii, radii});
        shape.setStroke(2, ContextCompat.getColor(context, R.color.divider_color));
        shape.setColor(ContextCompat.getColor(context, R.color.white));
        return shape;
    }

    public static void editTextAllCaps(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    editTextAllCaps(context, child);
                }
            } else if (v instanceof EditText) {
                ((EditText) v).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            } else if (v instanceof AutoCompleteTextView) {
                ((AutoCompleteTextView) v).setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            }
        } catch (Exception e) {
        }
    }

    public static String getDateFrom_YYYY_MM_DD() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat cu_date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String current_date = cu_date.format(newCalendar.getTime());
        return current_date;
    }

    public static String getDateFrom_dd_MM_YYYY() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat cu_date = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String current_date = cu_date.format(newCalendar.getTime());
        return current_date;
    }

    public static String getDateFrom_YYYY_MM() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat cu_date = new SimpleDateFormat("yyyy-MM-dd");
        String current_date = cu_date.format(newCalendar.getTime());
        return current_date;
    }

    /* todo music */

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean applicationVisible = false;

    public static boolean isapplicationVisible() {
        return applicationVisible;
    }

    public static void activityDestroyed() {

        applicationVisible = false;
        Logger.LogInfo(applicationVisible + "activty destroy");
    }

    public static void activityCreated() {

        applicationVisible = true;
        Logger.LogInfo(applicationVisible + "");
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    /* end */

}
