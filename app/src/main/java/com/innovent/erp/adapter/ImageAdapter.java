package com.innovent.erp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.R;
import com.innovent.erp.custom.TouchImageView;
import com.innovent.erp.model.ViewpagerModel;
import com.innovent.erp.service.DownloadService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by CRAFT BOX on 1/8/2018.
 */

public class ImageAdapter extends PagerAdapter {
    ArrayList<ViewpagerModel> data;
    Context context;
    LayoutInflater mLayoutInflater;
    Fragment fragment;
    public interface NextIntercommunitor {
        public void MoveNext(int i);
    }

    public interface PreviousIntercommunitor {
        public void MovePrevious(int i);
    }

    public interface SkipIntercommunitor {
        public void SkipPrevious(int i);
    }

    public ImageAdapter(Context Context, ArrayList<ViewpagerModel> da,Fragment fragment) {
        this.context = Context;
        data = da;
        this.fragment=fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View ssView, Object ssObject) {
        return ssView == ((LinearLayout) ssObject);
    }

    @Override
    public Object instantiateItem(ViewGroup ssContainer, final int ssPosition) {

        mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.list_viewpage, ssContainer, false);

        TextView title = (TextView) itemView.findViewById(R.id.title);
        TextView file_download = (TextView) itemView.findViewById(R.id.file_download);
        TextView desc = (TextView) itemView.findViewById(R.id.description);
        TextView next = (TextView) itemView.findViewById(R.id.next);
        TextView skip = (TextView) itemView.findViewById(R.id.skip);
        TextView previous = (TextView) itemView.findViewById(R.id.previous);
        TouchImageView img = (TouchImageView) itemView.findViewById(R.id.view_img);
        ImageView file_download_img = (ImageView) itemView.findViewById(R.id.file_download_img);
        title.setText("" + data.get(ssPosition).getTitle());

        desc.setMovementMethod(new ScrollingMovementMethod());
        desc.setText("" + data.get(ssPosition).getDescription());

        if (data.get(ssPosition).getPdf_file().equals("")) {
            file_download.setVisibility(View.GONE);
        } else {
            file_download.setVisibility(View.VISIBLE);
            File folder = new File(data.get(ssPosition).getPdf_file());
            String file_name = folder.getName();
            file_download.setText("" + file_name);
        }

        file_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra("file_url", data.get(ssPosition).getPdf_file());
                    context.startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        file_download_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra("file_url", data.get(ssPosition).getPdf_file());
                    context.startService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    NextIntercommunitor intercommunitor = (NextIntercommunitor) fragment;
                    intercommunitor.MoveNext(ssPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkipIntercommunitor intercommunitor = (SkipIntercommunitor) fragment;
                intercommunitor.SkipPrevious(ssPosition);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousIntercommunitor intercommunitor = (PreviousIntercommunitor) fragment;
                intercommunitor.MovePrevious(ssPosition);
            }
        });

        if (data.get(ssPosition).getImage_path().equals("")) {
            img.setVisibility(View.GONE);
        } else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options;
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            imageLoader.displayImage(data.get(ssPosition).getImage_path(), img, options);
            img.setVisibility(View.VISIBLE);
        }

        ssContainer.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup ssContainer, int ssPosition,
                            Object ssObject) {
        ((ViewPager) ssContainer).removeView((LinearLayout) ssObject);
    }

}
