package com.innovent.erp.helpDesk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.helpDesk.model.SerialNoModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class PipeLineDisplayAdapter extends RecyclerView.Adapter<PipeLineDisplayAdapter.ViewHolder> {


    private ArrayList<SerialNoModel> data = new ArrayList<>();
    private ArrayList<SerialNoModel> arraylist;
    private Context context;

    public PipeLineDisplayAdapter(Context context, ArrayList<SerialNoModel> data) {
        this.data = data;
        this.context = context;
        arraylist = new ArrayList<>();
        this.arraylist.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pipeline_display_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.listProductName.setText("" + data.get(i).getName());
        viewHolder.listProductModel.setText("" + data.get(i).getModel());
        viewHolder.listProductColor.setText("" + data.get(i).getColor());
        viewHolder.listSerialNo.setText("" + data.get(i).getSerialNo());
        viewHolder.listPurchaseDate.setText("" + data.get(i).getPurchaseDate());
        viewHolder.listPartsGuarantee.setText("" + data.get(i).getParts_gaurantee());
        viewHolder.listFullBody.setText("" + data.get(i).getFull_body_gaurantee());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_product_selection_view)
        View listProductSelectionView;
        @BindView(R.id.list_product_name)
        TextView listProductName;
        @BindView(R.id.list_serial_no)
        TextView listSerialNo;
        @BindView(R.id.list_product_model)
        TextView listProductModel;
        @BindView(R.id.list_product_color)
        TextView listProductColor;
        @BindView(R.id.list_purchase_date)
        TextView listPurchaseDate;
        @BindView(R.id.list_purchase1_date)
        TextView listPurchase1Date;

        @BindView(R.id.list_parts_guarantee)
        TextView listPartsGuarantee;
        @BindView(R.id.list_full_body)
        TextView listFullBody;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
