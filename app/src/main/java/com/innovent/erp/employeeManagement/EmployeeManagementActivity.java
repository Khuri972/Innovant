package com.innovent.erp.employeeManagement;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.innovent.erp.R;
import com.innovent.erp.adapter.DashboardAdapter;
import com.innovent.erp.custom.SpacesItemDecoration;
import com.innovent.erp.model.DashboardModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeManagementActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<DashboardModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Management");

        LoadTiles();

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

    public void LoadTiles() {

        /*
        * view
        * insert
        * delete
        * update
        * */

        DashboardModel da;
        da = new DashboardModel();
        da.setId("1");
        da.setName("Leave Request");
        da.setImage_path(R.drawable.power);
        da.setTarget(LeaveRequestActivity.class);
        da.setColor(ContextCompat.getColor(EmployeeManagementActivity.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("2");
        da.setName("Daily Report");
        da.setImage_path(R.drawable.power);
        da.setTarget(DailySalesReportActivity.class);
        da.setColor(ContextCompat.getColor(EmployeeManagementActivity.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("3");
        da.setName("Expense");
        da.setImage_path(R.drawable.power);
        da.setTarget(ExpanceActivity.class);
        da.setColor(ContextCompat.getColor(EmployeeManagementActivity.this, R.color.dash_border));
        data.add(da);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(EmployeeManagementActivity.this, 3);
        int spanCount = 3; // 3 columns
        int spacing = 3; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setAdapter(new DashboardAdapter(EmployeeManagementActivity.this, data));
        recyclerView.setLayoutManager(layoutManager);

    }
}
