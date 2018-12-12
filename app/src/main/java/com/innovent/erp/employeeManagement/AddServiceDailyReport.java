package com.innovent.erp.employeeManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.MyTextWatcher;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.employeeManagement.model.DailyFileAttachModel;
import com.innovent.erp.employeeManagement.model.DailyNoteModel;
import com.innovent.erp.employeeManagement.model.DailyServiceReportModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceDailyReport extends AppCompatActivity {

    @BindView(R.id.input_customer_name)
    AutoCompleteTextView inputCustomerName;
    /*@BindView(R.id.input_person_name)
    EditText inputPersonName;*/
    @BindView(R.id.input_address)
    EditText inputAddress;
    @BindView(R.id.purpose_spinner)
    MaterialSpinner purposeSpinner;
    @BindView(R.id.input_discuss)
    EditText inputDiscuss;
    @BindView(R.id.input_note)
    EditText inputNote;
    @BindView(R.id.file_name_txt)
    TextView fileNameTxt;
    @BindView(R.id.browse_txt)
    TextView browseTxt;
    @BindView(R.id.add_expense)
    TextView addExpense;
    @BindView(R.id.submit)
    TextView submit;


    ArrayList<GeneralModel> purposeData = new ArrayList<>();
    GeneralAdapter purpose_adapter;
    DailyServiceReportModel reportModel = new DailyServiceReportModel();

    String service_id;
    MyPreferences myPreferences;
    @BindView(R.id.layout_customer_name)
    TextInputLayout layoutCustomerName;
    /*@BindView(R.id.layout_person_name)
    TextInputLayout layoutPersonName;*/
    String insert_update_flag = "0";
    int position = 0;
    @BindView(R.id.save)
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_daily_report);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Daily Report");

        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_flag = bundle.getString("type");
            if (insert_update_flag.equals("1")) {
                position = bundle.getInt("position");
                reportModel = (DailyServiceReportModel) bundle.getSerializable("data");
                inputCustomerName.setText("" + reportModel.getCustomer_name());
                inputAddress.setText("" + reportModel.getAddress());
                inputDiscuss.setText("" + reportModel.getRemark());
                save.setText("Update");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (GlobalElements.isConnectingToInternet(AddServiceDailyReport.this)) {
            getData("get", "0");
        } else {
            GlobalElements.showDialog(AddServiceDailyReport.this);
        }

        inputCustomerName.addTextChangedListener(new MyTextWatcher(layoutCustomerName));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.BLANK_CHECK, inputCustomerName.getText().toString())) {
                    layoutCustomerName.setError("Enter Customer Name");
                } else {
                    if (GlobalElements.isConnectingToInternet(AddServiceDailyReport.this)) {
                        if (insert_update_flag.equals("0")) {
                            getData("add", "1");
                        } else {
                            getData("update", "1");
                        }
                    } else {
                        GlobalElements.showDialog(AddServiceDailyReport.this);
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.BLANK_CHECK, inputCustomerName.getText().toString())) {
                    layoutCustomerName.setError("Enter Customer Name");
                } else {
                    if (GlobalElements.isConnectingToInternet(AddServiceDailyReport.this)) {
                        if (insert_update_flag.equals("0")) {
                            getData("add", "1");
                        } else {
                            getData("update", "1");
                        }
                    } else {
                        GlobalElements.showDialog(AddServiceDailyReport.this);
                    }
                }
            }
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddServiceDailyReport.this, AddExpanceActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(final String type, String isSubmit) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddServiceDailyReport.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = null;

            if (type.equals("get")) {
                call = request.getServiceType(myPreferences.getPreferences(MyPreferences.id));
            } else if (type.equals("add")) {
                call = request.addDailyServiceReport(myPreferences.getPreferences(MyPreferences.id), inputCustomerName.getText().toString(), inputAddress.getText().toString(),
                        service_id, inputDiscuss.getText().toString(), isSubmit);
            } else if (type.equals("update")) {
                call = request.updateDailyServiceReport(myPreferences.getPreferences(MyPreferences.id), inputCustomerName.getText().toString(), inputAddress.getText().toString(),
                        service_id, inputDiscuss.getText().toString(), isSubmit, reportModel.getId(), "edit");
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (type.equals("get")) {
                            purposeData.clear();
                            if (json.getInt("ack") == 1) {
                                JSONArray result = json.getJSONArray("result");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    GeneralModel da = new GeneralModel();
                                    da.setId(c.getString("id"));
                                    da.setName(c.getString("name"));
                                    purposeData.add(da);
                                }
                            } else {
                                Toaster.show(AddServiceDailyReport.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                            }
                            purpose_adapter = new GeneralAdapter(AddServiceDailyReport.this, purposeData);
                            purposeSpinner.setAdapter(purpose_adapter);
                            purposeSpinner.setHint("Select Purpose");
                            SpinnerInteractionListener_purpose listener_1 = new SpinnerInteractionListener_purpose();
                            purposeSpinner.setOnTouchListener(listener_1);
                            purposeSpinner.setOnItemSelectedListener(listener_1);

                            try {
                                if (insert_update_flag.equals("1")) {
                                    for (int i = 0; i < purposeData.size(); i++) {
                                        if (purposeData.get(i).getId().equals("" + reportModel.getPurpose())) {
                                            service_id = reportModel.getId();
                                            purposeSpinner.setSelection(i + 1);
                                            break;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (type.equals("add")) {
                            if (json.getInt("ack") == 1) {
                                JSONObject result = json.getJSONObject("result");
                                reportModel.setId(result.getString("id"));
                                reportModel.setCustomer_name(result.getString("customer_name"));
                                reportModel.setAddress(result.getString("address"));
                                reportModel.setDate(result.getString("created_date"));
                                reportModel.setPurpose(result.getString("service_type_id"));
                                reportModel.setPurpose_slug(result.getString("service_type_name"));
                                reportModel.setRemark(result.getString("remark"));
                                reportModel.setIsSubmitted(result.getString("isSubmitted"));

                                ArrayList<DailyNoteModel> dailyNoteModels = new ArrayList<DailyNoteModel>();
                                reportModel.setDailyNoteModels(dailyNoteModels);
                                ArrayList<DailyFileAttachModel> dailyFileAttachModels = new ArrayList<DailyFileAttachModel>();
                                reportModel.setDailyFileAttachModels(dailyFileAttachModels);
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", reportModel);
                                intent.putExtras(bundle);
                                setResult(10, intent);
                                finish();
                            } else {
                                Toaster.show(AddServiceDailyReport.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                            }
                        } else if (type.equals("update")) {
                            if (json.getInt("ack") == 1) {
                                JSONObject result = json.getJSONObject("result");
                                reportModel.setCustomer_name(result.getString("customer_name"));
                                reportModel.setAddress(result.getString("address"));
                                reportModel.setDate(result.getString("created_date"));
                                reportModel.setPurpose(result.getString("service_type_id"));
                                reportModel.setPurpose_slug(result.getString("service_type_name"));
                                reportModel.setRemark(result.getString("remark"));
                                reportModel.setIsSubmitted(result.getString("isSubmitted"));

                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", reportModel);
                                intent.putExtras(bundle);
                                setResult(11, intent);
                                finish();
                            } else {
                                Toaster.show(AddServiceDailyReport.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SpinnerInteractionListener_purpose implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                try {
                    service_id = purposeData.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    service_id = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}
