package com.innovent.erp.employeeManagement.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.employeeManagement.AddExpanceActivity;
import com.innovent.erp.employeeManagement.model.ExpanceModel;
import com.innovent.erp.service.DownloadService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.shts.android.library.TriangleLabelView;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class ExpanceAdapter extends RecyclerView.Adapter<ExpanceAdapter.ViewHolder> {


    private ArrayList<ExpanceModel> data = new ArrayList<>();
    private Context context;

    public ExpanceAdapter(Context context, ArrayList<ExpanceModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_expance_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.categoryNameTxt.setText("" + data.get(i).getCategory_slug());
        viewHolder.amountTxt.setText("" + data.get(i).getAmount());
        viewHolder.remarkTxt.setText("" + data.get(i).getRemark());
        viewHolder.noteTxt.setText("" + data.get(i).getNote());
        viewHolder.dateTxt.setText("" + data.get(i).getDate());
        viewHolder.fileNameTxt.setText("" + data.get(i).getAttachment_name());

        if (data.get(i).getExpense_type().equals("0")) {
            viewHolder.expanseType.setPrimaryText("Save");
            viewHolder.expanseType.setTriangleBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
        } else if (data.get(i).getExpense_type().equals("1")) {
            viewHolder.expanseType.setPrimaryText("Submitted");
            viewHolder.expanseType.setTriangleBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        } else if (data.get(i).getExpense_type().equals("2")) {
            viewHolder.expanseType.setPrimaryText("Rejected");
            viewHolder.expanseType.setTriangleBackgroundColor(ContextCompat.getColor(context, R.color.red));
        } else if (data.get(i).getExpense_type().equals("3")) {
            viewHolder.expanseType.setPrimaryText("Conform");
            viewHolder.expanseType.setTriangleBackgroundColor(ContextCompat.getColor(context, R.color.green));
        }

        try {
            if (data.get(i).getAttachment_path().equals("")) {
                viewHolder.fileDownloadLayout.setVisibility(View.GONE);
            } else {
                viewHolder.fileDownloadLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.fileDownloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra("file_url", data.get(i).getAttachment_path());
                    context.startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (data.get(i).getExpense_type().equals("0")) {
                        Intent intent = new Intent(context, AddExpanceActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "1");
                        bundle.putInt("position", i);
                        bundle.putSerializable("data", data.get(i));
                        intent.putExtras(bundle);
                        ((Activity) context).startActivityForResult(intent, 0);
                    }
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

        @BindView(R.id.category_name_txt)
        TextView categoryNameTxt;
        @BindView(R.id.amount_txt)
        TextView amountTxt;
        @BindView(R.id.remark_txt)
        TextView remarkTxt;
        @BindView(R.id.note_txt)
        TextView noteTxt;
        @BindView(R.id.date_txt)
        TextView dateTxt;
        @BindView(R.id.file_name_txt)
        TextView fileNameTxt;
        @BindView(R.id.file_download_img)
        ImageView fileDownloadImg;
        @BindView(R.id.file_download_layout)
        LinearLayout fileDownloadLayout;

        @BindView(R.id.main_layout)
        LinearLayout mainLayout;
        @BindView(R.id.expense_type)
        TriangleLabelView expanseType;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
