package com.innovent.erp.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.NoteHistory;
import com.innovent.erp.R;
import com.innovent.erp.adapter.NoteCollectionAdapter;
import com.innovent.erp.custom.DividerItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CategoryModel;
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

public class NoteCollectionsFragment extends Fragment {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    NoteCollectionAdapter adapter;

    MyPreferences myPreferences;
    @BindView(R.id.add_Note)
    FloatingActionButton addNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());
                    alertDialog2.setTitle("Add Category");

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.dialog_new_folder, null);
                    alertDialog2.setView(dialogView);

                    final EditText newFolderEdt = (EditText) dialogView.findViewById(R.id.new_folder_edt);

                    alertDialog2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    try {
                                        if (GlobalElements.isConnectingToInternet(getActivity())) {
                                            if (newFolderEdt.getText().toString().equals("")) {
                                                Toaster.show(getActivity(), "Enter Category Name", false, Toaster.DANGER);
                                            } else {
                                                addCategory("add", newFolderEdt.getText().toString());
                                            }

                                        } else {
                                            GlobalElements.showDialog(getActivity());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    alertDialog2.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog

                                }
                            });

                    buildInfosDialog = alertDialog2.create();
                    buildInfosDialog.show();
                    buildInfosDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    buildInfosDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        adapter = new NoteCollectionAdapter(getActivity(), categoryModels);
        recycleview.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        recycleview.addItemDecoration(itemDecoration);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (GlobalElements.isConnectingToInternet(getActivity())) {
            addCategory("get", "");
        } else {
            GlobalElements.showDialog(getActivity());
        }
        return view;
    }

    private void addCategory(final String type, final String category) {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = null;
            if (type.equals("add")) {
                call = request.addCategory(myPreferences.getPreferences(MyPreferences.id), category);
            } else if (type.equals("get")) {
                call = request.getCategory(myPreferences.getPreferences(MyPreferences.id));
            }
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (type.equals("add")) {
                            if (json.getInt("ack") == 1) {
                                JSONObject result = json.getJSONObject("result");
                                CategoryModel da = new CategoryModel();
                                da.setId("" + result.getString("id"));
                                da.setTitle("" + category);
                                da.setCount(0);
                                categoryModels.add(da);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toaster.show(getActivity(), "" + json.getString("ack_msg"), false, Toaster.DANGER);
                            }
                        } else {
                            if (json.getInt("ack") == 1) {
                                JSONArray result = json.getJSONArray("result");
                                categoryModels.clear();
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    CategoryModel da = new CategoryModel();
                                    da.setId("" + c.getString("id"));
                                    da.setTitle("" + c.getString("category"));
                                    da.setCount(0);
                                    categoryModels.add(da);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                //Toaster.show(getActivity(), "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
