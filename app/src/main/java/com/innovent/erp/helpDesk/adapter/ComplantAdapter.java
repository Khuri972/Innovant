package com.innovent.erp.helpDesk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.helpDesk.model.ComplantModel;
import com.innovent.erp.model.NotificationModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class ComplantAdapter extends RecyclerView.Adapter<ComplantAdapter.ViewHolder> {

    private ArrayList<ComplantModel> data = new ArrayList<>();
    private ArrayList<ComplantModel> arraylist;
    private Context context;

    public ComplantAdapter(Context context, ArrayList<ComplantModel> data) {
        this.data = data;
        this.context = context;
        arraylist = new ArrayList<>();
        this.arraylist.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_complant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.complaintNoTxt.setText(data.get(i).getComplantNo());
        viewHolder.customerNameTxt.setText(data.get(i).getCustomerName());
        viewHolder.dealerNameTxt.setText(data.get(i).getDealerName());
        viewHolder.serviceEngNameTxt.setText(data.get(i).getServiceEngName());
        viewHolder.mobileTxt.setText(data.get(i).getMobile());
        viewHolder.cityTxt.setText(data.get(i).getCity());
        viewHolder.deadLineTxt.setText(data.get(i).getDeadLine());
        viewHolder.closingDateTxt.setText(data.get(i).getClosingDate());
        viewHolder.dateTxt.setText(data.get(i).getDate());
        viewHolder.statusTxt.setText(data.get(i).getStatus());

        viewHolder.listComplaintMain.setOnClickListener(new View.OnClickListener() {
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

        @BindView(R.id.complaint_no_txt)
        TextView complaintNoTxt;
        @BindView(R.id.customer_name_txt)
        TextView customerNameTxt;
        @BindView(R.id.dealer_name_txt)
        TextView dealerNameTxt;
        @BindView(R.id.service_eng_name_txt)
        TextView serviceEngNameTxt;
        @BindView(R.id.mobile_txt)
        TextView mobileTxt;
        @BindView(R.id.city_txt)
        TextView cityTxt;
        @BindView(R.id.dead_line_txt)
        TextView deadLineTxt;
        @BindView(R.id.closing_date_txt)
        TextView closingDateTxt;
        @BindView(R.id.date_txt)
        TextView dateTxt;
        @BindView(R.id.status_txt)
        TextView statusTxt;
        @BindView(R.id.list_complaint_main)
        LinearLayout listComplaintMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
