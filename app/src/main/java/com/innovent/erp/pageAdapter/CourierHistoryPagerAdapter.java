package com.innovent.erp.pageAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.innovent.erp.fragment.CourierListFragment;
import com.innovent.erp.fragment.ReadyToShipFragment;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CRAFT BOX on 12/23/2016.
 */

public class CourierHistoryPagerAdapter extends FragmentPagerAdapter {
    Fragment f;
    List<Fragment> mFragmentList = new ArrayList<>();
    ArrayList<GeneralModel> categoryModels = new ArrayList<>();

    public CourierHistoryPagerAdapter(FragmentManager fm, ArrayList<GeneralModel> topCategoryModels) {
        super(fm);
        this.categoryModels = topCategoryModels;
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
            f = new ReadyToShipFragment();
            mFragmentList.add(f);
            return f;
        } else {
            f = new CourierListFragment();
            mFragmentList.add(f);
            return f;
        }
    }
}
