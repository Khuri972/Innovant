package com.innovent.erp.employeeManagement;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.innovent.erp.R;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.CustomDateTimePickerDialog;
import com.innovent.erp.custom.PathUtil;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.employeeManagement.model.LeaveRequestModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLeaveRequest extends AppCompatActivity {

    @BindView(R.id.frome_date_edt)
    EditText fromeDateEdt;
    @BindView(R.id.layout_from_date)
    TextInputLayout layoutFromDate;
    @BindView(R.id.reason_edt)
    EditText reasonEdt;
    @BindView(R.id.layout_reason)
    TextInputLayout layoutReason;
    @BindView(R.id.file_name_txt)
    TextView fileNameTxt;
    @BindView(R.id.browse_txt)
    TextView browseTxt;
    @BindView(R.id.from_time_date_edt)
    EditText fromTimeDateEdt;
    @BindView(R.id.layout_from_time_date)
    TextInputLayout layoutFromTimeDate;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.activity_add_cheque)
    NestedScrollView activityAddCheque;
    @BindView(R.id.to_date_edt)
    EditText toDateEdt;
    @BindView(R.id.to_date_layout)
    TextInputLayout toDateLayout;
    @BindView(R.id.to_time_edt)
    EditText toTimeEdt;
    @BindView(R.id.to_time_layout)
    TextInputLayout toTimeLayout;

    CustomDatePicker customerDatePicker;
    File file = null;
    String mimeType;
    MyPreferences myPreferences;
    LeaveRequestModel data=new LeaveRequestModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leave_request);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leave Request");
        myPreferences = new MyPreferences(this);
        reasonEdt.addTextChangedListener(new MyTextWatcher(reasonEdt));

        fromeDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutFromDate.setErrorEnabled(false);
                customerDatePicker = new CustomDatePicker(AddLeaveRequest.this);
                customerDatePicker.setToDate(customerDatePicker.min, fromeDateEdt, "");
            }
        });

        fromTimeDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (fromeDateEdt.getText().toString().equals("")) {
                        Toaster.show(AddLeaveRequest.this, "Select from date", false, Toaster.DANGER);
                    } else {
                        final Calendar c = Calendar.getInstance();
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddLeaveRequest.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        Calendar datetime = Calendar.getInstance();
                                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        datetime.set(Calendar.MINUTE, minute);
                                        try {
                                            String[] temp = fromeDateEdt.getText().toString().split(" ");
                                            String temp_date = temp[0];
                                            String[] _date = temp_date.split("\\-");
                                            String day = _date[0];
                                            String month = _date[1];
                                            String year = _date[2];

                                            datetime.set(Calendar.YEAR, Integer.parseInt(year));
                                            datetime.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                                            datetime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            datetime = Calendar.getInstance();
                                        }

                                        Calendar c = Calendar.getInstance();
                                        if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                            //it's after current
                                            int hour = hourOfDay % 12;
                                            fromTimeDateEdt.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                                    minute, hourOfDay < 12 ? "AM" : "PM"));
                                        } else {
                                            //it's before current'
                                            Toaster.show(AddLeaveRequest.this, "You can not select past time", false, Toaster.DANGER);
                                        }
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        toDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDateLayout.setErrorEnabled(false);
                customerDatePicker = new CustomDatePicker(AddLeaveRequest.this);
                customerDatePicker.setToDate(customerDatePicker.min, toDateEdt, "");
            }
        });

        toTimeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (toDateEdt.getText().toString().equals("")) {
                        Toaster.show(AddLeaveRequest.this, "Select To Date", false, Toaster.DANGER);
                    } else {
                        final Calendar c = Calendar.getInstance();
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddLeaveRequest.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        Calendar datetime = Calendar.getInstance();
                                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        datetime.set(Calendar.MINUTE, minute);
                                        try {
                                            String[] temp = toDateEdt.getText().toString().split(" ");
                                            String temp_date = temp[0];
                                            String[] _date = temp_date.split("\\-");
                                            String day = _date[0];
                                            String month = _date[1];
                                            String year = _date[2];

                                            datetime.set(Calendar.YEAR, Integer.parseInt(year));
                                            datetime.set(Calendar.MONTH, Integer.parseInt(month) - 1);
                                            datetime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            datetime = Calendar.getInstance();
                                        }


                                        Calendar c = Calendar.getInstance();
                                        if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                            //it's after current
                                            int hour = hourOfDay % 12;
                                            toTimeEdt.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                                    minute, hourOfDay < 12 ? "AM" : "PM"));
                                        } else {
                                            //it's before current'
                                            Toaster.show(AddLeaveRequest.this, "You can not select past time", false, Toaster.DANGER);
                                        }
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        browseTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    startActivityForResult(intent, 7);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (fromeDateEdt.getText().toString().equals("")) {
                    layoutFromDate.setError("Select From Date");
                } else if (toDateEdt.getText().toString().equals("")) {
                    toDateLayout.setError("Select To Date");
                } else {
                    try {
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date _to_date = inputFormat.parse("" + toDateEdt.getText().toString());
                        Date _from_date = inputFormat.parse("" + fromeDateEdt.getText().toString());
                        Date to_date = outputFormat.parse("" + outputFormat.format(_to_date));  //to date
                        Date frdate = outputFormat.parse("" + outputFormat.format(_from_date)); // from date

                        if (frdate.after(to_date)) {
                            Toaster.show(AddLeaveRequest.this, "" + getResources().getString(R.string.from_date), false, Toaster.DANGER);
                        } else if (to_date.before(frdate)) {
                            Toaster.show(AddLeaveRequest.this, "" + getResources().getString(R.string.to_date), false, Toaster.DANGER);
                        } /*else if (frdate.equals(to_date)) {
                            Toaster.show(AddLeaveRequest.this, "" + getResources().getString(R.string.from_date_to_date_equals), false, Toaster.DANGER);
                        }*/ else {
                            //String diff= GlobalElements.printDifference(fromeDateEdt.getText().toString()+" "+fromTimeDateEdt.getText().toString(),toDateEdt.getText().toString()+" "+toTimeEdt.getText().toString());
                            //System.out.print(""+diff);
                            addLeaveRequest();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_courier, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri ur = data.getData();
            try {
                mimeType = getMimeType(ur);
                String PathHolder = PathUtil.getPath(AddLeaveRequest.this, ur);
                file = new File(PathHolder);
                if (file != null) {
                    //uploadFile();s
                    fileNameTxt.setText("" + file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = AddLeaveRequest.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.reason_edt:
                    layoutReason.setErrorEnabled(false);
                    break;
            }
        }
    }

    private void addLeaveRequest() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddLeaveRequest.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            RequestBody requestfile_image;
            MultipartBody.Part body_image;
            Call<ResponseBody> call;
            if (file == null) {
                call = request.addLeaveRequest(myPreferences.getPreferences(MyPreferences.id), fromeDateEdt.getText().toString(), fromTimeDateEdt.getText().toString(), toDateEdt.getText().toString(), toTimeEdt.getText().toString(), reasonEdt.getText().toString());
            } else {
                requestfile_image = RequestBody.create(MediaType.parse("image/*"), file);
                body_image = MultipartBody.Part.createFormData("file_path", file.getName(), requestfile_image);
                call = request.addLeaveRequest(myPreferences.getPreferences(MyPreferences.id), fromeDateEdt.getText().toString(), fromTimeDateEdt.getText().toString(), toDateEdt.getText().toString(), toTimeEdt.getText().toString(), reasonEdt.getText().toString(), body_image);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            try {
                                data.setId("" + result.getString("id"));
                                data.setFrom_date("" + result.getString("start_date"));
                                data.setFrom_time("" + result.getString("start_time"));
                                data.setTo_date(result.getString("end_date"));
                                data.setTo_time(result.getString("end_time"));
                                data.setReason(result.getString("reason"));
                                if (result.getString("file_path").equals("")) {
                                    data.setAttachment_name("");
                                    data.setAttachment_path("");
                                } else {
                                    File file = new File(result.getString("file_path"));
                                    data.setAttachment_name(file.getName());
                                    data.setAttachment_path(result.getString("file_path"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", data);
                            intent.putExtras(bundle);
                            setResult(10, intent);
                            finish();
                        } else {
                            Toaster.show(AddLeaveRequest.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
