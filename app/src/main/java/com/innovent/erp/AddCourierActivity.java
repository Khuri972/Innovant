package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.adapter.SearchAreaAdapter;
import com.innovent.erp.adapter.SearchReciverCompanyAdapter;
import com.innovent.erp.adapter.SearchReciverPersonAdapter;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCourierActivity extends AppCompatActivity {


    ArrayList<GeneralModel> percel_type = new ArrayList<>();
    ArrayList<GeneralModel> deliverdStatus = new ArrayList<>();
    GeneralAdapter percelTypeAdapter;
    GeneralAdapter deliverd_adapter;

    String cityId = "", districtId = "", stateId = "", countryId = "", zoneId = "";
    String cityReceiverId = "", districtReceiverId = "", stateReceiverId = "", countryReceiverId = "", zoneReceiverId = "";
    String deliveryId = "", typeId = "", courierType = "";
    DBHelper db;
    MyPreferences myPreferences;
    CustomDatePicker customerDatePicker;

    SearchReciverCompanyAdapter reciverCompanyAdapter;
    SearchReciverCompanyAdapter senderCompanyAdapter;

    SearchReciverPersonAdapter reciverPersonAdapter;
    SearchReciverPersonAdapter senderPersonAdapter;
    SearchReciverPersonAdapter senderassignorPersonAdapter;
    SearchReciverPersonAdapter receiverassignorPersonAdapter;

    SearchReciverCompanyAdapter courierCompanyAdapter;

    @BindView(R.id.radioSender)
    RadioButton radioSender;
    @BindView(R.id.radioReciver)
    RadioButton radioReciver;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.input_sender_company)
    AutoCompleteTextView inputSenderCompany;
    @BindView(R.id.input_sender_person)
    AutoCompleteTextView inputSenderPerson;
    @BindView(R.id.input_sender_person_name)
    AutoCompleteTextView inputSenderPersonName;
    @BindView(R.id.input_sender_person_mobile)
    AutoCompleteTextView inputSenderPersonMobile;
    @BindView(R.id.sender_person_layout)
    LinearLayout senderPersonLayout;
    @BindView(R.id.label_spinner)
    MaterialSpinner labelSpinner;
    @BindView(R.id.addLabel)
    ImageView addLabel;
    @BindView(R.id.sender_card)
    CardView senderCard;
    @BindView(R.id.input_reciver_company)
    AutoCompleteTextView inputReciverCompany;
    @BindView(R.id.input_reciver_person)
    AutoCompleteTextView inputReciverPerson;
    @BindView(R.id.input_receiver_address)
    AutoCompleteTextView inputReceiverAddress;
    @BindView(R.id.input_receiver_country)
    AutoCompleteTextView inputReceiverCountry;
    @BindView(R.id.input_receiver_state)
    AutoCompleteTextView inputReceiverState;
    @BindView(R.id.input_receiver_city)
    AutoCompleteTextView inputReceiverCity;
    @BindView(R.id.input_receiver_pin)
    EditText inputReceiverPin;
    @BindView(R.id.input_receiver_mobile)
    EditText inputReceiverMobile;
    @BindView(R.id.reciver_card)
    CardView reciverCard;
    @BindView(R.id.change_layout)
    LinearLayout changeLayout;
    @BindView(R.id.percel_type)
    MaterialSpinner percelType;
    @BindView(R.id.input_description)
    EditText inputDescription;
    @BindView(R.id.input_layout_description)
    TextInputLayout inputLayoutDescription;
    @BindView(R.id.input_weight)
    EditText inputWeight;
    @BindView(R.id.input_weight_gm)
    EditText inputWeightGm;
    @BindView(R.id.input_cost)
    EditText inputCost;
    @BindView(R.id.input_courier_company_name)
    AutoCompleteTextView inputCourierCompanyName;
    @BindView(R.id.input_pickup_person)
    EditText inputPickupPerson;
    @BindView(R.id.input_deliverd_date)
    EditText inputDeliverdDate;
    @BindView(R.id.input_tracking_no)
    EditText inputTrackingNo;
    @BindView(R.id.deliver_status_spinner)
    MaterialSpinner deliverStatusSpinner;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.input_sender_address)
    AutoCompleteTextView inputSenderAddress;
    @BindView(R.id.input_sender_country)
    AutoCompleteTextView inputSenderCountry;
    @BindView(R.id.input_sender_state)
    AutoCompleteTextView inputSenderState;
    @BindView(R.id.input_sender_city)
    AutoCompleteTextView inputSenderCity;
    @BindView(R.id.input_sender_pin)
    EditText inputSenderPin;
    @BindView(R.id.input_sender_mobile)
    EditText inputSenderMobile;
    @BindView(R.id.sender_layout)
    LinearLayout senderLayout;
    @BindView(R.id.reciver_layout)
    LinearLayout reciverLayout;
    @BindView(R.id.input_receiver_person_name)
    AutoCompleteTextView inputReceiverPersonName;
    @BindView(R.id.input_receiver_person_mobile)
    AutoCompleteTextView inputReceiverPersonMobile;
    @BindView(R.id.receiver_person_layout)
    LinearLayout receiverPersonLayout;

    String assigned_person_name, assigned_person_mobile;

    /* 28-05-2018 */
    @BindView(R.id.input_receiver_area)
    AutoCompleteTextView inputReceiverArea;
    @BindView(R.id.input_layout_area)
    TextInputLayout inputLayoutArea;
    @BindView(R.id.city_spinner)
    MaterialSpinner citySpinner;
    @BindView(R.id.district_spinner)
    MaterialSpinner districtSpinner;
    @BindView(R.id.state_spinner)
    MaterialSpinner stateSpinner;
    @BindView(R.id.country_spinner)
    MaterialSpinner countrySpinner;
    @BindView(R.id.zone_spinner)
    MaterialSpinner zoneSpinner;

    @BindView(R.id.input_sender_area)
    AutoCompleteTextView inputSenderArea;
    @BindView(R.id.city_sender_spinner)
    MaterialSpinner citySenderSpinner;
    @BindView(R.id.district_sender_spinner)
    MaterialSpinner districtSenderSpinner;
    @BindView(R.id.state_sender_spinner)
    MaterialSpinner stateSenderSpinner;
    @BindView(R.id.country_sender_spinner)
    MaterialSpinner countrySenderSpinner;
    @BindView(R.id.zone_sender_spinner)
    MaterialSpinner zoneSenderSpinner;

    SearchAreaAdapter areaAdapter;
    SearchAreaAdapter areaSenderAdapter;

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

    GeneralAdapter citySenderAdapter;
    GeneralAdapter districtSenderAdapter;
    GeneralAdapter stateSenderAdapter;
    GeneralAdapter countrySenderAdapter;
    GeneralAdapter zoneSenderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courier);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        db = new DBHelper(this);
        myPreferences = new MyPreferences(this);

        try {
            GlobalElements.editTextAllCaps(AddCourierActivity.this, mainLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            city_data = db.getCity();
            cityAdapter = new GeneralAdapter(AddCourierActivity.this, city_data);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setHint("Select City");

            city_data = db.getCity();
            citySenderAdapter = new GeneralAdapter(AddCourierActivity.this, city_data);
            citySenderSpinner.setAdapter(cityAdapter);
            citySenderSpinner.setHint("Select City");

            district_data = db.getDistrict();
            districtAdapter = new GeneralAdapter(AddCourierActivity.this, district_data);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setHint("Select District");

            district_data = db.getDistrict();
            districtSenderAdapter = new GeneralAdapter(AddCourierActivity.this, district_data);
            districtSenderSpinner.setAdapter(districtAdapter);
            districtSenderSpinner.setHint("Select District");

            state_data = db.getState();
            stateAdapter = new GeneralAdapter(AddCourierActivity.this, state_data);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setHint("Select State");

            state_data = db.getState();
            stateSenderAdapter = new GeneralAdapter(AddCourierActivity.this, state_data);
            stateSenderSpinner.setAdapter(stateAdapter);
            stateSenderSpinner.setHint("Select State");

            country_data = db.getCountry();
            countryAdapter = new GeneralAdapter(AddCourierActivity.this, country_data);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setHint("Select Country");

            country_data = db.getCountry();
            countrySenderAdapter = new GeneralAdapter(AddCourierActivity.this, country_data);
            countrySenderSpinner.setAdapter(countryAdapter);
            countrySenderSpinner.setHint("Select Country");

            zone_data = db.getZone();
            zoneAdapter = new GeneralAdapter(AddCourierActivity.this, zone_data);
            zoneSpinner.setAdapter(zoneAdapter);
            zoneSpinner.setHint("Select Zone");

            zone_data = db.getZone();
            zoneSenderAdapter = new GeneralAdapter(AddCourierActivity.this, zone_data);
            zoneSenderSpinner.setAdapter(zoneAdapter);
            zoneSenderSpinner.setHint("Select Zone");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            courierType = intent.getStringExtra("courier_type");  // courierType = 1 ready to ship and courierType = 2 courier list
            if (courierType.equals("1")) {
                radioSender.setChecked(true);
                radioReciver.setChecked(false);
            } else {
                radioSender.setChecked(false);
                radioReciver.setChecked(true);
            }
            if (radioSender.isChecked()) {
                changeLayout.removeViewAt(0);
                changeLayout.removeViewAt(0);
                changeLayout.addView(senderCard, 0);
                changeLayout.addView(reciverCard, 1);
                senderLayout.setVisibility(View.GONE);
                reciverLayout.setVisibility(View.VISIBLE);
                senderPersonLayout.setVisibility(View.VISIBLE);
                receiverPersonLayout.setVisibility(View.GONE);
            } else {
                changeLayout.removeViewAt(0);
                changeLayout.removeViewAt(0);
                changeLayout.addView(reciverCard, 0);
                changeLayout.addView(senderCard, 1);
                senderLayout.setVisibility(View.VISIBLE);
                reciverLayout.setVisibility(View.GONE);
                senderPersonLayout.setVisibility(View.GONE);
                receiverPersonLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        areaAdapter = new SearchAreaAdapter(AddCourierActivity.this);
        inputReceiverArea.setAdapter(areaAdapter);

        inputReceiverArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    inputReceiverPin.setText("" + areaAdapter.suggestions.get(arg2).getPincode());
                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getCity_id())) {
                            cityReceiverId = city_data.get(i).getId();
                            citySpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getDistrict_id())) {
                            districtReceiverId = district_data.get(i).getId();
                            districtSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getState_id())) {
                            stateReceiverId = state_data.get(i).getId();
                            stateSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getCountry_id())) {
                            countryReceiverId = country_data.get(i).getId();
                            countrySpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < zone_data.size(); i++) {
                        if (zone_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getZone_id())) {
                            zoneReceiverId = zone_data.get(i).getId();
                            zoneSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    InputMethodManager imm = (InputMethodManager) AddCourierActivity.this.getSystemService(AddCourierActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputReceiverArea.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        areaSenderAdapter = new SearchAreaAdapter(AddCourierActivity.this);
        inputSenderArea.setAdapter(areaSenderAdapter);

        inputSenderArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    inputSenderPin.setText("" + areaSenderAdapter.suggestions.get(arg2).getPincode());
                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(areaSenderAdapter.suggestions.get(arg2).getCity_id())) {
                            cityId = city_data.get(i).getId();
                            citySenderSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(areaSenderAdapter.suggestions.get(arg2).getDistrict_id())) {
                            districtId = district_data.get(i).getId();
                            districtSenderSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(areaSenderAdapter.suggestions.get(arg2).getState_id())) {
                            stateId = state_data.get(i).getId();
                            stateSenderSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(areaSenderAdapter.suggestions.get(arg2).getCountry_id())) {
                            countryId = country_data.get(i).getId();
                            countrySenderSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < zone_data.size(); i++) {
                        if (zone_data.get(i).getId().equals(areaSenderAdapter.suggestions.get(arg2).getZone_id())) {
                            zoneId = zone_data.get(i).getId();
                            zoneSenderSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) AddCourierActivity.this.getSystemService(AddCourierActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSenderArea.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        inputDeliverdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDatePicker = new CustomDatePicker(AddCourierActivity.this);
                customerDatePicker.setToDate(customerDatePicker.min, inputDeliverdDate, "");
            }
        });


        courierCompanyAdapter = new SearchReciverCompanyAdapter(AddCourierActivity.this);
        inputCourierCompanyName.setAdapter(courierCompanyAdapter);

        /* todo search  sender company done  */
        try {
            senderCompanyAdapter = new SearchReciverCompanyAdapter(AddCourierActivity.this);
            inputSenderCompany.setAdapter(senderCompanyAdapter);
            inputSenderCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    try {
                        inputSenderAddress.setText("" + senderCompanyAdapter.suggestions.get(arg2).getAddress());
                        inputSenderPin.setText("" + senderCompanyAdapter.suggestions.get(arg2).getPincode());
                        inputSenderMobile.setText("" + senderCompanyAdapter.suggestions.get(arg2).getMobile());
                        inputSenderArea.setText("" + senderCompanyAdapter.suggestions.get(arg2).getArea());

                        for (int i = 0; i < city_data.size(); i++) {
                            if (city_data.get(i).getId().equals(senderCompanyAdapter.suggestions.get(arg2).getCity())) {
                                cityId = city_data.get(i).getId();
                                citySenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < district_data.size(); i++) {
                            if (district_data.get(i).getId().equals(senderCompanyAdapter.suggestions.get(arg2).getDistrict())) {
                                districtId = district_data.get(i).getId();
                                districtSenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < state_data.size(); i++) {
                            if (state_data.get(i).getId().equals(senderCompanyAdapter.suggestions.get(arg2).getState())) {
                                stateId = state_data.get(i).getId();
                                stateSenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < country_data.size(); i++) {
                            if (country_data.get(i).getId().equals(senderCompanyAdapter.suggestions.get(arg2).getCountry())) {
                                countryId = country_data.get(i).getId();
                                countrySenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }
                        for (int i = 0; i < zone_data.size(); i++) {
                            if (zone_data.get(i).getId().equals(senderCompanyAdapter.suggestions.get(arg2).getZone())) {
                                zoneId = zone_data.get(i).getId();
                                zoneSenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        InputMethodManager imm = (InputMethodManager) AddCourierActivity.this.getSystemService(AddCourierActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputSenderCompany.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* todo sender person done */
        try {
            senderPersonAdapter = new SearchReciverPersonAdapter(AddCourierActivity.this);
            inputSenderPerson.setAdapter(senderPersonAdapter);
            inputSenderPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long l) {
                    System.out.print("");
                    if (!radioSender.isChecked()) { // receiver type
                        inputSenderAddress.setText("" + senderPersonAdapter.suggestions.get(arg2).getAddress());
                        inputSenderPin.setText("" + senderPersonAdapter.suggestions.get(arg2).getPincode());
                        inputSenderMobile.setText("" + senderPersonAdapter.suggestions.get(arg2).getMobile());
                        inputSenderArea.setText("" + senderPersonAdapter.suggestions.get(arg2).getArea());

                        for (int i = 0; i < city_data.size(); i++) {
                            if (city_data.get(i).getId().equals(senderPersonAdapter.suggestions.get(arg2).getCity())) {
                                cityId = city_data.get(i).getId();
                                citySenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < district_data.size(); i++) {
                            if (district_data.get(i).getId().equals(senderPersonAdapter.suggestions.get(arg2).getDistrict())) {
                                districtId = district_data.get(i).getId();
                                districtSenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < state_data.size(); i++) {
                            if (state_data.get(i).getId().equals(senderPersonAdapter.suggestions.get(arg2).getState())) {
                                stateId = state_data.get(i).getId();
                                stateSenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }

                        for (int i = 0; i < country_data.size(); i++) {
                            if (country_data.get(i).getId().equals(senderPersonAdapter.suggestions.get(arg2).getCountry())) {
                                countryId = country_data.get(i).getId();
                                countrySenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }
                        for (int i = 0; i < zone_data.size(); i++) {
                            if (zone_data.get(i).getId().equals(senderPersonAdapter.suggestions.get(arg2).getZone())) {
                                zoneId = zone_data.get(i).getId();
                                zoneSenderSpinner.setSelection(i + 1);
                                break;
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* todo sender persion done */
        try {
            senderassignorPersonAdapter = new SearchReciverPersonAdapter(AddCourierActivity.this);
            inputSenderPersonName.setAdapter(senderassignorPersonAdapter);
            inputSenderPersonName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    try {
                        inputSenderPersonMobile.setText("" + senderassignorPersonAdapter.suggestions.get(arg2).getMobile());
                        InputMethodManager imm = (InputMethodManager) AddCourierActivity.this.getSystemService(AddCourierActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputSenderPersonName.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* TODO SEARCH RECEIVER DERAIL */
        /* todo search reciver company detail done */

        try {
            reciverCompanyAdapter = new SearchReciverCompanyAdapter(AddCourierActivity.this);
            inputReciverCompany.setAdapter(reciverCompanyAdapter);
            inputReciverCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    try {
                        inputReceiverAddress.setText("" + reciverCompanyAdapter.suggestions.get(arg2).getAddress());
                        inputReceiverPin.setText("" + reciverCompanyAdapter.suggestions.get(arg2).getPincode());
                        inputReceiverMobile.setText("" + reciverCompanyAdapter.suggestions.get(arg2).getMobile());
                        inputReceiverArea.setText("" + reciverCompanyAdapter.suggestions.get(arg2).getArea());

                        try {
                            for (int i = 0; i < city_data.size(); i++) {
                                if (city_data.get(i).getId().equals(reciverCompanyAdapter.suggestions.get(arg2).getCity())) {
                                    cityReceiverId = city_data.get(i).getId();
                                    citySpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < district_data.size(); i++) {
                                if (district_data.get(i).getId().equals(reciverCompanyAdapter.suggestions.get(arg2).getDistrict())) {
                                    districtReceiverId = district_data.get(i).getId();
                                    districtSpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < state_data.size(); i++) {
                                if (state_data.get(i).getId().equals(reciverCompanyAdapter.suggestions.get(arg2).getState())) {
                                    stateReceiverId = state_data.get(i).getId();
                                    stateSpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < country_data.size(); i++) {
                                if (country_data.get(i).getId().equals(reciverCompanyAdapter.suggestions.get(arg2).getCountry())) {
                                    countryReceiverId = country_data.get(i).getId();
                                    countrySpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < zone_data.size(); i++) {
                                if (zone_data.get(i).getId().equals(reciverCompanyAdapter.suggestions.get(arg2).getZone())) {
                                    zoneReceiverId = zone_data.get(i).getId();
                                    zoneSpinner.setSelection(i + 1);
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        InputMethodManager imm = (InputMethodManager) AddCourierActivity.this.getSystemService(AddCourierActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputReciverPerson.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* todo search receiver persion detail done */
        try {
            reciverPersonAdapter = new SearchReciverPersonAdapter(AddCourierActivity.this);
            inputReciverPerson.setAdapter(reciverPersonAdapter);
            inputReciverPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    try {
                        inputReceiverAddress.setText("" + reciverPersonAdapter.suggestions.get(arg2).getAddress());
                        inputReceiverPin.setText("" + reciverPersonAdapter.suggestions.get(arg2).getPincode());
                        inputReceiverMobile.setText("" + reciverPersonAdapter.suggestions.get(arg2).getMobile());
                        inputReceiverArea.setText("" + reciverPersonAdapter.suggestions.get(arg2).getArea());

                        try {
                            for (int i = 0; i < city_data.size(); i++) {
                                if (city_data.get(i).getId().equals(reciverPersonAdapter.suggestions.get(arg2).getCity())) {
                                    cityReceiverId = city_data.get(i).getId();
                                    citySpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < district_data.size(); i++) {
                                if (district_data.get(i).getId().equals(reciverPersonAdapter.suggestions.get(arg2).getDistrict())) {
                                    districtReceiverId = district_data.get(i).getId();
                                    districtSpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < state_data.size(); i++) {
                                if (state_data.get(i).getId().equals(reciverPersonAdapter.suggestions.get(arg2).getState())) {
                                    stateReceiverId = state_data.get(i).getId();
                                    stateSpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < country_data.size(); i++) {
                                if (country_data.get(i).getId().equals(reciverPersonAdapter.suggestions.get(arg2).getCountry())) {
                                    countryReceiverId = country_data.get(i).getId();
                                    countrySpinner.setSelection(i + 1);
                                    break;
                                }
                            }
                            for (int i = 0; i < zone_data.size(); i++) {
                                if (zone_data.get(i).getId().equals(reciverPersonAdapter.suggestions.get(arg2).getZone())) {
                                    zoneReceiverId = zone_data.get(i).getId();
                                    zoneSpinner.setSelection(i + 1);
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        InputMethodManager imm = (InputMethodManager) AddCourierActivity.this.getSystemService(AddCourierActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputReciverPerson.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* todo assignor receiver persion search */
        try {
            receiverassignorPersonAdapter = new SearchReciverPersonAdapter(AddCourierActivity.this);
            inputReceiverPersonName.setAdapter(receiverassignorPersonAdapter);
            inputReceiverPersonName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    try {
                        inputReceiverPersonMobile.setText("" + receiverassignorPersonAdapter.suggestions.get(arg2).getMobile());
                        InputMethodManager imm = (InputMethodManager) AddCourierActivity.this.getSystemService(AddCourierActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputReceiverPersonName.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Type();
        DeliveryStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_courier, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (GlobalElements.isConnectingToInternet(AddCourierActivity.this)) {

                    RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                    if (rb.getText().toString().equals("Sender")) {
                        courierType = "1";
                        assigned_person_name = inputSenderPersonName.getText().toString();
                        assigned_person_mobile = inputSenderPersonMobile.getText().toString();
                    } else if (rb.getText().toString().equals("Receiver")) {
                        courierType = "2";
                        assigned_person_name = inputReceiverPersonName.getText().toString();
                        assigned_person_mobile = inputReceiverPersonMobile.getText().toString();
                    }

                    if (typeId.equals("")) {
                        percelType.setError("select Type");
                    } else {
                        addCourier();
                    }
                } else {
                    GlobalElements.showDialog(AddCourierActivity.this);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Type() {
        try {
            GeneralModel da = new GeneralModel();
            da.setId("1");
            da.setName("Cover");
            percel_type.add(da);

            da = new GeneralModel();
            da.setId("2");
            da.setName("Parcel");
            percel_type.add(da);

            percelTypeAdapter = new GeneralAdapter(AddCourierActivity.this, percel_type);
            percelType.setAdapter(percelTypeAdapter);
            percelType.setHint("Select Type");
            SpinnerInteractionListener_type listener_1 = new SpinnerInteractionListener_type();
            percelType.setOnTouchListener(listener_1);
            percelType.setOnItemSelectedListener(listener_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeliveryStatus() {
        try {
            GeneralModel da = new GeneralModel();
            da.setId("1");
            da.setName("Shipped");
            deliverdStatus.add(da);

            da = new GeneralModel();
            da.setId("2");
            da.setName("Deliverd");
            deliverdStatus.add(da);

            deliverd_adapter = new GeneralAdapter(AddCourierActivity.this, deliverdStatus);
            deliverStatusSpinner.setAdapter(deliverd_adapter);
            deliverStatusSpinner.setHint("Select Delivery Status");
            SpinnerInteractionListener_delivery listener_1 = new SpinnerInteractionListener_delivery();
            deliverStatusSpinner.setOnTouchListener(listener_1);
            deliverStatusSpinner.setOnItemSelectedListener(listener_1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SpinnerInteractionListener_type implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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

                    typeId = percel_type.get(position).getId();
                    percelType.setError(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    typeId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_delivery implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    deliveryId = deliverdStatus.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    deliveryId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    private void addCourier() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddCourierActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.addCourier(myPreferences.getPreferences(MyPreferences.id), assigned_person_name, assigned_person_mobile, inputSenderCompany.getText().toString(), inputSenderPerson.getText().toString(), inputSenderMobile.getText().toString(), inputSenderAddress.getText().toString(), inputSenderArea.getText().toString(), cityId, districtId,
                    stateId, countryId, inputSenderPin.getText().toString(), zoneId, inputReciverCompany.getText().toString(), inputReciverPerson.getText().toString(),
                    inputReceiverMobile.getText().toString(), inputReceiverAddress.getText().toString(), inputReceiverArea.getText().toString(), cityReceiverId, districtReceiverId, stateReceiverId, countryReceiverId, inputReceiverPin.getText().toString(), zoneReceiverId,
                    typeId, inputDescription.getText().toString(), inputWeight.getText().toString(), inputCost.getText().toString(), inputCourierCompanyName.getText().toString(), inputPickupPerson.getText().toString(), inputDeliverdDate.getText().toString(), inputTrackingNo.getText().toString(), deliveryId, courierType
                    , inputWeightGm.getText().toString(), inputWeight.getText().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            Intent intent = new Intent();
                            intent.putExtra("courierType",courierType);
                            setResult(10, intent);
                            finish();
                        } else {
                            Toaster.show(AddCourierActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
