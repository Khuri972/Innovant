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

import com.innovent.erp.AddReceptionActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.ReceptionHistoryAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.ReceptionModel;
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
 * Created by CRAFT BOX on 4/11/2018.
 */

public class VisitorFragment extends Fragment {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<ReceptionModel> receptionModels = new ArrayList<>();
    ReceptionHistoryAdapter adapter;
    MyPreferences myPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_all, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());

        adapter = new ReceptionHistoryAdapter(getActivity(), receptionModels);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(getActivity())) {
            getReceptionHistory();
        } else {
            GlobalElements.showDialog(getActivity());
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 11) {
            try {
                ReceptionModel da = (ReceptionModel) data.getSerializableExtra("data");
                receptionModels.add(da);
                adapter.notifyDataSetChanged();
                if (receptionModels.isEmpty()) {
                    recycleview.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                } else {
                    recycleview.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getReceptionHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getReceptionHistory(myPreferences.getPreferences(MyPreferences.id), "1");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result_array = json.getJSONArray("result");
                            for (int i = 0; i < result_array.length(); i++) {
                                JSONObject result = result_array.getJSONObject(i);
                                ReceptionModel da = new ReceptionModel();
                                da.setId("" + result.getString("id"));
                                da.setTag("");
                                da.setTag_slug("");
                                da.setLabel("" + result.getString("label_id"));
                                da.setLabel_slug("" + result.getString("label_slug"));
                                da.setTitle("" + result.getString("title"));
                                da.setPerson_name(result.getString("person_name"));
                                da.setCompany_name("" + result.getString("company_name"));
                                da.setMobile_no("" + result.getString("mobile_no"));
                                da.setEmail("" + result.getString("email"));
                                da.setWhome_to_meet("" + result.getString("whom_to_meet"));
                                da.setDescription("" + result.getString("reception_detail"));
                                da.setCity(result.getString("city"));
                                da.setType("" + result.getString("type"));
                                da.setDate("");
                                da.setName("" + result.getString("visitor_name"));
                                receptionModels.add(da);
                            }
                            adapter.notifyDataSetChanged();
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
