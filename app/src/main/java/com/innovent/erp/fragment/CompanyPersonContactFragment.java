package com.innovent.erp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.CompanyPersonHistoryAdapter;
import com.innovent.erp.adapter.IndividualContactHistoryAdapter;
import com.innovent.erp.model.IndividualContactHistoryModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CRAFT BOX on 3/5/2018.
 */

public class CompanyPersonContactFragment extends Fragment {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    ArrayList<IndividualContactHistoryModel> data = new ArrayList<>();
    CompanyPersonHistoryAdapter adapter;
    MyPreferences myPreferences;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    String company_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_person_contact, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());
        try {
            Bundle b = getArguments();
            company_id = b.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GlobalElements.isConnectingToInternet(getActivity())) {
            getHistory();
        } else {
            GlobalElements.showDialog(getActivity());
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 13) {
            if (GlobalElements.isConnectingToInternet(getActivity())) {
                getHistory();
            } else {
                GlobalElements.showDialog(getActivity());
            }
        }
    }

    private void getHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getIndividualContactHistory(myPreferences.getPreferences(MyPreferences.id), company_id);

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
                                IndividualContactHistoryModel da = new IndividualContactHistoryModel();
                                da.setId("" + c.getString("id"));
                                da.setLabel_id(c.getString("label_id"));
                                da.setLabel_slug(c.getString("label_slug"));
                                da.setTag_id(c.getString("tag_id"));
                                da.setTag_slug(c.getString("tag_slug"));
                                da.setPerson_name("" + c.getString("person_name"));
                                da.setPerson_middle_name(c.getString("person_middle_name"));
                                da.setPerson_surname(c.getString("person_surname"));
                                da.setPerson_jobtitle(c.getString("person_jobtitle"));
                                da.setPerson_home_address("" + c.getString("person_home_address"));

                                da.setPerson_area(c.getString("person_area"));
                                da.setPerson_city("" + c.getString("person_city"));
                                da.setPerson_district(c.getString("person_district"));
                                da.setPerson_state("" + c.getString("person_state"));
                                da.setPerson_country("" + c.getString("person_country"));

                                da.setPerson_area(c.getString("person_area"));
                                da.setPerson_city_name("" + c.getString("person_city_name"));
                                da.setPerson_district_name(c.getString("person_district_name"));
                                da.setPerson_state_name("" + c.getString("person_state_name"));
                                da.setPerson_country_name("" + c.getString("person_country_name"));

                                da.setPerson_pincode(c.getString("person_pincode"));
                                da.setPerson_home_phone(c.getString("person_home_phone"));
                                da.setPerson_mobile("" + c.getString("person_mobile"));
                                da.setPerson_whatsapp(c.getString("person_whatsapp"));
                                da.setPerson_email("" + c.getString("person_email"));
                                da.setPerson_website(c.getString("person_website"));
                                da.setPerson_birthdate(c.getString("person_birthdate"));
                                da.setPerson_anniversary(c.getString("person_anniversary"));
                                da.setPerson_event(c.getString("person_event"));
                                da.setPerson_eventNote(c.getString("person_event_note"));
                                da.setPerson_note(c.getString("person_note"));
                                da.setCourier_address(c.getString("person_courier_address"));
                                da.setPrint_label(c.getString("print_label"));
                                da.setPerson_adhar_no(c.getString("person_adhar_no"));
                                da.setPerson_pan_no(c.getString("person_pan_no"));
                                da.setImage_path("" + c.getString("person_photo"));
                                da.setPerson_office_address(c.getString("person_office_address"));

                                da.setPerson_office_area(c.getString("person_office_area"));
                                da.setPerson_office_city(c.getString("person_office_city"));
                                da.setPerson_office_district(c.getString("person_office_district"));
                                da.setPerson_office_state(c.getString("person_office_state"));
                                da.setPerson_office_country(c.getString("person_office_country"));

                                da.setPerson_office_area(c.getString("person_office_area"));
                                da.setPerson_office_city_name(c.getString("person_office_city_name"));
                                da.setPerson_office_district_name(c.getString("person_office_district_name"));
                                da.setPerson_office_state_name(c.getString("person_office_state_name"));
                                da.setPerson_office_country_name(c.getString("person_office_country_name"));

                                da.setPerson_office_pincode(c.getString("person_office_pincode"));
                                da.setPerson_bank_name(c.getString("person_bank_name"));
                                da.setPerson_account_name(c.getString("person_account_name"));
                                da.setPerson_bank_acc_no(c.getString("person_bank_acc_no"));
                                da.setPerson_bank_ifsc(c.getString("person_bank_ifsc"));
                                da.setSame_as_office(c.getString("same_as_office"));
                                data.add(da);
                            }

                            adapter = new CompanyPersonHistoryAdapter(getActivity(), data, company_id);
                            recycleview.setAdapter(adapter);
                            recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            emptyLayout.setVisibility(View.GONE);
                            recycleview.setVisibility(View.VISIBLE);
                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recycleview.setVisibility(View.GONE);
                            emptyText.setText("" + json.getString("ack_msg"));
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
