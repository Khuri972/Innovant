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
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.ImageInputHelper;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.IndividualContactHistoryModel;
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

public class AddCompanyPersonActivity extends AppCompatActivity implements ImageInputHelper.ImageActionListener {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.browse)
    TextView browse;
    @BindView(R.id.contact_emplayee)
    MaterialSpinner contactEmplayee;
    @BindView(R.id.contact_tag)
    MaterialSpinner contactTag;
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @BindView(R.id.input_job)
    EditText inputJob;
    @BindView(R.id.input_layout_job)
    TextInputLayout inputLayoutJob;
    @BindView(R.id.input_home_address)
    EditText inputHomeAddress;
    @BindView(R.id.input_layout_home_address)
    TextInputLayout inputLayoutHomeAddress;
    @BindView(R.id.input_country)
    AutoCompleteTextView inputCountry;
    @BindView(R.id.input_layout_country)
    TextInputLayout inputLayoutCountry;
    @BindView(R.id.input_state)
    AutoCompleteTextView inputState;
    @BindView(R.id.input_layout_state)
    TextInputLayout inputLayoutState;
    @BindView(R.id.input_city)
    AutoCompleteTextView inputCity;
    @BindView(R.id.input_layout_city)
    TextInputLayout inputLayoutCity;
    @BindView(R.id.input_pin)
    EditText inputPin;
    @BindView(R.id.input_layout_pin)
    TextInputLayout inputLayoutPin;
    @BindView(R.id.input_home_phone)
    EditText inputHomePhone;
    @BindView(R.id.input_layout_home_phone)
    TextInputLayout inputLayoutHomePhone;
    @BindView(R.id.input_mobile)
    EditText inputMobile;
    @BindView(R.id.input_layout_mobile)
    TextInputLayout inputLayoutMobile;
    @BindView(R.id.input_whatsapp)
    EditText inputWhatsapp;
    @BindView(R.id.input_layout_whatsapp)
    TextInputLayout inputLayoutWhatsapp;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_website)
    EditText inputWebsite;
    @BindView(R.id.input_layout_website)
    TextInputLayout inputLayoutWebsite;
    @BindView(R.id.birthday)
    EditText birthday;
    @BindView(R.id.anniversary)
    EditText anniversary;
    @BindView(R.id.events)
    EditText events;
    @BindView(R.id.notes)
    EditText notes;
    @BindView(R.id.input_aadhar)
    EditText inputAadhar;
    @BindView(R.id.input_layout_aadhar)
    TextInputLayout inputLayoutAadhar;
    @BindView(R.id.input_pan)
    EditText inputPan;
    @BindView(R.id.input_layout_pan)
    TextInputLayout inputLayoutPan;
    @BindView(R.id.courier_address)
    CheckBox courierAddress;
    @BindView(R.id.print_lable)
    CheckBox printLable;


    @BindView(R.id.addEmployee)
    ImageView addEmployee;
    @BindView(R.id.addTag)
    ImageView addTag;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.input_middle_name)
    EditText inputMiddleName;
    @BindView(R.id.input_layout_middle_name)
    TextInputLayout inputLayoutMiddleName;
    @BindView(R.id.input_last_name)
    EditText inputLastName;
    @BindView(R.id.input_layout_last_name)
    TextInputLayout inputLayoutLastName;
    @BindView(R.id.same_address)
    CheckBox sameAddress;
    @BindView(R.id.input_off_address)
    EditText inputOffAddress;
    @BindView(R.id.input_layout_off_address)
    TextInputLayout inputLayoutOffAddress;
    @BindView(R.id.input_off_country)
    AutoCompleteTextView inputOffCountry;
    @BindView(R.id.input_layout_off_country)
    TextInputLayout inputLayoutOffCountry;
    @BindView(R.id.input_off_state)
    AutoCompleteTextView inputOffState;
    @BindView(R.id.input_layout_off_state)
    TextInputLayout inputLayoutOffState;
    @BindView(R.id.input_off_city)
    AutoCompleteTextView inputOffCity;
    @BindView(R.id.input_layout_off_city)
    TextInputLayout inputLayoutOffCity;
    @BindView(R.id.input_off_pin)
    EditText inputOffPin;
    @BindView(R.id.input_layout_off_pin)
    TextInputLayout inputLayoutOffPin;
    @BindView(R.id.courier_address_office)
    CheckBox courierAddressOffice;
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
    @BindView(R.id.event_notes)
    EditText eventNotes;
    @BindView(R.id.input_area)
    AutoCompleteTextView inputArea;
    @BindView(R.id.input_layout_area)
    TextInputLayout inputLayoutArea;
    @BindView(R.id.input_district)
    AutoCompleteTextView inputDistrict;
    @BindView(R.id.input_layout_district)
    TextInputLayout inputLayoutDistrict;
    @BindView(R.id.input_off_area)
    AutoCompleteTextView inputOffArea;
    @BindView(R.id.input_layout_off_area)
    TextInputLayout inputLayoutOffArea;
    @BindView(R.id.input_off_district)
    AutoCompleteTextView inputOffDistrict;
    @BindView(R.id.input_layout_off_district)
    TextInputLayout inputLayoutOffDistrict;
    @BindView(R.id.spinner_job)
    MaterialSpinner spinnerJob;
    @BindView(R.id.city_spinner)
    MaterialSpinner citySpinner;
    @BindView(R.id.district_spinner)
    MaterialSpinner districtSpinner;
    @BindView(R.id.state_spinner)
    MaterialSpinner stateSpinner;
    @BindView(R.id.country_spinner)
    MaterialSpinner countrySpinner;
    @BindView(R.id.city_off_spinner)
    MaterialSpinner cityOffSpinner;
    @BindView(R.id.district_off_spinner)
    MaterialSpinner districtOffSpinner;
    @BindView(R.id.state_off_spinner)
    MaterialSpinner stateOffSpinner;
    @BindView(R.id.country_off_spinner)
    MaterialSpinner countryOffSpinner;


    private ImageInputHelper imageInputHelper;
    DBHelper db;

    SearchAreaAdapter areaAdapter;
    SearchAreaAdapter areaOffAdapter;

    GeneralAdapter cityAdapter;

    GeneralAdapter stateAdapter;
    GeneralAdapter countryAdapter;
    GeneralAdapter districtAdapter;

    ArrayList<GeneralModel> city_data = new ArrayList<>();
    ArrayList<GeneralModel> district_data = new ArrayList<>();
    ArrayList<GeneralModel> state_data = new ArrayList<>();
    ArrayList<GeneralModel> country_data = new ArrayList<>();

    GeneralAdapter cityoffAdapter;
    GeneralAdapter districtoffAdapter;
    GeneralAdapter stateoffAdapter;
    GeneralAdapter countryoffAdapter;

    ArrayList<GeneralModel> tag_data = new ArrayList<>();
    ArrayList<GeneralModel> jobTitle_data = new ArrayList<>();

    ArrayList<GeneralModel> employee_data = new ArrayList<>();
    GeneralAdapter employee_adapter;
    GeneralAdapter tag_adapter;
    GeneralAdapter jobTitle_adapter;

    String cityId, districtId, stateId, countryId = "", cityoffId, districtoffId, stateoffId, countryoffId = "";
    String employeeId = "", tagId = "", JobId = "", courierChk = "0", sameAsChk = "0", printChk = "0", contactUpdateId = "";

    CustomDatePicker customerDatePicker;
    MyPreferences myPreferences;
    File UploadLogo = null;
    String insert_update_type = "",  company_id;
    IndividualContactHistoryModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company_person);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(AddCompanyPersonActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageInputHelper = new ImageInputHelper(AddCompanyPersonActivity.this);
        imageInputHelper.setImageActionListener(AddCompanyPersonActivity.this);
        db = new DBHelper(AddCompanyPersonActivity.this);

        try {
            GlobalElements.editTextAllCaps(AddCompanyPersonActivity.this, mainLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            city_data = db.getCity();
            cityAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, city_data);
            citySpinner.setAdapter(cityAdapter);
            citySpinner.setHint("Select City");

            cityoffAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, city_data);
            cityOffSpinner.setAdapter(cityoffAdapter);
            cityOffSpinner.setHint("Select City");

            district_data = db.getDistrict();
            districtAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, district_data);
            districtSpinner.setAdapter(districtAdapter);
            districtSpinner.setHint("Select District");

            districtoffAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, district_data);
            districtOffSpinner.setAdapter(districtoffAdapter);
            districtOffSpinner.setHint("Select District");

            state_data = db.getState();
            stateAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, state_data);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setHint("Select State");

            stateoffAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, state_data);
            stateOffSpinner.setAdapter(stateoffAdapter);
            stateOffSpinner.setHint("Select State");

            country_data = db.getCountry();
            countryAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, country_data);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setHint("Select Country");

            countryoffAdapter = new GeneralAdapter(AddCompanyPersonActivity.this, country_data);
            countryOffSpinner.setAdapter(countryoffAdapter);
            countryOffSpinner.setHint("Select Country");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            insert_update_type = bundle.getString("type");
            if (insert_update_type.equals("0")) {
                company_id = intent.getStringExtra("id");
                getSupportActionBar().setTitle("Add new Contact");
                if (myPreferences.getPreferences(MyPreferences.employee_insert).equals("1")) {
                    addEmployee.setVisibility(View.VISIBLE);
                } else {
                    addEmployee.setVisibility(View.GONE);
                }
                if (myPreferences.getPreferences(MyPreferences.tag_insert).equals("1")) {
                    addTag.setVisibility(View.VISIBLE);
                } else {
                    addTag.setVisibility(View.GONE);
                }
            } else {
                getSupportActionBar().setTitle("Update Contact");
                data = (IndividualContactHistoryModel) bundle.getSerializable("data");
                contactUpdateId = data.getId();
                addEmployee.setVisibility(View.GONE);
                addTag.setVisibility(View.GONE);

                company_id = bundle.getString("company_id");
                inputName.setText("" + data.getPerson_name());
                inputMiddleName.setText("" + data.getPerson_middle_name());
                inputLastName.setText("" + data.getPerson_surname());
                inputJob.setText("" + data.getPerson_jobtitle());
                inputHomeAddress.setText("" + data.getPerson_home_address());
                inputCountry.setText("" + data.getPerson_country());
                inputState.setText("" + data.getPerson_state());
                inputCity.setText("" + data.getPerson_city());
                inputArea.setText("" + data.getPerson_area());
                inputDistrict.setText("" + data.getPerson_district());
                inputPin.setText("" + data.getPerson_pincode());
                inputHomePhone.setText("" + data.getPerson_home_phone());
                inputMobile.setText("" + data.getPerson_mobile());
                inputWhatsapp.setText("" + data.getPerson_whatsapp());
                inputEmail.setText("" + data.getPerson_email());
                inputWebsite.setText("" + data.getPerson_website());
                birthday.setText("" + data.getPerson_birthdate());
                anniversary.setText("" + data.getPerson_anniversary());
                events.setText("" + data.getPerson_event());
                eventNotes.setText("" + data.getPerson_eventNote());
                notes.setText("" + data.getPerson_note());
                inputAadhar.setText("" + data.getPerson_adhar_no());
                inputPan.setText("" + data.getPerson_pan_no());

                inputOffAddress.setText("" + data.getPerson_office_address());
                inputOffCountry.setText("" + data.getPerson_office_country());
                inputOffState.setText("" + data.getPerson_office_state());
                inputOffCity.setText("" + data.getPerson_office_city());
                inputOffArea.setText("" + data.getPerson_office_area());
                inputOffDistrict.setText("" + data.getPerson_office_district());
                inputOffPin.setText("" + data.getPerson_office_pincode());
                inputBankName.setText("" + data.getPerson_bank_name());
                inputBankHolderName.setText("" + data.getPerson_account_name());
                inputBankAccountNo.setText("" + data.getPerson_bank_acc_no());
                inputBankIfscCode.setText("" + data.getPerson_bank_ifsc());

                try {
                    if (!data.getImage_path().equals("")) {
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        DisplayImageOptions options;
                        imageLoader.init(ImageLoaderConfiguration.createDefault(AddCompanyPersonActivity.this));
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
                        if (city_data.get(i).getId().equals(data.getPerson_city())) {
                            cityId = city_data.get(i).getId();
                            citySpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(data.getPerson_district())) {
                            districtId = district_data.get(i).getId();
                            districtSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(data.getPerson_state())) {
                            stateId = state_data.get(i).getId();
                            stateSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(data.getPerson_country())) {
                            countryId = country_data.get(i).getId();
                            countrySpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(data.getPerson_office_city())) {
                            cityoffId = city_data.get(i).getId();
                            cityOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(data.getPerson_office_district())) {
                            districtoffId = district_data.get(i).getId();
                            districtOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(data.getPerson_office_state())) {
                            stateoffId = state_data.get(i).getId();
                            stateOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(data.getPerson_office_country())) {
                            countryoffId = country_data.get(i).getId();
                            countryOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (data.getCourier_address().equals("1")) {
                    courierAddress.setChecked(true);
                    courierAddressOffice.setChecked(false);
                } else if (data.getCourier_address().equals("2")) {
                    courierAddress.setChecked(false);
                    courierAddressOffice.setChecked(true);
                }

                if (data.getSame_as_office().equals("1")) {
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
        }

        areaAdapter = new SearchAreaAdapter(AddCompanyPersonActivity.this);
        inputArea.setAdapter(areaAdapter);

        areaOffAdapter = new SearchAreaAdapter(AddCompanyPersonActivity.this);
        inputOffArea.setAdapter(areaOffAdapter);

        inputArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    inputPin.setText("" + areaAdapter.suggestions.get(arg2).getPincode());

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

                    InputMethodManager imm = (InputMethodManager) AddCompanyPersonActivity.this.getSystemService(AddCompanyPersonActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputArea.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        inputOffArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    inputOffPin.setText("" + areaOffAdapter.suggestions.get(arg2).getPincode());

                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(areaOffAdapter.suggestions.get(arg2).getCity_id())) {
                            cityoffId = city_data.get(i).getId();
                            cityOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(areaOffAdapter.suggestions.get(arg2).getDistrict_id())) {
                            districtoffId = district_data.get(i).getId();
                            districtOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(areaOffAdapter.suggestions.get(arg2).getState_id())) {
                            stateoffId = state_data.get(i).getId();
                            stateOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(areaOffAdapter.suggestions.get(arg2).getCountry_id())) {
                            countryoffId = country_data.get(i).getId();
                            countryOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) AddCompanyPersonActivity.this.getSystemService(AddCompanyPersonActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputOffArea.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDatePicker = new CustomDatePicker(AddCompanyPersonActivity.this);
                customerDatePicker.setToDate(customerDatePicker.birthdate, birthday, "");
            }
        });

        anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDatePicker = new CustomDatePicker(AddCompanyPersonActivity.this);
                customerDatePicker.setToDate(customerDatePicker.max, anniversary, "");
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDatePicker = new CustomDatePicker(AddCompanyPersonActivity.this);
                customerDatePicker.setToDate(customerDatePicker.min, events, "");
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDatePicker = new CustomDatePicker(AddCompanyPersonActivity.this);
                customerDatePicker.setToDate(customerDatePicker.min, notes, "");
            }
        });

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

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddCompanyPersonActivity.this);
                    alertDialog2.setTitle("Add Employee");
                    LayoutInflater inflater = AddCompanyPersonActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.dialog_new_folder, null);
                    alertDialog2.setView(dialogView);

                    final EditText newFolderEdt = (EditText) dialogView.findViewById(R.id.new_folder_edt);

                    alertDialog2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    if (GlobalElements.isConnectingToInternet(AddCompanyPersonActivity.this)) {
                                        if (newFolderEdt.getText().toString().equals("")) {
                                            Toaster.show(AddCompanyPersonActivity.this, "Enter Employee", false, Toaster.DANGER);
                                        } else {
                                            addEmployee("" + newFolderEdt.getText().toString());
                                        }
                                    } else {
                                        GlobalElements.showDialog(AddCompanyPersonActivity.this);
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
                    buildInfosDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(AddCompanyPersonActivity.this, R.color.colorPrimary));
                    buildInfosDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(AddCompanyPersonActivity.this, R.color.colorPrimary));

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
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(AddCompanyPersonActivity.this);
                    alertDialog2.setTitle("Add Tag");
                    LayoutInflater inflater = AddCompanyPersonActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.dialog_new_folder, null);
                    alertDialog2.setView(dialogView);

                    final EditText newFolderEdt = (EditText) dialogView.findViewById(R.id.new_folder_edt);

                    alertDialog2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    if (GlobalElements.isConnectingToInternet(AddCompanyPersonActivity.this)) {
                                        if (newFolderEdt.getText().toString().equals("")) {
                                            Toaster.show(AddCompanyPersonActivity.this, "Enter Tag", false, Toaster.DANGER);
                                        } else {
                                            addTag("" + newFolderEdt.getText().toString());
                                        }
                                    } else {
                                        GlobalElements.showDialog(AddCompanyPersonActivity.this);
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
                    buildInfosDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(AddCompanyPersonActivity.this, R.color.colorPrimary));
                    buildInfosDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(AddCompanyPersonActivity.this, R.color.colorPrimary));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sameAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    inputOffAddress.setText("" + inputHomeAddress.getText().toString());
                    inputOffArea.setText("" + inputArea.getText().toString());
                    inputOffPin.setText("" + inputPin.getText().toString());

                    for (int i = 0; i < city_data.size(); i++) {
                        if (city_data.get(i).getId().equals(cityId)) {
                            cityoffId = city_data.get(i).getId();
                            cityOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < district_data.size(); i++) {
                        if (district_data.get(i).getId().equals(districtId)) {
                            districtoffId = district_data.get(i).getId();
                            districtOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < state_data.size(); i++) {
                        if (state_data.get(i).getId().equals(stateId)) {
                            stateoffId = state_data.get(i).getId();
                            stateOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                    for (int i = 0; i < country_data.size(); i++) {
                        if (country_data.get(i).getId().equals(stateId)) {
                            countryoffId = country_data.get(i).getId();
                            countryOffSpinner.setSelection(i + 1);
                            break;
                        }
                    }

                } else {
                    inputOffAddress.setText("");
                    inputOffPin.setText("");
                    inputOffArea.setText("");

                    cityoffId = "";
                    cityOffSpinner.setSelection(0);
                    districtoffId = "";
                    districtOffSpinner.setSelection(0);
                    stateoffId = "";
                    stateOffSpinner.setSelection(0);
                    countryoffId = "";
                    countryOffSpinner.setSelection(0);
                }
            }
        });

        courierAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    courierAddressOffice.setChecked(false);
                }
            }
        });

        courierAddressOffice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    courierAddress.setChecked(false);
                }
            }
        });

        //employee("");
        //tag("");
        JobTitle("");

    }

    public void JobTitle(String type) {
        try {
            jobTitle_data.clear();
            if (!myPreferences.getPreferences(MyPreferences.Job_title).equals("")) {
                JSONArray employeeArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Job_title));
                for (int i = 0; i < employeeArray.length(); i++) {
                    JSONObject c = employeeArray.getJSONObject(i);
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    jobTitle_data.add(da);
                }
            }
            jobTitle_adapter = new GeneralAdapter(AddCompanyPersonActivity.this, jobTitle_data);
            spinnerJob.setAdapter(jobTitle_adapter);
            contactEmplayee.setHint("Select Job");
            SpinnerInteractionListener_job listener_1 = new SpinnerInteractionListener_job();
            spinnerJob.setOnTouchListener(listener_1);
            spinnerJob.setOnItemSelectedListener(listener_1);

            if (insert_update_type.equals("1")) {
                for (int i = 0; i < jobTitle_data.size(); i++) {
                    if (jobTitle_data.get(i).getName().equals("" + data.getPerson_jobtitle())) {
                        JobId = jobTitle_data.get(i).getName();
                        spinnerJob.setSelection(i + 1);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void employee(String type) {
        try {
            employee_data.clear();
            if (!myPreferences.getPreferences(MyPreferences.Employee).equals("")) {
                JSONArray employeeArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Employee));
                for (int i = 0; i < employeeArray.length(); i++) {
                    JSONObject c = employeeArray.getJSONObject(i);
                    GeneralModel da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    employee_data.add(da);
                }
            }
            employee_adapter = new GeneralAdapter(AddCompanyPersonActivity.this, employee_data);
            contactEmplayee.setAdapter(employee_adapter);
            contactEmplayee.setHint("Select Employee");
            SpinnerInteractionListener_employee listener_1 = new SpinnerInteractionListener_employee();
            contactEmplayee.setOnTouchListener(listener_1);
            contactEmplayee.setOnItemSelectedListener(listener_1);

            if (type.equals("add")) {
                try {
                    employeeId = employee_data.get(employee_data.size() - 1).getId();
                    contactEmplayee.setSelection(employee_data.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (insert_update_type.equals("1")) {
                    for (int i = 0; i < employee_data.size(); i++) {
                        if (employee_data.get(i).getId().equals("" + data.getEmployee_id())) {
                            employeeId = employee_data.get(i).getId();
                            contactEmplayee.setSelection(i + 1);
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

            tag_adapter = new GeneralAdapter(AddCompanyPersonActivity.this, tag_data);
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
                try {
                    if (courierAddress.isChecked()) {
                        courierChk = "1";
                    } else if (courierAddressOffice.isChecked()) {
                        courierChk = "2";
                    } else {
                        courierChk = "0";
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

                    if (GlobalElements.isConnectingToInternet(AddCompanyPersonActivity.this)) {
                        if (insert_update_type.equals("0")) {
                            newContactAdd();
                        } else {
                            updateContactAdd();
                        }
                    } else {
                        GlobalElements.showDialog(AddCompanyPersonActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        imageInputHelper.requestCropImage(uri, 256, 256, 1, 1);
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

    public class SpinnerInteractionListener_job implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    JobId = jobTitle_data.get(position).getName();
                } catch (Exception e) {
                    e.printStackTrace();
                    JobId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_employee implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    employeeId = employee_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    employeeId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

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

    private void newContactAdd() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddCompanyPersonActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);

            RequestBody requestfile_image;
            MultipartBody.Part body_image;

            Call<ResponseBody> call;
            if (UploadLogo == null) {
                call = request.addIndividualContact(myPreferences.getPreferences(MyPreferences.id), company_id, employeeId, tagId, inputName.getText().toString(), inputMiddleName.getText().toString(), inputLastName.getText().toString(), JobId, inputHomeAddress.getText().toString(),
                        cityId, inputArea.getText().toString(), districtId, stateId, countryId, inputPin.getText().toString(), inputHomePhone.getText().toString(),
                        inputMobile.getText().toString(), inputWhatsapp.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), birthday.getText().toString(),
                        anniversary.getText().toString(), events.getText().toString(), eventNotes.getText().toString(), notes.getText().toString(), inputAadhar.getText().toString(), inputPan.getText().toString(), courierChk, printChk, sameAsChk, inputOffAddress.getText().toString(),
                        countryoffId, stateoffId, cityoffId, inputOffArea.getText().toString(), districtoffId, inputOffPin.getText().toString(),
                        inputBankHolderName.getText().toString(), inputBankName.getText().toString(), inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString());
            } else {
                requestfile_image = RequestBody.create(MediaType.parse("image/*"), UploadLogo);
                body_image = MultipartBody.Part.createFormData("person_photo", UploadLogo.getName(), requestfile_image);
                call = request.addIndividualContact(myPreferences.getPreferences(MyPreferences.id), company_id, employeeId, tagId, inputName.getText().toString(), inputMiddleName.getText().toString(), inputLastName.getText().toString(), JobId, inputHomeAddress.getText().toString(),
                        cityId, inputArea.getText().toString(), districtId, stateId, countryId, inputPin.getText().toString(), inputHomePhone.getText().toString(),
                        inputMobile.getText().toString(), inputWhatsapp.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), birthday.getText().toString(),
                        anniversary.getText().toString(), events.getText().toString(), eventNotes.getText().toString(), notes.getText().toString(), inputAadhar.getText().toString(), inputPan.getText().toString(), courierChk, printChk, sameAsChk, inputOffAddress.getText().toString(),
                        countryoffId, stateoffId, cityoffId, inputOffArea.getText().toString(), districtoffId, inputOffPin.getText().toString(),
                        inputBankHolderName.getText().toString(), inputBankName.getText().toString(), inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString(), body_image);
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
                            setResult(13, intent);
                            finish();
                        } else {
                            Toaster.show(AddCompanyPersonActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
            final ProgressDialog pd = new ProgressDialog(AddCompanyPersonActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            RequestBody requestfile_image;
            MultipartBody.Part body_image;
            Call<ResponseBody> call;
            if (UploadLogo == null) {
                call = request.updateIndividualContact(myPreferences.getPreferences(MyPreferences.id), company_id, contactUpdateId, employeeId, tagId, inputName.getText().toString(), inputMiddleName.getText().toString(), inputLastName.getText().toString(), inputJob.getText().toString(), inputHomeAddress.getText().toString(),
                        cityId, inputArea.getText().toString(), districtId, stateId, countryId, inputPin.getText().toString(), inputHomePhone.getText().toString(),
                        inputMobile.getText().toString(), inputWhatsapp.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), birthday.getText().toString(),
                        anniversary.getText().toString(), events.getText().toString(), eventNotes.getText().toString(), notes.getText().toString(), inputAadhar.getText().toString(), inputPan.getText().toString(), courierChk, printChk, sameAsChk, inputOffAddress.getText().toString(),
                        countryoffId, stateoffId, cityoffId, inputOffArea.getText().toString(), districtoffId, inputOffPin.getText().toString(),
                        inputBankHolderName.getText().toString(), inputBankName.getText().toString(), inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString());
            } else {
                requestfile_image = RequestBody.create(MediaType.parse("image/*"), UploadLogo);
                body_image = MultipartBody.Part.createFormData("person_photo", UploadLogo.getName(), requestfile_image);
                call = request.updateIndividualContact(myPreferences.getPreferences(MyPreferences.id), company_id, contactUpdateId, employeeId, tagId, inputName.getText().toString(), inputMiddleName.getText().toString(), inputLastName.getText().toString(), inputJob.getText().toString(), inputHomeAddress.getText().toString(),
                        cityId, inputArea.getText().toString(), districtId, stateId, countryId, inputPin.getText().toString(), inputHomePhone.getText().toString(),
                        inputMobile.getText().toString(), inputWhatsapp.getText().toString(), inputEmail.getText().toString(), inputWebsite.getText().toString(), birthday.getText().toString(),
                        anniversary.getText().toString(), events.getText().toString(), eventNotes.getText().toString(), notes.getText().toString(), inputAadhar.getText().toString(), inputPan.getText().toString(), courierChk, printChk, sameAsChk, inputOffAddress.getText().toString(),
                        countryoffId, stateoffId, cityoffId, inputOffArea.getText().toString(), districtoffId, inputOffPin.getText().toString(),
                        inputBankHolderName.getText().toString(), inputBankName.getText().toString(), inputBankAccountNo.getText().toString(), inputBankIfscCode.getText().toString(), body_image);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");

                            data.setLabel_id(result.getString("label_id"));
                            data.setLabel_slug(result.getString("label_slug"));
                            data.setTag_id(result.getString("tag_id"));
                            data.setTag_slug(result.getString("tag_slug"));
                            data.setPerson_name("" + result.getString("person_name"));
                            data.setPerson_middle_name(result.getString("person_middle_name"));
                            data.setPerson_surname(result.getString("person_surname"));
                            data.setPerson_jobtitle(result.getString("person_jobtitle"));
                            data.setPerson_home_address("" + result.getString("person_home_address"));
                            data.setPerson_area("" + result.getString("person_area"));

                            data.setPerson_city("" + result.getString("person_city"));
                            data.setPerson_district("" + result.getString("person_district"));
                            data.setPerson_state("" + result.getString("person_state"));
                            data.setPerson_country("" + result.getString("person_country"));

                            data.setPerson_city_name("" + result.getString("person_city_name"));
                            data.setPerson_district_name("" + result.getString("person_district_name"));
                            data.setPerson_state_name("" + result.getString("person_state_name"));
                            data.setPerson_country_name("" + result.getString("person_country_name"));

                            data.setPerson_pincode(result.getString("person_pincode"));
                            data.setPerson_home_phone(result.getString("person_home_phone"));
                            data.setPerson_mobile("" + result.getString("person_mobile"));
                            data.setPerson_whatsapp(result.getString("person_whatsapp"));
                            data.setPerson_email("" + result.getString("person_email"));
                            data.setPerson_website(result.getString("person_website"));
                            data.setPerson_birthdate(result.getString("person_birthdate"));
                            data.setPerson_anniversary(result.getString("person_anniversary"));
                            data.setPerson_eventNote(result.getString("person_event_note"));
                            data.setPerson_event(result.getString("person_event"));
                            data.setPerson_note(result.getString("person_note"));
                            data.setCourier_address(result.getString("person_courier_address"));
                            data.setPrint_label(result.getString("print_label"));
                            data.setPerson_adhar_no(result.getString("person_adhar_no"));
                            data.setPerson_pan_no(result.getString("person_pan_no"));
                            data.setImage_path("" + result.getString("person_photo"));
                            data.setPerson_office_address(result.getString("person_office_address"));

                            data.setPerson_office_area(result.getString("person_office_area"));
                            data.setPerson_office_district(result.getString("person_office_district"));
                            data.setPerson_office_city(result.getString("person_office_city"));
                            data.setPerson_office_state(result.getString("person_office_state"));
                            data.setPerson_office_country(result.getString("person_office_country"));

                            data.setPerson_office_area(result.getString("person_office_area"));
                            data.setPerson_office_district_name(result.getString("person_office_district_name"));
                            data.setPerson_office_city_name(result.getString("person_office_city_name"));
                            data.setPerson_office_state_name(result.getString("person_office_state_name"));
                            data.setPerson_office_country_name(result.getString("person_office_country_name"));

                            data.setPerson_office_pincode(result.getString("person_office_pincode"));
                            data.setPerson_bank_name(result.getString("person_bank_name"));
                            data.setPerson_account_name(result.getString("person_account_name"));
                            data.setPerson_bank_acc_no(result.getString("person_bank_acc_no"));
                            data.setPerson_bank_ifsc(result.getString("person_bank_ifsc"));
                            data.setSame_as_office(result.getString("same_as_office"));
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", data);
                            intent.putExtras(bundle);
                            setResult(13, intent);
                            finish();
                        } else {
                            Toaster.show(AddCompanyPersonActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void addTag(String tag) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddCompanyPersonActivity.this);
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
                            Toaster.show(AddCompanyPersonActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void addEmployee(String employee) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddCompanyPersonActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.addEmployee(employee);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            if (!myPreferences.getPreferences(MyPreferences.Employee).equals("")) {
                                JSONArray labelArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Employee));
                                JSONObject label = new JSONObject();
                                label.put("id", "" + result.getString("id"));
                                label.put("name", "" + result.getString("name"));
                                labelArray.put(label);
                                myPreferences.setPreferences(MyPreferences.Employee, labelArray.toString());
                                employee("add");
                            } else {
                                JSONArray labelArray = new JSONArray();
                                JSONObject label = new JSONObject();
                                label.put("id", "" + result.getString("id"));
                                label.put("name", "" + result.getString("name"));
                                labelArray.put(label);
                                myPreferences.setPreferences(MyPreferences.Employee, labelArray.toString());
                                employee("add");
                            }
                        } else {
                            Toaster.show(AddCompanyPersonActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
