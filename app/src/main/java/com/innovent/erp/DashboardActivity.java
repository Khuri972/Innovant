package com.innovent.erp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.calculator.CalCulateActivity;
import com.innovent.erp.ErpModule.sales_management.MyPipelineActivity;
import com.innovent.erp.ErpModule.sales_management.QuatationRequest;
import com.innovent.erp.ErpModule.sales_management.SalesOrder;
import com.innovent.erp.ErpModule.sales_management.SalesReturn;
import com.innovent.erp.adapter.DashboardAdapter;
import com.innovent.erp.adapter.NavigationItemAdapter;
import com.innovent.erp.custom.GPSTracker;
import com.innovent.erp.custom.SpacesItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.dialog.ChangeToAliasDialog;
import com.innovent.erp.dialog.NoteCirculeDialog;
import com.innovent.erp.employeeManagement.DailySalesReportActivity;
import com.innovent.erp.employeeManagement.DailyServiceReportActivity;
import com.innovent.erp.employeeManagement.DailyWorkReportActivity;
import com.innovent.erp.employeeManagement.ExpanceActivity;
import com.innovent.erp.employeeManagement.LeaveRequestActivity;
import com.innovent.erp.firabse.MyFirebaseMessagingService;
import com.innovent.erp.helpDesk.ComplantHistoryActivity;
import com.innovent.erp.helpDesk.DemoHistoryActivity;
import com.innovent.erp.helpDesk.ServiceInvoiceActivity;
import com.innovent.erp.model.DashboardModel;
import com.innovent.erp.model.NavigationItemModel;
import com.innovent.erp.model.ViewpagerModel;
import com.innovent.erp.music.MainCategoryActivity;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.schedualReminder.SchedualReminderActivity;
import com.innovent.erp.service.FiveMinuteAlarmReceiver;
import com.innovent.erp.service.HourAlarmReceiver;
import com.innovent.erp.service.LocationService;
import com.innovent.erp.tutoral.TapIntroHelper;
import com.innovent.erp.visitorBookModual.VisitorBookActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DashboardAdapter.intercommunication, NavigationItemAdapter.intercommunication {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_recycleview)
    RecyclerView navRecycleview;

    DashboardAdapter adapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<DashboardModel> data = new ArrayList<>();
    ArrayList<NavigationItemModel> navData = new ArrayList<>();
    NavigationItemAdapter itemAdapter;
    MyPreferences myPreferences;
    DBHelper db;
    android.app.AlertDialog buildInfosDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        db = new DBHelper(this);
        myPreferences = new MyPreferences(DashboardActivity.this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        View v = navigationView.getHeaderView(0);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView email = (TextView) v.findViewById(R.id.email);
        TextView mobile = (TextView) v.findViewById(R.id.mobile);
        name.setText("" + myPreferences.getPreferences(MyPreferences.name));
        email.setText("" + myPreferences.getPreferences(MyPreferences.email));
        mobile.setText("" + myPreferences.getPreferences(MyPreferences.mobile_no));
        navigationView.setNavigationItemSelectedListener(this);

        if (myPreferences.getPreferences(MyPreferences.login_type).equals("0"))  // admin login
        {
            myPreferences.setPreferences(MyPreferences.Execuvive_in_max, "23:59");
            myPreferences.setPreferences(MyPreferences.Execuvive_in_min, "01:00");
            myPreferences.setPreferences(MyPreferences.Execuvive_out, "23:59");
            myPreferences.setPreferences(MyPreferences.UDATE, GlobalElements.getCurrentdate());
            try {
                int i2 = 300;//= 3600
                Intent intent2 = new Intent(DashboardActivity.this, FiveMinuteAlarmReceiver.class);
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                        DashboardActivity.this.getApplicationContext(), 59833212, intent2, 0);
                AlarmManager alarmManager2 = (AlarmManager) DashboardActivity.this.getSystemService(ALARM_SERVICE);
                alarmManager2.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i2 * 1000), pendingIntent2);

                int i1 = 300;//= 3600
                Intent intent = new Intent(DashboardActivity.this, HourAlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this.getApplicationContext(), 234324245, intent, 0);
                AlarmManager alarmManager = (AlarmManager) DashboardActivity.this.getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i1 * 1000), pendingIntent);
                Intent i = new Intent(DashboardActivity.this, LocationService.class);
                startService(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (GlobalElements.isConnectingToInternet(DashboardActivity.this)) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "cdms");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            startService(new Intent(DashboardActivity.this, MyFirebaseMessagingService.class));
        }

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DashboardActivity.this, 3);
        int spanCount = 3; // 3 columns
        int spacing = 3; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
        adapter = new DashboardAdapter(DashboardActivity.this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        LoadTiles();
        LoadNavigationBar();

        try {
            Drawable drawable = ContextCompat.getDrawable(DashboardActivity.this, R.drawable.attendence);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));

            drawable = ContextCompat.getDrawable(DashboardActivity.this, R.drawable.notification_list);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!myPreferences.getPreferences(MyPreferences.Execuvive_out).equals("")) {
                if (LogoutTime(myPreferences.getPreferences(MyPreferences.Execuvive_out))) {
                    myPreferences.setPreferences(MyPreferences.UDATE, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GlobalElements.isConnectingToInternet(DashboardActivity.this)) {
            GetBanner("banner");
        } else {
            GlobalElements.showDialog(DashboardActivity.this);
        }

        try {
            //TapIntroHelper.showDashboardIntro(this, ContextCompat.getColor(DashboardActivity.this, R.color.colorPrimary));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (GlobalElements.viewpagerModels.isEmpty()) {
                //getNotice_circule();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        MenuItem item = menu.findItem(R.id.action_alias);
        if (myPreferences.getaliasPreferences(MyPreferences.change_to_alias).equals("1")) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_notification) {
            if (GlobalElements.compareDate(myPreferences.getPreferences(MyPreferences.UDATE), "dd-MM-yyyy")) {
                if (GlobalElements.isConnectingToInternet(DashboardActivity.this)) {
                    Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                } else {
                    GlobalElements.showDialog(DashboardActivity.this);
                }
            } else {
                Toaster.show(DashboardActivity.this, "" + DashboardActivity.this.getResources().getString(R.string.attendance), true, Toaster.DANGER);
            }
        } else if (id == R.id.action_alias) {
            try {
                ChangeToAliasDialog aliasDialog = ChangeToAliasDialog.newInstance(DashboardActivity.this);
                aliasDialog.show(getSupportFragmentManager(), "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static boolean AttendanceTime(String start_time, String endtimes) {
        try {
            // current time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String ctime = dateFormat.format(date);
            Date startime = dateFormat.parse(start_time);
            Date endtime = dateFormat.parse(endtimes);
            Date current_time = dateFormat.parse(ctime);
            if (current_time.after(startime) && current_time.before(endtime)) {
                System.out.println("Yes");
                return true;
            } else {
                System.out.println("No");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean LogoutTime(String start_time) {
        try {
            // current time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String ctime = dateFormat.format(date);
            Date startime = dateFormat.parse(start_time);
            Date current_time = dateFormat.parse(ctime);
            if (current_time.after(startime)) {
                System.out.println("Yes");
                return true;
            } else {
                System.out.println("No");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void LoadTiles() {

        /*
         * view
         * insert
         * delete
         * update
         * */

        DashboardModel da;
        data.clear();
        if (GlobalElements.compareDate(myPreferences.getPreferences(MyPreferences.UDATE), "dd-MM-yyyy")) {
            da = new DashboardModel();
            da.setId("01");
            da.setName("Attendance");
            da.setImage_path(R.drawable.attendence);
            da.setTarget(ContactHistory.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        } else {
            da = new DashboardModel();
            da.setId("00");
            da.setName("Attendance");
            da.setImage_path(R.drawable.attendence);
            da.setTarget(ContactHistory.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.red));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.task_view).equals("1")) {
            da = new DashboardModel();
            da.setId("4");
            da.setName("My Task");
            da.setImage_path(R.drawable.task);
            da.setTarget(TaskActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.todayTask_view).equals("1")) {
            da = new DashboardModel();
            da.setId("5");
            da.setName("Todays task");
            da.setImage_path(R.drawable.task);
            da.setTarget(TaskActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        }

        da = new DashboardModel();
        da.setId("8"); //9
        da.setName("Schedule");
        da.setImage_path(R.drawable.schedual);
        da.setTarget(SchedualReminderActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("01"); //9
        da.setName("Circular");
        da.setImage_path(R.drawable.circlar);
        da.setTarget(SchedualReminderActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);


        da = new DashboardModel();
        da.setId("01");
        da.setName("Doc");
        da.setImage_path(R.drawable.doc);
        da.setTarget(TaskActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("01");
        da.setName("Sms");
        da.setImage_path(R.drawable.sms);
        da.setTarget(TaskActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);


        if (myPreferences.getPreferences(MyPreferences.contact_view).equals("1")) {
            da = new DashboardModel();
            da.setId("1");
            da.setName("Contact");
            da.setImage_path(R.drawable.contact);
            da.setTarget(ContactHistory.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        }

        da = new DashboardModel();
        da.setId("9");
        da.setName("Reception");
        da.setImage_path(R.drawable.reception);
        da.setTarget(ReceptionActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("01");
        da.setName("Recruitment");
        da.setImage_path(R.drawable.recrutment);
        da.setTarget(ReceptionActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        if (myPreferences.getPreferences(MyPreferences.couriers_view).equals("1")) {
            da = new DashboardModel();
            da.setId("3");
            da.setName("Courier");
            da.setImage_path(R.drawable.couriers);
            da.setTarget(CourierActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.document_view).equals("1")) {
            da = new DashboardModel();
            da.setId("2");
            da.setName("Drive");
            da.setImage_path(R.drawable.driver);
            da.setTarget(DocumentActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        }

        /*da = new DashboardModel();
        da.setId("12");
        da.setName("Music");
        da.setImage_path(R.drawable.music);
        da.setTarget(MainCategoryActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);*/

        if (myPreferences.getPreferences(MyPreferences.cheque_view).equals("1")) {
            da = new DashboardModel();
            da.setId("6");
            da.setName("Cheque");
            da.setImage_path(R.drawable.cheque);
            da.setTarget(ChequeHistoryActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.INSERT_FLAG).equals("1") || myPreferences.getPreferences(MyPreferences.VIEW_FLAG).equals("1") || myPreferences.getPreferences(MyPreferences.followup).equals("1")) {
            da = new DashboardModel();
            da.setId("7");
            da.setName("Inquiry");
            da.setImage_path(R.drawable.document);
            da.setTarget(VisitorBookActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            data.add(da);
        }

        da = new DashboardModel();
        da.setId("01");
        da.setName("Survey");
        da.setImage_path(R.drawable.servey);
        da.setTarget(VisitorBookActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("01");
        da.setName("File Locator");
        da.setImage_path(R.drawable.filelocator);
        da.setTarget(VisitorBookActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("15");
        da.setName("Catalogue");
        da.setImage_path(R.drawable.catalog);
        da.setTarget(CatalogueActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

       /* da = new DashboardModel();
        da.setId("13");
        da.setName("Convertor");
        da.setImage_path(R.drawable.convertor);
        da.setTarget(CalCulateActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);*/

        /*da = new DashboardModel();
        da.setId("14");
        da.setName("Currency Calculator");
        da.setImage_path(R.drawable.currency);
        da.setTarget(CurrencyCalculaterActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);*/

        da = new DashboardModel();
        da.setId("10");
        da.setName("GST Calculator");
        da.setImage_path(R.drawable.gst_calculator);
        da.setTarget(GstCalculateActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        /*da = new DashboardModel();
        da.setId("11");
        da.setName("Notes");
        da.setImage_path(R.drawable.document);
        da.setTarget(NoteActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);*/

        da = new DashboardModel();
        da.setId("7");
        da.setName("Chat");
        da.setImage_path(R.drawable.ic_chat_black_24dp);
        da.setTarget(ChatRoomActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        data.add(da);

        if (myPreferences.getPreferences(MyPreferences.leave_view).equals("1")) {
            da = new DashboardModel();
            da.setId("19");
            da.setName("Leave Request");
            da.setImage_path(R.drawable.leave_request);
            da.setTarget(LeaveRequestActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.expence_view).equals("1")) {
            da = new DashboardModel();
            da.setId("22");
            da.setName("Expense History");
            da.setImage_path(R.drawable.expance);
            da.setTarget(ExpanceActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.daily_work_report_view).equals("1")) {
            da = new DashboardModel();
            da.setId("21");
            da.setName("Daily Work Report");
            da.setImage_path(R.drawable.sales_daily_report);
            da.setTarget(DailyWorkReportActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.daily_sales_report_view).equals("1")) {
            da = new DashboardModel();
            da.setId("21");
            da.setName("Daily Sales Report");
            da.setImage_path(R.drawable.sales_daily_report);
            da.setTarget(DailySalesReportActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            data.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.daily_service_report_view).equals("1")) {
            da = new DashboardModel();
            da.setId("21");
            da.setName("Daily Service Report");
            da.setImage_path(R.drawable.service_report);
            da.setTarget(DailyServiceReportActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            data.add(da);
        }

        da = new DashboardModel();
        da.setId("16");
        da.setName("Help Desk");
        da.setImage_path(R.drawable.pipline);
        da.setTarget(MyPipelineActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.sales_management));
        data.add(da);

        da = new DashboardModel();
        da.setId("17");
        da.setName("Sales Quatation");
        da.setImage_path(R.drawable.salse_quation);
        da.setTarget(QuatationRequest.class);  // change sales quatation
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.sales_management));
        data.add(da);

        da = new DashboardModel();
        da.setId("18");
        da.setName("Sales Order");
        da.setImage_path(R.drawable.sales_order);
        da.setTarget(SalesOrder.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.sales_management));
        data.add(da);

        da = new DashboardModel();
        da.setId("19");
        da.setName("Service Invoice");
        da.setImage_path(R.drawable.sales_order);
        da.setTarget(ServiceInvoiceActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        data.add(da);

        da = new DashboardModel();
        da.setId("20");
        da.setName("Sales Return");
        da.setImage_path(R.drawable.sales_order);
        da.setTarget(SalesReturn.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        data.add(da);


        /*da = new DashboardModel();
        da.setId("24");
        da.setName("Demo History");
        da.setImage_path(R.drawable.demo);
        da.setTarget(DemoHistoryActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        data.add(da);
        adapter.notifyDataSetChanged();

        da = new DashboardModel();
        da.setId("23");
        da.setName("Complaint History");
        da.setImage_path(R.drawable.complain);
        da.setTarget(ComplantHistoryActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        data.add(da);*/
    }

    public void LoadNavigationBar() {

        NavigationItemModel da;

        if (myPreferences.getPreferences(MyPreferences.task_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("4");
            da.setName("My Task");
            da.setImage_path(R.drawable.task);
            da.setTarget(TaskActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            da.setType(0);
            navData.add(da);
        }

        da = new NavigationItemModel();
        da.setId("9"); //9
        da.setName("Schedule");
        da.setImage_path(R.drawable.schedual);
        da.setTarget(SchedualReminderActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId(""); //9
        da.setName("Circular");
        da.setImage_path(R.drawable.circlar);
        da.setTarget(SchedualReminderActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId(""); //9
        da.setName("Doc");
        da.setImage_path(R.drawable.doc);
        da.setTarget(SchedualReminderActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId(""); //9
        da.setName("Sms");
        da.setImage_path(R.drawable.sms);
        da.setTarget(SchedualReminderActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        if (myPreferences.getPreferences(MyPreferences.contact_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("1");
            da.setName("Contact");
            da.setImage_path(R.drawable.contact);
            da.setTarget(ContactHistory.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            da.setType(0);
            navData.add(da);
        }

        da = new NavigationItemModel();
        da.setId("10");
        da.setName("Reception");
        da.setImage_path(R.drawable.reception);
        da.setTarget(ReceptionActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("");
        da.setName("Recruitment");
        da.setImage_path(R.drawable.recrutment);
        da.setTarget(ContactHistory.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        if (myPreferences.getPreferences(MyPreferences.contact_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("3");
            da.setName("Couriers");
            da.setImage_path(R.drawable.couriers);
            da.setTarget(CourierActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            da.setType(0);
            navData.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.document_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("2");
            da.setName("Drive");
            da.setImage_path(R.drawable.driver);
            da.setTarget(DocumentActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            da.setType(0);
            navData.add(da);
        }

        /*da = new NavigationItemModel();
        da.setId("13");
        da.setName("Music");
        da.setImage_path(R.drawable.ic_library_music_white_24dp);
        da.setTarget(MainCategoryActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);*/

        if (myPreferences.getPreferences(MyPreferences.cheque_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("5");
            da.setName("Cheque");
            da.setImage_path(R.drawable.ic_payment_black_24dp);
            da.setTarget(ChequeHistoryActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            da.setType(0);
            navData.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.INSERT_FLAG).equals("1") || myPreferences.getPreferences(MyPreferences.VIEW_FLAG).equals("1") || myPreferences.getPreferences(MyPreferences.followup).equals("1")) {
            da = new NavigationItemModel();
            da.setId("8");
            da.setName("Inquiry");
            da.setImage_path(R.drawable.document);
            da.setTarget(VisitorBookActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
            da.setType(0);
            navData.add(da);
        }

        da = new NavigationItemModel();
        da.setId("");
        da.setName("Survey");
        da.setImage_path(R.drawable.servey);
        da.setTarget(ContactHistory.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("");
        da.setName("File Locator");
        da.setImage_path(R.drawable.filelocator);
        da.setTarget(ContactHistory.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("16");
        da.setName("Catalogue");
        da.setImage_path(R.drawable.ic_perm_media_white_24dp);
        da.setTarget(CatalogueActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        /*da = new NavigationItemModel();
        da.setId("14");
        da.setName("Convertor");
        da.setImage_path(R.drawable.ic_monetization_on_black_24dp);
        da.setTarget(CalCulateActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("15");
        da.setName("Currency Calculator");
        da.setImage_path(R.drawable.ic_currency);
        da.setTarget(CurrencyCalculaterActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);*/

        da = new NavigationItemModel();
        da.setId("11");
        da.setName("GST Calculator");
        da.setImage_path(R.drawable.gst_calculator);
        da.setTarget(GstCalculateActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

       /* da = new NavigationItemModel();
        da.setId("12");
        da.setName("Notes");
        da.setImage_path(R.drawable.document);
        da.setTarget(NoteActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);*/

        da = new NavigationItemModel();
        da.setId("8");
        da.setName("Chat");
        da.setImage_path(R.drawable.ic_chat_black_24dp);
        da.setTarget(ChatRoomActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);


        da = new NavigationItemModel();
        da.setId("6");
        da.setName("Profile");
        da.setImage_path(R.drawable.profile);
        da.setTarget(ProfileActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("7");
        da.setName("Reset Tutoral");
        da.setImage_path(R.drawable.ic_video_library_black_24dp);
        da.setTarget(ContactHistory.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("");
        da.setName("Erp");
        da.setImage_path(R.drawable.task);
        da.setTarget(ContactHistory.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
        da.setType(2);
        navData.add(da);

        if (myPreferences.getPreferences(MyPreferences.leave_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("19");
            da.setName("Leave Request");
            da.setImage_path(R.drawable.leave_request);
            da.setTarget(LeaveRequestActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            da.setType(0);
            navData.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.expence_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("22");
            da.setName("Expense History");
            da.setImage_path(R.drawable.expance);
            da.setTarget(ExpanceActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            da.setType(0);
            navData.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.daily_work_report_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("21");
            da.setName("Daily Work Report");
            da.setImage_path(R.drawable.sales_daily_report);
            da.setTarget(DailyWorkReportActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            da.setType(0);
            navData.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.daily_sales_report_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("21");
            da.setName("Daily Sales Report");
            da.setImage_path(R.drawable.sales_daily_report);
            da.setTarget(DailySalesReportActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            da.setType(0);
            navData.add(da);
        }

        if (myPreferences.getPreferences(MyPreferences.daily_service_report_view).equals("1")) {
            da = new NavigationItemModel();
            da.setId("21");
            da.setName("Daily Service Report");
            da.setImage_path(R.drawable.service_report);
            da.setTarget(DailyServiceReportActivity.class);
            da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.employment_management));
            da.setType(0);
            navData.add(da);
        }

        da = new NavigationItemModel();
        da.setId("16");
        da.setName("Help Desk");
        da.setImage_path(R.drawable.pipline);
        da.setTarget(MyPipelineActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.sales_management));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("17");
        da.setName("Sales Quotation");
        da.setImage_path(R.drawable.salse_quation);
        da.setTarget(QuatationRequest.class); // change
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.sales_management));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("18");
        da.setName("Sales Order");
        da.setImage_path(R.drawable.sales_order);
        da.setTarget(SalesOrder.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.sales_management));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("19");
        da.setName("Service Invoice");
        da.setImage_path(R.drawable.sales_order);
        da.setTarget(ServiceInvoiceActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("20");
        da.setName("Sales Return");
        da.setImage_path(R.drawable.sales_order);
        da.setTarget(SalesReturn.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        da.setType(0);
        navData.add(da);


        /*da = new NavigationItemModel();
        da.setId("23");
        da.setName("Complaint History");
        da.setImage_path(R.drawable.complain);
        da.setTarget(ComplantHistoryActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        da.setType(0);
        navData.add(da);

        da = new NavigationItemModel();
        da.setId("24");
        da.setName("Demo History");
        da.setImage_path(R.drawable.demo);
        da.setTarget(DemoHistoryActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        da.setType(0);
        navData.add(da);*/


        da = new NavigationItemModel();
        da.setId("-1");
        da.setName("Share");
        da.setImage_path(R.drawable.ic_share_white_24dp);
        da.setTarget(DemoHistoryActivity.class);
        da.setColor(ContextCompat.getColor(DashboardActivity.this, R.color.help_desk));
        da.setType(0);
        navData.add(da);

        itemAdapter = new NavigationItemAdapter(DashboardActivity.this, navData);
        navRecycleview.setAdapter(itemAdapter);
        navRecycleview.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.VERTICAL, false));

    }

    private void GetBanner(String type) {
        try {
            final ProgressDialog pd = new ProgressDialog(DashboardActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getBanner();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            myPreferences.setPreferences(MyPreferences.Label, "" + result.getJSONArray("Label"));
                            myPreferences.setPreferences(MyPreferences.Employee, "" + result.getJSONArray("Employee"));
                            myPreferences.setPreferences(MyPreferences.Tag, "" + result.getJSONArray("Tag"));
                            myPreferences.setPreferences(MyPreferences.Job_title, "" + result.getJSONArray("Desiganation"));
                        } else {
                            Toaster.show(DashboardActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }
                        CheckVersion();
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

    private void getNotice_circule() {
        try {
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getNoticeCircule("");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                ViewpagerModel da = new ViewpagerModel();
                                da.setTitle("" + c.getString("title"));
                                da.setImage_path("" + c.getString("image_path"));
                                da.setDescription("" + c.getString("description"));
                                da.setPdf_file(c.getString("pdf_file"));
                                GlobalElements.viewpagerModels.add(da);
                            }
                            NoteCirculeDialog dialog = NoteCirculeDialog.newInstance();
                            dialog.show(getSupportFragmentManager(), "nothing");
                        } else {
                            Toaster.show(DashboardActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Logout(int query) {
        AlertDialog buildInfosDialog;
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog2.setTitle("Are you sure want to logout");

        alertDialog2.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        myPreferences.clearPreferences();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                    }
                });
        buildInfosDialog = alertDialog2.create();
        buildInfosDialog.show();
    }

    @Override
    public void attendance(int query) {
        try {
            if (!myPreferences.getPreferences(MyPreferences.Execuvive_in_min).equals("") || !myPreferences.getPreferences(MyPreferences.Execuvive_in_max).equals("")) {
                if (AttendanceTime(myPreferences.getPreferences(MyPreferences.Execuvive_in_min), myPreferences.getPreferences(MyPreferences.Execuvive_in_max))) // true
                {
                    if (GlobalElements.isConnectingToInternet(DashboardActivity.this)) {
                        //SearchDefaultProduct();
                        GPSTracker gps = new GPSTracker(DashboardActivity.this);
                        if (gps.canGetLocation()) {
                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();
                            db.AddSalesTracking("" + GlobalElements.getDateFrom_YYYY_MM_DD(), "" + latitude, "" + longitude, "Attendance");
                        } else {
                            gps.showSettingsAlert();
                        }

                        try {
                            int i2 = 300;//= 3600
                            Intent intent2 = new Intent(DashboardActivity.this, FiveMinuteAlarmReceiver.class);
                            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                                    DashboardActivity.this.getApplicationContext(), 59833212, intent2, 0);
                            AlarmManager alarmManager2 = (AlarmManager) DashboardActivity.this.getSystemService(ALARM_SERVICE);
                            alarmManager2.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i2 * 1000), pendingIntent2);

                            int i1 = 300;//= 3600
                            Intent intent = new Intent(DashboardActivity.this, HourAlarmReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this.getApplicationContext(), 234324245, intent, 0);
                            AlarmManager alarmManager = (AlarmManager) DashboardActivity.this.getSystemService(ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i1 * 1000), pendingIntent);
                            Intent i = new Intent(DashboardActivity.this, LocationService.class);
                            startService(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AddAttandance("In");
                    } else {
                        GlobalElements.showDialog(DashboardActivity.this);
                    }
                } else {
                    Toaster.show(DashboardActivity.this, "You are late \nplease contact to administrator for more details", true, Toaster.DANGER);
                    myPreferences.setPreferences(MyPreferences.UDATE, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void emptyLayout(int type) {
        if (type == 0) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            try {
                Intent intent = new Intent(DashboardActivity.this, CominingSoonActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            myPreferences.clearTutoralPreferences();
            try {
                TapIntroHelper.showDashboardIntro(this, getResources().getColor(R.color.colorPrimary));
            } catch (Exception e) {
                e.printStackTrace();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void shareApp(int query) {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.innovent.erp");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Innovent");
            startActivity(Intent.createChooser(sharingIntent, "Share Vie"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AddAttandance(final String type) {
        try {
            final ProgressDialog pd = new ProgressDialog(DashboardActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.AddAttendance(myPreferences.getPreferences(MyPreferences.id), myPreferences.getPreferences(MyPreferences.imei), type);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            myPreferences.setPreferences(MyPreferences.UDATE, GlobalElements.getCurrentdate());
                            if (GlobalElements.compareDate(myPreferences.getPreferences(MyPreferences.UDATE), "dd-MM-yyyy")) {
                                //attandance.setImageResource(R.drawable.attendence);
                                Drawable drawable = getResources().getDrawable(R.drawable.attendence);
                                drawable = DrawableCompat.wrap(drawable);
                                DrawableCompat.setTint(drawable, ContextCompat.getColor(DashboardActivity.this, R.color.dash_border));
                            }
                            LoadTiles();
                            Toaster.show(DashboardActivity.this, "" + json.getString("ack_msg"), true, Toaster.SUCCESS);
                        } else {
                            Toaster.show(DashboardActivity.this, "" + json.getString("ack_msg"), true, Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                    System.out.print("error" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckVersion() {
        try {
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.CheckVersion();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            String version_name = result.getString("version_name");

                            android.app.AlertDialog.Builder alertDialog2 = new android.app.AlertDialog.Builder(DashboardActivity.this);
                            LayoutInflater inflater = DashboardActivity.this.getLayoutInflater();
                            final View vi = inflater.inflate(R.layout.dialog_info, null);
                            alertDialog2.setView(vi);

                            ((TextView) vi.findViewById(R.id.info_sdk_version)).setText("Version" + version_name);
                            ((TextView) vi.findViewById(R.id.info_body)).setVisibility(View.GONE);
                            ((TextView) vi.findViewById(R.id.info_update)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (GlobalElements.isConnectingToInternet(DashboardActivity.this)) {
                                        try {
                                            if (GlobalElements.isConnectingToInternet(DashboardActivity.this)) {
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.innovent.erp"));
                                                startActivity(i);
                                            } else {
                                                GlobalElements.showDialog(DashboardActivity.this);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        GlobalElements.showDialog(DashboardActivity.this);
                                    }
                                }
                            });

                            String versionName = DashboardActivity.this.getPackageManager().getPackageInfo(DashboardActivity.this.getPackageName(), 0).versionName;
                            if (!versionName.equals("" + version_name)) {
                                buildInfosDialog = alertDialog2.create();
                                buildInfosDialog.setCancelable(false);
                                buildInfosDialog.show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.print("error" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
