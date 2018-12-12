package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CategoryModel;
import com.innovent.erp.model.NoteModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity {

    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.desc)
    EditText desc;

    MyPreferences myPreferences;
    String cid, type="0", nid;
    NoteModel noteModel = new NoteModel();
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        myPreferences = new MyPreferences(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("0")) {
            cid = intent.getStringExtra("cid");
            String category_name = intent.getStringExtra("category_name");
            if (!category_name.equals("")) {
                getSupportActionBar().setTitle("" + category_name);
            }
        } else {
            getSupportActionBar().setTitle("Update Notes");
            nid = intent.getStringExtra("nid");
            position = intent.getIntExtra("position", 0);
            title.setText("" + intent.getStringExtra("title"));
            desc.setText("" + intent.getStringExtra("desc"));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_notes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (title.getText().toString().equals("")) {
                    Toaster.show(AddNoteActivity.this, "Enter Title", false, Toaster.DANGER);
                } else if (desc.getText().toString().equals("")) {
                    Toaster.show(AddNoteActivity.this, "Enter Notes", false, Toaster.DANGER);
                } else {
                    if (GlobalElements.isConnectingToInternet(AddNoteActivity.this)) {
                        addNotes();
                    } else {
                        GlobalElements.showDialog(AddNoteActivity.this);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNotes() {
        try {
            final ProgressDialog pd = new ProgressDialog(AddNoteActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = null;

            if (type.equals("0")) {
                call = request.addNotes(myPreferences.getPreferences(MyPreferences.id), title.getText().toString(), desc.getText().toString(), cid);
            } else if (type.equals("1")) {
                call = request.updateNotes(myPreferences.getPreferences(MyPreferences.id), title.getText().toString(), desc.getText().toString(), nid);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (type.equals("0")) {
                            if (json.getInt("ack") == 1) {
                                JSONObject result = json.getJSONObject("result");
                                noteModel.setId(result.getString("id"));
                                noteModel.setTitle(result.getString("title"));
                                noteModel.setDesc(result.getString("description"));
                                noteModel.setNotes_date(result.getString("notes_date"));
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", noteModel);
                                intent.putExtras(bundle);
                                setResult(10, intent);
                                finish();
                            } else {
                                Toaster.show(AddNoteActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                            }
                        } else {
                            if (json.getInt("ack") == 1) {
                                JSONObject result = json.getJSONObject("result");
                                noteModel.setId(result.getString("id"));
                                noteModel.setTitle(result.getString("title"));
                                noteModel.setDesc(result.getString("description"));
                                noteModel.setNotes_date(result.getString("notes_date"));
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", noteModel);
                                bundle.putInt("position", position);
                                intent.putExtras(bundle);
                                setResult(11, intent);
                                finish();
                            } else {
                                Toaster.show(AddNoteActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
