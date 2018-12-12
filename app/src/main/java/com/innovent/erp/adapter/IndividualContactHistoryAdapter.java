package com.innovent.erp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.CompanyContactDetail;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.IndividualContactDetail;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.CompanyContactHistoryModel;
import com.innovent.erp.model.IndividualContactHistoryModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class IndividualContactHistoryAdapter extends RecyclerView.Adapter<IndividualContactHistoryAdapter.ViewHolder> {
    private ArrayList<IndividualContactHistoryModel> data;
    private Context context;
    MyPreferences myPreferences;
    AlertDialog buildInfosDialog;

    public IndividualContactHistoryAdapter(Context context, ArrayList<IndividualContactHistoryModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_history, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(data.get(i).getPerson_name());

        StringBuilder address = new StringBuilder();

        if (!data.get(i).getPerson_office_address().equals("")) {
            address.append("" + data.get(i).getPerson_office_address()).append(",");
        }
        if (!data.get(i).getPerson_office_area().equals("")) {
            address.append("" + data.get(i).getPerson_office_area()).append(",");
        }
        if (!data.get(i).getPerson_office_city_name().equals("")) {
            address.append("" + data.get(i).getPerson_office_city_name()).append(",");
        }
        if (!data.get(i).getPerson_office_district_name().equals("")) {
            address.append("" + data.get(i).getPerson_office_district_name()).append(",");
        }
        if (!data.get(i).getPerson_office_state_name().equals("")) {
            address.append("" + data.get(i).getPerson_office_state_name()).append(",");
        }
        if (!data.get(i).getPerson_office_country_name().equals("")) {
            address.append("" + data.get(i).getPerson_office_country_name()).append(",");
        }
        if (!data.get(i).getPerson_office_pincode().equals("")) {
            address.append("" + data.get(i).getPerson_office_pincode()).append(",");
        }

        viewHolder.address.setText("" + GlobalElements.getRemoveLastComma("" + address.toString()));
        viewHolder.mobile.setText("" + data.get(i).getPerson_mobile());
        viewHolder.email.setText("" + data.get(i).getPerson_email());

        if (data.get(i).getPerson_mobile().equals("")) {
            viewHolder.callImg.setVisibility(View.GONE);
        } else {
            viewHolder.callImg.setVisibility(View.VISIBLE);
        }

        if (data.get(i).getImage_path().equals("")) {
            viewHolder.img.setImageResource(R.drawable.default_logo_contact);
        } else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options;
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            imageLoader.displayImage(data.get(i).getImage_path(), viewHolder.img, options);
        }

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, IndividualContactDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data.get(i));
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.callImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.get(i).getPerson_mobile().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + data.get(i).getPerson_mobile()));
                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(intent);
                }
            }
        });

        viewHolder.shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareContact(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_name)
        TextView name;
        @BindView(R.id.contact_img)
        ImageView img;
        @BindView(R.id.contact_mobile)
        TextView mobile;
        @BindView(R.id.contact_address)
        TextView address;
        @BindView(R.id.contact_email)
        TextView email;
        @BindView(R.id.main_layout)
        LinearLayout mainLayout;

        @BindView(R.id.share_layout)
        LinearLayout shareLayout;
        @BindView(R.id.share_img)
        ImageView shareImg;
        @BindView(R.id.call_img)
        ImageView callImg;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void shareContact(final int position) {
        try {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.dialog_share_contact, null);
            alertDialog2.setView(dialogView);

            Button textShare = (Button) dialogView.findViewById(R.id.shareText);
            Button vcfShare = (Button) dialogView.findViewById(R.id.shareVcf);
            ImageView cancelbtn = (ImageView) dialogView.findViewById(R.id.cancelbtn);

            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buildInfosDialog.dismiss();
                }
            });

            textShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data.get(position).getPerson_mobile().equals("")) {
                        Toaster.show(context, "Mobile number is empty", false, Toaster.DANGER);
                    } else if (data.get(position).getPerson_name().equals("")) {
                        Toaster.show(context, "Company name is empty", false, Toaster.DANGER);
                    } else {
                        try {
                            buildInfosDialog.dismiss();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                            StringBuilder address = new StringBuilder();
                            if (!data.get(position).getPerson_office_address().equals("")) {
                                address.append("" + data.get(position).getPerson_office_address()).append(",");
                            }
                            if (!data.get(position).getPerson_office_area().equals("")) {
                                address.append("" + data.get(position).getPerson_office_area()).append(",");
                            }
                            if (!data.get(position).getPerson_office_city_name().equals("")) {
                                address.append("" + data.get(position).getPerson_office_city_name()).append(",");
                            }
                            if (!data.get(position).getPerson_office_district_name().equals("")) {
                                address.append("" + data.get(position).getPerson_office_district_name()).append(",");
                            }
                            if (!data.get(position).getPerson_office_state_name().equals("")) {
                                address.append("" + data.get(position).getPerson_office_state_name()).append(",");
                            }
                            if (!data.get(position).getPerson_office_country_name().equals("")) {
                                address.append("" + data.get(position).getPerson_office_country_name()).append(",");
                            }
                            if (!data.get(position).getPerson_office_pincode().equals("")) {
                                address.append("" + data.get(position).getPerson_office_pincode()).append(",");
                            }
                            share.putExtra(Intent.EXTRA_TEXT, "Name : " + data.get(position).getPerson_name() + "\nMobile : " + data.get(position).getPerson_mobile() + "\nEmail : " + data.get(position).getPerson_email() + "\naddress : " + GlobalElements.getRemoveLastComma("" + address.toString()));
                            context.startActivity(Intent.createChooser(share, "Share link!"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            vcfShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (data.get(position).getPerson_mobile().equals("")) {
                            Toaster.show(context, "Mobile number is empty", false, Toaster.DANGER);
                        } else if (data.get(position).getPerson_name().equals("")) {
                            Toaster.show(context, "name is empty", false, Toaster.DANGER);
                        } else {
                            buildInfosDialog.dismiss();
                            File vcfFile;
                            //String ph="9979045113",name="hardip";
                            File vdfdirectory = new File(
                                    Environment.getExternalStorageDirectory() + GlobalElements.directory);
                            // have the object build the directory structure, if needed.
                            if (!vdfdirectory.exists()) {
                                vdfdirectory.mkdirs();
                            }

                            vcfFile = new File(vdfdirectory, "company_contact.vcf");
                            FileWriter fw = null;
                            fw = new FileWriter(vcfFile);
                            fw.write("BEGIN:VCARD\r\n");
                            fw.write("VERSION:3.0\r\n");
                            // fw.write("N:" + p.getSurname() + ";" + p.getFirstName() + "\r\n");
                            fw.write("FN:" + data.get(position).getPerson_name() + "\r\n");
                            //  fw.write("ORG:" + p.getCompanyName() + "\r\n");
                            //  fw.write("TITLE:" + p.getTitle() + "\r\n");
                            fw.write("TEL;TYPE=WORK,VOICE:" + data.get(position).getPerson_mobile() + "\r\n");
                            //   fw.write("TEL;TYPE=HOME,VOICE:" + p.getHomePhone() + "\r\n");
                            //   fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
                            //fw.write("EMAIL;TYPE=PREF,INTERNET:" + etmail.getText().toString() + "\r\n");
                            fw.write("END:VCARD\r\n");
                            fw.close();

                            Uri contentUri = null;
                            if (GlobalElements.getVersionCheck()) {
                                contentUri = FileProvider.getUriForFile(context, "" + GlobalElements.fileprovider_path, vcfFile);
                            } else {
                                contentUri = Uri.fromFile(vcfFile);
                            }

                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_SEND);
                            i.setType("text/x-vcard");
                            i.putExtra(Intent.EXTRA_STREAM, contentUri);
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //startActivity(i);
                            context.startActivity(Intent.createChooser(i, ""));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            buildInfosDialog = alertDialog2.create();
            buildInfosDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
