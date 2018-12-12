package com.innovent.erp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innovent.erp.AddCompanyPersonActivity;
import com.innovent.erp.AddContactCompanyActivity;
import com.innovent.erp.CourierActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.CompanyContactHistoryAdapter;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.adapter.MultiCheckAdapter;
import com.innovent.erp.adapter.SearchAreaAdapter;
import com.innovent.erp.adapter.SearchCityAdapter;
import com.innovent.erp.adapter.SearchDistrictAdapter;
import com.innovent.erp.adapter.SearchZoneAdapter;
import com.innovent.erp.custom.MultiSelectionSpinner;
import com.innovent.erp.custom.MultiSelectionSpinnerEmployee;
import com.innovent.erp.custom.MultiSelectionSpinnerTag;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CompanyContactHistoryModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.DBHelper;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CRAFT BOX on 3/1/2018.
 */

public class ContactCompanyFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, MultiSelectionSpinnerEmployee.OnMultipleItemsSelectedListenerEmployee, MultiSelectionSpinnerTag.OnMultipleItemsSelectedListenerTag {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<CompanyContactHistoryModel> data = new ArrayList<>();
    CompanyContactHistoryAdapter adapter;

    MyPreferences myPreferences;

    GeneralAdapter cityAdapter;
    GeneralAdapter districtAdapter;
    GeneralAdapter zoneAdapter;
    ArrayList<GeneralModel> city_data = new ArrayList<>();
    ArrayList<GeneralModel> district_data = new ArrayList<>();
    ArrayList<GeneralModel> zone_data = new ArrayList<>();

    ArrayList<GeneralModel> employee_data = new ArrayList<>();
    ArrayList<GeneralModel> tag_data = new ArrayList<>();
    ArrayList<GeneralModel> label_data = new ArrayList<>();

    StringBuilder builder = new StringBuilder();
    ArrayList<String> labelData = new ArrayList<>();
    ArrayList<String> employeeData = new ArrayList<>();
    ArrayList<String> tagData = new ArrayList<>();

    String lableId = "0";
    String employeeId = "0";
    String tagId = "0", zoneId = "", districtId = "", cityId = "";

    int totalCount = 0;
    int filterCount = 0;
    DBHelper db;

    public interface CompanyIntercommunication {
        public void CompanyData(int total, int filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());
        db = new DBHelper(getActivity());
        if (GlobalElements.isConnectingToInternet(getActivity())) {
            getHistory();
        } else {
            GlobalElements.showDialog(getActivity());
        }
        return view;
    }

    public void getTotal() {
        CompanyIntercommunication intercommunication = (CompanyIntercommunication) getActivity();
        intercommunication.CompanyData(totalCount, filterCount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 11) {
            if (GlobalElements.isConnectingToInternet(getActivity())) {
                getHistory();
            } else {
                GlobalElements.showDialog(getActivity());
            }
        }
    }

    public void SearchData(SearchView searchView) {
        setUpSearchObservable(searchView);
    }

    public void FilterData() {

        lableId = "";
        employeeId = "";
        tagId = "";
        GlobalElements.city = "";
        GlobalElements.zone = "";
        GlobalElements.labelids = "";
        GlobalElements.tagids = "";
        cityId = "";
        zoneId = "";
        districtId = "";

        LayoutInflater inflater =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.dialog_filter_contact, null);
        final AlertDialog.Builder dg = new AlertDialog.Builder(getActivity());
        dg.setView(menuLayout);
        final AlertDialog dialog = dg.create();

        LinearLayout mainLayout = (LinearLayout) menuLayout.findViewById(R.id.main_layout);
        MultiSelectionSpinner contactLabel = (MultiSelectionSpinner) menuLayout.findViewById(R.id.contact_label);
        MultiSelectionSpinnerEmployee contactEmployee = (MultiSelectionSpinnerEmployee) menuLayout.findViewById(R.id.contact_emplayee);
        MultiSelectionSpinnerTag contactTag = (MultiSelectionSpinnerTag) menuLayout.findViewById(R.id.contact_tag);
        TextInputLayout input_layout_zone = (TextInputLayout) menuLayout.findViewById(R.id.input_layout_zone);
        final AutoCompleteTextView zone = (AutoCompleteTextView) menuLayout.findViewById(R.id.input_zone);
        final AutoCompleteTextView district = (AutoCompleteTextView) menuLayout.findViewById(R.id.input_district);
        final AutoCompleteTextView city = (AutoCompleteTextView) menuLayout.findViewById(R.id.input_city);
        final AutoCompleteTextView name = (AutoCompleteTextView) menuLayout.findViewById(R.id.input_name);
        final AutoCompleteTextView mobile = (AutoCompleteTextView) menuLayout.findViewById(R.id.input_mobile);
        final AutoCompleteTextView pincode = (AutoCompleteTextView) menuLayout.findViewById(R.id.input_pincode);
        GlobalElements.editTextAllCaps(getActivity(), mainLayout);

        MaterialSpinner zoneSpinner = (MaterialSpinner) menuLayout.findViewById(R.id.zone_spinner);
        MaterialSpinner districtSpinner = (MaterialSpinner) menuLayout.findViewById(R.id.district_spinner);
        MaterialSpinner citySpinner = (MaterialSpinner) menuLayout.findViewById(R.id.city_spinner);

        zone_data = db.getZone();
        zoneAdapter = new GeneralAdapter(getActivity(), zone_data);
        zoneSpinner.setAdapter(zoneAdapter);
        zoneSpinner.setHint("Select Zone");
        SpinnerInteractionListener_zone listener_1 = new SpinnerInteractionListener_zone();
        zoneSpinner.setOnTouchListener(listener_1);
        zoneSpinner.setOnItemSelectedListener(listener_1);

        city_data = db.getCity();
        cityAdapter = new GeneralAdapter(getActivity(), city_data);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setHint("Select City");
        SpinnerInteractionListener_city listener_2 = new SpinnerInteractionListener_city();
        citySpinner.setOnTouchListener(listener_2);
        citySpinner.setOnItemSelectedListener(listener_2);

        district_data = db.getDistrict();
        districtAdapter = new GeneralAdapter(getActivity(), district_data);
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setHint("Select District");
        SpinnerInteractionListener_district listener_3 = new SpinnerInteractionListener_district();
        districtSpinner.setOnTouchListener(listener_3);
        districtSpinner.setOnItemSelectedListener(listener_3);

        lable(contactLabel);
        Employee(contactEmployee);
        Tag(contactTag);
        ImageView btnCancel = (ImageView) menuLayout.findViewById(R.id.cancelbtn);
        Button btnClear = (Button) menuLayout.findViewById(R.id.clearbtn);
        Button submit = (Button) menuLayout.findViewById(R.id.submit);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityId = "";
                zoneId = "";
                districtId = "";
                getHistory();
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterHistory(cityId, zoneId, districtId, name.getText().toString(), mobile.getText().toString(), pincode.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public class SpinnerInteractionListener_zone implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    zoneId = zone_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    zoneId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_district implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    districtId = district_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    districtId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public class SpinnerInteractionListener_city implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    cityId = city_data.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    cityId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

    }

    public void lable(MultiSelectionSpinner multiSelectionSpinner) {
        try {
            label_data.clear();
            labelData.clear();
            GeneralModel da = new GeneralModel();
            da.setId("");
            da.setName("Select Label");
            label_data.add(da);
            labelData.add("Select Label");
            if (!myPreferences.getPreferences(MyPreferences.Label).equals("")) {
                JSONArray labelArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Label));
                for (int i = 0; i < labelArray.length(); i++) {
                    JSONObject c = labelArray.getJSONObject(i);
                    da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    label_data.add(da);
                    labelData.add(c.getString("name"));
                }
            }
            multiSelectionSpinner.setItems(labelData);
            multiSelectionSpinner.setListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Tag(MultiSelectionSpinnerTag multiSelectionSpinner) {
        try {
            tag_data.clear();
            tagData.clear();
            GeneralModel da = new GeneralModel();
            da.setId("");
            da.setName("Select Tag");
            tag_data.add(da);
            tagData.add("Select Tag");
            if (!myPreferences.getPreferences(MyPreferences.Tag).equals("")) {
                JSONArray tagArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Tag));
                for (int i = 0; i < tagArray.length(); i++) {
                    JSONObject c = tagArray.getJSONObject(i);
                    da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    tag_data.add(da);
                    tagData.add(c.getString("name"));
                }
            }

            multiSelectionSpinner.setItems(tagData);
            multiSelectionSpinner.setListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Employee(MultiSelectionSpinnerEmployee multiSelectionSpinner) {
        try {
            employee_data.clear();
            employeeData.clear();
            GeneralModel da = new GeneralModel();
            da.setId("");
            da.setName("Select Employee");
            employee_data.add(da);
            employeeData.add("Select Employee");
            if (!myPreferences.getPreferences(MyPreferences.Employee).equals("")) {
                JSONArray employeeArray = new JSONArray(myPreferences.getPreferences(MyPreferences.Employee));
                for (int i = 0; i < employeeArray.length(); i++) {
                    JSONObject c = employeeArray.getJSONObject(i);
                    da = new GeneralModel();
                    da.setId("" + c.getString("id"));
                    da.setName("" + c.getString("name"));
                    employee_data.add(da);
                    employeeData.add(c.getString("name"));
                }
            }
            multiSelectionSpinner.setItems(employeeData);
            multiSelectionSpinner.setListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpSearchObservable(SearchView search) {
        RxSearchObservable.fromView(search)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        if (text.isEmpty()) {
                            //textViewResult.setText("");
                            return true;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .switchMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String query) throws Exception {
                        return dataFromNetwork(query);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            System.out.print("" + responseBody.byteStream());
                            String json_response = null;
                            try {
                                json_response = responseBody.string();
                                JSONObject json = new JSONObject(json_response);
                                if (json.getInt("ack") == 1) {
                                    JSONArray result = json.getJSONArray("result");
                                    data.clear();
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject c = result.getJSONObject(i);
                                        CompanyContactHistoryModel da = new CompanyContactHistoryModel();
                                        da.setId("" + c.getString("id"));
                                        da.setLabel_id("" + c.getString("label_id"));
                                        da.setLabel_slug(c.getString("label_slug"));
                                        da.setTag_id(c.getString("tag_id"));
                                        da.setTag_slug(c.getString("tag_slug"));
                                        da.setCompany_name("" + c.getString("company_name"));
                                        da.setCompany_address("" + c.getString("company_address"));

                                        da.setCompany_area("" + c.getString("company_area"));
                                        da.setCompany_district("" + c.getString("company_district"));
                                        da.setCompany_city("" + c.getString("company_city"));
                                        da.setCompany_state("" + c.getString("company_state"));
                                        da.setCompany_country("" + c.getString("company_country"));
                                        da.setCompany_zone("" + c.getString("company_zone"));

                                        da.setCompany_district_name("" + c.getString("company_district_name"));
                                        da.setCompany_city_name("" + c.getString("company_city_name"));
                                        da.setCompany_state_name("" + c.getString("company_state_name"));
                                        da.setCompany_country_name("" + c.getString("company_country_name"));
                                        da.setCompany_zone_name("" + c.getString("company_zone_name"));

                                        da.setCompany_pincode("" + c.getString("company_pincode"));
                                        da.setCompany_mobile("" + c.getString("company_mobile"));
                                        da.setCompany_email("" + c.getString("company_email"));
                                        da.setCompany_website("" + c.getString("company_website"));
                                        da.setCompany_office_phone("" + c.getString("company_office_phone"));
                                        da.setCompany_wharehouse_address("" + c.getString("company_wharehouse_address"));

                                        da.setCompany_wharehouse_area("" + c.getString("company_wharehouse_area"));
                                        da.setCompany_wharehouse_district("" + c.getString("company_wharehouse_district"));
                                        da.setCompany_wharehouse_city("" + c.getString("company_wharehouse_city"));
                                        da.setCompany_wharehouse_state("" + c.getString("company_wharehouse_state"));
                                        da.setCompany_wharehouse_country("" + c.getString("company_wharehouse_country"));
                                        da.setCompany_wharehouse_zone("" + c.getString("company_wharehouse_zone"));

                                        da.setCompany_wharehouse_district_name("" + c.getString("company_wharehouse_district_name"));
                                        da.setCompany_wharehouse_city_name("" + c.getString("company_wharehouse_city_name"));
                                        da.setCompany_wharehouse_state_name("" + c.getString("company_wharehouse_state_name"));
                                        da.setCompany_wharehouse_country_name("" + c.getString("company_wharehouse_country_name"));
                                        da.setCompany_wharehouse_zone_name("" + c.getString("company_wharehouse_zone_name"));

                                        da.setCompany_wharehouse_pincode("" + c.getString("company_wharehouse_pincode"));
                                        da.setCompany_wharehouse_phone("" + c.getString("company_wharehouse_phone"));
                                        da.setCompany_wharehouse_mobile("" + c.getString("company_wharehouse_mobile"));
                                        da.setCompany_wharehouse_email("" + c.getString("company_wharehouse_email"));

                                        da.setCompany_gst_no(c.getString("company_gst_no"));
                                        da.setCompany_pan_no(c.getString("company_pan_no"));
                                        da.setCourier_address(c.getString("courier_address_company"));
                                        da.setPrint_label(c.getString("print_label"));
                                        da.setImage_path(c.getString("company_logo"));
                                        da.setCourier_address_wherehouse(c.getString("courier_address_wherehouse"));
                                        da.setSame_as_company(c.getString("same_as_company"));
                                        da.setCompany_bank_name(c.getString("company_bank_name"));
                                        da.setCompany_account_name(c.getString("company_account_name"));
                                        da.setCompany_bank_acc_no(c.getString("company_bank_acc_no"));
                                        da.setCompany_bank_ifsc(c.getString("company_bank_ifsc"));
                                        data.add(da);
                                    }
                                    adapter = new CompanyContactHistoryAdapter(getActivity(), data);
                                    recycleview.setAdapter(adapter);
                                    recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                                    emptyLayout.setVisibility(View.GONE);
                                    recycleview.setVisibility(View.VISIBLE);
                                    filterCount = data.size();
                                    CompanyIntercommunication intercommunication = (CompanyIntercommunication) getActivity();
                                    intercommunication.CompanyData(totalCount, filterCount);
                                } else {
                                    emptyLayout.setVisibility(View.VISIBLE);
                                    recycleview.setVisibility(View.GONE);
                                    emptyText.setText("" + json.getString("ack_msg"));
                                    filterCount = 0;
                                    CompanyIntercommunication intercommunication = (CompanyIntercommunication) getActivity();
                                    intercommunication.CompanyData(totalCount, filterCount);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", "" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("compate", "");
                    }
                });
    }

    /**
     * Simulation of network data
     */
    private Observable<ResponseBody> dataFromNetwork(final String query) {
        RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
        return request.searchCompanyContactHistory(myPreferences.getPreferences(MyPreferences.id), query);
    }

    public void getHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getCompanyContactHistory(myPreferences.getPreferences(MyPreferences.id));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            data.clear();
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                CompanyContactHistoryModel da = new CompanyContactHistoryModel();
                                da.setId("" + c.getString("id"));
                                da.setLabel_id("" + c.getString("label_id"));
                                da.setLabel_slug(c.getString("label_slug"));
                                da.setTag_id(c.getString("tag_id"));
                                da.setTag_slug(c.getString("tag_slug"));
                                da.setCompany_name("" + c.getString("company_name"));
                                da.setCompany_address("" + c.getString("company_address"));

                                da.setCompany_area("" + c.getString("company_area"));
                                da.setCompany_district("" + c.getString("company_district"));
                                da.setCompany_city("" + c.getString("company_city"));
                                da.setCompany_state("" + c.getString("company_state"));
                                da.setCompany_country("" + c.getString("company_country"));
                                da.setCompany_zone("" + c.getString("company_zone"));

                                da.setCompany_district_name("" + c.getString("company_district_name"));
                                da.setCompany_city_name("" + c.getString("company_city_name"));
                                da.setCompany_state_name("" + c.getString("company_state_name"));
                                da.setCompany_country_name("" + c.getString("company_country_name"));
                                da.setCompany_zone_name("" + c.getString("company_zone_name"));

                                da.setCompany_pincode("" + c.getString("company_pincode"));
                                da.setCompany_mobile("" + c.getString("company_mobile"));
                                da.setCompany_email("" + c.getString("company_email"));
                                da.setCompany_website("" + c.getString("company_website"));
                                da.setCompany_office_phone("" + c.getString("company_office_phone"));
                                da.setCompany_wharehouse_address("" + c.getString("company_wharehouse_address"));

                                da.setCompany_wharehouse_area("" + c.getString("company_wharehouse_area"));
                                da.setCompany_wharehouse_district("" + c.getString("company_wharehouse_district"));
                                da.setCompany_wharehouse_city("" + c.getString("company_wharehouse_city"));
                                da.setCompany_wharehouse_state("" + c.getString("company_wharehouse_state"));
                                da.setCompany_wharehouse_country("" + c.getString("company_wharehouse_country"));
                                da.setCompany_wharehouse_zone("" + c.getString("company_wharehouse_zone"));

                                da.setCompany_wharehouse_district_name("" + c.getString("company_wharehouse_district_name"));
                                da.setCompany_wharehouse_city_name("" + c.getString("company_wharehouse_city_name"));
                                da.setCompany_wharehouse_state_name("" + c.getString("company_wharehouse_state_name"));
                                da.setCompany_wharehouse_country_name("" + c.getString("company_wharehouse_country_name"));
                                da.setCompany_wharehouse_zone_name("" + c.getString("company_wharehouse_zone_name"));

                                da.setCompany_wharehouse_pincode("" + c.getString("company_wharehouse_pincode"));
                                da.setCompany_wharehouse_phone("" + c.getString("company_wharehouse_phone"));
                                da.setCompany_wharehouse_mobile("" + c.getString("company_wharehouse_mobile"));
                                da.setCompany_wharehouse_email("" + c.getString("company_wharehouse_email"));

                                da.setCompany_gst_no(c.getString("company_gst_no"));
                                da.setCompany_pan_no(c.getString("company_pan_no"));
                                da.setCourier_address(c.getString("courier_address_company"));
                                da.setPrint_label(c.getString("print_label"));
                                da.setImage_path(c.getString("company_logo"));
                                da.setCourier_address_wherehouse(c.getString("courier_address_wherehouse"));
                                da.setSame_as_company(c.getString("same_as_company"));
                                da.setCompany_bank_name(c.getString("company_bank_name"));
                                da.setCompany_account_name(c.getString("company_account_name"));
                                da.setCompany_bank_acc_no(c.getString("company_bank_acc_no"));
                                da.setCompany_bank_ifsc(c.getString("company_bank_ifsc"));
                                data.add(da);
                            }
                            adapter = new CompanyContactHistoryAdapter(getActivity(), data);
                            recycleview.setAdapter(adapter);
                            recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                            emptyLayout.setVisibility(View.GONE);
                            recycleview.setVisibility(View.VISIBLE);
                            totalCount = data.size();
                            CompanyIntercommunication intercommunication = (CompanyIntercommunication) getActivity();
                            intercommunication.CompanyData(totalCount, 0);
                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recycleview.setVisibility(View.GONE);
                            emptyText.setText("" + json.getString("ack_msg"));
                            CompanyIntercommunication intercommunication = (CompanyIntercommunication) getActivity();
                            intercommunication.CompanyData(0, 0);
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

    private void filterHistory(String city, String zone, String district, String name, String mobile, String pincode) {
        try {
            GlobalElements.city = city;
            GlobalElements.zone = zone;
            GlobalElements.labelids = lableId;
            GlobalElements.tagids = tagId;

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.filterCompanyContactHistory(myPreferences.getPreferences(MyPreferences.id), employeeId, tagId, lableId, city, zone, district, name, mobile, pincode);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            data.clear();
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                CompanyContactHistoryModel da = new CompanyContactHistoryModel();
                                da.setId("" + c.getString("id"));
                                da.setLabel_id("" + c.getString("label_id"));
                                da.setLabel_slug(c.getString("label_slug"));
                                da.setTag_id(c.getString("tag_id"));
                                da.setTag_slug(c.getString("tag_slug"));
                                da.setCompany_name("" + c.getString("company_name"));
                                da.setCompany_address("" + c.getString("company_address"));

                                da.setCompany_area("" + c.getString("company_area"));
                                da.setCompany_district("" + c.getString("company_district"));
                                da.setCompany_city("" + c.getString("company_city"));
                                da.setCompany_state("" + c.getString("company_state"));
                                da.setCompany_country("" + c.getString("company_country"));
                                da.setCompany_zone("" + c.getString("company_zone"));

                                da.setCompany_district_name("" + c.getString("company_district_name"));
                                da.setCompany_city_name("" + c.getString("company_city_name"));
                                da.setCompany_state_name("" + c.getString("company_state_name"));
                                da.setCompany_country_name("" + c.getString("company_country_name"));
                                da.setCompany_zone_name("" + c.getString("company_zone_name"));

                                da.setCompany_pincode("" + c.getString("company_pincode"));
                                da.setCompany_mobile("" + c.getString("company_mobile"));
                                da.setCompany_email("" + c.getString("company_email"));
                                da.setCompany_website("" + c.getString("company_website"));
                                da.setCompany_office_phone("" + c.getString("company_office_phone"));
                                da.setCompany_wharehouse_address("" + c.getString("company_wharehouse_address"));

                                da.setCompany_wharehouse_area("" + c.getString("company_wharehouse_area"));
                                da.setCompany_wharehouse_district("" + c.getString("company_wharehouse_district"));
                                da.setCompany_wharehouse_city("" + c.getString("company_wharehouse_city"));
                                da.setCompany_wharehouse_state("" + c.getString("company_wharehouse_state"));
                                da.setCompany_wharehouse_country("" + c.getString("company_wharehouse_country"));
                                da.setCompany_wharehouse_zone("" + c.getString("company_wharehouse_zone"));

                                da.setCompany_wharehouse_district_name("" + c.getString("company_wharehouse_district_name"));
                                da.setCompany_wharehouse_city_name("" + c.getString("company_wharehouse_city_name"));
                                da.setCompany_wharehouse_state_name("" + c.getString("company_wharehouse_state_name"));
                                da.setCompany_wharehouse_country_name("" + c.getString("company_wharehouse_country_name"));
                                da.setCompany_wharehouse_zone_name("" + c.getString("company_wharehouse_zone_name"));

                                da.setCompany_wharehouse_pincode("" + c.getString("company_wharehouse_pincode"));
                                da.setCompany_wharehouse_phone("" + c.getString("company_wharehouse_phone"));
                                da.setCompany_wharehouse_mobile("" + c.getString("company_wharehouse_mobile"));
                                da.setCompany_wharehouse_email("" + c.getString("company_wharehouse_email"));

                                da.setCompany_gst_no(c.getString("company_gst_no"));
                                da.setCompany_pan_no(c.getString("company_pan_no"));
                                da.setCourier_address(c.getString("courier_address_company"));
                                da.setPrint_label(c.getString("print_label"));
                                da.setImage_path(c.getString("company_logo"));
                                da.setCourier_address_wherehouse(c.getString("courier_address_wherehouse"));
                                da.setSame_as_company(c.getString("same_as_company"));
                                da.setCompany_bank_name(c.getString("company_bank_name"));
                                da.setCompany_account_name(c.getString("company_account_name"));
                                da.setCompany_bank_acc_no(c.getString("company_bank_acc_no"));
                                da.setCompany_bank_ifsc(c.getString("company_bank_ifsc"));
                                data.add(da);
                            }
                            adapter = new CompanyContactHistoryAdapter(getActivity(), data);
                            recycleview.setAdapter(adapter);
                            recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

                            emptyLayout.setVisibility(View.GONE);
                            recycleview.setVisibility(View.VISIBLE);
                            filterCount = data.size();
                            CompanyIntercommunication intercommunication = (CompanyIntercommunication) getActivity();
                            intercommunication.CompanyData(totalCount, filterCount);
                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recycleview.setVisibility(View.GONE);
                            emptyText.setText("" + json.getString("ack_msg"));
                            filterCount = 0;
                            CompanyIntercommunication intercommunication = (CompanyIntercommunication) getActivity();
                            intercommunication.CompanyData(totalCount, filterCount);
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

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        lableId = "";
        builder.setLength(0);
        for (int i = 0; i < label_data.size(); i++) {
            String name = label_data.get(i).getName();
            for (int j = 0; j < strings.size(); j++) {
                if (name.equals("" + strings.get(j).toString())) {
                    if (!name.equals("Select Label")) {
                        builder.append(label_data.get(i).getId()).append(",");
                    }
                }
            }
        }
        lableId = "" + GlobalElements.getRemoveLastComma(builder.toString());
    }

    @Override
    public void selectedIndicesEmployee(List<Integer> indices) {
    }

    @Override
    public void selectedStringsEmployee(List<String> strings) {
        employeeId = "";
        builder.setLength(0);
        for (int i = 0; i < employee_data.size(); i++) {
            String name = employee_data.get(i).getName();
            for (int j = 0; j < strings.size(); j++) {
                if (name.equals("" + strings.get(j).toString())) {
                    if (!name.equals("Select Employee")) {
                        builder.append(employee_data.get(i).getId()).append(",");
                    }
                }
            }
        }
        employeeId = "" + GlobalElements.getRemoveLastComma(builder.toString());
    }

    @Override
    public void selectedIndicesTag(List<Integer> indices) {

    }

    @Override
    public void selectedStringsTag(List<String> strings) {
        tagId = "";
        builder.setLength(0);
        for (int i = 0; i < tag_data.size(); i++) {
            String name = tag_data.get(i).getName();
            for (int j = 0; j < strings.size(); j++) {
                if (name.equals("" + strings.get(j).toString())) {
                    if (!name.equals("Select Tag")) {
                        builder.append(tag_data.get(i).getId()).append(",");
                    }
                }
            }
        }
        tagId = "" + GlobalElements.getRemoveLastComma(builder.toString());
    }

}
