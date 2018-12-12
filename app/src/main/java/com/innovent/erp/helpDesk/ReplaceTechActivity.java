package com.innovent.erp.helpDesk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.DividerItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.helpDesk.adapter.LogAdapter;
import com.innovent.erp.helpDesk.model.LogModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.TaskModel;
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

public class ReplaceTechActivity extends AppCompatActivity {

    GeneralAdapter technicianAdapter;
    ArrayList<GeneralModel> technician_data = new ArrayList<>();
    MyPreferences myPreferences;
    LogAdapter logAdapter;
    ArrayList<LogModel> logModels = new ArrayList<>();
    TaskModel taskModel = new TaskModel();
    String technicianId = "", technicianName = "", taskID = "", status = "", insert_update_id = "";
    int position = 0;

    @BindView(R.id.technician_spinner)
    MaterialSpinner technicianSpinner;
    @BindView(R.id.remark_edt)
    EditText remarkEdt;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.log_card_view)
    CardView logCardView;


    @BindView(R.id.technican_name_txt)
    TextView technicanNameTxt;
    @BindView(R.id.remark_txt)
    TextView remarkTxt;
    @BindView(R.id.added_on_txt)
    TextView addedOnTxt;
    @BindView(R.id.technician_card_view)
    CardView technicianCardView;
    int resultActivity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_tech);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(ReplaceTechActivity.this);
        recycleview.setNestedScrollingEnabled(false);

        try {
            Bundle bundle = getIntent().getExtras();
            taskModel = (TaskModel) bundle.getSerializable("data");
            getSupportActionBar().setTitle("" + taskModel.getTask_no());

            status = bundle.getString("status");
            position = bundle.getInt("position");
            insert_update_id = bundle.getString("type");
            taskID = taskModel.getId();

            if (taskModel.getReplace_tach_remarks().equals("") && taskModel.getReplace_tach_created_date_format().equals("")) {
                logCardView.setVisibility(View.GONE);
            } else {
                technicanNameTxt.setText("" + taskModel.getTask_assigned_by_name());
                remarkTxt.setText("" + taskModel.getReplace_tach_remarks());
                addedOnTxt.setText("" + taskModel.getReplace_tach_created_date_format());
                technicianCardView.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remarkEdt.getText().toString().equals("")) {
                    Toaster.show(ReplaceTechActivity.this, "Enter Remarks", false, Toaster.DANGER);
                } else {
                    if (GlobalElements.isConnectingToInternet(ReplaceTechActivity.this)) {
                        addTechnician();
                    } else {
                        GlobalElements.showDialog(ReplaceTechActivity.this);
                    }
                }
            }
        });

        if (GlobalElements.isConnectingToInternet(ReplaceTechActivity.this)) {
            getTechnician();
        } else {
            GlobalElements.showDialog(ReplaceTechActivity.this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (resultActivity == 0) {
                    finish();
                } else {
                    Intent intent = new Intent();
                    setResult(resultActivity, intent);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (resultActivity == 0) {
            finish();
        } else {
            Intent intent = new Intent();
            setResult(resultActivity, intent);
            finish();
        }
    }

    private void getTechnician() {
        try {
            final ProgressDialog pd = new ProgressDialog(ReplaceTechActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.getTechnician(myPreferences.getPreferences(MyPreferences.id));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId(c.getString("id"));
                                da.setName(c.getString("name"));
                                technician_data.add(da);
                            }
                            technicianAdapter = new GeneralAdapter(ReplaceTechActivity.this, technician_data);
                            technicianSpinner.setAdapter(technicianAdapter);
                            technicianSpinner.setHint("Select Technician");
                            SpinnerInteractionListener_technician listener_1 = new SpinnerInteractionListener_technician();
                            technicianSpinner.setOnTouchListener(listener_1);
                            technicianSpinner.setOnItemSelectedListener(listener_1);
                        } else {
                            technicianAdapter = new GeneralAdapter(ReplaceTechActivity.this, technician_data);
                            technicianSpinner.setAdapter(technicianAdapter);
                            technicianSpinner.setHint("Select Technician");
                            SpinnerInteractionListener_technician listener_1 = new SpinnerInteractionListener_technician();
                            technicianSpinner.setOnTouchListener(listener_1);
                            technicianSpinner.setOnItemSelectedListener(listener_1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getTechnicianLog();
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

    private void addTechnician() {
        try {
            final ProgressDialog pd = new ProgressDialog(ReplaceTechActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.addTechnician(technicianId, taskID, remarkEdt.getText().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            technicianCardView.setVisibility(View.VISIBLE);
                            technicanNameTxt.setText("" + technicianName);
                            remarkTxt.setText("" + remarkEdt.getText().toString());
                            addedOnTxt.setText("" + GlobalElements.getDateFrom_dd_MM_YYYY());
                            resultActivity = 20;
                            Toaster.show(ReplaceTechActivity.this, "" + json.getString("ack_msg"), false, Toaster.SUCCESS);
                        } else {
                            Toaster.show(ReplaceTechActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void getTechnicianLog() {
        try {

            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.getTechnicianLog(myPreferences.getPreferences(MyPreferences.id), taskID);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                LogModel da = new LogModel();
                                da.setId(c.getString("id"));
                                da.setDate(c.getString("log_time_format"));
                                da.setTitle(c.getString("title"));
                                da.setDescription(c.getString("description"));
                                logModels.add(da);
                            }
                            logAdapter = new LogAdapter(ReplaceTechActivity.this, logModels);
                            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(ReplaceTechActivity.this);
                            recycleview.addItemDecoration(itemDecoration);
                            recycleview.setAdapter(logAdapter);
                            recycleview.setLayoutManager(new LinearLayoutManager(ReplaceTechActivity.this, LinearLayoutManager.VERTICAL, false));
                            logCardView.setVisibility(View.VISIBLE);
                        } else {
                            logCardView.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SpinnerInteractionListener_technician implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    technicianId = technician_data.get(position).getId();
                    technicianName = technician_data.get(position).getName();
                } catch (Exception e) {
                    e.printStackTrace();
                    technicianId = "";
                    technicianName = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

}
