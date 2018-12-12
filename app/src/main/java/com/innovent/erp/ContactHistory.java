package com.innovent.erp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.pageAdapter.ContactHistoryPagerAdapter;
import com.innovent.erp.fragment.ContactCompanyFragment;
import com.innovent.erp.fragment.ContactIndividualFragment;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.service.DownloadService;
import com.innovent.erp.tutoral.TapIntroHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactHistory extends AppCompatActivity implements ContactCompanyFragment.CompanyIntercommunication, ContactIndividualFragment.IndividualIntercommunication {

    @BindView(R.id.add_contact_company)
    FloatingActionButton addContactCompany;
    @BindView(R.id.add_contact_individual)
    FloatingActionButton addContactIndividual;
    @BindView(R.id.fabDashboardMenu)
    FloatingActionMenu fabDashboardMenu;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    ContactHistoryPagerAdapter pagerAdapter;
    ArrayList<GeneralModel> data = new ArrayList<>();
    MyPreferences myPreferences;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_history);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (myPreferences.getPreferences(MyPreferences.contact_insert).equals("1")) {
            addContactCompany.setVisibility(View.VISIBLE);
            addContactIndividual.setVisibility(View.VISIBLE);
        } else {
            addContactCompany.setVisibility(View.GONE);
            addContactIndividual.setVisibility(View.GONE);
        }

        addContactCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactHistory.this, AddContactCompanyActivity.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent, 0);
            }
        });

        addContactIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactHistory.this, AddContactIndividualActivity.class);
                intent.putExtra("type", "0");
                startActivityForResult(intent, 0);
            }
        });

        GeneralModel da = new GeneralModel();
        da.setId("1");
        da.setName("Company Contact");
        data.add(da);

        da = new GeneralModel();
        da.setId("2");
        da.setName("Individual Contact");
        data.add(da);

        pagerAdapter = new ContactHistoryPagerAdapter(this.getSupportFragmentManager(), data);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    try {
                        ContactHistoryPagerAdapter fragmentPagerAdapter = (ContactHistoryPagerAdapter) viewPager.getAdapter();
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
                        ContactHistoryPagerAdapter fragmentPagerAdapter = (ContactHistoryPagerAdapter) viewPager.getAdapter();
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
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        try {
            TapIntroHelper.showContactIntro(this, ContextCompat.getColor(ContactHistory.this, R.color.colorPrimary));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_history_activity, menu);
        SearchManager searchManager = (SearchManager) ContactHistory.this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ContactHistory.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search ...");
        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchMagIcon.setImageResource(R.drawable.ic_close_white_24dp);

        Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
            if (viewPagerFragment instanceof ContactCompanyFragment) {
                ContactCompanyFragment oneFragment = (ContactCompanyFragment) viewPagerFragment;
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
                        if (viewPagerFragment instanceof ContactCompanyFragment) {
                            ContactCompanyFragment oneFragment = (ContactCompanyFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.SearchData(searchView);
                            }
                        }
                    }
                } else {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ContactIndividualFragment) {
                            ContactIndividualFragment oneFragment = (ContactIndividualFragment) viewPagerFragment;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.filter:
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    System.out.print("");
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ContactCompanyFragment) {
                            ContactCompanyFragment oneFragment = (ContactCompanyFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.FilterData();
                            }
                        }
                    }
                } else if (position == 1) {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ContactIndividualFragment) {
                            ContactIndividualFragment oneFragment = (ContactIndividualFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.FilterData();
                            }
                        }
                    }
                }
                break;
            case R.id.refresh:
                position = viewPager.getCurrentItem();
                if (position == 0) {
                    System.out.print("");
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ContactCompanyFragment) {
                            ContactCompanyFragment oneFragment = (ContactCompanyFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.getHistory();
                            }
                        }
                    }
                } else if (position == 1) {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ContactIndividualFragment) {
                            ContactIndividualFragment oneFragment = (ContactIndividualFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.getHistory();
                            }
                        }
                    }
                }
                break;
            case R.id.action_pdf:
                position = viewPager.getCurrentItem();
                if (position == 0) {
                    exportHistory("2");
                }
                break;
            case R.id.action_excel:
                position = viewPager.getCurrentItem();
                if (position == 0) {
                    exportHistory("1");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 11) {
            try {
                ContactHistoryPagerAdapter fragmentPagerAdapter = (ContactHistoryPagerAdapter) viewPager.getAdapter();
                for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ContactCompanyFragment) {
                            ContactCompanyFragment oneFragment = (ContactCompanyFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == 12) {
            try {
                ContactHistoryPagerAdapter fragmentPagerAdapter = (ContactHistoryPagerAdapter) viewPager.getAdapter();
                for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof ContactIndividualFragment) {
                            ContactIndividualFragment oneFragment = (ContactIndividualFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void CompanyData(int total, int filter) {
        System.out.print("" + total);
        if (viewPager.getCurrentItem() == 0) {
            getSupportActionBar().setSubtitle("Fc / Tc : " + filter + " / " + total);
        }
    }

    @Override
    public void IndividualData(int total, int filter) {
        if (viewPager.getCurrentItem() == 1) {
            getSupportActionBar().setSubtitle("Fc / Tc : " + filter + " / " + total);
        }
    }

    private void exportHistory(final String type) {
        try {

            final ProgressDialog pd = new ProgressDialog(ContactHistory.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.ExportContactHistory(myPreferences.getPreferences(MyPreferences.id), GlobalElements.tagids, GlobalElements.labelids, GlobalElements.city, GlobalElements.zone, type);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            if (type.equals("2"))  // export company contact detail
                            {
                                Intent intent = new Intent(ContactHistory.this, DownloadService.class);
                                intent.putExtra("file_url", "" + json.getString("pdf"));
                                startService(intent);
                            } else {
                                Intent intent = new Intent(ContactHistory.this, DownloadService.class);
                                intent.putExtra("file_url", "" + json.getString("excel"));
                                startService(intent);
                            }
                        } else {
                            Toaster.show(ContactHistory.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
