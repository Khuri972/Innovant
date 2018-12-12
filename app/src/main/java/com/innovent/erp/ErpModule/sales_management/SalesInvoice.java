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

import com.innovent.erp.ErpModule.sales_management.adapter.SalesInvoiceAdapter;
import com.innovent.erp.ErpModule.sales_management.model.SalesInvoiceModel;
import com.innovent.erp.ErpModule.sales_management.model.SalesOrderModel;
import com.innovent.erp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesInvoice extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add)
    FloatingActionButton add;

    ArrayList<SalesInvoiceModel> data = new ArrayList<>();
    SalesInvoiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_invoice);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sales Invoice");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SalesInvoice.this, AddSalesInvoice.class);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });

        SalesInvoiceModel da = new SalesInvoiceModel();
        da.setId("");
        da.setName("");
        data.add(da);
        da = new SalesInvoiceModel();
        da.setId("");
        da.setName("");
        data.add(da);
        da = new SalesInvoiceModel();
        da.setId("");
        da.setName("");
        data.add(da);

        adapter = new SalesInvoiceAdapter(SalesInvoice.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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
                SalesInvoiceModel da = (SalesInvoiceModel) _data.getSerializableExtra("data");
                data.add(da);
                adapter.notifyDataSetChanged();
            } else if (resultCode == 11) {
                SalesInvoiceModel da = (SalesInvoiceModel) _data.getSerializableExtra("data");
                int position = _data.getIntExtra("position", 0);
                data.set(position, da);
                adapter.notifyItemChanged(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
