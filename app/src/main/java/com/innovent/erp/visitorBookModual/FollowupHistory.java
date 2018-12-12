package com.innovent.erp.visitorBookModual;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class FollowupHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_followup;
    LinearLayout empty_layout;
    Button empty_txt;
    String visitor_id;
    MyPreferences myPreferences;
    ArrayList<FollowUpModel> data = new ArrayList<>();
    FollowUpHistoryAdapter adapter;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(FollowupHistory.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        add_followup = (FloatingActionButton) findViewById(R.id.add_followup);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        empty_txt = (Button) findViewById(R.id.empty_txt);

        try {
            Intent intent = getIntent();
            visitor_id = intent.getStringExtra("visitor_id");
            getSupportActionBar().setSubtitle("" + intent.getStringExtra("visitor_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GlobalElements.isConnectingToInternet(FollowupHistory.this)) {
            getFollowup();
        } else {
            GlobalElements.showDialog(FollowupHistory.this);
        }

        add_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowupHistory.this, AddFollowUpActivity.class);
                intent.putExtra("visitor_id", visitor_id);
                startActivityForResult(intent, 0);
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
            getFollowup();
        }
    }

    public void getFollowup() {
        final ProgressDialog pd = new ProgressDialog(FollowupHistory.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.getFollowupHistory(myPreferences.getPreferences(MyPreferences.id), myPreferences.getPreferences(MyPreferences.project_id), visitor_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.dismiss();
                try {
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    data.clear();
                    if (json.getInt("ack") == 1) {

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
                        adapter = new FollowUpHistoryAdapter(FollowupHistory.this, data);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FollowupHistory.this, LinearLayout.VERTICAL, false));
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                        recyclerView.scrollToPosition(position);
                    } else {
                        empty_layout.setVisibility(View.VISIBLE);
                        empty_txt.setText("" + json.getString("ack_msg"));
                        recyclerView.setVisibility(View.GONE);
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
    }

}
