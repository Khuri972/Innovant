package com.innovent.erp.visitorBookModual;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.SpacesItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.custom.Validation;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.visitorBookModual.adapter.CategoryAdapter;
import com.innovent.erp.visitorBookModual.model.CategoryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVisitorActivity extends AppCompatActivity implements CategoryAdapter.intercommunication {

    @BindView(R.id.profile_name_edt)
    EditText profileNameEdt;
    @BindView(R.id.profile_mobile_edt)
    EditText profileMobileEdt;
    @BindView(R.id.category_rv)
    RecyclerView categoryRv;
    @BindView(R.id.profile_email_edt)
    EditText profileEmailEdt;
    @BindView(R.id.reference_edt)
    EditText referenceEdt;
    @BindView(R.id.profile_detail_edt)
    EditText profileDetailEdt;
    @BindView(R.id.project_manager_spin)
    Spinner projectManagerSpin;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.send_save_btn)
    Button sendSaveBtn;
    @BindView(R.id.nestedScrollview)
    NestedScrollView nestedScrollview;

    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    ArrayList<GeneralModel> managerModels = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    GeneralAdapter managerAdapter;
    int category_id = 0;
    String save_type, project_manager_id = "";
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visitor);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        this.setTitle("Add Visitor");
        myPreferences = new MyPreferences(AddVisitorActivity.this);
        getSupportActionBar().setSubtitle(myPreferences.getPreferences(MyPreferences.project_title));

        if (GlobalElements.isConnectingToInternet(AddVisitorActivity.this)) {
            getcategory("category");
        } else {
            GlobalElements.showDialog(AddVisitorActivity.this);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.BLANK_CHECK, profileNameEdt.getText().toString())) {
                    profileNameEdt.setError("" + getResources().getString(R.string.visitor_name));
                } else if (!Validation.isValid(Validation.BLANK_CHECK, profileMobileEdt.getText().toString())) {
                    profileMobileEdt.setError("" + getResources().getString(R.string.visitor_mobile));
                } else if (!Validation.isValid(Validation.MOBILE, profileMobileEdt.getText().toString())) {
                    profileMobileEdt.setError("" + getResources().getString(R.string.visitor_mobile_validattion));
                } else if (!profileEmailEdt.getText().toString().equals("") && !Validation.isValid(Validation.EMAIL, profileEmailEdt.getText().toString())) {
                    profileEmailEdt.setError("" + getResources().getString(R.string.visitor_email));
                } else {
                    if (GlobalElements.isConnectingToInternet(AddVisitorActivity.this)) {
                        //project_manager_id = managerModels.get(projectManagerSpin.getSelectedItemPosition()).getId();
                        save_type = "0";
                        addVisitor("add_data");
                    } else {
                        GlobalElements.showDialog(AddVisitorActivity.this);
                    }
                }
            }
        });

        sendSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validation.isValid(Validation.BLANK_CHECK, profileNameEdt.getText().toString())) {
                    profileNameEdt.setError("" + getResources().getString(R.string.visitor_name));
                } else if (!Validation.isValid(Validation.BLANK_CHECK, profileMobileEdt.getText().toString())) {
                    profileMobileEdt.setError("" + getResources().getString(R.string.visitor_mobile));
                } else if (!Validation.isValid(Validation.MOBILE, profileMobileEdt.getText().toString())) {
                    profileMobileEdt.setError("" + getResources().getString(R.string.visitor_mobile_validattion));
                } else if (!profileEmailEdt.getText().toString().equals("") && !Validation.isValid(Validation.EMAIL, profileEmailEdt.getText().toString())) {
                    profileEmailEdt.setError("" + getResources().getString(R.string.visitor_email));

                } else {
                    if (GlobalElements.isConnectingToInternet(AddVisitorActivity.this)) {
                        // project_manager_id = managerModels.get(projectManagerSpin.getSelectedItemPosition()).getId();
                        save_type = "1";
                        addVisitor("add_data");
                    } else {
                        GlobalElements.showDialog(AddVisitorActivity.this);
                    }
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeData(int position) {
        try {
            for (int i = 0; i < categoryModels.size(); i++) {
                if (position == i) {
                    CategoryModel da = categoryModels.get(i);
                    category_id = Integer.parseInt(categoryModels.get(i).getId());
                    da.setCheck(true);
                    categoryModels.set(i, da);
                    categoryAdapter.notifyItemChanged(i);
                } else {
                    CategoryModel da = categoryModels.get(i);
                    da.setCheck(false);
                    categoryModels.set(i, da);
                    categoryAdapter.notifyItemChanged(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getcategory(final String type) {
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = null;
        if (type.equals("category")) {
            call = req.getCategory();
        } else if (type.equals("manager")) {
            call = req.getProjectManager(myPreferences.getPreferences(MyPreferences.project_id));
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (type.equals("category")) {
                    try {
                        String jsonst = response.body().string();
                        JSONObject json = new JSONObject(jsonst);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                CategoryModel da = new CategoryModel();
                                da.setId("" + c.get("id"));
                                da.setName("" + c.getString("name"));
                                if (c.get("id").equals("0")) {
                                    da.setCheck(true);
                                } else {
                                    da.setCheck(false);
                                }
                                categoryModels.add(da);
                            }
                            int spanCount = 3; // 3 columns
                            int spacing = 3; // 50px
                            boolean includeEdge = false;
                            categoryAdapter = new CategoryAdapter(AddVisitorActivity.this, categoryModels);
                            categoryRv.setAdapter(categoryAdapter);
                            categoryRv.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
                            categoryRv.setLayoutManager(new GridLayoutManager(AddVisitorActivity.this, 2));
                            categoryAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getcategory("manager");
                } else if (type.equals("manager")) {
                    try {
                        String jsonst = response.body().string();
                        JSONObject json = new JSONObject(jsonst);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId("" + c.get("id"));
                                da.setName("" + c.getString("name"));
                                managerModels.add(da);
                            }
                            managerAdapter = new GeneralAdapter(AddVisitorActivity.this, managerModels);
                            projectManagerSpin.setAdapter(managerAdapter);
                            managerAdapter.notifyDataSetChanged();
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

    private void addVisitor(final String type) {
        final ProgressDialog pd = new ProgressDialog(AddVisitorActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(true);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = null;

        if (type.equals("add_data")) {
            call = req.add_view_data(myPreferences.getPreferences(MyPreferences.id), profileNameEdt.getText().toString(), profileMobileEdt.getText().toString(), profileEmailEdt.getText().toString(), profileDetailEdt.getText().toString(), save_type, category_id, referenceEdt.getText().toString(), myPreferences.getPreferences(MyPreferences.project_id), myPreferences.getPreferences(MyPreferences.id));
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (type.equals("add_data")) {
                    pd.dismiss();
                    try {
                        String jsonst = response.body().string();
                        JSONObject json = new JSONObject(jsonst);
                        View view = AddVisitorActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        if (json.getInt("ack") == 1) {
                            profileEmailEdt.setText("");
                            profileMobileEdt.setText("");
                            profileDetailEdt.setText("");
                            profileNameEdt.setText("");
                            referenceEdt.setText("");
                            projectManagerSpin.setSelection(0);
                            try {
                                for (int i = 0; i < categoryModels.size(); i++) {
                                    CategoryModel da = categoryModels.get(i);
                                    if (categoryModels.get(i).getId().equals("0")) {
                                        da.setCheck(true);
                                    } else {
                                        da.setCheck(false);
                                    }
                                    categoryModels.set(i, da);
                                    categoryAdapter.notifyItemChanged(i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            nestedScrollview.post(new Runnable() {
                                @Override
                                public void run() {
                                    nestedScrollview.scrollTo(0, 0);
                                }
                            });

                            Toaster.show(AddVisitorActivity.this, "" + json.getString("ack_msg"), false, Toaster.SUCCESS);
                        } else {
                            Toaster.show(AddVisitorActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
}
