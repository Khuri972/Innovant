package com.innovent.erp;

import android.app.ProgressDialog;
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

import com.innovent.erp.adapter.NoteAdapter;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CategoryModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.NoteModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.pageAdapter.NotePagerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteHistory extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add_Note)
    FloatingActionButton addNote;

    ArrayList<NoteModel> noteModels = new ArrayList<>();
    NoteAdapter noteAdapter;
    String cid, category_name;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_history);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(this);

        try {
            Intent intent = getIntent();
            cid = intent.getStringExtra("cid");
            category_name = intent.getStringExtra("category_name");
            getSupportActionBar().setTitle(category_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        noteAdapter = new NoteAdapter(NoteHistory.this, noteModels);
        recycleview.setAdapter(noteAdapter);
        recycleview.setLayoutManager(new LinearLayoutManager(NoteHistory.this, LinearLayoutManager.VERTICAL, false));

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteHistory.this, AddNoteActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("cid", cid);
                intent.putExtra("category_name", category_name);
                startActivityForResult(intent, 0);
            }
        });

        if (GlobalElements.isConnectingToInternet(NoteHistory.this)) {
            getNotesHistory();
        } else {
            GlobalElements.showDialog(NoteHistory.this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            try {
                NoteModel da = (NoteModel) data.getSerializableExtra("data");
                noteModels.add(da);
                noteAdapter.notifyDataSetChanged();
                if (noteModels.isEmpty()) {
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
        else if(resultCode==11)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getNotesHistory() {
        try {
            final ProgressDialog pd = new ProgressDialog(NoteHistory.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getNoteHistory(myPreferences.getPreferences(MyPreferences.id), cid);

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

                            noteAdapter.notifyDataSetChanged();
                            if (noteModels.isEmpty()) {
                                recycleview.setVisibility(View.GONE);
                                emptyLayout.setVisibility(View.VISIBLE);
                            } else {
                                recycleview.setVisibility(View.VISIBLE);
                                emptyLayout.setVisibility(View.GONE);
                            }

                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyText.setText("" + json.getString("ack_msg"));
                            recycleview.setVisibility(View.GONE);
                            //Toaster.show(NoteHistory.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
