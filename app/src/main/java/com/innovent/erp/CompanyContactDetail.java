package com.innovent.erp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.innovent.erp.adapter.ContactCompanyDetailPagerAdapter;
import com.innovent.erp.fragment.CompanyContactDetailFragment;
import com.innovent.erp.fragment.CompanyPersonContactFragment;
import com.innovent.erp.model.CompanyContactHistoryModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyContactDetail extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.add_person_contact)
    FloatingActionButton addPersonContact;

    ContactCompanyDetailPagerAdapter pagerAdapter;
    ArrayList<GeneralModel> models = new ArrayList<>();
    CompanyContactHistoryModel data;
    int resultCode = 0;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_contact_detail);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Detail");

        try {
            Bundle bundle = getIntent().getExtras();
            data = (CompanyContactHistoryModel) bundle.getSerializable("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        GeneralModel da = new GeneralModel();
        da.setId("1");
        da.setName("Company Detail");
        models.add(da);

        da = new GeneralModel();
        da.setId("2");
        da.setName("Company Person Contact");
        models.add(da);

        pagerAdapter = new ContactCompanyDetailPagerAdapter(this.getSupportFragmentManager(), models, data);
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
                    addPersonContact.setVisibility(View.GONE);
                    invalidateOptionsMenu();
                } else {
                    addPersonContact.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addPersonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyContactDetail.this, AddCompanyPersonActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("id", "" + data.getId());
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.company_contact_detail, menu);
        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem update = menu.findItem(R.id.action_update);
        try {
            if (myPreferences.getPreferences(MyPreferences.contact_update).equals("1")) {
                update.setVisible(true);
            } else {
                update.setVisible(true);
            }
            if (viewPager.getCurrentItem() == 0) {
                share.setVisible(true);
            } else {
                share.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (resultCode == 11) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data);
                    intent.putExtras(bundle);
                    setResult(11, intent);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.action_update:
                Intent intent = new Intent(CompanyContactDetail.this, AddContactCompanyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "1");
                bundle.putSerializable("data", data);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
            case R.id.action_share:
                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof CompanyContactDetailFragment) {
                        CompanyContactDetailFragment oneFragment = (CompanyContactDetailFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            oneFragment.shareContact(); // your custom method
                        }
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (resultCode == 11) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            intent.putExtras(bundle);
            setResult(11, intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(requestCode, _resultCode, _data);
        if (_resultCode == 11) {
            try {
                resultCode = 11;
                Bundle bundle = _data.getExtras();
                data = (CompanyContactHistoryModel) bundle.getSerializable("data");
                // setData();
                ContactCompanyDetailPagerAdapter fragmentPagerAdapter = (ContactCompanyDetailPagerAdapter) viewPager.getAdapter();
                for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                    Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                    if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                        if (viewPagerFragment instanceof CompanyContactDetailFragment) {
                            CompanyContactDetailFragment oneFragment = (CompanyContactDetailFragment) viewPagerFragment;
                            if (oneFragment != null) {
                                oneFragment.onActivityResult(requestCode, resultCode, _data); // your custom method
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (_resultCode == 13) {
            ContactCompanyDetailPagerAdapter fragmentPagerAdapter = (ContactCompanyDetailPagerAdapter) viewPager.getAdapter();
            for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, i);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof CompanyPersonContactFragment) {
                        CompanyPersonContactFragment oneFragment = (CompanyPersonContactFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            oneFragment.onActivityResult(requestCode, _resultCode, _data); // your custom method
                        }
                    }
                }
            }
        }
    }

}
