package com.innovent.erp.visitorBookModual.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.visitorBookModual.model.CategoryModel;

import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CategoryModel> data;
    private Context context;

    public interface intercommunication {
        public void changeData(int position);
    }

    public CategoryAdapter(Context context, ArrayList<CategoryModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.category_name.setText(data.get(i).getName());
        viewHolder.radio.setTag("" + data.get(i).getId());
        viewHolder.radio.setChecked(data.get(i).isCheck());
        viewHolder.radio.setOnCheckedChangeListener(null);
        viewHolder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (viewHolder.radio.getTag().equals("" + data.get(i).getId())) {
                    if (b) {
                        intercommunication intercommunication = (intercommunication) context;
                        intercommunication.changeData(i);
                    } else {
                        intercommunication intercommunication = (intercommunication) context;
                        intercommunication.changeData(i);
                    }
                }
            }
        });

        viewHolder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(i).isCheck()) {
                    viewHolder.radio.setChecked(false);
                    intercommunication intercommunication = (intercommunication) context;
                    intercommunication.changeData(i);
                } else {
                    viewHolder.radio.setChecked(true);
                    intercommunication intercommunication = (intercommunication) context;
                    intercommunication.changeData(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name;
        LinearLayout main_layout;
        RadioButton radio;

        public ViewHolder(View view) {
            super(view);
            radio = (RadioButton) view.findViewById(R.id.radio);
            main_layout = (LinearLayout) view.findViewById(R.id.main_layout);
            category_name = (TextView) view.findViewById(R.id.category_name);
        }
    }
}
