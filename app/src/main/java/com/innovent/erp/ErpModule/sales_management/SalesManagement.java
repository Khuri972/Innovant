package com.innovent.erp.ErpModule.sales_management;

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

public class SalesManagement extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<DashboardModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_management);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sales Management");

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
        da.setName("My Pipeline");
        da.setImage_path(R.drawable.power);
        da.setTarget(MyPipelineActivity.class);
        da.setColor(ContextCompat.getColor(SalesManagement.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("2");
        da.setName("Quotation Request");
        da.setImage_path(R.drawable.power);
        da.setTarget(QuatationRequest.class);
        da.setColor(ContextCompat.getColor(SalesManagement.this, R.color.dash_border));
        data.add(da);

        da = new DashboardModel();
        da.setId("4");
        da.setName("Sales Order");
        da.setImage_path(R.drawable.power);
        da.setTarget(SalesOrder.class);
        da.setColor(ContextCompat.getColor(SalesManagement.this, R.color.dash_border));
        data.add(da);

       /* da = new DashboardModel();
        da.setId("7");
        da.setName("Sales Return");
        da.setImage_path(R.drawable.power);
        da.setTarget(SalesReturn.class);
        data.add(da);*/

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SalesManagement.this, 3);
        int spanCount = 3; // 3 columns
        int spacing = 3; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setAdapter(new DashboardAdapter(SalesManagement.this, data));
        recyclerView.setLayoutManager(layoutManager);
    }

}
