package com.innovent.erp.ErpModule.sales_management;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.adapter.SalesReturnDisplayProductAdapter;
import com.innovent.erp.ErpModule.sales_management.adapter.SearchCustomerAdapter;
import com.innovent.erp.ErpModule.sales_management.adapter.SearchItemAdapter;
import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.ErpModule.sales_management.model.SalesReturnModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.helpDesk.model.SerialNoModel;
import com.innovent.erp.helpDesk.searchAdapter.SearchSerialNoItemAdapter;
import com.innovent.erp.model.GeneralModel;
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

public class AddSalesReturn extends AppCompatActivity {

    @BindView(R.id.date_edt)
    AutoCompleteTextView dateEdt;
    @BindView(R.id.customer_name_edt)
    AutoCompleteTextView customerName;
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
    @BindView(R.id.search_item_edt)
    AutoCompleteTextView searchItemEdt;
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
    @BindView(R.id.nested)
    NestedScrollView nested;

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

    SearchCustomerAdapter customerAdapter;
    SearchSerialNoItemAdapter itemAdapter;
    SalesReturnDisplayProductAdapter adapter;

    String insert_update_flag = "0", customerId = "";
    String cityId = "", districtId = "", stateId = "", stateName = "", countryId = "", zoneId = "";
    MyPreferences myPreferences;
    JSONArray json_array;
    DBHelper db;
    private ArrayList<SerialNoModel> data = new ArrayList<>();
    SalesReturnModel model=new SalesReturnModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_return);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(this);
        db = new DBHelper(this);

        try {
            city_data = db.getCity();
            cityAdapter = new GeneralAdapter(AddSalesReturn.this, city_data);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setHint("Select City");
            SpinnerInteractionListener_city listener_1 = new SpinnerInteractionListener_city();
            citySpinner.setOnTouchListener(listener_1);
            citySpinner.setOnItemSelectedListener(listener_1);

            district_data = db.getDistrict();
            districtAdapter = new GeneralAdapter(AddSalesReturn.this, district_data);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setHint("Select District");
            SpinnerInteractionListener_district listener_2 = new SpinnerInteractionListener_district();
            districtSpinner.setOnTouchListener(listener_2);
            districtSpinner.setOnItemSelectedListener(listener_2);

            state_data = db.getState();
            stateAdapter = new GeneralAdapter(AddSalesReturn.this, state_data);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setHint("Select State");
            SpinnerInteractionListener_state listener_3 = new SpinnerInteractionListener_state();
            stateSpinner.setOnTouchListener(listener_3);
            stateSpinner.setOnItemSelectedListener(listener_3);

            country_data = db.getCountry();
            countryAdapter = new GeneralAdapter(AddSalesReturn.this, country_data);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setHint("Select Country");
            SpinnerInteractionListener_country listener_4 = new SpinnerInteractionListener_country();
            countrySpinner.setOnTouchListener(listener_4);
            countrySpinner.setOnItemSelectedListener(listener_4);

            zone_data = db.getZone();
            zoneAdapter = new GeneralAdapter(AddSalesReturn.this, zone_data);
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
            insert_update_flag = bundle.getString("type");

            if (insert_update_flag.equals("0")) {
                getSupportActionBar().setTitle("Add Sales Return");
            } else {
                getSupportActionBar().setTitle("Update Sales Return");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new SalesReturnDisplayProductAdapter(AddSalesReturn.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(AddSalesReturn.this, LinearLayoutManager.VERTICAL, false));

        customerAdapter = new SearchCustomerAdapter(AddSalesReturn.this);
        customerName.setAdapter(customerAdapter);

        customerName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    customerId = customerAdapter.suggestions.get(arg2).getId();
                    customerEmailEdt.setText("" + customerAdapter.suggestions.get(arg2).getEmail());
                    customerMobileEdt.setText("" + customerAdapter.suggestions.get(arg2).getPhone_1());
                    customerAddress1Edt.setText("" + customerAdapter.suggestions.get(arg2).getAddress_1());
                    customerPincodeEdt.setText("" + customerAdapter.suggestions.get(arg2).getPincode());
                    customerGstEdt.setText("" + customerAdapter.suggestions.get(arg2).getGst_no());
                    customerPancardEdt.setText("" + customerAdapter.suggestions.get(arg2).getPancard_no());
                    customerAreaEdt.setText(customerAdapter.suggestions.get(arg2).getArea());

                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(customerAdapter.suggestions.get(arg2).getCity())) {
                            cityId = city_data.get(i).getId();
                            citySpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(customerAdapter.suggestions.get(arg2).getDistrict())) {
                            districtId = district_data.get(i).getId();
                            districtSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(customerAdapter.suggestions.get(arg2).getState())) {
                            stateId = state_data.get(i).getId();
                            stateName = state_data.get(i).getName().toLowerCase();
                            stateSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(customerAdapter.suggestions.get(arg2).getCountry())) {
                            countryId = country_data.get(i).getId();
                            countrySpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < zone_data.size(); i++) {
                        if (zone_data.get(i).getId().equals(customerAdapter.suggestions.get(arg2).getZone())) {
                            zoneId = zone_data.get(i).getId();
                            zoneSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) AddSalesReturn.this.getSystemService(AddSalesReturn.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(customerName.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        itemAdapter = new SearchSerialNoItemAdapter(AddSalesReturn.this, customerId);
        searchItemEdt.setAdapter(itemAdapter);
        searchItemEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {

                    boolean flag = false;
                    int position = 0;
                    for (int j = 0; j < data.size(); j++) {
                        if (data.get(j).getId().equals("" + itemAdapter.suggestions.get(i).getId())) {
                            position = j;
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        /*int qty = data.get(position).getQty();
                        if (qty == 0) {
                            qty = 2;
                        } else {
                            qty++;
                        }
                        data.get(position).setQty(qty);
                        adapter.notifyItemChanged(position);*/
                    } else {
                        SerialNoModel da = new SerialNoModel();
                        da.setId("" + itemAdapter.suggestions.get(i).getId());
                        da.setName(itemAdapter.suggestions.get(i).getName());
                        da.setHsn_code(itemAdapter.suggestions.get(i).getHsn_code());
                        da.setSerialNo(itemAdapter.suggestions.get(i).getSerialNo());
                        data.add(da);
                        adapter.notifyDataSetChanged();
                    }

                    InputMethodManager imm = (InputMethodManager) AddSalesReturn.this.getSystemService(AddSalesReturn.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchItemEdt.getWindowToken(), 0);
                    searchItemEdt.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(AddSalesReturn.this)) {
                    JSONObject user;
                    json_array = new JSONArray();
                    for (int i = 0; i < data.size(); i++) {
                        user = new JSONObject();
                        try {
                            user.put("batch_no", "" + data.get(i).getSerialNo());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        json_array.put(user);
                    }

                    if (data.isEmpty()) {
                        Toaster.show(AddSalesReturn.this, "Add Item", true, Toaster.DANGER);
                    } else {
                        AlertDialog buildInfosDialog;
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddSalesReturn.this);
                        if (insert_update_flag.equals("0")) {
                            alertDialog2.setTitle("Are you sure want create Sales Return");
                        } else {
                            alertDialog2.setTitle("Are you sure want update Sales Return");
                        }

                        alertDialog2.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        try {
                                            if (GlobalElements.isConnectingToInternet(AddSalesReturn.this)) {
                                                AddSalesReturn("" + json_array.toString());
                                            } else {
                                                GlobalElements.showDialog(AddSalesReturn.this);
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
                    GlobalElements.showDialog(AddSalesReturn.this);
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

    private void AddSalesReturn(String items) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddSalesReturn.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call;

            if (insert_update_flag.equals("0")) {
                call = request.addSalesReturn(myPreferences.getPreferences(MyPreferences.id), customerId, customerName.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), narrationEdt.getText().toString(), termsEdt.getText().toString(), items);
            } else {
                call = request.addSalesReturn(myPreferences.getPreferences(MyPreferences.id), customerId, customerName.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), narrationEdt.getText().toString(), termsEdt.getText().toString(), items);
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
                                JSONObject result =json.getJSONObject("result");
                                model.setId(result.getString("id"));
                                model.setCustomer_name(result.getString("customer_name"));
                                model.setSales_return_no(result.getString("sales_return_no"));
                                model.setStatus_name(result.getString("status_name"));
                                model.setGrandtotal(result.getString("grandtotal"));
                                model.setStatus(result.getString("status"));
                                model.setSales_return_format(result.getString("sales_return_date_format"));
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data",model);
                                intent.putExtras(bundle);
                                setResult(10, intent);
                                finish();
                            } else {
                                finish();
                            }

                        } else {
                            Toaster.show(AddSalesReturn.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
