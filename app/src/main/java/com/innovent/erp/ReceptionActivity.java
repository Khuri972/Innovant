package com.innovent.erp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.innovent.erp.fragment.CallFragment;
import com.innovent.erp.fragment.VisitorFragment;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.pageAdapter.ReceptionPagerAdapter;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceptionActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    ArrayList<GeneralModel> data = new ArrayList<>();
    ReceptionPagerAdapter pagerAdapter;
    @BindView(R.id.add)
    FloatingActionButton add;
    @BindView(R.id.add_visitor)
    com.github.clans.fab.FloatingActionButton addVisitor;
    @BindView(R.id.add_call)
    com.github.clans.fab.FloatingActionButton addCall;
    @BindView(R.id.fabDashboardMenu)
    FloatingActionMenu fabDashboardMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reception");

        addVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceptionActivity.this, AddReceptionActivity.class);
                intent.putExtra("type","1");
                startActivityForResult(intent, 0);
            }
        });

        addCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReceptionActivity.this, AddReceptionActivity.class);
                intent.putExtra("type","0");
                startActivityForResult(intent, 0);
            }
        });

        GeneralModel da = new GeneralModel();
        da.setId("1");
        da.setName("Visitor");
        data.add(da);

        da = new GeneralModel();
        da.setId("2");
        da.setName("Call");
        data.add(da);

        pagerAdapter = new ReceptionPagerAdapter(this.getSupportFragmentManager(), data);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);

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
        if (resultCode == 11) {
            try {
                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof VisitorFragment) {
                        VisitorFragment oneFragment = (VisitorFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == 12) {
            try {
                Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 1);
                if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                    if (viewPagerFragment instanceof CallFragment) {
                        CallFragment oneFragment = (CallFragment) viewPagerFragment;
                        if (oneFragment != null) {
                            oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
