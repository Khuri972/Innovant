package com.innovent.erp.ErpModule.sales_management;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.adapter.SalesReturnAdapter;
import com.innovent.erp.ErpModule.sales_management.model.DeliveryOrderModel;
import com.innovent.erp.ErpModule.sales_management.model.SalesReturnModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.helpDesk.ServiceInvoiceActivity;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReturn extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add)
    FloatingActionButton add;

    ArrayList<SalesReturnModel> data = new ArrayList<>();
    SalesReturnAdapter adapter;
    MyPreferences myPreferences;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sales Return");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalesReturn.this, AddSalesReturn.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent,0);
            }
        });

        adapter = new SalesReturnAdapter(SalesReturn.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(SalesReturn.this, LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(SalesReturn.this)) {
            getSalesReturn();
        } else {
            GlobalElements.showDialog(SalesReturn.this);
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
                SalesReturnModel da = (SalesReturnModel) _data.getSerializableExtra("data");
                data.add(da);
                adapter.notifyDataSetChanged();
                if (data.isEmpty()) {
                    recycleview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyText.setText("Sales Return Empty");
                } else {
                    recycleview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
            } else if (resultCode == 11) {
                SalesReturnModel da = (SalesReturnModel) _data.getSerializableExtra("data");
                int position = _data.getIntExtra("position", 0);
                data.set(position, da);
                adapter.notifyItemChanged(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quatation_menu,menu);
        try {
            SearchManager searchManager = (SearchManager) SalesReturn.this.getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SalesReturn.this.getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchEditText.setTextColor(getResources().getColor(R.color.white));
            searchEditText.setHintTextColor(getResources().getColor(R.color.white));
            searchEditText.setHint("Search ...");
            ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            searchMagIcon.setImageResource(R.drawable.ic_close_white_24dp);
            setUpSearchObservable(searchView);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void setUpSearchObservable(SearchView search) {
        RxSearchObservable.fromView(search)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        if (text.isEmpty()) {
                            //textViewResult.setText("");
                            return true;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .switchMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String query) throws Exception {
                        return dataFromNetwork(query);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            System.out.print("" + responseBody.byteStream());
                            String json_response = null;
                            try {
                                json_response = responseBody.string();
                                getResult(json_response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", "" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("compate", "");
                    }
                });
    }

    public Observable<ResponseBody> dataFromNetwork(final String query) {
        RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
        return request.searchSalesReturn(myPreferences.getPreferences(MyPreferences.id), query);
    }

    private void getSalesReturn() {
        try {
            final ProgressDialog pd = new ProgressDialog(SalesReturn.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.getSalesReturn(myPreferences.getPreferences(MyPreferences.id), "", "", "", "", "");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        getResult(json_response);
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

    public void getResult(String json_response) {
        try {
            JSONObject json = new JSONObject(json_response);
            if (json.getInt("ack") == 1) {
                data.clear();
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);
                    SalesReturnModel da = new SalesReturnModel();
                    da.setId(c.getString("id"));
                    da.setCustomer_name(c.getString("customer_name"));
                    da.setSales_return_no(c.getString("sales_return_no"));
                    da.setStatus_name(c.getString("status_name"));
                    da.setGrandtotal(c.getString("grandtotal"));
                    da.setStatus(c.getString("status"));
                    da.setSales_return_format(c.getString("sales_return_date_format"));
                    data.add(da);
                }
                adapter = new SalesReturnAdapter(this, data);
                recycleview.setAdapter(adapter);
                recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                recycleview.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);

            } else {
                recycleview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyText.setText("" + json.getString("ack_msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            recycleview.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            emptyText.setText("Sales Return Empty");
        }
    }
}
