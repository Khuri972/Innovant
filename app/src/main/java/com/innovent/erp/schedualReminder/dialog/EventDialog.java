package com.innovent.erp.schedualReminder.dialog;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.innovent.erp.R;
import com.innovent.erp.custom.CustomDateTimePickerDialog;
import com.innovent.erp.schedualReminder.Appconstant;
import com.innovent.erp.schedualReminder.CalanderUtil;
import com.innovent.erp.schedualReminder.model.EventData;
import com.innovent.erp.schedualReminder.schedualservice.MyReceiver;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Khushvinders on 21-Oct-16.
 */

public class EventDialog extends DialogFragment implements View.OnClickListener {


    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    private Button createEvent;
    private Button cancelEvent;
    private EditText eventTitle;
    private EditText eventDes;
    private EditText eventLocation, eventStartTime, eventEndTime;
    Spinner sp_reminder;

    CustomDateTimePickerDialog dateTimePickerDialog;

    String type = "";
    int position = 0;

    public interface EventIntercommunicator {
        public void createEvent();
    }

    public static EventDialog newInstance(Context context, String type, int position) {
        EventDialog f = new EventDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putInt("position", position);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type = getArguments().getString("type");
        this.position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event_layout, container, false);
        eventTitle = (EditText) view.findViewById(R.id.eventTitle);
        eventDes = (EditText) view.findViewById(R.id.eventDes);
        eventLocation = (EditText) view.findViewById(R.id.eventLocation);
        sp_reminder = (Spinner) view.findViewById(R.id.sp_reminder);
//
        createEvent = (Button) view.findViewById(R.id.createEvent);
        cancelEvent = (Button) view.findViewById(R.id.cancelEvent);
        eventStartTime = (EditText) view.findViewById(R.id.eventStartTime);
        eventEndTime = (EditText) view.findViewById(R.id.eventEndTime);
        createEvent.setOnClickListener(this);
        cancelEvent.setOnClickListener(this);
        eventStartTime.setOnClickListener(this);
        eventEndTime.setOnClickListener(this);

        String[] times = {"On time", "before 30 minutes", "before 1 hour", "before 1 day"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, times);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        sp_reminder.setAdapter(spinnerArrayAdapter);

        if (type.equals("1")) {
            eventTitle.setText("" + Appconstant.eventDataArrayList.get(position).getName());
            eventDes.setText("" + Appconstant.eventDataArrayList.get(position).getDesc());
            eventLocation.setText("" + Appconstant.eventDataArrayList.get(position).getLocation());
            eventStartTime.setText("" + Appconstant.eventDataArrayList.get(position).getStartDate() + " " + Appconstant.eventDataArrayList.get(position).getStarttime());
            eventEndTime.setText("" + Appconstant.eventDataArrayList.get(position).getEndDate() + " " + Appconstant.eventDataArrayList.get(position).getEndTime());
        }

        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.create_event_layout, null);
        final Dialog d = new Dialog(getActivity());
        d.setContentView(root);
        d.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return d;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelEvent:
                dismiss();
                break;
            case R.id.eventStartTime:
                dateTimePickerDialog = new CustomDateTimePickerDialog(view.getContext());
                dateTimePickerDialog.showDateTimePicker(eventStartTime, "");
                break;

            case R.id.eventEndTime:
                dateTimePickerDialog = new CustomDateTimePickerDialog(view.getContext());
                dateTimePickerDialog.showDateTimePicker(eventEndTime, "");
                break;

            case R.id.createEvent:
                try {
                    SimpleDateFormat dateFormatter;
                    dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);

                    String title = eventTitle.getText().toString();
                    String desc = eventDes.getText().toString();
                    String location = eventLocation.getText().toString();
                    String startDate = eventStartTime.getText().toString();
                    String endDate = eventEndTime.getText().toString();

                    try {
                        Date d = dateFormatter.parse(startDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        setReminder(cal.getTimeInMillis(), view.getContext(), title, desc);
                        Log.i("calender", cal.getTimeInMillis() + "");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String[] a = startDate.split(" ");
                    String startdate = a[0];
                    String startTime = a[1] + " " + a[2];

                    String[] b = endDate.split(" ");
                    String enddate = b[0];
                    String endTime = b[1] + " " + b[2];

                    EventData date = new EventData();
                    List<Date> datesList = CalanderUtil.getDatesBetweenTwoDates(startdate, enddate);
                    if (datesList.size() == 1) {
                        date.setEvent_type(MyDynamicCalendar.ONE_DAY_EVENT);
                    } else {
                        date.setEvent_type(MyDynamicCalendar.MULTIPLE_DAY_EVENT);
                    }

                    final int random = new Random().nextInt(61) + 20;

                    date.setEventId("" + random);
                    date.setDesc(desc);
                    date.setName(title);
                    date.setLocation(location);
                    date.setStartDate(startdate);
                    date.setStarttime(startTime);
                    date.setEndDate(enddate);
                    date.setEndTime(endTime);

                    if (type.equals("1")) {
                        Appconstant.eventDataArrayList.set(position, date);
                    } else {
                        Appconstant.eventDataArrayList.add(date);
                    }

                    try {
                        EventIntercommunicator intercommunicator = (EventIntercommunicator) getActivity();
                        intercommunicator.createEvent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
                break;
        }
    }

    private void setReminder(long time, Context context, String title, String des) {
        long alertTime;
        switch (sp_reminder.getSelectedItemPosition()) {
            case 0:
                alertTime = time;
                break;
            case 1:
                alertTime = time - (1000 * 60 * 30);
                break;
            case 2:
                alertTime = time - (1000 * 60 * 60);
                break;
            case 3:
                alertTime = time - (1000 * 60 * 60 * 24);
                break;
            default:
                alertTime = time;
        }
        Intent notifyIntent = new Intent(context, MyReceiver.class);
        notifyIntent.putExtra("title", title);
        notifyIntent.putExtra("desc", des);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alertTime,1000 * 60 * 60 * 24, pendingIntent);
    }

}
