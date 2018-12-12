package com.innovent.erp.netUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CRAFT BOX on 1/23/2017.
 */

public class MyPreferences {

    Context context;

    public static String PreferenceName = "cdms";
    public static String EncraptionKey = "cdms@2442";

    public static String id = "u_id", name = "name", email = "email", mobile_no = "mobile_no", login_type = "login_type";
    public static String imei = "imei", refreshedToken = "refreshedToken";

    public static String Label = "label", Employee = "employee", Tag = "tag", Job_title = "job_title";

    public static String contact_view = "contact_view", contact_insert = "contact_insert", contact_update = "contact_update", contact_delete = "contact_delete";
    public static String document_view = "document_view", document_insert = "document_insert", document_update = "document_update", document_delete = "document_delete";
    public static String couriers_view = "couriers_view", couriers_insert = "couriers_insert", couriers_update = "couriers_update", couriers_delete = "couriers_delete";
    public static String task_view = "task_view", task_insert = "task_insert", task_update = "task_update", task_delete = "task_delete";
    public static String todayTask_view = "todayTask_view", todayTask_insert = "todayTask_insert", todayTask_update = "todayTask_update", todayTask_delete = "todayTask_delete";
    public static String tag_view = "tag_view", tag_insert = "tag_insert", tag_update = "tag_update", tag_delete = "tag_delete";
    public static String employee_view = "employee_view", employee_insert = "employee_insert", employee_update = "employee_update", employee_delete = "employee_delete";
    public static String label_view = "label_view", label_insert = "label_insert", label_update = "label_update", label_delete = "label_delete";
    public static String cheque_view = "cheque_view", cheque_insert = "cheque_insert", cheque_update = "cheque_update", cheque_delete = "cheque_delete";

    /* todo employee moduel */
    public static String daily_sales_report_view = "daily_sales_report_view", daily_sales_report_insert = "daily_sales_report_insert", daily_sales_report_update = "daily_sales_report_update", daily_sales_report_delete = "daily_sales_report_delete";
    public static String daily_service_report_view = "daily_service_report_view", daily_service_report_insert = "daily_service_report_insert", daily_service_report_update = "daily_service_report_update", daily_service_report_delete = "daily_service_report_delete";
    public static String daily_work_report_view = "daily_work_report_view", daily_work_report_insert = "daily_work_report_insert", daily_work_report_update = "daily_work_report_update", daily_work_report_delete = "daily_work_report_delete";
    public static String leave_view = "leave_view", leave_insert = "leave_insert", leave_update = "leave_update", leave_delete = "leave_delete";
    public static String expence_view = "expence_view", expence_insert = "expence_insert", expence_update = "expence_update", expence_delete = "expence_delete";
    /* todo */


    public static String currencyArray = "currencyArray";
    public static String UDATE = "u_date", Execuvive_in_min = "execuvive_in_min", Execuvive_in_max = "execuvive_in_max", Execuvive_out = "execuvive_out";

    /* todo visitorBook  */
    public static String INSERT_FLAG = "insert_flag", VIEW_FLAG = "view_flag", delete = "delete", followup = "followup", projects_array = "projects_array";
    public static String project_id = "project_id";
    public static String project_title = "project_title";
    public static String notification_count = "notification_count";
    /* end */

    public MyPreferences(Context context) {
        this.context = context;
    }

    String value = "";

    public String getPreferences(String key) {
        value = "";
        try {
            SharedPreferences channel = context.getSharedPreferences("" + PreferenceName, Context.MODE_PRIVATE);
            value = AESCrypt.decrypt("" + EncraptionKey, channel.getString("" + key, "").toString());
        } catch (Exception e) {
            e.printStackTrace();
            value = "";
            return value;
        }
        return value;
    }

    public void setPreferences(String key, String value) {
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences("" + PreferenceName, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedpreferences.edit();
            ed.putString("" + key, AESCrypt.encrypt("" + EncraptionKey, "" + value));
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clearPreferences() {
        try {
            SharedPreferences settings = context.getSharedPreferences("" + PreferenceName, Context.MODE_PRIVATE);
            return settings.edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* todo tutoral praferance */

    public static String PreferenceTutoralName = "cdms_tutoral";

    public static String dashboard_tutorial = "dashboard_tutorial";
    public static String my_contact_tutorial = "my_contact_tutorial";
    public static String my_document_tutorial = "my_document_tutorial";
    public static String courier_tutorial = "courier_tutorial";
    public static String my_task_tutorial = "my_task_tutorial";
    public static String cheque_tutorial = "cheque_tutorial";

    public String getTutoralPreferences(String key) {
        String value = null;
        try {
            SharedPreferences channel = context.getSharedPreferences("" + PreferenceTutoralName, Context.MODE_PRIVATE);
            value = AESCrypt.decrypt("" + EncraptionKey, channel.getString("" + key, "").toString());
        } catch (Exception e) {
            e.printStackTrace();
            value = "";
            return value;
        }

        return value;
    }

    public void setTutoralPreferences(String key, String value) {
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences("" + PreferenceTutoralName, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedpreferences.edit();
            ed.putString("" + key, AESCrypt.encrypt("" + EncraptionKey, "" + value));
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clearTutoralPreferences() {
        try {
            SharedPreferences settings = context.getSharedPreferences("" + PreferenceTutoralName, Context.MODE_PRIVATE);
            return settings.edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* todo alias preference */

    public static String Preferencealias = "cdms_alias";
    public static String change_to_alias = "change_to_alias";
    public static String aliasArray = "aliasArray";
    public static String currency = "currency";

    public String getaliasPreferences(String key) {
        String value = null;
        try {
            SharedPreferences channel = context.getSharedPreferences("" + Preferencealias, Context.MODE_PRIVATE);
            value = AESCrypt.decrypt("" + EncraptionKey, channel.getString("" + key, "").toString());
        } catch (Exception e) {
            e.printStackTrace();
            value = "";
            return value;
        }

        return value;
    }

    public void setaliasPreferences(String key, String value) {
        try {
            SharedPreferences sharedpreferences = context.getSharedPreferences("" + Preferencealias, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedpreferences.edit();
            ed.putString("" + key, AESCrypt.encrypt("" + EncraptionKey, "" + value));
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean clearaliasPreferences() {
        try {
            SharedPreferences settings = context.getSharedPreferences("" + Preferencealias, Context.MODE_PRIVATE);
            return settings.edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
