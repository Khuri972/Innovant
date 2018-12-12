package com.innovent.erp.music.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.music.PlayMusicActivity;
import com.innovent.erp.music.custom.AppConstant;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.model.Music;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.RecyclerViewHolder> {
    private ArrayList<Music> musicArrayList;
    private Context context;

    public MusicListAdapter(Context context, ArrayList<Music> itemList) {
        this.context = context;
        this.musicArrayList = itemList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item, parent, false);
        return new RecyclerViewHolder(layoutView);
    }

    @Override
    public int getItemCount() {
        return musicArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        Music music = musicArrayList.get(position);
        holder.tvMusicName.setText(music.getName());
        try {
            if (GlobalElements.isMyServiceRunning(backgroundMusicService.class, context)) {
                if (backgroundMusicService.MUSIC_LIST_CURRENT_POSITION == position) {
                    if (MusicDetailFragment.mode == MusicDetailFragment.FRAGMENT_IN_PLAY_MODE) {
                        holder.imgPauseInMusicList.setVisibility(View.VISIBLE);
                        holder.imgPlayInMusicList.setVisibility(View.GONE);
                    } else {
                        holder.imgPlayInMusicList.setVisibility(View.VISIBLE);
                        holder.imgPauseInMusicList.setVisibility(View.GONE);
                    }
                } else {
                    holder.imgPlayInMusicList.setVisibility(View.VISIBLE);
                    holder.imgPauseInMusicList.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundMusicService.MUSIC_LIST_CURRENT_POSITION = position;
                Intent i = new Intent(context, PlayMusicActivity.class);
                i.putExtra(AppConstant.MUSIC, position);
                context.startActivity(i);
            }
        });
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_music_name)
        AppCompatTextView tvMusicName;
        @BindView(R.id.tv_music_time)
        AppCompatTextView tvMusicTime;
        @BindView(R.id.img_play_in_music_list)
        AppCompatImageView imgPlayInMusicList;
        @BindView(R.id.img_pause_in_music_list)
        AppCompatImageView imgPauseInMusicList;

        public RecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
