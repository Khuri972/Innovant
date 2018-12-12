package com.innovent.erp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import com.innovent.erp.R;
import com.innovent.erp.interfacess.ItemTouchHelperAdapter;
import com.innovent.erp.interfacess.ItemTouchHelperViewHolder;
import com.innovent.erp.interfacess.OnDashboardChangedListener;
import com.innovent.erp.interfacess.OnStartDragListener;
import com.innovent.erp.model.DashboardCardModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by om on 21-Feb-17.
 */

public class DashboardCardAdapter extends RecyclerView.Adapter<DashboardCardAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    Context mContext;
    List<DashboardCardModel> modelCards;
    private OnStartDragListener mDragStartListener;
    private OnDashboardChangedListener mListChangedListener;

    public DashboardCardAdapter(Context mContext, List<DashboardCardModel> modelCards
            , OnStartDragListener dragLlistener,
                                OnDashboardChangedListener listChangedListener) {
        this.mContext = mContext;
        this.modelCards = modelCards;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText("" + modelCards.get(position).getCardName());
    }

    public DashboardCardModel getItem(int position) {
        return modelCards.get(position);
    }

    @Override
    public int getItemCount() {
        return modelCards.size();
    }

    public void remove(int position) {
        modelCards.remove(position);
        notifyItemRemoved(position);
    }

    /*public void swap(int firstPosition, int secondPosition) {
        Collections.swap(modelCards, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }*/

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(modelCards, fromPosition, toPosition);
        mListChangedListener.onNoteListChanged(modelCards);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @BindView(R.id.list_task_name)
        TextView name;
        @BindView(R.id.list_task_desc)
        TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
