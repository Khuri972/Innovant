package com.innovent.erp.helpDesk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.innovent.erp.R;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.helpDesk.model.ComplantModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddComplantActivity extends AppCompatActivity {

    @BindView(R.id.customer_name_edt)
    AutoCompleteTextView customerNameEdt;
    @BindView(R.id.dealer_name_edt)
    AutoCompleteTextView dealerNameEdt;
    @BindView(R.id.service_eng_name_edt)
    AutoCompleteTextView serviceEngNameEdt;
    @BindView(R.id.mobile_edt)
    AutoCompleteTextView mobileEdt;
    @BindView(R.id.city_edt)
    AutoCompleteTextView cityEdt;
    @BindView(R.id.dead_line_edt)
    EditText deadLineEdt;
    @BindView(R.id.closing_date_edt)
    EditText closingDateEdt;

    CustomDatePicker datePicker;
    ComplantModel model = new ComplantModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complant);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Complaint");

        deadLineEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker = new CustomDatePicker(AddComplantActivity.this);
                datePicker.setToDate(datePicker.min, deadLineEdt, "");
            }
        });

        closingDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker = new CustomDatePicker(AddComplantActivity.this);
                datePicker.setToDate(datePicker.min, closingDateEdt, "");
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
                try {
                    model.setId("1");
                    model.setComplantNo("d12");
                    model.setCustomerName("abc");
                    model.setDealerName("ravi");
                    model.setServiceEngName("jay ");
                    model.setMobile("9979045113");
                    model.setCity("Rajkot");
                    model.setDeadLine("12-04-2018");
                    model.setClosingDate("15-04-2018");
                    model.setStatus("Pending");

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", model);
                    intent.putExtras(bundle);
                    setResult(10, intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_courier, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
