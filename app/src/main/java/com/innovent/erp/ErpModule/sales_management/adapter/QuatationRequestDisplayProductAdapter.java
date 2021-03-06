package com.innovent.erp.ErpModule.sales_management.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.R;
import com.innovent.erp.custom.DigitsInputFilter;
import com.innovent.erp.netUtils.MyPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class QuatationRequestDisplayProductAdapter extends RecyclerView.Adapter<QuatationRequestDisplayProductAdapter.ViewHolder> {

    private ArrayList<ProductModel> data;
    private Context context;
    MyPreferences myPreferences;

    public QuatationRequestDisplayProductAdapter(Context context, ArrayList<ProductModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_display_quatation_request_product_search, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        try {
            viewHolder.salesProductDesc.setText("" + data.get(i).getName());
            viewHolder.salesProductHsn.setText("" + data.get(i).getHsnCode());
            viewHolder.salesProductUnit.setText("" + data.get(i).getUnit());
            viewHolder.salesProductPrice.setText("" + data.get(i).getSell_price());

            viewHolder.salesProductQty.setTag("" + data.get(i).getId());
            viewHolder.salesProductDisctount.setTag("" + data.get(i).getId());
            viewHolder.salesProductPrice.setTag("" + data.get(i).getId());

            viewHolder.salesProductQty.setClickable(false);
            viewHolder.salesProductQty.setFocusable(false);
            viewHolder.salesProductQty.setFocusableInTouchMode(false);
            viewHolder.salesProductQty.setCursorVisible(false);

            viewHolder.salesProductDisctount.setClickable(false);
            viewHolder.salesProductDisctount.setFocusable(false);
            viewHolder.salesProductDisctount.setFocusableInTouchMode(false);
            viewHolder.salesProductDisctount.setCursorVisible(false);

            viewHolder.salesProductPrice.setClickable(false);
            viewHolder.salesProductPrice.setFocusable(false);
            viewHolder.salesProductPrice.setFocusableInTouchMode(false);
            viewHolder.salesProductPrice.setCursorVisible(false);

            viewHolder.salesProductPrice.setFilters(new InputFilter[]{new DigitsInputFilter(5, 2)});

            if (data.get(i).getQty() == 0) {
                viewHolder.salesProductQty.setText("");
                viewHolder.salesProductAmnt.setText("");
            } else {
                viewHolder.salesProductQty.setText("" + data.get(i).getQty());
                data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                // data.get(i).setNetAmount(data.get(i).getQty() * data.get(i).getSell_price());
                viewHolder.salesProductAmnt.setText("" + data.get(i).getAmount());
                if (data.get(i).getGstType() == 0) {  // 0 = gujrat
                    data.get(i).setCgst(data.get(i).getGst() / 2);
                    data.get(i).setSgst(data.get(i).getGst() / 2);
                    double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst()) / 100;
                    data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                } else {
                    data.get(i).setIgst(data.get(i).getGst());
                    double netAmount = data.get(i).getAmount() * data.get(i).getIgst() / 100;
                    data.get(i).setNetAmount(data.get(i).getAmount() + netAmount);
                }
            }

            if (data.get(i).getDiscount() == 0) {
                viewHolder.salesProductDisctount.setText("");
                data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
            } else {
                viewHolder.salesProductDisctount.setText("" + data.get(i).getDiscount());
                data.get(i).setAmount(data.get(i).getQty() * data.get(i).getSell_price());
                data.get(i).setAmount((data.get(i).getAmount() - (data.get(i).getAmount() * data.get(i).getDiscount() / 100)));

                if (data.get(i).getGstType() == 0) {
                    data.get(i).setCgst(data.get(i).getGst() / 2);
                    data.get(i).setSgst(data.get(i).getGst() / 2);
                    double netAmount = data.get(i).getAmount() * (data.get(i).getCgst() + data.get(i).getSgst())/100;
                    data.get(i).setNetAmount(data.get(i).getAmount()+netAmount);
                } else {
                    data.get(i).setIgst(data.get(i).getGst());
                    double netAmount = data.get(i).getAmount() * data.get(i).getIgst()/100;
                    data.get(i).setNetAmount(data.get(i).getAmount()+netAmount);
                }
                viewHolder.salesProductAmnt.setText(""+data.get(i).getAmount());
                viewHolder.salesProductNatAmt.setText("" + data.get(i).getNetAmount());
            }

            viewHolder.salesProductGst.setText("" + data.get(i).getGst());
            viewHolder.salesProductCgst.setText("" + data.get(i).getCgst());
            viewHolder.salesProductSgst.setText("" + data.get(i).getSgst());
            viewHolder.salesProductIgst.setText("" + data.get(i).getIgst());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sales_product_desc)
        TextView salesProductDesc;
        @BindView(R.id.sales_product_hsn)
        TextView salesProductHsn;
        @BindView(R.id.sales_product_unit)
        TextView salesProductUnit;
        @BindView(R.id.sales_product_qty)
        EditText salesProductQty;
        @BindView(R.id.sales_product_price)
        EditText salesProductPrice;
        @BindView(R.id.sales_product_disctount)
        EditText salesProductDisctount;
        @BindView(R.id.sales_product_amnt)
        TextView salesProductAmnt;
        @BindView(R.id.sales_product_gst)
        TextView salesProductGst;
        @BindView(R.id.sales_product_cgst)
        TextView salesProductCgst;
        @BindView(R.id.sales_product_sgst)
        TextView salesProductSgst;
        @BindView(R.id.sales_product_igst)
        TextView salesProductIgst;
        @BindView(R.id.sales_product_nat_amt)
        TextView salesProductNatAmt;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
