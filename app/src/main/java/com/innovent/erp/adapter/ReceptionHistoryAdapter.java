package com.innovent.erp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.model.ReceptionModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class ReceptionHistoryAdapter extends RecyclerView.Adapter<ReceptionHistoryAdapter.ViewHolder> {


    private ArrayList<ReceptionModel> data = new ArrayList<>();
    private Context context;

    public ReceptionHistoryAdapter(Context context, ArrayList<ReceptionModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reception_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        try {
            //viewHolder.tag.setText("" + data.get(i).getTag_slug());
            viewHolder.label.setText("" + data.get(i).getLabel_slug());
            viewHolder.titleTxt.setText(""+data.get(i).getTitle());
            viewHolder.personName.setText(""+data.get(i).getPerson_name());
            viewHolder.companyName.setText("" + data.get(i).getCompany_name());
            viewHolder.mobile.setText("" + data.get(i).getMobile_no());
            viewHolder.email.setText("" + data.get(i).getEmail());
            viewHolder.whomeToMeet.setText("" + data.get(i).getWhome_to_meet());

            //viewHolder.date.setText("" + data.get(i).getDate());
            viewHolder.name.setText("" + data.get(i).getName());   // visitor or caller name
            viewHolder.cityTxt.setText(""+data.get(i).getCity());
            viewHolder.desc.setText("" + data.get(i).getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.label)
        TextView label;
        @BindView(R.id.title_txt)
        TextView titleTxt;

        @BindView(R.id.person_name)
        TextView personName;
        @BindView(R.id.company_name)
        TextView companyName;
        @BindView(R.id.mobile)
        TextView mobile;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.city_txt)
        TextView cityTxt;
        @BindView(R.id.whome_to_meet)
        TextView whomeToMeet;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.list_currency_main)
        LinearLayout listCurrencyMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
