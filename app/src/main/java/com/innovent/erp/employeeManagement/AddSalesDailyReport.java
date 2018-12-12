package com.innovent.erp.employeeManagement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.SearchReciverCompanyAdapter;
import com.innovent.erp.custom.PathUtil;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.employeeManagement.model.DailyFileAttachModel;
import com.innovent.erp.employeeManagement.model.DailyNoteModel;
import com.innovent.erp.employeeManagement.model.DailySalesReportModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSalesDailyReport extends AppCompatActivity {

    @BindView(R.id.input_company_name)
    AutoCompleteTextView companyName;
    @BindView(R.id.input_person_name)
    EditText personName;
    @BindView(R.id.input_address)
    EditText address;
    @BindView(R.id.input_discuss)
    EditText discuss;
    @BindView(R.id.input_note)
    EditText noteEdt;
    @BindView(R.id.file_name_txt)
    TextView fileNameTxt;
    @BindView(R.id.browse_txt)
    TextView browseTxt;
    @BindView(R.id.sales_order)
    TextView salesOrderTxt;
    @BindView(R.id.add_expense)
    TextView addExpenseTxt;
    @BindView(R.id.submit)
    TextView submitTxt;
    @BindView(R.id.save)
    TextView save;

    SearchReciverCompanyAdapter senderCompanyAdapter;
    File file = null;
    String mimeType;

    DailySalesReportModel reportModel = new DailySalesReportModel();
    MyPreferences myPreferences;
    String insert_update_flag = "0";
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_report);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Daily Report");
        myPreferences = new MyPreferences(this);


        Bundle bundle = getIntent().getExtras();
        insert_update_flag = bundle.getString("type");
        if (insert_update_flag.equals("1")) {
            position = bundle.getInt("position");
            reportModel = (DailySalesReportModel) bundle.getSerializable("data");
            companyName.setText("" + reportModel.getCompany_name());
            personName.setText("" + reportModel.getPerson_name());
            address.setText("" + reportModel.getAddress());
            discuss.setText("" + reportModel.getDiscuss());
            save.setText("Update");
        }

        senderCompanyAdapter = new SearchReciverCompanyAdapter(AddSalesDailyReport.this);
        companyName.setAdapter(senderCompanyAdapter);

        /* todo sear sender  */
        companyName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    StringBuilder add = new StringBuilder();
                    if (!senderCompanyAdapter.suggestions.get(arg2).getAddress().equals("")) {
                        add.append("" + senderCompanyAdapter.suggestions.get(arg2).getAddress()).append(",");
                    }

                    if (!senderCompanyAdapter.suggestions.get(arg2).getCountry().equals("")) {
                        add.append("" + senderCompanyAdapter.suggestions.get(arg2).getCountry()).append(",");
                    }

                    if (!senderCompanyAdapter.suggestions.get(arg2).getState().equals("")) {
                        add.append("" + senderCompanyAdapter.suggestions.get(arg2).getState()).append(",");
                    }

                    if (!senderCompanyAdapter.suggestions.get(arg2).getCity().equals("")) {
                        add.append("" + senderCompanyAdapter.suggestions.get(arg2).getCity()).append(",");
                    }

                    if (!senderCompanyAdapter.suggestions.get(arg2).getPincode().equals("")) {
                        add.append("" + senderCompanyAdapter.suggestions.get(arg2).getPincode()).append(",");
                    }

                    if (add.length() > 0) {
                        address.setText("" + GlobalElements.getRemoveLastComma(add.toString()));
                    }

                    InputMethodManager imm = (InputMethodManager) AddSalesDailyReport.this.getSystemService(AddSalesDailyReport.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(companyName.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        submitTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (GlobalElements.isConnectingToInternet(AddSalesDailyReport.this)) {
                        addSalesReport("1");
                    } else {
                        GlobalElements.showDialog(AddSalesDailyReport.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (GlobalElements.isConnectingToInternet(AddSalesDailyReport.this)) {
                        addSalesReport("0");
                    } else {
                        GlobalElements.showDialog(AddSalesDailyReport.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        addExpenseTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSalesDailyReport.this, AddExpanceActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri ur = data.getData();
            try {
                mimeType = getMimeType(ur);
                String PathHolder = PathUtil.getPath(AddSalesDailyReport.this, ur);
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
            ContentResolver cr = AddSalesDailyReport.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void addSalesReport(String isSubmit) {
        try {
            final ProgressDialog pd = new ProgressDialog(AddSalesDailyReport.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call;
            if (insert_update_flag.equals("0")) {
                call = request.addSalesReport(myPreferences.getPreferences(MyPreferences.id), companyName.getText().toString(), personName.getText().toString(), address.getText().toString(),
                        discuss.getText().toString(), isSubmit);
            } else {
                call = request.updateSalesReport(myPreferences.getPreferences(MyPreferences.id), companyName.getText().toString(), personName.getText().toString(), address.getText().toString(),
                        discuss.getText().toString(), isSubmit, reportModel.getId(), "edit");
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        //getResult(json_response);
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            if (insert_update_flag.equals("0")) {
                                JSONObject result = json.getJSONObject("result");
                                reportModel.setId("" + result.getString("id"));
                                reportModel.setCompany_name("" + result.getString("company_name"));
                                reportModel.setPerson_name("" + result.getString("person_name"));
                                reportModel.setDate("" + result.getString("created_date"));
                                reportModel.setAddress("" + result.getString("address"));
                                reportModel.setDiscuss("" + result.getString("remark"));
                                reportModel.setIsSubmitted(result.getString("isSubmitted"));

                                ArrayList<DailyNoteModel> dailyNoteModels = new ArrayList<DailyNoteModel>();
                                reportModel.setDailyNoteModels(dailyNoteModels);
                                ArrayList<DailyFileAttachModel> dailyFileAttachModels = new ArrayList<DailyFileAttachModel>();
                                reportModel.setDailyFileAttachModels(dailyFileAttachModels);

                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", reportModel);
                                intent.putExtras(bundle);
                                setResult(10, intent);
                                finish();
                            } else {
                                JSONObject result = json.getJSONObject("result");
                                reportModel.setCompany_name("" + result.getString("company_name"));
                                reportModel.setPerson_name("" + result.getString("person_name"));
                                reportModel.setDate("" + result.getString("created_date"));
                                reportModel.setAddress("" + result.getString("address"));
                                reportModel.setDiscuss("" + result.getString("remark"));
                                reportModel.setIsSubmitted(result.getString("isSubmitted"));

                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", position);
                                bundle.putSerializable("data", reportModel);
                                intent.putExtras(bundle);
                                setResult(11, intent);
                                finish();
                            }
                        } else {
                            Toaster.show(AddSalesDailyReport.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
