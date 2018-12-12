package com.innovent.erp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.AddCurrencyModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class AddCurrencyAdapter extends RecyclerView.Adapter<AddCurrencyAdapter.ViewHolder> {

    private ArrayList<AddCurrencyModel> data = new ArrayList<>();
    private Context context;

    public interface Intercommmunitator {
        public void CalculateMethod();
    }

    public AddCurrencyAdapter(Context context, ArrayList<AddCurrencyModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_currency, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.noteTxt.setText("" + data.get(i).getTitle());
        viewHolder.noteQty.setTag(data.get(i).getId());

        if (data.get(i).getQty().equals("")) {
            viewHolder.noteQty.setText("");
        } else {
            viewHolder.noteQty.setText("" + data.get(i).getQty());
        }

        if (data.get(i).getQty().equals("")) {
            viewHolder.noteTotal.setText("");
        } else {
            viewHolder.noteTotal.setText("" + data.get(i).getTotal());
        }

        viewHolder.noteQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
                try {
                    if(viewHolder.noteQty.hasFocus())
                    {
                        if(viewHolder.noteQty.getTag().equals(data.get(i).getId()))
                        {
                            if (charSequence.length() > 0) {
                                int total = Integer.parseInt(viewHolder.noteTxt.getText().toString()) * Integer.parseInt(viewHolder.noteQty.getText().toString().trim());
                                viewHolder.noteTotal.setText(context.getResources().getString(R.string.currence) + " " + total);
                                data.get(i).setQty(viewHolder.noteQty.getText().toString());
                                data.get(i).setTotal("" + total);
                            } else {
                                viewHolder.noteTotal.setText("");
                                data.get(i).setQty("");
                                data.get(i).setTotal("");
                            }
                            Intercommmunitator intercommmunitator = (Intercommmunitator) context;
                            intercommmunitator.CalculateMethod();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_2000_txt)
        TextView noteTxt;
        @BindView(R.id.note_2000_qty)
        EditText noteQty;
        @BindView(R.id.note_2000_total)
        TextView noteTotal;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
