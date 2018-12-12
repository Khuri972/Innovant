package com.innovent.erp.ErpModule.sales_management;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.innovent.erp.ErpModule.sales_management.fragment.MyPipelineFragment;
import com.innovent.erp.ErpModule.sales_management.pageAdapter.MyPipelinePagerAdapter;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.TaskActivity;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.adapter.TaskPagerAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.fragment.TaskFragment;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.tutoral.TapIntroHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPipelineActivity extends AppCompatActivity {

    MyPipelinePagerAdapter pagerAdapter;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    public ViewPager viewPager;
    public ArrayList<GeneralModel> data = new ArrayList<>();
    MyPreferences myPreferences;

    ArrayList<GeneralModel> taskAssignedModels = new ArrayList<>();
    ArrayList<GeneralModel> taskToModels = new ArrayList<>();

    GeneralAdapter taskAssignedAdapter;
    GeneralAdapter taskToAdapter;
    GeneralAdapter taskStatusAdapter;

    EditText todate;
    EditText fromdate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private PopupWindow mPopupWindow;
    ImageView filter, more, refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pipeline);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        filter = (ImageView) toolbar.findViewById(R.id.filter);
        more = (ImageView) toolbar.findViewById(R.id.action_more);
        refresh = (ImageView) toolbar.findViewById(R.id.refresh);


        if (GlobalElements.isConnectingToInternet(MyPipelineActivity.this)) {
            getTaskStatus();
        } else {
            GlobalElements.showDialog(MyPipelineActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_activity, menu);
        return super.onCreateOptionsMenu(menu);
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
        if (resultCode == 18 || resultCode == 19) {
            int vi = viewPager.getCurrentItem();
            Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, vi);
            if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                if (viewPagerFragment instanceof MyPipelineFragment) {
                    MyPipelineFragment oneFragment = (MyPipelineFragment) viewPagerFragment;
                    if (oneFragment != null) {
                        oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                    }
                }
            }
        } else if (resultCode == 20) {
            if (GlobalElements.isConnectingToInternet(MyPipelineActivity.this)) {
                getTaskStatus();
            } else {
                GlobalElements.showDialog(MyPipelineActivity.this);
            }
        } else if (resultCode == RESULT_OK) {
            int vi = viewPager.getCurrentItem();
            Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, vi);
            if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                if (viewPagerFragment instanceof MyPipelineFragment) {
                    MyPipelineFragment oneFragment = (MyPipelineFragment) viewPagerFragment;
                    if (oneFragment != null) {
                        oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                    }
                }
            }
        }
    }

    private void getTaskStatus() {
        try {
            final ProgressDialog pd = new ProgressDialog(MyPipelineActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.getMyPipelineStatus(myPreferences.getPreferences(MyPreferences.id));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        data.clear();
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                da.setSlug("" + c.getString("slug"));
                                data.add(da);
                            }
                            pagerAdapter = new MyPipelinePagerAdapter(MyPipelineActivity.this.getSupportFragmentManager(), data);
                            viewPager.setAdapter(pagerAdapter);
                            pagerAdapter.notifyDataSetChanged();
                            tabLayout.setupWithViewPager(viewPager);
                        } else {
                            pagerAdapter = new MyPipelinePagerAdapter(MyPipelineActivity.this.getSupportFragmentManager(), data);
                            viewPager.setAdapter(pagerAdapter);
                            pagerAdapter.notifyDataSetChanged();
                            tabLayout.setupWithViewPager(viewPager);
                            Toaster.show(MyPipelineActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }

                        /*try {
                            TapIntroHelper.showTaskIntro(MyPipelineActivity.this, ContextCompat.getColor(MyPipelineActivity.this, R.color.colorPrimary));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
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
