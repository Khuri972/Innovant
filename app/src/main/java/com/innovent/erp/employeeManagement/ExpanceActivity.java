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
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.employeeManagement.adapter.ExpanceAdapter;
import com.innovent.erp.employeeManagement.model.ExpanceModel;
import com.innovent.erp.model.GeneralModel;
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

public class ExpanceActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add_expance)
    FloatingActionButton addExpance;

    ArrayList<ExpanceModel> data = new ArrayList<>();
    ExpanceAdapter adapter;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expance);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Expense History");
        myPreferences = new MyPreferences(this);
        addExpance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpanceActivity.this, AddExpanceActivity.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent, 0);
            }
        });

        adapter = new ExpanceAdapter(ExpanceActivity.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(ExpanceActivity.this, LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(ExpanceActivity.this)) {
            getExpanceHistory();
        } else {
            GlobalElements.showDialog(ExpanceActivity.this);
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
            try {
                ExpanceModel da = (ExpanceModel) _data.getSerializableExtra("data");
                data.add(0, da);
                adapter.notifyDataSetChanged();
                if (data.isEmpty()) {
                    recycleview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                } else {
                    recycleview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == 12) {
            try {
                int position = _data.getIntExtra("position", 0);
                data.get(position).setExpense_type("1");
                adapter.notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getExpanceHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(ExpanceActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getExpanceHistory(myPreferences.getPreferences(MyPreferences.id));
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
                                ExpanceModel da = new ExpanceModel();
                                da.setId("" + c.getString("id"));
                                da.setCategory_id(c.getString("category_id"));
                                da.setCategory_slug(c.getString("category_name"));
                                da.setAmount(c.getString("amount"));
                                da.setRemark(c.getString("remark"));
                                da.setNote(c.getString("note"));
                                da.setDate(c.getString("expense_date"));
                                da.setExpense_type(c.getString("type"));
                                try {
                                    if (c.getString("file_path").equals("")) {
                                        da.setAttachment_name("");
                                        da.setAttachment_path("");
                                    } else {
                                        File file = new File(c.getString("file_path"));
                                        da.setAttachment_name(file.getName());
                                        da.setAttachment_path(c.getString("file_path"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    da.setAttachment_name("");
                                    da.setAttachment_path("");
                                }
                                data.add(da);
                            }

                            adapter = new ExpanceAdapter(ExpanceActivity.this, data);
                            recycleview.setAdapter(adapter);
                            recycleview.setLayoutManager(new LinearLayoutManager(ExpanceActivity.this, LinearLayoutManager.VERTICAL, false));

                            recycleview.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);

                        } else {
                            recycleview.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyText.setText("" + json.getString("ack_msg"));

                            //Toaster.show(AddExpanceActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
