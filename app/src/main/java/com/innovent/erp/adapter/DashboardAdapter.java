package com.innovent.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.CominingSoonActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.DashboardModel;
import com.innovent.erp.netUtils.MyPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private ArrayList<DashboardModel> data;
    private Context context;
    MyPreferences myPreferences;

    public interface intercommunication {
        public void Logout(int query);
        public void attendance(int query);
    }

    public DashboardAdapter(Context context, ArrayList<DashboardModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_dashboard, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(data.get(i).getName());
        try {
            Drawable drawable = ContextCompat.getDrawable(context, data.get(i).getImage_path());
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable,  data.get(i).getColor());
            viewHolder.img.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.list_dashboard_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!myPreferences.getPreferences(MyPreferences.Execuvive_out).equals("")) {
                        if (LogoutTime(myPreferences.getPreferences(MyPreferences.Execuvive_out))) {
                            //  true
                            myPreferences.setPreferences(MyPreferences.UDATE, "");
                        }
                    }

                    if(data.get(i).getId().equals("00"))
                    {
                        intercommunication intercommunitoter = (intercommunication) context;
                        intercommunitoter.attendance(1);
                    }
                    else if(data.get(i).getId().equals("01"))
                    {
                        try {
                            Intent intent = new Intent(context, CominingSoonActivity.class);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        if (GlobalElements.compareDate(myPreferences.getPreferences(MyPreferences.UDATE), "dd-MM-yyyy")) {
                            if (data.get(i).getId().equals("0")) {
                                intercommunication intercommunitoter = (intercommunication) context;
                                intercommunitoter.Logout(1);
                            } else {
                                Intent g = new Intent(context, data.get(i).getTarget());
                                g.putExtra("type", "0");
                                context.startActivity(g);
                            }
                        } else {
                            if (data.get(i).getId().equals("0")) {
                                intercommunication intercommunitoter = (intercommunication) context;
                                intercommunitoter.Logout(1);
                            } else {
                                Toaster.show(context, "" + context.getResources().getString(R.string.attendance), true, Toaster.DANGER);
                            }
                        }
                    }

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
        @BindView(R.id.list_dashboard_name)
        TextView name;
        @BindView(R.id.list_dashboard_img)
        ImageView img;
        @BindView(R.id.list_dashboard_main)
        LinearLayout list_dashboard_main;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static boolean LogoutTime(String start_time) {
        try {
            // current time
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String ctime = dateFormat.format(date);
            Date startime = dateFormat.parse(start_time);
            Date current_time = dateFormat.parse(ctime);
            if (current_time.after(startime)) {
                System.out.println("Yes");
                return true;
            } else {
                System.out.println("No");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
