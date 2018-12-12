package com.innovent.erp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.AddChequeActivity;
import com.innovent.erp.ChequeHistoryActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.ChequeModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
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
public class ChequeHistoryAdapter extends RecyclerView.Adapter<ChequeHistoryAdapter.ViewHolder> {

    private ArrayList<ChequeModel> data;
    private Context context;
    MyPreferences myPreferences;
    String check_type="";
    public ChequeHistoryAdapter(Context context, ArrayList<ChequeModel> data,String check_type) {
        this.data = data;
        this.context = context;
        this.check_type=check_type;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_cheque_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        try {
            viewHolder.chequeNo.setText("" + data.get(i).getCheque_no());
            viewHolder.bankName.setText("" + data.get(i).getBank_name());
            viewHolder.companyName.setText("" + data.get(i).getCompany_name());
            viewHolder.partyName.setText("" + data.get(i).getParty_name());
            viewHolder.amount.setText("" + data.get(i).getAmount());
            if (data.get(i).getCheque_type().equals("account_pay")) {
                viewHolder.checkType.setText("Account Pay");
            } else if (data.get(i).getCheque_type().equals("self")) {
                viewHolder.checkType.setText("Self");
            }
            viewHolder.date.setText("" + data.get(i).getCheque_date());
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
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
                                    deleteCheque(i, data.get(i).getId());
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

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, AddChequeActivity.class);
                    Bundle b = new Bundle();
                    b.putString("type", "1");
                    b.putString("cheque_type", check_type);
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
        @BindView(R.id.cheque_no)
        TextView chequeNo;
        @BindView(R.id.bank_name)
        TextView bankName;
        @BindView(R.id.company_name)
        TextView companyName;
        @BindView(R.id.party_name)
        TextView partyName;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.check_type)
        TextView checkType;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.delete)
        ImageView delete;
        @BindView(R.id.main_layout)
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void deleteCheque(final int position, String id) {
        try {
            final ProgressDialog pd = new ProgressDialog(context);
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.deleteCheque(myPreferences.getPreferences(MyPreferences.id), id,check_type);
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
