package com.innovent.erp.visitorBookModual;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.visitorBookModual.adapter.FollowUpHistoryAdapter;
import com.innovent.erp.visitorBookModual.model.FollowUpModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysFollowupActivity extends AppCompatActivity {

    RecyclerView recycleview;
    LinearLayout empty_layout;
    MyPreferences myPreferences;

    ArrayList<FollowUpModel> data = new ArrayList<>();
    FollowUpHistoryAdapter adapter;
    int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_followup);
        myPreferences = new MyPreferences(TodaysFollowupActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(myPreferences.getPreferences(MyPreferences.project_title));
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);

        if (GlobalElements.isConnectingToInternet(TodaysFollowupActivity.this)) {
            getToodaysFollowup();
        } else {
            GlobalElements.showDialog(TodaysFollowupActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            try {
                position = data.getIntExtra("position", 0);
            } catch (Exception e) {
                e.printStackTrace();
                position = 0;
            }
            getToodaysFollowup();
        }
    }

    private void getToodaysFollowup() {
        final ProgressDialog pd = new ProgressDialog(TodaysFollowupActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.getTodaysFollowup(myPreferences.getPreferences(MyPreferences.id),myPreferences.getPreferences(MyPreferences.project_id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonst = response.body().string();
                    JSONObject json = new JSONObject(jsonst);

                    data.clear();
                    if (json.getInt("ack") == 1) {
                        recycleview.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                        JSONArray result = json.getJSONArray("result");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);
                            FollowUpModel da = new FollowUpModel();
                            da.setId(c.getString("id"));
                            da.setDescription(c.getString("description"));
                            da.setThrough(c.getString("type_slug"));
                            da.setThrough_slug(c.getString("type_slug"));
                            da.setFollowup_date(c.getString("followup_date"));
                            da.setFuture_date(c.getString("future_date"));
                            da.setNext_action(c.getString("next_action"));
                            da.setStatus(c.getString("status"));
                            da.setStatus_slug(c.getString("status_slug"));
                            da.setVisitor_id(c.getString("visitor_id"));
                            da.setResponse(c.getString("response"));
                            da.setFollowupBy(c.getString("user_name"));
                            da.setCategoryName(c.getString("category_name"));
                            da.setVisitor_name(c.getString("name"));
                            da.setVisitor_email(c.getString("email"));
                            da.setVisitor_mobile_no(c.getString("mobile_no"));
                            da.setInquiryBy(c.getString("user_name"));
                            da.setRating(c.getInt("rating"));
                            data.add(da);
                        }

                        adapter = new FollowUpHistoryAdapter(TodaysFollowupActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(TodaysFollowupActivity.this, LinearLayout.VERTICAL, false));

                    } else {
                        recycleview.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }
        });
    }
}
