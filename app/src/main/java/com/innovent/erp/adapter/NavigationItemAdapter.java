package com.innovent.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.DashboardModel;
import com.innovent.erp.model.NavigationItemModel;
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
public class NavigationItemAdapter extends RecyclerView.Adapter<NavigationItemAdapter.ViewHolder> {
    private ArrayList<NavigationItemModel> data;
    private Context context;
    MyPreferences myPreferences;

    public interface intercommunication {
        public void emptyLayout(int query);
        public void shareApp(int query);
    }

    public NavigationItemAdapter(Context context, ArrayList<NavigationItemModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_navigation_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(data.get(i).getName());
        try {
            Drawable drawable = ContextCompat.getDrawable(context, data.get(i).getImage_path());
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, data.get(i).getColor());
            viewHolder.img.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.get(i).getType() == 0) {
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.navItemLayout.setBackgroundColor(Color.parseColor("#00000000"));
            viewHolder.name.setTextColor(ContextCompat.getColor(context, R.color.divider_color));
        } else if (data.get(i).getType() == 1) {
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.navItemLayout.setBackgroundColor(Color.parseColor("#10000000"));
            viewHolder.name.setTextColor(ContextCompat.getColor(context, R.color.divider_color));
        } else {
            viewHolder.img.setVisibility(View.GONE);
            viewHolder.name.setTextColor(ContextCompat.getColor(context, R.color.white));
            viewHolder.navItemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        }

        viewHolder.navItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (GlobalElements.compareDate(myPreferences.getPreferences(MyPreferences.UDATE), "dd-MM-yyyy")) {
                        if(data.get(i).getId().equals(""))
                        {
                            intercommunication intercommunitoter = (intercommunication) context;
                            intercommunitoter.emptyLayout(0);
                        }
                        else
                        {
                            for (int j = 0; j < data.size(); j++) {
                                if (j == i) {
                                    data.get(j).setType(1);
                                } else {
                                    if (data.get(j).getType() == 2) {
                                        data.get(j).setType(2);
                                    } else {
                                        data.get(j).setType(0);
                                    }
                                }
                            }
                            notifyDataSetChanged();
                            if (data.get(i).getId().equals("")) {
                                intercommunication intercommunitoter = (intercommunication) context;
                                intercommunitoter.emptyLayout(0);
                            }else if (data.get(i).getId().equals("-1")) { // share app
                                intercommunication intercommunitoter = (intercommunication) context;
                                intercommunitoter.shareApp(0);
                            }
                            else if (data.get(i).getId().equals("7")) {
                                intercommunication intercommunitoter = (intercommunication) context;
                                intercommunitoter.emptyLayout(1);
                            } else {
                                intercommunication intercommunitoter = (intercommunication) context;
                                intercommunitoter.emptyLayout(0);
                                Intent g = new Intent(context, data.get(i).getTarget());
                                g.putExtra("type", "0");
                                context.startActivity(g);
                            }
                        }
                    }
                    else
                    {
                        Toaster.show(context, "" + context.getResources().getString(R.string.attendance), true, Toaster.DANGER);
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
        @BindView(R.id.nav_item_title)
        TextView name;
        @BindView(R.id.nav_item_img)
        ImageView img;
        @BindView(R.id.nav_item_layout)
        LinearLayout navItemLayout;

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
