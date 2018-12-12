package com.innovent.erp.ErpModule.sales_management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.adapter.QuatationRequestDisplayProductAdapter;
import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.ErpModule.sales_management.model.QuatationRequestModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
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

public class QuatationRequestDetail extends AppCompatActivity {

    QuatationRequestModel model = new QuatationRequestModel();
    @BindView(R.id.dealer_name)
    TextView dealerName;
    @BindView(R.id.dealer_address)
    TextView dealerAddressTxt;
    @BindView(R.id.dealer_quatation_no)
    TextView dealerQuatationNo;
    @BindView(R.id.dealer_quatation_mobile)
    TextView dealerQuatationMobile;
    @BindView(R.id.dealer_quatation_email)
    TextView dealerQuatationEmail;
    @BindView(R.id.dealer_quatation_date)
    TextView dealerQuatationDate;
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

    private ArrayList<ProductModel> data = new ArrayList<>();
    QuatationRequestDisplayProductAdapter adapter;
    MyPreferences myPreferences;
    int position = 0;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.quatation_no)
    TextView quatationNo;
    @BindView(R.id.quatation_mobile)
    TextView quatationMobile;
    @BindView(R.id.quatation_email)
    TextView quatationEmail;

    @BindView(R.id.quatation_date)
    TextView quatationDate;
    @BindView(R.id.address)
    TextView addressTxt;

    @BindView(R.id.list_inquiry_main)
    LinearLayout listInquiryMain;
    @BindView(R.id.toolbar_shadow)
    View toolbarShadow;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.nested)
    NestedScrollView nested;

    @BindView(R.id.narration_edt)
    EditText narrationEdt;
    @BindView(R.id.terms_edt)
    EditText termsEdt;

    String discount_type = "1";

    TextView product_selection_header_qty, product_selection_header_price, product_selection_header_discount, product_selection_header_discount_amount, product_selection_header_discount_type, product_selection_header_grand_total;

    int resultCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quatation_request_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            model = (QuatationRequestModel) bundle.getSerializable("data");
            setData();
            getSupportActionBar().setTitle("" + model.getQuotation_request_no());
        } catch (Exception e) {
            e.printStackTrace();
            getSupportActionBar().setTitle("Quotation Detail");
        }

        if (GlobalElements.isConnectingToInternet(QuatationRequestDetail.this)) {
            getQuatationRequestDetail();
        } else {
            GlobalElements.showDialog(QuatationRequestDetail.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quatation_request_detail, menu);
        MenuItem edit = menu.findItem(R.id.action_update);
        if (model.getStatus().equals("0")) {
            edit.setVisible(true);
        } else {
            edit.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void setData() {
        try {
            name.setText("" + model.getCustomer_name());
            quatationNo.setText("Quotation No : " + model.getQuotation_request_no());
            quatationMobile.setText("Mobile : " + model.getCustomer_phone_1());
            quatationEmail.setText("Email : " + model.getCustomer_email());
            quatationDate.setText("Quotation Date : " + model.getQuotation_request_date_format());

            dealerName.setText("" + model.getCustomer_name());
            dealerQuatationNo.setText("Quotation No : " + model.getQuotation_request_no());
            dealerQuatationMobile.setText("Mobile : " + model.getCustomer_phone_1());
            dealerQuatationEmail.setText("Email : " + model.getCustomer_email());
            dealerQuatationDate.setText("Quotation Date : " + model.getQuotation_request_date_format());

            narrationEdt.setText("" + model.getNarration());
            termsEdt.setText("" + model.getTerms_condition());

            StringBuilder adddres = new StringBuilder();
            if (!model.getCustomer_address_1().equals("")) {
                adddres.append("" + model.getCustomer_address_1() + "\n");
            }

            if (!model.getCustomer_city_name().equals("")) {
                adddres.append("" + model.getCustomer_city_name() + " " + model.getCustomer_pincode());
            }

            if (!model.getCustomer_state_name().equals("")) {
                adddres.append("" + model.getCustomer_state_name() + " " + model.getCustomer_country_name());
            }

            addressTxt.setText("" + adddres.toString());
            dealerAddressTxt.setText("" + adddres.toString());

            try {
                discount_type = model.getDiscount_type();
                if (discount_type.equals("0")) {
                    product_selection_header_discount_amount.setVisibility(View.GONE);
                    product_selection_header_discount_amount.setText("" + model.getDiscount_amount());
                    product_selection_header_discount_type.setText("Rs.");
                    if (model.getDiscount() == 0) {
                        product_selection_header_discount.setText("0");
                    } else {
                        product_selection_header_discount.setText("" + GlobalElements.FirstRemoveZero("" + model.getDiscount_amount()));
                    }
                } else {
                    if (model.getDiscount() == 0) {
                        product_selection_header_discount.setText("0");
                    } else {
                        product_selection_header_discount.setText("" + GlobalElements.DecimalFormat("" + model.getDiscount_amount()));
                    }
                    product_selection_header_discount_amount.setVisibility(View.VISIBLE);
                    product_selection_header_discount_amount.setText("" + model.getDiscount() + "%");
                    product_selection_header_discount_type.setText("Rs.");
                }
                product_selection_header_qty.setText("" + model.getTotal_qty());
                product_selection_header_price.setText("" + model.getSubtotal());
                product_selection_header_grand_total.setText("" + model.getGrandtotal());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);

        try {
            this.resultCode = resultCode;
            if (resultCode == 11) {
                model = (QuatationRequestModel) _data.getSerializableExtra("model");
                setData();
                data.clear();
                data = (ArrayList<ProductModel>) _data.getSerializableExtra("data");
                adapter = new QuatationRequestDisplayProductAdapter(QuatationRequestDetail.this, data);
                recycleview.setAdapter(adapter);
                recycleview.setLayoutManager(new LinearLayoutManager(QuatationRequestDetail.this, LinearLayoutManager.VERTICAL, false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (resultCode == 11) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", model);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                setResult(11, intent);
                finish();
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (resultCode == 11) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", model);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    setResult(11, intent);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.action_update:
                try {
                    Intent intent = new Intent(QuatationRequestDetail.this, AddSalesQuotationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("quotation_data", model);
                    bundle.putSerializable("product_data", data);
                    bundle.putInt("position", position);
                    bundle.putString("type", "1");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getQuatationRequestDetail() {
        final ProgressDialog pd = new ProgressDialog(QuatationRequestDetail.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.getQuatationRequestDetail(model.getId(), myPreferences.getPreferences(MyPreferences.id));
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

                        adapter = new QuatationRequestDisplayProductAdapter(QuatationRequestDetail.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(QuatationRequestDetail.this, LinearLayoutManager.VERTICAL, false));

                    } else {
                        adapter = new QuatationRequestDisplayProductAdapter(QuatationRequestDetail.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(QuatationRequestDetail.this, LinearLayoutManager.VERTICAL, false));
                        Toaster.show(QuatationRequestDetail.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
