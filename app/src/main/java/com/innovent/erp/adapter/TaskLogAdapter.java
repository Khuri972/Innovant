package com.innovent.erp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.TaskLogModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class TaskLogAdapter extends RecyclerView.Adapter<TaskLogAdapter.ViewHolder> {

    private ArrayList<TaskLogModel> data = new ArrayList<>();
    private Context context;

    public TaskLogAdapter(Context context, ArrayList<TaskLogModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.createdByTxt.setText("" + data.get(i).getCreatedBy());
        viewHolder.forwardTxt.setText("" + data.get(i).getForwardTo());
        viewHolder.titleTxt.setText("" + data.get(i).getTitle());
        viewHolder.descriptionTxt.setText("" + data.get(i).getDescription());
        viewHolder.dateTxt.setText("" + data.get(i).getDate());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.created_by_txt)
        TextView createdByTxt;
        @BindView(R.id.forward_txt)
        TextView forwardTxt;
        @BindView(R.id.title_txt)
        TextView titleTxt;
        @BindView(R.id.description_txt)
        TextView descriptionTxt;
        @BindView(R.id.date_txt)
        TextView dateTxt;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
