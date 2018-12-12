package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.adapter.NotificationAdapter;
import com.innovent.erp.custom.RecyclerViewPositionHelper;
import com.innovent.erp.model.NotificationModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    int firstVisibleItem, visibleItemCount, totalItemCount, count = 0;
    protected int m_PreviousTotalCount;
    RecyclerViewPositionHelper mRecyclerViewHelper;

    ArrayList<NotificationModel> data = new ArrayList<>();
    NotificationAdapter adapter;
    String type = "0";
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cdms_notification);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(NotificationActivity.this);

        try {
            Intent i = getIntent();
            type = i.getStringExtra("type");
        } catch (Exception e) {
            e.printStackTrace();
            type="0";
        }

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                if (totalItemCount == 0 || adapter == null)
                    return;
                if (m_PreviousTotalCount == totalItemCount) {
                    return;
                } else {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                    if (loadMore) {
                        m_PreviousTotalCount = totalItemCount;
                        // GetNotifaction();
                    }
                }
            }
        });

        if (GlobalElements.isConnectingToInternet(NotificationActivity.this)) {
            GetNotifaction();
        } else {
            GlobalElements.showDialog(NotificationActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (type.equals("0")) {
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (type.equals("0")) {
            finish();
        } else {
            Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void GetNotifaction() {
        try {
            final ProgressDialog pd = new ProgressDialog(NotificationActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            final RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getNotification(myPreferences.getPreferences(MyPreferences.id), count, GlobalElements.ll_count);

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
                                JSONObject result_obj = result.getJSONObject(i);
                                NotificationModel da = new NotificationModel();
                                da.setId(result_obj.getString("id"));
                                da.setTitle(result_obj.getString("notification_title"));
                                da.setDesc(result_obj.getString("notification_description"));
                                data.add(da);
                            }

                            if (count == 0) {
                                adapter = new NotificationAdapter(NotificationActivity.this, data);
                                recycleView.setAdapter(adapter);
                                recycleView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayout.VERTICAL, false));
                            } else {
                                adapter.Updatedata();
                            }
                            if (result.length() == 0) {
                                count = 0;
                            } else {
                                count += result.length();
                            }
                            emptyLayout.setVisibility(View.GONE);
                            recycleView.setVisibility(View.VISIBLE);
                        } else {
                            if (count == 0) {
                                emptyText.setText("" + json.getString("ack_msg"));
                                emptyLayout.setVisibility(View.VISIBLE);
                                recycleView.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    System.out.print("error" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
