package com.innovent.erp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CompanyContactHistoryModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 3/5/2018.
 */

public class CompanyContactDetailFragment extends Fragment {


    MyPreferences myPreferences;
    CompanyContactHistoryModel data;
    AlertDialog buildInfosDialog;

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.input_label_name)
    EditText inputLabelName;
    @BindView(R.id.input_tag_name)
    EditText inputTagName;
    @BindView(R.id.input_layout_tag)
    TextInputLayout inputLayoutTag;
    @BindView(R.id.input_company_name)
    EditText inputCompanyName;
    @BindView(R.id.input_layout_company_name)
    TextInputLayout inputLayoutCompanyName;
    @BindView(R.id.input_off_address)
    EditText inputOffAddress;
    @BindView(R.id.input_layout_off_address)
    TextInputLayout inputLayoutOffAddress;
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
    @BindView(R.id.input_where_country)
    AutoCompleteTextView inputWhereCountry;
    @BindView(R.id.input_layout_where_country)
    TextInputLayout inputLayoutWhereCountry;
    @BindView(R.id.input_where_state)
    AutoCompleteTextView inputWhereState;
    @BindView(R.id.input_layout_where_state)
    TextInputLayout inputLayoutWhereState;
    @BindView(R.id.input_where_city)
    AutoCompleteTextView inputWhereCity;
    @BindView(R.id.input_layout_where_city)
    TextInputLayout inputLayoutWhereCity;
    @BindView(R.id.input_where_pin)
    EditText inputWherePin;
    @BindView(R.id.input_layout_where_pin)
    TextInputLayout inputLayoutWherePin;
    @BindView(R.id.input_where_offmobile)
    EditText inputWhereOffmobile;
    @BindView(R.id.input_layout_where_offmobile)
    TextInputLayout inputLayoutWhereOffmobile;
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
    @BindView(R.id.input_layout_label)
    TextInputLayout inputLayoutLabel;
    @BindView(R.id.same_address)
    CheckBox sameAddress;
    @BindView(R.id.courier_address_wherehouse)
    CheckBox courierAddressWherehouse;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_contact_detail, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());

        try {
            Bundle bundle = getArguments();
            data = (CompanyContactHistoryModel) bundle.getSerializable("data");
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        try {
            Bundle bundle = _data.getExtras();
            data = (CompanyContactHistoryModel) bundle.getSerializable("data");
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareContact() {
        try {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
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
                    if (data.getCompany_mobile().equals("")) {
                        Toaster.show(getActivity(), "Mobile number is empty", false, Toaster.DANGER);
                    } else if (data.getCompany_name().equals("")) {
                        Toaster.show(getActivity(), "Company name is empty", false, Toaster.DANGER);
                    } else {
                        try {
                            buildInfosDialog.dismiss();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                            StringBuilder address = new StringBuilder();
                            if (!data.getCompany_address().equals("")) {
                                address.append("" + data.getCompany_address()).append(",");
                            }
                            if (!data.getCompany_area().equals("")) {
                                address.append("" + data.getCompany_area()).append(",");
                            }
                            if (!data.getCompany_city().equals("")) {
                                address.append("" + data.getCompany_city()).append(",");
                            }
                            if (!data.getCompany_pincode().equals("")) {
                                address.append("" + data.getCompany_pincode()).append(",");
                            }

                            share.putExtra(Intent.EXTRA_TEXT, "C Name : " + data.getCompany_name() + "\nMobile : " + data.getCompany_mobile() +"\nEmail :"+data.getCompany_email()+ "\naddress : " + GlobalElements.getRemoveLastComma("" + address.toString()));
                            getActivity().startActivity(Intent.createChooser(share, "Share link!"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            vcfShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (data.getCompany_mobile().equals("")) {
                            Toaster.show(getActivity(), "Mobile number is empty", false, Toaster.DANGER);
                        } else if (data.getCompany_name().equals("")) {
                            Toaster.show(getActivity(), "Company name is empty", false, Toaster.DANGER);
                        } else {
                            buildInfosDialog.dismiss();
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
                            fw.write("FN:" + data.getCompany_name() + "\r\n");
                            //  fw.write("ORG:" + p.getCompanyName() + "\r\n");
                            //  fw.write("TITLE:" + p.getTitle() + "\r\n");
                            fw.write("TEL;TYPE=WORK,VOICE:" + data.getCompany_mobile() + "\r\n");
                            //   fw.write("TEL;TYPE=HOME,VOICE:" + p.getHomePhone() + "\r\n");
                            //   fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
                            //fw.write("EMAIL;TYPE=PREF,INTERNET:" + etmail.getText().toString() + "\r\n");
                            fw.write("END:VCARD\r\n");
                            fw.close();

                            Uri contentUri = null;
                            if (GlobalElements.getVersionCheck()) {
                                contentUri = FileProvider.getUriForFile(getActivity(), "" + GlobalElements.fileprovider_path, vcfFile);
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
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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
        try {
            inputLabelName.setText("" + data.getLabel_slug());
            inputTagName.setText("" + data.getTag_slug());
            inputCompanyName.setText("" + data.getCompany_name());
            inputOffAddress.setText("" + data.getCompany_address());
            inputCountry.setText("" + data.getCompany_country_name());
            inputState.setText("" + data.getCompany_state_name());
            inputCity.setText("" + data.getCompany_city_name());
            inputZone.setText("" + data.getCompany_zone_name());
            inputArea.setText("" + data.getCompany_area());
            inputDistrict.setText("" + data.getCompany_district_name());
            inputPin.setText("" + data.getCompany_pincode());
            inputOffPhone.setText("" + data.getCompany_office_phone());
            inputMobile.setText("" + data.getCompany_mobile());
            inputEmail.setText("" + data.getCompany_email());
            inputWebsite.setText("" + data.getCompany_website());
            inputWhereAddress.setText("" + data.getCompany_wharehouse_address());
            inputWhereCountry.setText("" + data.getCompany_wharehouse_country_name());
            inputWhereState.setText("" + data.getCompany_wharehouse_state_name());
            inputWhereCity.setText("" + data.getCompany_wharehouse_city_name());
            inputWhereZone.setText("" + data.getCompany_wharehouse_zone_name());
            inputWhereArea.setText("" + data.getCompany_wharehouse_area());
            inputWhereDistrict.setText("" + data.getCompany_wharehouse_district_name());
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
                    imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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
            courierAddressWherehouse.setClickable(false);
            courierAddressWherehouse.setFocusable(false);
            courierAddressWherehouse.setFocusableInTouchMode(false);
            printLable.setClickable(false);
            printLable.setFocusable(false);
            printLable.setFocusableInTouchMode(false);

            sameAddress.setClickable(false);
            sameAddress.setFocusable(false);
            sameAddress.setFocusableInTouchMode(false);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
