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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.innovent.erp.music.SubCategoryActivity;
import com.innovent.erp.music.custom.AppConstant;
import com.innovent.erp.music.model.MainCategory;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.RecyclerViewHolder> {

    public static ArrayList<MainCategory> mainCategories;
    private Context context;

    public MainCategoryAdapter(Context context, ArrayList<MainCategory> itemList) {
        this.context = context;
        this.mainCategories = itemList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.maincategory_list_item,parent, false);
        return new RecyclerViewHolder(layoutView);
    }

    @Override
    public int getItemCount() {
        return mainCategories.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        MainCategory mainCategory = mainCategories.get(position);
        holder.tvMaincategoryName.setText(mainCategory.getName());

        if (!mainCategory.getImageUrl().equals("")) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options;
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            imageLoader.displayImage(mainCategory.getImageUrl(), holder.imgMainCategory, options);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundMusicService.MAIN_CATEGORY_LIST_POSITION = position;
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra(AppConstant.MAIN_CATEGORY, position);
                context.startActivity(i);
            }
        });
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_maincategory_name)
        AppCompatTextView tvMaincategoryName;
        @BindView(R.id.img_main_category)
        AppCompatImageView imgMainCategory;

        public RecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
