package com.innovent.erp.helpDesk;

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

import com.innovent.erp.ErpModule.sales_management.QuatationRequest;
import com.innovent.erp.ErpModule.sales_management.SalesOrder;
import com.innovent.erp.ErpModule.sales_management.adapter.SalesOrderAdapter;
import com.innovent.erp.ErpModule.sales_management.interFace.UpdateData;
import com.innovent.erp.ErpModule.sales_management.model.SalesOrderModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.helpDesk.adapter.ServiceInvoiceAdapter;
import com.innovent.erp.helpDesk.model.ServiceInvoiceModel;
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

public class ServiceInvoiceActivity extends AppCompatActivity implements UpdateData {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<ServiceInvoiceModel> data = new ArrayList<>();
    ServiceInvoiceAdapter adapter;
    MyPreferences myPreferences;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_invoice);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Service Invoice");

        adapter = new ServiceInvoiceAdapter(this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(ServiceInvoiceActivity.this)) {
            getServiceInvoice();
        } else {
            GlobalElements.showDialog(ServiceInvoiceActivity.this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quatation_menu,menu);
        try {
            SearchManager searchManager = (SearchManager) ServiceInvoiceActivity.this.getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ServiceInvoiceActivity.this.getComponentName()));
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
        return request.searchServiceInvoice(myPreferences.getPreferences(MyPreferences.id), query);
    }

    private void getServiceInvoice() {
        try {
            final ProgressDialog pd = new ProgressDialog(ServiceInvoiceActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.getServiceInvoice(myPreferences.getPreferences(MyPreferences.id), "", "", "", "", "");

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
                    ServiceInvoiceModel da = new ServiceInvoiceModel();
                    da.setId("" + c.getString("id"));
                    da.setInvoice_no("" + c.getString("invoice_no"));
                    da.setCustomer_id(c.getString("customer_id"));
                    da.setCustomer_name("" + c.getString("customer_name"));
                    da.setCustomer_email(c.getString("customer_email"));
                    da.setCustomer_phone_1(c.getString("customer_phone_1"));
                    da.setCustomer_phone_2(c.getString("customer_phone_2"));
                    da.setCustomer_address_1(c.getString("customer_address_1"));
                    da.setCustomer_address_2(c.getString("customer_address_2"));
                    da.setCustomer_landmark("");
                    da.setCustomer_country(c.getString("customer_country"));
                    da.setCustomer_state("" + c.getString("customer_state"));
                    da.setCustomer_city(c.getString("customer_city"));
                    da.setCustomer_zone(c.getString("customer_zone"));
                    da.setCustomer_district(c.getString("customer_district"));
                    da.setCustomer_pincode(c.getString("customer_pincode"));
                    da.setCustomer_gst_no(c.getString("customer_gst_no"));
                    da.setCustomer_pancard_no(c.getString("customer_pancard_no"));
                    da.setCustomer_zone_name(c.getString("customer_zone_name"));
                    da.setCustomer_area_name(c.getString("customer_area"));
                    da.setCustomer_gst_no(c.getString("customer_gst_no"));
                    da.setCustomer_pancard_no(c.getString("customer_pancard_no"));
                    da.setNarration(c.getString("narration"));
                    da.setTerms_condition(c.getString("terms_condition"));
                    da.setTotal_qty(c.getInt("total_qty"));
                    da.setSubtotal(c.getDouble("subtotal"));
                    da.setDiscount_type(c.getString("discount_type"));
                    da.setDiscount(c.getDouble("discount"));
                    da.setDiscount_amount(c.getDouble("discount_amount"));
                    da.setGrandtotal(c.getString("grandtotal"));
                    da.setStatus(c.getString("status"));
                    da.setStatus_name(c.getString("status_name"));
                    da.setInvoice_date_format(c.getString("invoice_date_format"));
                    da.setCustomer_country_name(c.getString("customer_country_name"));
                    da.setCustomer_state_name(c.getString("customer_state_name"));
                    da.setCustomer_city_name(c.getString("customer_city_name"));
                    da.setCustomer_district_name(c.getString("customer_district_name"));
                    da.setCustomer_zone_name(c.getString("customer_zone_name"));
                    data.add(da);
                }
                adapter = new ServiceInvoiceAdapter(this, data);
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
            emptyText.setText("Service Invoice Empty");
        }
    }

    @Override
    public void callbackCall() {
        if (data.isEmpty()) {
            recycleview.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            emptyText.setText("Service Invoice Empty");
        } else {
            recycleview.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }
}
