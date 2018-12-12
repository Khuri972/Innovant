package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.custom.Toaster;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_url_path)
    TextView loginUrlPath;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.login_main_linear)
    LinearLayout loginMainLinear;

    String refreshedtoken = "";
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(LoginActivity.this);

        try {
            TelephonyManager tele = (TelephonyManager) getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.MOBILE, number.getText().toString())) {
                    Toaster.show(LoginActivity.this, "Enter valid mobile number", false, Toaster.DANGER);
                    return;
                } else if (!Validation.isValid(Validation.BLANK_CHECK, password.getText().toString())) {
                    Toaster.show(LoginActivity.this, "Enter password", false, Toaster.DANGER);
                    return;
                } else {
                    try {
                        refreshedtoken = FirebaseInstanceId.getInstance().getToken();
                        if (refreshedtoken == null || refreshedtoken.equals("")) {
                            refreshedtoken = FirebaseInstanceId.getInstance().getToken();
                            myPreferences.setPreferences(MyPreferences.refreshedToken, refreshedtoken);

                            AlertDialog buildInfosDialog;
                            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(LoginActivity.this);
                            alertDialog2.setMessage("We could not get your device information try again");

                            alertDialog2.setPositiveButton("Try again",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to execute after dialog
                                            try {
                                                refreshedtoken = FirebaseInstanceId.getInstance().getToken();
                                                myPreferences.setPreferences(MyPreferences.refreshedToken, refreshedtoken);

                                                if (refreshedtoken == null || refreshedtoken.equals("")) {
                                                    refreshedtoken = FirebaseInstanceId.getInstance().getToken();
                                                    myPreferences.setPreferences(MyPreferences.refreshedToken, refreshedtoken);
                                                    return;
                                                }
                                                if (GlobalElements.isConnectingToInternet(LoginActivity.this)) {
                                                    loginUser();
                                                } else {
                                                    GlobalElements.showDialog(LoginActivity.this);
                                                }
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });

                            buildInfosDialog = alertDialog2.create();
                            buildInfosDialog.show();

                            return;
                        } else {
                            if (GlobalElements.isConnectingToInternet(LoginActivity.this)) {
                                loginUser();
                            } else {
                                GlobalElements.showDialog(LoginActivity.this);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void loginUser() {
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.LoginUser(number.getText().toString(), password.getText().toString(), myPreferences.getPreferences(MyPreferences.imei), refreshedtoken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        JSONArray rights = json.getJSONArray("rights");
                        try {
                            myPreferences.setPreferences(MyPreferences.login_type,""+result.getString("type"));
                            myPreferences.setPreferences(MyPreferences.name, "" + result.getString("name"));
                            myPreferences.setPreferences(MyPreferences.email, "" + result.getString("email"));
                            myPreferences.setPreferences(MyPreferences.mobile_no, "" + result.getString("phone_1"));
                            myPreferences.setPreferences(MyPreferences.Execuvive_in_max, "" + result.getString("max_working_time"));
                            myPreferences.setPreferences(MyPreferences.Execuvive_in_min, "" + result.getString("min_working_time"));
                            myPreferences.setPreferences(MyPreferences.Execuvive_out, "" + result.getString("working_end_time"));

                            if (result.getString("type").equals("0")) {

                                myPreferences.setaliasPreferences(MyPreferences.change_to_alias,"1");
                                myPreferences.setPreferences(MyPreferences.tag_view, "1");
                                myPreferences.setPreferences(MyPreferences.tag_insert, "1");
                                myPreferences.setPreferences(MyPreferences.tag_update, "1");
                                myPreferences.setPreferences(MyPreferences.tag_delete, "1");
                                myPreferences.setPreferences(MyPreferences.employee_view, "1");
                                myPreferences.setPreferences(MyPreferences.employee_insert, "1");
                                myPreferences.setPreferences(MyPreferences.employee_update, "1");
                                myPreferences.setPreferences(MyPreferences.employee_delete, "1");
                                myPreferences.setPreferences(MyPreferences.label_view, "1");
                                myPreferences.setPreferences(MyPreferences.label_insert, "1");
                                myPreferences.setPreferences(MyPreferences.label_update, "1");
                                myPreferences.setPreferences(MyPreferences.label_delete, "1");

                                myPreferences.setPreferences(MyPreferences.contact_view, "1");
                                myPreferences.setPreferences(MyPreferences.contact_insert, "1");
                                myPreferences.setPreferences(MyPreferences.contact_update, "1");
                                myPreferences.setPreferences(MyPreferences.contact_delete, "1");
                                myPreferences.setPreferences(MyPreferences.document_view, "1");
                                myPreferences.setPreferences(MyPreferences.document_insert, "1");
                                myPreferences.setPreferences(MyPreferences.document_update, "1");
                                myPreferences.setPreferences(MyPreferences.document_delete, "1");
                                myPreferences.setPreferences(MyPreferences.couriers_view, "1");
                                myPreferences.setPreferences(MyPreferences.couriers_insert, "1");
                                myPreferences.setPreferences(MyPreferences.couriers_update, "1");
                                myPreferences.setPreferences(MyPreferences.couriers_delete, "1");
                                myPreferences.setPreferences(MyPreferences.task_view, "1");
                                myPreferences.setPreferences(MyPreferences.task_insert, "1");
                                myPreferences.setPreferences(MyPreferences.task_update, "1");
                                myPreferences.setPreferences(MyPreferences.task_delete, "1");
                                myPreferences.setPreferences(MyPreferences.cheque_view, "1");
                                myPreferences.setPreferences(MyPreferences.cheque_insert, "1");
                                myPreferences.setPreferences(MyPreferences.cheque_update, "1");
                                myPreferences.setPreferences(MyPreferences.cheque_delete, "1");
                                myPreferences.setPreferences(MyPreferences.INSERT_FLAG, "1");
                                myPreferences.setPreferences(MyPreferences.VIEW_FLAG, "1");
                                myPreferences.setPreferences(MyPreferences.delete, "1");
                                myPreferences.setPreferences(MyPreferences.followup, "1");

                                myPreferences.setPreferences(MyPreferences.leave_view, "1");
                                myPreferences.setPreferences(MyPreferences.leave_insert, "1");
                                myPreferences.setPreferences(MyPreferences.leave_update, "1");
                                myPreferences.setPreferences(MyPreferences.leave_delete, "1");
                                myPreferences.setPreferences(MyPreferences.expence_view, "1");
                                myPreferences.setPreferences(MyPreferences.expence_insert, "1");
                                myPreferences.setPreferences(MyPreferences.expence_update, "1");
                                myPreferences.setPreferences(MyPreferences.expence_delete, "1");

                                myPreferences.setPreferences(MyPreferences.daily_sales_report_view, "1");
                                myPreferences.setPreferences(MyPreferences.daily_sales_report_insert, "1");
                                myPreferences.setPreferences(MyPreferences.daily_sales_report_update, "1");
                                myPreferences.setPreferences(MyPreferences.daily_sales_report_delete, "1");
                                myPreferences.setPreferences(MyPreferences.daily_service_report_view, "1");
                                myPreferences.setPreferences(MyPreferences.daily_service_report_insert, "1");
                                myPreferences.setPreferences(MyPreferences.daily_service_report_update, "1");
                                myPreferences.setPreferences(MyPreferences.daily_service_report_delete, "1");

                                myPreferences.setPreferences(MyPreferences.daily_work_report_view, "1");
                                myPreferences.setPreferences(MyPreferences.daily_work_report_insert, "1");
                                myPreferences.setPreferences(MyPreferences.daily_work_report_update, "1");
                                myPreferences.setPreferences(MyPreferences.daily_work_report_delete, "1");


                            } else {
                                for (int i = 0; i < rights.length(); i++) {
                                    JSONObject c = rights.getJSONObject(i);
                                    /*if (c.getString("page_id").equals("504")) {  //tag_page
                                        myPreferences.setPreferences(MyPreferences.tag_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.tag_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.tag_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.tag_delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("505")) { //employee_page
                                        myPreferences.setPreferences(MyPreferences.employee_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.employee_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.employee_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.employee_delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("506")) { // label_page
                                        myPreferences.setPreferences(MyPreferences.label_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.label_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.label_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.label_delete, "" + c.getString("delete_flag"));
                                    } else*/ if (c.getString("page_id").equals("515")) { //contact_page
                                        myPreferences.setPreferences(MyPreferences.contact_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.contact_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.contact_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.contact_delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("512")) { //document_page
                                        myPreferences.setPreferences(MyPreferences.document_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.document_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.document_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.document_delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("513")) { //courier_page
                                        myPreferences.setPreferences(MyPreferences.couriers_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.couriers_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.couriers_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.couriers_delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("511")) { //task_page
                                        myPreferences.setPreferences(MyPreferences.task_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.task_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.task_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.task_delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("514")) { //cheque_page
                                        myPreferences.setPreferences(MyPreferences.cheque_view, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.cheque_insert, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.cheque_update, "" + c.getString("update_flag"));
                                        myPreferences.setPreferences(MyPreferences.cheque_delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("541")) { //cheque_page
                                        myPreferences.setPreferences(MyPreferences.INSERT_FLAG, "" + c.getString("insert_flag"));
                                        myPreferences.setPreferences(MyPreferences.VIEW_FLAG, "" + c.getString("view_flag"));
                                        myPreferences.setPreferences(MyPreferences.delete, "" + c.getString("delete_flag"));
                                    } else if (c.getString("page_id").equals("542")) { //cheque_page
                                        try {
                                            myPreferences.setPreferences(MyPreferences.followup, "" + c.getString("insert_flag"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (c.getString("page_id").equals("551")) {
                                        try {
                                            myPreferences.setPreferences(MyPreferences.leave_view, "" + c.getString("view_flag"));
                                            myPreferences.setPreferences(MyPreferences.leave_insert, "" + c.getString("insert_flag"));
                                            myPreferences.setPreferences(MyPreferences.leave_update, "" + c.getString("update_flag"));
                                            myPreferences.setPreferences(MyPreferences.leave_delete, "" + c.getString("delete_flag"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (c.getString("page_id").equals("550")) {
                                        try {
                                            myPreferences.setPreferences(MyPreferences.expence_view, "" + c.getString("view_flag"));
                                            myPreferences.setPreferences(MyPreferences.expence_insert, "" + c.getString("insert_flag"));
                                            myPreferences.setPreferences(MyPreferences.expence_update, "" + c.getString("update_flag"));
                                            myPreferences.setPreferences(MyPreferences.expence_delete, "" + c.getString("delete_flag"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (c.getString("page_id").equals("547")) {
                                        try {
                                            myPreferences.setPreferences(MyPreferences.daily_sales_report_view, "" + c.getString("view_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_sales_report_insert, "" + c.getString("insert_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_sales_report_update, "" + c.getString("update_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_sales_report_delete, "" + c.getString("delete_flag"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (c.getString("page_id").equals("548")) {
                                        try {
                                            myPreferences.setPreferences(MyPreferences.daily_service_report_view, "" + c.getString("view_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_service_report_insert, "" + c.getString("insert_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_service_report_update, "" + c.getString("update_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_service_report_delete, "" + c.getString("delete_flag"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (c.getString("page_id").equals("549")) {
                                        try {
                                            myPreferences.setPreferences(MyPreferences.daily_work_report_view, "" + c.getString("view_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_work_report_insert, "" + c.getString("insert_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_work_report_update, "" + c.getString("update_flag"));
                                            myPreferences.setPreferences(MyPreferences.daily_work_report_delete, "" + c.getString("delete_flag"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            try {
                                JSONArray projects_array = json.getJSONArray("project");
                                if (projects_array.length() > 0) {
                                    myPreferences.setPreferences(MyPreferences.projects_array, "" + projects_array.toString());

                                    if (projects_array.length() > 0) {
                                        for (int i = 0; i < projects_array.length(); i++) {
                                            JSONObject c1 = projects_array.getJSONObject(i);
                                            if (i == 0) {
                                                myPreferences.setPreferences(MyPreferences.project_id, c1.getString("project_id"));
                                                myPreferences.setPreferences(MyPreferences.project_title, c1.getString("project_title"));
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    myPreferences.setPreferences(MyPreferences.projects_array, "");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        JSONArray currancy = json.getJSONArray("currancy");
                        if (currancy.length() > 0) {
                            myPreferences.setPreferences(MyPreferences.currencyArray, "" + currancy);
                        } else {
                            myPreferences.setPreferences(MyPreferences.currencyArray, "");
                        }

                        //myPreferences.setPreferences(MyPreferences.id, "" + result.getString("id"));

                        Intent i = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                        i.putExtra("uid", "" + result.getString("id"));
                        startActivity(i);
                        //finish();
                    } else {
                        Toaster.show(LoginActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    myPreferences.setPreferences(MyPreferences.id, "");
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                myPreferences.setPreferences(MyPreferences.id, "");
            }
        });
    }
}
