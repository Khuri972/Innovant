package com.desai.vatsal.mydynamiccalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HCL on 01-10-2016.
 */
public class ShowDayViewEventsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ShowEventsModel> showEventsModelList;

    private OnWeekDayViewClickListener onWeekDayViewClickListener;

    public ShowDayViewEventsListAdapter(Context context, ArrayList<ShowEventsModel> showEventsModelList) {
        this.context = context;
        this.showEventsModelList = showEventsModelList;
    }

    public void setOnWeekDayViewClickListener(OnWeekDayViewClickListener onWeekDayViewClickListener) {
        this.onWeekDayViewClickListener = onWeekDayViewClickListener;
    }

    class ShowEventsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_parrent, ll_sub_parrent;
        HorizontalScrollView horizontalScrollView;

        public ShowEventsViewHolder(View itemView) {
            super(itemView);
            ll_parrent = (LinearLayout) itemView.findViewById(R.id.ll_parrent);
            ll_sub_parrent = (LinearLayout) itemView.findViewById(R.id.ll_sub_parrent);
            horizontalScrollView = (HorizontalScrollView) itemView.findViewById(R.id.horizontalScrollView);

        }

        public void setShowEvents(final ShowEventsModel model) {

            ll_sub_parrent.removeAllViews();

            View row_event_layout = LayoutInflater.from(context).inflate(R.layout.day_week_event, null, false);

            row_event_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView tv_event_simbol = (TextView) row_event_layout.findViewById(R.id.tv_event_simbol);
            TextView tv_event_name = (TextView) row_event_layout.findViewById(R.id.tv_event_name);
            TextView tv_event_date = (TextView) row_event_layout.findViewById(R.id.tv_event_date);
            TextView tv_event_time = (TextView) row_event_layout.findViewById(R.id.tv_event_time);
            LinearLayout ll_month_events = (LinearLayout) row_event_layout.findViewById(R.id.ll_month_events);
            View v_divider = (View) row_event_layout.findViewById(R.id.v_divider);

            v_divider.setVisibility(View.GONE);

            tv_event_name.setText("");
            tv_event_date.setText("");
            tv_event_time.setText("");

            ll_sub_parrent.addView(row_event_layout);

            setDayEvents(model);

        }

        public void setDayEvents(final ShowEventsModel model) {
            int countEvent = 0;
            // set event color
            for (int i = 0; i < AppConstants.eventList.size(); i++) {
                if (AppConstants.eventList.get(i).getStrDate().
                        equals(AppConstants.sdfDate.format(model.getDates()))) {
                    if (countEvent == 0) {
                        //ll_sub_parrent.removeAllViews();
                    }

                    if (AppConstants.eventList.get(i).getEvent_type() == MyDynamicCalendar.BIRTHDAY_EVENT) {
                        if (GlobalMethods.convertDate("00:00", AppConstants.sdfHourMinute, AppConstants.sdfHour)
                                .equals(GlobalMethods.convertDate(model.getHours(),
                                        AppConstants.sdfHourMinute, AppConstants.sdfHour)))
                            setLayout(i, model, MyDynamicCalendar.BIRTHDAY_EVENT);
                    } else if (AppConstants.eventList.get(i).getEvent_type() == MyDynamicCalendar.HOLYDAY_EVENT) {
                        if (GlobalMethods.convertDate("00:00", AppConstants.sdfHourMinute, AppConstants.sdfHour)
                                .equals(GlobalMethods.convertDate(model.getHours(),
                                        AppConstants.sdfHourMinute, AppConstants.sdfHour)))
                        setLayout(i, model, MyDynamicCalendar.HOLYDAY_EVENT);
                    } else if (GlobalMethods.convertDate(AppConstants.eventList.get(i).getStrStartTime(),
                            AppConstants.sdfHourMinute, AppConstants.sdfHour)
                            .equals(GlobalMethods.convertDate(model.getHours(),
                                    AppConstants.sdfHourMinute, AppConstants.sdfHour))) {
                        setLayout(i, model, MyDynamicCalendar.ONE_DAY_EVENT);
                    }
                }
                countEvent++;
            }
        }

        private void setLayout(final int i, final ShowEventsModel model, int event_type) {

            View row_event_layout = LayoutInflater.from(context).inflate(R.layout.day_week_event,null, false);

            row_event_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWeekDayViewClickListener.onClick(AppConstants.eventList.get(i).getEventId(), model.getHours());
                }
            });

            row_event_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onWeekDayViewClickListener.onLongClick(AppConstants.eventList.get(i).getEventId(), model.getHours());
                    return true;
                }
            });

            TextView tv_event_simbol = (TextView) row_event_layout.findViewById(R.id.tv_event_simbol);
            TextView tv_event_name = (TextView) row_event_layout.findViewById(R.id.tv_event_name);
            TextView tv_event_date = (TextView) row_event_layout.findViewById(R.id.tv_event_date);
            TextView tv_event_time = (TextView) row_event_layout.findViewById(R.id.tv_event_time);
            LinearLayout ll_month_events = (LinearLayout) row_event_layout.findViewById(R.id.ll_month_events);
            View v_divider = (View) row_event_layout.findViewById(R.id.v_divider);
            v_divider.setVisibility(View.VISIBLE);

            tv_event_date.setText(AppConstants.eventList.get(i).getStrDate());
//                    tv_event_date.setText(AppConstants.sdfDate.format(model.getDates()));

            switch (event_type) {
                case MyDynamicCalendar.BIRTHDAY_EVENT:
                    tv_event_name.setText(AppConstants.eventList.get(i).getStrName() + " 's BirthDay");
                    break;
                case MyDynamicCalendar.HOLYDAY_EVENT:
                    tv_event_name.setText(AppConstants.eventList.get(i).getStrName());
                    break;
                default:
                    tv_event_name.setText(AppConstants.eventList.get(i).getStrName());
                    tv_event_time.setText(String.format("%s to %s", AppConstants.eventList.get(i).getStrStartTime(), AppConstants.eventList.get(i).getStrEndTime()));
//                    tv_event_time.setText(AppConstants.sdfHour.format(model.getHours()));
                    break;
            }

            if (AppConstants.eventList.get(i).getImage() != -1) {
                tv_event_simbol.setBackgroundResource(AppConstants.eventList.get(i).getImage());
            } else {
                tv_event_simbol.setBackgroundResource(R.drawable.event_view);
            }

            if (AppConstants.eventCellBackgroundColor != -1) {
                ll_month_events.setBackgroundColor(AppConstants.eventCellBackgroundColor);
            }

            if (!AppConstants.strEventCellBackgroundColor.equals("null")) {
                ll_month_events.setBackgroundColor(Color.parseColor(AppConstants.strEventCellBackgroundColor));
            }

            if (AppConstants.eventCellTextColor != -1) {
                tv_event_name.setTextColor(AppConstants.eventCellTextColor);
                tv_event_date.setTextColor(AppConstants.eventCellTextColor);
                tv_event_time.setTextColor(AppConstants.eventCellTextColor);
            }

            if (!AppConstants.strEventCellTextColor.equals("null")) {
                tv_event_name.setTextColor(Color.parseColor(AppConstants.strEventCellTextColor));
                tv_event_date.setTextColor(Color.parseColor(AppConstants.strEventCellTextColor));
                tv_event_time.setTextColor(Color.parseColor(AppConstants.strEventCellTextColor));
            }

            ll_sub_parrent.addView(row_event_layout);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_day_view_show_events, parent, false);
        return new ShowEventsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShowEventsModel showEventsModel = showEventsModelList.get(position);
        ShowEventsViewHolder showEventsViewHolder = (ShowEventsViewHolder) holder;
        showEventsViewHolder.setShowEvents(showEventsModel);
    }

    @Override
    public int getItemCount() {
        return showEventsModelList.size();
    }

}
