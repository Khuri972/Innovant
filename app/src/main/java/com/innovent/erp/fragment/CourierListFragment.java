package com.innovent.erp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.CourierHistoryAdapter;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.model.CourierModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CRAFT BOX on 4/11/2018.
 */

public class CourierListFragment extends Fragment {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    ArrayList<CourierModel> data = new ArrayList<>();
    CourierHistoryAdapter adapter;

    MyPreferences myPreferences;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_all, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());

        if (GlobalElements.isConnectingToInternet(getActivity())) {
            getCourierHistory();
        } else {
            GlobalElements.showDialog(getActivity());
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        if (resultCode == 10) {
            getCourierHistory();
        }
    }

    public void changeType() {
        getCourierHistory();
    }

    private void getCourierHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getCourierHistory(myPreferences.getPreferences(MyPreferences.id), "1");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        getResult(json_response);
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

    public void filterCourierHistory(String to_date, String from_date, String mode) {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getCourierHistory(myPreferences.getPreferences(MyPreferences.id), to_date, from_date, mode, "0");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        getResult(json_response);
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

    public void setUpSearchObservable(SearchView search) {
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
                                getResult(json_response);
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

    public Observable<ResponseBody> dataFromNetwork(final String query) {
        RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
        return request.searchCourierHistory(myPreferences.getPreferences(MyPreferences.id), query, "1");
    }

    public void getResult(String json_response) {
        try {
            JSONObject json = new JSONObject(json_response);
            data.clear();
            if (json.getInt("ack") == 1) {
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);
                    CourierModel da = new CourierModel();
                    da.setId(c.getString("id"));
                    da.setCourier_no(c.getString("courier_no"));
                    da.setLabel_id("");
                    da.setShipping_date(c.getString("shipping_date"));
                    da.setSender_contact_no(c.getString("sender_contact_no"));
                    da.setSender_address(c.getString("sender_address"));

                    da.setSender_area(c.getString("sender_area"));
                    da.setSender_city(c.getString("sender_city"));
                    da.setSender_district(c.getString("sender_district"));
                    da.setSender_state(c.getString("sender_state"));
                    da.setSender_country(c.getString("sender_country"));
                    da.setSender_zone(c.getString("sender_zone"));
                    da.setSender_city_name(c.getString("sender_city_name"));
                    da.setSender_district_name(c.getString("sender_district_name"));
                    da.setSender_state_name(c.getString("sender_state_name"));
                    da.setSender_country_name(c.getString("sender_country_name"));
                    da.setSender_zone_name(c.getString("sender_zone_name"));
                    da.setSender_pincode(c.getString("sender_pincode"));

                    da.setReceiver_company_name(c.getString("receiver_company_name"));
                    da.setReceiver_person_name(c.getString("receiver_person_name"));
                    da.setReceiver_contact_no(c.getString("receiver_contact_no"));
                    da.setReceiver_address(c.getString("receiver_address"));

                    da.setReceiver_area(c.getString("receiver_area"));
                    da.setReceiver_city(c.getString("receiver_city"));
                    da.setReceiver_district(c.getString("receiver_district"));
                    da.setReceiver_state(c.getString("receiver_state"));
                    da.setReceiver_country(c.getString("receiver_country"));
                    da.setReceiver_zone(c.getString("receiver_zone"));
                    da.setReceiver_city_name(c.getString("receiver_city_name"));
                    da.setReceiver_district_name(c.getString("receiver_district_name"));
                    da.setReceiver_state_name(c.getString("receiver_state_name"));
                    da.setReceiver_country_name(c.getString("receiver_country_name"));
                    da.setReceiver_zone_name(c.getString("receiver_zone_name"));
                    da.setReceiver_pincode(c.getString("receiver_pincode"));

                    da.setParcel_weight(c.getString("parcel_weight"));
                    da.setParcel_cost(c.getString("parcel_cost"));
                    da.setSender_company_name(c.getString("sender_company"));
                    da.setSender_person_name(c.getString("sender_person"));
                    da.setLabel_slug("");
                    da.setParcel_type_slug(c.getString("parcel_type_slug"));
                    da.setShipping_deliverd_date(c.getString("shipping_deliverd_date"));
                    da.setParcel_description(c.getString("parcel_description"));
                    da.setShipping_courier_name(c.getString("shipping_courier_name"));
                    da.setShipping_pickup_person(c.getString("shipping_pickup_person"));
                    da.setTracking_no(c.getString("tracking_no"));
                    da.setDelivery_status(c.getString("delivery_status_slug"));
                    da.setAssigned_person_name(c.getString("assigned_person_name"));
                    da.setAssigned_person_mobile(c.getString("assigned_person_mobile"));
                    da.setCourier_type(c.getString("courier_type"));
                    da.setKg_unit(c.getString("kg_unit"));
                    da.setKg_value(c.getString("kg_value"));
                    da.setGm_unit(c.getString("gm_unit"));
                    da.setGm_value(c.getString("gm_value"));
                    da.setCourier_type(c.getString("courier_type"));
                    if (GlobalElements.courierType.equals("sender") && c.getString("courier_type").equals("1")) {
                        data.add(da);
                    } else if (GlobalElements.courierType.equals("receiver") && c.getString("courier_type").equals("2")) {
                        data.add(da);
                    }
                }
                adapter = new CourierHistoryAdapter(getActivity(), data);
                recycleview.setAdapter(adapter);
                recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recycleview.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            } else {
                adapter = new CourierHistoryAdapter(getActivity(), data);
                recycleview.setAdapter(adapter);
                recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recycleview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyText.setText("" + json.getString("ack_msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
