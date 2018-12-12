package com.innovent.erp.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.innovent.erp.AddTaskActivity;
import com.innovent.erp.DashboardActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.ChangeAliasAdapter;
import com.innovent.erp.adapter.TaskLogAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.AliasModel;
import com.innovent.erp.model.TaskLogModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CB-PHP-1 on 8/9/2016.
 */
public class ChangeToAliasDialog extends DialogFragment {


    MyPreferences myPreferences;
    ArrayList<AliasModel> data = new ArrayList<>();
    ChangeAliasAdapter adapter;
    MaterialSpinner alialSpinner;
    int spinnerPosition = -1;

    public interface InterfaceCommunicator {
        void filterDate(String toDate, String fromDate);
    }

    public static ChangeToAliasDialog newInstance(Context context) {
        ChangeToAliasDialog f = new ChangeToAliasDialog();
        // Supply num input as an argument.
        /*Bundle args = new Bundle();
        args.putString("type",type);
        f.setArguments(args);*/
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*this.type=getArguments().getString("type");*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_change_alias_layout, container, false);
        myPreferences = new MyPreferences(getActivity());
        alialSpinner = (MaterialSpinner) v.findViewById(R.id.alial_spinner);
        Button cancel = (Button) v.findViewById(R.id.cancel);
        Button submit = (Button) v.findViewById(R.id.submit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerPosition != -1) {
                    try {
                        myPreferences.clearPreferences();
                        myPreferences.setPreferences(MyPreferences.login_type, "" + data.get(spinnerPosition).getType());
                        myPreferences.setPreferences(MyPreferences.id, "" + data.get(spinnerPosition).getId());
                        myPreferences.setPreferences(MyPreferences.name, "" + data.get(spinnerPosition).getName());
                        myPreferences.setPreferences(MyPreferences.email, "" + data.get(spinnerPosition).getEmail());
                        myPreferences.setPreferences(MyPreferences.mobile_no, "" + data.get(spinnerPosition).getMobile());
                        myPreferences.setPreferences(MyPreferences.Execuvive_in_max, "23:59");
                        myPreferences.setPreferences(MyPreferences.Execuvive_in_min, "01:00");
                        myPreferences.setPreferences(MyPreferences.Execuvive_out, "23:59");
                        myPreferences.setPreferences(MyPreferences.UDATE, GlobalElements.getCurrentdate());

                        if (data.get(spinnerPosition).getType().equals("0")) {

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

                            myPreferences.setPreferences(MyPreferences.daily_sales_report_view, "0");
                            myPreferences.setPreferences(MyPreferences.daily_sales_report_insert, "0");
                            myPreferences.setPreferences(MyPreferences.daily_sales_report_update, "0");
                            myPreferences.setPreferences(MyPreferences.daily_sales_report_delete, "0");
                            myPreferences.setPreferences(MyPreferences.daily_service_report_view, "0");
                            myPreferences.setPreferences(MyPreferences.daily_service_report_insert, "0");
                            myPreferences.setPreferences(MyPreferences.daily_service_report_update, "0");
                            myPreferences.setPreferences(MyPreferences.daily_service_report_delete, "0");

                            myPreferences.setPreferences(MyPreferences.daily_work_report_view, "1");
                            myPreferences.setPreferences(MyPreferences.daily_work_report_insert, "1");
                            myPreferences.setPreferences(MyPreferences.daily_work_report_update, "1");
                            myPreferences.setPreferences(MyPreferences.daily_work_report_delete, "1");

                        } else {

                            JSONArray rights = new JSONArray(data.get(spinnerPosition).getRights());
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
                                    } else*/
                                if (c.getString("page_id").equals("515")) { //contact_page
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

                            JSONArray projects_array = new JSONArray(data.get(spinnerPosition).getProject());
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
                        }

                        JSONArray currancy = new JSONArray(data.get(spinnerPosition).getCurrency());
                        if (currancy.length() > 0) {
                            myPreferences.setPreferences(MyPreferences.currencyArray, "" + currancy);
                        } else {
                            myPreferences.setPreferences(MyPreferences.currencyArray, "");
                        }

                        dismiss();
                        Intent i = new Intent(getActivity(), DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        if (GlobalElements.isConnectingToInternet(getActivity())) {
            getChangeAlias();
        } else {
            GlobalElements.showDialog(getActivity());
        }

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_change_alias_layout, null);
        final Dialog d = new Dialog(getActivity());
        d.setContentView(root);
        d.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return d;
    }

    private void getChangeAlias() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.changeAlias();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    data.clear();
                    if (json.getInt("ack") == 1) {

                        JSONArray result = json.getJSONArray("result");
                        JSONArray currency = json.getJSONArray("currency");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);
                            AliasModel da = new AliasModel();
                            if (c.getString("type").equals("0")) {
                                da.setId("" + c.getString("id"));
                                da.setAlias_name("Back To Orignal");
                                da.setName("" + c.getString("name"));
                                da.setEmail("" + c.getString("email"));
                                da.setMobile(c.getString("phone_1"));
                                da.setMax_working_time("23:59");
                                da.setMin_working_time("01:00");
                                da.setWorking_end_time("23:59");
                                da.setType("0");
                                da.setRights("");
                                da.setProject("" + c.getString("project"));
                                da.setCurrency("" + currency);
                                data.add(0, da);
                            } else {
                                da.setId("" + c.getString("id"));
                                da.setAlias_name("" + c.getString("name"));
                                da.setName("" + c.getString("name"));
                                da.setEmail("" + c.getString("email"));
                                da.setMobile(c.getString("phone_1"));
                                da.setMax_working_time("23:59");
                                da.setMin_working_time("01:00");
                                da.setWorking_end_time("23:59");
                                da.setRights("" + c.getString("rights"));
                                da.setProject("" + c.getString("project"));
                                da.setType("" + c.getString("type"));
                                da.setCurrency("" + currency);
                                data.add(da);
                            }
                        }
                        adapter = new ChangeAliasAdapter(getActivity(), data);
                        alialSpinner.setAdapter(adapter);
                        alialSpinner.setHint("Select Alias");
                        SpinnerInteractionListener_alias listener_1 = new SpinnerInteractionListener_alias();
                        alialSpinner.setOnTouchListener(listener_1);
                        alialSpinner.setOnItemSelectedListener(listener_1);
                    } else {
                        Toaster.show(getActivity(), "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    public class SpinnerInteractionListener_alias implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                spinnerPosition = position;
            } else {
                spinnerPosition = -1;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
