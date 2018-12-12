package com.innovent.erp.employeeManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.MyTextWatcher;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.employeeManagement.model.DailyFileAttachModel;
import com.innovent.erp.employeeManagement.model.DailyNoteModel;
import com.innovent.erp.employeeManagement.model.DailyWorkReportModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDailyWorkReport extends AppCompatActivity {

    @BindView(R.id.input_work_type)
    AutoCompleteTextView inputWorkType;
    @BindView(R.id.layout_work_type)
    TextInputLayout layoutWorkType;
    @BindView(R.id.input_person_name)
    EditText inputPersonName;
    @BindView(R.id.input_address)
    EditText inputAddress;
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
    @BindView(R.id.sender_card)
    CardView senderCard;

    MyPreferences myPreferences;
    @BindView(R.id.layout_person_name)
    TextInputLayout layoutPersonName;

    DailyWorkReportModel workReportModel = new DailyWorkReportModel();
    String insert_update_flag = "0";
    int position = 0;
    @BindView(R.id.save)
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_work_report);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Daily Work Report");

        inputWorkType.addTextChangedListener(new MyTextWatcher(layoutWorkType));
        inputPersonName.addTextChangedListener(new MyTextWatcher(layoutPersonName));

        Bundle bundle = getIntent().getExtras();
        insert_update_flag = bundle.getString("type");
        if (insert_update_flag.equals("1")) {
            position = bundle.getInt("position");
            workReportModel = (DailyWorkReportModel) bundle.getSerializable("data");
            inputWorkType.setText("" + workReportModel.getWork_type());
            inputPersonName.setText("" + workReportModel.getPerson_name());
            inputAddress.setText("" + workReportModel.getAddress());
            inputDiscuss.setText("" + workReportModel.getRemark());
            save.setText("Update");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.BLANK_CHECK, inputWorkType.getText().toString())) {
                    layoutWorkType.setError("Enter Work Type");
                } else if (!Validation.isValid(Validation.BLANK_CHECK, inputPersonName.getText().toString())) {
                    layoutPersonName.setError("Enter Person Name");
                } else {
                    if (GlobalElements.isConnectingToInternet(AddDailyWorkReport.this)) {
                        addDailyWorkReport("0");
                    } else {
                        GlobalElements.showDialog(AddDailyWorkReport.this);
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.BLANK_CHECK, inputWorkType.getText().toString())) {
                    layoutWorkType.setError("Enter Work Type");
                } else if (!Validation.isValid(Validation.BLANK_CHECK, inputPersonName.getText().toString())) {
                    layoutPersonName.setError("Enter Person Name");
                } else {
                    if (GlobalElements.isConnectingToInternet(AddDailyWorkReport.this)) {
                        addDailyWorkReport("1");
                    } else {
                        GlobalElements.showDialog(AddDailyWorkReport.this);
                    }
                }
            }
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddDailyWorkReport.this, AddExpanceActivity.class);
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

    private void addDailyWorkReport(String isSubmit) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddDailyWorkReport.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call;
            if (insert_update_flag.equals("0")) {
                call = request.addDailyWorkReport(myPreferences.getPreferences(MyPreferences.id), inputWorkType.getText().toString(), inputAddress.getText().toString(),
                        inputPersonName.getText().toString(), inputDiscuss.getText().toString(), isSubmit);
            } else {
                call = request.updateDailyWorkReport(myPreferences.getPreferences(MyPreferences.id), inputWorkType.getText().toString(), inputAddress.getText().toString(),
                        inputPersonName.getText().toString(), inputDiscuss.getText().toString(), isSubmit, workReportModel.getId(), "edit");
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        //getResult(json_response);
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            if (insert_update_flag.equals("0")) {
                                JSONObject result = json.getJSONObject("result");
                                workReportModel.setId("" + result.getString("id"));
                                workReportModel.setWork_type("" + result.getString("work_type"));
                                workReportModel.setPerson_name("" + result.getString("person_name"));
                                workReportModel.setDate("" + result.getString("created_date"));
                                workReportModel.setAddress("" + result.getString("address"));
                                workReportModel.setRemark("" + result.getString("remark"));
                                workReportModel.setIsSubmitted(result.getString("isSubmitted"));

                                ArrayList<DailyNoteModel> dailyNoteModels = new ArrayList<DailyNoteModel>();
                                workReportModel.setDailyNoteModels(dailyNoteModels);
                                ArrayList<DailyFileAttachModel> dailyFileAttachModels = new ArrayList<DailyFileAttachModel>();
                                workReportModel.setDailyFileAttachModels(dailyFileAttachModels);
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", workReportModel);
                                intent.putExtras(bundle);
                                setResult(10, intent);
                                finish();
                            } else {
                                JSONObject result = json.getJSONObject("result");
                                workReportModel.setWork_type("" + result.getString("work_type"));
                                workReportModel.setPerson_name("" + result.getString("person_name"));
                                workReportModel.setDate("" + result.getString("created_date"));
                                workReportModel.setAddress("" + result.getString("address"));
                                workReportModel.setRemark("" + result.getString("remark"));
                                workReportModel.setIsSubmitted(result.getString("isSubmitted"));

                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", position);
                                bundle.putSerializable("data", workReportModel);
                                intent.putExtras(bundle);
                                setResult(11, intent);
                                finish();
                            }
                        } else {
                            Toaster.show(AddDailyWorkReport.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
}
