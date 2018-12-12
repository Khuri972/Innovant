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

public class DemoSerialNoAdapter extends RecyclerView.Adapter<DemoSerialNoAdapter.ViewHolder> {

    private ArrayList<SerialNoModel> data = new ArrayList<>();
    private ArrayList<SerialNoModel> arraylist;
    private Context context;

    public DemoSerialNoAdapter(Context context, ArrayList<SerialNoModel> data) {
        this.data = data;
        this.context = context;
        arraylist = new ArrayList<>();
        this.arraylist.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_demo_serial_no, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.listProductName.setText("" + data.get(i).getName());
        viewHolder.listProductBrand.setText("" + data.get(i).getBrand());
        viewHolder.listProductModel.setText("" + data.get(i).getModel());
        viewHolder.listProductColor.setText("" + data.get(i).getColor());
        viewHolder.listSerialNo.setText("" + data.get(i).getSerialNo());
        viewHolder.listPurchaseDate.setText("" + data.get(i).getPurchaseDate());
        viewHolder.listQty.setText("" + data.get(i).getQty());
        if (data.get(i).getPurchaseDate().equals("hide")) {
            viewHolder.listPurchaseDate.setVisibility(View.GONE);
            viewHolder.listPurchaseDate1.setVisibility(View.GONE);
            viewHolder.layoutDelete.setVisibility(View.GONE);
            viewHolder.deleteTxt.setVisibility(View.GONE);
        } else {
            viewHolder.listPurchaseDate.setVisibility(View.VISIBLE);
            viewHolder.listPurchaseDate1.setVisibility(View.VISIBLE);
            viewHolder.layoutDelete.setVisibility(View.VISIBLE);
            viewHolder.deleteTxt.setVisibility(View.VISIBLE);
        }

        viewHolder.salesProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    data.remove(i);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_product_name)
        TextView listProductName;
        @BindView(R.id.list_product_brand)
        TextView listProductBrand;
        @BindView(R.id.list_product_model)
        TextView listProductModel;
        @BindView(R.id.list_product_color)
        TextView listProductColor;
        @BindView(R.id.list_serial_no)
        TextView listSerialNo;
        @BindView(R.id.list_purchase_date)
        TextView listPurchaseDate;
        @BindView(R.id.list_purchase1_date)
        TextView listPurchaseDate1;
        @BindView(R.id.list_qty)
        TextView listQty;
        @BindView(R.id.sales_product_delete)
        ImageView salesProductDelete;
        @BindView(R.id.layout_delete)
        LinearLayout layoutDelete;
        @BindView(R.id.delete_txt)
        TextView deleteTxt;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
