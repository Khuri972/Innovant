package com.innovent.erp.adapter;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.CourierActivity;
import com.innovent.erp.DocumentActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.custom.MultiSelectionSpinner;
import com.innovent.erp.custom.MultiSelectionSpinnerEmployee;
import com.innovent.erp.custom.MultiSelectionSpinnerPeople;
import com.innovent.erp.custom.MultiSelectionSpinnerTag;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.DashboardModel;
import com.innovent.erp.model.DocumentModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.service.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CRAFT BOX on 10/10/2016.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> implements MultiSelectionSpinnerPeople.OnMultipleItemsSelectedListenerOption {

    private ArrayList<DocumentModel> data = new ArrayList<>();
    private ArrayList<GeneralModel> generalModels = new ArrayList<>();
    private ArrayList<String> peopleData = new ArrayList<>();
    private Context context;
    MyPreferences myPreferences;
    StringBuilder builder = new StringBuilder();
    String peopleIds = "", resourceId = "", visibility = "";
    int toggelSwitch = 0;

    public interface clickFolder{
        public void clickFolder(int position);
    }
    public DocumentAdapter(Context context, ArrayList<DocumentModel> data) {
        this.data = data;
        this.context = context;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_document, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.name.setText("" + data.get(i).getName());

        if (data.get(i).getType().equals("1")) {
            viewHolder.documentImg.setVisibility(View.GONE);
            viewHolder.documentMore.setVisibility(View.VISIBLE);
        } else {
            viewHolder.documentImg.setVisibility(View.VISIBLE);
            viewHolder.documentMore.setVisibility(View.GONE);
        }

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (data.get(i).getType().equals("0")) {
                        clickFolder folder=(clickFolder)context;
                        folder.clickFolder(i);
                        /*Intent intent = new Intent(context, DocumentActivity.class);
                        intent.putExtra("name", "" + data.get(i).getName());
                        intent.putExtra("id", "" + data.get(i).getId());
                        context.startActivity(intent);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.documentMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PopupMenu popupMenu;
                    popupMenu = new PopupMenu(context, view);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_share_resource:
                                    if (GlobalElements.isConnectingToInternet(context)) {
                                        toggelSwitch = 0;
                                        resourceId = data.get(i).getId();
                                        visibility = "";
                                        getSharableUrl();
                                    } else {
                                        GlobalElements.showDialog(context);
                                    }
                                    break;
                                case R.id.action_get_sharable:
                                    if (GlobalElements.isConnectingToInternet(context)) {
                                        toggelSwitch = 0;
                                        resourceId = data.get(i).getId();
                                        visibility = data.get(i).getVisibility();
                                        getSharableUrl();
                                    } else {
                                        GlobalElements.showDialog(context);
                                    }
                                    break;
                                case R.id.action_download:
                                    try {
                                        if (GlobalElements.isConnectingToInternet(context)) {
                                            Intent intent = new Intent(context, DownloadService.class);
                                            intent.putExtra("file_url", data.get(i).getResource_media_url());
                                            intent.putExtra("file_name", data.get(i).getName());
                                            context.startService(intent);
                                        } else {
                                            GlobalElements.showDialog(context);
                                        }
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void selectedIndicesOption(List<Integer> indices) {

    }

    @Override
    public void selectedStringsOption(List<String> strings) {
        peopleIds = "";
        builder.setLength(0);
        for (int i = 0; i < generalModels.size(); i++) {
            String name = generalModels.get(i).getName();
            for (int j = 0; j < strings.size(); j++) {
                if (name.equals("" + strings.get(j).toString())) {
                    if (!name.equals("Select People")) {
                        builder.append(generalModels.get(i).getId()).append(",");
                    }
                }
            }
        }
        peopleIds = "" + GlobalElements.getRemoveLastComma(builder.toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_document_name)
        TextView name;
        @BindView(R.id.list_document_img)
        ImageView documentImg;
        @BindView(R.id.list_document_more)
        ImageView documentMore;
        @BindView(R.id.list_document_main)
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void getSharableUrl() {
        try {
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call;

            if (visibility.equals("public") && toggelSwitch == 0) {
                call = request.getResourceVisibilityPublic(myPreferences.getPreferences(MyPreferences.id), resourceId);
            } else if (visibility.equals("private") && toggelSwitch == 0) {
                call = request.getResourceVisibilityPrivate(myPreferences.getPreferences(MyPreferences.id), resourceId);
            } else {
                call = request.getPerson(myPreferences.getPreferences(MyPreferences.id), resourceId);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        if (visibility.equals("public") && toggelSwitch == 0 || visibility.equals("private") && toggelSwitch == 0) {
                            JSONObject json = new JSONObject(json_response);
                            if (json.getInt("ack") == 1) {
                                String shared_url = json.getString("shared_url");
                                getSharableDialog(shared_url);
                            } else {
                                Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                            }
                        } else {
                            if (toggelSwitch == 1) {
                                getSharableDialog(json_response);
                            } else {
                                getSharableResourceDialog(json_response);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareResourcePrivate() {
        try {
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call;

            call = request.shareResourcePrivate(myPreferences.getPreferences(MyPreferences.id), resourceId,peopleIds);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.SUCCESS);
                        } else {
                            Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSharableDialog(final String url) {
        try {

            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View menuLayout = inflater.inflate(R.layout.dialog_sharable_url, null);
            final AlertDialog.Builder dg = new AlertDialog.Builder(context);
            dg.setView(menuLayout);
            final AlertDialog dialog = dg.create();

            final TextView copyLinkTxt = (TextView) menuLayout.findViewById(R.id.copyLinkTxt);
            final TextView text_title = (TextView) menuLayout.findViewById(R.id.text_title);
            ToggleSwitch switch_toggle = (ToggleSwitch) menuLayout.findViewById(R.id.switch_toggle);
            final MultiSelectionSpinnerPeople peopleSipnner = (MultiSelectionSpinnerPeople) menuLayout.findViewById(R.id.spinner_people);

            Button copyLinkBtn = (Button) menuLayout.findViewById(R.id.copyLinkBtn);
            Button close = (Button) menuLayout.findViewById(R.id.close);
            switch_toggle.setCheckedTogglePosition(toggelSwitch);

            if (switch_toggle.getCheckedTogglePosition() == 0) {
                text_title.setText("Anyone with link can access resource");
                copyLinkBtn.setText("Copy Link");
                peopleSipnner.setVisibility(View.GONE);
                copyLinkTxt.setVisibility(View.VISIBLE);
            } else {
                text_title.setText("People");
                copyLinkBtn.setText("Share");
                peopleSipnner.setVisibility(View.VISIBLE);
                copyLinkTxt.setVisibility(View.GONE);
            }

            if (toggelSwitch == 0) {
                copyLinkTxt.setText("" + url);
            } else {
                try {
                    JSONObject json = new JSONObject(url);
                    generalModels.clear();
                    peopleData.clear();
                    GeneralModel da = new GeneralModel();
                    da.setId("");
                    da.setName("Select People");
                    generalModels.add(da);
                    peopleData.add("Select People");
                    if (json.getInt("ack") == 1) {
                        try {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                generalModels.add(da);
                                peopleData.add(c.getString("name"));
                            }
                            peopleSipnner.setItems(peopleData);
                            peopleSipnner.setListener(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            switch_toggle.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
                @Override
                public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                    if (position == 0 && isChecked) {
                        toggelSwitch = 0;
                        dialog.dismiss();
                        getSharableUrl();
                    } else {
                        toggelSwitch = 1;
                        dialog.dismiss();
                        getSharableUrl();
                    }
                }
            });

            copyLinkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toggelSwitch == 0) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", "" + url);
                        clipboard.setPrimaryClip(clip);
                        Toaster.show(context, "Text copied", false, Toaster.DANGER);
                    } else {
                        if (GlobalElements.isConnectingToInternet(context)) {
                            shareResourcePrivate();
                        } else {
                            GlobalElements.showDialog(context);
                        }
                    }
                    dialog.dismiss();
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSharableResourceDialog(String json_response) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.dialog_share_resource, null);
        final AlertDialog.Builder dg = new AlertDialog.Builder(context);
        dg.setView(menuLayout);
        final AlertDialog dialog = dg.create();

        MultiSelectionSpinnerPeople peopleSipnner = (MultiSelectionSpinnerPeople) menuLayout.findViewById(R.id.spinner_people);

        try {
            JSONObject json = new JSONObject(json_response);
            generalModels.clear();
            peopleData.clear();
            GeneralModel da = new GeneralModel();
            da.setId("");
            da.setName("Select People");
            generalModels.add(da);
            peopleData.add("Select People");
            if (json.getInt("ack") == 1) {
                try {
                    JSONArray result = json.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        da = new GeneralModel();
                        da.setId("" + c.getString("id"));
                        da.setName("" + c.getString("name"));
                        generalModels.add(da);
                        peopleData.add(c.getString("name"));
                    }
                    peopleSipnner.setItems(peopleData);
                    peopleSipnner.setListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toaster.show(context, "" + json.getString("ack_msg"), false, Toaster.DANGER);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button close = (Button) menuLayout.findViewById(R.id.close);
        Button submit = (Button) menuLayout.findViewById(R.id.share);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalElements.isConnectingToInternet(context)) {
                    shareResourcePrivate();
                } else {
                    GlobalElements.showDialog(context);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
