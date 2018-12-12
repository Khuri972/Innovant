package com.innovent.erp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.custom.ScaleImageView;
import com.innovent.erp.model.ChatModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by Suleiman on 26-07-2015.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<ChatModel> data=new ArrayList<>();
    Context context;
    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    String uid;
    public ChatAdapter(Context context, ArrayList<ChatModel> da, String uid)
    {
        this.context=context;
        this.data=da;
        this.uid=uid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chat_item, parent, false);
        ViewHolder holder = new ViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            if(data.get(position).getIsOwnMessage().equals("1")) {

                if(data.get(position).getMessage_type().equals("1")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.RIGHT;
                    params.setMargins(5, 5, 5, 5);
                    holder.layout.setPadding(15,15,70,15);
                    holder.layout.setLayoutParams(params);
                    holder.img_message.setVisibility(View.VISIBLE);
                    holder.message.setVisibility(View.GONE);

                    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                    options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.ic_launcher)
                            .showImageForEmptyUri(R.mipmap.ic_launcher)
                            .showImageOnFail(R.mipmap.ic_launcher)
                            .build();
                    imageLoader.displayImage(data.get(position).getMedia_url(), holder.img_message, options);
                    holder.time.setText("" + data.get(position).getDate_time().toUpperCase());
                    holder.message.setText("" + data.get(position).getMessage());

                }
                else
                {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.RIGHT;
                    params.setMargins(5, 5, 5, 5);
                    holder.layout.setPadding(15,15,70,15);
                    holder.layout.setLayoutParams(params);
                    holder.layout.setBackgroundResource(R.drawable.left_layout);

                    holder.message.setVisibility(View.VISIBLE);
                    holder.img_message.setVisibility(View.GONE);
                    holder.message.setText("" + data.get(position).getMessage());
                    holder.time.setText("" + data.get(position).getDate_time().toUpperCase());
                }
            }
            else if(data.get(position).getIsOwnMessage().equals("0"))
            {
                if(data.get(position).getMessage_type().equals("1")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(5, 5, 5, 5);
                    holder.layout.setPadding(70,15,15,15);
                    holder.layout.setLayoutParams(params);
                    holder.img_message.setVisibility(View.VISIBLE);
                    holder.message.setVisibility(View.GONE);

                    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                    options = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.mipmap.ic_launcher)
                            .showImageForEmptyUri(R.mipmap.ic_launcher)
                            .showImageOnFail(R.mipmap.ic_launcher)
                            .build();
                    imageLoader.displayImage(data.get(position).getMedia_url(), holder.img_message, options);
                    holder.time.setText("" + data.get(position).getDate_time());
                    holder.message.setText("" + data.get(position).getMessage().toUpperCase());

                }
                else
                {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.LEFT;
                    params.setMargins(5, 5, 5, 5);
                    holder.layout.setPadding(70,15,15,15);
                    holder.layout.setLayoutParams(params);
                    holder.layout.setBackgroundResource(R.drawable.right_layout);

                    holder.message.setVisibility(View.VISIBLE);
                    holder.img_message.setVisibility(View.GONE);
                    holder.message.setText("" + data.get(position).getMessage());
                    holder.time.setText("" + data.get(position).getDate_time().toUpperCase());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView message,time;
        ScaleImageView img_message;
        //CardView cc_message;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.tv_message);
            time = (TextView)itemView.findViewById(R.id.tv_time);
            img_message = (ScaleImageView)itemView.findViewById(R.id.img_message);
           // cc_message = (CardView) itemView.findViewById(R.id.cc_message);
            layout = (LinearLayout) itemView.findViewById(R.id.ll_message);
        }
    }

}
