package com.innovent.erp.employeeManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.employeeManagement.adapter.ExpanceAdapter;
import com.innovent.erp.employeeManagement.adapter.LeaveRequestAdapter;
import com.innovent.erp.employeeManagement.model.ExpanceModel;
import com.innovent.erp.employeeManagement.model.LeaveRequestModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRequestActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add_leave)
    FloatingActionButton addLeave;

    ArrayList<LeaveRequestModel> data = new ArrayList<>();
    LeaveRequestAdapter adapter;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leave Request History");
        myPreferences = new MyPreferences(this);

        addLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeaveRequestActivity.this, AddLeaveRequest.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent, 0);
            }
        });

        adapter = new LeaveRequestAdapter(LeaveRequestActivity.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(LeaveRequestActivity.this, LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(LeaveRequestActivity.this)) {
            getLeaveHistory();
        } else {
            GlobalElements.showDialog(LeaveRequestActivity.this);
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        if (resultCode == 10) {
            LeaveRequestModel da = (LeaveRequestModel) _data.getSerializableExtra("data");
            data.add(0,da);
            adapter.notifyDataSetChanged();

            if (data.isEmpty()) {
                recycleview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                recycleview.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            }
        }
    }

    private void getLeaveHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(LeaveRequestActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getLeaveHistory(myPreferences.getPreferences(MyPreferences.id));
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
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                LeaveRequestModel da = new LeaveRequestModel();
                                da.setId(c.getString("id"));
                                da.setFrom_date(c.getString("start_date"));
                                da.setFrom_time(c.getString("start_time"));
                                da.setTo_date(c.getString("end_date"));
                                da.setTo_time(c.getString("end_time"));
                                da.setReason(c.getString("reason"));
                                if (c.getString("file_path").equals("")) {
                                    da.setAttachment_path("");
                                    da.setAttachment_name("");
                                } else {
                                    da.setAttachment_path("file_path");
                                    File file = new File("" + c.getString("file_path"));
                                    da.setAttachment_name("" + file.getName());
                                }
                                data.add(da);
                            }
                            adapter = new LeaveRequestAdapter(LeaveRequestActivity.this, data);
                            recycleview.setAdapter(adapter);
                            recycleview.setLayoutManager(new LinearLayoutManager(LeaveRequestActivity.this, LinearLayoutManager.VERTICAL, false));
                            recycleview.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            recycleview.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyText.setText("" + json.getString("ack_msg"));
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
