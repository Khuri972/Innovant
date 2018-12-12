package com.innovent.erp.schedualReminder;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.innovent.erp.R;
import com.innovent.erp.schedualReminder.dialog.EventDialog;
import com.innovent.erp.schedualReminder.dialog.HolidayDialog;
import com.innovent.erp.schedualReminder.model.EventData;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.desai.vatsal.mydynamiccalendar.OnEventClickListener;
import com.desai.vatsal.mydynamiccalendar.OnWeekDayViewClickListener;
import com.github.clans.fab.FloatingActionMenu;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SchedualReminderActivity extends AppCompatActivity implements EventDialog.EventIntercommunicator,HolidayDialog.HolidayIntercommunicator {

    private MyDynamicCalendar myCalendar;
    FloatingActionMenu fabDashboardMenu;
    com.github.clans.fab.FloatingActionButton month,dayView,addEvent,addHoliday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedual_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Schedual With Reminder");
        myCalendar = (MyDynamicCalendar) findViewById(R.id.myCalendar);
        fabDashboardMenu  = (FloatingActionMenu) findViewById(R.id.fabMenu);
        month = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.month_view);
        dayView = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.day_view);
        addEvent = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.add_event);
        addHoliday = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.add_holiday);

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabDashboardMenu.close(true);
                showMonthView();
            }
        });

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabDashboardMenu.close(true);
                Calendar cal = Calendar.getInstance();
                showDayView(cal);
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabDashboardMenu.close(true);
                EventDialog eventDialog = EventDialog.newInstance(SchedualReminderActivity.this, "0", 0);
                eventDialog.show(getFragmentManager(), "");
            }
        });

        addHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabDashboardMenu.close(true);
                HolidayDialog holidayDialog = new HolidayDialog();
                holidayDialog.show(getFragmentManager(), "dialog");
                showMonthView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMonthView();
    }

    public void getEvent() {
        myCalendar.deleteAllEvent();
        for (int i1 = 0; i1 < Appconstant.eventDataArrayList.size(); i1++) {
            EventData data = Appconstant.eventDataArrayList.get(i1);

            switch (data.getEvent_type()) {
                case MyDynamicCalendar.HOLYDAY_EVENT:
                    myCalendar.addHoliday(data.getEventId(),
                            MyDynamicCalendar.HOLYDAY_EVENT,
                            data.getStartDate(),
                            data.getName(),
                            R.drawable.ic_holyday);
                    break;

                case MyDynamicCalendar.BIRTHDAY_EVENT:
                    myCalendar.addBirthday(data.getEventId(),
                            MyDynamicCalendar.BIRTHDAY_EVENT,
                            data.getStartDate(),
                            data.getName(),
                            R.drawable.ic_cake);
                    break;

                case MyDynamicCalendar.ONE_DAY_EVENT:
                    myCalendar.addEvent(data.getEventId(),
                            MyDynamicCalendar.ONE_DAY_EVENT,
                            data.startDate,
                            data.starttime,
                            data.endTime,
                            data.getName()
                    );
                    break;

                case MyDynamicCalendar.MULTIPLE_DAY_EVENT:
                    List<Date> datesList = CalanderUtil.getDatesBetweenTwoDates(data.startDate, data.endDate);
                    for (int i = 0; i < datesList.size(); i++) {
                        Date date = datesList.get(i);
                        String date1 = CalanderUtil.getFormattedDate(date.getTime(), "dd-MM-yyyy");
                        Log.i("dates" + i, datesList.get(i).toString());
                        if (i == 0) {
                            myCalendar.addEvent(data.getEventId(),
                                    MyDynamicCalendar.MULTIPLEDAY_EVENT_LEFT,
                                    date1,
                                    data.starttime,
                                    "11:59",
                                    data.getName());
                        } else if (i == datesList.size() - 1) {
                            myCalendar.addEvent(data.getEventId(),
                                    MyDynamicCalendar.MULTIPLEDAY_EVENT_RIGHT,
                                    date1,
                                    "00:00",
                                    data.endTime,
                                    data.getName());
                        } else {
                            myCalendar.addEvent(data.getEventId(),
                                    MyDynamicCalendar.MULTIPLEDAY_EVENT_CENTER,
                                    date1,
                                    "00:00",
                                    "11:59",
                                    data.getName());
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedual_reminder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_birthday:
                HolidayDialog holidayDialog = new HolidayDialog();
                holidayDialog.show(getFragmentManager(), "dialog");
                showMonthView();
                break;
            case R.id.action_month:
                showMonthView();
                return true;
            case R.id.action_day:
                Calendar cal = Calendar.getInstance();
                showDayView(cal);
                return true;
            /*case R.id.action_today:
                myCalendar.goToCurrentDate();
                return true;*/
            case R.id.action_add_event:
                /*EventDialog eventDialog = new EventDialog();
                eventDialog.show(getFragmentManager(), "dialog");*/
                EventDialog eventDialog = EventDialog.newInstance(SchedualReminderActivity.this, "0", 0);
                eventDialog.show(getFragmentManager(), "");
                //showMonthView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMonthView() {
        getEvent();
        myCalendar.showMonthView();
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                showDayView(cal);
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });
    }

    private void showDayView(Calendar cal) {
        myCalendar.showDayView(cal);

        myCalendar.setOnWeekDayViewClickListener(new OnWeekDayViewClickListener() {
            @Override
            public void onClick(String eventID, String time) {
                //Toast.makeText(SchedualReminderActivity.this, "" + eventID, Toast.LENGTH_SHORT).show();
                int position = 0;
                for (int i1 = 0; i1 < Appconstant.eventDataArrayList.size(); i1++) {
                    if (eventID.equals("" + Appconstant.eventDataArrayList.get(i1).getEventId())) {
                        position = i1;
                        break;
                    }
                }

                EventDialog eventDialog = EventDialog.newInstance(SchedualReminderActivity.this, "1", position);
                eventDialog.show(getFragmentManager(), "");

            }

            @Override
            public void onLongClick(final String eventID, String time) {
                AlertDialog buildInfosDialog;
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SchedualReminderActivity.this);
                alertDialog2.setTitle("Are you sure want clear wishlist");

                alertDialog2.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                // new All().execute("clear_wishlist");
                                int position = -1;
                                for (int i1 = 0; i1 < Appconstant.eventDataArrayList.size(); i1++) {
                                    if (eventID.equals("" + Appconstant.eventDataArrayList.get(i1).getEventId())) {
                                        position = i1;
                                        break;
                                    }
                                }

                                if (position != -1) {
                                    Appconstant.eventDataArrayList.remove(position);
                                    showMonthView();
                                }

                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                            }
                        });
                buildInfosDialog = alertDialog2.create();
                buildInfosDialog.show();
            }
        });
        /*myCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onClick() {
                Log.e("showDayView", "from setOnEventClickListener onClick");
            }

            @Override
            public void onLongClick() {
                Log.e("showDayView", "from setOnEventClickListener onLongClick");
            }
        });*/
    }

    @Override
    public void createEvent() {
        showMonthView();
    }

    @Override
    public void createHoliday() {
        showMonthView();
    }
}
