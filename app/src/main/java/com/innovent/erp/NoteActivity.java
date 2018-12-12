package com.innovent.erp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.innovent.erp.fragment.ContactCompanyFragment;
import com.innovent.erp.fragment.NoteAllFragment;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.pageAdapter.NotePagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    ArrayList<GeneralModel> data = new ArrayList<>();
    NotePagerAdapter notePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notes");

        GeneralModel da = new GeneralModel();
        da.setId("1");
        da.setName("All");
        data.add(da);

        da = new GeneralModel();
        da.setId("2");
        da.setName("Collections");
        data.add(da);

        notePagerAdapter = new NotePagerAdapter(this.getSupportFragmentManager(), data);
        viewPager.setAdapter(notePagerAdapter);
        notePagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 11) {
            Fragment viewPagerFragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, 0);
            if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                if (viewPagerFragment instanceof NoteAllFragment) {
                    NoteAllFragment oneFragment = (NoteAllFragment) viewPagerFragment;
                    if (oneFragment != null) {
                        oneFragment.onActivityResult(requestCode, resultCode, data); // your custom method
                    }
                }
            }
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
}
