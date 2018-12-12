package com.innovent.erp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.AddCurrencyActivity;
import com.innovent.erp.R;
import com.innovent.erp.model.CurrencyModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {

    private ArrayList<CurrencyModel> data = new ArrayList<>();
    private Context context;

    public CurrencyAdapter(Context context, ArrayList<CurrencyModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_currency_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        try {
            viewHolder.title.setText("" + data.get(i).getTitle());
            viewHolder.desc.setText("" + data.get(i).getDesc());
            viewHolder.total.setText("" + data.get(i).getTotal());
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, AddCurrencyActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("position", i);
                    intent.putExtra("id", data.get(i).getId());
                    intent.putExtra("title", data.get(i).getTitle());
                    intent.putExtra("desc", data.get(i).getDesc());
                    intent.putExtra("itemArray", data.get(i).getCurrency_object().toString());
                    ((Activity) context).startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.total)
        TextView total;
        @BindView(R.id.list_currency_main)
        LinearLayout layout;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
