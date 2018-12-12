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
import com.innovent.erp.adapter.ChequeHistoryAdapter;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.model.ChequeModel;
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

public class ReceiveChequeFragment extends Fragment {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    MyPreferences myPreferences;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<ChequeModel> data = new ArrayList<>();
    ChequeHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_all, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());

        if (GlobalElements.isConnectingToInternet(getActivity())) {
            getChequeHistory();
        } else {
            GlobalElements.showDialog(getActivity());
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        if (resultCode == 14) {
            getChequeHistory();
        }
        else if (resultCode == 15) {
            int position = _data.getIntExtra("position", 0);
            ChequeModel da = (ChequeModel) _data.getSerializableExtra("data");
            data.set(position, da);
            adapter.notifyDataSetChanged();
        }
    }

    public void SearchData(SearchView searchView) {
        setUpSearchObservable(searchView);
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

    private Observable<ResponseBody> dataFromNetwork(final String query) {
        RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
        return request.searchChequeHistory(myPreferences.getPreferences(MyPreferences.id), query,"1");
    }

    public void filterChequeHistory(String to_date, String from_date) {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.filterChequeHistory(myPreferences.getPreferences(MyPreferences.id), to_date, from_date,"1");

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

    private void getChequeHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getChequeHistory(myPreferences.getPreferences(MyPreferences.id),"1");

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

    public void getResult(String json_response) {
        try {
            JSONObject json = new JSONObject(json_response);
            data.clear();
            if (json.getInt("ack") == 1) {
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);
                    ChequeModel da = new ChequeModel();
                    da.setId("" + c.getString("id"));
                    da.setBank_name(c.getString("bank_name"));
                    da.setCompany_name(c.getString("company_name"));
                    da.setCheque_no("" + c.getString("cheque_no"));
                    da.setParty_name("" + c.getString("party_name"));
                    da.setCheque_type("" + c.getString("cheque_type"));
                    da.setAmount("" + c.getString("amount"));
                    da.setCheque_date("" + c.getString("cheque_date"));
                    data.add(da);
                }
                adapter = new ChequeHistoryAdapter(getActivity(), data,"receive");
                recycleview.setAdapter(adapter);
                recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recycleview.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);

            } else {
                recycleview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyText.setText("" + json.getString("ack_msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
