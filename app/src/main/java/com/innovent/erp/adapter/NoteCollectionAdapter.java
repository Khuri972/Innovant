package com.innovent.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.NoteHistory;
import com.innovent.erp.R;
import com.innovent.erp.model.CategoryModel;
import com.innovent.erp.model.DocumentModel;
import com.innovent.erp.model.NoteModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class NoteCollectionAdapter extends RecyclerView.Adapter<NoteCollectionAdapter.ViewHolder> {

    private ArrayList<CategoryModel> data = new ArrayList<>();
    private Context context;

    public NoteCollectionAdapter(Context context, ArrayList<CategoryModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note_collection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.title.setText("" + data.get(i).getTitle());
        viewHolder.detail.setVisibility(View.VISIBLE);
        viewHolder.detail.setText("(" + data.get(i).getCount() + ")");
        viewHolder.detail.setVisibility(View.GONE);

        viewHolder.noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteHistory.class);
                intent.putExtra("cid", data.get(i).getId());
                intent.putExtra("category_name", data.get(i).getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_note_title)
        TextView title;
        @BindView(R.id.list_note_count)
        TextView detail;
        @BindView(R.id.list_note_view)
        LinearLayout noteView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
