package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.adapter.SearchAreaAdapter;
import com.innovent.erp.custom.ImageInputHelper;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CompanyContactHistoryModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactCompanyActivity extends AppCompatActivity implements ImageInputHelper.ImageActionListener {

    @BindView(R.id.contact_label)
    MaterialSpinner contactLabel;
    @BindView(R.id.contact_tag)
    MaterialSpinner contactTag;
    @BindView(R.id.input_company_name)
    EditText inputCompanyName;
    @BindView(R.id.input_layout_company_name)
    TextInputLayout inputLayoutCompanyName;
    @BindView(R.id.input_off_address)
    EditText inputOffAddress;
    @BindView(R.id.input_layout_off_address)
    TextInputLayout inputLayoutOffAddress;
    @BindView(R.id.input_city)
    AutoCompleteTextView inputCity;
    @BindView(R.id.input_layout_city)
    TextInputLayout inputLayoutCity;
    @BindView(R.id.input_state)
    AutoCompleteTextView inputState;
    @BindView(R.id.input_layout_state)
    TextInputLayout inputLayoutState;
    @BindView(R.id.input_country)
    AutoCompleteTextView inputCountry;
    @BindView(R.id.input_layout_country)
    TextInputLayout inputLayoutCountry;
    @BindView(R.id.input_pin)
    EditText inputPin;
    @BindView(R.id.input_layout_pin)
    TextInputLayout inputLayoutPin;
    @BindView(R.id.input_off_phone)
    EditText inputOffPhone;
    @BindView(R.id.input_layout_off_phone)
    TextInputLayout inputLayoutOffPhone;
    @BindView(R.id.input_mobile)
    EditText inputMobile;
    @BindView(R.id.input_layout_mobile)
    TextInputLayout inputLayoutMobile;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_website)
    EditText inputWebsite;
    @BindView(R.id.input_layout_website)
    TextInputLayout inputLayoutWebsite;
    @BindView(R.id.input_where_address)
    EditText inputWhereAddress;
    @BindView(R.id.input_layout_where_address)
    TextInputLayout inputLayoutWhereAddress;
    @BindView(R.id.input_where_city)
    AutoCompleteTextView inputWhereCity;
    @BindView(R.id.input_layout_where_city)
    TextInputLayout inputLayoutWhereCity;
    @BindView(R.id.input_where_state)
    AutoCompleteTextView inputWhereState;
    @BindView(R.id.input_layout_where_state)
    TextInputLayout inputLayoutWhereState;
    @BindView(R.id.input_where_country)
    AutoCompleteTextView inputWhereCountry;
    @BindView(R.id.input_layout_where_country)
    TextInputLayout inputLayoutWhereCountry;
    @BindView(R.id.input_where_pin)
    EditText inputWherePin;
    @BindView(R.id.input_layout_where_pin)
    TextInputLayout inputLayoutWherePin;
    @BindView(R.id.input_where_mobile)
    EditText inputWhereMobile;
    @BindView(R.id.input_layout_where_mobile)
    TextInputLayout inputLayoutWhereMobile;
    @BindView(R.id.input_where_email)
    EditText inputWhereEmail;
    @BindView(R.id.input_layout_where_email)
    TextInputLayout inputLayoutWhereEmail;
    @BindView(R.id.input_gst)
    EditText inputGst;
    @BindView(R.id.input_layout_gst)
    TextInputLayout inputLayoutGst;
    @BindView(R.id.input_pan)
    EditText inputPan;
    @BindView(R.id.input_layout_pan)
    TextInputLayout inputLayoutPan;
    @BindView(R.id.courier_address)
    CheckBox courierAddress;
    @BindView(R.id.print_lable)
    CheckBox printLable;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.browse)
    TextView browse;
    @BindView(R.id.input_where_offmobile)
    EditText inputWhereOffmobile;
    @BindView(R.id.input_layout_where_offmobile)
    TextInputLayout inputLayoutWhereOffmobile;
    @BindView(R.id.addLabel)
    ImageView addLabel;
    @BindView(R.id.addTag)
    ImageView addTag;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.same_address)
    CheckBox sameAddress;
    @BindView(R.id.input_bank_holder_name)
    EditText inputBankHolderName;
    @BindView(R.id.input_layout_bank_holder_name)
    TextInputLayout inputLayoutBankHolderName;
    @BindView(R.id.input_bank_name)
    EditText inputBankName;
    @BindView(R.id.input_layout_bank_name)
    TextInputLayout inputLayoutBankName;
    @BindView(R.id.input_bank_account_no)
    EditText inputBankAccountNo;
    @BindView(R.id.input_layout_bank_account_no)
    TextInputLayout inputLayoutBankAccountNo;
    @BindView(R.id.input_bank_ifsc_code)
    EditText inputBankIfscCode;
    @BindView(R.id.input_layout_bank_ifsc_code)
    TextInputLayout inputLayoutBankIfscCode;
    @BindView(R.id.courier_address_wherehouse)
    CheckBox courierAddressWherehouse;
    @BindView(R.id.input_zone)
    AutoCompleteTextView inputZone;
    @BindView(R.id.input_layout_zone)
    TextInputLayout inputLayoutZone;
    @BindView(R.id.input_area)
    AutoCompleteTextView inputArea;
    @BindView(R.id.input_layout_area)
    TextInputLayout inputLayoutArea;
    @BindView(R.id.input_where_zone)
    AutoCompleteTextView inputWhereZone;
    @BindView(R.id.input_layout_where_zone)
    TextInputLayout inputLayoutWhereZone;
    @BindView(R.id.input_where_area)
    AutoCompleteTextView inputWhereArea;
    @BindView(R.id.input_layout_where_area)
    TextInputLayout inputLayoutWhereArea;
    @BindView(R.id.input_district)
    AutoCompleteTextView inputDistrict;
    @BindView(R.id.input_layout_district)
    TextInputLayout inputLayoutDistrict;
    @BindView(R.id.input_where_district)
    AutoCompleteTextView inputWhereDistrict;
    @BindView(R.id.input_layout_where_district)
    TextInputLayout inputLayoutWhereDistrict;

    @BindView(R.id.city_spinner)
    MaterialSpinner citySpinner;
    @BindView(R.id.district_spinner)
    MaterialSpinner districtSpinner;
    @BindView(R.id.state_spinner)
    MaterialSpinner stateSpinner;
    @BindView(R.id.country_spinner)
    MaterialSpinner countrySpinner;
    @BindView(R.id.pincode_spinner)
    MaterialSpinner pincodeSpinner;
    @BindView(R.id.zone_spinner)
    MaterialSpinner zoneSpinner;

    @BindView(R.id.city_where_spinner)
    MaterialSpinner cityWhereSpinner;
    @BindView(R.id.district_where_spinner)
    MaterialSpinner districtWhereSpinner;
    @BindView(R.id.state_where_spinner)
    MaterialSpinner stateWhereSpinner;
    @BindView(R.id.country_where_spinner)
    MaterialSpinner countryWhereSpinner;
    @BindView(R.id.pincode_where_spinner)
    MaterialSpinner pincodeWhereSpinner;
    @BindView(R.id.zone_where_spinner)
    MaterialSpinner zoneWhereSpinner;

    ArrayList<GeneralModel> tag_data = new ArrayList<>();
    GeneralAdapter tag_adapter;
    ArrayList<GeneralModel> label_data = new ArrayList<>();
    GeneralAdapter label_adapter;

    SearchAreaAdapter areaAdapter;
    SearchAreaAdapter areawhereAdapter;

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

    GeneralAdapter cityWhereAdapter;
    GeneralAdapter districtWhereAdapter;
    GeneralAdapter stateWhereAdapter;
    GeneralAdapter countryWhereAdapter;
    GeneralAdapter zoneWhereAdapter;


    String cityId, districtId, stateId, countryId = "",  zoneId = "",cityWhereId, districtWhereId, stateWhereId, countryWhereId = "",  zoneWhereId = ""
            , lableId = "", tagId = "", courierChk = "0", sameAsChk = "0", courierWhereChk = "0", printChk = "0";
    String contactUpdateId = "";
    DBHelper db;
    MyPreferences myPreferences;
    File UploadLogo = null;


    private ImageInputHelper imageInputHelper;
    String insert_update_type = "";
    CompanyContactHistoryModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_company);
        ButterKnife.bind(this);
        db = new DBHelper(AddContactCompanyActivity.this);
        myPreferences = new MyPreferences(AddContactCompanyActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageInputHelper = new ImageInputHelper(AddContactCompanyActivity.this);
        imageInputHelper.setImageActionListener(AddContactCompanyActivity.this);

        GlobalElements.editTextAllCaps(AddContactCompanyActivity.this, mainLayout);

        try {
            city_data = db.getCity();
            cityAdapter = new GeneralAdapter(AddContactCompanyActivity.this, city_data);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setHint("Select City");

            cityWhereAdapter = new GeneralAdapter(AddContactCompanyActivity.this, city_data);
            cityWhereSpinner.setAdapter(cityWhereAdapter);
            cityWhereSpinner.setHint("Select City");

            district_data = db.getDistrict();
            districtAdapter = new GeneralAdapter(AddContactCompanyActivity.this, district_data);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setHint("Select District");

            districtWhereAdapter = new GeneralAdapter(AddContactCompanyActivity.this, district_data);
            districtWhereSpinner.setAdapter(districtWhereAdapter);
            districtWhereSpinner.setHint("Select District");

            state_data = db.getState();
            stateAdapter = new GeneralAdapter(AddContactCompanyActivity.this, state_data);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setHint("Select State");

            stateWhereAdapter = new GeneralAdapter(AddContactCompanyActivity.this, state_data);
            stateWhereSpinner.setAdapter(stateWhereAdapter);
            stateWhereSpinner.setHint("Select State");

            country_data = db.getCountry();
            countryAdapter = new GeneralAdapter(AddContactCompanyActivity.this, country_data);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setHint("Select Country");

            countryWhereAdapter = new GeneralAdapter(AddContactCompanyActivity.this, country_data);
            countryWhereSpinner.setAdapter(countryWhereAdapter);
            countryWhereSpinner.setHint("Select Country");

            zone_data = db.getZone();
            zoneAdapter = new GeneralAdapter(AddContactCompanyActivity.this, zone_data);
            zoneSpinner.setAdapter(zoneAdapter);
            zoneSpinner.setHint("Select Zone");

            zoneWhereAdapter = new GeneralAdapter(AddContactCompanyActivity.this, zone_data);
            zoneWhereSpinner.setAdapter(zoneWhereAdapter);
            zoneWhereSpinner.setHint("Select Zone");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_type = bundle.getString("type");
            if (insert_update_type == null || insert_update_type.equals("0")) {
                insert_update_type = "0";
                getSupportActionBar().setTitle("Add new Contact");
                if (myPreferences.getPreferences(MyPreferences.label_insert).equals("1")) {
                    addLabel.setVisibility(View.VISIBLE);
                } else {
                    addLabel.setVisibility(View.GONE);
                }

                if (myPreferences.getPreferences(MyPreferences.tag_insert).equals("1")) {
                    addTag.setVisibility(View.VISIBLE);
                } else {
                    addTag.setVisibility(View.GONE);
                }
            } else {
                getSupportActionBar().setTitle("Update Contact");
                data = (CompanyContactHistoryModel) bundle.getSerializable("data");
                addLabel.setVisibility(View.GONE);
                addTag.setVisibility(View.GONE);
                contactUpdateId = data.getId();
                inputCompanyName.setText("" + data.getCompany_name());
                inputOffAddress.setText("" + data.getCompany_address());
                inputCountry.setText("" + data.getCompany_country());
                inputState.setText("" + data.getCompany_state());
                inputCity.setText("" + data.getCompany_city());
                inputArea.setText("" + data.getCompany_area());
                inputZone.setText("" + data.getCompany_zone());
                inputDistrict.setText("" + data.getCompany_district());
                inputPin.setText("" + data.getCompany_pincode());
                inputOffPhone.setText("" + data.getCompany_office_phone());
                inputMobile.setText("" + data.getCompany_mobile());
                inputEmail.setText("" + data.getCompany_email());
                inputWebsite.setText("" + data.getCompany_website());
                inputWhereAddress.setText("" + data.getCompany_wharehouse_address());
                inputWhereCountry.setText("" + data.getCompany_wharehouse_country());
                inputWhereState.setText("" + data.getCompany_wharehouse_state());
                inputWhereCity.setText("" + data.getCompany_wharehouse_city());
                inputWhereArea.setText("" + data.getCompany_wharehouse_area());
                inputWhereZone.setText("" + data.getCompany_wharehouse_zone());
                inputWhereDistrict.setText("" + data.getCompany_wharehouse_district());
                inputWherePin.setText("" + data.getCompany_wharehouse_pincode());
                inputWhereOffmobile.setText("" + data.getCompany_wharehouse_phone());
                inputWhereMobile.setText("" + data.getCompany_wharehouse_mobile());
                inputWhereEmail.setText("" + data.getCompany_wharehouse_email());
                inputGst.setText("" + data.getCompany_gst_no());
                inputPan.setText("" + data.getCompany_pan_no());
                inputBankName.setText("" + data.getCompany_bank_name());
                inputBankHolderName.setText("" + data.getCompany_account_name());
                inputBankAccountNo.setText("" + data.getCompany_bank_acc_no());
                inputBankIfscCode.setText("" + data.getCompany_bank_ifsc());

                try {
                    if (!data.getImage_path().equals("")) {
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        DisplayImageOptions options;
                        imageLoader.init(ImageLoaderConfiguration.createDefault(AddContactCompanyActivity.this));
                        options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .build();
                        imageLoader.displayImage(data.getImage_path(), img, options);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(data.getCompany_city())) {
                            cityId = city_data.get(i).getId();
                            citySpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(data.getCompany_district())) {
                            districtId = district_data.get(i).getId();
                            districtSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(data.getCompany_state())) {
                            stateId = state_data.get(i).getId();
                            stateSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(data.getCompany_country())) {
                            countryId = country_data.get(i).getId();
                            countrySpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < zone_data.size(); i++) {
                        if (zone_data.get(i).getId().equals(data.getCompany_zone())) {
                            zoneId = zone_data.get(i).getId();
                            zoneSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(data.getCompany_wharehouse_city())) {
                            cityWhereId = city_data.get(i).getId();
                            cityWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(data.getCompany_wharehouse_district())) {
                            districtWhereId = district_data.get(i).getId();
                            districtWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(data.getCompany_wharehouse_state())) {
                            stateWhereId = state_data.get(i).getId();
                            stateWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(data.getCompany_wharehouse_country())) {
                            countryWhereId = country_data.get(i).getId();
                            countryWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < zone_data.size(); i++) {
                        if (zone_data.get(i).getId().equals(data.getCompany_wharehouse_zone())) {
                            zoneWhereId = zone_data.get(i).getId();
                            zoneWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (data.getCourier_address().equals("1")) {
                    courierAddress.setChecked(true);
                } else {
                    courierAddress.setChecked(false);
                }

                if (data.getCourier_address_wherehouse().equals("1")) {
                    courierAddressWherehouse.setChecked(true);
                } else {
                    courierAddressWherehouse.setChecked(false);
                }

                if (data.getSame_as_company().equals("1")) {
                    sameAddress.setChecked(true);
                } else {
                    sameAddress.setChecked(false);
                }

                if (data.getPrint_label().equals("1")) {
                    printLable.setChecked(true);
                } else {
                    printLable.setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            insert_update_type = "0";
        }

        areaAdapter = new SearchAreaAdapter(AddContactCompanyActivity.this);
        inputArea.setAdapter(areaAdapter);

        areawhereAdapter = new SearchAreaAdapter(AddContactCompanyActivity.this);
        inputWhereArea.setAdapter(areawhereAdapter);

        inputArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    inputPin.setText(""+areaAdapter.suggestions.get(arg2).getPincode());
                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getCity_id())) {
                            cityId = city_data.get(i).getId();
                            citySpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getDistrict_id())) {
                            districtId = district_data.get(i).getId();
                            districtSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getState_id())) {
                            stateId = state_data.get(i).getId();
                            stateSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getCountry_id())) {
                            countryId = country_data.get(i).getId();
                            countrySpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < zone_data.size(); i++) {
                        if (zone_data.get(i).getId().equals(areaAdapter.suggestions.get(arg2).getZone_id())) {
                            zoneId = zone_data.get(i).getId();
                            zoneSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    InputMethodManager imm = (InputMethodManager) AddContactCompanyActivity.this.getSystemService(AddContactCompanyActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputArea.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        inputWhereArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    inputWherePin.setText(""+areawhereAdapter.suggestions.get(arg2).getPincode());

                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(areawhereAdapter.suggestions.get(arg2).getCity_id())) {
                            cityWhereId = city_data.get(i).getId();
                            cityWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(areawhereAdapter.suggestions.get(arg2).getDistrict_id())) {
                            districtWhereId = district_data.get(i).getId();
                            districtWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(areawhereAdapter.suggestions.get(arg2).getState_id())) {
                            stateWhereId = state_data.get(i).getId();
                            stateWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(areawhereAdapter.suggestions.get(arg2).getCountry_id())) {
                            countryWhereId = country_data.get(i).getId();
                            countryWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < zone_data.size(); i++) {
                        if (zone_data.get(i).getId().equals(areawhereAdapter.suggestions.get(arg2).getZone_id())) {
                            zoneWhereId = zone_data.get(i).getId();
                            zoneWhereSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    InputMethodManager imm = (InputMethodManager) AddContactCompanyActivity.this.getSystemService(AddContactCompanyActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputWhereArea.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lable("");
        tag("");
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    imageInputHelper.selectImageFromGallery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddContactCompanyActivity.this);
                    alertDialog2.setTitle("Add Label");
                    LayoutInflater inflater = AddContactCompanyActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.dialog_new_folder, null);
                    alertDialog2.setView(dialogView);

                    final EditText newFolderEdt = (EditText) dialogView.findViewById(R.id.new_folder_edt);

                    alertDialog2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    if (GlobalElements.isConnectingToInternet(AddContactCompanyActivity.this)) {
                                        if (newFolderEdt.getText().toString().equals("")) {
                                            Toaster.show(AddContactCompanyActivity.this, "Enter Label", false, Toaster.DANGER);
                                        } else {
                                            addLabel("" + newFolderEdt.getText().toString());
                                        }
                                    } else {
                                        GlobalElements.showDialog(AddContactCompanyActivity.this);
                                    }
                                }
                            });

                    alertDialog2.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog

                                }
                            });

                    buildInfosDialog = alertDialog2.create();
                    buildInfosDialog.show();
                    buildInfosDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(AddContactCompanyActivity.this, R.color.colorPrimary));
                    buildInfosDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(AddContactCompanyActivity.this, R.color.colorPrimary));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddContactCompanyActivity.this);
                    alertDialog2.setTitle("Add Tag");
                    LayoutInflater inflater = AddContactCompanyActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.dialog_new_folder, null);
                    alertDialog2.setView(dialogView);

                    final EditText newFolderEdt = (EditText) dialogView.findViewById(R.id.new_folder_edt);

                    alertDialog2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    if (GlobalElements.isConnectingToInternet(AddContactCompanyActivity.this)) {
                                        if (newFolderEdt.getText().toString().equals("")) {
                                            Toaster.show(AddContactCompanyActivity.this, "Enter Tag", false, Toaster.DANGER);
                                        } else {
                                            addTag("" + newFolderEdt.getText().toString());
                                        }
                                    } else {
                                        GlobalElements.showDialog(AddContactCompanyActivity.this);
                                    }
                                }
                            });

                    alertDialog2.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog

                                }
                            });

                    buildInfosDialog = alertDialog2.create();
                    buildInfosDialog.show();
                    buildInfosDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(AddContactCompanyActivity.this, R.color.colorPrimary));
                    buildInfosDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(AddContactCompanyActivity.this, R.color.colorPrimary));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sameAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        inputWhereAddress.setText("" + inputOffAddress.getText().toString());
                        inputWhereOffmobile.setText("" + inputOffPhone.getText().toString());
                        inputWhereMobile.setText("" + inputMobile.getText().toString());
                        inputWhereEmail.setText("" + inputEmail.getText().toString());
                        inputWherePin.setText("" + inputPin.getText().toString());

                        inputWhereMobile.requestFocus();
                        inputWhereArea.clearFocus();
                        inputWhereArea.setText("" + inputArea.getText().toString());
                        inputWhereArea.clearFocus();
                        InputMethodManager imm = (InputMethodManager) AddContactCompanyActivity.this.getSystemService(AddContactCompanyActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputWhereArea.getWindowToken(), 0);

                        try {
                            for (int i = 0; i < city_data.size(); i++) {
                                if (city_data.get(i).getId().equals(cityId)) {
                                    cityWhereId = city_data.get(i).getId();
                                    cityWhereSpinner.setSelection(i + 1);
                                    break;
                                }
                            }

                            for (int i = 0; i < district_data.size(); i++) {
                                if (district_data.get(i).getId().equals(districtId)) {
                                    districtWhereId = district_data.get(i).getId();
                                    districtWhereSpinner.setSelection(i + 1);
                                    break;
                                }
                            }

                            for (int i = 0; i < state_data.size(); i++) {
                                if (state_data.get(i).getId().equals(stateId)) {
                                    stateWhereId = state_data.get(i).getId();
                                    stateWhereSpinner.setSelection(i + 1);
                                    break;
                                }
                            }

                            for (int i = 0; i < country_data.size(); i++) {
                                if (country_data.get(i).getId().equals(countryId)) {
                                    countryWhereId = country_data.get(i).getId();
                                    countryWhereSpinner.setSelection(i + 1);
                                    break;
                                }
                            }

                            for (int i = 0; i < zone_data.size(); i++) {
                                if (zone_data.get(i).getId().equals(zoneId)) {
                                    zoneWhereId = zone_data.get(i).getId();
                                    zoneWhereSpinner.setSelection(i + 1);
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /*inputWherePin.setText("" + inputPin.getText().toString());
                        inputWhereZone.setText("" + inputZone.getText().toString());
                        inputWhereDistrict.setText("" + inputDistrict.getText().toString());
                        inputWhereCity.setText("" + inputCity.getText().toString());
                        inputWhereState.setText("" + inputState.getText().toString());
                        inputWhereCountry.setText("" + inputCountry.getText().toString());*/

                    } else {
                        inputWhereAddress.setText("");
                        inputWhereOffmobile.setText("");
                        inputWhereMobile.setText("");
                        inputWhereEmail.setText("");
                        inputWherePin.setText("");
                        inputWhereArea.setText("");

                        cityWhereId = "";
                        cityWhereSpinner.setSelection(0);
                        districtWhereId = "";
                        districtWhereSpinner.setSelection(0);
                        stateWhereId = "";
                        stateWhereSpinner.setSelection(0);
                        countryWhereId = "";
                        countryWhereSpinner.setSelection(0);
                        zoneWhereId = "";
                        zoneWhereSpinner.setSelection(0);
                        pincodeWhereSpinner.setSelection(0);

                        /*inputWhereCountry.setText("");
                        inputWhereState.setText("");
                        inputWhereCity.setText("");
                        inputWherePin.setText("");*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        courierAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    courierAddressWherehouse.setChecked(false);
                }
            }
        });

        courierAddressWherehouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    courierAddress.setChecked(false);
                }
            }
        });
    }

    public void lable(String type) {
        try {
            label_data.clear();
            if (!myPreferences.getPreferences(MyPreferences.Label).equals("")) {
                JSONArray labelArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Label));
                for (int i = 0; i < labelArray.length(); i++) {
                    JSONObject c = labelArray.getJSONObject(i);
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    label_data.add(da);
                }
            }
            label_adapter = new GeneralAdapter(AddContactCompanyActivity.this, label_data);
            contactLabel.setAdapter(label_adapter);
            contactLabel.setHint("Select Label");
            SpinnerInteractionListener_label listener_1 = new SpinnerInteractionListener_label();
            contactLabel.setOnTouchListener(listener_1);
            contactLabel.setOnItemSelectedListener(listener_1);

            if (type.equals("add")) {
                try {
                    lableId = label_data.get(label_data.size() - 1).getId();
                    contactLabel.setSelection(label_data.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (insert_update_type.equals("1")) {
                    for (int i = 0; i < label_data.size(); i++) {
                        if (label_data.get(i).getId().equals("" + data.getLabel_id())) {
                            lableId = label_data.get(i).getId();
                            contactLabel.setSelection(i + 1);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tag(String type) {
        try {
            tag_data.clear();
            if (!myPreferences.getPreferences(MyPreferences.Tag).equals("")) {
                JSONArray tagArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Tag));
                for (int i = 0; i < tagArray.length(); i++) {
                    JSONObject c = tagArray.getJSONObject(i);
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    tag_data.add(da);
                }
            }
            tag_adapter = new GeneralAdapter(AddContactCompanyActivity.this, tag_data);
            contactTag.setAdapter(tag_adapter);
            contactTag.setHint("Select Tag");
            SpinnerInteractionListener_tag listener_1 = new SpinnerInteractionListener_tag();
            contactTag.setOnTouchListener(listener_1);
            contactTag.setOnItemSelectedListener(listener_1);

            if (type.equals("add")) {
                try {
                    tagId = tag_data.get(tag_data.size() - 1).getId();
                    contactTag.setSelection(tag_data.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (insert_update_type.equals("1")) {
                    for (int i = 0; i < tag_data.size(); i++) {

                        if (tag_data.get(i).getId().equals("" + data.getTag_id())) {
                            tagId = tag_data.get(i).getId();
                            contactTag.setSelection(i + 1);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (lableId.equals("")) {
                    Toaster.show(AddContactCompanyActivity.this, "Select Label", false, Toaster.DANGER);
                } else if (tagId.equals("")) {
                    Toaster.show(AddContactCompanyActivity.this, "Select Tag", false, Toaster.DANGER);
                } else {

                    if (courierAddress.isChecked()) {
                        courierChk = "1";
                    } else {
                        courierChk = "0";
                    }

                    if (courierAddressWherehouse.isChecked()) {
                        courierWhereChk = "1";
                    } else {
                        courierWhereChk = "0";
                    }

                    if (sameAddress.isChecked()) {
                        sameAsChk = "1";
                    } else {
                        sameAsChk = "0";
                    }

                    if (printLable.isChecked()) {
                        printChk = "1";
                    } else {
                        printChk = "0";
                    }

                    if (GlobalElements.isConnectingToInternet(AddContactCompanyActivity.this)) {
                        if (insert_update_type.equals("0")) {
                            newContactAdd();
                        } else {
                            updateContactAdd();
                        }
                    } else {
                        GlobalElements.showDialog(AddContactCompanyActivity.this);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        imageInputHelper.requestCropImage(uri, 300, 300, 1, 1);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {

    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {
        try {
            UploadLogo = imageFile;
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }

    public class SpinnerInteractionListener_tag implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    tagId = tag_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    tagId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_label implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    lableId = label_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    lableId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    private void addTag(String tag) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddContactCompanyActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.addTag(tag);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            if (!myPreferences.getPreferences(MyPreferences.Tag).equals("")) {
                                JSONArray tagArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Tag));
                                JSONObject label = new JSONObject();
                                label.put("id", "" + result.getString("id"));
                                label.put("name", "" + result.getString("name"));
                                tagArray.put(label);
                                myPreferences.setPreferences(MyPreferences.Tag, tagArray.toString());
                                tag("add");
                            } else {
                                JSONArray tagArray = new JSONArray();
                                JSONObject label = new JSONObject();
                                label.put("id", "" + result.getString("id"));
                                label.put("name", "" + result.getString("name"));
                                tagArray.put(label);
                                myPreferences.setPreferences(MyPreferences.Tag, tagArray.toString());
                                tag("add");
                            }
                        } else {
                            Toaster.show(AddContactCompanyActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void addLabel(String label) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddContactCompanyActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.addLabel(label);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            if (!myPreferences.getPreferences(MyPreferences.Label).equals("")) {
                                JSONArray labelArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Label));
                                String j = labelArray.toString();
                                Log.d("", "" + j);
                                JSONObject label = new JSONObject();
                                label.put("id", "" + result.getString("id"));
                                label.put("name", "" + result.getString("name"));
                                labelArray.put(label);
                                myPreferences.setPreferences(MyPreferences.Label, labelArray.toString());
                                lable("add");
                            } else {
                                JSONArray labelArray = new JSONArray();
                                JSONObject label = new JSONObject();
                                label.put("id", "" + result.getString("id"));
                                label.put("name", "" + result.getString("name"));
                                labelArray.put(label);
                                myPreferences.setPreferences(MyPreferences.Label, labelArray.toString());
                                lable("add");
                            }
                        } else {
                            Toaster.show(AddContactCompanyActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void newContactAdd() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddContactCompanyActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);

            RequestBody requestfile_image;
            MultipartBody.Part body_image;

            Call<ResponseBody> call;
            if (UploadLogo == null) {
                call = request.addCompanyContact(myPreferences.getPreferences(MyPreferences.id), lableId, tagId, inputCompanyName.getText().toString(), inputOffAddress.getText().toString(), countryId,
                        stateId, cityId, zoneId, inputArea.getText().toString(), districtId, inputPin.getText().toString(), inputOffPhone.getText().toString(),
                        inputMobile.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), inputWhereAddress.getText().toString(),
                        countryWhereId, stateWhereId, cityWhereId, zoneWhereId, inputWhereArea.getText().toString(), districtWhereId, inputWherePin.getText().toString(), inputWhereOffmobile.getText().toString(),
                        inputWhereMobile.getText().toString(), inputWhereEmail.getText().toString(), inputGst.getText().toString(), inputPan.getText().toString(), courierChk, printChk, courierWhereChk, inputBankName.getText().toString(),
                        inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString(), inputBankHolderName.getText().toString(), sameAsChk);
            } else {
                requestfile_image = RequestBody.create(MediaType.parse("image/*"), UploadLogo);
                body_image = MultipartBody.Part.createFormData("company_logo", UploadLogo.getName(), requestfile_image);
                call = request.addCompanyContact(myPreferences.getPreferences(MyPreferences.id), lableId, tagId, inputCompanyName.getText().toString(), inputOffAddress.getText().toString(), countryId,
                        stateId, cityId, zoneId, inputArea.getText().toString(), districtId, inputPin.getText().toString(), inputOffPhone.getText().toString(),
                        inputMobile.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), inputWhereAddress.getText().toString(),
                        countryWhereId, stateWhereId, cityWhereId, zoneWhereId, inputWhereArea.getText().toString(), districtWhereId, inputWherePin.getText().toString(),
                        inputWhereOffmobile.getText().toString(), inputWhereMobile.getText().toString(), inputWhereEmail.getText().toString(), inputGst.getText().toString(), inputPan.getText().toString(), courierChk, printChk, courierWhereChk, inputBankName.getText().toString(),
                        inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString(), inputBankHolderName.getText().toString(), sameAsChk, body_image);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            Intent intent = new Intent();
                            setResult(11, intent);
                            finish();
                        } else {
                            Toaster.show(AddContactCompanyActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void updateContactAdd() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddContactCompanyActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            RequestBody requestfile_image;
            MultipartBody.Part body_image;

            Call<ResponseBody> call;
            if (UploadLogo == null) {
                call = request.updateCompanyContact(myPreferences.getPreferences(MyPreferences.id), contactUpdateId, lableId, tagId, inputCompanyName.getText().toString(), inputOffAddress.getText().toString(), countryId,
                        stateId, cityId, zoneId, inputArea.getText().toString(), districtId, inputPin.getText().toString(), inputOffPhone.getText().toString(),
                        inputMobile.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), inputWhereAddress.getText().toString(),
                        countryWhereId, stateWhereId, cityWhereId, zoneWhereId, inputWhereArea.getText().toString(), districtId, inputWherePin.getText().toString(), inputWhereOffmobile.getText().toString(),
                        inputWhereMobile.getText().toString(), inputWhereEmail.getText().toString(), inputGst.getText().toString(), inputPan.getText().toString(), courierChk, printChk, courierWhereChk, inputBankName.getText().toString(),
                        inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString(), inputBankHolderName.getText().toString(), sameAsChk);
            } else {
                requestfile_image = RequestBody.create(MediaType.parse("image/*"), UploadLogo);
                body_image = MultipartBody.Part.createFormData("company_logo", UploadLogo.getName(), requestfile_image);
                call = request.updateCompanyContact(myPreferences.getPreferences(MyPreferences.id), contactUpdateId, lableId, tagId, inputCompanyName.getText().toString(), inputOffAddress.getText().toString(), countryId,
                        stateId, cityId, zoneId, inputArea.getText().toString(), districtId, inputPin.getText().toString(), inputOffPhone.getText().toString(),
                        inputMobile.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), inputWhereAddress.getText().toString(),
                        countryWhereId, stateWhereId, cityWhereId, zoneWhereId, inputWhereArea.getText().toString(),districtId, inputWherePin.getText().toString(),
                        inputWhereOffmobile.getText().toString(), inputWhereMobile.getText().toString(), inputWhereEmail.getText().toString(), inputGst.getText().toString(), inputPan.getText().toString(), courierChk, printChk, courierWhereChk, inputBankName.getText().toString(),
                        inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString(), inputBankHolderName.getText().toString(), sameAsChk, body_image);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {

                            JSONObject c = json.getJSONObject("result");
                            data.setLabel_id("" + c.getString("label_id"));
                            data.setLabel_slug(c.getString("label_slug"));
                            data.setTag_id(c.getString("tag_id"));
                            data.setTag_slug(c.getString("tag_slug"));
                            data.setCompany_name("" + c.getString("company_name"));
                            data.setCompany_address("" + c.getString("company_address"));
                            data.setCompany_area("" + c.getString("company_area"));

                            data.setCompany_city("" + c.getString("company_city"));
                            data.setCompany_district("" + c.getString("company_district"));
                            data.setCompany_state("" + c.getString("company_state"));
                            data.setCompany_country("" + c.getString("company_country"));
                            data.setCompany_zone("" + c.getString("company_zone"));

                            data.setCompany_city_name("" + c.getString("company_city_name"));
                            data.setCompany_district_name("" + c.getString("company_district_name"));
                            data.setCompany_state_name("" + c.getString("company_state_name"));
                            data.setCompany_country_name("" + c.getString("company_country_name"));
                            data.setCompany_zone_name("" + c.getString("company_zone_name"));

                            data.setCompany_pincode("" + c.getString("company_pincode"));
                            data.setCompany_mobile("" + c.getString("company_mobile"));
                            data.setCompany_email("" + c.getString("company_email"));
                            data.setCompany_website("" + c.getString("company_website"));
                            data.setCompany_office_phone("" + c.getString("company_office_phone"));
                            data.setCompany_wharehouse_address("" + c.getString("company_wharehouse_address"));

                            data.setCompany_wharehouse_area("" + c.getString("company_wharehouse_area"));
                            data.setCompany_wharehouse_district("" + c.getString("company_wharehouse_district"));
                            data.setCompany_wharehouse_city("" + c.getString("company_wharehouse_city"));
                            data.setCompany_wharehouse_state("" + c.getString("company_wharehouse_state"));
                            data.setCompany_wharehouse_country("" + c.getString("company_wharehouse_country"));
                            data.setCompany_wharehouse_zone("" + c.getString("company_wharehouse_zone"));

                            data.setCompany_wharehouse_district_name("" + c.getString("company_wharehouse_district_name"));
                            data.setCompany_wharehouse_city_name("" + c.getString("company_wharehouse_city_name"));
                            data.setCompany_wharehouse_state_name("" + c.getString("company_wharehouse_state_name"));
                            data.setCompany_wharehouse_country_name("" + c.getString("company_wharehouse_country_name"));
                            data.setCompany_wharehouse_zone_name("" + c.getString("company_wharehouse_zone_name"));

                            data.setCompany_wharehouse_pincode("" + c.getString("company_wharehouse_pincode"));
                            data.setCompany_wharehouse_phone("" + c.getString("company_wharehouse_phone"));
                            data.setCompany_wharehouse_mobile("" + c.getString("company_wharehouse_mobile"));
                            data.setCompany_wharehouse_email("" + c.getString("company_wharehouse_email"));
                            data.setCompany_gst_no(c.getString("company_gst_no"));
                            data.setCompany_pan_no(c.getString("company_pan_no"));
                            data.setCourier_address(c.getString("courier_address_company"));
                            data.setPrint_label(c.getString("print_label"));
                            data.setImage_path(c.getString("company_logo"));
                            data.setCourier_address_wherehouse(c.getString("courier_address_wherehouse"));
                            data.setSame_as_company(c.getString("same_as_company"));
                            data.setCompany_bank_name(c.getString("company_bank_name"));
                            data.setCompany_account_name(c.getString("company_account_name"));
                            data.setCompany_bank_acc_no(c.getString("company_bank_acc_no"));
                            data.setCompany_bank_ifsc(c.getString("company_bank_ifsc"));
                            data.setSame_as_company(c.getString("same_as_company"));

                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", data);
                            intent.putExtras(bundle);
                            setResult(11, intent);
                            finish();
                        } else {
                            Toaster.show(AddContactCompanyActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
