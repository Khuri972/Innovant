package com.innovent.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 10/25/2016.
 */

public class MultiCheckAdapter extends BaseAdapter {

    private Context activity;
    private ArrayList<GeneralModel> data = null;
    private LayoutInflater inflater1 = null;
    public MultiCheckAdapter(Context act, ArrayList<GeneralModel> da)
    {
        activity = act;
        data = da;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if(convertView == null)
        {
            inflater1 = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater1.inflate(R.layout.list_multi_check,null);
        }
        TextView name;
        CheckBox check;
        name=(TextView)vi.findViewById(R.id.general_name);
        check = (CheckBox) vi.findViewById(R.id.check);
        name.setText(data.get(position).getName());

        return vi;
    }
}
