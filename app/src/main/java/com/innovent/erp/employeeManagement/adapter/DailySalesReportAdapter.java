package com.innovent.erp.employeeManagement.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.employeeManagement.AddDailyWorkReport;
import com.innovent.erp.employeeManagement.AddSalesDailyReport;
import com.innovent.erp.employeeManagement.DailyWorkReportActivity;
import com.innovent.erp.employeeManagement.interFace.DeleteDailyReport;
import com.innovent.erp.employeeManagement.model.DailyFileAttachModel;
import com.innovent.erp.employeeManagement.model.DailyNoteModel;
import com.innovent.erp.employeeManagement.model.DailySalesReportModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.service.DownloadService;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CRAFT BOX on 11/9/2016.
 */

public class DailySalesReportAdapter extends RecyclerView.Adapter<DailySalesReportAdapter.ViewHolder> {

    private ArrayList<DailySalesReportModel> data = new ArrayList<>();
    private Context context;
    MyPreferences myPreferences;
    String status;
    AlertDialog buildInfosDialog;
    int position = 0;

    public DailySalesReportAdapter(Context context, ArrayList<DailySalesReportModel> data, String status) {
        this.data = data;
        this.context = context;
        this.status = status;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_daily_sales_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        try {
            viewHolder.listCompanyName.setText("C Name : " + data.get(i).getCompany_name());
            viewHolder.listDailyAddress.setText(data.get(i).getAddress());
            viewHolder.listPersonName.setText("P Name : " + data.get(i).getPerson_name());
            viewHolder.createdDate.setText("Date : " + data.get(i).getDate());
            viewHolder.listDailyDiscuss.setText("Remark : " + data.get(i).getDiscuss());

            try {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (data.get(i).getDailyNoteModels().size() > 0) {
                    viewHolder.noteLayout.removeAllViews();
                    for (int j = 0; j < data.get(i).getDailyNoteModels().size(); j++) {
                        final View addView = layoutInflater.inflate(R.layout.list_sales_report_note, null);
                        TextView description = (TextView) addView.findViewById(R.id.description);
                        TextView created_date = (TextView) addView.findViewById(R.id.created_date);
                        description.setText("" + data.get(i).getDailyNoteModels().get(j).getNote());
                        created_date.setText("" + data.get(i).getDailyNoteModels().get(j).getDate());
                        viewHolder.noteLayout.addView(addView);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (data.get(i).getDailyFileAttachModels().size() > 0) {
                    viewHolder.attachmentLayout.removeAllViews();
                    for (int j = 0; j < data.get(i).getDailyFileAttachModels().size(); j++) {
                        final View addView = layoutInflater.inflate(R.layout.list_sales_report_attachment, null);
                        TextView description = (TextView) addView.findViewById(R.id.description);
                        final ImageView img = (ImageView) addView.findViewById(R.id.attachment_img);
                        TextView created_date = (TextView) addView.findViewById(R.id.created_date);
                        description.setText("" + data.get(i).getDailyFileAttachModels().get(j).getFile_name());
                        created_date.setText("" + data.get(i).getDailyFileAttachModels().get(j).getDate());
                        img.setTag("" + j);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = new Intent(context, DownloadService.class);
                                    int a = 0;
                                    a = Integer.parseInt("" + img.getTag());
                                    intent.putExtra("file_url", "" + data.get(i).getDailyFileAttachModels().get(a).getFile_path());
                                    context.startService(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        viewHolder.attachmentLayout.addView(addView);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.taskMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        PopupMenu popupMenu = new PopupMenu(context, view);
                        Menu m = popupMenu.getMenu();
                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.popup_menu_daily_report, popupMenu.getMenu());


                        /*m.removeItem(R.id.action_delete);
                        if (!myPreferences.getPreferences(MyPreferences.task_delete).equals("1")) {
                            m.removeItem(R.id.action_delete);
                        }*/

                        if (data.get(i).getIsSubmitted().equals("1")) {
                            m.removeItem(R.id.action_delete);
                            m.removeItem(R.id.action_update);
                        }


                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.action_delete:
                                        try {
                                            AlertDialog buildInfosDialog;
                                            AlertDialog.Builder alertDialog2;
                                            alertDialog2 = new AlertDialog.Builder(context);
                                            alertDialog2.setTitle("Are you sure want to delete");

                                            alertDialog2.setPositiveButton("Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Write your code here to execute after dialog
                                                            //new All().execute("clear_wishlist");
                                                            if (GlobalElements.isConnectingToInternet(context)) {
                                                                deleteTask(data.get(i).getId(), i);
                                                            } else {
                                                                GlobalElements.showDialog(context);
                                                            }
                                                        }
                                                    });

                                            alertDialog2.setNegativeButton("NO",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Write your code here to execute after dialog

                                                        }
                                                    });

                                            buildInfosDialog = alertDialog2.create();
                                            buildInfosDialog.show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case R.id.action_update:
                                        Intent intent = null;
                                        try {
                                            intent = new Intent(context, AddSalesDailyReport.class);
                                            intent.putExtra("type", "1");
                                            intent.putExtra("position", i);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("data", data.get(i));
                                            intent.putExtras(bundle);
                                            ((Activity) context).startActivityForResult(intent, 0);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case R.id.action_attachment:
                                        try {
                                            try {
                                                position = i;
                                                intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                                intent.setType("*/*");
                                                ((Activity) context).startActivityForResult(intent, 7);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case R.id.action_note:
                                        try {
                                            AlertDialog.Builder alertDialog2;
                                            alertDialog2 = new AlertDialog.Builder(context);

                                            LayoutInflater inflater = LayoutInflater.from(context);
                                            final View dialogView = inflater.inflate(R.layout.dialog_note, null);
                                            alertDialog2.setView(dialogView);

                                            ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelbtn);
                                            final EditText noteEdt = (EditText) dialogView.findViewById(R.id.note_edt);
                                            Button close = (Button) dialogView.findViewById(R.id.close);
                                            Button create_note = (Button) dialogView.findViewById(R.id.create_note);

                                            cancelImg.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    buildInfosDialog.dismiss();
                                                }
                                            });
                                            close.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    buildInfosDialog.dismiss();
                                                }
                                            });

                                            noteEdt.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                    try {
                                                        noteEdt.setError(null);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void afterTextChanged(Editable editable) {

                                                }
                                            });

                                            create_note.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (noteEdt.getText().toString().equals("")) {
                                                        noteEdt.setError("This is a required field");
                                                    } else {
                                                        if (GlobalElements.isConnectingToInternet(context)) {
                                                            /*DailyNoteModel na = new DailyNoteModel();
                                                            na.setId("" + "");
                                                            na.setNote("" + "addsd ads dasd");
                                                            na.setDate("asdsad");
                                                            data.get(i).getDailyNoteModels().add(na);
                                                            notifyItemChanged(i);*/
                                                            createNote(data.get(i).getId(), i, noteEdt.getText().toString());
                                                        } else {
                                                            GlobalElements.showDialog(context);
                                                        }
                                                        buildInfosDialog.dismiss();
                                                    }
                                                }
                                            });
                                            buildInfosDialog = alertDialog2.create();
                                            buildInfosDialog.show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_company_name)
        TextView listCompanyName;
        @BindView(R.id.task_more)
        ImageView taskMore;
        @BindView(R.id.list_person_name)
        TextView listPersonName;
        @BindView(R.id.list_daily_address)
        TextView listDailyAddress;
        @BindView(R.id.list_daily_discuss)
        TextView listDailyDiscuss;
        @BindView(R.id.note_layout)
        LinearLayout noteLayout;
        @BindView(R.id.attachment_layout)
        LinearLayout attachmentLayout;
        @BindView(R.id.created_date)
        TextView createdDate;
        @BindView(R.id.list_task_main)
        LinearLayout listTaskMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public void getFilePath(File file, String mimeType) {
        try {
            createAttachment(file, mimeType, data.get(position).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNote(String sid, final int position, String note) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call;
        call = req.createSalesReportNote(myPreferences.getPreferences(MyPreferences.id), sid, "" + note);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        DailyNoteModel na = new DailyNoteModel();
                        na.setId("" + result.getString("id"));
                        na.setNote("" + result.getString("note"));
                        na.setDate(result.getString("created_date"));
                        data.get(position).getDailyNoteModels().add(na);
                        notifyItemChanged(position);
                    } else {
                        Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void createAttachment(final File file, String mimeType, String sid) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
        RequestBody requestfile_image;
        MultipartBody.Part body_image;
        Call<ResponseBody> call;

        requestfile_image = RequestBody.create(MediaType.parse("image/*"), file);
        body_image = MultipartBody.Part.createFormData("file_path", file.getName(), requestfile_image);
        call = request.createSalesReportAttachment(myPreferences.getPreferences(MyPreferences.id), "" + sid, body_image);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        DailyFileAttachModel na = new DailyFileAttachModel();
                        na.setId("" + result.getString("id"));
                        na.setFile_path("" + result.getString("file_path"));
                        if (result.getString("file_path").equals("")) {
                            na.setFile_name("");
                        } else {
                            File file1 = new File(result.getString("file_path"));
                            na.setFile_name("" + file1.getName());
                        }
                        na.setDate("" + result.getString("created_date"));
                        data.get(position).getDailyFileAttachModels().add(na);
                        notifyItemChanged(position);
                    } else {
                        Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    private void deleteTask(String report_id, final int position) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.deleteDailyReport(myPreferences.getPreferences(MyPreferences.id), report_id, "sales");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        data.remove(position);
                        notifyDataSetChanged();
                        if (data.isEmpty()) {
                            try {
                                DeleteDailyReport dailyReport = (DeleteDailyReport) context;
                                dailyReport.callbackCall();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
            }

        });
    }

}
