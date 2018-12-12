package com.innovent.erp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.innovent.erp.adapter.GeneralAdapter;
import com.innovent.erp.adapter.TaskLogAdapter;
import com.innovent.erp.custom.CustomDatePicker;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.model.TaskAttachmentModel;
import com.innovent.erp.model.TaskLogModel;
import com.innovent.erp.model.TaskModel;
import com.innovent.erp.model.TaskNoteModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.service.DownloadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.input_taskname)
    EditText inputTaskName;
    @BindView(R.id.input_layout_taskname)
    TextInputLayout inputLayoutTaskname;
    @BindView(R.id.spinner_task_type)
    MaterialSpinner spinnerTaskType;
    @BindView(R.id.spinner_task_assigned)
    MaterialSpinner spinnerTaskAssigned;
    @BindView(R.id.spinner_task_to)
    MaterialSpinner spinnerTaskTo;
    @BindView(R.id.input_taskdesc)
    EditText inputTaskDesc;
    @BindView(R.id.input_layout_taskdesc)
    TextInputLayout inputLayoutTaskdesc;
    @BindView(R.id.spinner_task_color)
    MaterialSpinner spinnerTaskColor;
    @BindView(R.id.input_taskdeadline)
    EditText inputTaskDeadline;
    @BindView(R.id.input_layout_taskdeadline)
    TextInputLayout inputLayoutTaskdeadline;

    ArrayList<GeneralModel> taskTypeModels = new ArrayList<>();
    ArrayList<GeneralModel> taskAssignedModels = new ArrayList<>();
    ArrayList<GeneralModel> taskToModels = new ArrayList<>();
    ArrayList<GeneralModel> taskColorModels = new ArrayList<>();

    GeneralAdapter taskAdapter;
    GeneralAdapter taskAssignedAdapter;
    GeneralAdapter taskToAdapter;
    GeneralAdapter taskColorAdapter;
    MyPreferences myPreferences;
    CustomDatePicker customerDatePicker;
    String taskTypeId = "", taskAssignedId = "", taskToId = "", taskColorId = "", status;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    TaskModel taskModel = new TaskModel();
    @BindView(R.id.task_title)
    TextView taskTitle;
    @BindView(R.id.input_taskno)
    EditText inputTaskno;
    @BindView(R.id.input_layout_taskNo)
    TextInputLayout inputLayoutTaskNo;
    @BindView(R.id.input_taskdate)
    EditText inputTaskdate;
    @BindView(R.id.input_layout_taskdate)
    TextInputLayout inputLayoutTaskdate;
    String insert_update_type = "0";
    int position = 0;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    @BindView(R.id.attachment_layout)
    LinearLayout attachmentLayout;
    @BindView(R.id.attachment_card_view)
    CardView attachmentCardView;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.log_card_view)
    CardView logCardView;

    ArrayList<TaskLogModel> logModels = new ArrayList<>();
    TaskLogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(AddTaskActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycleview.setNestedScrollingEnabled(false);
        try {
            GlobalElements.editTextAllCaps(AddTaskActivity.this, mainLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent = getIntent();
            status = intent.getStringExtra("status");
            insert_update_type = intent.getStringExtra("type");

            if (insert_update_type.equals("0")) {
                inputLayoutTaskNo.setVisibility(View.GONE);
                inputLayoutTaskdate.setVisibility(View.GONE);
            } else if (insert_update_type.equals("1")) {
                Bundle bundle = getIntent().getExtras();
                taskModel = (TaskModel) bundle.getSerializable("data");
                position = bundle.getInt("position");
                getSupportActionBar().setTitle("Update");
                taskTitle.setText("Update");
                inputLayoutTaskNo.setVisibility(View.VISIBLE);
                inputLayoutTaskdate.setVisibility(View.VISIBLE);

                inputTaskno.setText("" + taskModel.getTask_no());
                inputTaskdate.setText("" + taskModel.getTask_create_date());
                inputTaskName.setText("" + taskModel.getTask_name());
                inputTaskDesc.setText("" + taskModel.getTask_description());
                inputTaskDeadline.setText("" + taskModel.getTask_deadline());
                try {
                    if (!taskModel.getTask_priority().equals("")) {
                        ratingBar.setRating(Float.parseFloat("" + taskModel.getTask_priority()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (insert_update_type.equals("2")) {  /// attachment layout visible

                Bundle bundle = getIntent().getExtras();
                taskModel = (TaskModel) bundle.getSerializable("data");
                position = bundle.getInt("position");
                getSupportActionBar().setTitle("View");
                taskTitle.setText("View");
                inputLayoutTaskNo.setVisibility(View.VISIBLE);
                inputLayoutTaskdate.setVisibility(View.VISIBLE);

                inputTaskno.setText("" + taskModel.getTask_no());
                inputTaskdate.setText("" + taskModel.getTask_create_date());
                inputTaskName.setText("" + taskModel.getTask_name());
                inputTaskDesc.setText("" + taskModel.getTask_description());
                inputTaskDeadline.setText("" + taskModel.getTask_deadline());

                edittextDisabled(inputTaskName);
                edittextDisabled(inputTaskDesc);
                ratingBar.setClickable(false);
                ratingBar.setFocusable(false);

                try {
                    if (!taskModel.getTask_priority().equals("")) {
                        ratingBar.setRating(Float.parseFloat("" + taskModel.getTask_priority()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (taskModel.getAttachment().equals("0")) {
                    attachmentCardView.setVisibility(View.GONE);
                } else {
                    attachmentCardView.setVisibility(View.VISIBLE);

                    try {
                        LayoutInflater layoutInflater = (LayoutInflater) AddTaskActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        if (taskModel.getAttachmentModels().size() > 0) {
                            attachmentLayout.removeAllViews();
                            for (int j = 0; j < taskModel.getAttachmentModels().size(); j++) {
                                final View addView = layoutInflater.inflate(R.layout.list_sales_report_attachment, null);
                                TextView description = (TextView) addView.findViewById(R.id.description);
                                final ImageView img = (ImageView) addView.findViewById(R.id.attachment_img);
                                TextView created_date = (TextView) addView.findViewById(R.id.created_date);
                                description.setText("" + taskModel.getAttachmentModels().get(j).getFile_name());
                                created_date.setText("" + taskModel.getAttachmentModels().get(j).getCreated_date());
                                img.setTag("" + j);
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            Intent intent = new Intent(AddTaskActivity.this, DownloadService.class);
                                            int a = 0;
                                            a = Integer.parseInt("" + img.getTag());
                                            intent.putExtra("file_url", "" + taskModel.getAttachmentModels().get(a).getFile_path());
                                            AddTaskActivity.this.startService(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                attachmentLayout.addView(addView);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        inputTaskDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!insert_update_type.equals("2")) {
                    customerDatePicker = new CustomDatePicker(AddTaskActivity.this);
                    customerDatePicker.setToDate(customerDatePicker.min, inputTaskDeadline, "");
                }

            }
        });

        getTaskUtils();

    }

    public void edittextDisabled(EditText editText) {
        editText.setClickable(false);
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setCursorVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.add_task, menu);
            MenuItem save = menu.findItem(R.id.action_save);
            if (insert_update_type.equals("2")) {
                save.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                if (GlobalElements.isConnectingToInternet(AddTaskActivity.this)) {
                    if (insert_update_type.equals("0")) {
                        addTask();
                    } else {
                        updateTask();
                    }
                } else {
                    GlobalElements.showDialog(AddTaskActivity.this);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SpinnerInteractionListener_task_type implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    spinnerTaskType.setError(null);
                    taskTypeId = taskTypeModels.get(position).getId();
                    if (taskTypeModels.get(position).getName().toLowerCase().equals("owned")) {
                        for (int i = 0; i < taskToModels.size(); i++) {
                            if (taskToModels.get(i).getId().equals("" + myPreferences.getPreferences(MyPreferences.id))) {
                                taskToId = taskToModels.get(i).getId();
                                spinnerTaskTo.setSelection(i + 1);
                                spinnerTaskTo.setEnabled(false);
                                break;
                            }
                        }
                    } else {
                        try {
                            taskToId = "";
                            spinnerTaskTo.setSelection(0);
                            spinnerTaskTo.setEnabled(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    taskTypeId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_task_assigned implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    spinnerTaskAssigned.setError(null);
                    taskAssignedId = taskAssignedModels.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    taskAssignedId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_task_to implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    spinnerTaskTo.setError(null);
                    taskToId = taskToModels.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    taskToId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public class SpinnerInteractionListener_task_color implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
        boolean userSelect = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            userSelect = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (userSelect) {
                // Your selection handling code here
                try {
                    spinnerTaskColor.setError(null);
                    taskColorId = taskColorModels.get(position).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                    taskColorId = "";
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void getTaskUtils() {
        final ProgressDialog pd = new ProgressDialog(AddTaskActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.getTaskUtils(myPreferences.getPreferences(MyPreferences.id));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        JSONArray task_types = result.getJSONArray("task_types");
                        JSONArray taskers = result.getJSONArray("taskers");
                        JSONArray task_colors = result.getJSONArray("task_colors");

                        if (task_types.length() > 0) {
                            taskTypeModels.clear();
                            for (int i = 0; i < task_types.length(); i++) {
                                JSONObject c = task_types.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                taskTypeModels.add(da);
                            }
                            taskAdapter = new GeneralAdapter(AddTaskActivity.this, taskTypeModels);
                            spinnerTaskType.setAdapter(taskAdapter);
                            spinnerTaskType.setHint("" + getResources().getString(R.string.task_type));
                            SpinnerInteractionListener_task_type listener_1 = new SpinnerInteractionListener_task_type();
                            spinnerTaskType.setOnTouchListener(listener_1);
                            spinnerTaskType.setOnItemSelectedListener(listener_1);

                            if (insert_update_type.equals("1") || insert_update_type.equals("2")) {
                                for (int i = 0; i < taskTypeModels.size(); i++) {
                                    if (taskTypeModels.get(i).getId().equals("" + taskModel.getTask_type())) {
                                        taskTypeId = taskTypeModels.get(i).getId();
                                        spinnerTaskType.setSelection(i + 1);
                                        break;
                                    }
                                }
                            }
                            if (insert_update_type.equals("2")) {
                                spinnerTaskType.setEnabled(false);
                            }

                        } else {
                            taskTypeModels.clear();
                            taskAdapter = new GeneralAdapter(AddTaskActivity.this, taskTypeModels);
                            spinnerTaskType.setAdapter(taskAdapter);
                            spinnerTaskType.setHint("" + getResources().getString(R.string.task_type));
                            SpinnerInteractionListener_task_type listener_1 = new SpinnerInteractionListener_task_type();
                            spinnerTaskType.setOnTouchListener(listener_1);
                            spinnerTaskType.setOnItemSelectedListener(listener_1);
                        }

                        if (taskers.length() > 0) {
                            taskAssignedModels.clear();
                            for (int i = 0; i < taskers.length(); i++) {
                                JSONObject c = taskers.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                taskAssignedModels.add(da);
                            }
                            taskAssignedAdapter = new GeneralAdapter(AddTaskActivity.this, taskAssignedModels);
                            spinnerTaskAssigned.setAdapter(taskAssignedAdapter);
                            spinnerTaskAssigned.setHint("" + getResources().getString(R.string.task_assigned));
                            SpinnerInteractionListener_task_assigned listener_1 = new SpinnerInteractionListener_task_assigned();
                            spinnerTaskAssigned.setOnTouchListener(listener_1);
                            spinnerTaskAssigned.setOnItemSelectedListener(listener_1);

                            for (int i = 0; i < taskAssignedModels.size(); i++) {
                                if (myPreferences.getPreferences(MyPreferences.id).equals("" + taskAssignedModels.get(i).getId())) {
                                    taskAssignedId = taskAssignedModels.get(i).getId();
                                    spinnerTaskAssigned.setSelection(i + 1);
                                    break;
                                }
                            }
                            spinnerTaskAssigned.setEnabled(false);

                        } else {
                            taskAssignedModels.clear();
                            taskAssignedAdapter = new GeneralAdapter(AddTaskActivity.this, taskAssignedModels);
                            spinnerTaskAssigned.setAdapter(taskAssignedAdapter);
                            spinnerTaskAssigned.setHint("" + getResources().getString(R.string.task_assigned));
                            SpinnerInteractionListener_task_assigned listener_1 = new SpinnerInteractionListener_task_assigned();
                            spinnerTaskAssigned.setOnTouchListener(listener_1);
                            spinnerTaskAssigned.setOnItemSelectedListener(listener_1);
                        }

                        if (taskers.length() > 0) {
                            taskToModels.clear();
                            for (int i = 0; i < taskers.length(); i++) {
                                JSONObject c = taskers.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                taskToModels.add(da);
                            }
                            taskToAdapter = new GeneralAdapter(AddTaskActivity.this, taskToModels);
                            spinnerTaskTo.setAdapter(taskToAdapter);
                            spinnerTaskTo.setHint("" + getResources().getString(R.string.task_to));
                            SpinnerInteractionListener_task_to listener_1 = new SpinnerInteractionListener_task_to();
                            spinnerTaskTo.setOnTouchListener(listener_1);
                            spinnerTaskTo.setOnItemSelectedListener(listener_1);

                            if (insert_update_type.equals("1") || insert_update_type.equals("2")) {
                                for (int i = 0; i < taskToModels.size(); i++) {
                                    if (taskToModels.get(i).getId().equals("" + taskModel.getTask_assigned_to())) {
                                        taskToId = taskToModels.get(i).getId();
                                        spinnerTaskTo.setSelection(i + 1);
                                        break;
                                    }
                                }
                            }
                            if (insert_update_type.equals("2")) {
                                spinnerTaskTo.setEnabled(false);
                            }
                        } else {
                            taskToModels.clear();
                            taskToAdapter = new GeneralAdapter(AddTaskActivity.this, taskToModels);
                            spinnerTaskTo.setAdapter(taskToAdapter);
                            spinnerTaskTo.setHint("" + getResources().getString(R.string.task_to));
                            SpinnerInteractionListener_task_to listener_1 = new SpinnerInteractionListener_task_to();
                            spinnerTaskTo.setOnTouchListener(listener_1);
                            spinnerTaskTo.setOnItemSelectedListener(listener_1);
                        }

                        if (task_colors.length() > 0) {
                            taskColorModels.clear();
                            for (int i = 0; i < task_colors.length(); i++) {
                                JSONObject c = task_colors.getJSONObject(i);
                                GeneralModel da = new GeneralModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("name"));
                                da.setColorCode(c.getString("slug"));
                                taskColorModels.add(da);
                            }
                            taskColorAdapter = new GeneralAdapter(AddTaskActivity.this, taskColorModels);
                            spinnerTaskColor.setAdapter(taskColorAdapter);
                            spinnerTaskColor.setHint("" + getResources().getString(R.string.task_color));
                            SpinnerInteractionListener_task_color listener_1 = new SpinnerInteractionListener_task_color();
                            spinnerTaskColor.setOnTouchListener(listener_1);
                            spinnerTaskColor.setOnItemSelectedListener(listener_1);

                            if (insert_update_type.equals("1") || insert_update_type.equals("2")) {
                                for (int i = 0; i < taskColorModels.size(); i++) {
                                    if (taskColorModels.get(i).getId().equals("" + taskModel.getTask_color_tag())) {
                                        taskColorId = taskColorModels.get(i).getId();
                                        spinnerTaskColor.setSelection(i + 1);
                                        break;
                                    }
                                }
                            }

                        } else {
                            taskColorModels.clear();
                            taskColorAdapter = new GeneralAdapter(AddTaskActivity.this, taskColorModels);
                            spinnerTaskColor.setAdapter(taskColorAdapter);
                            spinnerTaskColor.setHint("" + getResources().getString(R.string.task_color));
                            SpinnerInteractionListener_task_color listener_1 = new SpinnerInteractionListener_task_color();
                            spinnerTaskColor.setOnTouchListener(listener_1);
                            spinnerTaskColor.setOnItemSelectedListener(listener_1);
                        }

                    } else {
                        Toaster.show(AddTaskActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                    }

                    if (insert_update_type.equals("2") && myPreferences.getPreferences(MyPreferences.login_type).equals("0")) // view flag
                    {
                        getTaskLog();
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

    private void getTaskLog() {
        final ProgressDialog pd = new ProgressDialog(AddTaskActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.getTaskLog(myPreferences.getPreferences(MyPreferences.id), taskModel.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    logModels.clear();
                    if (json.getInt("ack") == 1) {
                        JSONArray result = json.getJSONArray("result");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject c = result.getJSONObject(i);
                            TaskLogModel da = new TaskLogModel();
                            da.setId("" + c.getString("id"));
                            da.setCreatedBy("" + c.getString("user_id"));
                            da.setForwardTo(c.getString("forwarded_id"));
                            da.setTitle(c.getString("title"));
                            da.setDescription(c.getString("description"));
                            da.setDate(c.getString("created_date"));
                            logModels.add(da);
                        }
                        logAdapter = new TaskLogAdapter(AddTaskActivity.this, logModels);
                        recycleview.setAdapter(logAdapter);
                        recycleview.setLayoutManager(new LinearLayoutManager(AddTaskActivity.this, LinearLayoutManager.VERTICAL, false));
                        logCardView.setVisibility(View.VISIBLE);
                    } else {
                        logCardView.setVisibility(View.GONE);
                        Toaster.show(AddTaskActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void addTask() {
        final ProgressDialog pd = new ProgressDialog(AddTaskActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.createTask(myPreferences.getPreferences(MyPreferences.id), status, inputTaskName.getText().toString(), taskTypeId, taskAssignedId, taskToId, inputTaskDesc.getText().toString(),
                taskColorId, inputTaskDeadline.getText().toString(), "" + ratingBar.getRating());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        taskModel.setId(result.getString("id"));
                        taskModel.setTask_no(result.getString("task_no"));
                        taskModel.setTask_name(result.getString("task_name"));
                        taskModel.setTask_deadline(result.getString("task_deadline"));
                        taskModel.setTask_description(result.getString("task_description"));
                        taskModel.setTask_type(result.getString("task_type"));
                        taskModel.setTask_color_tag(result.getString("task_color_tag"));
                        taskModel.setTask_assigned_by(result.getString("task_assigned_by"));
                        taskModel.setTask_assigned_by_name(result.getString("task_assigned_by_name"));
                        if (result.getString("task_assigned_by").equals("" + myPreferences.getPreferences(MyPreferences.id)) && !result.getString("task_assigned_to").equals("" + myPreferences.getPreferences(MyPreferences.id))) {
                            taskModel.setTask_color_tag_slug("#FF0000"); // red
                        } else if (result.getString("task_assigned_to").equals("" + myPreferences.getPreferences(MyPreferences.id)) && result.getString("task_assigned_by").equals("" + myPreferences.getPreferences(MyPreferences.id))) {
                            taskModel.setTask_color_tag_slug("#197b30"); // green
                        } else {
                            taskModel.setTask_color_tag_slug("#0c08d8"); // blue
                        }

                        taskModel.setTask_create_date(result.getString("task_create_date"));
                        taskModel.setTask_priority(result.getString("task_priority"));
                        taskModel.setTask_assigned_to(result.getString("task_assigned_to"));
                        taskModel.setTask_assigned_to_name(result.getString("task_assigned_to_name"));

                        taskModel.setAttachment("0");
                        ArrayList<TaskNoteModel> noteModels = new ArrayList<TaskNoteModel>();
                        taskModel.setNoteModels(noteModels);
                        ArrayList<TaskAttachmentModel> attachmentModels = new ArrayList<TaskAttachmentModel>();
                        taskModel.setAttachmentModels(attachmentModels);

                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putSerializable("data", taskModel);
                        intent.putExtras(b);
                        setResult(18, intent);
                        finish();
                    } else {
                        Toaster.show(AddTaskActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void updateTask() {
        final ProgressDialog pd = new ProgressDialog(AddTaskActivity.this);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call = req.updateTask(myPreferences.getPreferences(MyPreferences.id), taskModel.getId(), status, inputTaskName.getText().toString(), taskTypeId, taskAssignedId, taskToId, inputTaskDesc.getText().toString(),
                taskColorId, inputTaskDeadline.getText().toString(), "" + ratingBar.getRating());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        taskModel.setId(result.getString("id"));
                        taskModel.setTask_no(result.getString("task_no"));
                        taskModel.setTask_name(result.getString("task_name"));
                        taskModel.setTask_deadline(result.getString("task_deadline"));
                        taskModel.setTask_description(result.getString("task_description"));
                        taskModel.setTask_type(result.getString("task_type"));
                        taskModel.setTask_color_tag(result.getString("task_color_tag"));
                        taskModel.setTask_assigned_by(result.getString("task_assigned_by"));
                        taskModel.setTask_assigned_by_name(result.getString("task_assigned_by_name"));
                        if (result.getString("task_assigned_by").equals("" + myPreferences.getPreferences(MyPreferences.id)) && !result.getString("task_assigned_to").equals("" + myPreferences.getPreferences(MyPreferences.id))) {
                            taskModel.setTask_color_tag_slug("#FF0000"); // red
                        } else if (result.getString("task_assigned_to").equals("" + myPreferences.getPreferences(MyPreferences.id)) && result.getString("task_assigned_by").equals("" + myPreferences.getPreferences(MyPreferences.id))) {
                            taskModel.setTask_color_tag_slug("#197b30"); // green
                        } else {
                            taskModel.setTask_color_tag_slug("#0c08d8"); // blue
                        }
                        //taskModel.setTask_color_tag_slug(result.getString("task_color_tag_slug"));
                        taskModel.setTask_create_date(result.getString("task_create_date"));
                        taskModel.setTask_priority(result.getString("task_priority"));
                        taskModel.setTask_assigned_to(result.getString("task_assigned_to"));
                        taskModel.setTask_assigned_to_name(result.getString("task_assigned_to_name"));
                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putSerializable("data", taskModel);
                        b.putInt("position", position);
                        intent.putExtras(b);
                        setResult(19, intent);
                        finish();
                    } else {
                        Toaster.show(AddTaskActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
