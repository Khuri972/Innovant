package com.innovent.erp.dialog;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.innovent.erp.DocumentFilterAdapter;
import com.innovent.erp.R;
import com.innovent.erp.custom.DividerItemDecoration;
import com.innovent.erp.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 2/27/2018.
 */

public class DocumentFilterBottomSheetFragment extends BottomSheetDialogFragment {

    ArrayList<GeneralModel> data=new ArrayList<>();
    String sort="0";
    public DocumentFilterBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_move_task, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        try {
            Bundle b=getArguments();
            sort = b.getString("sort");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.clear();
        GeneralModel da=new GeneralModel();
        da.setId("0");
        da.setName("Short By");
        data.add(da);
        da=new GeneralModel();
        da.setId("1");
        da.setName("Name A-Z");
        data.add(da);
        da=new GeneralModel();
        da.setId("2");
        da.setName("Name Z-A");
        data.add(da);
        da=new GeneralModel();
        da.setId("3");
        da.setName("Size Ascending");
        data.add(da);
        da=new GeneralModel();
        da.setId("4");
        da.setName("Size Descending");
        data.add(da);
        da=new GeneralModel();
        da.setId("5");
        da.setName("Date Ascending");
        data.add(da);
        da=new GeneralModel();
        da.setId("6");
        da.setName("Date Descending");
        data.add(da);

        DocumentFilterAdapter adapter = new DocumentFilterAdapter(getActivity(), data,sort);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        return view;
    }
}
