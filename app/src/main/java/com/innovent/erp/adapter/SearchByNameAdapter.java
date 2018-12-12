package com.innovent.erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by CRAFT BOX on 1/24/2017.
 */

public class SearchByNameAdapter extends BaseAdapter implements Filterable {

    private Context context;
    public ArrayList<GeneralModel> data = new ArrayList<>();
    ArrayList<GeneralModel> arraylist = new ArrayList<>();
    private LayoutInflater inflater1 = null;

    public SearchByNameAdapter(Context context, ArrayList<GeneralModel> da) {
        this.context = context;
        data = da;
        arraylist.addAll(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        name.setText("" + data.get(position).getName());
        return vi;
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = null;
                try {
                    filterResults = new FilterResults();
                    if (constraint != null) {
                        data.clear();

                        for (GeneralModel wp : arraylist) {
                            if (wp.getName().toUpperCase(Locale.getDefault()).contains(constraint)) {
                                data.add(wp);
                            }
                        }
                        filterResults.values = data;
                        filterResults.count = data.size();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                try {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        return myFilter;
    }
}
