package com.innovent.erp.ErpModule.sales_management.SearchView;


import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.DigitsInputFilter;
import com.innovent.erp.netUtils.MyPreferences;

import java.util.ArrayList;
import java.util.Locale;

public class SearchAdapter extends ArrayAdapter<SearchItemModel> {

    private final Context mContext;
    private final ArrayList<SearchItemModel> mItem;
    ArrayList<SearchItemModel> arraylist = new ArrayList<>();
    MyPreferences myPreferences;

    public SearchAdapter(Context context, ArrayList<SearchItemModel> itemsArrayList) {
        super(context, R.layout.list_search_row, itemsArrayList);
        this.mContext = context;
        this.mItem = itemsArrayList;
        arraylist.addAll(mItem);
        myPreferences = new MyPreferences(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_search_row, parent, false);
        TextView name = (TextView) v.findViewById(R.id.product_name);
        TextView price = (TextView) v.findViewById(R.id.product_price);
        final EditText productQty = (EditText) v.findViewById(R.id.product_qty);

        final CheckBox check = (CheckBox) v.findViewById(R.id.list_search_check);
        LinearLayout linear = (LinearLayout) v.findViewById(R.id.list_search_linear);

        productQty.setTag(mItem.get(position).getId());
        productQty.setFilters(new InputFilter[]{new DigitsInputFilter(5, 2)});

        name.setText(mItem.get(position).getName());
        price.setText("" + GlobalElements.DecimalFormat("" + mItem.get(position).getSell_price()));
        check.setChecked(mItem.get(position).isChecked());

        if (mItem.get(position).getQty() == 0) {
            productQty.setText("");
        } else {
            productQty.setText("" + mItem.get(position).getQty());
        }


        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int po = arraylist.indexOf(mItem.get(position));
                arraylist.get(po).setChecked(b);
                SearchItemModel da = mItem.get(position);
                da.setChecked(b);
            }
        });

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItem.get(position).isChecked()) {
                    int po = arraylist.indexOf(mItem.get(position));
                    arraylist.get(po).setChecked(false);
                    SearchItemModel da = mItem.get(position);
                    da.setChecked(false);
                    check.setChecked(false);
                } else {
                    int po = arraylist.indexOf(mItem.get(position));
                    arraylist.get(po).setChecked(true);
                    SearchItemModel da = mItem.get(position);
                    da.setChecked(true);
                    check.setChecked(true);

                }
            }
        });

        productQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s.toString().length() > 0) {
                        if (productQty.getTag().equals(mItem.get(position).getId())) {
                            int po = arraylist.indexOf(mItem.get(position));
                            if (mItem.get(position).isChecked()) {
                                SearchItemModel da = mItem.get(position);
                                da.setQty(Double.parseDouble("" + productQty.getText().toString()));
                                if (!productQty.getText().toString().equals("")) {
                                    da.setChecked(true);
                                    check.setChecked(true);
                                    arraylist.get(po).setChecked(true);
                                }
                                mItem.set(position, da);
                                arraylist.get(po).setQty(Double.parseDouble("" + productQty.getText().toString()));
                            } else {
                                SearchItemModel da = mItem.get(position);
                                da.setQty(Double.parseDouble("" + productQty.getText().toString()));
                                if (!productQty.getText().toString().equals("")) {
                                    da.setChecked(true);
                                    check.setChecked(true);
                                    arraylist.get(po).setChecked(true);
                                }
                                mItem.set(position, da);
                                arraylist.get(po).setQty(Double.parseDouble("" + productQty.getText().toString()));
                            }
                        }
                    } else {
                        if (productQty.getTag().equals(mItem.get(position).getId())) {
                            int po = arraylist.indexOf(mItem.get(position));
                            SearchItemModel da = mItem.get(position);
                            da.setQty(0);
                            da.setChecked(false);
                            check.setChecked(false);
                            mItem.set(position, da);
                            arraylist.get(po).setQty(0);
                            arraylist.get(po).setChecked(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mItem.clear();
        if (charText.length() == 0) {
            mItem.addAll(arraylist);
        } else {
            for (SearchItemModel wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mItem.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
