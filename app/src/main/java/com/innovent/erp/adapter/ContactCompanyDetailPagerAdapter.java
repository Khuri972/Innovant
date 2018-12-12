package com.innovent.erp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.innovent.erp.fragment.CompanyContactDetailFragment;
import com.innovent.erp.fragment.CompanyPersonContactFragment;
import com.innovent.erp.model.CompanyContactHistoryModel;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CRAFT BOX on 12/23/2016.
 */

public class ContactCompanyDetailPagerAdapter extends FragmentPagerAdapter {
    Fragment f;
    List<Fragment> mFragmentList = new ArrayList<>();
    ArrayList<GeneralModel> categoryModels = new ArrayList<>();
    CompanyContactHistoryModel data;

    public ContactCompanyDetailPagerAdapter(FragmentManager fm, ArrayList<GeneralModel> topCategoryModels, CompanyContactHistoryModel data) {
        super(fm);
        this.categoryModels = topCategoryModels;
        this.data = data;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categoryModels.get(position).getName();
    }

    @Override
    public int getCount() {
        return categoryModels.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            f = new CompanyContactDetailFragment();
            Bundle b = new Bundle();
            b.putSerializable("data", data);
            f.setArguments(b);
            mFragmentList.add(f);
            return f;
        } else {
            f = new CompanyPersonContactFragment();
            Bundle b = new Bundle();
            b.putString("id", "" + data.getId());
            f.setArguments(b);
            mFragmentList.add(f);
            return f;
        }
    }
}
