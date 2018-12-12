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

import com.innovent.erp.ErpModule.sales_management.QuatationRequestDetail;
import com.innovent.erp.ErpModule.sales_management.interFace.UpdateData;
import com.innovent.erp.ErpModule.sales_management.model.QuatationRequestModel;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.employeeManagement.interFace.DeleteDailyReport;
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
public class QuatationRequestAdapter extends RecyclerView.Adapter<QuatationRequestAdapter.ViewHolder> {

    private ArrayList<QuatationRequestModel> data;
    private Context context;
    MyPreferences myPreferences;

    public QuatationRequestAdapter(Context context, ArrayList<QuatationRequestModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_quatation_request_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        try {
            viewHolder.status.setText("" + data.get(i).getStatus_name());
            viewHolder.name.setText("" + data.get(i).getCustomer_name());
            viewHolder.quatationNo.setText("Quotation No : " + data.get(i).getQuotation_request_no());
            viewHolder.quatationMobile.setText("Mobile : " + data.get(i).getCustomer_phone_1());
            viewHolder.quatationGrandTotal.setText("Grand Total : " + data.get(i).getGrandtotal());
            viewHolder.quatationDate.setText("Quotation Request Date : " + data.get(i).getQuotation_request_date_format());

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
                try {
                    AlertDialog buildInfosDialog;
                    AlertDialog.Builder alertDialog2;
                    alertDialog2 = new AlertDialog.Builder(context);
                    alertDialog2.setTitle("Are you sure want to delete");
                    alertDialog2.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    if (GlobalElements.isConnectingToInternet(context)) {
                                        deleteQuatation(data.get(i).getId(), i);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, QuatationRequestDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putSerializable("data", data.get(i));
                    intent.putExtras(bundle);
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

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.quatation_status)
        TextView status;
        @BindView(R.id.quatation_no)
        TextView quatationNo;
        @BindView(R.id.quatation_mobile)
        TextView quatationMobile;
        @BindView(R.id.quatation_grand_total)
        TextView quatationGrandTotal;
        @BindView(R.id.quatation_date)
        TextView quatationDate;
        @BindView(R.id.cancel_order)
        ImageView cancelOrder;
        @BindView(R.id.cancel_layout)
        LinearLayout cancelLayout;
        @BindView(R.id.list_inquiry_main)
        LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void deleteQuatation(String id, final int position) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient1().create(RequestInterface.class);
        Call<ResponseBody> call = req.deleteQuatationRequest(myPreferences.getPreferences(MyPreferences.id), id, "quotation");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        data.remove(position);
                        notifyDataSetChanged();
                        if (data.isEmpty()) {
                            try {
                                UpdateData updateData = (UpdateData) context;
                                updateData.callbackCall();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }

        });
    }
}
