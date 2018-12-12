package com.innovent.erp.ErpModule.sales_management.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.ErpModule.sales_management.SalesReturnDetailActivity;
import com.innovent.erp.ErpModule.sales_management.model.SalesReturnModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class SalesReturnAdapter extends RecyclerView.Adapter<SalesReturnAdapter.ViewHolder> {


    private ArrayList<SalesReturnModel> data;
    private Context context;
    MyPreferences myPreferences;

    public SalesReturnAdapter(Context context, ArrayList<SalesReturnModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sales_return_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.customerName.setText("" + data.get(i).getCustomer_name());
        viewHolder.salesReturnNo.setText("Sales Return No : " + data.get(i).getSales_return_no());
        viewHolder.status.setText("Status : " + data.get(i).getStatus_name());
        viewHolder.grandTotal.setText("Grand Total : " + data.get(i).getGrandtotal());
        viewHolder.salesReturnDate.setText("Sales Return Date : " + data.get(i).getSales_return_format());

        try {
            if (data.get(i).getStatus().equals("0")) {
                viewHolder.cancelLayout.setVisibility(View.VISIBLE);
            } else {
                viewHolder.cancelLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog buildInfosDialog;
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
                alertDialog2.setTitle("Are you sure want to delete");

                alertDialog2.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                if (GlobalElements.isConnectingToInternet(context)) {
                                    deleteSalesReturn(i, data.get(i).getId());
                                } else {
                                    GlobalElements.showDialog(context);
                                }
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

        viewHolder.listInquiryMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, SalesReturnDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putString("type", "1");
                    b.putString("invoice_id", "" + data.get(i).getId());
                    b.putInt("position", i);
                    b.putSerializable("data", data.get(i));
                    intent.putExtras(b);
                    ((Activity) context).startActivityForResult(intent, 0);
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

        @BindView(R.id.customer_name)
        TextView customerName;
        @BindView(R.id.sales_return_no)
        TextView salesReturnNo;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.grand_total)
        TextView grandTotal;
        @BindView(R.id.sales_return_date)
        TextView salesReturnDate;
        @BindView(R.id.cancel_order)
        ImageView cancelOrder;
        @BindView(R.id.cancel_layout)
        LinearLayout cancelLayout;
        @BindView(R.id.list_inquiry_main)
        LinearLayout listInquiryMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void deleteSalesReturn(final int position, String id) {
        try {
            final ProgressDialog pd = new ProgressDialog(context);
            RequestInterface request = RetrofitClient.getClient1().create(RequestInterface.class);
            Call<ResponseBody> call = request.deleteSalesReturn(myPreferences.getPreferences(MyPreferences.id), id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
                            Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
