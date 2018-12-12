package com.innovent.erp;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovent.erp.custom.Toaster;
import com.innovent.erp.fragment.CourierListFragment;
import com.innovent.erp.fragment.ReadyToShipFragment;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.pageAdapter.CourierHistoryPagerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourierActivity extends AppCompatActivity {

    MyPreferences myPreferences;
    TextView todate;
    TextView fromdate;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.ready_to_ship)
    FloatingActionButton readyToShip;
    @BindView(R.id.courier_list)
    FloatingActionButton courierList;
    @BindView(R.id.fabDashboardMenu)
    FloatingActionMenu fabDashboardMenu;
    @BindView(R.id.filter)
    ImageView filter;
    @BindView(R.id.refresh)
    ImageView refresh;
    @BindView(R.id.action_more)
    ImageView actionMore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SearchView searchView;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    ArrayList<GeneralModel> data = new ArrayList<>();
    CourierHistoryPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        ButterKnife.bind(this);
        // setSupportActionBar(toolbar);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GlobalElements.courierType="receiver";
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        /*SearchManager searchManager = (SearchManager) CourierActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(CourierActivity.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search ...");
        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchMagIcon.setImageResource(R.drawable.ic_close_white_24dp);
        setUpSearchObservable(searchView);*/

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        if (myPreferences.getPreferences(MyPreferences.couriers_insert).equals("1")) {
            fabDashboardMenu.setVisibility(View.VISIBLE);
        } else {
            fabDashboardMenu.setVisibility(View.GONE);
        }

        readyToShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabDashboardMenu.close(true);
                Intent intent = new Intent(CourierActivity.this, AddCourierActivity.class);
                intent.putExtra("courier_type", "1");
                startActivityForResult(intent, 0);
            }
        });

        courierList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabDashboardMenu.close(true);
                Intent intent = new Intent(CourierActivity.this, AddCourierActivity.class);
                intent.putExtra("courier_type", "2");
                startActivityForResult(intent, 0);
            }
        });


        GeneralModel da = new GeneralModel();
        da.setId("1");
        da.setName("Ready To Ship");
        data.add(da);

        da = new GeneralModel();
        da.setId("2");
        da.setName("Courier List");
        data.add(da);

        pagerAdapter = new CourierHistoryPagerAdapter(this.getSupportFragmentManager(), data);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.courier_activity, menu);
        SearchManager searchManager = (SearchManager) CourierActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(CourierActivity.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search ...");
        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchMagIcon.setImageResource(R.drawable.ic_close_white_24dp);
        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
            if (viewPagerFragment instanceof ReadyToShipFragment) {
                ReadyToShipFragment oneFragment = (ReadyToShipFragment) viewPagerFragment;
                if (oneFragment != null) {
                    oneFragment.setUpSearchObservable(searchView);
                }
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //searchView.onActionViewCollapsed();

                if (searchView.isIconified()) {
                    System.out.print("");
                } else {
                    searchView.onActionViewCollapsed();
                }
                if (position == 0) {
                    System.out.print("");
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ReadyToShipFragment) {
                            ReadyToShipFragment oneFragment = (ReadyToShipFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.setUpSearchObservable(searchView);
                            }
                        }
                    }
                } else {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof CourierListFragment) {
                            CourierListFragment oneFragment = (CourierListFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.setUpSearchObservable(searchView);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        MenuItem sender = menu.findItem(R.id.action_sender);
        MenuItem receiver = menu.findItem(R.id.action_receive);

        int position = viewPager.getCurrentItem();
        if (position == 0) {
            sender.setVisible(false);
            receiver.setVisible(false);
        } else {
            sender.setVisible(true);
            receiver.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.filter:
                filterDialog();
                break;
            case R.id.action_sender:
                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof CourierListFragment) {
                        CourierListFragment oneFragment = (CourierListFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            GlobalElements.courierType = "sender";
                            oneFragment.changeType();
                        }
                    }
                }
                break;
            case R.id.action_receive:
                viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof CourierListFragment) {
                        CourierListFragment oneFragment = (CourierListFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            GlobalElements.courierType = "receiver";
                            oneFragment.changeType();
                        }
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            String courierType = data.getStringExtra("courierType");
            if (courierType.equals("1")) // courierType = 1 ready to ship and courierType = 2 courier list
            {
                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof ReadyToShipFragment) {
                        ReadyToShipFragment oneFragment = (ReadyToShipFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            oneFragment.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                }
            } else {
                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof CourierListFragment) {
                        CourierListFragment oneFragment = (CourierListFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            oneFragment.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                }
            }
        }
    }

    private void filterDialog() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.dialog_filter, null);
        final AlertDialog.Builder dg = new AlertDialog.Builder(CourierActivity.this);
        dg.setView(menuLayout);
        final AlertDialog dialog = dg.create();
        fromdate = (TextView) menuLayout.findViewById(R.id.fromdate);
        todate = (TextView) menuLayout.findViewById(R.id.todate);

        todate.setText("" + sdf.format(new Date()));
        String dt = todate.getText().toString();
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -8);  // number of days to minus
        dt = sdf.format(c.getTime());
        fromdate.setText(dt.toString());
        setFromDate();
        setToDate();

        ImageView btnCancel = (ImageView) menuLayout.findViewById(R.id.cancelbtn);
        Button btnClear = (Button) menuLayout.findViewById(R.id.clearbtn);
        Button shippedBtn = (Button) menuLayout.findViewById(R.id.shippedBtn);
        Button deliveredBtn = (Button) menuLayout.findViewById(R.id.deliveredBtn);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                fromdate.setText("");
                todate.setText("");
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromdate.setText("");
                todate.setText("");
                if (GlobalElements.isConnectingToInternet(CourierActivity.this)) {
                    int position = viewPager.getCurrentItem();
                    if (position == 0) {
                        System.out.print("");
                        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                            if (viewPagerFragment instanceof ReadyToShipFragment) {
                                ReadyToShipFragment oneFragment = (ReadyToShipFragment) viewPagerFragment;
                                if (oneFragment != null) {
                                    oneFragment.filterCourierHistory("", "", "shipping");
                                }
                            }
                        }
                    } else if (position == 1) {
                        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                            if (viewPagerFragment instanceof CourierListFragment) {
                                CourierListFragment oneFragment = (CourierListFragment) viewPagerFragment;
                                if (oneFragment != null) {
                                    oneFragment.filterCourierHistory("", "", "shipping");
                                }
                            }
                        }
                    }
                } else {
                    GlobalElements.showDialog(CourierActivity.this);
                }
                dialog.dismiss();
            }
        });

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        shippedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date _to_date = inputFormat.parse("" + todate.getText().toString());
                    Date _from_date = inputFormat.parse("" + fromdate.getText().toString());
                    Date to_date = outputFormat.parse("" + outputFormat.format(_to_date));  //to date
                    Date frdate = outputFormat.parse("" + outputFormat.format(_from_date)); // from date

                    if (frdate.after(to_date)) {
                        Toaster.show(CourierActivity.this, "" + getResources().getString(R.string.from_date), false, Toaster.DANGER);
                    } else if (to_date.before(frdate)) {
                        Toaster.show(CourierActivity.this, "" + getResources().getString(R.string.to_date), false, Toaster.DANGER);
                    } else if (frdate.equals(to_date)) {
                        Toaster.show(CourierActivity.this, "" + getResources().getString(R.string.from_date_to_date_equals), false, Toaster.DANGER);
                    } else {
                        if (GlobalElements.isConnectingToInternet(CourierActivity.this)) {

                            int position = viewPager.getCurrentItem();
                            if (position == 0) {
                                System.out.print("");
                                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                    if (viewPagerFragment instanceof ReadyToShipFragment) {
                                        ReadyToShipFragment oneFragment = (ReadyToShipFragment) viewPagerFragment;
                                        if (oneFragment != null) {
                                            oneFragment.filterCourierHistory(todate.getText().toString(), fromdate.getText().toString(), "shipping");
                                        }
                                    }
                                }
                            } else if (position == 1) {
                                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                    if (viewPagerFragment instanceof CourierListFragment) {
                                        CourierListFragment oneFragment = (CourierListFragment) viewPagerFragment;
                                        if (oneFragment != null) {
                                            oneFragment.filterCourierHistory(todate.getText().toString(), fromdate.getText().toString(), "shipping");
                                        }
                                    }
                                }
                            }

                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            GlobalElements.showDialog(CourierActivity.this);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date _to_date = inputFormat.parse("" + todate.getText().toString());
                    Date _from_date = inputFormat.parse("" + fromdate.getText().toString());
                    Date to_date = outputFormat.parse("" + outputFormat.format(_to_date));  //to date
                    Date frdate = outputFormat.parse("" + outputFormat.format(_from_date)); // from date

                    if (frdate.after(to_date)) {
                        Toaster.show(CourierActivity.this, "" + getResources().getString(R.string.from_date), false, Toaster.DANGER);
                    } else if (to_date.before(frdate)) {
                        Toaster.show(CourierActivity.this, "" + getResources().getString(R.string.to_date), false, Toaster.DANGER);
                    } else if (frdate.equals(to_date)) {
                        Toaster.show(CourierActivity.this, "" + getResources().getString(R.string.from_date_to_date_equals), false, Toaster.DANGER);
                    } else {
                        if (GlobalElements.isConnectingToInternet(CourierActivity.this)) {
                            int position = viewPager.getCurrentItem();
                            if (position == 0) {
                                System.out.print("");
                                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                    if (viewPagerFragment instanceof ReadyToShipFragment) {
                                        ReadyToShipFragment oneFragment = (ReadyToShipFragment) viewPagerFragment;
                                        if (oneFragment != null) {
                                            oneFragment.filterCourierHistory(todate.getText().toString(), fromdate.getText().toString(), "deliverd");
                                        }
                                    }
                                }
                            } else if (position == 1) {
                                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                    if (viewPagerFragment instanceof CourierListFragment) {
                                        CourierListFragment oneFragment = (CourierListFragment) viewPagerFragment;
                                        if (oneFragment != null) {
                                            oneFragment.filterCourierHistory(todate.getText().toString(), fromdate.getText().toString(), "deliverd");
                                        }
                                    }
                                }
                            }
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            GlobalElements.showDialog(CourierActivity.this);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void setFromDate() {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.DATE, -8);
        fromDatePickerDialog = new DatePickerDialog(CourierActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setToDate() {

        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(CourierActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                todate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

}
