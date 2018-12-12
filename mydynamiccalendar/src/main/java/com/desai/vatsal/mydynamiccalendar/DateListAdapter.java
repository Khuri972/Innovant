package com.desai.vatsal.mydynamiccalendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vatsaldesai on 23-09-2016.
 */
public class DateListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<DateModel> dateModelList;

    private Calendar calendar1 = Calendar.getInstance();
    private Date date_current_date = calendar1.getTime();

    private OnMonthBellowEventsDateClickListener onMonthBellowEventsDateClickListener;
    private OnDateClickListener onDateClickListener;

    public DateListAdapter(Context context, ArrayList<DateModel> dateModelList) {
        this.context = context;
        this.dateModelList = dateModelList;
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public void setOnMonthBellowEventsClick(OnMonthBellowEventsDateClickListener onMonthBellowEventsDateClickListener) {
        this.onMonthBellowEventsDateClickListener = onMonthBellowEventsDateClickListener;
    }

    class DateViewHolder extends RecyclerView.ViewHolder {

        TextView tv_month_date, tv_week_date;//, tv_event_simbol;
        LinearLayout ll_sub_parrent, ll_event_image;

        public DateViewHolder(View itemView) {
            super(itemView);
            tv_month_date = (TextView) itemView.findViewById(R.id.tv_month_date);
            tv_week_date = (TextView) itemView.findViewById(R.id.tv_week_date);
            ll_sub_parrent = (LinearLayout) itemView.findViewById(R.id.ll_sub_parrent);
            // tv_event_simbol = (TextView) itemView.findViewById(R.id.tv_event_simbol);
            ll_event_image = (LinearLayout) itemView.findViewById(R.id.ll_event_image);
        }

        public void setDates(final DateModel model) {

            try {
                Date date_current_month_date = AppConstants.main_calendar.getTime();
                // set month view dates
                if (model.getFlag().equals("month")) {
                    tv_week_date.setVisibility(View.GONE);
                    tv_month_date.setVisibility(View.VISIBLE);

                    tv_month_date.setText(String.valueOf(model.getDates().getDate()));

                    // set extra dates of month color & current dates of month color
                    if (model.getDates().getMonth() == date_current_month_date.getMonth() && model.getDates().getYear() == date_current_month_date.getYear()) {

                        if (AppConstants.datesBackgroundColor != -1) {
                            ll_sub_parrent.setBackgroundColor(AppConstants.datesBackgroundColor);
                        }

                        if (!AppConstants.strDatesBackgroundColor.equals("null")) {
                            ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strDatesBackgroundColor));
                        }

                        if (AppConstants.datesTextColor != -1) {
                            tv_month_date.setTextColor(AppConstants.datesTextColor);
                        }

                        if (!AppConstants.strDatesTextColor.equals("null")) {
                            tv_month_date.setTextColor(Color.parseColor(AppConstants.strDatesTextColor));
                        } else {
                            tv_month_date.setTextColor(context.getResources().getColor(R.color.black));
                        }

                    } else {

                        if (AppConstants.extraDatesBackgroundColor != -1) {
                            ll_sub_parrent.setBackgroundColor(AppConstants.extraDatesBackgroundColor);
                        }

                        if (!AppConstants.strExtraDatesBackgroundColor.equals("null")) {
                            ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strExtraDatesBackgroundColor));
                        }

                        if (AppConstants.extraDatesTextColor != -1) {
                            tv_month_date.setTextColor(AppConstants.extraDatesTextColor);
                        }

                        if (!AppConstants.strExtraDatesTextColor.equals("null")) {
                            tv_month_date.setTextColor(Color.parseColor(AppConstants.strExtraDatesTextColor));
                        } else {
                            tv_month_date.setTextColor(context.getResources().getColor(R.color.whiteDark8));
                        }

                    }

                    // set all saturday color
                    if (AppConstants.isSaturdayOff) {

                        if (new SimpleDateFormat("EEEE").format(model.getDates()).equals("Saturday")) {
                            if (AppConstants.saturdayOffBackgroundColor != -1) {
                                ll_sub_parrent.setBackgroundColor(AppConstants.saturdayOffBackgroundColor);
                            }

                            if (!AppConstants.strSaturdayOffBackgroundColor.equals("null")) {
                                ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strSaturdayOffBackgroundColor));
                            }

                            if (AppConstants.saturdayOffTextColor != -1) {
                                tv_month_date.setTextColor(AppConstants.saturdayOffTextColor);
                            }

                            if (!AppConstants.strSaturdayOffTextColor.equals("null")) {
                                tv_month_date.setTextColor(Color.parseColor(AppConstants.strSaturdayOffTextColor));
                            }

                        }
                    }

                    // set all sunday color
                    if (AppConstants.isSundayOff) {

                        if (new SimpleDateFormat("EEEE").format(model.getDates()).equals("Sunday")) {

                            if (AppConstants.sundayOffBackgroundColor != -1) {
                                ll_sub_parrent.setBackgroundColor(AppConstants.sundayOffBackgroundColor);
                            }

                            if (!AppConstants.strSundayOffBackgroundColor.equals("null")) {
                                ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strSundayOffBackgroundColor));
                            }

                            if (AppConstants.sundayOffTextColor != -1) {
                                tv_month_date.setTextColor(AppConstants.sundayOffTextColor);
                            }

                            if (!AppConstants.strSundayOffTextColor.equals("null")) {
                                tv_month_date.setTextColor(Color.parseColor(AppConstants.strSundayOffTextColor));
                            }

                        }
                    }

                    // set holiday color
                    for (int i = 0; i < AppConstants.holidayList.size(); i++) {
                        if (AppConstants.holidayList.get(i).equals(AppConstants.sdfDate.format(model.getDates()))) {
                            if (AppConstants.holidayCellBackgroundColor != -1) {
                                ll_sub_parrent.setBackgroundColor(AppConstants.holidayCellBackgroundColor);
                            }

                            if (!AppConstants.strHolidayCellBackgroundColor.equals("null")) {
                                ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strHolidayCellBackgroundColor));
                            }

                            if (AppConstants.holidayCellTextColor != -1) {
                                tv_month_date.setTextColor(AppConstants.holidayCellTextColor);
                            }

                            if (!AppConstants.strHolidayCellTextColor.equals("null")) {
                                tv_month_date.setTextColor(Color.parseColor(AppConstants.strHolidayCellTextColor));
                            }

                            if (AppConstants.isHolidayCellClickable) {
                                ll_sub_parrent.setClickable(true);
                                ll_sub_parrent.setEnabled(true);
                            } else {
                                ll_sub_parrent.setClickable(false);
                                ll_sub_parrent.setEnabled(false);
                            }

                        }
                    }

                    for (int i = 0; i < AppConstants.eventList.size(); i++) {
                        if (AppConstants.eventList.get(i).getStrDate().equals(AppConstants.sdfDate.format(model.getDates()))) {

//                        tv_event_simbol.setVisibility(View.VISIBLE);
                            if (AppConstants.eventList.get(i).getImage() != -1) {

                                LinearLayout a = new LinearLayout(context);
                                a.setOrientation(LinearLayout.HORIZONTAL);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.setMargins(8, 0, 8, 1);

                                //set imageview
                                ImageView img = new ImageView(context);
                                img.setBackgroundResource(AppConstants.eventList.get(i).getImage());
                                int width = context.getResources().getDimensionPixelSize(R.dimen.event_image_size);
                                int height = context.getResources().getDimensionPixelSize(R.dimen.event_image_size);
                                LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(width, height);
                                img_params.setMargins(0, 0, 8, 0);
                                img.setLayoutParams(img_params);

                                //set text view
                                TextView tv = new TextView(context);
                                int height1 = context.getResources().getDimensionPixelSize(R.dimen.event_image_size);
                                LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        height1);
                                tv_params.setMargins(0, 0, 8, 0);
                                tv.setText(AppConstants.eventList.get(i).getStrName());
                                tv.setLayoutParams(tv_params);
                                tv.setTextSize(8);
                                tv.setBackgroundResource(R.drawable.event_view);
                                tv.setTextColor(context.getResources().getColor(R.color.white));
                                tv.setPadding(8, 0, 0, 1);
                                //add views to ll
                                a.addView(img);
                                a.addView(tv);
                                ll_event_image.addView(a);
//
//                            tv_event_simbol.setBackgroundResource(AppConstants.eventList.get(i).getImage());
                            } else {
                                try {
                                    TextView tv = new TextView(context);
                                    tv.setBackgroundResource(R.drawable.event_view);
                                    int height = context.getResources().getDimensionPixelSize(R.dimen.event_image_size);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            height);

                                    switch (AppConstants.eventList.get(i).getEvent_type()) {
                                        case MyDynamicCalendar.ONE_DAY_EVENT:
                                            tv.setPadding(8, 0, 8, 0);
                                            tv.setText(AppConstants.eventList.get(i).getStrName());
                                            params.setMargins(8, 0, 8, 1);
                                            tv.setLayoutParams(params);
                                            tv.setTextSize(8);
                                            tv.setTextColor(context.getResources().getColor(R.color.white));
                                            ll_event_image.addView(tv);
                                            break;

                                        case MyDynamicCalendar.MULTIPLEDAY_EVENT_LEFT:
                                            AppConstants.eventList.get(i).setEvent_count(ll_event_image.getChildCount());
                                            tv.setPadding(8, 0, 0, 0);
                                            tv.setText(AppConstants.eventList.get(i).getStrName());
                                            params.setMargins(8, 0, 0, 1);
                                            tv.setLayoutParams(params);
                                            tv.setTextSize(8);
                                            tv.setTextColor(context.getResources().getColor(R.color.white));
                                            ll_event_image.addView(tv);

                                            break;

                                        case MyDynamicCalendar.MULTIPLEDAY_EVENT_CENTER:

                                            int previousViewPosition = AppConstants.eventList.get(i - 1).getEvent_count();
                                            AppConstants.eventList.get(i).setEvent_count(previousViewPosition);
                                            tv.setPadding(0, 0, 0, 0);
                                            //                                    tv.setText(AppConstants.eventList.get(i).getStrName());
                                            params.setMargins(0, 0, 0, 1);
                                            tv.setLayoutParams(params);
                                            tv.setTextSize(8);
                                            tv.setTextColor(context.getResources().getColor(R.color.white));
                                            int childcount = ll_event_image.getChildCount();

                                            if (previousViewPosition > childcount) {
                                                for (int j = childcount; j < (previousViewPosition - childcount); j++) {
                                                    TextView tv1 = new TextView(context);
                                                    tv1.setLayoutParams(params);
                                                    tv1.setPadding(0, 0, 0, 0);
                                                    ll_event_image.addView(tv1);
                                                }
                                                ll_event_image.addView(tv, previousViewPosition);
                                            } else
                                                ll_event_image.addView(tv, previousViewPosition);

                                            break;

                                        case MyDynamicCalendar.MULTIPLEDAY_EVENT_RIGHT:
                                            tv.setPadding(0, 0, 8, 0);
                                            //                                    tv.setText(AppConstants.eventList.get(i).getStrName());
                                            params.setMargins(0, 0, 8, 1);
                                            tv.setLayoutParams(params);
                                            tv.setTextSize(8);
                                            tv.setTextColor(context.getResources().getColor(R.color.white));
                                            int previousViewPosition1 = AppConstants.eventList.get(i - 1).getEvent_count();
                                            int childcount1 = ll_event_image.getChildCount();
                                            if (previousViewPosition1 > childcount1) {
                                                for (int j = childcount1; j < (previousViewPosition1 - childcount1); j++) {
                                                    TextView tv1 = new TextView(context);
                                                    tv1.setLayoutParams(params);
                                                    tv1.setPadding(0, 0, 0, 0);
                                                    ll_event_image.addView(tv1);
                                                }
                                                ll_event_image.addView(tv, previousViewPosition1);
                                            } else
                                                ll_event_image.addView(tv, previousViewPosition1);
                                            break;
                                    }

                                    if (AppConstants.eventCellBackgroundColor != -1) {
                                        ll_sub_parrent.setBackgroundColor(AppConstants.eventCellBackgroundColor);
                                    }

                                    if (!AppConstants.strEventCellBackgroundColor.equals("null")) {
                                        ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strEventCellBackgroundColor));
                                    }

                                    if (AppConstants.eventCellTextColor != -1) {
                                        tv_month_date.setTextColor(AppConstants.eventCellTextColor);
                                    }

                                    if (!AppConstants.strEventCellTextColor.equals("null")) {
                                        tv_month_date.setTextColor(Color.parseColor(AppConstants.strEventCellTextColor));
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("Error in display", e.toString());
                                }
                            }
                            // count++;
                        } else {
//                        tv_event_simbol.setVisibility(View.VISIBLE);
                        }
                    }

                    // set current date color
                    if (model.getDates().getDate() == date_current_date.getDate() && model.getDates().getMonth() == date_current_date.getMonth() && model.getDates().getYear() == date_current_date.getYear()) {

                        if (AppConstants.currentDateBackgroundColor != -1) {
                            ll_sub_parrent.setBackgroundColor(AppConstants.currentDateBackgroundColor);
                        }

                        if (!AppConstants.strCurrentDateBackgroundColor.equals("null")) {
                            ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strCurrentDateBackgroundColor));
                        }

                        if (AppConstants.currentDateTextColor != -1) {
                            tv_month_date.setTextColor(AppConstants.currentDateTextColor);
                        }

                        if (!AppConstants.strCurrentDateTextColor.equals("null")) {
                            tv_month_date.setTextColor(Color.parseColor(AppConstants.strCurrentDateTextColor));
                        } else {
                            tv_month_date.setTextColor(Color.BLUE);
                        }

                    }

                }
                // set week view dates
                else if (model.getFlag().equals("week")) {
                    tv_month_date.setVisibility(View.GONE);
                    tv_week_date.setVisibility(View.VISIBLE);
                    tv_week_date.setText(String.valueOf(model.getDates().getDate()));

                    // set extra dates of month color & current dates of month color
                    if (model.getDates().getMonth() == date_current_month_date.getMonth() && model.getDates().getYear() == date_current_month_date.getYear()) {

                        if (AppConstants.datesBackgroundColor != -1) {
                            ll_sub_parrent.setBackgroundColor(AppConstants.datesBackgroundColor);
                        }

                        if (!AppConstants.strDatesBackgroundColor.equals("null")) {
                            ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strDatesBackgroundColor));
                        }

                        if (AppConstants.datesTextColor != -1) {
                            tv_week_date.setTextColor(AppConstants.datesTextColor);
                        }

                        if (!AppConstants.strDatesTextColor.equals("null")) {
                            tv_week_date.setTextColor(Color.parseColor(AppConstants.strDatesTextColor));
                        } else {
                            tv_week_date.setTextColor(context.getResources().getColor(R.color.black));
                        }

                    } else {

                        if (AppConstants.extraDatesBackgroundColor != -1) {
                            ll_sub_parrent.setBackgroundColor(AppConstants.extraDatesBackgroundColor);
                        }

                        if (!AppConstants.strExtraDatesBackgroundColor.equals("null")) {
                            ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strExtraDatesBackgroundColor));
                        }

                        if (AppConstants.extraDatesTextColor != -1) {
                            tv_week_date.setTextColor(AppConstants.extraDatesTextColor);
                        }

                        if (!AppConstants.strExtraDatesTextColor.equals("null")) {
                            tv_week_date.setTextColor(Color.parseColor(AppConstants.strExtraDatesTextColor));
                        } else {
                            tv_week_date.setTextColor(context.getResources().getColor(R.color.whiteDark8));
                        }
                    }

                    // set all saturday color
                    if (AppConstants.isSaturdayOff) {

                        if (new SimpleDateFormat("EEEE").format(model.getDates()).equals("Saturday")) {
                            if (AppConstants.saturdayOffBackgroundColor != -1) {
                                ll_sub_parrent.setBackgroundColor(AppConstants.saturdayOffBackgroundColor);
                            }

                            if (!AppConstants.strSaturdayOffBackgroundColor.equals("null")) {
                                ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strSaturdayOffBackgroundColor));
                            }

                            if (AppConstants.saturdayOffTextColor != -1) {
                                tv_week_date.setTextColor(AppConstants.saturdayOffTextColor);
                            }

                            if (!AppConstants.strSaturdayOffTextColor.equals("null")) {
                                tv_week_date.setTextColor(Color.parseColor(AppConstants.strSaturdayOffTextColor));
                            }
                        }
                    }

                    // set all sunday color
                    if (AppConstants.isSundayOff) {

                        if (new SimpleDateFormat("EEEE").format(model.getDates()).equals("Sunday")) {

                            if (AppConstants.sundayOffBackgroundColor != -1) {
                                ll_sub_parrent.setBackgroundColor(AppConstants.sundayOffBackgroundColor);
                            }

                            if (!AppConstants.strSundayOffBackgroundColor.equals("null")) {
                                ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strSundayOffBackgroundColor));
                            }

                            if (AppConstants.sundayOffTextColor != -1) {
                                tv_week_date.setTextColor(AppConstants.sundayOffTextColor);
                            }

                            if (!AppConstants.strSundayOffTextColor.equals("null")) {
                                tv_week_date.setTextColor(Color.parseColor(AppConstants.strSundayOffTextColor));
                            }
                        }
                    }

                    // set holiday color
                    for (int i = 0; i < AppConstants.holidayList.size(); i++) {
                        if (AppConstants.holidayList.get(i).equals(AppConstants.sdfDate.format(model.getDates()))) {

                            if (AppConstants.holidayCellBackgroundColor != -1) {
                                ll_sub_parrent.setBackgroundColor(AppConstants.holidayCellBackgroundColor);
                            }

                            if (!AppConstants.strHolidayCellBackgroundColor.equals("null")) {
                                ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strHolidayCellBackgroundColor));
                            }

                            if (AppConstants.holidayCellTextColor != -1) {
                                tv_month_date.setTextColor(AppConstants.holidayCellTextColor);
                            }

                            if (!AppConstants.strHolidayCellTextColor.equals("null")) {
                                tv_month_date.setTextColor(Color.parseColor(AppConstants.strHolidayCellTextColor));
                            }

                            if (AppConstants.isHolidayCellClickable) {
                                ll_sub_parrent.setClickable(true);
                                ll_sub_parrent.setEnabled(true);
                            } else {
                                ll_sub_parrent.setClickable(false);
                                ll_sub_parrent.setEnabled(false);
                            }
                        }
                    }

                    // set event color
                    for (int i = 0; i < AppConstants.eventList.size(); i++) {
                        if (AppConstants.eventList.get(i).getStrDate().equals(AppConstants.sdfDate.format(model.getDates()))) {
                            // tv_event_simbol.setVisibility(View.VISIBLE);

                            if (AppConstants.eventList.get(i).getImage() != -1) {
                                //tv_event_simbol.setBackgroundResource(AppConstants.eventList.get(i).getImage());
                            } else {
                                // tv_event_simbol.setBackgroundResource(R.drawable.event_view);
                            }

                            if (AppConstants.eventCellBackgroundColor != -1) {
                                ll_sub_parrent.setBackgroundColor(AppConstants.eventCellBackgroundColor);
                            }

                            if (!AppConstants.strEventCellBackgroundColor.equals("null")) {
                                ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strEventCellBackgroundColor));
                            }

                            if (AppConstants.eventCellTextColor != -1) {
                                tv_week_date.setTextColor(AppConstants.eventCellTextColor);
                            }

                            if (!AppConstants.strEventCellTextColor.equals("null")) {
                                tv_week_date.setTextColor(Color.parseColor(AppConstants.strEventCellTextColor));
                            }

                        } else {
                            //tv_event_simbol.setVisibility(View.VISIBLE);
                        }
                    }

                    // set current date color
                    if (model.getDates().getDate() == date_current_date.getDate() && model.getDates().getMonth() == date_current_date.getMonth() && model.getDates().getYear() == date_current_date.getYear()) {

                        if (AppConstants.currentDateBackgroundColor != -1) {
                            ll_sub_parrent.setBackgroundColor(AppConstants.currentDateBackgroundColor);
                        }

                        if (!AppConstants.strCurrentDateBackgroundColor.equals("null")) {
                            ll_sub_parrent.setBackgroundColor(Color.parseColor(AppConstants.strCurrentDateBackgroundColor));
                        }

                        if (AppConstants.currentDateTextColor != -1) {
                            tv_week_date.setTextColor(AppConstants.currentDateTextColor);
                        }

                        if (!AppConstants.strCurrentDateTextColor.equals("null")) {
                            tv_week_date.setTextColor(Color.parseColor(AppConstants.strCurrentDateTextColor));
                        } else {
                            tv_week_date.setTextColor(Color.BLUE);
                        }
                    }
                }

                ll_sub_parrent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDateClickListener.onClick(model.getDates());
                        if (AppConstants.isShowMonthWithBellowEvents || AppConstants.isAgenda) {
                            onMonthBellowEventsDateClickListener.onClick(model.getDates());
                        }
                    }
                });

                ll_sub_parrent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onDateClickListener.onLongClick(model.getDates());
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.row_date, parent, false);
        return new DateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DateModel dateModel = dateModelList.get(position);
        DateViewHolder dateViewHolder = (DateViewHolder) holder;
        dateViewHolder.setDates(dateModel);
    }

    @Override
    public int getItemCount() {
        return dateModelList.size();
    }

}
