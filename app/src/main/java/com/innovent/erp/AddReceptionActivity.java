package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.innovent.erp.adapter.SearchCityAdapter;
import com.innovent.erp.adapter.SearchReciverCompanyAdapter;
import com.innovent.erp.adapter.SearchReciverPersonAdapter;
import com.innovent.erp.custom.CustomDateTimePickerDialog;
import com.innovent.erp.custom.MultiSelectionSpinner;
import com.innovent.erp.custom.MultiSelectionSpinnerTag;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.ReceptionModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReceptionActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, MultiSelectionSpinnerTag.OnMultipleItemsSelectedListenerTag {

    ArrayList<GeneralModel> tag_data = new ArrayList<>();
    ArrayList<GeneralModel> label_data = new ArrayList<>();

    StringBuilder builder = new StringBuilder();
    ArrayList<String> labelData = new ArrayList<>();
    ArrayList<String> tagData = new ArrayList<>();

    String lableId = "";
    String tagId = "";
    SearchReciverCompanyAdapter reciverCompanyAdapter;
    MyPreferences myPreferences;
    @BindView(R.id.radioSender)
    RadioButton radioSender;
    @BindView(R.id.radioReciver)
    RadioButton radioReciver;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.contact_tag)
    MultiSelectionSpinnerTag contactTag;
    @BindView(R.id.contact_label)
    MultiSelectionSpinner contactLabel;
    @BindView(R.id.appoment_date)
    EditText appomentDate;
    @BindView(R.id.name)
    AutoCompleteTextView name;
    @BindView(R.id.company_name)
    AutoCompleteTextView companyName;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.whome_to_meet)
    AutoCompleteTextView whomeToMeet;
    @BindView(R.id.desc)
    AutoCompleteTextView desc;
    @BindView(R.id.city_edt)
    AutoCompleteTextView city;
    @BindView(R.id.visitor_name_edt)
    AutoCompleteTextView visitorNameEdt;

    String receptionType = "1";
    CustomDateTimePickerDialog dateTimePickerDialog;
    SearchReciverCompanyAdapter senderCompanyAdapter;
    SearchReciverPersonAdapter senderPersonAdapter;
    SearchCityAdapter searchCityAdapter;

    ReceptionModel receptionModel = new ReceptionModel();
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.whome_to_meet_layout)
    TextInputLayout whomeToMeetLayout;
    @BindView(R.id.visitor_name_layout)
    TextInputLayout visitorNameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reception);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reception Entery");
        myPreferences = new MyPreferences(this);

        reciverCompanyAdapter = new SearchReciverCompanyAdapter(AddReceptionActivity.this);
        whomeToMeet.setAdapter(reciverCompanyAdapter);

        try {
            Intent intent = getIntent();
            receptionType = intent.getStringExtra("type");
            if (receptionType.equals("1")) {
                whomeToMeetLayout.setHint("Whome To meet");
                visitorNameLayout.setHint("Visitor Name");
            } else {
                whomeToMeetLayout.setHint("Whome To Call");
                visitorNameLayout.setHint("Caller Name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        senderCompanyAdapter = new SearchReciverCompanyAdapter(AddReceptionActivity.this);
        companyName.setAdapter(senderCompanyAdapter);

        senderPersonAdapter = new SearchReciverPersonAdapter(AddReceptionActivity.this);
        name.setAdapter(senderPersonAdapter);

        searchCityAdapter = new SearchCityAdapter(AddReceptionActivity.this);
        city.setAdapter(searchCityAdapter);

        companyName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    email.setText("" + senderCompanyAdapter.suggestions.get(arg2).getEmail());
                    mobile.setText("" + senderCompanyAdapter.suggestions.get(arg2).getMobile());
                    InputMethodManager imm = (InputMethodManager) AddReceptionActivity.this.getSystemService(AddReceptionActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(companyName.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    email.setText("" + senderPersonAdapter.suggestions.get(arg2).getEmail());
                    mobile.setText("" + senderPersonAdapter.suggestions.get(arg2).getMobile());
                    InputMethodManager imm = (InputMethodManager) AddReceptionActivity.this.getSystemService(AddReceptionActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        appomentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePickerDialog = new CustomDateTimePickerDialog(AddReceptionActivity.this);
                dateTimePickerDialog.showDateTimePicker(appomentDate, "");
            }
        });

        lable(contactLabel);
        Tag(contactTag);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reception_entry, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (GlobalElements.isConnectingToInternet(AddReceptionActivity.this)) {
                    //String[] datearray = appomentDate.getText().toString().split(" ");
                    //String date = datearray[0];
                    //String time = datearray[1];
                    addReceptionEntry("", "");
                } else {
                    GlobalElements.showDialog(AddReceptionActivity.this);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void lable(MultiSelectionSpinner multiSelectionSpinner) {
        try {
            label_data.clear();
            labelData.clear();
            GeneralModel da = new GeneralModel();
            da.setId("");
            da.setName("Select Label");
            label_data.add(da);
            labelData.add("Select Label");
            if (!myPreferences.getPreferences(MyPreferences.Label).equals("")) {
                JSONArray labelArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Label));
                for (int i = 0; i < labelArray.length(); i++) {
                    JSONObject c = labelArray.getJSONObject(i);
                    da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    label_data.add(da);
                    labelData.add(c.getString("name"));
                }
            }
            multiSelectionSpinner.setItems(labelData);
            multiSelectionSpinner.setListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Tag(MultiSelectionSpinnerTag multiSelectionSpinner) {
        try {
            tag_data.clear();
            tagData.clear();
            GeneralModel da = new GeneralModel();
            da.setId("");
            da.setName("Select Tag");
            tag_data.add(da);
            tagData.add("Select Tag");
            if (!myPreferences.getPreferences(MyPreferences.Tag).equals("")) {
                JSONArray tagArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Tag));
                for (int i = 0; i < tagArray.length(); i++) {
                    JSONObject c = tagArray.getJSONObject(i);
                    da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    tag_data.add(da);
                    tagData.add(c.getString("name"));
                }
            }

            multiSelectionSpinner.setItems(tagData);
            multiSelectionSpinner.setListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        lableId = "";
        builder.setLength(0);
        for (int i = 0; i < label_data.size(); i++) {
            String name = label_data.get(i).getName();
            for (int j = 0; j < strings.size(); j++) {
                if (name.equals("" + strings.get(j).toString())) {
                    if (!name.equals("Select Label")) {
                        builder.append(label_data.get(i).getId()).append(",");
                    }
                }
            }
        }
        lableId = "" + GlobalElements.getRemoveLastComma(builder.toString());
    }

    @Override
    public void selectedIndicesTag(List<Integer> indices) {

    }

    @Override
    public void selectedStringsTag(List<String> strings) {
        tagId = "";
        builder.setLength(0);
        for (int i = 0; i < tag_data.size(); i++) {
            String name = tag_data.get(i).getName();
            for (int j = 0; j < strings.size(); j++) {
                if (name.equals("" + strings.get(j).toString())) {
                    if (!name.equals("Select Tag")) {
                        builder.append(tag_data.get(i).getId()).append(",");
                    }
                }
            }
        }
        tagId = "" + GlobalElements.getRemoveLastComma(builder.toString());
    }

    private void addReceptionEntry(String date, String time) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddReceptionActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call;

            if (receptionType.equals("1")) {
                call = request.addReceptionEntery(myPreferences.getPreferences(MyPreferences.id), receptionType, tagId, lableId, title.getText().toString(), name.getText().toString(),
                        companyName.getText().toString(), mobile.getText().toString(), email.getText().toString(), whomeToMeet.getText().toString(), desc.getText().toString(), city.getText().toString(), visitorNameEdt.getText().toString());
            } else {
                call = request.addReceptionEntery1(myPreferences.getPreferences(MyPreferences.id), receptionType, tagId, lableId, title.getText().toString(), name.getText().toString(),
                        companyName.getText().toString(), mobile.getText().toString(), email.getText().toString(), whomeToMeet.getText().toString(), desc.getText().toString(), city.getText().toString(), visitorNameEdt.getText().toString());
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

                            receptionModel.setId("" + result.getString("id"));
                            receptionModel.setTag("");
                            receptionModel.setTag_slug("");
                            receptionModel.setLabel("" + result.getString("label_id"));
                            receptionModel.setLabel_slug("" + result.getString("label_slug"));
                            receptionModel.setTitle("" + result.getString("title"));
                            receptionModel.setPerson_name("" + result.getString("person_name"));
                            receptionModel.setCompany_name("" + result.getString("company_name"));
                            receptionModel.setMobile_no("" + result.getString("mobile_no"));
                            receptionModel.setEmail("" + result.getString("email"));

                            receptionModel.setDescription("" + result.getString("reception_detail"));
                            receptionModel.setCity("" + result.getString("city"));
                            if (receptionType.equals("1")) {
                                receptionModel.setWhome_to_meet("" + result.getString("whom_to_meet"));
                                receptionModel.setName("" + result.getString("visitor_name"));
                            } else {
                                receptionModel.setWhome_to_meet("" + result.getString("whom_to_call"));
                                receptionModel.setName("" + result.getString("caller_name"));
                            }
                            receptionModel.setDate("");
                            receptionModel.setType("" + result.getString("type"));

                            if (receptionType.equals("1")) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", receptionModel);
                                intent.putExtras(bundle);
                                setResult(11, intent);
                                finish();
                            } else {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", receptionModel);
                                intent.putExtras(bundle);
                                setResult(12, intent);
                                finish();
                            }

                        } else {
                            Toaster.show(AddReceptionActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
