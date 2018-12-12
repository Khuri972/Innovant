package com.innovent.erp.dialog;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.innovent.erp.R;

/**
 * Created by CRAFT BOX on 2/27/2018.
 */

public class BottomSheetFragment extends BottomSheetDialogFragment {


    public interface newFolderCreate {

        public void createFolder();

        public void UploadFile();
    }

    public BottomSheetFragment() {
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
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        ImageView folderImg = (ImageView) view.findViewById(R.id.folder_img);
        ImageView uploadImg = (ImageView) view.findViewById(R.id.upload_img);

        folderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    newFolderCreate folderCreate = (newFolderCreate) getActivity();
                    folderCreate.createFolder();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    newFolderCreate upload = (newFolderCreate) getActivity();
                    upload.UploadFile();
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
