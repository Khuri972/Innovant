package com.innovent.erp;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.adapter.ChatRoomAdapter;
import com.innovent.erp.custom.DividerItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.ChatRoomModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<ChatRoomModel> data = new ArrayList<>();
    ChatRoomAdapter chatRoomAdapter;
    MyPreferences myPreferences;
    String insert_update_flag;
    BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chat Room");
        myPreferences = new MyPreferences(ChatRoomActivity.this);

        try {
            Intent i = getIntent();
            insert_update_flag = i.getStringExtra("insert_update_flag");
            if (insert_update_flag == null || insert_update_flag.equals("")) {
                insert_update_flag = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            insert_update_flag = "0";
        }

        if (GlobalElements.isConnectingToInternet(ChatRoomActivity.this)) {
            getChatRoom();
        } else {
            GlobalElements.showDialog(ChatRoomActivity.this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlobalElements.isChatActive = true;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();

                int notificationId = b.getInt("notificationId");
                int chat_room_id = b.getInt("chat_room_id");
                int new_message_count = b.getInt("new_message_count");

                try {
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getId().equals("" + chat_room_id)) {
                            ChatRoomModel da = data.get(i);
                            da.setNew_message_count(new_message_count);
                            data.set(i, da);
                            chatRoomAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                abortBroadcast();
                try {
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(notificationId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1);
        intentFilter.addAction("com.cdms.Firebase");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    if (insert_update_flag.equals("0")) {
                        finish();
                    } else {
                        Intent i = new Intent(ChatRoomActivity.this, DashboardActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (insert_update_flag.equals("0")) {
            finish();
        } else {
            Intent i = new Intent(ChatRoomActivity.this, DashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    private void getChatRoom() {
        try {
            final ProgressDialog pd = new ProgressDialog(ChatRoomActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getChatRoom(myPreferences.getPreferences(MyPreferences.id), "1");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                ChatRoomModel da = new ChatRoomModel();
                                da.setId("" + c.getString("id"));
                                da.setTitle("" + c.getString("title"));
                                da.setNew_message_count(c.getInt("new_message_count"));
                                data.add(da);
                            }
                            chatRoomAdapter = new ChatRoomAdapter(ChatRoomActivity.this, data);
                            recycleview.setAdapter(chatRoomAdapter);
                            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(ChatRoomActivity.this);
                            recycleview.addItemDecoration(itemDecoration);
                            recycleview.setLayoutManager(new LinearLayoutManager(ChatRoomActivity.this, LinearLayout.VERTICAL, false));
                            recycleview.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            recycleview.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyText.setText("" + json.getString("ack_msg"));
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
