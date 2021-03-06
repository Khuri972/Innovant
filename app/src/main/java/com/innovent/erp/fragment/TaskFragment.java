package com.innovent.erp.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.AddTaskActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.TaskActivity;
import com.innovent.erp.adapter.TaskAdapter;
import com.innovent.erp.adapter.TaskDialogAdapter;
import com.innovent.erp.custom.DividerItemDecoration;
import com.innovent.erp.custom.PathUtil;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.employeeManagement.DailySalesReportActivity;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.TaskAttachmentModel;
import com.innovent.erp.model.TaskNoteModel;
import com.innovent.erp.model.TaskModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.service.DownloadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CRAFT BOX on 3/5/2018.
 */

public class TaskFragment extends Fragment implements TaskAdapter.moveTask, TaskDialogAdapter.moveTask {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<TaskModel> data = new ArrayList<>();
    TaskAdapter adapter;
    MyPreferences myPreferences;
    String status = "";

    @BindView(R.id.add_task)
    FloatingActionButton addTask;
    public TaskActivity taskActivity;
    AlertDialog buildInfosDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);
        myPreferences = new MyPreferences(getActivity());
        try {
            Bundle b = getArguments();
            status = b.getString("status");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (myPreferences.getPreferences(MyPreferences.task_insert).equals("1")) {
            addTask.setVisibility(View.VISIBLE);
        } else {
            addTask.setVisibility(View.GONE);
        }

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                    intent.putExtra("status", "" + status);
                    intent.putExtra("type", "0");
                    getActivity().startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        adapter = new TaskAdapter(getActivity(), data, status, this);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        getTaskStatus();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent _data) {
        super.onActivityResult(requestCode, resultCode, _data);
        try {
            if (resultCode == 18) {
                try {
                    TaskModel da = (TaskModel) _data.getSerializableExtra("data");
                    data.add(da);
                    adapter.notifyDataSetChanged();
                    emptyLayout.setVisibility(View.GONE);
                    recycleView.setVisibility(View.VISIBLE);
                    Collections.sort(data, new Comparator<TaskModel>() {
                        @Override
                        public int compare(TaskModel p1, TaskModel p2) {
                            return p2.getTask_priority().compareTo(p1.getTask_priority()); // if you want to short by name
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == 19) {
                try {
                    TaskModel da = (TaskModel) _data.getSerializableExtra("data");
                    int position = _data.getIntExtra("position", 0);
                    data.set(position, da);
                    adapter.notifyDataSetChanged();
                    Collections.sort(data, new Comparator<TaskModel>() {
                        @Override
                        public int compare(TaskModel p1, TaskModel p2) {
                            return p2.getTask_priority().compareTo(p1.getTask_priority()); // if you want to short by name
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == getActivity().RESULT_OK) {
                Uri ur = _data.getData();
                try {
                    String mimeType = getMimeType(ur);
                    String PathHolder = PathUtil.getPath(getActivity(), ur);
                    File file = new File(PathHolder);
                    if (file != null) {
                        adapter.getFilePath(file, mimeType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getActivity().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void getTaskStatus() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            if (!GlobalElements.taskStatus.equals("")) {
                status = GlobalElements.taskStatus;
            }
            Call<ResponseBody> call = request.getTask(myPreferences.getPreferences(MyPreferences.id), status, GlobalElements.taskAssignedById, GlobalElements.taskAssignedTo, GlobalElements.taskTitle, GlobalElements.toDate, GlobalElements.fromeDate, GlobalElements.actionShowHide, GlobalElements.task_type, GlobalElements.admintype);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);

                        data.clear();
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                TaskModel da = new TaskModel();
                                da.setId(c.getString("id"));
                                da.setTask_no(c.getString("task_no"));
                                da.setTask_name(c.getString("task_name"));
                                da.setTask_deadline(c.getString("task_deadline"));
                                da.setTask_description(c.getString("task_description"));
                                da.setTask_type(c.getString("task_type"));
                                da.setTask_color_tag(c.getString("task_color_tag"));
                                da.setTask_assigned_by(c.getString("task_assigned_by"));
                                da.setTask_assigned_by_name(c.getString("task_assigned_by_name"));
                                da.setIsActive(c.getString("isActive"));

                                if (c.getString("task_assigned_by").equals("" + myPreferences.getPreferences(MyPreferences.id)) && !c.getString("task_assigned_to").equals("" + myPreferences.getPreferences(MyPreferences.id))) {
                                    da.setTask_color_tag_slug("#FF0000"); // red
                                } else if (c.getString("task_assigned_to").equals("" + myPreferences.getPreferences(MyPreferences.id)) && !c.getString("task_assigned_by").equals("" + myPreferences.getPreferences(MyPreferences.id))) {
                                    da.setTask_color_tag_slug("#0c08d8"); // blue
                                } else {
                                    da.setTask_color_tag_slug("#197b30"); // green
                                }

                                da.setTask_create_date(c.getString("task_create_date"));
                                da.setTask_priority(c.getString("task_priority"));
                                da.setTask_assigned_to(c.getString("task_assigned_to"));
                                da.setTask_assigned_to_name(c.getString("task_assigned_to_name"));

                                ArrayList<TaskNoteModel> noteModels = new ArrayList<TaskNoteModel>();
                                JSONArray note = c.getJSONArray("note");
                                for (int j = 0; j < note.length(); j++) {
                                    JSONObject n = note.getJSONObject(j);
                                    TaskNoteModel na = new TaskNoteModel();
                                    na.setId(n.getString("id"));
                                    na.setUsername(n.getString("username"));
                                    na.setDescription(n.getString("description"));
                                    na.setCreated_date(n.getString("created_date"));
                                    noteModels.add(na);
                                }
                                da.setNoteModels(noteModels);

                                ArrayList<TaskAttachmentModel> attachmentModels = new ArrayList<TaskAttachmentModel>();
                                da.setAttachment(c.getString("attachment"));
                                if (c.getString("attachment").equals("0")) {
                                    da.setAttachmentModels(attachmentModels);
                                } else {
                                    JSONArray taskAttachment = c.getJSONArray("task_attachment");
                                    for (int j = 0; j < taskAttachment.length(); j++) {
                                        JSONObject n = taskAttachment.getJSONObject(j);
                                        TaskAttachmentModel na = new TaskAttachmentModel();
                                        na.setId(n.getString("id"));
                                        File file = new File(n.getString("file_path"));
                                        na.setFile_name("" + file.getName());
                                        na.setFile_path(n.getString("file_path"));
                                        na.setCreated_date(n.getString("created_date"));
                                        attachmentModels.add(na);
                                    }
                                    da.setAttachmentModels(attachmentModels);
                                }
                                data.add(da);
                            }
                            adapter.notifyDataSetChanged();
                            emptyLayout.setVisibility(View.GONE);
                            recycleView.setVisibility(View.VISIBLE);
                            Collections.sort(data, new Comparator<TaskModel>() {
                                @Override
                                public int compare(TaskModel p1, TaskModel p2) {
                                    return p2.getTask_priority().compareTo(p1.getTask_priority()); // if you want to short by name
                                }
                            });
                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                            emptyText.setText("" + json.getString("ack_msg"));
                            recycleView.setVisibility(View.GONE);
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

    public void MoveTask(TaskModel da) {
        try {
            data.add(da);
            adapter.notifyDataSetChanged();
            Collections.sort(data, new Comparator<TaskModel>() {
                @Override
                public int compare(TaskModel p1, TaskModel p2) {
                    return p2.getTask_priority().compareTo(p1.getTask_priority()); // if you want to short by name
                }
            });

            if (data.size() > 0) {
                recycleView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            } else {
                recycleView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveTask(int position) {
        try {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_move_task, null);
            alertDialog2.setView(dialogView);
            RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.recycleview);

            taskActivity = (TaskActivity) getActivity();
            ArrayList<GeneralModel> _data = new ArrayList<>();
            for (int i = 0; i < taskActivity.data.size(); i++) {
                if (!status.equals("" + taskActivity.data.get(i).getId())) {
                    GeneralModel da = new GeneralModel();
                    da.setId(taskActivity.data.get(i).getId());
                    da.setName(taskActivity.data.get(i).getName());
                    _data.add(da);
                }
            }
            TaskDialogAdapter adapter = new TaskDialogAdapter(getActivity(), _data, status, this, position);
            recyclerView.setAdapter(adapter);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity());
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            buildInfosDialog = alertDialog2.create();
            buildInfosDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveTaskChangeStage(String taskId, int taskPosition) {
        try {
            deleteTask(taskId, data.get(taskPosition).getId(), taskPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(final String taskId, String tid, final int taskPosition) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call;

        call = req.archiveTask(myPreferences.getPreferences(MyPreferences.id), tid, status, "" + taskId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        taskActivity = (TaskActivity) getActivity();
                        int vi = 0;
                        for (int i = 0; i < taskActivity.data.size(); i++) {
                            if (taskId.equals("" + taskActivity.data.get(i).getId())) {
                                vi = i;
                                break;
                            }
                        }
                        Fragment viewPagerFragment = (Fragment) taskActivity.viewPager.getAdapter().instantiateItem(taskActivity.viewPager, vi);
                        if (viewPagerFragment != null && viewPagerFragment.isAdded()) {
                            if (viewPagerFragment instanceof TaskFragment) {
                                TaskFragment oneFragment = (TaskFragment) viewPagerFragment;
                                if (oneFragment != null) {
                                    oneFragment.MoveTask(data.get(taskPosition));
                                }
                            }
                        }
                        data.remove(taskPosition);
                        adapter.notifyItemRemoved(taskPosition);
                        buildInfosDialog.dismiss();
                    } else {
                        Toaster.show(getActivity(), "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
