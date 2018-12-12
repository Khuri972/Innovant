package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.adapter.SearchReciverCompanyAdapter;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.MyTextWatcher;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.model.ChequeModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.searchAdapter.SearchBankAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChequeActivity extends AppCompatActivity {

    @BindView(R.id.input_party_name)
    EditText inputPartyName;
    @BindView(R.id.input_layout_partyname)
    TextInputLayout inputLayoutPartyname;
    @BindView(R.id.input_cheque_no)
    EditText inputChequeNo;
    @BindView(R.id.input_layout_cheque_no)
    TextInputLayout inputLayoutChequeNo;
    @BindView(R.id.input_amount)
    EditText inputAmount;
    @BindView(R.id.input_layout_amount)
    TextInputLayout inputLayoutAmount;
    @BindView(R.id.input_payment_date)
    EditText inputPaymentDate;
    @BindView(R.id.input_layout_payment_date)
    TextInputLayout inputLayoutPaymentDate;
    @BindView(R.id.payment_type)
    MaterialSpinner chequeTypeSpinner;
    @BindView(R.id.activity_add_cheque)
    NestedScrollView activityAddCheque;

    @BindView(R.id.input_company_name)
    AutoCompleteTextView inputCompanyName;
    @BindView(R.id.input_layout_company_name)
    TextInputLayout inputLayoutCompanyName;

    CustomDatePicker customerDatePicker;
    ArrayList<GeneralModel> chequeType = new ArrayList<>();
    GeneralAdapter chequeAdapter;
    MyPreferences myPreferences;
    String chequeTypeId = "";
    SearchReciverCompanyAdapter senderCompanyAdapter;

    String insert_update_flag = "", cheque_type = "";
    ChequeModel model;
    int position;
    @BindView(R.id.cheque_title)
    TextView chequeTitle;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    SearchBankAdapter adapter;
    @BindView(R.id.input_bank_name)
    AutoCompleteTextView inputBankName;
    @BindView(R.id.input_layout_bank)
    TextInputLayout inputLayoutBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cheque);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(AddChequeActivity.this);

        try {
            GlobalElements.editTextAllCaps(AddChequeActivity.this, mainLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new SearchBankAdapter(AddChequeActivity.this);
        inputBankName.setAdapter(adapter);

        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_flag = bundle.getString("type");
            cheque_type = bundle.getString("cheque_type");
            if (insert_update_flag.equals("1")) {
                getSupportActionBar().setTitle("Update Cheque");
                chequeTitle.setText("Update Cheque");
                position = bundle.getInt("position");
                model = (ChequeModel) bundle.getSerializable("data");
                inputCompanyName.setText("" + model.getCompany_name());
                inputPartyName.setText("" + model.getParty_name());
                inputChequeNo.setText("" + model.getCheque_no());
                inputAmount.setText("" + model.getAmount());
                inputPaymentDate.setText("" + model.getCheque_date());
                inputBankName.setText("" + model.getBank_name());
            }
        } catch (Exception e) {
            e.printStackTrace();
            insert_update_flag = "0";
        }

        inputCompanyName.addTextChangedListener(new MyTextWatcher(inputLayoutCompanyName));
        inputPartyName.addTextChangedListener(new MyTextWatcher(inputLayoutPartyname));
        inputChequeNo.addTextChangedListener(new MyTextWatcher(inputLayoutChequeNo));
        inputAmount.addTextChangedListener(new MyTextWatcher(inputLayoutAmount));
        inputBankName.addTextChangedListener(new MyTextWatcher(inputLayoutBank));

        inputPaymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerDatePicker = new CustomDatePicker(AddChequeActivity.this);
                customerDatePicker.setToDate(customerDatePicker.min, inputPaymentDate, "");
            }
        });

        senderCompanyAdapter = new SearchReciverCompanyAdapter(AddChequeActivity.this);
        inputCompanyName.setAdapter(senderCompanyAdapter);


        paymentTypeList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_cheque, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (inputBankName.getText().toString().equals("")) {
                    inputLayoutBank.setError("Please Enter Bank Name");
                } else if (chequeTypeSpinner.equals("")) {
                    chequeTypeSpinner.setError("Please Payment Type");
                } else if (!Validation.isValid(Validation.BLANK_CHECK, inputCompanyName.getText().toString())) {
                    inputLayoutCompanyName.setError("Enter Company Name");
                } else if (!Validation.isValid(Validation.BLANK_CHECK, inputPartyName.getText().toString())) {
                    inputLayoutPartyname.setError("Enter Party Name");
                } else if (!Validation.isValid(Validation.BLANK_CHECK, inputChequeNo.getText().toString())) {
                    inputLayoutChequeNo.setError("Enter Cheque No");
                } else if (!Validation.isValid(Validation.BLANK_CHECK, inputAmount.getText().toString())) {
                    inputLayoutAmount.setError("Enter Amount");
                } else if (!Validation.isValid(Validation.BLANK_CHECK, inputPaymentDate.getText().toString())) {
                    inputLayoutPaymentDate.setError("Select Cheque Date");
                } else {
                    if (GlobalElements.isConnectingToInternet(AddChequeActivity.this)) {
                        if (insert_update_flag.equals("0")) {
                            addCheque();
                        } else {
                            updateCheque();
                        }
                    } else {
                        GlobalElements.showDialog(AddChequeActivity.this);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void paymentTypeList() {
        try {
            chequeType.clear();
            GeneralModel da = new GeneralModel();
            da.setId("account_pay");
            da.setName("Account Pay");
            chequeType.add(da);
            da = new GeneralModel();
            da.setId("self");
            da.setName("self");
            chequeType.add(da);

            chequeAdapter = new GeneralAdapter(AddChequeActivity.this, chequeType);
            chequeTypeSpinner.setAdapter(chequeAdapter);
            chequeTypeSpinner.setHint("Payment Type");
            SpinnerInteractionListener_paymentType listener_1 = new SpinnerInteractionListener_paymentType();
            chequeTypeSpinner.setOnTouchListener(listener_1);
            chequeTypeSpinner.setOnItemSelectedListener(listener_1);

            if (insert_update_flag.equals("1")) {
                for (int i = 0; i < chequeType.size(); i++) {
                    if (chequeType.get(i).getId().equals("" + model.getCheque_type())) {
                        chequeTypeId = chequeType.get(i).getId();
                        chequeTypeSpinner.setSelection(i + 1);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bankList() {
        /*try {
            bankData.clear();
            GeneralModel da = new GeneralModel();
            da.setId("Federal Bank");
            da.setName("Federal Bank");
            bankData.add(da);
            da = new GeneralModel();
            da.setId("Karur Vysya Bank");
            da.setName("Karur Vysya Bank");
            bankData.add(da);
            da = new GeneralModel();
            da.setId("Centeral Bank Of India");
            da.setName("Centeral Bank Of India");
            bankData.add(da);
            da = new GeneralModel();
            da.setId("Andhra Bank");
            da.setName("Andhra Bank");
            bankData.add(da);
            da = new GeneralModel();
            da.setId("Kotak Mahidra Bank");
            da.setName("Kotak Mahidra Bank");
            bankData.add(da);

            bankAdapter = new GeneralAdapter(AddChequeActivity.this, bankData);
            bankSpinner.setAdapter(bankAdapter);
            bankSpinner.setHint("Select Bank");
            SpinnerInteractionListener_bank listener_1 = new SpinnerInteractionListener_bank();
            bankSpinner.setOnTouchListener(listener_1);
            bankSpinner.setOnItemSelectedListener(listener_1);

            if (insert_update_flag.equals("1")) {
                for (int i = 0; i < bankData.size(); i++) {
                    if (bankData.get(i).getId().equals("" + model.getBank_name())) {
                        bankId = bankData.get(i).getId();
                        bankSpinner.setSelection(i + 1);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public class SpinnerInteractionListener_paymentType implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    chequeTypeSpinner.setError(null);
                    chequeTypeId = chequeType.get(position).getId().toLowerCase();
                } catch (Exception e) {
                    e.printStackTrace();
                    chequeTypeId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void addCheque() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddChequeActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.addCheque(myPreferences.getPreferences(MyPreferences.id), inputBankName.getText().toString(), inputCompanyName.getText().toString(), inputChequeNo.getText().toString(), inputPartyName.getText().toString(), chequeTypeId, inputPaymentDate.getText().toString(), inputAmount.getText().toString(), cheque_type);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            Intent intent = new Intent();
                            intent.putExtra("cheque_type", cheque_type);
                            setResult(14, intent);
                            finish();
                        } else {
                            Toaster.show(AddChequeActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void updateCheque() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddChequeActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);

            Call<ResponseBody> call = request.updateCheque(myPreferences.getPreferences(MyPreferences.id), model.getId(), inputBankName.getText().toString(), inputCompanyName.getText().toString(), inputChequeNo.getText().toString(), inputPartyName.getText().toString(), chequeTypeId, inputPaymentDate.getText().toString(), inputAmount.getText().toString(), cheque_type);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {

                            JSONObject result = json.getJSONObject("result");
                            model.setCheque_no(result.getString("cheque_no"));
                            model.setCompany_name(result.getString("company_name"));
                            model.setBank_name(result.getString("bank_name"));
                            model.setParty_name(result.getString("party_name"));
                            model.setAmount(result.getString("amount"));
                            model.setCheque_date(result.getString("cheque_date"));
                            model.setCheque_type(result.getString("cheque_type"));

                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", position);
                            bundle.putString("cheque_type", cheque_type);
                            bundle.putSerializable("data", model);
                            intent.putExtras(bundle);
                            setResult(15, intent);
                            finish();
                        } else {
                            Toaster.show(AddChequeActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
