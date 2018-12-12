package com.innovent.erp.ErpModule.sales_management;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.ErpModule.sales_management.model.SalesReturnModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.helpDesk.adapter.ServieInvoiceDisplayAdapter;
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

public class SalesReturnDetailActivity extends AppCompatActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.quatation_no)
    TextView quatationNo;
    @BindView(R.id.quatation_mobile)
    TextView quatationMobile;
    @BindView(R.id.quatation_email)
    TextView quatationEmail;
    @BindView(R.id.list_inquiry_main)
    LinearLayout listInquiryMain;
    @BindView(R.id.dealer_name)
    TextView dealerName;
    @BindView(R.id.dealer_address)
    TextView dealerAddress;
    @BindView(R.id.dealer_quatation_no)
    TextView dealerQuatationNo;
    @BindView(R.id.dealer_quatation_mobile)
    TextView dealerQuatationMobile;
    @BindView(R.id.dealer_quatation_email)
    TextView dealerQuatationEmail;

    @BindView(R.id.toolbar_shadow)
    View toolbarShadow;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.narration_edt)
    EditText narrationEdt;
    @BindView(R.id.terms_edt)
    EditText termsEdt;

    @BindView(R.id.nested)
    NestedScrollView nested;

    TextView product_selection_header_grand_total;
    @BindView(R.id.approve)
    TextView approve;
    @BindView(R.id.reject)
    TextView reject;
    @BindView(R.id.status_layout)
    LinearLayout statusLayout;

    private ArrayList<ProductModel> data = new ArrayList<>();
    ServieInvoiceDisplayAdapter adapter;
    MyPreferences myPreferences;
    int position = 0;
    String invoiceId = "";
    SalesReturnModel model = new SalesReturnModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycleview.setNestedScrollingEnabled(false);
        nested.setVisibility(View.GONE);
        myPreferences = new MyPreferences(this);

        product_selection_header_grand_total = (TextView) findViewById(R.id.product_selection_header_grand_total);

        try {
            Bundle bundle = getIntent().getExtras();
            position = bundle.getInt("position");
            invoiceId = bundle.getString("invoice_id");
            model = (SalesReturnModel) bundle.getSerializable("data");
            getSupportActionBar().setTitle("" + model.getSales_return_no());
            adapter = new ServieInvoiceDisplayAdapter(SalesReturnDetailActivity.this, data);
            recycleview.setAdapter(adapter);
            recycleview.setLayoutManager(new LinearLayoutManager(SalesReturnDetailActivity.this, LinearLayoutManager.VERTICAL, false));

            if (model.getStatus().equals("0")) {
                statusLayout.setVisibility(View.VISIBLE);
            } else {
                statusLayout.setVisibility(View.GONE);
            }

            if (GlobalElements.isConnectingToInternet(SalesReturnDetailActivity.this)) {
                getSericeReturnDetail();
            } else {
                GlobalElements.showDialog(SalesReturnDetailActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getSupportActionBar().setTitle("SalesReturn Detail");
        }

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(SalesReturnDetailActivity.this)) {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SalesReturnDetailActivity.this);
                    alertDialog2.setTitle("Are you sure want to approve");

                    alertDialog2.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    getSericeReturnStatus("approve");
                                }
                            });

                    alertDialog2.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog

                                }
                            });
                    buildInfosDialog = alertDialog2.create();
                    buildInfosDialog.show();
                } else {
                    GlobalElements.showDialog(SalesReturnDetailActivity.this);
                }
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(SalesReturnDetailActivity.this)) {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SalesReturnDetailActivity.this);
                    alertDialog2.setTitle("Are you sure want to reject");

                    alertDialog2.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    getSericeReturnStatus("reject");
                                }
                            });

                    alertDialog2.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog

                                }
                            });
                    buildInfosDialog = alertDialog2.create();
                    buildInfosDialog.show();
                } else {
                    GlobalElements.showDialog(SalesReturnDetailActivity.this);
                }
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


    private void getSericeReturnDetail() {
        final ProgressDialog pd = new ProgressDialog(SalesReturnDetailActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.viewSalesReturn(myPreferences.getPreferences(MyPreferences.id), invoiceId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    data.clear();
                    if (json.getInt("ack") == 1) {

                        JSONArray result_array = json.getJSONArray("result");
                        JSONObject result = result_array.getJSONObject(0);

                        name.setText("" + result.getString("customer_name"));
                        address.setText("Address : " + result.getString("customer_address_1") + " " + result.getString("customer_city_name") + " " + result.getString("customer_pincode") + "\n" + result.getString("customer_state_name") + " " + result.getString("customer_country_name"));
                        quatationMobile.setText("Mobile : " + result.getString("customer_phone_1"));
                        quatationEmail.setText("Email : " + result.getString("customer_email"));

                        JSONObject company_detail = json.getJSONObject("company_detail");
                        dealerName.setText("" + company_detail.getString("client_name"));
                        dealerAddress.setText("Address : " + GlobalElements.fromHtml("" + company_detail.getString("client_address1")));
                        dealerQuatationMobile.setText("Mobile : " + company_detail.getString("client_phone1"));
                        dealerQuatationEmail.setText("Email : " + company_detail.getString("client_email"));

                        narrationEdt.setText("" + result.getString("narration"));
                        termsEdt.setText("" + result.getString("terms_condition"));

                        try {
                            product_selection_header_grand_total.setText("Grand Total : " + result.getString("grandtotal"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        JSONArray items = result.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject c = items.getJSONObject(i);
                            ProductModel da = new ProductModel();
                            da.setId("" + c.getString("item_id"));
                            da.setName(c.getString("name"));
                            da.setHsnCode(c.getString("hsn_code"));
                            da.setUnit(c.getString("unit_name"));
                            da.setGstType(1);
                            try {
                                if (result.getString("customer_state_name").toLowerCase().equals("gujarat")) {
                                    da.setGstType(0);
                                } else {
                                    da.setGstType(1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            da.setSerial_no(c.getString("batch_no"));
                            da.setQty(c.getInt("qty"));
                            da.setSell_price(c.getDouble("rate"));
                            da.setDiscount(c.getDouble("discount"));
                            da.setAmount(0);
                            da.setGst(c.getInt("gst"));
                            da.setCgst(0);
                            da.setSgst(0);
                            da.setIgst(0);
                            da.setNetAmount(c.getDouble("net_amount"));
                            data.add(da);
                        }

                        adapter = new ServieInvoiceDisplayAdapter(SalesReturnDetailActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(SalesReturnDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        nested.setVisibility(View.VISIBLE);
                    } else {
                        adapter = new ServieInvoiceDisplayAdapter(SalesReturnDetailActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(SalesReturnDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        nested.setVisibility(View.VISIBLE);
                        Toaster.show(SalesReturnDetailActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }

        });
    }

    private void getSericeReturnStatus(String status) {
        final ProgressDialog pd = new ProgressDialog(SalesReturnDetailActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.SalesReturnStatus(myPreferences.getPreferences(MyPreferences.id), invoiceId, status);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    data.clear();
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        model.setStatus("" + result.getString("status"));
                        model.setStatus_name("" + result.getString("status_name"));
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        bundle.putSerializable("data", model);
                        intent.putExtras(bundle);
                        setResult(11, intent);
                        finish();
                    } else {
                        Toaster.show(SalesReturnDetailActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }
        });
    }
}
