package com.innovent.erp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 10/25/2016.
 */

public class GeneralAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GeneralModel> data = null;
    private LayoutInflater inflater1 = null;

    public GeneralAdapter(Context context, ArrayList<GeneralModel> da) {
        this.context = context;
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
        if (convertView == null) {
            inflater1 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater1.inflate(R.layout.list_general, null);
        }
        TextView name, colorText;
        name = (TextView) vi.findViewById(R.id.general_name);
        colorText = (TextView) vi.findViewById(R.id.colorText);
        name.setText(data.get(position).getName());

        try {
            if (data.get(position).getColorCode() != null) {
                colorText.setBackgroundColor(Color.parseColor("" + data.get(position).getColorCode()));
                colorText.setVisibility(View.VISIBLE);
            } else {
                colorText.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vi;
    }
}
