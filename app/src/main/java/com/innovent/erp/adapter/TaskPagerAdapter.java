package com.innovent.erp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.innovent.erp.fragment.ContactCompanyFragment;
import com.innovent.erp.fragment.ContactIndividualFragment;
import com.innovent.erp.fragment.TaskFragment;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CRAFT BOX on 12/23/2016.
 */

public class TaskPagerAdapter extends FragmentPagerAdapter {
    Fragment f;
    List<Fragment> mFragmentList = new ArrayList<>();
    ArrayList<GeneralModel> taskModels = new ArrayList<>();

    public TaskPagerAdapter(FragmentManager fm, ArrayList<GeneralModel> taskModels) {
        super(fm);
        this.taskModels = taskModels;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return taskModels.get(position).getName();
    }

    @Override
    public int getCount() {
        return taskModels.size();
    }

    @Override
    public Fragment getItem(int position) {
        f = new TaskFragment();
        Bundle b=new Bundle();
        b.putString("status",""+taskModels.get(position).getId());
        f.setArguments(b);
        mFragmentList.add(f);
        return f;
    }
}
