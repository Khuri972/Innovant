package com.innovent.erp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Suleiman on 26-07-2015.
 */
public class DocumentFilterAdapter extends RecyclerView.Adapter<DocumentFilterAdapter.ViewHolder> {

    ArrayList<GeneralModel> data = new ArrayList<>();
    Context context;
    String sort;

    public interface shortFilter {
        public void shortFilter(String id);
    }

    public DocumentFilterAdapter(Context context, ArrayList<GeneralModel> da, String sort) {
        this.context = context;
        this.data = da;
        this.sort = sort;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_move_task, parent, false);
        ViewHolder holder = new ViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            holder.name.setText("" + data.get(position).getName());

            if (data.get(position).getId().equals("0")) {
                holder.name.setGravity(Gravity.CENTER);
            } else {
                holder.name.setGravity(Gravity.LEFT);
            }

            try {
                if (sort.equals("" + data.get(position).getId()) && !data.get(position).getId().equals("0")) {
                    holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                } else {
                    holder.name.setTextColor(ContextCompat.getColor(context, R.color.black));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (!data.get(position).getId().equals("0")) {
                            shortFilter filter = (shortFilter) context;
                            filter.shortFilter(data.get(position).getId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.general_name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
