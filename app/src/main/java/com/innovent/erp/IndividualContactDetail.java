package com.innovent.erp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.IndividualContactHistoryModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndividualContactDetail extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.input_employee)
    EditText inputEmployee;
    @BindView(R.id.input_tag)
    EditText inputTag;
    @BindView(R.id.input_layout_tag)
    TextInputLayout inputLayoutTag;
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

    IndividualContactHistoryModel data;
    int resultCode = 0;
    MyPreferences myPreferences;
    @BindView(R.id.input_layout_employee)
    TextInputLayout inputLayoutEmployee;
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
    @BindView(R.id.input_middle_name)
    EditText inputMiddleName;
    @BindView(R.id.input_layout_middle_name)
    TextInputLayout inputLayoutMiddleName;
    @BindView(R.id.input_last_name)
    EditText inputLastName;
    @BindView(R.id.input_layout_last_name)
    TextInputLayout inputLayoutLastName;
    @BindView(R.id.event_notes)
    EditText eventNotes;


    @BindView(R.id.input_off_area)
    AutoCompleteTextView inputOffArea;
    @BindView(R.id.input_layout_off_area)
    TextInputLayout inputLayoutOffArea;
    @BindView(R.id.input_off_district)
    AutoCompleteTextView inputOffDistrict;
    @BindView(R.id.input_layout_off_district)
    TextInputLayout inputLayoutOffDistrict;
    @BindView(R.id.input_area)
    AutoCompleteTextView inputArea;
    @BindView(R.id.input_layout_area)
    TextInputLayout inputLayoutArea;
    @BindView(R.id.input_district)
    AutoCompleteTextView inputDistrict;
    @BindView(R.id.input_layout_district)
    TextInputLayout inputLayoutDistrict;

    AlertDialog buildInfosDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_contact_detail);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Detail");
        try {
            Bundle bundle = getIntent().getExtras();
            data = (IndividualContactHistoryModel) bundle.getSerializable("data");
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.company_contact_detail, menu);
        try {
            //MenuItem share = menu.findItem(R.id.action_share);
            MenuItem update = menu.findItem(R.id.action_update);
            if (myPreferences.getPreferences(MyPreferences.contact_update).equals("1")) {
                update.setVisible(true);
            } else {
                update.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (resultCode == 12) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data);
                    intent.putExtras(bundle);
                    setResult(12, intent);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.action_update:
                Intent intent = new Intent(IndividualContactDetail.this, AddContactIndividualActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "1");
                bundle.putSerializable("data", data);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
            case R.id.action_share:
                shareContact();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (resultCode == 12) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            intent.putExtras(bundle);
            setResult(12, intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(requestCode, _resultCode, _data);
        if (_resultCode == 12) {
            try {
                resultCode = 12;
                Bundle bundle = _data.getExtras();
                data = (IndividualContactHistoryModel) bundle.getSerializable("data");
                setData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shareContact() {
        try {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(IndividualContactDetail.this);
            LayoutInflater inflater = IndividualContactDetail.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_share_contact, null);
            alertDialog2.setView(dialogView);

            Button textShare = (Button) dialogView.findViewById(R.id.shareText);
            Button vcfShare = (Button) dialogView.findViewById(R.id.shareVcf);
            ImageView cancelbtn = (ImageView) dialogView.findViewById(R.id.cancelbtn);

            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buildInfosDialog.dismiss();
                }
            });

            textShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.getPerson_mobile().equals("") && data.getPerson_whatsapp().equals("")) {
                        Toaster.show(IndividualContactDetail.this, "Mobile number is empty", false, Toaster.DANGER);
                    } else if (data.getPerson_name().equals("")) {
                        Toaster.show(IndividualContactDetail.this, "Persion name is empty", false, Toaster.DANGER);
                    } else {
                        try {
                            buildInfosDialog.dismiss();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            StringBuilder address = new StringBuilder();
                            if (!data.getPerson_office_address().equals("")) {
                                address.append("" + data.getPerson_office_address()).append(",");
                            }
                            if (!data.getPerson_office_area().equals("")) {
                                address.append("" + data.getPerson_office_area()).append(",");
                            }
                            if (!data.getPerson_office_city_name().equals("")) {
                                address.append("" + data.getPerson_office_city_name()).append(",");
                            }
                            if (!data.getPerson_office_district_name().equals("")) {
                                address.append("" + data.getPerson_office_district_name()).append(",");
                            }
                            if (!data.getPerson_office_state_name().equals("")) {
                                address.append("" + data.getPerson_office_state_name()).append(",");
                            }
                            if (!data.getPerson_office_country_name().equals("")) {
                                address.append("" + data.getPerson_office_country_name()).append(",");
                            }
                            if (!data.getPerson_office_pincode().equals("")) {
                                address.append("" + data.getPerson_office_pincode()).append(",");
                            }
                            share.putExtra(Intent.EXTRA_TEXT, "Name : " + data.getPerson_name() + "\nMobile : " + data.getPerson_mobile() + "\nEmail : " + data.getPerson_email() + "\naddress : " + GlobalElements.getRemoveLastComma("" + address.toString()));
                            startActivity(Intent.createChooser(share, "Share link!"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            vcfShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.getPerson_mobile().equals("") && data.getPerson_whatsapp().equals("")) {
                        Toaster.show(IndividualContactDetail.this, "Mobile number is empty", false, Toaster.DANGER);
                    } else if (data.getPerson_name().equals("")) {
                        Toaster.show(IndividualContactDetail.this, "Persion name is empty", false, Toaster.DANGER);
                    } else {

                        try {
                            File vcfFile;
                            //String ph="9979045113",name="hardip";
                            File vdfdirectory = new File(
                                    Environment.getExternalStorageDirectory() + GlobalElements.directory);
                            // have the object build the directory structure, if needed.
                            if (!vdfdirectory.exists()) {
                                vdfdirectory.mkdirs();
                            }

                            vcfFile = new File(vdfdirectory, "company_contact.vcf");
                            FileWriter fw = null;
                            fw = new FileWriter(vcfFile);
                            fw.write("BEGIN:VCARD\r\n");
                            fw.write("VERSION:3.0\r\n");
                            // fw.write("N:" + p.getSurname() + ";" + p.getFirstName() + "\r\n");
                            fw.write("FN:" + data.getPerson_name() + "\r\n");
                            //  fw.write("ORG:" + p.getCompanyName() + "\r\n");
                            //  fw.write("TITLE:" + p.getTitle() + "\r\n");
                            if (!data.getPerson_mobile().equals("") && !data.getPerson_whatsapp().equals("")) {
                                fw.write("TEL;TYPE=WORK,VOICE:" + data.getPerson_mobile() + "\r\n");
                                fw.write("TEL;TYPE=WORK,VOICE:" + data.getPerson_whatsapp() + "\r\n");
                            } else if (!data.getPerson_mobile().equals("")) {
                                fw.write("TEL;TYPE=WORK,VOICE:" + data.getPerson_mobile() + "\r\n");
                            } else if (!data.getPerson_whatsapp().equals("")) {
                                fw.write("TEL;TYPE=WORK,VOICE:" + data.getPerson_whatsapp() + "\r\n");
                            }
                            //   fw.write("TEL;TYPE=HOME,VOICE:" + p.getHomePhone() + "\r\n");
                            //   fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
                            //fw.write("EMAIL;TYPE=PREF,INTERNET:" + etmail.getText().toString() + "\r\n");
                            fw.write("END:VCARD\r\n");
                            fw.close();

                            Uri contentUri = null;
                            if (GlobalElements.getVersionCheck()) {
                                contentUri = FileProvider.getUriForFile(IndividualContactDetail.this, "" + GlobalElements.fileprovider_path, vcfFile);
                            } else {
                                contentUri = Uri.fromFile(vcfFile);
                            }

                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_SEND);
                            i.setType("text/x-vcard");
                            i.putExtra(Intent.EXTRA_STREAM, contentUri);
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //startActivity(i);
                            startActivity(Intent.createChooser(i, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            buildInfosDialog = alertDialog2.create();
            buildInfosDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData() {
        inputEmployee.setText("" + data.getLabel_slug());
        inputTag.setText("" + data.getTag_slug());
        inputName.setText("" + data.getPerson_name());
        inputMiddleName.setText("" + data.getPerson_middle_name());
        inputLastName.setText("" + data.getPerson_surname());
        inputJob.setText("" + data.getPerson_jobtitle());
        inputHomeAddress.setText("" + data.getPerson_home_address());
        inputCity.setText("" + data.getPerson_city_name());
        inputArea.setText("" + data.getPerson_area());
        inputDistrict.setText("" + data.getPerson_district_name());
        inputState.setText("" + data.getPerson_state_name());
        inputCountry.setText("" + data.getPerson_country_name());
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
        inputOffCountry.setText("" + data.getPerson_office_country_name());
        inputOffState.setText("" + data.getPerson_office_state_name());
        inputOffCity.setText("" + data.getPerson_office_city_name());
        inputOffArea.setText("" + data.getPerson_office_area());
        inputOffDistrict.setText("" + data.getPerson_office_district_name());
        inputOffPin.setText("" + data.getPerson_office_pincode());
        inputBankName.setText("" + data.getPerson_bank_name());
        inputBankHolderName.setText("" + data.getPerson_account_name());
        inputBankAccountNo.setText("" + data.getPerson_bank_acc_no());
        inputBankIfscCode.setText("" + data.getPerson_bank_ifsc());

        try {
            if (!data.getImage_path().equals("")) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options;
                imageLoader.init(ImageLoaderConfiguration.createDefault(IndividualContactDetail.this));
                options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                imageLoader.displayImage(data.getImage_path(), img, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        courierAddress.setClickable(false);
        courierAddress.setFocusable(false);
        courierAddress.setFocusableInTouchMode(false);

        courierAddressOffice.setClickable(false);
        courierAddressOffice.setFocusable(false);
        courierAddressOffice.setFocusableInTouchMode(false);

        sameAddress.setClickable(false);
        sameAddress.setFocusable(false);
        sameAddress.setFocusableInTouchMode(false);

        printLable.setClickable(false);
        printLable.setFocusable(false);
        printLable.setFocusableInTouchMode(false);

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
}
