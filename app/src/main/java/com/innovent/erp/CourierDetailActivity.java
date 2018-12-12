package com.innovent.erp;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.innovent.erp.model.CourierModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourierDetailActivity extends AppCompatActivity {


    CourierModel data;
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


    @BindView(R.id.sender_card)
    CardView senderCard;
    @BindView(R.id.input_reciver_company)
    AutoCompleteTextView inputReciverCompany;
    @BindView(R.id.input_reciver_person)
    AutoCompleteTextView inputReciverPerson;
    @BindView(R.id.input_receiver_person_name)
    AutoCompleteTextView inputReceiverPersonName;
    @BindView(R.id.input_receiver_person_mobile)
    AutoCompleteTextView inputReceiverPersonMobile;
    @BindView(R.id.receiver_person_layout)
    LinearLayout receiverPersonLayout;
    @BindView(R.id.input_receiver_mobile)
    EditText inputReceiverMobile;
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
    @BindView(R.id.reciver_layout)
    LinearLayout reciverLayout;
    @BindView(R.id.reciver_card)
    CardView reciverCard;
    @BindView(R.id.change_layout)
    LinearLayout changeLayout;
    @BindView(R.id.input_parcel_type)
    EditText inputParcelType;
    @BindView(R.id.input_layout_parcel_type)
    TextInputLayout inputLayoutParcelType;
    @BindView(R.id.input_description)
    EditText inputDescription;
    @BindView(R.id.input_layout_description)
    TextInputLayout inputLayoutDescription;
    @BindView(R.id.input_weight)
    EditText inputWeight;
    @BindView(R.id.input_layout_weight)
    TextInputLayout inputLayoutWeight;
    @BindView(R.id.input_weight_gm)
    EditText inputWeightGm;
    @BindView(R.id.input_layout_weight_gm)
    TextInputLayout inputLayoutWeightGm;
    @BindView(R.id.input_cost)
    EditText inputCost;
    @BindView(R.id.input_layout_cost)
    TextInputLayout inputLayoutCost;
    @BindView(R.id.input_courier_company_name)
    EditText inputCourierCompanyName;
    @BindView(R.id.input_layout_courier_company_name)
    TextInputLayout inputLayoutCourierCompanyName;
    @BindView(R.id.input_pickup_person)
    EditText inputPickupPerson;
    @BindView(R.id.input_layout_pickup_person)
    TextInputLayout inputLayoutPickupPerson;
    @BindView(R.id.input_deliverd_date)
    EditText inputDeliverdDate;
    @BindView(R.id.input_layout_deliverd_date)
    TextInputLayout inputLayoutDeliverdDate;
    @BindView(R.id.input_tracking_no)
    EditText inputTrackingNo;
    @BindView(R.id.input_layout_tracking_no)
    TextInputLayout inputLayoutTrackingNo;
    @BindView(R.id.input_delivery_status)
    EditText inputDeliveryStatus;
    @BindView(R.id.input_sender_area)
    AutoCompleteTextView inputSenderArea;
    @BindView(R.id.input_sender_district)
    AutoCompleteTextView inputSenderDistrict;
    @BindView(R.id.input_sender_zone)
    AutoCompleteTextView inputSenderZone;
    @BindView(R.id.input_receiver_area)
    AutoCompleteTextView inputReceiverArea;
    @BindView(R.id.input_receiver_district)
    AutoCompleteTextView inputReceiverDistrict;
    @BindView(R.id.input_receiver_zone)
    AutoCompleteTextView inputReceiverZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {

            Bundle bundle = getIntent().getExtras();
            data = (CourierModel) bundle.getSerializable("data");
            getSupportActionBar().setTitle("#" + data.getCourier_no());

            inputSenderPersonName.setText("" + data.getAssigned_person_name());
            inputSenderPersonMobile.setText("" + data.getAssigned_person_mobile());
            inputReceiverPersonName.setText("" + data.getAssigned_person_name());
            inputReceiverPersonMobile.setText("" + data.getAssigned_person_mobile());

            inputSenderCompany.setText("" + data.getSender_company_name());
            inputSenderPerson.setText("" + data.getSender_person_name());
            inputReciverCompany.setText("" + data.getReceiver_company_name());
            inputReciverPerson.setText("" + data.getReceiver_person_name());

            inputSenderMobile.setText("" + data.getSender_contact_no());
            inputSenderAddress.setText("" + data.getSender_address());
            inputSenderArea.setText("" + data.getSender_area());
            inputSenderCity.setText("" + data.getSender_city_name());
            inputSenderDistrict.setText("" + data.getSender_district_name());
            inputSenderState.setText("" + data.getSender_state_name());
            inputSenderCountry.setText("" + data.getSender_country_name());
            inputSenderPin.setText("" + data.getSender_pincode());
            inputSenderZone.setText("" + data.getSender_zone_name());

            inputReceiverMobile.setText("" + data.getReceiver_contact_no());
            inputReceiverAddress.setText("" + data.getReceiver_address());
            inputReceiverArea.setText("" + data.getReceiver_area());
            inputReceiverCity.setText("" + data.getReceiver_city_name());
            inputReceiverDistrict.setText("" + data.getReceiver_district_name());
            inputReceiverState.setText("" + data.getReceiver_state_name());
            inputReceiverCountry.setText("" + data.getReceiver_country_name());
            inputReceiverPin.setText("" + data.getReceiver_pincode());
            inputReceiverZone.setText("" + data.getReceiver_zone_name());

            inputParcelType.setText("" + data.getParcel_type_slug());
            inputDescription.setText("" + data.getParcel_description());

            inputCost.setText("" + data.getParcel_cost());
            inputCourierCompanyName.setText("" + data.getShipping_courier_name());
            inputPickupPerson.setText("" + data.getShipping_pickup_person());
            inputDeliverdDate.setText("" + data.getShipping_deliverd_date());
            inputTrackingNo.setText("" + data.getTracking_no());
            inputDeliveryStatus.setText("" + data.getDelivery_status());

            if (data.getKg_unit().equals("kg")) {
                inputWeight.setText("" + data.getKg_value());
            }

            if (data.getGm_unit().equals("gm")) {
                inputWeightGm.setText("" + data.getGm_value());
            }

            inputSenderPerson.setText("" + data.getAssigned_person_name());
            inputSenderMobile.setText("" + data.getAssigned_person_mobile());

            if (data.getCourier_type().equals("1")) {
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
}
