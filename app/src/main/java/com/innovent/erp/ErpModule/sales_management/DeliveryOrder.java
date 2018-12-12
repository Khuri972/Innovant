package com.innovent.erp.ErpModule.sales_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.adapter.DeliveryOrderAdapter;
import com.innovent.erp.ErpModule.sales_management.model.DeliveryOrderModel;
import com.innovent.erp.ErpModule.sales_management.model.SalesInvoiceModel;
import com.innovent.erp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryOrder extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add)
    FloatingActionButton add;

    ArrayList<DeliveryOrderModel> data = new ArrayList<>();
    DeliveryOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery Order");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryOrder.this, AddDeliveryOrder.class);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });

        DeliveryOrderModel da = new DeliveryOrderModel();
        da.setId("");
        da.setName("");
        data.add(da);
        da = new DeliveryOrderModel();
        da.setId("");
        da.setName("");
        data.add(da);
        da = new DeliveryOrderModel();
        da.setId("");
        da.setName("");
        data.add(da);

        adapter = new DeliveryOrderAdapter(DeliveryOrder.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(DeliveryOrder.this, LinearLayoutManager.VERTICAL, false));


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
    protected void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        try {
            if (resultCode == 10) {
                DeliveryOrderModel da = (DeliveryOrderModel) _data.getSerializableExtra("data");
                data.add(da);
                adapter.notifyDataSetChanged();
            } else if (resultCode == 11) {
                DeliveryOrderModel da = (DeliveryOrderModel) _data.getSerializableExtra("data");
                int position = _data.getIntExtra("position", 0);
                data.set(position, da);
                adapter.notifyItemChanged(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
