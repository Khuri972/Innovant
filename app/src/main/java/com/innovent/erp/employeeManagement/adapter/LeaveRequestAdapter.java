package com.innovent.erp.employeeManagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.employeeManagement.model.LeaveRequestModel;
import com.innovent.erp.service.DownloadService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder> {


    private ArrayList<LeaveRequestModel> data = new ArrayList<>();
    private Context context;

    public LeaveRequestAdapter(Context context, ArrayList<LeaveRequestModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leave_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.fromDateTxt.setText("" + data.get(i).getFrom_date() + " " + data.get(i).getFrom_time());
        viewHolder.toDateTxt.setText("" + data.get(i).getTo_date() + " " + data.get(i).getTo_time());
        viewHolder.reasonTxt.setText("" + data.get(i).getReason());
        viewHolder.fileNameTxt.setText(""+data.get(i).getAttachment_name());

        if (data.get(i).getAttachment_path().equals("")) {
            viewHolder.fileDownloadLayout.setVisibility(View.GONE);
        } else {
            viewHolder.fileDownloadLayout.setVisibility(View.VISIBLE);
        }

        viewHolder.fileDownloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra("file_url", data.get(i).getAttachment_path());
                    context.startService(intent);
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

        @BindView(R.id.from_date_txt)
        TextView fromDateTxt;
        @BindView(R.id.to_date_txt)
        TextView toDateTxt;
        @BindView(R.id.reason_txt)
        TextView reasonTxt;
        @BindView(R.id.file_name_txt)
        TextView fileNameTxt;
        @BindView(R.id.file_download_img)
        ImageView fileDownloadImg;
        @BindView(R.id.file_download_layout)
        LinearLayout fileDownloadLayout;
        @BindView(R.id.main_layout)
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
