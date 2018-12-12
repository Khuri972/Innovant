package com.innovent.erp.visitorBookModual;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextFollowupActivity extends AppCompatActivity {

    EditText detail;
    Button submit;
    TextView follow_up_date, msg, call, email;
    String visitor_id, follow_up_id, _detail, msg_flag = "";

    Calendar date;
    private SimpleDateFormat dateFormatter, dateFormatter1;
    String select_date, select_time;
    MyPreferences myPreferences;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_followup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(NextFollowupActivity.this);

        follow_up_date = (TextView) findViewById(R.id.add_follow_up_date);
        detail = (EditText) findViewById(R.id.add_follow_up_detail);
        submit = (Button) findViewById(R.id.add_follow_up_submit);
        msg = (TextView) findViewById(R.id.add_follow_up_msg);
        call = (TextView) findViewById(R.id.add_follow_up_call);
        email = (TextView) findViewById(R.id.add_follow_up_email);

        try {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            visitor_id = "" + bundle.get("visitor_id");
            follow_up_id = "" + bundle.get("follow_up_id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        msg_flag = "1";
        msg.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
        msg.setTextColor(getResources().getColor(R.color.text_color));
        call.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
        call.setTextColor(getResources().getColor(R.color.white));
        email.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
        email.setTextColor(getResources().getColor(R.color.text_color));

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_flag = "1";
                msg.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
                msg.setTextColor(getResources().getColor(R.color.text_color));
                call.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                call.setTextColor(getResources().getColor(R.color.white));
                email.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
                email.setTextColor(getResources().getColor(R.color.text_color));

            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_flag = "2";
                msg.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                msg.setTextColor(getResources().getColor(R.color.white));
                call.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
                call.setTextColor(getResources().getColor(R.color.text_color));
                email.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
                email.setTextColor(getResources().getColor(R.color.text_color));
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_flag = "3";
                msg.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
                msg.setTextColor(getResources().getColor(R.color.text_color));
                call.setBackgroundDrawable(getResources().getDrawable(R.color.dashboard_header));
                call.setTextColor(getResources().getColor(R.color.text_color));
                email.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                email.setTextColor(getResources().getColor(R.color.white));
            }
        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        follow_up_date.setText("" + dateFormatter.format(new java.util.Date()));
        follow_up_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail.getText().toString().equals("")) {
                    detail.setError("Please Enter Detail");
                    return;
                } else if (msg_flag.equals("")) {
                    Toaster.show(NextFollowupActivity.this, "", false, Toaster.DANGER);
                    return;
                } else if (follow_up_date.getText().toString().equals("Select Date")) {
                    Toaster.show(NextFollowupActivity.this, "Please Select followup followup_date", false, Toaster.DANGER);
                    return;
                } else {
                    _detail = detail.getText().toString();
                    if (GlobalElements.isConnectingToInternet(NextFollowupActivity.this)) {
                        addFollowup(detail.getText().toString(), msg_flag, follow_up_date.getText().toString());
                    } else {
                        GlobalElements.showDialog(NextFollowupActivity.this);
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(10, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(10, intent);
        intent.putExtra("position", position);
        finish();
    }

    public void showDateTimePicker() {

        String[] temp = follow_up_date.getText().toString().split(" ");
        String temp_date = temp[0];
        String[] _date = temp_date.split("\\-");
        String day = _date[0];
        String month = _date[1];
        String year = _date[2];

        final Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.YEAR, Integer.parseInt(year));
        currentDate.set(Calendar.MONTH, Integer.parseInt(month));
        currentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

        date = Calendar.getInstance();
        date.set(Calendar.YEAR, Integer.parseInt(year));
        date.set(Calendar.MONTH, Integer.parseInt(month));
        date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

        DatePickerDialog d = new DatePickerDialog(NextFollowupActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                TimePickerDialog time = new TimePickerDialog(NextFollowupActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        try {

                            select_date = "" + dateFormatter1.format(date.getTime());
                            Date date1 = dateFormatter1.parse("" + dateFormatter1.format(date.getTime()));
                            Date date2 = dateFormatter1.parse("" + dateFormatter1.format(new java.util.Date()));

                            if (date1.equals(date2)) {
                                /// equelse followup_date
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                Date inTime = null;
                                String sd = "" + hourOfDay + ":" + minute;

                                String current_time = sdf.format(currentDate.getTime());

                                select_time = sd;
                                inTime = sdf.parse(current_time);
                                Date outTime = sdf.parse("" + sd);
                                if (inTime.compareTo(outTime) < 0) {
                                    follow_up_date.setText(dateFormatter.format(date.getTime()));
                                    String[] split = dateFormatter.format(date.getTime()).split(" ");
                                    select_date = split[0];
                                    select_time = split[1] + " " + split[2];
                                }
                            } else {
                                follow_up_date.setText(dateFormatter.format(date.getTime()));
                                String[] split = dateFormatter.format(date.getTime()).split(" ");
                                select_date = split[0];
                                select_time = split[1] + " " + split[2];
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                time.show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH) - 1, currentDate.get(Calendar.DATE));
        d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        d.show();
    }

    public void addFollowup(String description, String through, String follow_up_date) {
        final ProgressDialog pd = new ProgressDialog(NextFollowupActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.addFollowup(myPreferences.getPreferences(MyPreferences.id), visitor_id, description, through, follow_up_date);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pd.dismiss();
                try {
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        Intent intent = new Intent();
                        setResult(10, intent);
                        intent.putExtra("position", position);
                        finish();
                    } else {
                        Toaster.show(NextFollowupActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                System.out.print("error" + t.getMessage());
            }
        });
    }

}
