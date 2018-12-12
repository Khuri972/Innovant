package com.innovent.erp.ErpModule.sales_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.model.SearchCustomerModel;
import com.innovent.erp.R;
import com.innovent.erp.model.SearchModel;
import com.innovent.erp.netUtils.UserFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CRAFT BOX on 1/24/2017.
 */

public class SearchCustomerAdapter extends BaseAdapter implements Filterable {

    private Context context;
    public List<SearchCustomerModel> suggestions;
    private LayoutInflater inflater1 = null;

    public SearchCustomerAdapter(Context context) {
        this.context = context;
        suggestions = new ArrayList<SearchCustomerModel>();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public Object getItem(int i) {
        return suggestions.get(i).getName();
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
        try {
            TextView name;
            name = (TextView) vi.findViewById(R.id.general_name);
            name.setText("" + suggestions.get(position).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        // A class that queries a web API, parses the data and
                        // returns an ArrayList<GoEuroGetSet>
                        UserFunction jp = new UserFunction();
                        List<SearchCustomerModel> new_suggestions = jp.searCustomer(constraint.toString());
                        suggestions.clear();
                        suggestions.addAll(new_suggestions);
                        // Now assign the values and count to the FilterResults
                        // object
                        filterResults.values = suggestions;
                        filterResults.count = suggestions.size();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint,
                                          FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}
