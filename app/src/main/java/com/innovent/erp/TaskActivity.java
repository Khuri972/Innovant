package com.innovent.erp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.adapter.TaskPagerAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.fragment.TaskFragment;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.service.DownloadService;
import com.innovent.erp.tutoral.TapIntroHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {

    TaskPagerAdapter pagerAdapter;
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
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        GlobalElements.taskTitle = "";
        GlobalElements.taskAssignedById = "";
        GlobalElements.taskAssignedTo = "";
        GlobalElements.taskStatus = "";
        GlobalElements.task_type = "";
        GlobalElements.admintype = "1";
        GlobalElements.toDate = "";
        GlobalElements.fromeDate = "";
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        filter = (ImageView) toolbar.findViewById(R.id.filter);
        more = (ImageView) toolbar.findViewById(R.id.action_more);
        refresh = (ImageView) toolbar.findViewById(R.id.refresh);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(TaskActivity.this)) {
                    filterDialog();
                } else {
                    GlobalElements.showDialog(TaskActivity.this);
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(TaskActivity.this)) {
                    try {
                        LayoutInflater inflater = (LayoutInflater) TaskActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View customView = inflater.inflate(R.layout.layout_task_menu, null);
                        mPopupWindow = new PopupWindow(
                                customView,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT, true
                        );

                        mPopupWindow.setOutsideTouchable(true);
                        mPopupWindow.setFocusable(true);
                        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                    mPopupWindow.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });

                        // Call requires API level 21
                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5);
                        }

                        CheckBox actionCheck = (CheckBox) customView.findViewById(R.id.action_check);
                        TextView actionPdf = (TextView) customView.findViewById(R.id.action_pdf);
                        TextView actionExcel = (TextView) customView.findViewById(R.id.action_excel);
                        TextView actionForward = (TextView) customView.findViewById(R.id.action_forward);
                        TextView actionReceived = (TextView) customView.findViewById(R.id.action_received);
                        TextView actionOwned = (TextView) customView.findViewById(R.id.action_owned);
                        LinearLayout adminLayout = (LinearLayout) customView.findViewById(R.id.admin_layout);
                        TextView adminTxt = (TextView) customView.findViewById(R.id.admin_txt);
                        TextView allTxt = (TextView) customView.findViewById(R.id.all_txt);

                        if (GlobalElements.actionShowHide.equals("0")) {
                            actionCheck.setChecked(false);
                        } else {
                            actionCheck.setChecked(true);
                        }

                        if (myPreferences.getPreferences(MyPreferences.login_type).equals("0")) {
                            adminLayout.setVisibility(View.VISIBLE);
                        } else {
                            adminLayout.setVisibility(View.GONE);
                        }

                        actionCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) {
                                    GlobalElements.actionShowHide = "1";
                                } else {
                                    GlobalElements.actionShowHide = "0";
                                }
                                getTaskStatus();
                                mPopupWindow.dismiss();
                            }
                        });

                        actionPdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                exportTask("pdf");
                                mPopupWindow.dismiss();
                            }
                        });

                        actionExcel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                exportTask("excel");
                                mPopupWindow.dismiss();
                            }
                        });

                        adminTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GlobalElements.admintype = "0";
                                getTaskStatus();
                                mPopupWindow.dismiss();
                            }
                        });

                        allTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GlobalElements.admintype = "1";
                                getTaskStatus();
                                mPopupWindow.dismiss();
                            }
                        });

                        actionForward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GlobalElements.task_type = "forword";
                                getTaskStatus();
                                mPopupWindow.dismiss();
                            }
                        });

                        actionReceived.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GlobalElements.task_type = "received";
                                getTaskStatus();
                                mPopupWindow.dismiss();
                            }
                        });

                        actionOwned.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GlobalElements.task_type = "owned";
                                getTaskStatus();
                                mPopupWindow.dismiss();
                            }
                        });


                        mPopupWindow.showAtLocation(more, Gravity.RIGHT | Gravity.TOP, 30, 80);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    GlobalElements.showDialog(TaskActivity.this);
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(TaskActivity.this)) {
                    GlobalElements.taskTitle = "";
                    GlobalElements.taskAssignedById = "";
                    GlobalElements.taskAssignedTo = "";
                    GlobalElements.taskStatus = "";
                    GlobalElements.task_type = "";
                    GlobalElements.admintype = "1";
                    GlobalElements.toDate = "";
                    GlobalElements.fromeDate = "";
                    getTaskStatus();
                } else {
                    GlobalElements.showDialog(TaskActivity.this);
                }
            }
        });

        if (GlobalElements.isConnectingToInternet(TaskActivity.this)) {
            getTaskStatus();
        } else {
            GlobalElements.showDialog(TaskActivity.this);
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
                if (viewPagerFragment instanceof TaskFragment) {
                    TaskFragment oneFragment = (TaskFragment) viewPagerFragment;
                    if (oneFragment != null) {
                        oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                    }
                }
            }
        } else if (resultCode == RESULT_OK) {
            int vi = viewPager.getCurrentItem();
            Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, vi);
            if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                if (viewPagerFragment instanceof TaskFragment) {
                    TaskFragment oneFragment = (TaskFragment) viewPagerFragment;
                    if (oneFragment != null) {
                        oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                    }
                }
            }
        }
    }

    private void filterDialog() {
        try {
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View menuLayout = inflater.inflate(R.layout.dialog_filter_task, null);
            final AlertDialog.Builder dg = new AlertDialog.Builder(TaskActivity.this);
            dg.setView(menuLayout);
            final AlertDialog dialog = dg.create();

            ImageView close = (ImageView) menuLayout.findViewById(R.id.closebtn);
            Button clear = (Button) menuLayout.findViewById(R.id.clearbtn);
            Button submit = (Button) menuLayout.findViewById(R.id.submitBtn);

            final EditText taskName = (EditText) menuLayout.findViewById(R.id.input_task_name);
            fromdate = (EditText) menuLayout.findViewById(R.id.input_fromdate);
            todate = (EditText) menuLayout.findViewById(R.id.input_todate);
            MaterialSpinner spinnerTaskAssigned = (MaterialSpinner) menuLayout.findViewById(R.id.spinner_task_assigned);
            MaterialSpinner spinnerTaskAssignedTo = (MaterialSpinner) menuLayout.findViewById(R.id.spinner_task_assigned_to);
            MaterialSpinner spinnerTaskStatus = (MaterialSpinner) menuLayout.findViewById(R.id.spinner_task_status);

            getTaskUtils(spinnerTaskAssigned, spinnerTaskAssignedTo, spinnerTaskStatus);

            GlobalElements.taskTitle = "";
            GlobalElements.taskAssignedById = "";
            GlobalElements.taskAssignedTo = "";
            GlobalElements.taskStatus = "";
            GlobalElements.task_type = "";
            GlobalElements.admintype = "1";
            GlobalElements.toDate = "";
            GlobalElements.fromeDate = "";

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalElements.taskTitle = "";
                    GlobalElements.taskAssignedById = "";
                    GlobalElements.taskAssignedTo = "";
                    GlobalElements.taskStatus = "";
                    GlobalElements.task_type = "";
                    GlobalElements.admintype = "1";
                    GlobalElements.toDate = "";
                    GlobalElements.fromeDate = "";
                    if (GlobalElements.isConnectingToInternet(TaskActivity.this)) {
                        getTaskStatus();
                    } else {
                        GlobalElements.showDialog(TaskActivity.this);
                    }
                    dialog.dismiss();
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        GlobalElements.taskTitle = taskName.getText().toString();
                        if (!fromdate.getText().toString().equals("") && todate.getText().toString().equals("")) {
                            Toaster.show(TaskActivity.this, "Select to date", false, Toaster.DANGER);
                        } else if (!todate.getText().toString().equals("") && fromdate.getText().toString().equals("")) {
                            Toaster.show(TaskActivity.this, "Select  from date", false, Toaster.DANGER);
                        } else if (!fromdate.getText().toString().equals("") && !todate.getText().toString().equals("")) {
                            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                            DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date _to_date = inputFormat.parse("" + todate.getText().toString());
                            Date _from_date = inputFormat.parse("" + fromdate.getText().toString());
                            Date to_date = outputFormat.parse("" + outputFormat.format(_to_date));  //to date
                            Date frdate = outputFormat.parse("" + outputFormat.format(_from_date)); // from date

                            if (frdate.after(to_date)) {
                                Toaster.show(TaskActivity.this, "" + getResources().getString(R.string.from_date), false, Toaster.DANGER);
                            } else if (to_date.before(frdate)) {
                                Toaster.show(TaskActivity.this, "" + getResources().getString(R.string.to_date), false, Toaster.DANGER);
                            } else if (frdate.equals(to_date)) {
                                Toaster.show(TaskActivity.this, "" + getResources().getString(R.string.from_date_to_date_equals), false, Toaster.DANGER);
                            } else {
                                dialog.dismiss();
                                GlobalElements.toDate = todate.getText().toString();
                                GlobalElements.fromeDate = fromdate.getText().toString();
                                if (GlobalElements.isConnectingToInternet(TaskActivity.this)) {
                                    getTaskStatus();
                                } else {
                                    GlobalElements.showDialog(TaskActivity.this);
                                }
                            }
                        } else {
                            dialog.dismiss();
                            if (GlobalElements.isConnectingToInternet(TaskActivity.this)) {
                                GlobalElements.toDate = todate.getText().toString();
                                GlobalElements.fromeDate = fromdate.getText().toString();
                                getTaskStatus();
                            } else {
                                GlobalElements.showDialog(TaskActivity.this);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            fromdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFromDate();
                }
            });
            todate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setToDate();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFromDate() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.DATE, -8);
        fromDatePickerDialog = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
    }

    private void setToDate() {
        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                todate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.show();
    }

    public class SpinnerInteractionListener_task_assigned implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    GlobalElements.taskAssignedById = taskAssignedModels.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalElements.taskAssignedById = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_task_to implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    GlobalElements.taskAssignedTo = taskToModels.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalElements.taskAssignedTo = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_task_status implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
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
                    GlobalElements.taskStatus = data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalElements.taskStatus = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void getTaskUtils(final MaterialSpinner spinnerTaskAssigned, final MaterialSpinner spinnerTaskTo, final MaterialSpinner spinnerTaskStatus) {
        final ProgressDialog pd = new ProgressDialog(TaskActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.getTaskUtils(myPreferences.getPreferences(MyPreferences.id));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        JSONArray taskers = result.getJSONArray("taskers");

                        taskAssignedModels.clear();
                        GeneralModel da = new GeneralModel();
                        da.setId("");
                        da.setName("" + getResources().getString(R.string.task_assigned));
                        taskAssignedModels.add(da);
                        if (taskers.length() > 0) {
                            taskAssignedModels.clear();
                            for (int i = 0; i < taskers.length(); i++) {
                                JSONObject c = taskers.getJSONObject(i);
                                da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                taskAssignedModels.add(da);
                            }
                            taskAssignedAdapter = new GeneralAdapter(TaskActivity.this, taskAssignedModels);
                            spinnerTaskAssigned.setAdapter(taskAssignedAdapter);
                            spinnerTaskAssigned.setHint("" + getResources().getString(R.string.task_assigned));
                            SpinnerInteractionListener_task_assigned listener_1 = new SpinnerInteractionListener_task_assigned();
                            spinnerTaskAssigned.setOnTouchListener(listener_1);
                            spinnerTaskAssigned.setOnItemSelectedListener(listener_1);
                        } else {
                            taskAssignedAdapter = new GeneralAdapter(TaskActivity.this, taskAssignedModels);
                            spinnerTaskAssigned.setAdapter(taskAssignedAdapter);
                            spinnerTaskAssigned.setHint("" + getResources().getString(R.string.task_assigned));
                            SpinnerInteractionListener_task_assigned listener_1 = new SpinnerInteractionListener_task_assigned();
                            spinnerTaskAssigned.setOnTouchListener(listener_1);
                            spinnerTaskAssigned.setOnItemSelectedListener(listener_1);
                        }

                        if (taskers.length() > 0) {
                            taskToModels.clear();
                            for (int i = 0; i < taskers.length(); i++) {
                                JSONObject c = taskers.getJSONObject(i);
                                da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                taskToModels.add(da);
                            }
                            taskToAdapter = new GeneralAdapter(TaskActivity.this, taskToModels);
                            spinnerTaskTo.setAdapter(taskToAdapter);
                            spinnerTaskTo.setHint("" + getResources().getString(R.string.task_to));
                            SpinnerInteractionListener_task_to listener_1 = new SpinnerInteractionListener_task_to();
                            spinnerTaskTo.setOnTouchListener(listener_1);
                            spinnerTaskTo.setOnItemSelectedListener(listener_1);
                        } else {
                            taskToModels.clear();
                            taskToAdapter = new GeneralAdapter(TaskActivity.this, taskToModels);
                            spinnerTaskTo.setAdapter(taskToAdapter);
                            spinnerTaskTo.setHint("" + getResources().getString(R.string.task_to));
                            SpinnerInteractionListener_task_to listener_1 = new SpinnerInteractionListener_task_to();
                            spinnerTaskTo.setOnTouchListener(listener_1);
                            spinnerTaskTo.setOnItemSelectedListener(listener_1);
                        }

                        taskStatusAdapter = new GeneralAdapter(TaskActivity.this, data);
                        spinnerTaskStatus.setAdapter(taskStatusAdapter);
                        spinnerTaskStatus.setHint("Search By Status");
                        SpinnerInteractionListener_task_status listener_1 = new SpinnerInteractionListener_task_status();
                        spinnerTaskStatus.setOnTouchListener(listener_1);
                        spinnerTaskStatus.setOnItemSelectedListener(listener_1);

                    } else {
                        Toaster.show(TaskActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void getTaskStatus() {
        try {
            final ProgressDialog pd = new ProgressDialog(TaskActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getTaskStatus(myPreferences.getPreferences(MyPreferences.id));
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
                            pagerAdapter = new TaskPagerAdapter(TaskActivity.this.getSupportFragmentManager(), data);
                            viewPager.setAdapter(pagerAdapter);
                            pagerAdapter.notifyDataSetChanged();
                            tabLayout.setupWithViewPager(viewPager);
                        } else {
                            pagerAdapter = new TaskPagerAdapter(TaskActivity.this.getSupportFragmentManager(), data);
                            viewPager.setAdapter(pagerAdapter);
                            pagerAdapter.notifyDataSetChanged();
                            tabLayout.setupWithViewPager(viewPager);
                            Toaster.show(TaskActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }

                        try {
                            TapIntroHelper.showTaskIntro(TaskActivity.this, ContextCompat.getColor(TaskActivity.this, R.color.colorPrimary));
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private void exportTask(final String type) {
        try {
            final ProgressDialog pd = new ProgressDialog(TaskActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);

            Call<ResponseBody> call = request.exportTask(myPreferences.getPreferences(MyPreferences.id), "", GlobalElements.taskAssignedById, GlobalElements.taskAssignedTo, GlobalElements.taskTitle, GlobalElements.toDate, GlobalElements.fromeDate, GlobalElements.actionShowHide, GlobalElements.task_type, GlobalElements.admintype);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);

                        if (json.getInt("ack") == 1) {
                            try {
                                if (type.equals("pdf")) {
                                    Intent intent = new Intent(TaskActivity.this, DownloadService.class);
                                    intent.putExtra("file_url", json.getString("pdf"));
                                    startService(intent);
                                } else {
                                    Intent intent = new Intent(TaskActivity.this, DownloadService.class);
                                    intent.putExtra("file_url", json.getString("excel"));
                                    startService(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toaster.show(TaskActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
