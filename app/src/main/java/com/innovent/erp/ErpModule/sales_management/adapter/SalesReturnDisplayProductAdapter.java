package com.innovent.erp.ErpModule.sales_management.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.model.ProductModel;
import com.innovent.erp.R;
import com.innovent.erp.helpDesk.model.SerialNoModel;
import com.innovent.erp.netUtils.MyPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class SalesReturnDisplayProductAdapter extends RecyclerView.Adapter<SalesReturnDisplayProductAdapter.ViewHolder> {

    private ArrayList<SerialNoModel> data;
    private Context context;
    MyPreferences myPreferences;

    public interface Intercommunicator {
        public void UpdateQty();
    }

    public void ChangeData() {
        try {
            Intercommunicator intercommunicator = (Intercommunicator) context;
            intercommunicator.UpdateQty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SalesReturnDisplayProductAdapter(Context context, ArrayList<SerialNoModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sales_return_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.salesProductDesc.setText("" + data.get(i).getName() + "\n\n" + data.get(i).getHsn_code());
        viewHolder.salesProductSerialNo.setText("" + data.get(i).getSerialNo());

        viewHolder.salesProductDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog buildInfosDialog;
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                alertDialog2.setTitle("Are you sure want delete this item");

                alertDialog2.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                data.remove(i);
                                notifyDataSetChanged();
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

        @BindView(R.id.list_product_selection_view)
        View listProductSelectionView;
        @BindView(R.id.sales_product_desc)
        TextView salesProductDesc;
        @BindView(R.id.sales_product_serial_no)
        TextView salesProductSerialNo;
        @BindView(R.id.sales_product_delete)
        ImageView salesProductDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
