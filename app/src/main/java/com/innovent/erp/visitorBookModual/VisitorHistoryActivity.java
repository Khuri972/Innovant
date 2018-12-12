package com.innovent.erp.visitorBookModual;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.visitorBookModual.adapter.ViewAdapter;
import com.innovent.erp.visitorBookModual.dialog.RatingDialog;
import com.innovent.erp.visitorBookModual.model.CategoryModel;
import com.innovent.erp.visitorBookModual.model.ViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorHistoryActivity extends AppCompatActivity implements ViewAdapter.Intercommunication, RatingDialog.InterfaceCommunicator {

    RecyclerView view_history_rv;
    LinearLayout empty_layout;
    ArrayList<ViewModel> viewModels = new ArrayList<>();
    ViewAdapter viewAdapter;
    EditText fromdate, todate, search_edt;
    String Fromsetteddate = "";
    String tosettedDate = "";
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    String searchName;
    String tdate, fdate, category_id;
    Spinner category_spn;
    ArrayList<GeneralModel> categoryModels = new ArrayList<>();
    GeneralAdapter categorySpinnerAdapter;
    TextView view_total_visitor_tv, view_fromdate_tv, view_todate_tv, view_category_tv, view_search_name_tv;
    LinearLayout from_date_layout, to_date_layout, category_layout, search_layout;
    NestedScrollView layout_main;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_history);
        myPreferences = new MyPreferences(VisitorHistoryActivity.this);
        getSupportActionBar().setSubtitle(myPreferences.getPreferences(MyPreferences.project_title));
        view_history_rv = (RecyclerView) findViewById(R.id.view_history_rv);
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);

        view_total_visitor_tv = (TextView) findViewById(R.id.view_total_visitor_tv);
        view_fromdate_tv = (TextView) findViewById(R.id.view_fromdate_tv);
        view_todate_tv = (TextView) findViewById(R.id.view_todate_tv);
        view_category_tv = (TextView) findViewById(R.id.view_category_tv);
        view_search_name_tv = (TextView) findViewById(R.id.view_search_name_tv);

        search_layout = (LinearLayout) findViewById(R.id.search_layout);
        from_date_layout = (LinearLayout) findViewById(R.id.from_date_layout);
        to_date_layout = (LinearLayout) findViewById(R.id.to_date_layout);
        category_layout = (LinearLayout) findViewById(R.id.category_layout);
        layout_main = (NestedScrollView) findViewById(R.id.layout_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        this.setTitle("Visitor History ");
        if (GlobalElements.isConnectingToInternet(VisitorHistoryActivity.this)) {
            Viewhistory();
        } else {
            GlobalElements.showDialog(VisitorHistoryActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_filter:
                Filter();
                return true;
            case R.id.action_search:
                search();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Viewhistory() {
        final ProgressDialog pd = new ProgressDialog(VisitorHistoryActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.view_history(myPreferences.getPreferences(MyPreferences.id), myPreferences.getPreferences(MyPreferences.project_id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonst = response.body().string();
                    JSONObject json = new JSONObject(jsonst);
                    if (json.getInt("ack") == 1) {
                        layout_main.setVisibility(View.VISIBLE);
                        empty_layout.setVisibility(View.GONE);
                        view_history_rv.removeAllViews();
                        viewModels.clear();
                        JSONArray result = json.getJSONArray("result");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);
                            ViewModel da = new ViewModel();
                            da.setId("" + c.get("id"));
                            da.setName("" + c.getString("name"));
                            da.setEmail("" + c.getString("email"));
                            da.setMobile_no("" + c.getString("mobile_no"));
                            da.setDetail("" + c.getString("detail"));
                            da.setCreated_date("" + c.getString("created_date"));
                            da.setCategory_name("" + c.getString("category_name"));
                            da.setReference("" + c.getString("reference"));
                            da.setRating(c.getInt("rating"));
                            da.setProject_manager_name(c.getString("project_manager_name"));
                            da.setInquiryBy(c.getString("user_name"));
                            viewModels.add(da);
                        }
                        String total = json.getString("Total_Visitor");
                        view_total_visitor_tv.setText(total);
                        from_date_layout.setVisibility(View.GONE);
                        to_date_layout.setVisibility(View.GONE);
                        category_layout.setVisibility(View.GONE);
                        search_layout.setVisibility(View.GONE);

                        String to_date = json.getString("to_date");
                        view_todate_tv.setText(to_date);
                        String from_date = json.getString("from_date");
                        view_fromdate_tv.setText(from_date);
                        String search = json.getString("search_name");
                        view_search_name_tv.setText(search);
                        String category = json.getString("category_name");
                        view_category_tv.setText(category);
                        viewAdapter = new ViewAdapter(VisitorHistoryActivity.this, viewModels);
                        view_history_rv.setAdapter(viewAdapter);
                        view_history_rv.setLayoutManager(new LinearLayoutManager(VisitorHistoryActivity.this));
                        viewAdapter.notifyDataSetChanged();
                    } else {
                        layout_main.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                System.out.print("error" + t.getMessage());
            }
        });
    }

    private void Filter() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.list_filter, null);
        final AlertDialog.Builder dg = new AlertDialog.Builder(VisitorHistoryActivity.this);
        fromdate = (EditText) menuLayout.findViewById(R.id.fromdate);
        todate = (EditText) menuLayout.findViewById(R.id.todate);
        category_spn = (Spinner) menuLayout.findViewById(R.id.category_spn);
        dg.setView(menuLayout);
        final AlertDialog dialog = dg.create();

        fromdate.setFocusable(false);
        todate.setFocusable(false);

        fromdate.setText("" + sdf.format(new Date()));
        todate.setText("" + sdf.format(new Date()));
        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        if (GlobalElements.isConnectingToInternet(VisitorHistoryActivity.this)) {
            product_List("product_list");
        } else {
            GlobalElements.showDialog(VisitorHistoryActivity.this);
        }

        if (!Fromsetteddate.equals("")) {
            String[] date = Fromsetteddate.toString().split("\\-");
            String year = date[2];
            String month = date[1];
            String day = date[0];
            c.set(Calendar.YEAR, Integer.parseInt(year));
            c.set(Calendar.MONTH, Integer.parseInt(month));
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            fromdate.setText(Fromsetteddate);
        }
        if (!tosettedDate.equals("")) {
            String[] date = tosettedDate.toString().split("\\-");
            String year = date[2];
            String month = date[1];
            String day = date[0];
            c2.set(Calendar.YEAR, Integer.parseInt(year));
            c2.set(Calendar.MONTH, Integer.parseInt(month));
            c2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            todate.setText(tosettedDate);
        }

        Button btnCancel = (Button) menuLayout.findViewById(R.id.cancelbtn);
        Button btnClear = (Button) menuLayout.findViewById(R.id.clearbtn);
        Button btnSet = (Button) menuLayout.findViewById(R.id.setbtn);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                fromdate.setText("");
                todate.setText("");
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromdate.setText("");
                todate.setText("");
            }
        });
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date _to_date = inputFormat.parse("" + todate.getText().toString());
                    Date _from_date = inputFormat.parse("" + fromdate.getText().toString());
                    Fromsetteddate = fromdate.getText().toString();
                    tosettedDate = todate.getText().toString();
                    Date to_date = sdf.parse("" + outputFormat.format(_to_date));  //to followup_date
                    Date frdate = sdf.parse("" + outputFormat.format(_from_date)); // from followup_date

                    if (frdate.after(to_date)) {
                        Toaster.show(VisitorHistoryActivity.this, "Fromdate Should Be Less then todate", false, Toaster.DANGER);
                    } else if (to_date.before(frdate)) {
                        Toaster.show(VisitorHistoryActivity.this, "Todate  Should Be More then Fromdate", false, Toaster.DANGER);
                    } else {
                        if (GlobalElements.isConnectingToInternet(VisitorHistoryActivity.this)) {
                            fdate = fromdate.getText().toString();
                            tdate = todate.getText().toString();
                            category_id = categoryModels.get(category_spn.getSelectedItemPosition()).getId();
                            searchName = "";
                            getSearch_history("search_filter");
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            GlobalElements.showDialog(VisitorHistoryActivity.this);
                        }
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }*/
                if (GlobalElements.isConnectingToInternet(VisitorHistoryActivity.this)) {
                    dialog.dismiss();
                    Viewhistory();
                } else {
                    GlobalElements.showDialog(VisitorHistoryActivity.this);
                }
            }
        });

        try {
            fromDatePickerDialog = new DatePickerDialog(VisitorHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        fromdate.setText(dateFormatter.format(newDate.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            toDatePickerDialog = new DatePickerDialog(VisitorHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    try {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        todate.setText(dateFormatter.format(newDate.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }

    private void search() {
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.list_search_debit, null);
        final AlertDialog.Builder dg = new AlertDialog.Builder(VisitorHistoryActivity.this);
        search_edt = (EditText) menuLayout.findViewById(R.id.search_edt);

        dg.setView(menuLayout);
        final AlertDialog dialog = dg.create();

        Button btnCancel = (Button) menuLayout.findViewById(R.id.cancel_btn);
        Button btn_search = (Button) menuLayout.findViewById(R.id.search_btn);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(VisitorHistoryActivity.this)) {
                    searchName = search_edt.getText().toString();
                    tdate = "";
                    fdate = "";
                    category_id = "";
                    getSearch_history_name("search_filter");
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    GlobalElements.showDialog(VisitorHistoryActivity.this);
                }
            }
        });
        dialog.show();
    }

    private void product_List(final String type) {
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = null;
        if (type.equals("product_list")) {
            call = req.getCategory();
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (type.equals("product_list")) {
                    try {
                        String jsonst = response.body().string();
                        JSONObject json = new JSONObject(jsonst);
                        if (json.getInt("ack") == 1) {
                            categoryModels.clear();
                            JSONArray result = json.getJSONArray("result");
                            GeneralModel da = new GeneralModel();
                            da.setId("");
                            da.setName("Please Select Category");
                            categoryModels.add(da);
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                categoryModels.add(da);
                            }
                            categorySpinnerAdapter = new GeneralAdapter(VisitorHistoryActivity.this, categoryModels);
                            category_spn.setAdapter(categorySpinnerAdapter);
                            categorySpinnerAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.print("error" + t.getMessage());
            }
        });
    }

    private void getSearch_history(final String type) {
        final ProgressDialog pd = new ProgressDialog(VisitorHistoryActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = null;

        if (type.equals("search_filter")) {
            call = req.search_view_history(myPreferences.getPreferences(MyPreferences.id), myPreferences.getPreferences(MyPreferences.project_id), searchName, tdate, fdate, category_id);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (type.equals("search_filter")) {
                    pd.dismiss();
                    try {
                        String jsonst = response.body().string();
                        JSONObject json = new JSONObject(jsonst);

                        if (json.getInt("ack") == 1) {
                            layout_main.setVisibility(View.VISIBLE);
                            empty_layout.setVisibility(View.GONE);
                            view_history_rv.removeAllViews();
                            viewModels.clear();
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                ViewModel da = new ViewModel();
                                da.setId("" + c.get("id"));
                                da.setName("" + c.getString("name"));
                                da.setEmail("" + c.getString("email"));
                                da.setMobile_no("" + c.getString("mobile_no"));
                                da.setDetail("" + c.getString("detail"));
                                da.setCreated_date("" + c.getString("created_date"));
                                da.setCategory_name("" + c.getString("category_name"));
                                da.setReference("" + c.getString("reference"));
                                da.setRating(c.getInt("rating"));
                                da.setProject_manager_name(c.getString("project_manager_name"));
                                da.setInquiryBy(c.getString("user_name"));
                                viewModels.add(da);
                            }
                            String total = json.getString("Total_Visitor");
                            from_date_layout.setVisibility(View.VISIBLE);
                            to_date_layout.setVisibility(View.VISIBLE);
                            category_layout.setVisibility(View.VISIBLE);
                            search_layout.setVisibility(View.GONE);

                            view_total_visitor_tv.setText(total);
                            String to_date = json.getString("to_date");
                            view_todate_tv.setText(to_date);
                            String from_date = json.getString("from_date");
                            view_fromdate_tv.setText(from_date);
                            String category = json.getString("category_name");
                            view_category_tv.setText(category);

                            viewAdapter = new ViewAdapter(VisitorHistoryActivity.this, viewModels);
                            view_history_rv.setAdapter(viewAdapter);
                            view_history_rv.setLayoutManager(new LinearLayoutManager(VisitorHistoryActivity.this));
                            viewAdapter.notifyDataSetChanged();
                        } else {
                            layout_main.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                System.out.print("error" + t.getMessage());
            }
        });
    }

    private void getSearch_history_name(final String type) {
        final ProgressDialog pd = new ProgressDialog(VisitorHistoryActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = null;

        if (type.equals("search_filter")) {
            call = req.search_view_history(myPreferences.getPreferences(MyPreferences.id), myPreferences.getPreferences(MyPreferences.project_id), searchName, tdate, fdate, category_id);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (type.equals("search_filter")) {
                    pd.dismiss();
                    try {
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);

                        if (json.getInt("ack") == 1) {
                            layout_main.setVisibility(View.VISIBLE);
                            empty_layout.setVisibility(View.GONE);
                            view_history_rv.removeAllViews();
                            viewModels.clear();
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                ViewModel da = new ViewModel();
                                da.setId("" + c.get("id"));
                                da.setName("" + c.getString("name"));
                                da.setEmail("" + c.getString("email"));
                                da.setMobile_no("" + c.getString("mobile_no"));
                                da.setDetail("" + c.getString("detail"));
                                da.setCreated_date("" + c.getString("created_date"));
                                da.setCategory_name("" + c.getString("category_name"));
                                da.setReference("" + c.getString("reference"));
                                da.setRating(c.getInt("rating"));
                                da.setProject_manager_name(c.getString("project_manager_name"));
                                da.setInquiryBy(c.getString("user_name"));
                                viewModels.add(da);
                            }
                            String total = json.getString("Total_Visitor");
                            from_date_layout.setVisibility(View.GONE);
                            to_date_layout.setVisibility(View.GONE);
                            category_layout.setVisibility(View.GONE);
                            search_layout.setVisibility(View.VISIBLE);

                            view_total_visitor_tv.setText(total);

                            String search = json.getString("search_name");
                            view_search_name_tv.setText(search);

                            viewAdapter = new ViewAdapter(VisitorHistoryActivity.this, viewModels);
                            view_history_rv.setAdapter(viewAdapter);
                            view_history_rv.setLayoutManager(new LinearLayoutManager(VisitorHistoryActivity.this));
                            viewAdapter.notifyDataSetChanged();
                        } else {
                            layout_main.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                System.out.print("error" + t.getMessage());
            }
        });
    }

    @Override
    public void addRating(int position, String visitor_id) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        RatingDialog rd = RatingDialog.newInstance(VisitorHistoryActivity.this, "" + visitor_id, "" + position);
        rd.show(fm, "");
    }

    @Override
    public void sendRequestCode(int position, int rating) {
        try {
            viewModels.get(position).setRating(rating);
            viewAdapter.notifyItemChanged(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
