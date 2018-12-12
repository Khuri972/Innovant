package com.innovent.erp.ErpModule.sales_management;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.SearchView.SearchAdapter;
import com.innovent.erp.ErpModule.sales_management.SearchView.SearchItemModel;
import com.innovent.erp.ErpModule.sales_management.Utils.InitiateSearch;
import com.innovent.erp.ErpModule.sales_management.adapter.PendingQuotationAdapter;
import com.innovent.erp.ErpModule.sales_management.adapter.QuatationRequestProductAdapter;
import com.innovent.erp.ErpModule.sales_management.adapter.SearchCustomerAdapter;
import com.innovent.erp.ErpModule.sales_management.adapter.SearchItemAdapter;
import com.innovent.erp.ErpModule.sales_management.model.PendingQuotationModel;
import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.ErpModule.sales_management.model.QuatationRequestModel;
import com.innovent.erp.ErpModule.sales_management.model.SalesOrderModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

public class AddSalesOrder extends AppCompatActivity implements QuatationRequestProductAdapter.Intercommunicator {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listContainer)
    ListView listContainer;
    @BindView(R.id.image_search_back)
    ImageView imageSearchBack;
    @BindView(R.id.edit_text_search)
    EditText editTextSearch;
    @BindView(R.id.clearSearch)
    ImageView clearSearch;
    @BindView(R.id.linearLayout_search)
    LinearLayout linearLayoutSearch;
    @BindView(R.id.line_divider)
    View lineDivider;
    @BindView(R.id.card_search)
    CardView cardSearch;
    @BindView(R.id.toolbar_shadow)
    View toolbarShadow;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.nested)
    NestedScrollView nested;
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

    @BindView(R.id.customer_pincode_edt)
    AutoCompleteTextView customerPincodeEdt;
    @BindView(R.id.customer_area_edt)
    AutoCompleteTextView customerAreaEdt;
    @BindView(R.id.customer_gst_edt)
    AutoCompleteTextView customerGstEdt;
    @BindView(R.id.customer_pancard_edt)
    AutoCompleteTextView customerPancardEdt;

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
    @BindView(R.id.pending_quotation_spinner)
    MaterialSpinner pendingQuotationSpinner;

    @BindView(R.id.search_item_edt)
    AutoCompleteTextView searchItemEdt;
    @BindView(R.id.narration_edt)
    EditText narrationEdt;
    @BindView(R.id.terms_edt)
    EditText termsEdt;
    @BindView(R.id.pending_quotation_layout)
    LinearLayout pendingQuotationLayout;

    TextView product_selection_header_qty, product_selection_header_price, product_selection_header_discount, product_selection_header_discount_amount, product_selection_header_discount_type, product_selection_header_grand_total;

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

    private ArrayList<SearchItemModel> mItem;
    private ArrayList<ProductModel> data = new ArrayList<>();
    private ArrayList<PendingQuotationModel> pendingQuotationModels = new ArrayList<>();
    private SearchAdapter searchAdapter;
    PendingQuotationAdapter quotationAdapter;
    SearchCustomerAdapter customerAdapter;
    SearchItemAdapter itemAdapter;

    QuatationRequestProductAdapter adapter;
    SalesOrderModel model = new SalesOrderModel();

    AlertDialog buildInfosDialog;

    private InitiateSearch initiateSearch;
    String toolbarTitle = "Sales Order";
    StringBuilder serverIds = new StringBuilder();
    String insert_update_flag = "0", customerId = "";
    String cityId = "", districtId = "", stateId = "", stateName = "", countryId = "", zoneId = "";

    int position = 0;
    int total_qty;
    double total_amount;
    double discount = 0;
    double grand_total;
    String discount_type = "1", pendingQuotationId = "", order_id = "";
    double discount_amount, dis = 0; //dialog
    MyPreferences myPreferences;
    CustomDatePicker datePicker;
    JSONArray json_array;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_order);
        ButterKnife.bind(this);
        db = new DBHelper(this);
        myPreferences = new MyPreferences(this);

        product_selection_header_qty = (TextView) findViewById(R.id.product_selection_header_qty);
        product_selection_header_price = (TextView) findViewById(R.id.product_selection_header_price);
        product_selection_header_discount = (TextView) findViewById(R.id.product_selection_header_discount);
        product_selection_header_discount_amount = (TextView) findViewById(R.id.product_selection_header_discount_amount);
        product_selection_header_discount_type = (TextView) findViewById(R.id.product_selection_header_discount_type);  // Rs or %
        product_selection_header_grand_total = (TextView) findViewById(R.id.product_selection_header_grand_total);

        try {
            city_data = db.getCity();
            cityAdapter = new GeneralAdapter(AddSalesOrder.this, city_data);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setHint("Select City");
            SpinnerInteractionListener_city listener_1 = new SpinnerInteractionListener_city();
            citySpinner.setOnTouchListener(listener_1);
            citySpinner.setOnItemSelectedListener(listener_1);

            district_data = db.getDistrict();
            districtAdapter = new GeneralAdapter(AddSalesOrder.this, district_data);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setHint("Select District");
            SpinnerInteractionListener_district listener_2 = new SpinnerInteractionListener_district();
            districtSpinner.setOnTouchListener(listener_2);
            districtSpinner.setOnItemSelectedListener(listener_2);

            state_data = db.getState();
            stateAdapter = new GeneralAdapter(AddSalesOrder.this, state_data);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setHint("Select State");
            SpinnerInteractionListener_state listener_3 = new SpinnerInteractionListener_state();
            stateSpinner.setOnTouchListener(listener_3);
            stateSpinner.setOnItemSelectedListener(listener_3);

            country_data = db.getCountry();
            countryAdapter = new GeneralAdapter(AddSalesOrder.this, country_data);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setHint("Select Country");
            SpinnerInteractionListener_country listener_4 = new SpinnerInteractionListener_country();
            countrySpinner.setOnTouchListener(listener_4);
            countrySpinner.setOnItemSelectedListener(listener_4);

            zone_data = db.getZone();
            zoneAdapter = new GeneralAdapter(AddSalesOrder.this, zone_data);
            zoneSpinner.setAdapter(zoneAdapter);
            zoneSpinner.setHint("Select Zone");
            SpinnerInteractionListener_zone listener_5 = new SpinnerInteractionListener_zone();
            zoneSpinner.setOnTouchListener(listener_5);
            zoneSpinner.setOnItemSelectedListener(listener_5);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mItem = new ArrayList<>();
        searchAdapter = new SearchAdapter(AddSalesOrder.this, mItem);
        listContainer.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

        adapter = new QuatationRequestProductAdapter(AddSalesOrder.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(AddSalesOrder.this, LinearLayoutManager.VERTICAL, false));

        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_flag = bundle.getString("type");

            if (insert_update_flag.equals("0")) {
                submit.setText("Create Order");
            } else {
                toolbarTitle = "Update Sales Order";
                pendingQuotationLayout.setVisibility(View.GONE);
                model = (SalesOrderModel) bundle.getSerializable("order_data");
                data = (ArrayList<ProductModel>) bundle.getSerializable("product_data");
                position = bundle.getInt("position");
                order_id = model.getId();
                pendingQuotationId = model.getQuotation_id();
                customerId = model.getCustomer_id();
                dateEdt.setText("" + model.getOrder_date_format());
                customerName.setText("" + model.getCustomer_name());
                customerEmailEdt.setText("" + model.getCustomer_email());
                customerMobileEdt.setText("" + model.getCustomer_phone_1());
                customerAlterMobileEdt.setText("" + model.getCustomer_phone_2());
                customerAddress1Edt.setText("" + model.getCustomer_address_1());
                customerAddress2Edt.setText("" + model.getCustomer_address_2());
                customerLandmarkEdt.setText("" + model.getCustomer_landmark());
                customerPincodeEdt.setText("" + model.getCustomer_pincode());
                customerGstEdt.setText("" + model.getCustomer_gst_no());
                customerPancardEdt.setText("" + model.getCustomer_pancard_no());
                customerAreaEdt.setText("" + model.getCustomer_area_name());
                submit.setText("Update Quotation");
                narrationEdt.setText("" + model.getNarration());
                termsEdt.setText("" + model.getTerms_condition());
                discount_type = model.getDiscount_type();

                discount = model.getDiscount();
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
                    product_selection_header_discount_amount.setText("" + GlobalElements.DecimalFormat("" + model.getDiscount(), 0) + "%");
                    product_selection_header_discount_type.setText("Rs.");
                }

                product_selection_header_qty.setText("" + model.getTotal_qty());
                product_selection_header_price.setText("" + model.getSubtotal());
                product_selection_header_grand_total.setText("" + model.getGrandtotal());

                adapter = new QuatationRequestProductAdapter(AddSalesOrder.this, data);
                recycleview.setAdapter(adapter);
                recycleview.setLayoutManager(new LinearLayoutManager(AddSalesOrder.this, LinearLayoutManager.VERTICAL, false));


                for (int i = 0; i < city_data.size(); i++) {
                    if (city_data.get(i).getId().equals(model.getCustomer_city())) {
                        cityId = city_data.get(i).getId();
                        citySpinner.setSelection(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < district_data.size(); i++) {
                    if (district_data.get(i).getId().equals(model.getCustomer_district())) {
                        districtId = district_data.get(i).getId();
                        districtSpinner.setSelection(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < state_data.size(); i++) {
                    if (state_data.get(i).getId().equals(model.getCustomer_state())) {
                        stateId = state_data.get(i).getId();
                        stateName = state_data.get(i).getName().toLowerCase();
                        stateSpinner.setSelection(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < country_data.size(); i++) {
                    if (country_data.get(i).getId().equals(model.getCustomer_country())) {
                        countryId = country_data.get(i).getId();
                        countrySpinner.setSelection(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < zone_data.size(); i++) {
                    if (zone_data.get(i).getId().equals(model.getCustomer_zone())) {
                        zoneId = zone_data.get(i).getId();
                        zoneSpinner.setSelection(i + 1);
                        break;
                    }
                }

                //getTotalAmount();
                //getTotalQty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    datePicker = new CustomDatePicker(AddSalesOrder.this);
                    datePicker.setToDate(datePicker.min, dateEdt, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        itemAdapter = new SearchItemAdapter(AddSalesOrder.this, customerId);
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
                        int qty = data.get(position).getQty();
                        if (qty == 0) {
                            qty = 2;
                        } else {
                            qty++;
                        }
                        data.get(position).setQty(qty);
                        adapter.notifyItemChanged(position);
                    } else {
                        ProductModel da = new ProductModel();
                        da.setId("" + itemAdapter.suggestions.get(i).getId());
                        da.setName(itemAdapter.suggestions.get(i).getName());
                        da.setHsnCode(itemAdapter.suggestions.get(i).getHsnCode());
                        da.setUnit(itemAdapter.suggestions.get(i).getUnit());
                        da.setGstType(1);
                        if (stateName.equals("gujarat")) {
                            da.setGstType(0);
                        } else {
                            da.setGstType(1);
                        }
                        da.setQty(itemAdapter.suggestions.get(i).getQty());
                        da.setSell_price(itemAdapter.suggestions.get(i).getSell_price());
                        da.setDiscount(itemAdapter.suggestions.get(i).getDiscount());
                        da.setAmount(itemAdapter.suggestions.get(i).getAmount());
                        da.setGst(itemAdapter.suggestions.get(i).getGst());
                        da.setCgst(itemAdapter.suggestions.get(i).getCgst());
                        da.setSgst(itemAdapter.suggestions.get(i).getSgst());
                        da.setIgst(itemAdapter.suggestions.get(i).getIgst());
                        da.setNetAmount(itemAdapter.suggestions.get(i).getNetAmount());
                        data.add(da);
                        adapter.notifyDataSetChanged();
                    }

                    getTotalQty();
                    getTotalAmount();
                    InputMethodManager imm = (InputMethodManager) AddSalesOrder.this.getSystemService(AddSalesOrder.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchItemEdt.getWindowToken(), 0);
                    searchItemEdt.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        customerAdapter = new SearchCustomerAdapter(AddSalesOrder.this);
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

                    if (!data.isEmpty()) {
                        for (int k = 0; k < data.size(); k++) {
                            if (stateName.equals("gujarat")) {
                                data.get(k).setGstType(0);
                            } else {
                                data.get(k).setGstType(1);
                            }
                            data.get(k).setCgst(0);
                            data.get(k).setSgst(0);
                            data.get(k).setIgst(0);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    InputMethodManager imm = (InputMethodManager) AddSalesOrder.this.getSystemService(AddSalesOrder.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(customerName.getWindowToken(), 0);

                    if (insert_update_flag.equals("0")) {
                        getPendingQuatation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        initiateSearch = new InitiateSearch();
        InitiateToolbarTabs();
        InitiateSearch();
        HandleSearch();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(AddSalesOrder.this)) {
                    int add_flag = 0;
                    JSONObject user;
                    json_array = new JSONArray();
                    for (int i = 0; i < data.size(); i++) {
                        user = new JSONObject();
                        String id = data.get(i).getId();
                        if (data.get(i).getQty() == 0) {
                            add_flag = 1;
                            Toaster.show(AddSalesOrder.this, "Enter Qty", true, Toaster.DANGER);
                            break;
                        }
                        if (data.get(i).getSell_price() == 0) {
                            add_flag = 1;
                            Toaster.show(AddSalesOrder.this, "Enter Rate", true, Toaster.DANGER);
                            break;
                        } else {
                            try {
                                user.put("item_id", "" + id);
                                user.put("qty", "" + data.get(i).getQty());
                                user.put("rate", "" + data.get(i).getSell_price());
                                user.put("discount", "" + data.get(i).getDiscount());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            json_array.put(user);
                            add_flag = 2;
                        }
                    }

                    if (add_flag == 0) {
                        Toaster.show(AddSalesOrder.this, "Add Item", true, Toaster.DANGER);
                    } else if (add_flag == 2) {
                        AlertDialog buildInfosDialog;
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddSalesOrder.this);
                        if (insert_update_flag.equals("0")) {
                            alertDialog2.setTitle("Are you sure want create Order");
                        } else {
                            alertDialog2.setTitle("Are you sure want update Order");
                        }

                        alertDialog2.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        try {
                                            if (GlobalElements.isConnectingToInternet(AddSalesOrder.this)) {
                                                AddSalesOrderService("" + json_array.toString());
                                            } else {
                                                GlobalElements.showDialog(AddSalesOrder.this);
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
                    GlobalElements.showDialog(AddSalesOrder.this);
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

    private void InitiateToolbarTabs() {
        toolbar.setTitleTextColor(AddSalesOrder.this.getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.quatation_create);
        toolbar.setTitle("" + toolbarTitle);
    }

    private void InitiateSearch() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                switch (menuItem) {
                    case android.R.id.home:
                        finish();
                        return true;
                    case R.id.action_discount:
                        try {
                            if (data.size() > 0) {
                                boolean chk_flag = false; // this flag is chek qty empty or not
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getQty() == 0) {
                                        chk_flag = false;
                                        Toaster.show(AddSalesOrder.this, "Enter Qty", true, Toaster.DANGER);
                                        break;
                                    } else {
                                        chk_flag = true;
                                    }
                                }
                                if (chk_flag) {
                                    final TextView d_product_qty, d_product_price, d_product_grand_total, d_product_discount_type, ok, cancle;
                                    final EditText d_product_discount;
                                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddSalesOrder.this);
                                    LayoutInflater inflater = AddSalesOrder.this.getLayoutInflater();
                                    final View vi = inflater.inflate(R.layout.dialog_product, null);
                                    alertDialog2.setView(vi);

                                    d_product_qty = (TextView) vi.findViewById(R.id.dialog_product_qty);
                                    d_product_price = (TextView) vi.findViewById(R.id.dialog_product_price);
                                    d_product_discount = (EditText) vi.findViewById(R.id.dialog_product_discount);
                                    d_product_grand_total = (TextView) vi.findViewById(R.id.dialog_product_grand_total);
                                    d_product_discount_type = (TextView) vi.findViewById(R.id.dialog_product_discount_type);
                                    ok = (TextView) vi.findViewById(R.id.dialog_product_ok);
                                    cancle = (TextView) vi.findViewById(R.id.dialog_product_cancle);

                                    d_product_qty.setText("" + product_selection_header_qty.getText().toString());
                                    d_product_price.setText("" + product_selection_header_price.getText().toString());
                                    d_product_grand_total.setText("" + product_selection_header_grand_total.getText().toString());
                                    d_product_discount.setSelection(Integer.parseInt("" + d_product_discount.getText().toString().length()));
                                    if (discount_type.equals("0")) {
                                        d_product_discount_type.setText("Rs");
                                        d_product_discount_type.setBackground(getResources().getDrawable(R.drawable.textview_price_background));
                                        if (product_selection_header_discount.getText().toString().equals("0.00") || product_selection_header_discount.getText().toString().equals("0")) {
                                            d_product_discount.setText("");
                                        } else {
                                            d_product_discount.setText("" + product_selection_header_discount.getText().toString());
                                        }
                                        d_product_discount.setSelection(Integer.parseInt("" + d_product_discount.getText().toString().length()));
                                    } else {
                                        d_product_discount_type.setText("%");
                                        d_product_discount_type.setBackground(getResources().getDrawable(R.drawable.textview_discount_background));
                                        if (product_selection_header_discount_amount.getText().toString().equals("0")) {
                                            d_product_discount.setText("");
                                        } else {
                                            String[] part = product_selection_header_discount_amount.getText().toString().split("\\%");
                                            d_product_discount.setText("" + part[0]);
                                            d_product_discount.setSelection(Integer.parseInt("" + d_product_discount.getText().toString().length()));
                                        }

                                    }
                                    d_product_discount_type.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                if (discount_type.equals("0")) {

                                                    //discount
                                                    discount_type = "1";
                                                    d_product_discount_type.setText("%");
                                                    d_product_discount_type.setBackground(getResources().getDrawable(R.drawable.textview_discount_background));

                                                    //d_product_grand_total.setText(""+_dis);
                                                    d_product_discount.setText("");
                                                    d_product_grand_total.setText("" + GlobalElements.DecimalFormat("" + d_product_price.getText().toString()));
                                                    d_product_discount.setSelection(Integer.parseInt("" + d_product_discount.getText().toString().length()));

                                                } else {

                                                    //price
                                                    discount_type = "0";
                                                    d_product_discount_type.setText("Rs");
                                                    d_product_discount_type.setBackground(getResources().getDrawable(R.drawable.textview_price_background));

                                                    //d_product_grand_total.setText(""+_dis);
                                                    d_product_discount.setText("");
                                                    d_product_grand_total.setText("" + GlobalElements.DecimalFormat("" + d_product_price.getText().toString()));
                                                    d_product_discount.setSelection(Integer.parseInt("" + d_product_discount.getText().toString().length()));

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    d_product_discount.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            try {
                                                if (d_product_discount.getText().toString().equals("")) {
                                                    d_product_grand_total.setText("" + d_product_price.getText().toString());
                                                } else {
                                                    try {
                                                        if (discount_type.equals("0")) {
                                                            //price
                                                            if (Double.parseDouble(d_product_discount.getText().toString()) <= Double.parseDouble(d_product_price.getText().toString())) {
                                                                dis = 0;
                                                                dis = Double.parseDouble(d_product_price.getText().toString()) - Double.parseDouble(d_product_discount.getText().toString());
                                                                d_product_grand_total.setText("" + GlobalElements.DecimalFormat("" + dis));
                                                            } else {
                                                                Double myDouble = Double.valueOf(d_product_price.getText().toString());
                                                                Integer val = Integer.valueOf(myDouble.intValue());
                                                                d_product_discount.setText("" + val);
                                                                d_product_grand_total.setText("0.0");
                                                                d_product_discount.setSelection(Integer.parseInt("" + d_product_discount.getText().toString().length()));
                                                            }
                                                        } else {
                                                            //discount
                                                            if (Double.parseDouble(d_product_discount.getText().toString()) <= Double.parseDouble("100")) {
                                                                dis = 0;
                                                                dis = +Double.parseDouble(d_product_price.getText().toString()) * Double.parseDouble(d_product_discount.getText().toString()) / 100;
                                                                discount_amount = dis;
                                                                dis = Double.parseDouble(d_product_price.getText().toString()) - Double.parseDouble(GlobalElements.DecimalFormat("" + Math.round(dis)));
                                                                d_product_grand_total.setText("" + GlobalElements.DecimalFormat("" + Math.round(dis)));
                                                            } else {
                                                                d_product_discount.setText("100");
                                                                d_product_grand_total.setText("0.0");
                                                                d_product_discount.setSelection(Integer.parseInt("" + d_product_discount.getText().toString().length()));
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });

                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (d_product_discount.getText().toString().equals("")) {
                                                Toaster.show(AddSalesOrder.this, "Enter Discount Amount", true, Toaster.DANGER);
                                            } else {
                                                try {
                                                    product_selection_header_grand_total.setText("" + GlobalElements.DecimalFormat("" + dis));
                                                    if (discount_type.equals("0")) {
                                                        discount_type = "0";
                                                        product_selection_header_discount_amount.setVisibility(View.GONE);
                                                        product_selection_header_discount_amount.setText("");
                                                        product_selection_header_discount_type.setText("Rs.");
                                                        if (d_product_discount.getText().toString().equals("")) {
                                                            product_selection_header_discount.setText("0");
                                                            discount = 0;
                                                        } else {
                                                            discount = Double.parseDouble(d_product_discount.getText().toString());
                                                            product_selection_header_discount.setText("" + GlobalElements.FirstRemoveZero(d_product_discount.getText().toString()));
                                                        }
                                                    } else {
                                                        discount_type = "1";
                                                        if (d_product_discount.getText().toString().equals("")) {
                                                            product_selection_header_discount.setText("0");
                                                            discount = 0;
                                                        } else {
                                                            product_selection_header_discount.setText("" + GlobalElements.DecimalFormat("" + discount_amount));
                                                            discount = Double.parseDouble(d_product_discount.getText().toString());
                                                        }
                                                        product_selection_header_discount_amount.setVisibility(View.VISIBLE);
                                                        product_selection_header_discount_amount.setText("" + d_product_discount.getText().toString() + "%");
                                                        product_selection_header_discount_type.setText("Rs.");
                                                    }

                                                    getTotalAmount();
                                                    getTotalQty();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                buildInfosDialog.dismiss();
                                            }
                                        }
                                    });
                                    cancle.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            buildInfosDialog.dismiss();
                                        }
                                    });
                                    buildInfosDialog = alertDialog2.create();
                                    buildInfosDialog.setCancelable(false);
                                    buildInfosDialog.show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextSearch.getText().toString().length() == 0) {

                } else {
                    editTextSearch.setText("");
                    searchAdapter.filter(editTextSearch.getText().toString().toLowerCase());
                    ((InputMethodManager) AddSalesOrder.this.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });
    }

    private void HandleSearch() {
        imageSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nested.setVisibility(View.VISIBLE);
                    initiateSearch.handleToolBar(AddSalesOrder.this, cardSearch, toolbar, editTextSearch, toolbarTitle, R.menu.quatation_create);
                    listContainer.setVisibility(View.GONE);
                    nested.setVisibility(View.VISIBLE);
                    toolbarShadow.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /* todo add searchable data in a recycleview */

                try {

                    serverIds.setLength(0);
                    for (int i = 0; i < mItem.size(); i++) {
                        if (mItem.get(i).isChecked()) {
                            ProductModel da = new ProductModel();
                            da.setId("" + mItem.get(i).getId());
                            da.setName("" + mItem.get(i).getName());
                            da.setSell_price(mItem.get(i).getSell_price());
                            //da.setQty(mItem.get(i).getQty());
                            da.setAmount(0);

                            boolean isValidProduct = true;

                            try {
                                for (int j = 0; j < data.size(); j++) {
                                    if (data.get(j).getId().equals(da.getId())) {
                                        isValidProduct = false;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (isValidProduct) {
                                serverIds.append("" + mItem.get(i).getId()).append(",");
                                data.add(da);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    nested.setVisibility(View.VISIBLE);
                    initiateSearch.handleToolBar(AddSalesOrder.this, cardSearch, toolbar, editTextSearch, toolbarTitle, R.menu.quatation_create);
                    nested.setVisibility(View.VISIBLE);
                    toolbarShadow.setVisibility(View.VISIBLE);

                    clearItems();
                    getTotalQty();
                    getTotalAmount();

                    View view = AddSalesOrder.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.filter(editTextSearch.getText().toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void clearItems() {
        listContainer.setVisibility(View.GONE);
        nested.setVisibility(View.VISIBLE);
        mItem.clear();
        searchAdapter.notifyDataSetChanged();
    }

    public void getTotalQty() {
        try {
            total_qty = 0;
            serverIds.setLength(0);
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getQty() != 0) {
                    total_qty += data.get(i).getQty();
                }
            }
            product_selection_header_qty.setText("" + total_qty);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTotalAmount() {
        try {
            total_amount = 0;
            for (int i = 0; i < data.size(); i++) {
                total_amount += data.get(i).getNetAmount();
            }
            product_selection_header_price.setText("" + GlobalElements.DecimalFormat("" + total_amount));

            if (discount_type.equals("0")) {
                //price
                try {
                    if (Double.parseDouble(product_selection_header_discount.getText().toString()) <= Double.parseDouble(product_selection_header_price.getText().toString())) {
                        double dis = 0;
                        dis = Double.parseDouble("" + GlobalElements.DecimalFormat("" + total_amount)) - Double.parseDouble(product_selection_header_discount.getText().toString());
                        product_selection_header_grand_total.setText("" + "" + GlobalElements.DecimalFormat("" + dis));
                        grand_total = dis;

                    } else {
                        product_selection_header_discount.setText("" + "" + GlobalElements.DecimalFormat("" + total_amount));
                        product_selection_header_grand_total.setText("0.0");
                        grand_total = 0;
                        //discount=0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //discount
                try {
                    String[] part = product_selection_header_discount_amount.getText().toString().split("\\%");
                    double discount = Integer.parseInt("" + part[0]);
                    double dis = +Double.parseDouble(product_selection_header_price.getText().toString()) * discount / 100;
                    product_selection_header_discount.setText("" + GlobalElements.DecimalFormat("" + Math.round(dis)));
                    dis = Double.parseDouble(product_selection_header_price.getText().toString()) - Double.parseDouble("" + GlobalElements.DecimalFormat("" + dis));
                    product_selection_header_grand_total.setText("" + GlobalElements.DecimalFormat("" + Math.round(dis)));
                    grand_total = dis;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UpdateQty() {
        getTotalQty();
        getTotalAmount();
    }

    private void AddSalesOrderService(String items) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddSalesOrder.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call;

            if (insert_update_flag.equals("0")) {
                call = request.addOrder(myPreferences.getPreferences(MyPreferences.id), customerId, pendingQuotationId, customerName.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), GlobalElements.getDateFrom_YYYY_MM(), narrationEdt.getText().toString(), termsEdt.getText().toString(), "" + discount, "" + discount_type, items);
            } else {

                call = request.updateOrder(myPreferences.getPreferences(MyPreferences.id), order_id, customerId, pendingQuotationId, customerName.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), GlobalElements.getDateFrom_YYYY_MM(), narrationEdt.getText().toString(), termsEdt.getText().toString(), "" + discount, "" + discount_type, items);
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
                                JSONArray c = json.getJSONArray("result");
                                JSONObject result = c.getJSONObject(0);
                                model.setId("" + result.getString("id"));
                                model.setQuotation_id("" + result.getString("quotation_id"));
                                model.setOrder_no("" + result.getString("order_no"));
                                model.setCustomer_id(result.getString("customer_id"));
                                model.setCustomer_name("" + result.getString("customer_name"));
                                model.setCustomer_email(result.getString("customer_email"));
                                model.setCustomer_phone_1(result.getString("customer_phone_1"));
                                model.setCustomer_phone_2(result.getString("customer_phone_2"));
                                model.setCustomer_address_1(result.getString("customer_address_1"));
                                model.setCustomer_address_2(result.getString("customer_address_2"));
                                model.setCustomer_landmark("");
                                model.setCustomer_country(result.getString("customer_country"));
                                model.setCustomer_state("" + result.getString("customer_state"));
                                model.setCustomer_city(result.getString("customer_city"));
                                model.setCustomer_zone(result.getString("customer_zone"));
                                model.setCustomer_district(result.getString("customer_district"));
                                model.setCustomer_pincode(result.getString("customer_pincode"));
                                model.setCustomer_gst_no(result.getString("customer_gst_no"));
                                model.setCustomer_pancard_no(result.getString("customer_pancard_no"));
                                model.setCustomer_zone_name(result.getString("customer_zone_name"));
                                model.setCustomer_area_name(result.getString("customer_area"));
                                model.setCustomer_gst_no(result.getString("customer_gst_no"));
                                model.setCustomer_pancard_no(result.getString("customer_pancard_no"));
                                model.setNarration(result.getString("narration"));
                                model.setTerms_condition(result.getString("terms_condition"));
                                model.setTotal_qty(result.getInt("total_qty"));
                                model.setSubtotal(result.getDouble("subtotal"));
                                model.setDiscount_type(result.getString("discount_type"));
                                model.setDiscount(result.getDouble("discount"));
                                model.setDiscount_amount(result.getDouble("discount_amount"));
                                model.setGrandtotal(result.getString("grandtotal"));
                                model.setStatus(result.getString("status"));
                                model.setStatus_name(result.getString("status_name"));
                                model.setOrder_date_format(result.getString("order_date"));
                                model.setCustomer_country_name(result.getString("customer_country_name"));
                                model.setCustomer_state_name(result.getString("customer_state_name"));
                                model.setCustomer_city_name(result.getString("customer_city_name"));
                                model.setCustomer_district_name(result.getString("customer_district_name"));
                                model.setCustomer_zone_name(result.getString("customer_zone_name"));

                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", model);
                                intent.putExtras(bundle);
                                setResult(10, intent);
                                finish();
                            } else {
                                JSONArray c = json.getJSONArray("result");
                                JSONObject result = c.getJSONObject(0);
                                model.setQuotation_id("" + result.getString("quotation_id"));
                                model.setOrder_no("" + result.getString("order_no"));
                                model.setCustomer_id(result.getString("customer_id"));
                                model.setCustomer_name("" + result.getString("customer_name"));
                                model.setCustomer_email(result.getString("customer_email"));
                                model.setCustomer_phone_1(result.getString("customer_phone_1"));
                                model.setCustomer_phone_2(result.getString("customer_phone_2"));
                                model.setCustomer_address_1(result.getString("customer_address_1"));
                                model.setCustomer_address_2(result.getString("customer_address_2"));
                                model.setCustomer_landmark("");
                                model.setCustomer_country(result.getString("customer_country"));
                                model.setCustomer_state("" + result.getString("customer_state"));
                                model.setCustomer_city(result.getString("customer_city"));
                                model.setCustomer_zone(result.getString("customer_zone"));
                                model.setCustomer_district(result.getString("customer_district"));
                                model.setCustomer_pincode(result.getString("customer_pincode"));
                                model.setCustomer_gst_no(result.getString("customer_gst_no"));
                                model.setCustomer_pancard_no(result.getString("customer_pancard_no"));
                                model.setCustomer_zone_name(result.getString("customer_zone_name"));
                                model.setCustomer_area_name(result.getString("customer_area"));
                                model.setCustomer_gst_no(result.getString("customer_gst_no"));
                                model.setCustomer_pancard_no(result.getString("customer_pancard_no"));
                                model.setNarration(result.getString("narration"));
                                model.setTerms_condition(result.getString("terms_condition"));
                                model.setTotal_qty(result.getInt("total_qty"));
                                model.setSubtotal(result.getDouble("subtotal"));
                                model.setDiscount_type(result.getString("discount_type"));
                                model.setDiscount(result.getDouble("discount"));
                                model.setDiscount_amount(result.getDouble("discount_amount"));
                                model.setGrandtotal(result.getString("grandtotal"));
                                model.setStatus(result.getString("status"));
                                model.setStatus_name(result.getString("status_name"));
                                model.setOrder_date_format(result.getString("order_date_format"));
                                model.setCustomer_country_name(result.getString("customer_country_name"));
                                model.setCustomer_state_name(result.getString("customer_state_name"));
                                model.setCustomer_city_name(result.getString("customer_city_name"));
                                model.setCustomer_district_name(result.getString("customer_district_name"));
                                model.setCustomer_zone_name(result.getString("customer_zone_name"));

                                data.clear();
                                JSONArray items = json.getJSONArray("items");
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject a = items.getJSONObject(i);
                                    ProductModel da = new ProductModel();
                                    da.setId("" + a.getString("item_id"));
                                    da.setName(a.getString("name"));
                                    da.setHsnCode(a.getString("hsn_code"));
                                    da.setUnit(a.getString("unit_name"));
                                    da.setGstType(1);
                                    da.setQty(a.getInt("qty"));
                                    da.setSell_price(a.getDouble("sell_price"));
                                    da.setDiscount(a.getDouble("discount"));
                                    da.setAmount(0);
                                    da.setGst(a.getInt("gst"));
                                    da.setCgst(0);
                                    da.setSgst(0);
                                    da.setIgst(0);
                                    da.setNetAmount(a.getDouble("net_amount"));
                                    data.add(da);
                                }
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("model", model);
                                bundle.putSerializable("data", data);
                                intent.putExtras(bundle);
                                setResult(11, intent);
                                finish();
                            }

                        } else {
                            Toaster.show(AddSalesOrder.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void getPendingQuatation() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddSalesOrder.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.getPendingQuatation(myPreferences.getPreferences(MyPreferences.id), customerId, "", "", "", "");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        pendingQuotationModels.clear();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                PendingQuotationModel da1 = new PendingQuotationModel();
                                da1.setId(c.getString("id"));
                                da1.setQuotationNo(c.getString("quotation_no"));
                                JSONArray items = c.getJSONArray("items");
                                ArrayList<ProductModel> productModels = new ArrayList<>();
                                for (int j = 0; j < items.length(); j++) {
                                    JSONObject p = items.getJSONObject(j);
                                    ProductModel da = new ProductModel();
                                    da.setId("" + p.getString("item_id"));
                                    da.setName(p.getString("name"));
                                    da.setHsnCode(p.getString("hsn_code"));
                                    da.setUnit(p.getString("unit_name"));
                                    da.setGstType(1);
                                    if (stateName.equals("gujarat")) {
                                        da.setGstType(0);
                                    } else {
                                        da.setGstType(1);
                                    }
                                    da.setQty(p.getInt("qty"));
                                    da.setSell_price(p.getDouble("sell_price"));
                                    da.setDiscount(p.getDouble("discount"));
                                    da.setAmount(p.getDouble("discounted_amount"));
                                    da.setGst(p.getDouble("gst"));
                                    if (stateName.equals("gujarat")) {
                                        da.setCgst(p.getDouble("cgst_tax"));
                                        da.setSgst(p.getDouble("sgst_tax"));
                                        da.setIgst(0);
                                    } else {
                                        da.setCgst(0);
                                        da.setSgst(0);
                                        da.setIgst(p.getDouble("igst_tax"));
                                    }
                                    da.setNetAmount(p.getDouble("net_amount"));
                                    productModels.add(da);
                                }
                                da1.setProductModels(productModels);
                                pendingQuotationModels.add(da1);
                            }
                            quotationAdapter = new PendingQuotationAdapter(AddSalesOrder.this, pendingQuotationModels);
                            pendingQuotationSpinner.setAdapter(quotationAdapter);
                            pendingQuotationSpinner.setHint("Select Pending Quotation");
                            SpinnerInteractionListener_pendingQuotation listener_4 = new SpinnerInteractionListener_pendingQuotation();
                            pendingQuotationSpinner.setOnTouchListener(listener_4);
                            pendingQuotationSpinner.setOnItemSelectedListener(listener_4);
                        } else {
                            quotationAdapter = new PendingQuotationAdapter(AddSalesOrder.this, pendingQuotationModels);
                            pendingQuotationSpinner.setAdapter(quotationAdapter);
                            pendingQuotationSpinner.setHint("Select Pending Quotation");
                            SpinnerInteractionListener_pendingQuotation listener_4 = new SpinnerInteractionListener_pendingQuotation();
                            pendingQuotationSpinner.setOnTouchListener(listener_4);
                            pendingQuotationSpinner.setOnItemSelectedListener(listener_4);
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

    public class SpinnerInteractionListener_pendingQuotation implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    pendingQuotationId = pendingQuotationModels.get(i).getId();
                    pendingQuotationModels.get(i).getProductModels();
                    data.clear();
                    for (int k = 0; k < pendingQuotationModels.get(i).getProductModels().size(); k++) {
                        boolean flag = false;
                        int position = 0;
                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getId().equals("" + pendingQuotationModels.get(i).getProductModels().get(k).getId())) {
                                position = j;
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            int qty = data.get(position).getQty();
                            if (qty == 0) {
                                qty = 2;
                            } else {
                                qty++;
                            }
                            data.get(position).setQty(qty);
                            adapter.notifyItemChanged(position);
                        } else {
                            ProductModel da = new ProductModel();
                            da.setId("" + pendingQuotationModels.get(i).getProductModels().get(k).getId());
                            da.setName(pendingQuotationModels.get(i).getProductModels().get(k).getName());
                            da.setHsnCode(pendingQuotationModels.get(i).getProductModels().get(k).getHsnCode());
                            da.setUnit(pendingQuotationModels.get(i).getProductModels().get(k).getUnit());
                            da.setGstType(1);
                            if (stateName.equals("gujarat")) {
                                da.setGstType(0);
                            } else {
                                da.setGstType(1);
                            }
                            da.setQty(pendingQuotationModels.get(i).getProductModels().get(k).getQty());
                            da.setSell_price(pendingQuotationModels.get(i).getProductModels().get(k).getSell_price());
                            da.setDiscount(pendingQuotationModels.get(i).getProductModels().get(k).getDiscount());
                            da.setAmount(pendingQuotationModels.get(i).getProductModels().get(k).getAmount());
                            da.setGst(pendingQuotationModels.get(i).getProductModels().get(k).getGst());
                            da.setCgst(pendingQuotationModels.get(i).getProductModels().get(k).getCgst());
                            da.setSgst(pendingQuotationModels.get(i).getProductModels().get(k).getSgst());
                            da.setIgst(pendingQuotationModels.get(i).getProductModels().get(k).getIgst());
                            da.setNetAmount(pendingQuotationModels.get(i).getProductModels().get(k).getNetAmount());
                            data.add(da);
                            adapter.notifyDataSetChanged();

                        }
                    }
                    adapter.ChangeData();
                } catch (Exception e) {
                    pendingQuotationId = "";
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }


}
