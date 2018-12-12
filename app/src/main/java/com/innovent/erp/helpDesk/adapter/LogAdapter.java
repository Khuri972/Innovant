package com.innovent.erp.helpDesk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.helpDesk.model.LogModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {


    private ArrayList<LogModel> data = new ArrayList<>();
    private Context context;

    public LogAdapter(Context context, ArrayList<LogModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.listLogDate.setText("" + data.get(i).getDate());
        viewHolder.listLogTitle.setText("" + data.get(i).getTitle());
        viewHolder.listLogDesc.setText("" + data.get(i).getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_log_date)
        TextView listLogDate;
        @BindView(R.id.list_log_title)
        TextView listLogTitle;
        @BindView(R.id.list_log_desc)
        TextView listLogDesc;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
