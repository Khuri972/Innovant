package com.innovent.erp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.NotificationModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<NotificationModel> data = new ArrayList<>();
    private ArrayList<NotificationModel> arraylist;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> data) {
        this.data = data;
        this.context = context;
        arraylist = new ArrayList<>();
        this.arraylist.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notifaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(Html.fromHtml(data.get(i).getTitle()));
        viewHolder.detail.setText(Html.fromHtml(data.get(i).getDesc()));

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_notifaction_title)
        TextView title;
        @BindView(R.id.list_notifaction_desc)
        TextView detail;
        @BindView(R.id.list_notification_main)
        LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void Updatedata() {
        notifyDataSetChanged();
        this.arraylist.clear();
        this.arraylist = new ArrayList<NotificationModel>();
        this.arraylist.addAll(data);
    }
}
