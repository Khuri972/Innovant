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
import com.innovent.erp.music.MusicListActivity;
import com.innovent.erp.music.SubCategoryActivity;
import com.innovent.erp.music.custom.AppConstant;
import com.innovent.erp.music.model.SubCategory;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.RecyclerViewHolder> {
    private ArrayList<SubCategory> subCategories;
    private Context context;
    String category_id;
    public SubCategoryAdapter(Context context, ArrayList<SubCategory> itemList,String category_id) {
        this.context = context;
        this.subCategories = itemList;
        this.category_id=category_id;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_list_item,parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 30;
        layoutView.setLayoutParams(lp);
        return new RecyclerViewHolder(layoutView);
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        SubCategory subCategory = subCategories.get(position);
        holder.tvSubcategoryName.setText(subCategory.getName());
        Glide.with(context).load(subCategory.getImageUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgSubCategory);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra(AppConstant.SUB_CATEGORY, position);
                context.startActivity(i);
            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundMusicService.SUB_CATEGORY_LIST_POSITION=position;
                Intent i = new Intent(context, MusicListActivity.class);
                i.putExtra("category_id", ""+category_id);
                i.putExtra(AppConstant.SUB_CATEGORY, position);
                context.startActivity(i);
            }
        });
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_subcategory_name)
        AppCompatTextView tvSubcategoryName;
        @BindView(R.id.img_sub_category)
        AppCompatImageView imgSubCategory;
        public RecyclerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
