package com.innovent.erp.visitorBookModual;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.innovent.erp.DashboardActivity;
import com.innovent.erp.R;
import com.innovent.erp.adapter.DashboardAdapter;
import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.custom.SpacesItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.DashboardModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
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

public class VisitorBookActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    MyPreferences myPreferences;

    ArrayList<DashboardModel> data = new ArrayList<>();
    ArrayList<GeneralModel> categoryModels = new ArrayList<>();
    GeneralAdapter generalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_book);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Digital Visitor Book");
        myPreferences = new MyPreferences(this);
        LoadTiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_visitor_book, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem projectID = menu.findItem(R.id.action_change_project);
        try {
            if (!myPreferences.getPreferences(MyPreferences.projects_array).equals("")) {
                JSONArray projects_array = new JSONArray(myPreferences.getPreferences(MyPreferences.projects_array));
                if (projects_array.length() > 1) {
                    projectID.setVisible(true);
                } else {
                    for (int i = 0; i < projects_array.length(); i++) {
                        JSONObject c = projects_array.getJSONObject(i);
                        myPreferences.setPreferences(MyPreferences.project_id, c.getString("project_id"));
                        myPreferences.setPreferences(MyPreferences.project_title, c.getString("project_title"));
                    }
                    projectID.setVisible(false);
                }
            }
            try {
                getSupportActionBar().setSubtitle(myPreferences.getPreferences(MyPreferences.project_title));
            } catch (Exception e) {
                e.printStackTrace();
            }
            invalidateOptionsMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_change_project:
                selectProject();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadTiles() {

        try {
            data.clear();
            DashboardModel da;

            if (myPreferences.getPreferences(MyPreferences.INSERT_FLAG).equals("1")) {
                da = new DashboardModel();
                da.setId("11");
                da.setName("Add");
                da.setImage_path(R.drawable.new_inquiry);
                da.setTarget(AddVisitorActivity.class);
                da.setColor(ContextCompat.getColor(VisitorBookActivity.this, R.color.dash_border));
                data.add(da);
            }

            if (myPreferences.getPreferences(MyPreferences.VIEW_FLAG).equals("1")) {
                da = new DashboardModel();
                da.setId("12");
                da.setName("View");
                da.setImage_path(R.drawable.view);
                da.setTarget(VisitorHistoryActivity.class);
                da.setColor(ContextCompat.getColor(VisitorBookActivity.this, R.color.dash_border));
                data.add(da);
            }

            if (myPreferences.getPreferences(MyPreferences.followup).equals("1")) {
                da = new DashboardModel();
                da.setId("13");
                da.setName("Todays Follow Up");
                da.setImage_path(R.drawable.followup);
                da.setTarget(TodaysFollowupActivity.class);
                da.setColor(ContextCompat.getColor(VisitorBookActivity.this, R.color.dash_border));
                data.add(da);
            }


            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(VisitorBookActivity.this, 2);
            int spanCount = 2; // 3 columns
            int spacing = 2; // 50px
            boolean includeEdge = true;
            recycleview.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
            recycleview.setAdapter(new DashboardAdapter(VisitorBookActivity.this, data));
            recycleview.setLayoutManager(layoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectProject() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.dialog_project, null);
        final AlertDialog.Builder dg = new AlertDialog.Builder(VisitorBookActivity.this);
        dg.setView(menuLayout);
        final AlertDialog dialog = dg.create();
        final Spinner project_spin = (Spinner) menuLayout.findViewById(R.id.project_spin);
        Button submit = (Button) menuLayout.findViewById(R.id.submit);
        Button cancel = (Button) menuLayout.findViewById(R.id.cancel);

        try {
            if (!myPreferences.getPreferences(MyPreferences.projects_array).equals("")) {
                JSONArray projects_array = new JSONArray(myPreferences.getPreferences(MyPreferences.projects_array));

                if (projects_array.length() > 1) {
                    categoryModels.clear();
                    for (int i = 0; i < projects_array.length(); i++) {
                        JSONObject c = projects_array.getJSONObject(i);
                        GeneralModel da = new GeneralModel();
                        da.setId(c.getString("project_id"));
                        da.setName(c.getString("project_title"));
                        categoryModels.add(da);
                    }
                    generalAdapter = new GeneralAdapter(VisitorBookActivity.this, categoryModels);
                    project_spin.setAdapter(generalAdapter);
                    generalAdapter.notifyDataSetChanged();

                    for (int i = 0; i < categoryModels.size(); i++) {
                        if (myPreferences.getPreferences(MyPreferences.project_id).equals("" + categoryModels.get(i).getId())) {
                            project_spin.setSelection(i);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String project_id = categoryModels.get(project_spin.getSelectedItemPosition()).getId();
                    String project_title = categoryModels.get(project_spin.getSelectedItemPosition()).getName();
                    myPreferences.setPreferences(MyPreferences.project_id, project_id);
                    myPreferences.setPreferences(MyPreferences.project_title, project_title);
                    getSupportActionBar().setSubtitle(myPreferences.getPreferences(MyPreferences.project_title));
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }
}
