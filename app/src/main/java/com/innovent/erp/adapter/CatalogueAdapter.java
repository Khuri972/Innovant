package com.innovent.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.FileOpen;
import com.innovent.erp.model.CatalogueModel;
import com.innovent.erp.service.DownloadService;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.ViewHolder> {

    private ArrayList<CatalogueModel> data = new ArrayList<>();
    private Context context;

    public CatalogueAdapter(Context context, ArrayList<CatalogueModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_catalogue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.title.setText("" + data.get(i).getTitle());
        viewHolder.name.setText("" + data.get(i).getFile_name());
        viewHolder.date.setText("" + data.get(i).getDate());

        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), GlobalElements.directory_document + "/" + data.get(i).getFile_name());
                    if (file.exists()) {
                        FileOpen.openFile(context, file);
                    } else {
                        Intent intent = new Intent(context, DownloadService.class);
                        intent.putExtra("file_url", data.get(i).getFilePath());
                        context.startService(intent);
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

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.download)
        ImageView download;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
