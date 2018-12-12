package com.innovent.erp.visitorBookModual.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.visitorBookModual.FollowUpResponse;
import com.innovent.erp.visitorBookModual.model.FollowUpModel;

import java.util.ArrayList;


/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class FollowUpHistoryAdapter extends RecyclerView.Adapter<FollowUpHistoryAdapter.ViewHolder> {
    private ArrayList<FollowUpModel> data = new ArrayList<>();
    private Context context;

    public FollowUpHistoryAdapter(Context context, ArrayList<FollowUpModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_followup_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.desc.setText("" + data.get(i).getDescription());
        viewHolder.followup_by.setText("" + data.get(i).getThrough_slug());
        viewHolder.followup_date.setText("" + data.get(i).getFollowup_date());
        viewHolder.status.setText("" + data.get(i).getStatus_slug());
        viewHolder.followup_by_user.setText("" + data.get(i).getFollowupBy());
        viewHolder.category_name.setText("" + data.get(i).getCategoryName());
        viewHolder.visitor_name.setText("" + data.get(i).getVisitor_name());
        viewHolder.visitor_email.setText("" + data.get(i).getVisitor_email());
        viewHolder.visitor_mobile.setText("" + data.get(i).getVisitor_mobile_no());
        viewHolder.visitor_inquiry_by.setText("" + data.get(i).getInquiryBy());

        try {
            viewHolder.review_rat.setRating(Float.parseFloat("" + data.get(i).getRating()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!Validation.isValid(Validation.MOBILE, data.get(i).getVisitor_mobile_no())) {
                viewHolder.view_mobile.setVisibility(View.GONE);
            } else {
                viewHolder.view_mobile.setVisibility(View.VISIBLE);
            }

            if (!Validation.isValid(Validation.EMAIL, data.get(i).getVisitor_email())) {
                viewHolder.view_email.setVisibility(View.GONE);
            } else {
                viewHolder.view_email.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.view_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.get(i).getVisitor_mobile_no().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + data.get(i).getVisitor_mobile_no()));
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(intent);
                }
            }
        });

        viewHolder.view_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.get(i).getVisitor_email().equals("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + data.get(i).getVisitor_email()));
                    context.startActivity(emailIntent);
                }
            }
        });


        if (data.get(i).getNext_action().equals("2")) {
            if (!data.get(i).getFuture_date().equals("")) {
                viewHolder.future_layout.setVisibility(View.VISIBLE);
                viewHolder.future_date.setText("" + data.get(i).getFuture_date());
            } else {
                viewHolder.future_layout.setVisibility(View.GONE);
            }
        } else {
            viewHolder.future_layout.setVisibility(View.GONE);
        }

        if (data.get(i).getResponse().equals("")) {
            viewHolder.response_layout.setVisibility(View.GONE);
        } else {
            viewHolder.response.setText("" + data.get(i).getResponse());
            viewHolder.response_layout.setVisibility(View.VISIBLE);
        }

        viewHolder.list_inquiry_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, FollowUpResponse.class);
                    Bundle b = new Bundle();
                    b.putSerializable("data", data.get(i));
                    b.putInt("position", i);
                    intent.putExtras(b);
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
        private TextView desc, followup_by, followup_date, status, future_date, response, followup_by_user, category_name;
        TextView visitor_name, visitor_mobile, visitor_email, visitor_inquiry_by,view_email,view_mobile;
        LinearLayout list_inquiry_main, future_layout, response_layout;
        RatingBar review_rat;

        public ViewHolder(View view) {
            super(view);
            desc = (TextView) view.findViewById(R.id.description);
            followup_by = (TextView) view.findViewById(R.id.followup_by);
            followup_date = (TextView) view.findViewById(R.id.followup_date);
            future_date = (TextView) view.findViewById(R.id.future_date);
            response = (TextView) view.findViewById(R.id.response);
            status = (TextView) view.findViewById(R.id.status);
            list_inquiry_main = (LinearLayout) view.findViewById(R.id.list_inquiry_main);
            future_layout = (LinearLayout) view.findViewById(R.id.future_layout);
            response_layout = (LinearLayout) view.findViewById(R.id.response_layout);
            followup_by_user = (TextView) view.findViewById(R.id.followup_by_user);
            category_name = (TextView) view.findViewById(R.id.category_name);
            visitor_name = (TextView) view.findViewById(R.id.visitor_name);
            visitor_email = (TextView) view.findViewById(R.id.visitor_email);
            visitor_mobile = (TextView) view.findViewById(R.id.visitor_mobile);
            visitor_inquiry_by = (TextView) view.findViewById(R.id.visitor_inquiry_by);
            review_rat = (RatingBar) view.findViewById(R.id.review_rat);
            view_email = (TextView) view.findViewById(R.id.view_email);
            view_mobile = (TextView) view.findViewById(R.id.view_mobile);
        }
    }

}
