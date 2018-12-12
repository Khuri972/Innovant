package com.innovent.erp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.AddNoteActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.NoteHistory;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.NoteModel;
import com.innovent.erp.model.NotificationModel;
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

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<NoteModel> data = new ArrayList<>();
    private Context context;
    MyPreferences myPreferences;

    public NoteAdapter(Context context, ArrayList<NoteModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.title.setText(data.get(i).getTitle());
        viewHolder.detail.setText(data.get(i).getDesc());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNoteActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("nid", data.get(i).getId());
                intent.putExtra("title", data.get(i).getTitle());
                intent.putExtra("desc", data.get(i).getDesc());
                intent.putExtra("position", i);
                ((Activity) context).startActivityForResult(intent, 0);
            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog buildInfosDialog;
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                alertDialog2.setTitle("Are you sure want to delete");

                alertDialog2.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                if (GlobalElements.isConnectingToInternet(context)) {
                                    deleteNote(i, data.get(i).getId());
                                } else {
                                    GlobalElements.showDialog(context);
                                }
                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                            }
                        });
                buildInfosDialog = alertDialog2.create();
                buildInfosDialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_note_title)
        TextView title;
        @BindView(R.id.list_note_desc)
        TextView detail;
        @BindView(R.id.list_notification_main)
        FrameLayout layout;
        @BindView(R.id.delete_note)
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void deleteNote(final int position, String nid) {
        try {
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.deleteNote(myPreferences.getPreferences(MyPreferences.id), nid);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
