package com.innovent.erp.helpDesk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.innovent.erp.ErpModule.sales_management.Utils.InitiateSearch;
import com.innovent.erp.ErpModule.sales_management.adapter.SearchCustomerAdapter;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.helpDesk.adapter.DemoSerialNoAdapter;
import com.innovent.erp.helpDesk.model.SerialNoModel;
import com.innovent.erp.helpDesk.searchAdapter.SearchSerialNoItemAdapter;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.TaskAttachmentModel;
import com.innovent.erp.model.TaskModel;
import com.innovent.erp.model.TaskNoteModel;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDemoActivity extends AppCompatActivity {

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
    EditText dateEdt;
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
    /*@BindView(R.id.district_spinner)
    MaterialSpinner districtSpinner;*/
    @BindView(R.id.zone_spinner)
    MaterialSpinner zoneSpinner;
    @BindView(R.id.state_spinner)
    MaterialSpinner stateSpinner;
    @BindView(R.id.country_spinner)
    MaterialSpinner countrySpinner;
    @BindView(R.id.dealer_spinner)
    MaterialSpinner dealerSpinner;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.search_item_edt)
    AutoCompleteTextView searchItemEdt;
    @BindView(R.id.pending_quotation_layout)
    LinearLayout pendingQuotationLayout;
    @BindView(R.id.terms_edt)
    EditText termsEdt;
    @BindView(R.id.narration_edt)
    EditText narrationEdt;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.serial_no_edt)
    EditText serialNoEdt;
    @BindView(R.id.item_name_spinner)
    MaterialSpinner itemNameSpinner;
    @BindView(R.id.model_spinner)
    MaterialSpinner modelSpinner;
    @BindView(R.id.color_spinner)
    MaterialSpinner colorSpinner;
    @BindView(R.id.purchase_date_edt)
    EditText purchaseDateEdt;
    @BindView(R.id.part_guarantee_edt)
    EditText partGuaranteeEdt;
    @BindView(R.id.full_body_guarantee_edt)
    EditText fullBodyGuaranteeEdt;


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

    private ArrayList<SerialNoModel> data = new ArrayList<>();
    ArrayList<GeneralModel> dealerModels = new ArrayList<>();
    GeneralAdapter delearAdapter;

    ArrayList<GeneralModel> itemNameModels = new ArrayList<>();
    GeneralAdapter itemNameAdapter;

    ArrayList<GeneralModel> itemModels = new ArrayList<>();
    GeneralAdapter itemModelAdapter;

    ArrayList<GeneralModel> itemColorModels = new ArrayList<>();
    GeneralAdapter itemColorAdapter;

    SearchCustomerAdapter customerAdapter;
    SearchSerialNoItemAdapter itemAdapter;

    DemoSerialNoAdapter adapter;
    TaskModel model = new TaskModel();

    private InitiateSearch initiateSearch;
    String toolbarTitle = "Create Demo";
    String insert_update_flag = "0", customerId = "";
    String cityId = "", districtId = "", stateId = "", stateName = "", countryId = "", zoneId = "";
    String itemId = "", modelId = "", colorId = "";

    int position = 0;
    String activityType = "0", status = "", dealer_id = "", order_id = "";
    MyPreferences myPreferences;
    CustomDatePicker datePicker;
    JSONArray json_array;
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demo);
        ButterKnife.bind(this);
        db = new DBHelper(this);
        myPreferences = new MyPreferences(this);
        GlobalElements.editTextAllCaps(AddDemoActivity.this, mainLayout);

        try {
            city_data = db.getCity();
            cityAdapter = new GeneralAdapter(AddDemoActivity.this, city_data);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setHint("Select City");
            SpinnerInteractionListener_city listener_1 = new SpinnerInteractionListener_city();
            citySpinner.setOnTouchListener(listener_1);
            citySpinner.setOnItemSelectedListener(listener_1);

            /*district_data = db.getDistrict();
            districtAdapter = new GeneralAdapter(AddDemoActivity.this, district_data);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setHint("Select District");
            SpinnerInteractionListener_district listener_2 = new SpinnerInteractionListener_district();
            districtSpinner.setOnTouchListener(listener_2);
            districtSpinner.setOnItemSelectedListener(listener_2);*/

            state_data = db.getState();
            stateAdapter = new GeneralAdapter(AddDemoActivity.this, state_data);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setHint("Select State");
            SpinnerInteractionListener_state listener_3 = new SpinnerInteractionListener_state();
            stateSpinner.setOnTouchListener(listener_3);
            stateSpinner.setOnItemSelectedListener(listener_3);

            country_data = db.getCountry();
            countryAdapter = new GeneralAdapter(AddDemoActivity.this, country_data);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setHint("Select Country");
            SpinnerInteractionListener_country listener_4 = new SpinnerInteractionListener_country();
            countrySpinner.setOnTouchListener(listener_4);
            countrySpinner.setOnItemSelectedListener(listener_4);

            zone_data = db.getZone();
            zoneAdapter = new GeneralAdapter(AddDemoActivity.this, zone_data);
            zoneSpinner.setAdapter(zoneAdapter);
            zoneSpinner.setHint("Select Zone");
            SpinnerInteractionListener_zone listener_5 = new SpinnerInteractionListener_zone();
            zoneSpinner.setOnTouchListener(listener_5);
            zoneSpinner.setOnItemSelectedListener(listener_5);

        } catch (Exception e) {
            e.printStackTrace();
        }


        adapter = new DemoSerialNoAdapter(AddDemoActivity.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(AddDemoActivity.this, LinearLayoutManager.VERTICAL, false));

        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_flag = bundle.getString("type");
            status = bundle.getString("status");
            activityType = bundle.getString("activityType");

            if (insert_update_flag.equals("0")) {

                if (activityType.equals("demo")) {
                    submit.setText("Create Demo");
                    toolbarTitle = "Create Demo";
                } else if (activityType.equals("complain")) {
                    submit.setText("Create Complain");
                    toolbarTitle = "Create Complain";
                } else {
                    submit.setText("Create Installation");
                    toolbarTitle = "Create Installation";
                }
            } else {
                if (activityType.equals("demo")) {
                    submit.setText("Update Demo");
                    toolbarTitle = "Update Demo";
                } else if (activityType.equals("complain")) {
                    submit.setText("Update Complain");
                    toolbarTitle = "Update Complain";
                } else {
                    submit.setText("Update Installation");
                    toolbarTitle = "Update Installation";
                }

                model = (TaskModel) bundle.getSerializable("data");
                position = bundle.getInt("position");
                order_id = model.getId();
                dealer_id = model.getDealer_id();
                customerId = model.getCustomer_id();
                dateEdt.setText("" + model.getTask_create_date());
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
                narrationEdt.setText("" + model.getNarration());
                termsEdt.setText("" + model.getTerms_condition());

                disbaleEdittextview(customerName);
                disbaleEdittextview(customerEmailEdt);
                disbaleEdittextview(customerMobileEdt);
                disbaleEdittextview(customerAlterMobileEdt);
                disbaleEdittextview(customerAddress1Edt);
                disbaleEdittextview(customerAddress2Edt);
                disbaleEdittextview(customerLandmarkEdt);
                disbaleEdittextview(customerLandmarkEdt);
                disbaleEdittextview(customerPincodeEdt);
                disbaleEdittextview(customerGstEdt);
                disbaleEdittextview(customerPancardEdt);
                disbaleEdittextview(customerAreaEdt);

                try {
                    if (!model.getTask_priority().equals("")) {
                        ratingBar.setRating(Float.parseFloat("" + model.getTask_priority()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < city_data.size(); i++) {
                    if (city_data.get(i).getId().equals(model.getCustomer_city())) {
                        cityId = city_data.get(i).getId();
                        citySpinner.setSelection(i + 1);
                        citySpinner.setEnabled(false);
                        break;
                    }
                }

                for (int i = 0; i < state_data.size(); i++) {
                    if (state_data.get(i).getId().equals(model.getCustomer_state())) {
                        stateId = state_data.get(i).getId();
                        stateName = state_data.get(i).getName().toLowerCase();
                        stateSpinner.setSelection(i + 1);
                        stateSpinner.setEnabled(false);
                        break;
                    }
                }

                for (int i = 0; i < country_data.size(); i++) {
                    if (country_data.get(i).getId().equals(model.getCustomer_country())) {
                        countryId = country_data.get(i).getId();
                        countrySpinner.setSelection(i + 1);
                        countrySpinner.setEnabled(false);
                        break;
                    }
                }

                for (int i = 0; i < zone_data.size(); i++) {
                    if (zone_data.get(i).getId().equals(model.getCustomer_zone())) {
                        zoneId = zone_data.get(i).getId();
                        zoneSpinner.setSelection(i + 1);
                        zoneSpinner.setEnabled(false);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GlobalElements.isConnectingToInternet(AddDemoActivity.this)) {
            getDealer();
        } else {
            GlobalElements.showDialog(AddDemoActivity.this);
        }

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    datePicker = new CustomDatePicker(AddDemoActivity.this);
                    datePicker.setToDate(datePicker.min, dateEdt, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        purchaseDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    datePicker = new CustomDatePicker(AddDemoActivity.this);
                    datePicker.setToDate(datePicker.empty, purchaseDateEdt, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        customerAdapter = new SearchCustomerAdapter(AddDemoActivity.this);
        customerName.setAdapter(customerAdapter);

        itemAdapter = new SearchSerialNoItemAdapter(AddDemoActivity.this, customerId);
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
                        da.setBrand(itemAdapter.suggestions.get(i).getBrand());
                        da.setModel(itemAdapter.suggestions.get(i).getModel());
                        da.setColor(itemAdapter.suggestions.get(i).getColor());
                        da.setSerialNo(itemAdapter.suggestions.get(i).getSerialNo());
                        da.setPurchaseDate(itemAdapter.suggestions.get(i).getPurchaseDate());
                        da.setQty(itemAdapter.suggestions.get(i).getQty());
                        data.add(da);
                        adapter.notifyDataSetChanged();
                    }
                    InputMethodManager imm = (InputMethodManager) AddDemoActivity.this.getSystemService(AddDemoActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchItemEdt.getWindowToken(), 0);
                    searchItemEdt.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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

                    /*for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(customerAdapter.suggestions.get(arg2).getDistrict())) {
                            districtId = district_data.get(i).getId();
                            districtSpinner.setSelection(i + 1);
                            break;
                        }
                    }
*/
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
                    InputMethodManager imm = (InputMethodManager) AddDemoActivity.this.getSystemService(AddDemoActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(customerName.getWindowToken(), 0);
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
                if (GlobalElements.isConnectingToInternet(AddDemoActivity.this)) {
                    json_array = new JSONArray();

                    if (!serialNoEdt.getText().toString().equals("") && itemId.equals("")) {
                        Toaster.show(AddDemoActivity.this, "Select Item Name", false, Toaster.DANGER);
                    } else if (!itemId.equals("") && modelId.equals("")) {
                        Toaster.show(AddDemoActivity.this, "Select Model", false, Toaster.DANGER);
                    } else if (!modelId.equals("") && colorId.equals("")) {
                        Toaster.show(AddDemoActivity.this, "Select Color", false, Toaster.DANGER);
                    } else if (!colorId.equals("") && purchaseDateEdt.getText().toString().equals("")) {
                        Toaster.show(AddDemoActivity.this, "Enter Purchase Date", false, Toaster.DANGER);
                    } else if (!purchaseDateEdt.getText().toString().equals("") && partGuaranteeEdt.getText().toString().equals("")) {
                        Toaster.show(AddDemoActivity.this, "Enter Part Guarantee", false, Toaster.DANGER);
                    } else if (!partGuaranteeEdt.getText().toString().equals("") && fullBodyGuaranteeEdt.getText().toString().equals("")) {
                        Toaster.show(AddDemoActivity.this, "Enter Full Body Guarantee", false, Toaster.DANGER);
                    } else {

                        if (!serialNoEdt.getText().toString().equals("")) {
                            if (customerId.equals("")) {
                                Toaster.show(AddDemoActivity.this, "Select Customer", true, Toaster.DANGER);
                            } else if (dealer_id.equals("")) {
                                Toaster.show(AddDemoActivity.this, "Select Dealer", true, Toaster.DANGER);
                            } else {
                                AlertDialog buildInfosDialog;
                                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddDemoActivity.this);
                                if (insert_update_flag.equals("0")) {
                                    alertDialog2.setTitle("Are you sure want create demo");
                                } else {
                                    alertDialog2.setTitle("Are you sure want update demo");
                                }

                                alertDialog2.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to execute after dialog
                                                try {
                                                    if (GlobalElements.isConnectingToInternet(AddDemoActivity.this)) {
                                                        AddDemo("" + json_array.toString());
                                                    } else {
                                                        GlobalElements.showDialog(AddDemoActivity.this);
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
                            if (!data.isEmpty()) {
                                int add_flag = 0;
                                JSONObject user;
                                json_array = new JSONArray();
                                for (int i = 0; i < data.size(); i++) {
                                    user = new JSONObject();
                                    String id = data.get(i).getId();
                                    if (data.get(i).getQty() == 0) {
                                        add_flag = 1;
                                        Toaster.show(AddDemoActivity.this, "Enter Qty", true, Toaster.DANGER);
                                        break;
                                    }
                                    if (data.get(i).getQty() == 0) {
                                        add_flag = 1;
                                        Toaster.show(AddDemoActivity.this, "Enter Rate", true, Toaster.DANGER);
                                        break;
                                    } else {
                                        try {
                                            user.put("delivery_order_item_batch_id", "" + id);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        json_array.put(user);
                                        add_flag = 2;
                                    }
                                }

                                if (add_flag == 0) {
                                    Toaster.show(AddDemoActivity.this, "Add Item", true, Toaster.DANGER);
                                } else if (customerId.equals("")) {
                                    Toaster.show(AddDemoActivity.this, "Select Customer", true, Toaster.DANGER);
                                } else if (dealer_id.equals("")) {
                                    Toaster.show(AddDemoActivity.this, "Select Dealer", true, Toaster.DANGER);
                                } else if (add_flag == 2) {
                                    AlertDialog buildInfosDialog;
                                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddDemoActivity.this);
                                    if (insert_update_flag.equals("0")) {
                                        alertDialog2.setTitle("Are you sure want create demo");
                                    } else {
                                        alertDialog2.setTitle("Are you sure want update demo");
                                    }

                                    alertDialog2.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to execute after dialog
                                                    try {
                                                        if (GlobalElements.isConnectingToInternet(AddDemoActivity.this)) {
                                                            AddDemo("" + json_array.toString());
                                                        } else {
                                                            GlobalElements.showDialog(AddDemoActivity.this);
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
                                Toaster.show(AddDemoActivity.this, "Add Item", true, Toaster.DANGER);
                            }
                        }
                    }
                } else {
                    GlobalElements.showDialog(AddDemoActivity.this);
                }
            }
        });

    }

    public void disbaleEdittextview(EditText view) {
        view.setFocusableInTouchMode(false);
        view.setCursorVisible(false);
        view.setFocusable(true);
        view.setClickable(false);
    }

    private void InitiateToolbarTabs() {
        toolbar.setTitleTextColor(AddDemoActivity.this.getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.create_demo);
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
                    ((InputMethodManager) AddDemoActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
                    initiateSearch.handleToolBar(AddDemoActivity.this, cardSearch, toolbar, editTextSearch, toolbarTitle, R.menu.quatation_create);
                    listContainer.setVisibility(View.GONE);
                    nested.setVisibility(View.VISIBLE);
                    toolbarShadow.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /* todo add searchable data in a recycleview */
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDealer() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddDemoActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.getDealer(myPreferences.getPreferences(MyPreferences.id));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        dealerModels.clear();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                GeneralModel da1 = new GeneralModel();
                                da1.setId(c.getString("id"));
                                da1.setName(c.getString("name"));
                                dealerModels.add(da1);
                            }
                            delearAdapter = new GeneralAdapter(AddDemoActivity.this, dealerModels);
                            dealerSpinner.setAdapter(delearAdapter);
                            dealerSpinner.setHint("Select Dealer");
                            SpinnerInteractionListener_dealer listener_4 = new SpinnerInteractionListener_dealer();
                            dealerSpinner.setOnTouchListener(listener_4);
                            dealerSpinner.setOnItemSelectedListener(listener_4);

                            if (insert_update_flag.equals("1")) {
                                for (int i = 0; i < dealerModels.size(); i++) {
                                    if (dealer_id.equals("" + dealerModels.get(i).getId())) {
                                        dealerSpinner.setSelection(i + 1);
                                        dealerSpinner.setEnabled(false);
                                        break;
                                    }
                                }
                            }


                            JSONArray itemNameArray = json.getJSONArray("items");
                            for (int i = 0; i < itemNameArray.length(); i++) {
                                JSONObject c = itemNameArray.getJSONObject(i);
                                GeneralModel da1 = new GeneralModel();
                                da1.setId(c.getString("id"));
                                da1.setName(c.getString("name"));
                                itemNameModels.add(da1);
                            }

                            itemNameAdapter = new GeneralAdapter(AddDemoActivity.this, itemNameModels);
                            itemNameSpinner.setAdapter(itemNameAdapter);
                            itemNameSpinner.setHint("Select Item Name");
                            SpinnerInteractionListener_itemName listener = new SpinnerInteractionListener_itemName();
                            itemNameSpinner.setOnTouchListener(listener);
                            itemNameSpinner.setOnItemSelectedListener(listener);

                            JSONArray itemcolorArray = json.getJSONArray("color");
                            for (int i = 0; i < itemcolorArray.length(); i++) {
                                JSONObject c = itemcolorArray.getJSONObject(i);
                                GeneralModel da1 = new GeneralModel();
                                da1.setId(c.getString("id"));
                                da1.setName(c.getString("name"));
                                itemColorModels.add(da1);
                            }

                            itemColorAdapter = new GeneralAdapter(AddDemoActivity.this, itemColorModels);
                            colorSpinner.setAdapter(itemColorAdapter);
                            colorSpinner.setHint("Select Color");
                            SpinnerInteractionListener_itemColor listener_1 = new SpinnerInteractionListener_itemColor();
                            colorSpinner.setOnTouchListener(listener_1);
                            colorSpinner.setOnItemSelectedListener(listener_1);

                            JSONArray itemmodelArray = json.getJSONArray("model");
                            for (int i = 0; i < itemmodelArray.length(); i++) {
                                JSONObject c = itemmodelArray.getJSONObject(i);
                                GeneralModel da1 = new GeneralModel();
                                da1.setId(c.getString("id"));
                                da1.setName(c.getString("name"));
                                itemModels.add(da1);
                            }

                            itemModelAdapter = new GeneralAdapter(AddDemoActivity.this, itemModels);
                            modelSpinner.setAdapter(itemModelAdapter);
                            modelSpinner.setHint("Select Model");
                            SpinnerInteractionListener_itemModel listener_2 = new SpinnerInteractionListener_itemModel();
                            modelSpinner.setOnTouchListener(listener_2);
                            modelSpinner.setOnItemSelectedListener(listener_2);

                        } else {
                            delearAdapter = new GeneralAdapter(AddDemoActivity.this, dealerModels);
                            dealerSpinner.setAdapter(delearAdapter);
                            dealerSpinner.setHint("Select Dealer");
                            SpinnerInteractionListener_dealer listener_4 = new SpinnerInteractionListener_dealer();
                            dealerSpinner.setOnTouchListener(listener_4);
                            dealerSpinner.setOnItemSelectedListener(listener_4);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (insert_update_flag.equals("1")) {
                        ViewOrder();
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

    private void AddDemo(String items) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddDemoActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call;

            if (insert_update_flag.equals("0")) {
                call = request.addDemo(myPreferences.getPreferences(MyPreferences.id), customerId, customerName.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), narrationEdt.getText().toString(), termsEdt.getText().toString(), "" + GlobalElements.getCurrentdate(), "" + ratingBar.getRating(), dealer_id, "" + activityType, itemId, modelId, colorId, purchaseDateEdt.getText().toString(), partGuaranteeEdt.getText().toString(), fullBodyGuaranteeEdt.getText().toString(), serialNoEdt.getText().toString(), items);
            } else {

                call = request.updateDemo(myPreferences.getPreferences(MyPreferences.id), model.getId(), customerId, customerName.getText().toString(), customerEmailEdt.getText().toString(),
                        customerMobileEdt.getText().toString(), customerAlterMobileEdt.getText().toString(), customerAddress1Edt.getText().toString(), customerAddress2Edt.getText().toString(),
                        customerLandmarkEdt.getText().toString(), cityId, stateId, countryId,
                        customerPincodeEdt.getText().toString(), zoneId, customerAreaEdt.getText().toString(), districtId, "", customerGstEdt.getText().toString(),
                        customerPancardEdt.getText().toString(), narrationEdt.getText().toString(), termsEdt.getText().toString(), "" + GlobalElements.getCurrentdate(), "" + ratingBar.getRating(), dealer_id, "" + activityType, itemId, modelId, colorId, purchaseDateEdt.getText().toString(), partGuaranteeEdt.getText().toString(), fullBodyGuaranteeEdt.getText().toString(), serialNoEdt.getText().toString(), items);
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

                                JSONObject c = json.getJSONObject("result");
                                model.setId(c.getString("id"));
                                model.setPipelineType(c.getString("type"));
                                model.setCustomer_id(c.getString("customer_id"));
                                model.setCustomer_name(c.getString("customer_name"));
                                model.setCustomer_email(c.getString("customer_email"));
                                model.setCustomer_phone_1(c.getString("customer_phone_1"));
                                model.setCustomer_phone_2(c.getString("customer_phone_2"));
                                model.setCustomer_address_1(c.getString("customer_address_1"));
                                model.setCustomer_address_2(c.getString("customer_address_2"));
                                model.setCustomer_area_name(c.getString("customer_area"));
                                model.setCustomer_city(c.getString("customer_city_id"));
                                model.setCustomer_city_name(c.getString("customer_city"));
                                model.setCustomer_district("");//no required
                                model.setCustomer_district_name("");//no required
                                model.setCustomer_zone(c.getString("customer_zone_id"));
                                model.setCustomer_zone_name(c.getString("customer_zone"));
                                model.setCustomer_state(c.getString("customer_state_id"));
                                model.setCustomer_state_name(c.getString("customer_state"));
                                model.setCustomer_country(c.getString("customer_country_id"));
                                model.setCustomer_country_name(c.getString("customer_country"));

                                model.setCustomer_pincode(c.getString("customer_pincode"));
                                model.setCustomer_gst_no(c.getString("customer_gst_no"));
                                model.setCustomer_pancard_no(c.getString("customer_pancard_no"));
                                model.setDealer_id(c.getString("dealer_id"));
                                model.setNarration(c.getString("narration"));
                                model.setTerms_condition(c.getString("terms_condition"));

                                model.setTask_no(c.getString("request_no"));
                                model.setTask_name(c.getString("request_no"));
                                model.setTask_deadline(c.getString("deadline_date_format").trim());
                                model.setTask_description("");
                                model.setTask_type(c.getString("type"));
                                model.setTask_color_tag("");
                                model.setTask_assigned_by("");
                                model.setTask_assigned_by_name("");
                                model.setReplace_tach_remarks("");
                                model.setReplace_tach_created_date_format("");
                                model.setIsActive(c.getString("isActive"));

                                if (c.getString("type").equals("demo")) {
                                    model.setTask_color_tag_slug("#FF0000"); // red
                                } else if (c.getString("type").equals("complain")) {
                                    model.setTask_color_tag_slug("#0c08d8"); // blue
                                } else {
                                    model.setTask_color_tag_slug("#197b30"); // green
                                }
                                model.setTask_create_date(c.getString("request_date_format").trim());
                                model.setTask_priority(c.getString("demo_priority"));
                                model.setTask_assigned_to("");
                                model.setTask_assigned_to_name("");
                                ArrayList<TaskNoteModel> noteModels = new ArrayList<TaskNoteModel>();
                                model.setNoteModels(noteModels);
                                ArrayList<TaskAttachmentModel> attachmentModels = new ArrayList<TaskAttachmentModel>();
                                model.setAttachment("0");
                                model.setAttachmentModels(attachmentModels);

                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", model);
                                bundle.putInt("position", position);
                                intent.putExtras(bundle);
                                setResult(18, intent);
                                finish();
                            } else {
                                JSONObject c = json.getJSONObject("result");

                                model.setCustomer_id(c.getString("customer_id"));
                                model.setCustomer_name(c.getString("customer_name"));
                                model.setCustomer_email(c.getString("customer_email"));
                                model.setCustomer_phone_1(c.getString("customer_phone_1"));
                                model.setCustomer_phone_2(c.getString("customer_phone_2"));
                                model.setCustomer_address_1(c.getString("customer_address_1"));
                                model.setCustomer_address_2(c.getString("customer_address_2"));
                                model.setCustomer_area_name(c.getString("customer_area"));
                                model.setCustomer_city(c.getString("customer_city_id"));
                                model.setCustomer_city_name(c.getString("customer_city"));
                                model.setCustomer_district("");//no required
                                model.setCustomer_district_name("");//no required
                                model.setCustomer_zone(c.getString("customer_zone_id"));
                                model.setCustomer_zone_name(c.getString("customer_zone"));
                                model.setCustomer_state(c.getString("customer_state_id"));
                                model.setCustomer_state_name(c.getString("customer_state"));
                                model.setCustomer_country(c.getString("customer_country_id"));
                                model.setCustomer_country_name(c.getString("customer_country"));

                                model.setCustomer_pincode(c.getString("customer_pincode"));
                                model.setCustomer_gst_no(c.getString("customer_gst_no"));
                                model.setCustomer_pancard_no(c.getString("customer_pancard_no"));

                                model.setTask_priority(c.getString("demo_priority"));
                                model.setNarration(c.getString("narration"));
                                model.setTerms_condition(c.getString("terms_condition"));
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", model);
                                bundle.putInt("position", position);
                                intent.putExtras(bundle);
                                setResult(19, intent);
                                finish();
                            }

                        } else {
                            Toaster.show(AddDemoActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void ViewOrder() {
        final ProgressDialog pd = new ProgressDialog(AddDemoActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.getPipelineViewTask(model.getId(), myPreferences.getPreferences(MyPreferences.id));
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
                        String item_flag = "0";
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject c = items.getJSONObject(i);
                            SerialNoModel da = new SerialNoModel();
                            da.setId("" + c.getString("delivery_order_item_batch_id"));
                            da.setName(c.getString("name"));
                            da.setBrand(c.getString("brand_name"));
                            da.setModel(c.getString("model_name"));
                            da.setColor(c.getString("color_name"));
                            da.setSerialNo(c.getString("batch_no"));
                            da.setPurchaseDate("" + c.getString("purchase_date"));
                            da.setQty(c.getInt("qty"));

                            item_flag = c.getString("item_flag");
                            if (item_flag.equals("0")) {
                                data.add(da);
                            } else {
                                itemId = c.getString("item_id");
                                modelId = c.getString("model");
                                colorId = c.getString("color");

                                serialNoEdt.setText("" + c.getString("batch_no"));
                                purchaseDateEdt.setText("" + c.getString("purchase_date_format"));
                                partGuaranteeEdt.setText("" + c.getString("part_guarantee"));
                                fullBodyGuaranteeEdt.setText("" + c.getString("full_guarantee"));
                            }
                        }
                        adapter = new DemoSerialNoAdapter(AddDemoActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(AddDemoActivity.this, LinearLayoutManager.VERTICAL, false));


                        for (int i = 0; i < itemNameModels.size(); i++) {
                            if (itemNameModels.get(i).getId().equals("" + itemId)) {
                                itemNameSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < itemModels.size(); i++) {
                            if (itemModels.get(i).getId().equals("" + modelId)) {
                                modelSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < itemColorModels.size(); i++) {
                            if (itemColorModels.get(i).getId().equals("" + colorId)) {
                                colorSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                    } else {
                        adapter = new DemoSerialNoAdapter(AddDemoActivity.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(AddDemoActivity.this, LinearLayoutManager.VERTICAL, false));
                        //Toaster.show(AddDemoActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    public class SpinnerInteractionListener_dealer implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    dealer_id = dealerModels.get(i).getId();
                } catch (Exception e) {
                    dealer_id = "";
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_itemName implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    itemId = itemNameModels.get(i).getId();
                } catch (Exception e) {
                    itemId = "";
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_itemModel implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    modelId = itemModels.get(i).getId();
                } catch (Exception e) {
                    modelId = "";
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_itemColor implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    colorId = itemColorModels.get(i).getId();
                } catch (Exception e) {
                    colorId = "";
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}
