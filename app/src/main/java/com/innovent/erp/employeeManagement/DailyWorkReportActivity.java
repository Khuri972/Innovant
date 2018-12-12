package com.innovent.erp.employeeManagement;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.PathUtil;
import com.innovent.erp.employeeManagement.adapter.DailyServiceReportAdapter;
import com.innovent.erp.employeeManagement.adapter.DailyWorkReportAdapter;
import com.innovent.erp.employeeManagement.interFace.DeleteDailyReport;
import com.innovent.erp.employeeManagement.model.DailyFileAttachModel;
import com.innovent.erp.employeeManagement.model.DailyNoteModel;
import com.innovent.erp.employeeManagement.model.DailyServiceReportModel;
import com.innovent.erp.employeeManagement.model.DailyWorkReportModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyWorkReportActivity extends AppCompatActivity implements DeleteDailyReport {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add_report)
    FloatingActionButton addReport;

    MyPreferences myPreferences;
    ArrayList<DailyWorkReportModel> workReportModels = new ArrayList<>();
    DailyWorkReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_work_report);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daily Work Report");

        adapter = new DailyWorkReportAdapter(DailyWorkReportActivity.this, workReportModels, "");
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(DailyWorkReportActivity.this, LinearLayoutManager.VERTICAL, false));

        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(DailyWorkReportActivity.this, AddDailyWorkReport.class);
                    intent.putExtra("type", "0");
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (GlobalElements.isConnectingToInternet(DailyWorkReportActivity.this)) {
            getDailyWorkReport();
        } else {
            GlobalElements.showDialog(DailyWorkReportActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        if (resultCode == 10) {
            try {
                DailyWorkReportModel da = (DailyWorkReportModel) _data.getSerializableExtra("data");
                workReportModels.add(0, da);
                adapter.notifyDataSetChanged();
                if (workReportModels.isEmpty()) {
                    recycleView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                } else {
                    recycleView.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == 11) {
            try {
                int position = _data.getIntExtra("position", 0);
                DailyWorkReportModel da = (DailyWorkReportModel) _data.getSerializableExtra("data");
                workReportModels.set(position, da);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK) {
            Uri ur = _data.getData();
            try {
                String mimeType = getMimeType(ur);
                String PathHolder = PathUtil.getPath(DailyWorkReportActivity.this, ur);
                File file = new File(PathHolder);
                if (file != null) {
                    adapter.getFilePath(file, mimeType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = DailyWorkReportActivity.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void getDailyWorkReport() {
        try {
            final ProgressDialog pd = new ProgressDialog(DailyWorkReportActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getDailyWorkReport(myPreferences.getPreferences(MyPreferences.id));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        //getResult(json_response);
                        workReportModels.clear();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {

                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                DailyWorkReportModel da = new DailyWorkReportModel();
                                da.setId("" + c.getString("id"));
                                da.setAddress("" + c.getString("address"));
                                da.setWork_type("" + c.getString("work_type"));
                                da.setPerson_name("" + c.getString("person_name"));
                                da.setRemark("" + c.getString("remark"));
                                da.setIsSubmitted(c.getString("isSubmitted"));

                                JSONArray note = c.getJSONArray("notes");
                                ArrayList<DailyNoteModel> dailyNoteModels = new ArrayList<>();
                                for (int j = 0; j < note.length(); j++) {
                                    JSONObject n = note.getJSONObject(j);
                                    DailyNoteModel not = new DailyNoteModel();
                                    not.setId("" + n.getString("id"));
                                    not.setDate("" + n.getString("created_date"));
                                    not.setNote("" + n.getString("note"));
                                    dailyNoteModels.add(not);
                                }
                                da.setDailyNoteModels(dailyNoteModels);
                                ArrayList<DailyFileAttachModel> attachModels = new ArrayList<>();
                                JSONArray attachment = c.getJSONArray("attachment");
                                for (int a = 0; a < attachment.length(); a++) {
                                    JSONObject at = attachment.getJSONObject(a);
                                    DailyFileAttachModel attachModel = new DailyFileAttachModel();
                                    attachModel.setId("" + at.getString("id"));
                                    attachModel.setDate("" + at.getString("created_date"));
                                    if (!at.getString("file_path").equals("")) {
                                        File file = new File(at.getString("file_path"));
                                        attachModel.setFile_name("" + file.getName());
                                    } else {
                                        attachModel.setFile_name("");
                                    }
                                    attachModel.setFile_path("" + at.getString("file_path"));
                                    attachModels.add(attachModel);
                                }
                                da.setDailyFileAttachModels(attachModels);
                                workReportModels.add(da);
                            }

                            adapter = new DailyWorkReportAdapter(DailyWorkReportActivity.this, workReportModels, "");
                            recycleView.setAdapter(adapter);
                            recycleView.setLayoutManager(new LinearLayoutManager(DailyWorkReportActivity.this, LinearLayoutManager.VERTICAL, false));
                            recycleView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            recycleView.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyText.setText("" + json.getString("ack_msg"));
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

    @Override
    public void callbackCall() {
        if (workReportModels.isEmpty()) {
            recycleView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            recycleView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }
}
