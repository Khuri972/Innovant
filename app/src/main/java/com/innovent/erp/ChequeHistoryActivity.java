package com.innovent.erp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovent.erp.adapter.ChequeHistoryAdapter;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.fragment.ContactCompanyFragment;
import com.innovent.erp.fragment.ContactIndividualFragment;
import com.innovent.erp.fragment.CreateChequeFragment;
import com.innovent.erp.fragment.ReceiveChequeFragment;
import com.innovent.erp.model.ChequeModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.pageAdapter.ChequeHistoryPagerAdapter;
import com.innovent.erp.tutoral.TapIntroHelper;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChequeHistoryActivity extends AppCompatActivity {

    /*@BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;*/

    @BindView(R.id.add_cheque)
    FloatingActionButton addCheque;

    // ArrayList<ChequeModel> data = new ArrayList<>();
    // ChequeHistoryAdapter adapter;
    MyPreferences myPreferences;

    TextView todate;
    TextView fromdate;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.create_cheque)
    com.github.clans.fab.FloatingActionButton createCheque;
    @BindView(R.id.receive_cheque)
    com.github.clans.fab.FloatingActionButton receiveCheque;
    @BindView(R.id.fabDashboardMenu)
    FloatingActionMenu fabDashboardMenu;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    ArrayList<GeneralModel> data = new ArrayList<>();
    ChequeHistoryPagerAdapter pagerAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_history);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        createCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChequeHistoryActivity.this, AddChequeActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("cheque_type", "sender");
                startActivityForResult(intent, 0);
            }
        });

        receiveCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChequeHistoryActivity.this, AddChequeActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("cheque_type", "receive");
                startActivityForResult(intent, 0);
            }
        });

        GeneralModel da = new GeneralModel();
        da.setId("1");
        da.setName("Create");
        data.add(da);

        da = new GeneralModel();
        da.setId("2");
        da.setName("Receive");
        data.add(da);

        pagerAdapter = new ChequeHistoryPagerAdapter(this.getSupportFragmentManager(), data);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*if (position == 0) {
                    try {
                        CourierHistoryPagerAdapter fragmentPagerAdapter = (CourierHistoryPagerAdapter) viewPager.getAdapter();
                        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                            Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                            if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                if (viewPagerFragment instanceof ContactCompanyFragment) {
                                    ContactCompanyFragment oneFragment = (ContactCompanyFragment) viewPagerFragment;
                                    if (oneFragment != null) {
                                        oneFragment.getTotal(); // your custom method
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        CourierHistoryPagerAdapter fragmentPagerAdapter = (CourierHistoryPagerAdapter) viewPager.getAdapter();
                        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                            Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                            if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                if (viewPagerFragment instanceof ContactIndividualFragment) {
                                    ContactIndividualFragment oneFragment = (ContactIndividualFragment) viewPagerFragment;
                                    if (oneFragment != null) {
                                        oneFragment.getTotal(); // your custom method
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cheque_history_activity, menu);
        SearchManager searchManager = (SearchManager) ChequeHistoryActivity.this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ChequeHistoryActivity.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search ...");
        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchMagIcon.setImageResource(R.drawable.ic_close_white_24dp);
        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
            if (viewPagerFragment instanceof CreateChequeFragment) {
                CreateChequeFragment oneFragment = (CreateChequeFragment) viewPagerFragment;
                if (oneFragment != null) {
                    oneFragment.SearchData(searchView);
                }
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (searchView.isIconified()) {
                    System.out.print("");
                } else {
                    searchView.onActionViewCollapsed();
                }
                if (position == 0) {
                    System.out.print("");
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof CreateChequeFragment) {
                            CreateChequeFragment oneFragment = (CreateChequeFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.SearchData(searchView);
                            }
                        }
                    }
                } else {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ReceiveChequeFragment) {
                            ReceiveChequeFragment oneFragment = (ReceiveChequeFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.SearchData(searchView);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        if (resultCode == 14) {
            try {
                String cheque_type = _data.getStringExtra("cheque_type");
                if (cheque_type.equals("sender")) {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof CreateChequeFragment) {
                            CreateChequeFragment oneFragment = (CreateChequeFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.onActivityResult(requestCode, resultCode, _data);
                            }
                        }
                    }
                } else {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ReceiveChequeFragment) {
                            ReceiveChequeFragment oneFragment = (ReceiveChequeFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.onActivityResult(requestCode, resultCode, _data);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == 15) {
            try {
                String cheque_type = _data.getStringExtra("cheque_type");
                if (cheque_type.equals("sender")) {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof CreateChequeFragment) {
                            CreateChequeFragment oneFragment = (CreateChequeFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.onActivityResult(requestCode, resultCode, _data);
                            }
                        }
                    }
                } else {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ReceiveChequeFragment) {
                            ReceiveChequeFragment oneFragment = (ReceiveChequeFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.onActivityResult(requestCode, resultCode, _data);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void filterDialog() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.dialog_filter, null);
        final AlertDialog.Builder dg = new AlertDialog.Builder(ChequeHistoryActivity.this);
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
        Button submit = (Button) menuLayout.findViewById(R.id.shippedBtn);
        Button deliveredBtn = (Button) menuLayout.findViewById(R.id.deliveredBtn);
        deliveredBtn.setVisibility(View.GONE);
        submit.setText("Submit");

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
                if (GlobalElements.isConnectingToInternet(ChequeHistoryActivity.this)) {
                    int position = viewPager.getCurrentItem();
                    if (position == 0) {
                        System.out.print("");
                        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                            if (viewPagerFragment instanceof CreateChequeFragment) {
                                CreateChequeFragment oneFragment = (CreateChequeFragment) viewPagerFragment;
                                if (oneFragment != null) {
                                    oneFragment.filterChequeHistory(todate.getText().toString(), fromdate.getText().toString());
                                }
                            }
                        }
                    } else if (position == 1) {
                        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                            if (viewPagerFragment instanceof ReceiveChequeFragment) {
                                ReceiveChequeFragment oneFragment = (ReceiveChequeFragment) viewPagerFragment;
                                if (oneFragment != null) {
                                    oneFragment.filterChequeHistory(todate.getText().toString(), fromdate.getText().toString());
                                }
                            }
                        }
                    }
                } else {
                    GlobalElements.showDialog(ChequeHistoryActivity.this);
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

        submit.setOnClickListener(new View.OnClickListener() {
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
                        Toaster.show(ChequeHistoryActivity.this, "" + getResources().getString(R.string.from_date), false, Toaster.DANGER);
                    } else if (to_date.before(frdate)) {
                        Toaster.show(ChequeHistoryActivity.this, "" + getResources().getString(R.string.to_date), false, Toaster.DANGER);
                    } else if (frdate.equals(to_date)) {
                        Toaster.show(ChequeHistoryActivity.this, "" + getResources().getString(R.string.from_date_to_date_equals), false, Toaster.DANGER);
                    } else {
                        if (GlobalElements.isConnectingToInternet(ChequeHistoryActivity.this)) {
                            //filterChequeHistory(todate.getText().toString(), fromdate.getText().toString());
                            int position = viewPager.getCurrentItem();
                            if (position == 0) {
                                System.out.print("");
                                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                    if (viewPagerFragment instanceof CreateChequeFragment) {
                                        CreateChequeFragment oneFragment = (CreateChequeFragment) viewPagerFragment;
                                        if (oneFragment != null) {
                                            oneFragment.filterChequeHistory(todate.getText().toString(), fromdate.getText().toString());
                                        }
                                    }
                                }
                            } else if (position == 1) {
                                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                                    if (viewPagerFragment instanceof ReceiveChequeFragment) {
                                        ReceiveChequeFragment oneFragment = (ReceiveChequeFragment) viewPagerFragment;
                                        if (oneFragment != null) {
                                            oneFragment.filterChequeHistory(todate.getText().toString(), fromdate.getText().toString());
                                        }
                                    }
                                }
                            }

                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            GlobalElements.showDialog(ChequeHistoryActivity.this);
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
        fromDatePickerDialog = new DatePickerDialog(ChequeHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setToDate() {
        Calendar newCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog(ChequeHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                todate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

}
