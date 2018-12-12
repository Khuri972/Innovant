package com.innovent.erp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Suleiman on 26-07-2015.
 */
public class TaskDialogAdapter extends RecyclerView.Adapter<TaskDialogAdapter.ViewHolder> {

    ArrayList<GeneralModel> data = new ArrayList<>();
    Context context;
    String status;
    Fragment fragment;
    int taskPosition;
    public interface moveTask{
        public void moveTaskChangeStage(String status,int taskPosition);
    }

    public TaskDialogAdapter(Context context, ArrayList<GeneralModel> da, String status,Fragment fragment,int taskPosition) {
        this.context = context;
        this.data = da;
        this.fragment=fragment;
        this.taskPosition=taskPosition;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_move_task, parent, false);
        ViewHolder holder = new ViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            if (!data.get(position).getId().equals("" + status)) {
                holder.name.setText("" + data.get(position).getName());
            }
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        moveTask task=(moveTask)fragment;
                        task.moveTaskChangeStage(data.get(position).getId(),taskPosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.general_name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
