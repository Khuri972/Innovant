package com.innovent.erp.helpDesk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.AddSalesOrder;
import com.innovent.erp.ErpModule.sales_management.QuatationRequestDetail;
import com.innovent.erp.ErpModule.sales_management.adapter.QuatationRequestDisplayProductAdapter;
import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.ErpModule.sales_management.model.QuatationRequestModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.helpDesk.adapter.ServieInvoiceDisplayAdapter;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.TaskModel;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddServiceInvoiceActivity extends AppCompatActivity {

    @BindView(R.id.date_edt)
    AutoCompleteTextView dateEdt;
    @BindView(R.id.customer_name_edt)
    AutoCompleteTextView customerNameEdt;
    @BindView(R.id.customer_email_edt)
    AutoCompleteTextView customerEmailEdt;
    @BindView(R.id.customer_mobile_edt)
    AutoCompleteTextView customerMobileEdt;
    @BindView(R.id.customer_alter_mobile_edt)
    AutoCompleteTextView customerAlterMobileEdt;
    @BindView(R.id.customer_address1_edt)
    AutoCompleteTextView customerAddress1Edt;
    @BindView(R.id.customer_address2_edt)
    AutoCompleteTextView customerAddress2Edt;
    @BindView(R.id.customer_landmark_edt)
    AutoCompleteTextView customerLandmarkEdt;
    @BindView(R.id.customer_area_edt)
    AutoCompleteTextView customerAreaEdt;
    @BindView(R.id.city_spinner)
    MaterialSpinner citySpinner;
    @BindView(R.id.district_spinner)
    MaterialSpinner districtSpinner;
    @BindView(R.id.zone_spinner)
    MaterialSpinner zoneSpinner;
    @BindView(R.id.state_spinner)
    MaterialSpinner stateSpinner;
    @BindView(R.id.country_spinner)
    MaterialSpinner countrySpinner;
    @BindView(R.id.customer_pincode_edt)
    AutoCompleteTextView customerPincodeEdt;
    @BindView(R.id.customer_gst_edt)
    AutoCompleteTextView customerGstEdt;
    @BindView(R.id.customer_pancard_edt)
    AutoCompleteTextView customerPancardEdt;
    @BindView(R.id.toolbar_shadow)
    View toolbarShadow;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.narration_edt)
    EditText narrationEdt;
    @BindView(R.id.terms_edt)
    EditText termsEdt;
    @BindView(R.id.submit)
    TextView submit;

    TextView product_selection_header_qty, product_selection_header_price, product_selection_header_discount, product_selection_header_discount_amount, product_selection_header_discount_type, product_selection_header_grand_total;

    private ArrayList<ProductModel> data = new ArrayList<>();
    ServieInvoiceDisplayAdapter adapter;
    MyPreferences myPreferences;
    int position = 0;
    TaskModel taskModel = new TaskModel();

    GeneralAdapter cityAdapter;
    GeneralAdapter districtAdapter;
    GeneralAdapter stateAdapter;
    GeneralAdapter countryAdapter;
    GeneralAdapter zoneAdapter;

    ArrayList<GeneralModel> city_data = new ArrayList<>();
    ArrayList<GeneralModel> district_data = new ArrayList<>();
    ArrayList<GeneralModel> state_data = new ArrayList<>();
    ArrayList<GeneralModel> country_data = new ArrayList<>();
    ArrayList<GeneralModel> zone_data = new ArrayList<>();

    String insert_update_flag = "0", customerId = "";
    String cityId = "", districtId = "", stateId = "", stateName = "", countryId = "", zoneId = "";
    double discount = 0;
    String discount_type = "1";
    JSONArray json_array;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_invoice);
        ButterKnife.bind(this);
        db = new DBHelper(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Service Invoice");

        product_selection_header_qty = (TextView) findViewById(R.id.product_selection_header_qty);
        product_selection_header_price = (TextView) findViewById(R.id.product_selection_header_price);
        product_selection_header_discount = (TextView) findViewById(R.id.product_selection_header_discount);
        product_selection_header_discount_amount = (TextView) findViewById(R.id.product_selection_header_discount_amount);
        product_selection_header_discount_type = (TextView) findViewById(R.id.product_selection_header_discount_type);  // Rs or %
        product_selection_header_grand_total = (TextView) findViewById(R.id.product_selection_header_grand_total);

        try {
            city_data = db.getCity();
            cityAdapter = new GeneralAdapter(AddServiceInvoiceActivity.this, city_data);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setHint("Select City");
            SpinnerInteractionListener_city listener_1 = new SpinnerInteractionListener_city();
            citySpinner.setOnTouchListener(listener_1);
            citySpinner.setOnItemSelectedListener(listener_1);

            district_data = db.getDistrict();
            districtAdapter = new GeneralAdapter(AddServiceInvoiceActivity.this, district_data);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setHint("Select District");
            SpinnerInteractionListener_district listener_2 = new SpinnerInteractionListener_district();
            districtSpinner.setOnTouchListener(listener_2);
            districtSpinner.setOnItemSelectedListener(listener_2);

            state_data = db.getState();
            stateAdapter = new GeneralAdapter(AddServiceInvoiceActivity.this, state_data);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setHint("Select State");
            SpinnerInteractionListener_state listener_3 = new SpinnerInteractionListener_state();
            stateSpinner.setOnTouchListener(listener_3);
            stateSpinner.setOnItemSelectedListener(listener_3);

            country_data = db.getCountry();
            countryAdapter = new GeneralAdapter(AddServiceInvoiceActivity.this, country_data);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setHint("Select Country");
            SpinnerInteractionListener_country listener_4 = new SpinnerInteractionListener_country();
            countrySpinner.setOnTouchListener(listener_4);
            countrySpinner.setOnItemSelectedListener(listener_4);

            zone_data = db.getZone();
            zoneAdapter = new GeneralAdapter(AddServiceInvoiceActivity.this, zone_data);
            zoneSpinner.setAdapter(zoneAdapter);
            zoneSpinner.setHint("Select Zone");
            SpinnerInteractionListener_zone listener_5 = new SpinnerInteractionListener_zone();
            zoneSpinner.setOnTouchListener(listener_5);
            zoneSpinner.setOnItemSelectedListener(listener_5);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Bundle bundle = getIntent().getExtras();
            position = bundle.getInt("position");
            taskModel = (TaskModel) bundle.getSerializable("data");

            if (GlobalElements.isConnectingToInternet(AddServiceInvoiceActivity.this)) {
                getSericeInvoice();
            } else {
                GlobalElements.showDialog(AddServiceInvoiceActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(AddServiceInvoiceActivity.this)) {
                    int add_flag = 0;
                    JSONObject user;
                    json_array = new JSONArray();
                    for (int i = 0; i < data.size(); i++) {
                        user = new JSONObject();
                        String id = data.get(i).getId();
                        if (data.get(i).getQty() == 0) {
                            add_flag = 1;
                            Toaster.show(AddServiceInvoiceActivity.this, "Enter Qty", true, Toaster.DANGER);
                            break;
                        }
                        if (data.get(i).getSell_price() == 0) {
                            add_flag = 1;
                            Toaster.show(AddServiceInvoiceActivity.this, "Enter Rate", true, Toaster.DANGER);
                            break;
                        } else {
                            try {
                                user.put("item_id", "" + id);
                                user.put("qty", "" + data.get(i).getQty());
                                user.put("rate", "" + data.get(i).getSell_price());
                                user.put("discount", "" + data.get(i).getDiscount());
                                user.put("batch_no", "" + data.get(i).getSerial_no());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            json_array.put(user);
                            add_flag = 2;
                        }
                    }

                    if (add_flag == 0) {
                        Toaster.show(AddServiceInvoiceActivity.this, "Add Item", true, Toaster.DANGER);
                    } else if (add_flag == 2) {
                        AlertDialog buildInfosDialog;
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddServiceInvoiceActivity.this);
                        if (insert_update_flag.equals("0")) {
                            alertDialog2.setTitle("Are you sure want create service invoice");
                        } else {
                            alertDialog2.setTitle("Are you sure want update service invoice");
                        }

                        alertDialog2.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        try {
                                            if (GlobalElements.isConnectingToInternet(AddServiceInvoiceActivity.this)) {
                                                AddServiceInvoice("" + json_array.toString());
                                            } else {
                                                GlobalElements.showDialog(AddServiceInvoiceActivity.this);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
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
                    }
                } else {
                    GlobalElements.showDialog(AddServiceInvoiceActivity.this);
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

    public void edittextviewDesible(View editText)
    {
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setClickable(false);
    }

    private void getSericeInvoice() {
        final ProgressDialog pd = new ProgressDialog(AddServiceInvoiceActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.getServiceInvoicePipeline(taskModel.getId(), myPreferences.getPreferences(MyPreferences.id));
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

                        customerId = result.getString("customer_id");
                        customerNameEdt.setText("" + result.getString("customer_name"));
                        customerEmailEdt.setText("" + result.getString("customer_email"));
                        customerMobileEdt.setText("" + result.getString("customer_phone_1"));
                        customerAlterMobileEdt.setText("" + result.getString("customer_phone_2"));
                        customerAddress1Edt.setText("" + result.getString("customer_address_1"));
                        customerAddress2Edt.setText("" + result.getString("customer_address_2"));
                        customerLandmarkEdt.setText("" + result.getString("customer_landmark"));
                        customerPincodeEdt.setText("" + result.getString("customer_pincode"));
                        customerGstEdt.setText("" + result.getString("customer_gst_no"));
                        customerPancardEdt.setText("" + result.getString("customer_pancard_no"));
                        customerAreaEdt.setText("" + result.getString("customer_area"));

                        narrationEdt.setText("" + result.getString("narration"));
                        termsEdt.setText("" + result.getString("terms_condition"));

                        edittextviewDesible(customerNameEdt);
                        edittextviewDesible(customerEmailEdt);
                        edittextviewDesible(customerMobileEdt);
                        edittextviewDesible(customerAlterMobileEdt);
                        edittextviewDesible(customerAddress1Edt);
                        edittextviewDesible(customerAddress2Edt);
                        edittextviewDesible(customerLandmarkEdt);
                        edittextviewDesible(customerPincodeEdt);
                        edittextviewDesible(customerGstEdt);
                        edittextviewDesible(customerPancardEdt);
                        edittextviewDesible(customerAreaEdt);
                        edittextviewDesible(narrationEdt);
                        edittextviewDesible(termsEdt);

                        for (int i = 0; i < city_data.size(); i++) {
                            if (city_data.get(i).getId().equals(result.getString("customer_city_id"))) {
                                cityId = city_data.get(i).getId();
                                citySpinner.setSelection(i + 1);
                                citySpinner.setEnabled(false);
                                break;
                            }
                        }

                        for (int i = 0; i < district_data.size(); i++) {
                            if (district_data.get(i).getId().equals(result.getString("customer_district_id"))) {
                                districtId = district_data.get(i).getId();
                                districtSpinner.setSelection(i + 1);
                                districtSpinner.setEnabled(false);
                                break;
                            }
                        }

                        for (int i = 0; i < state_data.size(); i++) {
                            if (state_data.get(i).getId().equals(result.getString("customer_state_id"))) {
                                stateId = state_data.get(i).getId();
                                stateName = state_data.get(i).getName().toLowerCase();
                                stateSpinner.setSelection(i + 1);
                                stateSpinner.setEnabled(false);
                                break;
                            }
                        }

                        for (int i = 0; i < country_data.size(); i++) {
                            if (country_data.get(i).getId().equals(result.getString("customer_country_id"))) {
                                countryId = country_data.get(i).getId();
                                countrySpinner.setSelection(i + 1);
                                countrySpinner.setEnabled(false);
                                break;
                            }
                        }

                        for (int i = 0; i < zone_data.size(); i++) {
                            if (zone_data.get(i).getId().equals(result.getString("customer_zone_id"))) {
                                zoneId = zone_data.get(i).getId();
                                zoneSpinner.setSelection(i + 1);
                                zoneSpinner.setEnabled(false);
                                break;
                            }
                        }

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
                        adapter = new ServieInvoiceDisplayAdapter(AddServiceInvoiceActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(AddServiceInvoiceActivity.this, LinearLayoutManager.VERTICAL, false));
                    } else {
                        adapter = new ServieInvoiceDisplayAdapter(AddServiceInvoiceActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(AddServiceInvoiceActivity.this, LinearLayoutManager.VERTICAL, false));
                        Toaster.show(AddServiceInvoiceActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void AddServiceInvoice(String items) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddServiceInvoiceActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call;

            if (insert_update_flag.equals("0")) {
                call = request.addServiceInvoicePipeline(myPreferences.getPreferences(MyPreferences.id), "" + taskModel.getId(), customerId, customerNameEdt.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), narrationEdt.getText().toString(), termsEdt.getText().toString(), "" + discount, "" + discount_type, items);
            } else {
                call = request.addServiceInvoicePipeline(myPreferences.getPreferences(MyPreferences.id), "" + taskModel.getId(), customerId, customerNameEdt.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), narrationEdt.getText().toString(), termsEdt.getText().toString(), "" + discount, "" + discount_type, items);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            if (insert_update_flag.equals("0")) {
                                /*Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", model);
                                intent.putExtras(bundle);
                                setResult(10, intent);*/
                                finish();
                            } else {

                                finish();
                            }

                        } else {
                            Toaster.show(AddServiceInvoiceActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    public class SpinnerInteractionListener_city implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    cityId = city_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    cityId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_district implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    districtId = district_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    districtId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_state implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    stateId = state_data.get(position).getId();
                    stateName = state_data.get(position).getName().toLowerCase();
                } catch (Exception e) {
                    e.printStackTrace();
                    stateId = "";
                    stateName = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_country implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    countryId = country_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    countryId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_zone implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    zoneId = zone_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    zoneId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

}
