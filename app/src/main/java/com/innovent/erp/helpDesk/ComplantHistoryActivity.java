package com.innovent.erp.helpDesk;

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

import com.innovent.erp.R;
import com.innovent.erp.helpDesk.adapter.ComplantAdapter;
import com.innovent.erp.helpDesk.model.ComplantModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComplantHistoryActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add_complant)
    FloatingActionButton addComplant;

    ArrayList<ComplantModel> data = new ArrayList<>();
    ComplantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complant_history);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complant History");

        adapter = new ComplantAdapter(ComplantHistoryActivity.this, data);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(ComplantHistoryActivity.this, LinearLayoutManager.VERTICAL, false));

        addComplant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(ComplantHistoryActivity.this, AddComplantActivity.class);
                    intent.putExtra("type", "0");
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        if (resultCode == 10) {
            try {
                ComplantModel da = (ComplantModel) _data.getSerializableExtra("data");
                data.add(0, da);
                adapter.notifyDataSetChanged();

                if (data.isEmpty()) {
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
}
