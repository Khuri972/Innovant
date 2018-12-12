package com.innovent.erp.ErpModule.sales_management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.innovent.erp.R;

public class AddDeliveryOrder extends AppCompatActivity {

    String insert_update_flag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_order);
        try {
            Bundle bundle = getIntent().getExtras();
            insert_update_flag = bundle.getString("type");

            if (insert_update_flag.equals("0")) {
                getSupportActionBar().setTitle("Add Delivery Order");
            } else {
                getSupportActionBar().setTitle("Update Delivery Order");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
