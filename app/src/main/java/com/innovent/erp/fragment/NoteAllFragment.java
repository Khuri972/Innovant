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
import com.innovent.erp.NoteHistory;
import com.innovent.erp.R;
import com.innovent.erp.adapter.NoteAdapter;
import com.innovent.erp.custom.RecyclerViewPositionHelper;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.NoteModel;
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

public class NoteAllFragment extends Fragment {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<NoteModel> noteModels = new ArrayList<>();
    NoteAdapter noteAdapter;

    MyPreferences myPreferences;
    int firstVisibleItem, visibleItemCount, totalItemCount, count = 0;
    protected int m_PreviousTotalCount;
    RecyclerViewPositionHelper mRecyclerViewHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_all, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());

        noteAdapter = new NoteAdapter(getActivity(), noteModels);
        recycleview.setAdapter(noteAdapter);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                if (totalItemCount == 0 || noteAdapter == null)
                    return;
                if (m_PreviousTotalCount == totalItemCount) {
                    return;
                } else {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                    if (loadMore) {
                        m_PreviousTotalCount = totalItemCount;
                        // GetNotifaction();
                        if (GlobalElements.isConnectingToInternet(getActivity())) {
                            //getNotesHistory();
                        }
                    }
                }
            }
        });

        if (GlobalElements.isConnectingToInternet(getActivity())) {
            getNotesHistory();
        } else {
            GlobalElements.showDialog(getActivity());
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==11)
        {
            try {
                NoteModel da= (NoteModel) data.getSerializableExtra("data");
                int position = data.getIntExtra("position",0);
                noteModels.set(position,da);
                noteAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getNotesHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getNoteHistory(myPreferences.getPreferences(MyPreferences.id), "0", count, GlobalElements.ll_count);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            noteModels.clear();
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                NoteModel da = new NoteModel();
                                da.setId(c.getString("id"));
                                da.setTitle(c.getString("title"));
                                da.setDesc(c.getString("description"));
                                da.setNotes_date(c.getString("notes_date"));
                                noteModels.add(da);
                            }

                            if (count == 0) {
                                noteAdapter.notifyDataSetChanged();
                                if (noteModels.isEmpty()) {
                                    recycleview.setVisibility(View.GONE);
                                    emptyLayout.setVisibility(View.VISIBLE);
                                } else {
                                    recycleview.setVisibility(View.VISIBLE);
                                    emptyLayout.setVisibility(View.GONE);
                                }
                            }
                            if (result.length() == 0) {
                                count = 0;
                            } else {
                                count += result.length();
                            }
                        } else {
                            if (count == 0) {
                                emptyLayout.setVisibility(View.VISIBLE);
                                emptyText.setText("" + json.getString("ack_msg"));
                                recycleview.setVisibility(View.GONE);
                            }
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
