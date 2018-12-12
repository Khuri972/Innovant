package com.innovent.erp.helpDesk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.helpDesk.adapter.DemoSerialNoAdapter;
import com.innovent.erp.helpDesk.adapter.PipeLineDisplayAdapter;
import com.innovent.erp.helpDesk.model.SerialNoModel;
import com.innovent.erp.model.TaskModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.service.DownloadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PipelineTaskView extends AppCompatActivity {


    @BindView(R.id.toolbar_shadow)
    View toolbarShadow;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.narration_edt)
    EditText narrationEdt;
    @BindView(R.id.terms_edt)
    EditText termsEdt;
    @BindView(R.id.nested)
    NestedScrollView nested;

    String id = "", activityType = "";
    PipeLineDisplayAdapter adapter;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.attachment_layout)
    LinearLayout attachmentLayout;
    @BindView(R.id.attachment_card_view)
    CardView attachmentCardView;
    @BindView(R.id.request)
    TextView request;
    @BindView(R.id.request_date)
    TextView requestDate;
    @BindView(R.id.deadline_date)
    TextView deadlineDate;
    @BindView(R.id.request_status)
    TextView requestStatus;
    @BindView(R.id.total_tasker)
    TextView totalTasker;
    @BindView(R.id.Duration)
    TextView Duration;
    @BindView(R.id.customer_name)
    TextView customerName;
    @BindView(R.id.customer_address)
    TextView customerAddress;
    @BindView(R.id.customer_mobile)
    TextView customerMobile;
    @BindView(R.id.customer_email)
    TextView customerEmail;
    @BindView(R.id.customer_city)
    TextView customerCity;
    @BindView(R.id.customer_pincode)
    TextView customerPincode;
    @BindView(R.id.customer_state)
    TextView customerState;
    @BindView(R.id.customer_country)
    TextView customerCountry;
    @BindView(R.id.dealer_name)
    TextView dealerName;
    @BindView(R.id.dealer_address)
    TextView dealerAddress;
    @BindView(R.id.dealer_mobile)
    TextView dealerMobile;
    @BindView(R.id.dealer_email)
    TextView dealerEmail;
    @BindView(R.id.dealer_city)
    TextView dealerCity;
    @BindView(R.id.dealer_pincode)
    TextView dealerPincode;
    @BindView(R.id.dealer_state)
    TextView dealerState;
    @BindView(R.id.dealer_country)
    TextView dealerCountry;
    private ArrayList<SerialNoModel> data = new ArrayList<>();
    MyPreferences myPreferences;


    TaskModel taskModel = new TaskModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipeline_task_view);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycleview.setNestedScrollingEnabled(false);
        myPreferences = new MyPreferences(this);
        GlobalElements.editTextAllCaps(PipelineTaskView.this, mainLayout);

        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            activityType = intent.getStringExtra("activity_type");
            taskModel = (TaskModel) intent.getSerializableExtra("data");
            getSupportActionBar().setTitle("" + taskModel.getTask_no());

            if (taskModel.getAttachment().equals("0")) {
                attachmentCardView.setVisibility(View.GONE);
            } else {
                attachmentCardView.setVisibility(View.GONE);
                try {
                    LayoutInflater layoutInflater = (LayoutInflater) PipelineTaskView.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (taskModel.getAttachmentModels().size() > 0) {
                        attachmentLayout.removeAllViews();
                        for (int j = 0; j < taskModel.getAttachmentModels().size(); j++) {
                            final View addView = layoutInflater.inflate(R.layout.list_sales_report_attachment, null);
                            TextView description = (TextView) addView.findViewById(R.id.description);
                            final ImageView img = (ImageView) addView.findViewById(R.id.attachment_img);
                            TextView created_date = (TextView) addView.findViewById(R.id.created_date);
                            description.setText("" + taskModel.getAttachmentModels().get(j).getFile_name());
                            created_date.setText("" + taskModel.getAttachmentModels().get(j).getCreated_date());
                            img.setTag("" + j);
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        Intent intent = new Intent(PipelineTaskView.this, DownloadService.class);
                                        int a = 0;
                                        a = Integer.parseInt("" + img.getTag());
                                        intent.putExtra("file_url", "" + taskModel.getAttachmentModels().get(a).getFile_path());
                                        PipelineTaskView.this.startService(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            attachmentLayout.addView(addView);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new PipeLineDisplayAdapter(PipelineTaskView.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(PipelineTaskView.this, LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(PipelineTaskView.this)) {
            ViewOrder();
        } else {
            GlobalElements.showDialog(PipelineTaskView.this);
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

    private void ViewOrder() {
        final ProgressDialog pd = new ProgressDialog(PipelineTaskView.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.getPipelineViewTask(id, myPreferences.getPreferences(MyPreferences.id));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    data.clear();
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");

                        request.setText("Request : " + result.getString("request_no"));
                        requestDate.setText("Request Date : " + result.getString("request_date_format"));
                        deadlineDate.setText("Deadline Date : " + result.getString("deadline_date_format"));
                        requestStatus.setText("Request Status : " + result.getString("status_name"));
                        totalTasker.setText("Total Taskers : " + " -- ");
                        Duration.setText("Duration : " + " -- ");

                        customerName.setText("Name : " + result.getString("customer_name"));
                        customerAddress.setText("Address : " + result.getString("customer_address_1"));
                        customerEmail.setText("Email : " + result.getString("customer_email"));
                        customerMobile.setText("Mobile : " + result.getString("customer_phone_1"));
                        customerCity.setText("City : " + result.getString("customer_city"));
                        customerState.setText("State : " + result.getString("customer_state"));
                        customerCountry.setText("Country : " + result.getString("customer_country"));

                        dealerName.setText("Name : " + result.getString("dealer_name"));
                        dealerAddress.setText("Address : ");
                        dealerEmail.setText("Email : " + result.getString("dealer_email"));
                        dealerMobile.setText("Mobile : " + result.getString("dealer_mobile"));
                        dealerCity.setText("City : " + result.getString("dealer_city"));
                        dealerState.setText("State : " + result.getString("dealer_state"));
                        dealerCountry.setText("Country : " + result.getString("dealer_country"));

                        narrationEdt.setText("" + result.getString("narration"));
                        termsEdt.setText("" + result.getString("terms_condition"));

                        JSONArray items = result.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject c = items.getJSONObject(i);
                            SerialNoModel da = new SerialNoModel();
                            da.setId("" + c.getString("id"));
                            da.setName(c.getString("name"));
                            da.setBrand(c.getString("brand_name"));
                            da.setModel(c.getString("model_name"));
                            da.setColor(c.getString("color_name"));
                            da.setSerialNo(c.getString("batch_no"));
                            da.setPurchaseDate("" + c.getString("purchase_date_format"));
                            da.setParts_gaurantee("" + c.getString("part_guarantee"));
                            da.setFull_body_gaurantee("" + c.getString("full_guarantee"));
                            data.add(da);
                        }
                        adapter = new PipeLineDisplayAdapter(PipelineTaskView.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(PipelineTaskView.this, LinearLayoutManager.VERTICAL, false));
                        recycleview.setVisibility(View.VISIBLE);
                        nested.setVisibility(View.VISIBLE);

                    } else {
                        adapter = new PipeLineDisplayAdapter(PipelineTaskView.this, data);
                        recycleview.setAdapter(adapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(PipelineTaskView.this, LinearLayoutManager.VERTICAL, false));
                        recycleview.setVisibility(View.VISIBLE);
                        nested.setVisibility(View.VISIBLE);
                        Toaster.show(PipelineTaskView.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }

        });
    }
}
