package com.innovent.erp.visitorBookModual.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.visitorBookModual.FollowupHistory;
import com.innovent.erp.visitorBookModual.model.ViewModel;

import java.util.ArrayList;

/**
 * Created by Craftbox-4 on 06-Sep-17.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private ArrayList<ViewModel> data;
    private Context context;
    MyPreferences myPreferences;

    public interface Intercommunication {
        public void addRating(int position, String visitor_id);
    }

    public ViewAdapter(Context context, ArrayList<ViewModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_visitor_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.view_name_tv.setText(data.get(i).getName());
        viewHolder.view_mobile_tv.setText(data.get(i).getMobile_no());
        viewHolder.view_massage_tv.setText(Html.fromHtml(data.get(i).getDetail()));
        viewHolder.view_date_tv.setText(data.get(i).getCreated_date());
        viewHolder.view_email_tv.setText(data.get(i).getEmail());
        viewHolder.view_category_tv.setText(data.get(i).getCategory_name());
        viewHolder.reference_tv.setText(data.get(i).getReference());
        viewHolder.inquiryBy.setText("" + data.get(i).getInquiryBy());

        if (data.get(i).getProject_manager_name().equals("")) {
            viewHolder.project_manager_layout.setVisibility(View.GONE);
        } else {
            viewHolder.project_manager_name.setText("Project Manager -: " + data.get(i).getProject_manager_name());
            viewHolder.project_manager_layout.setVisibility(View.GONE);
        }

        try {
            viewHolder.review_rat.setRating(Float.parseFloat("" + data.get(i).getRating()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!Validation.isValid(Validation.MOBILE, data.get(i).getMobile_no())) {
                viewHolder.view_mobile.setVisibility(View.GONE);
            } else {
                viewHolder.view_mobile.setVisibility(View.VISIBLE);
            }

            if (!Validation.isValid(Validation.EMAIL, data.get(i).getEmail())) {
                viewHolder.view_email.setVisibility(View.GONE);
            } else {
                viewHolder.view_email.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (myPreferences.getPreferences(MyPreferences.followup).equals("1")) {
            viewHolder.view_mobile.setVisibility(View.VISIBLE);
            viewHolder.view_email.setVisibility(View.VISIBLE);
        } else {
            viewHolder.view_mobile.setVisibility(View.GONE);
            viewHolder.view_email.setVisibility(View.GONE);
        }

        viewHolder.view_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.get(i).getMobile_no().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + data.get(i).getMobile_no()));
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
                if (!data.get(i).getEmail().equals("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + data.get(i).getEmail()));
                    context.startActivity(emailIntent);
                }
            }
        });

        viewHolder.list_inquiry_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myPreferences.getPreferences(MyPreferences.followup).equals("1")) {
                    Intent intent = new Intent(context, FollowupHistory.class);
                    intent.putExtra("visitor_id", "" + data.get(i).getId());
                    intent.putExtra("visitor_name", "" + data.get(i).getName());
                    context.startActivity(intent);
                }

            }
        });

        viewHolder.rat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intercommunication intercommunication = (Intercommunication) context;
                    intercommunication.addRating(i, data.get(i).getId());
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
        TextView view_name_tv, view_mobile_tv, view_massage_tv, view_date_tv, view_email_tv, view_category_tv, reference_tv, project_manager_name, inquiryBy;
        TextView view_mobile, view_email;
        LinearLayout list_inquiry_main, rat_layout, project_manager_layout;
        RatingBar review_rat;

        public ViewHolder(View view) {
            super(view);
            view_name_tv = (TextView) view.findViewById(R.id.view_name_tv);
            view_mobile_tv = (TextView) view.findViewById(R.id.view_mobile_tv);
            view_mobile = (TextView) view.findViewById(R.id.view_mobile);
            view_massage_tv = (TextView) view.findViewById(R.id.view_massage_tv);
            view_date_tv = (TextView) view.findViewById(R.id.view_date_tv);
            view_email_tv = (TextView) view.findViewById(R.id.view_email_tv);
            view_email = (TextView) view.findViewById(R.id.view_email);
            view_category_tv = (TextView) view.findViewById(R.id.view_category_tv);
            reference_tv = (TextView) view.findViewById(R.id.reference_tv);
            list_inquiry_main = (LinearLayout) view.findViewById(R.id.list_inquiry_main);
            project_manager_layout = (LinearLayout) view.findViewById(R.id.project_manager_layout);
            rat_layout = (LinearLayout) view.findViewById(R.id.rat_layout);
            review_rat = (RatingBar) view.findViewById(R.id.review_rat);
            project_manager_name = (TextView) view.findViewById(R.id.project_manager_name);
            inquiryBy = (TextView) view.findViewById(R.id.view_inquiry_by);
        }
    }
}
