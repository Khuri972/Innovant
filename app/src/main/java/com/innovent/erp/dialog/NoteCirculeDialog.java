package com.innovent.erp.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.adapter.ImageAdapter;
import com.innovent.erp.custom.WrapContentViewPager;
import com.innovent.erp.model.ViewpagerModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 9/26/2016.
 */
public class NoteCirculeDialog extends DialogFragment implements ImageAdapter.NextIntercommunitor, ImageAdapter.SkipIntercommunitor, ImageAdapter.PreviousIntercommunitor {

    Context context;
    Dialog d;
    ImageAdapter adapter;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static NoteCirculeDialog newInstance() {
        NoteCirculeDialog f = new NoteCirculeDialog();
        Bundle args = new Bundle();
        //args.putSerializable("data", viewpagerModels);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        /*this.uid = getArguments().getString("uid");
        this.pid = getArguments().getString("pid");
        this.pos = getArguments().getInt("pos");
        this.model = (ArrayList<ProductModel>) getArguments().getSerializable("data");*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_notice_circule, container, false);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        ButterKnife.bind(this, v);

        adapter = new ImageAdapter(getActivity(), GlobalElements.viewpagerModels,this);
        viewPager.setAdapter(adapter);
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_notice_circule, null);
        d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(root);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return d;
    }

    @Override
    public void MoveNext(int i) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @Override
    public void MovePrevious(int i) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @Override
    public void SkipPrevious(int i) {
        dismiss();
    }
}
