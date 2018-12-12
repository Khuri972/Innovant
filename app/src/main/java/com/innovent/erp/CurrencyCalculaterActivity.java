package com.innovent.erp;

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

import com.innovent.erp.adapter.CurrencyAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CurrencyModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyCalculaterActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add_currency)
    FloatingActionButton addCurrency;

    ArrayList<CurrencyModel> data = new ArrayList<>();
    CurrencyAdapter adapter;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_calculater);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Currency Calculator");
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(CurrencyCalculaterActivity.this);

        addCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrencyCalculaterActivity.this, AddCurrencyActivity.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent, 0);
            }
        });

        adapter = new CurrencyAdapter(CurrencyCalculaterActivity.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(CurrencyCalculaterActivity.this, LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(CurrencyCalculaterActivity.this)) {
            getCurrency();
        } else {
            GlobalElements.showDialog(CurrencyCalculaterActivity.this);
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
        try {
            if (resultCode == 10) {
                String itemArray = _data.getStringExtra("itemArray");
                String id = _data.getStringExtra("id");
                String title = _data.getStringExtra("title");
                String desc = _data.getStringExtra("desc");
                String total = _data.getStringExtra("total");
                CurrencyModel da = new CurrencyModel();
                da.setId(id);
                da.setTitle(title);
                da.setDesc(desc);
                da.setTotal(total);
                da.setCurrency_object(itemArray);
                data.add(da);
                adapter.notifyDataSetChanged();
            }
            if (resultCode == 11) {
                int position = _data.getIntExtra("position", 0);

                String itemArray = _data.getStringExtra("itemArray");
                String id = _data.getStringExtra("id");
                String title = _data.getStringExtra("title");
                String desc = _data.getStringExtra("desc");
                String total = _data.getStringExtra("total");
                CurrencyModel da = new CurrencyModel();
                da.setId(id);
                da.setTitle(title);
                da.setDesc(desc);
                da.setTotal(total);
                da.setCurrency_object(itemArray);
                data.set(position, da);

                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrency() {
        try {
            final ProgressDialog pd = new ProgressDialog(CurrencyCalculaterActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getCurrencyHistory(myPreferences.getPreferences(MyPreferences.id));

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
                                CurrencyModel da = new CurrencyModel();
                                da.setId(c.getString("id"));
                                da.setTitle(c.getString("title"));
                                da.setDesc(c.getString("description"));
                                da.setTotal(c.getString("total"));
                                JSONArray item = c.getJSONArray("item");
                                da.setCurrency_object(item.toString());
                                data.add(da);
                            }
                            adapter = new CurrencyAdapter(CurrencyCalculaterActivity.this, data);
                            recycleview.setAdapter(adapter);
                            recycleview.setLayoutManager(new LinearLayoutManager(CurrencyCalculaterActivity.this, LinearLayoutManager.VERTICAL, false));

                        } else {
                            Toaster.show(CurrencyCalculaterActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
