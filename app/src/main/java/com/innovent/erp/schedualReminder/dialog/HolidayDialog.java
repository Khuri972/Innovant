package com.innovent.erp.schedualReminder.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.innovent.erp.R;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.schedualReminder.Appconstant;
import com.innovent.erp.schedualReminder.model.EventData;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;


/**
 */
public class HolidayDialog extends DialogFragment implements View.OnClickListener {
    private EditText etEventName;
    private EditText etEventDesc;
    private EditText etEventDate;
    private Button createEvent;
    private Button cancelEvent;
    CustomDatePicker datePickerDialog;
    private Spinner etEventType;

    public interface HolidayIntercommunicator {
        public void createHoliday();
    }

    public HolidayDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_holiday, container, false);
        etEventName = (EditText) view.findViewById(R.id.et_event_name);
        etEventDesc = (EditText) view.findViewById(R.id.et_event_desc);
        etEventDate = (EditText) view.findViewById(R.id.et_event_date);
        createEvent = (Button) view.findViewById(R.id.createEvent);
        cancelEvent = (Button) view.findViewById(R.id.cancelEvent);
        etEventType = (Spinner) view.findViewById(R.id.et_event_type);

        String events[] = {"Holiday", "Birthday"};

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, events);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // The drop down view
        etEventType.setAdapter(spinnerArrayAdapter);
        etEventDate.setOnClickListener(this);
        createEvent.setOnClickListener(this);
        cancelEvent.setOnClickListener(this);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_holiday, null);
        final Dialog d = new Dialog(getActivity());
        d.setContentView(root);
        d.getWindow().setTitleColor(getResources().getColor(R.color.colorPrimary));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        return d;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_event_date:
                datePickerDialog = new CustomDatePicker(v.getContext());
                datePickerDialog.setToDate(datePickerDialog.min, etEventDate, "");
                break;
            case R.id.createEvent:
                String title = etEventName.getText().toString();
                String desc = etEventDesc.getText().toString();
                String startDate = etEventDate.getText().toString();

                EventData date = new EventData();
                if (etEventType.getSelectedItemPosition() == 0) {
                    date.setEvent_type(MyDynamicCalendar.HOLYDAY_EVENT);
                } else {
                    date.setEvent_type(MyDynamicCalendar.BIRTHDAY_EVENT);
                }
                date.setDesc(desc);
                date.setName(title);
                date.setStartDate(startDate);
                Appconstant.eventDataArrayList.add(date);
                try {
                    HolidayIntercommunicator intercommunicator = (HolidayIntercommunicator) getActivity();
                    intercommunicator.createHoliday();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
                break;
            case R.id.cancelEvent:
                dismiss();
                break;
        }
    }
}
