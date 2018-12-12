package com.innovent.erp.visitorBookModual;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.innovent.erp.visitorBookModual.model.FollowUpModel;

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

public class FollowUpResponse extends AppCompatActivity {

    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter, dateFormatter1;

    String visitor_id, follow_up_id, future_date = "";
    TextView followup_date, status, detail, submit;
    EditText response;
    AlertDialog buildInfosDialog;
    MyPreferences myPreferences;
    FollowUpModel followUpModel;
    Calendar date;
    int position = 0;
    String notification_type = "", response_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up_response);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(FollowUpResponse.this);

        followup_date = (TextView) findViewById(R.id.follow_up_response_date);
        status = (TextView) findViewById(R.id.follow_up_response_status);
        detail = (TextView) findViewById(R.id.follow_up_response_detail);
        response = (EditText) findViewById(R.id.follow_up_response);
        submit = (TextView) findViewById(R.id.follow_up_response_next);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);
        dateFormatter1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        try {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            notification_type = bundle.getString("type");
            if (notification_type == null) {
                followUpModel = (FollowUpModel) bundle.getSerializable("data");
                position = bundle.getInt("position");
                follow_up_id = followUpModel.getId();
                visitor_id = followUpModel.getVisitor_id();
                response_status = followUpModel.getStatus();

                if (response_status.equals("1")) {
                    response.setText("" + followUpModel.getResponse());
                }
                followup_date.setText("" + followUpModel.getFollowup_date());
                status.setText("" + followUpModel.getStatus_slug());
                detail.setText("" + followUpModel.getDescription());
            } else {
                follow_up_id = bundle.getString("follow_up_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GlobalElements.isConnectingToInternet(FollowUpResponse.this)) {
            getFollowupDetail();
        } else {
            GlobalElements.showDialog(FollowUpResponse.this);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response.getText().toString().equals("")) {
                    response.setError("Please Enter Response");
                } else {
                    if (response_status.equals("1")) {
                        if (GlobalElements.isConnectingToInternet(FollowUpResponse.this)) {
                            updateFollowupResponse(response.getText().toString());
                        } else {
                            GlobalElements.showDialog(FollowUpResponse.this);
                        }
                    } else {
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(FollowUpResponse.this);
                        LayoutInflater inflater = FollowUpResponse.this.getLayoutInflater();
                        final View vi = inflater.inflate(R.layout.dialog_followup, null);
                        alertDialog2.setView(vi);

                        TextView next = (TextView) vi.findViewById(R.id.dialog_followup_next);
                        TextView future = (TextView) vi.findViewById(R.id.dialog_followup_future);
                        TextView cancle = (TextView) vi.findViewById(R.id.dialog_followup_cancle);

                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (GlobalElements.isConnectingToInternet(FollowUpResponse.this)) {
                                        addFollowupResponse(response.getText().toString(), "1", future_date);
                                        buildInfosDialog.dismiss();
                                    } else {
                                        GlobalElements.showDialog(FollowUpResponse.this);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        cancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (GlobalElements.isConnectingToInternet(FollowUpResponse.this)) {
                                    addFollowupResponse(detail.getText().toString(), "-1", "");
                                } else {
                                    GlobalElements.showDialog(FollowUpResponse.this);
                                }
                            }
                        });

                        future.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                buildInfosDialog.dismiss();
                                if (GlobalElements.isConnectingToInternet(FollowUpResponse.this)) {
                                    Calendar newCalendar = Calendar.getInstance();

                                    final Calendar currentDate = Calendar.getInstance();
                                    date = Calendar.getInstance();

                                    toDatePickerDialog = new DatePickerDialog(FollowUpResponse.this, new DatePickerDialog.OnDateSetListener() {

                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            Calendar newDate = Calendar.getInstance();
                                            newDate.set(year, monthOfYear, dayOfMonth);
                                            date.set(year, monthOfYear, dayOfMonth);

                                            TimePickerDialog time = new TimePickerDialog(FollowUpResponse.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                    date.set(Calendar.MINUTE, minute);
                                                    try {

                                                        Date date1 = dateFormatter1.parse("" + dateFormatter1.format(date.getTime()));
                                                        Date date2 = dateFormatter1.parse("" + dateFormatter1.format(new java.util.Date()));

                                                        if (date1.equals(date2)) {
                                                            /// equelse followup_date
                                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                                            Date inTime = null;
                                                            String sd = "" + hourOfDay + ":" + minute;
                                                            String current_time = sdf.format(currentDate.getTime());
                                                            inTime = sdf.parse(current_time);
                                                            Date outTime = sdf.parse("" + sd);
                                                            if (inTime.compareTo(outTime) < 0) {
                                                                future_date = dateFormatter.format(date.getTime());
                                                                addFollowupResponse(response.getText().toString(), "2", future_date);
                                                            }
                                                        } else {
                                                            future_date = dateFormatter.format(date.getTime());
                                                            addFollowupResponse(response.getText().toString(), "2", future_date);
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
                                            time.show();
                                        }
                                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                                    toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                    toDatePickerDialog.show();
                                } else {
                                    GlobalElements.showDialog(FollowUpResponse.this);
                                }
                            }
                        });

                        buildInfosDialog = alertDialog2.create();
                        buildInfosDialog.show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    if (notification_type == null) {
                        Intent intent = new Intent();
                        intent.putExtra("position", position);
                        setResult(10, intent);
                        finish();
                    } else {
                        Intent i = new Intent(getApplicationContext(), VisitorBookActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if (notification_type == null) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(10, intent);
                finish();
            } else {
                Intent i = new Intent(getApplicationContext(), VisitorBookActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            try {
                if (notification_type == null) {
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    setResult(10, intent);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), VisitorBookActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addFollowupResponse(String description, final String through, String follow_up_date) {
        final ProgressDialog pd = new ProgressDialog(FollowUpResponse.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.addFollowupResponse(myPreferences.getPreferences(MyPreferences.id), follow_up_id, description, through, follow_up_date);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pd.dismiss();
                try {
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        if (through.equals("1")) {
                            Intent i = new Intent(FollowUpResponse.this, NextFollowupActivity.class);
                            i.putExtra("follow_up_id", "" + follow_up_id);
                            i.putExtra("visitor_id", "" + visitor_id);
                            i.putExtra("position", position);
                            startActivityForResult(i, 0);
                        } else {
                            if (notification_type == null) {
                                Intent intent = new Intent();
                                intent.putExtra("position", position);
                                setResult(10, intent);
                                finish();
                            } else {
                                Intent i = new Intent(getApplicationContext(), VisitorBookActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    } else {
                        Toaster.show(FollowUpResponse.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    public void updateFollowupResponse(String description) {
        final ProgressDialog pd = new ProgressDialog(FollowUpResponse.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.updateFollowupResponse(myPreferences.getPreferences(MyPreferences.id), follow_up_id, description);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pd.dismiss();
                try {
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        if (notification_type == null) {
                            Intent intent = new Intent();
                            intent.putExtra("position", position);
                            setResult(10, intent);
                            finish();
                        } else {
                            Intent i = new Intent(getApplicationContext(), VisitorBookActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Toaster.show(FollowUpResponse.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    public void getFollowupDetail() {
        final ProgressDialog pd = new ProgressDialog(FollowUpResponse.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.getFollowUpDetail(follow_up_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response_) {

                pd.dismiss();
                try {
                    String json_response = response_.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {

                        JSONObject result = json.getJSONObject("result");
                        visitor_id = result.getString("visitor_id");
                        response_status = result.getString("status");
                        followup_date.setText("" + result.getString("followup_date"));
                        status.setText("" + result.getString("status_slug"));
                        detail.setText("" + result.getString("description"));

                        if (response_status.equals("1")) {
                            response.setText("" + result.getString("response"));
                        }
                    } else {
                        Toaster.show(FollowUpResponse.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
