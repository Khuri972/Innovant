package com.innovent.erp.ErpModule.sales_management.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.model.PendingQuotationModel;
import com.innovent.erp.R;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 10/25/2016.
 */

public class PendingQuotationAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PendingQuotationModel> data = null;
    private LayoutInflater inflater1 = null;

    public PendingQuotationAdapter(Context context, ArrayList<PendingQuotationModel> da) {
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
        TextView name;
        name = (TextView) vi.findViewById(R.id.general_name);
        name.setText(data.get(position).getQuotationNo());
        return vi;
    }
}
