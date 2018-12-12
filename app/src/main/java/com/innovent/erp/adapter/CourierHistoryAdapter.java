package com.innovent.erp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.CourierDetailActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.IndividualContactDetail;
import com.innovent.erp.R;
import com.innovent.erp.model.CourierModel;
import com.innovent.erp.model.IndividualContactHistoryModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.shts.android.library.TriangleLabelView;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class CourierHistoryAdapter extends RecyclerView.Adapter<CourierHistoryAdapter.ViewHolder> {
    private ArrayList<CourierModel> data;
    private Context context;
    MyPreferences myPreferences;

    public CourierHistoryAdapter(Context context, ArrayList<CourierModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_courier_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.courierNo.setText("#" + data.get(i).getCourier_no());
        viewHolder.shippingDate.setText("" + data.get(i).getShipping_date());
        viewHolder.deliveredDate.setText("" + data.get(i).getShipping_deliverd_date());
        viewHolder.trackingNo.setText("" + data.get(i).getTracking_no());
        viewHolder.status.setText("" + data.get(i).getDelivery_status());
        viewHolder.senderCompany.setText("" + GlobalElements.fromHtml("<b>C : </b>" + data.get(i).getSender_company_name()));
        viewHolder.senderPerson.setText("" + GlobalElements.fromHtml("<b>P : </b>" + data.get(i).getSender_person_name()));
        viewHolder.reciverCompany.setText("" + GlobalElements.fromHtml("<b>C : </b>" + data.get(i).getReceiver_company_name()));
        viewHolder.reciverPerson.setText("" + GlobalElements.fromHtml("<b>P : </b>" + data.get(i).getReceiver_person_name()));

        viewHolder.courierType.setVisibility(View.GONE);
        if (data.get(i).getCourier_type().equals("1")) {
            viewHolder.courierType.setPrimaryText("Sender");
            viewHolder.courierType.setTriangleBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else {
            viewHolder.courierType.setPrimaryText("Reciver");
            viewHolder.courierType.setTriangleBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        }

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, CourierDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data.get(i));
                    intent.putExtras(bundle);
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
        @BindView(R.id.courier_no)
        TextView courierNo;
        @BindView(R.id.shipping_date)
        TextView shippingDate;
        @BindView(R.id.delivered_date)
        TextView deliveredDate;
        @BindView(R.id.tracking_no)
        TextView trackingNo;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.sender_company)
        TextView senderCompany;
        @BindView(R.id.sender_person)
        TextView senderPerson;
        @BindView(R.id.reciver_company)
        TextView reciverCompany;
        @BindView(R.id.reciver_person)
        TextView reciverPerson;

        @BindView(R.id.main_layout)
        LinearLayout mainLayout;

        @BindView(R.id.courier_type)
        TriangleLabelView courierType;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
