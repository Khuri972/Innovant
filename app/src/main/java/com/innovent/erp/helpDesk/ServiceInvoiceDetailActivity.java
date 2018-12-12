package com.innovent.erp.helpDesk;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
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

public class ServiceInvoiceDetailActivity extends AppCompatActivity {

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
    @BindView(R.id.quatation_date)
    TextView quatationDate;
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
    @BindView(R.id.dealer_quatation_date)
    TextView dealerQuatationDate;
    @BindView(R.id.toolbar_shadow)
    View toolbarShadow;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.narration_edt)
    EditText narrationEdt;
    @BindView(R.id.terms_edt)
    EditText termsEdt;
    @BindView(R.id.product_selection_header_qty)
    TextView productSelectionHeaderQty;
    @BindView(R.id.product_selection_header_price)
    TextView productSelectionHeaderPrice;
    @BindView(R.id.product_selection_header_discount_amount)
    TextView productSelectionHeaderDiscountAmount;
    @BindView(R.id.product_selection_header_discount_type)
    TextView productSelectionHeaderDiscountType;
    @BindView(R.id.product_selection_header_discount)
    TextView productSelectionHeaderDiscount;
    @BindView(R.id.product_selection_header_grand_total)
    TextView productSelectionHeaderGrandTotal;
    @BindView(R.id.nested)
    NestedScrollView nested;

    TextView product_selection_header_qty, product_selection_header_price, product_selection_header_discount, product_selection_header_discount_amount, product_selection_header_discount_type, product_selection_header_grand_total;

    private ArrayList<ProductModel> data = new ArrayList<>();
    ServieInvoiceDisplayAdapter adapter;
    MyPreferences myPreferences;
    int position = 0;
    double discount = 0;
    String discount_type = "1";

    String  invoiceId = "",invoiceNo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_invoice_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycleview.setNestedScrollingEnabled(false);
        nested.setVisibility(View.GONE);
        myPreferences = new MyPreferences(this);

        product_selection_header_qty = (TextView) findViewById(R.id.product_selection_header_qty);
        product_selection_header_price = (TextView) findViewById(R.id.product_selection_header_price);
        product_selection_header_discount = (TextView) findViewById(R.id.product_selection_header_discount);
        product_selection_header_discount_amount = (TextView) findViewById(R.id.product_selection_header_discount_amount);
        product_selection_header_discount_type = (TextView) findViewById(R.id.product_selection_header_discount_type);  // Rs or %
        product_selection_header_grand_total = (TextView) findViewById(R.id.product_selection_header_grand_total);

        try {
            Bundle bundle = getIntent().getExtras();
            position = bundle.getInt("position");
            invoiceId = bundle.getString("invoice_id");
            invoiceNo = bundle.getString("invoice_no");
            getSupportActionBar().setTitle(""+invoiceNo);

            if (GlobalElements.isConnectingToInternet(ServiceInvoiceDetailActivity.this)) {
                getSericeInvoice();
            } else {
                GlobalElements.showDialog(ServiceInvoiceDetailActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getSupportActionBar().setTitle("Invoice Detail");
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

    private void getSericeInvoice() {
        final ProgressDialog pd = new ProgressDialog(ServiceInvoiceDetailActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.getServiceInvoiceDetail(invoiceId, myPreferences.getPreferences(MyPreferences.id));
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

                        name.setText(""+result.getString("customer_name"));
                        address.setText("Address : "+result.getString("customer_address_1")+" "+result.getString("customer_city_name")+" "+result.getString("customer_pincode")+"\n"+result.getString("customer_state_name")+" "+result.getString("customer_country_name"));
                        quatationMobile.setText("Mobile : "+result.getString("customer_phone_1"));
                        quatationEmail.setText("Email : "+result.getString("customer_email"));
                        quatationDate.setText("Invoice Date : "+result.getString("invoice_date_format"));

                        JSONObject company_detail = json.getJSONObject("company_detail");
                        dealerName.setText(""+company_detail.getString("client_name"));
                        dealerAddress.setText("Address : "+GlobalElements.fromHtml(""+company_detail.getString("client_address1")));
                        dealerQuatationMobile.setText("Mobile : "+company_detail.getString("client_phone1"));
                        dealerQuatationEmail.setText("Email : "+company_detail.getString("client_email"));
                        dealerQuatationDate.setText("Invoice Date : "+result.getString("invoice_date_format"));

                        narrationEdt.setText("" + result.getString("narration"));
                        termsEdt.setText("" + result.getString("terms_condition"));

                        try {
                            discount = result.getDouble("discount");
                            discount_type = result.getString("discount_type");
                            if (discount_type.equals("0")) {
                                product_selection_header_discount_amount.setVisibility(View.GONE);
                                product_selection_header_discount_amount.setText("" + result.getString("discount_amount"));
                                product_selection_header_discount_type.setText("Rs.");
                                if (discount == 0) {
                                    product_selection_header_discount.setText("0");
                                } else {
                                    product_selection_header_discount.setText("" + GlobalElements.FirstRemoveZero("" + result.getString("discount_amount")));
                                }
                            } else {
                                if (discount == 0) {
                                    product_selection_header_discount.setText("0");
                                } else {
                                    product_selection_header_discount.setText("" + GlobalElements.DecimalFormat("" + result.getString("discount_amount")));
                                }
                                product_selection_header_discount_amount.setVisibility(View.VISIBLE);
                                product_selection_header_discount_amount.setText("" + GlobalElements.DecimalFormat("" + discount, 0) + "%");
                                product_selection_header_discount_type.setText("Rs.");
                            }

                            product_selection_header_qty.setText("" + result.getString("total_qty"));
                            product_selection_header_price.setText("" + result.getString("subtotal"));
                            product_selection_header_grand_total.setText("" + result.getString("grandtotal"));
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
                        adapter = new ServieInvoiceDisplayAdapter(ServiceInvoiceDetailActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(ServiceInvoiceDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        nested.setVisibility(View.VISIBLE);
                    } else {
                        adapter = new ServieInvoiceDisplayAdapter(ServiceInvoiceDetailActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(ServiceInvoiceDetailActivity.this, LinearLayoutManager.VERTICAL, false));

                        nested.setVisibility(View.VISIBLE);
                        Toaster.show(ServiceInvoiceDetailActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
