package com.innovent.erp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.innovent.erp.adapter.ChatAdapter;
import com.innovent.erp.custom.RecyclerViewPositionHelper;
import com.innovent.erp.model.ChatModel;
import com.innovent.erp.netUtils.MyPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chat_progress)
    ProgressBar chat_progress;
    @BindView(R.id.recv_chat)
    RecyclerView recyclerView;
    @BindView(R.id.edt_message)
    EditText edt_message;
    @BindView(R.id.img_attach)
    ImageView img_attach;
    @BindView(R.id.chat_linear)
    LinearLayout chat_linear;

    ArrayList<ChatModel> data = new ArrayList<>();
    ChatAdapter adapter;
    String id = "", insert_update_flag = "0";
    String content = "";
    BroadcastReceiver receiver;
    MyPreferences myPreferences;

    /* load more */
    int firstVisibleItem, visibleItemCount, totalItemCount, count = 0;
    protected int m_PreviousTotalCount;
    RecyclerViewPositionHelper mRecyclerViewHelper;


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

                String id = b.getString("id");
                String media_title = b.getString("media_title");
                String isOwnMessage = b.getString("isOwnMessage");
                String message_type = b.getString("message_type");
                String message = b.getString("message");
                String media_url = b.getString("media_url");
                String datetime = b.getString("datetime");
                int notificationId = b.getInt("notificationId");

                ChatModel da = new ChatModel();
                da.setId(id);
                da.setMedia_title(media_title);
                da.setIsOwnMessage(isOwnMessage);
                da.setMessage_type(message_type);
                da.setMessage(message);
                da.setMedia_url(media_url);
                da.setDate_time(datetime);
                data.add(0, da);

                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        myPreferences = new MyPreferences(ChatActivity.this);
        try {
            Intent i = getIntent();
            id = i.getStringExtra("id");
            insert_update_flag = i.getStringExtra("insert_update_flag");
            getSupportActionBar().setTitle("" + i.getStringExtra("title"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        chat_linear.setBackground(GlobalElements.RoundedButtion_gray_redies_10(ChatActivity.this));

        adapter = new ChatAdapter(ChatActivity.this, data, "");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mRecyclerViewHelper.getItemCount();
                firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();

                if (totalItemCount == 0 || adapter == null)
                    return;
                if (m_PreviousTotalCount == totalItemCount) {
                    return;
                } else {
                    boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                    if (loadMore) {
                        m_PreviousTotalCount = totalItemCount;
                        if (GlobalElements.isConnectingToInternet(ChatActivity.this)) {
                            //getMessage();
                        } else {
                            GlobalElements.showDialog(ChatActivity.this);
                        }
                    }
                }
            }
        });

        edt_message.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                edt_message.requestLayout();
                ChatActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
                return false;
            }
        });

        edt_message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt_message.getRight() - edt_message.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        DateFormat df = new SimpleDateFormat("M d hh:mm");
                        Date dateobj = new Date();

                        if (!edt_message.getText().toString().equals("")) {

                            content = edt_message.getText().toString();


                            if (GlobalElements.isConnectingToInternet(ChatActivity.this)) {
                                ChatModel da = new ChatModel();
                                da.setId("1");
                                da.setMedia_title("hardip");
                                da.setIsOwnMessage("1");
                                da.setMessage_type("0");
                                da.setMessage("" + edt_message.getText().toString());
                                da.setDate_time("" + GlobalElements.getTime());
                                da.setMedia_url("");
                                data.add(0, da);
                                adapter.notifyDataSetChanged();


                                recyclerView.scrollToPosition(0);
                                //sendMessage(content);
                                edt_message.setText("");
                            } else {
                                GlobalElements.showDialog(ChatActivity.this);
                            }
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (insert_update_flag.equals("0")) {
                finish();
            } else {
                Intent i = new Intent(ChatActivity.this, ChatRoomActivity.class);
                i.putExtra("insert_update_flag", "1");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (insert_update_flag.equals("0")) {
            finish();
        } else {
            Intent i = new Intent(ChatActivity.this, ChatRoomActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("insert_update_flag", "1");
            startActivity(i);
            finish();
        }
    }

    /*private void getMessage() {
        try {
            chat_progress.setVisibility(View.VISIBLE);
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getMessage(myPreferences.getPreferences(MyPreferences.ID), id, "1", count, GlobalElements.ll_count);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        chat_progress.setVisibility(View.GONE);
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);

                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                ChatModel da = new ChatModel();
                                da.setId(c.getString("id"));
                                da.setMedia_title(c.getString("media_title"));
                                da.setIsOwnMessage(c.getString("isOwnMessage"));
                                da.setMessage_type(c.getString("message_type"));
                                da.setMessage(c.getString("message"));
                                da.setMedia_url(c.getString("media_url"));
                                da.setDate_time(c.getString("datetime"));
                                data.add(da);
                            }

                            if (count == 0) {
                                count += result.length();
                                adapter = new ChatAdapter(ChatActivity.this, data, myPreferences.getPreferences(MyPreferences.ID));
                                recyclerView.setAdapter(adapter);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
                                mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.scrollToPosition(0);
                            } else {
                                count += result.length();
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            if (count == 0) {
                                adapter = new ChatAdapter(ChatActivity.this, data, myPreferences.getPreferences(MyPreferences.ID));
                                recyclerView.setAdapter(adapter);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(ChatActivity.this);
                                mLayoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.scrollToPosition(0);
                                //Toaster.show(ChatActivity.this,""+json.getString("ack_msg"),true,Toaster.DANGER);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {

            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.sendMessage(myPreferences.getPreferences(MyPreferences.ID), message, "0", id);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {

                        } else {
                            //Toaster.show(ChatActivity.this,""+json.getString("ack_msg"),true,Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
