package com.innovent.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.innovent.erp.ChatActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.model.ChatRoomModel;
import com.github.pavlospt.CircleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Suleiman on 26-07-2015.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    ArrayList<ChatRoomModel> data=new ArrayList<>();
    Context context;
    public ChatRoomAdapter(Context context, ArrayList<ChatRoomModel> da)
    {
        this.context=context;
        this.data=da;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_chat_room, parent, false);
        ViewHolder holder = new ViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        try {
            holder.title.setText(""+data.get(position).getTitle());

            if(data.get(position).getNew_message_count()==0)
            {
                holder.chat_count.setVisibility(View.GONE);
            }
            else
            {
                holder.chat_count.setVisibility(View.VISIBLE);
                holder.chat_count.setTitleText(""+data.get(position).getNew_message_count());
                holder.chat_count.setSubtitleText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if(GlobalElements.isConnectingToInternet(context))
                    {
                        ChatRoomModel da=data.get(position);
                        da.setNew_message_count(0);
                        data.set(position,da);
                        notifyItemChanged(position);

                        Intent i = new Intent(context, ChatActivity.class);
                        i.putExtra("id",""+data.get(position).getId());
                        i.putExtra("title",""+data.get(position).getTitle());
                        i.putExtra("insert_update_flag","0");
                        context.startActivity(i);
                    }
                    else
                    {
                        GlobalElements.showDialog(context);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_room_title) TextView title;
        @BindView(R.id.chat_count)
        CircleView chat_count;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
