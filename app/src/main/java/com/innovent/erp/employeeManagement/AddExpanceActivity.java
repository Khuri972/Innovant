package com.innovent.erp.employeeManagement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.PathUtil;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.employeeManagement.model.ExpanceModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class AddExpanceActivity extends AppCompatActivity {


    @BindView(R.id.category_spin)
    MaterialSpinner categorySpin;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.layout_amount)
    TextInputLayout layoutAmount;
    @BindView(R.id.remark_edt)
    EditText remarkEdt;
    @BindView(R.id.layout_Remark)
    TextInputLayout layoutRemark;
    @BindView(R.id.note_edt)
    EditText noteEdt;
    @BindView(R.id.layout_note)
    TextInputLayout layoutNote;
    @BindView(R.id.date_edt)
    EditText dateEdt;
    @BindView(R.id.layout_date)
    TextInputLayout layoutDate;
    @BindView(R.id.file_name_txt)
    TextView fileNameTxt;
    @BindView(R.id.browse_txt)
    TextView browseTxt;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.activity_add_cheque)
    NestedScrollView activityAddCheque;

    File file = null;
    String mimeType, category_id = "2";
    CustomDatePicker customerDatePicker;
    MyPreferences myPreferences;

    ExpanceModel data = new ExpanceModel();
    @BindView(R.id.save_txt)
    TextView saveTxt;
    @BindView(R.id.submit_txt)
    TextView submitTxt;

    ArrayList<GeneralModel> cagegoryData = new ArrayList<>();
    GeneralAdapter categoryAdapter;

    String insert_update_flag = "";
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expance);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Expence");
        myPreferences = new MyPreferences(AddExpanceActivity.this);

        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_flag = bundle.getString("type");
            if (!insert_update_flag.equals("0")) {
                saveTxt.setVisibility(View.GONE);
                data = (ExpanceModel) bundle.getSerializable("data");
                position = bundle.getInt("position");

                amount.setEnabled(false);
                remarkEdt.setEnabled(false);
                noteEdt.setEnabled(false);
                dateEdt.setEnabled(false);
                browseTxt.setEnabled(false);
                categorySpin.setEnabled(false);

                amount.setText("" + data.getAmount());
                remarkEdt.setText("" + data.getRemark());
                noteEdt.setText("" + data.getNote());
                dateEdt.setText("" + data.getDate());
                fileNameTxt.setText("" + data.getAttachment_name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getExpanceCategory();

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutDate.setErrorEnabled(false);
                customerDatePicker = new CustomDatePicker(AddExpanceActivity.this);
                customerDatePicker.setToDate(customerDatePicker.max, dateEdt, "");
            }
        });

        browseTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    startActivityForResult(intent, 7);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Validation.isValid(Validation.BLANK_CHECK, amount.getText().toString())) {
                    layoutAmount.setError("Enter Amount");
                } else {
                    addExpance("0");
                }
            }
        });

        submitTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.BLANK_CHECK, amount.getText().toString())) {
                    layoutAmount.setError("Enter Amount");
                } else {
                    if (insert_update_flag.equals("0")) {
                        addExpance("1");
                    } else {
                        updateExpanceStatus("1");
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri ur = data.getData();
            try {
                mimeType = getMimeType(ur);
                String PathHolder = PathUtil.getPath(AddExpanceActivity.this, ur);
                file = new File(PathHolder);
                if (file != null) {
                    //uploadFile();s
                    fileNameTxt.setText("" + file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = AddExpanceActivity.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void getExpanceCategory() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddExpanceActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getExpanceCategory();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        cagegoryData.clear();
                        if (json.getInt("ack") == 1) {

                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                cagegoryData.add(da);
                            }
                            categoryAdapter = new GeneralAdapter(AddExpanceActivity.this, cagegoryData);
                            categorySpin.setAdapter(categoryAdapter);
                            categorySpin.setHint("Select Category");
                            SpinnerInteractionListener_tag listener_1 = new SpinnerInteractionListener_tag();
                            categorySpin.setOnTouchListener(listener_1);
                            categorySpin.setOnItemSelectedListener(listener_1);

                            if (!insert_update_flag.equals("0")) {
                                for (int i = 0; i < cagegoryData.size(); i++) {
                                    if (cagegoryData.get(i).getId().equals("" + data.getCategory_id())) {
                                        categorySpin.setSelection(i + 1);
                                        break;
                                    }
                                }
                            }

                        } else {
                            Toaster.show(AddExpanceActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
                    category_id = cagegoryData.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    category_id = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void addExpance(String type) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddExpanceActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            RequestBody requestfile_image;
            MultipartBody.Part body_image;
            Call<ResponseBody> call;
            if (file == null) {
                call = request.addExpance(myPreferences.getPreferences(MyPreferences.id), category_id, amount.getText().toString(), remarkEdt.getText().toString(), noteEdt.getText().toString(), type, dateEdt.getText().toString());
            } else {
                requestfile_image = RequestBody.create(MediaType.parse("image/*"), file);
                body_image = MultipartBody.Part.createFormData("file_path", file.getName(), requestfile_image);
                call = request.addExpance(myPreferences.getPreferences(MyPreferences.id), category_id, amount.getText().toString(), remarkEdt.getText().toString(), noteEdt.getText().toString(), type, dateEdt.getText().toString(), body_image);
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

                            try {
                                data.setId("" + result.getString("id"));
                                data.setCategory_id("" + result.getString("category_id"));
                                data.setCategory_slug("" + result.getString("category_name"));
                                data.setAmount(result.getString("amount"));
                                data.setRemark(result.getString("remark"));
                                data.setNote(result.getString("note"));
                                data.setDate(result.getString("expense_date"));
                                data.setExpense_type(result.getString("type"));
                                if (result.getString("file_path").equals("")) {
                                    data.setAttachment_name("");
                                    data.setAttachment_path("");
                                } else {
                                    File file = new File(result.getString("file_path"));
                                    data.setAttachment_name(file.getName());
                                    data.setAttachment_path(result.getString("file_path"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", data);
                            intent.putExtras(bundle);
                            setResult(10, intent);
                            finish();
                        } else {
                            Toaster.show(AddExpanceActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void updateExpanceStatus(String type) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddExpanceActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.updateExpanceStatus(myPreferences.getPreferences(MyPreferences.id), "" + data.getId(), "1");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", position);
                            intent.putExtras(bundle);
                            setResult(12, intent);
                            finish();
                        } else {
                            Toaster.show(AddExpanceActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
