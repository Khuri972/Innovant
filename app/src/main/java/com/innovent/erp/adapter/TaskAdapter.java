package com.innovent.erp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innovent.erp.AddTaskActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.R;
import com.innovent.erp.TaskActivity;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.model.TaskAttachmentModel;
import com.innovent.erp.model.TaskNoteModel;
import com.innovent.erp.model.TaskModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONException;
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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<TaskModel> data = new ArrayList<>();
    private Context context;
    MyPreferences myPreferences;
    String status;
    AlertDialog buildInfosDialog;
    Fragment fragment;
    File file = null;
    TextView fileNameTxt;
    PopupWindow mPopupWindow;

    public interface moveTask {
        public void moveTask(int position);
    }

    public TaskAdapter(Context context, ArrayList<TaskModel> data, String status, Fragment fragment) {
        this.data = data;
        this.context = context;
        this.status = status;
        this.fragment = fragment;
        myPreferences = new MyPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        try {
            viewHolder.taskName.setText(data.get(i).getTask_name());
            viewHolder.taskDescription.setText(data.get(i).getTask_description());
            viewHolder.taskAssignedBy.setText(data.get(i).getTask_no() + " " + data.get(i).getTask_assigned_by_name() + "-" + data.get(i).getTask_assigned_to_name());
            viewHolder.deadLineDate.setText("" + data.get(i).getTask_deadline());
            if (data.get(i).getTask_priority().equals("")) {
                viewHolder.taskRate.setRating(Float.parseFloat("1"));
            } else {
                viewHolder.taskRate.setRating(Float.parseFloat("" + data.get(i).getTask_priority()));
            }
            try {
                if (GlobalElements.compareDateAfter(data.get(i).getTask_deadline())) {
                    if (status.equals("5")) {
                        viewHolder.deadlineImg.setVisibility(View.GONE);
                    } else {
                        viewHolder.deadlineImg.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolder.deadlineImg.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (data.get(i).getTask_color_tag_slug().equals("#FF0000")) {
                    viewHolder.taskIcon.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                } else if (data.get(i).getTask_color_tag_slug().equals("#197b30")) {
                    viewHolder.taskIcon.setImageResource(R.drawable.ic_person_black_24dp); // green
                } else {
                    viewHolder.taskIcon.setImageResource(R.drawable.ic_arrow_downward_black_24dp); // blue
                }
                viewHolder.taskAssignedBy.setTextColor(Color.parseColor("" + data.get(i).getTask_color_tag_slug()));
                viewHolder.colorCode.setBackgroundColor(Color.parseColor("" + data.get(i).getTask_color_tag_slug()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (data.get(i).getNoteModels().size() > 0) {
                    viewHolder.noteLayout.removeAllViews();
                    for (int j = 0; j < data.get(i).getNoteModels().size(); j++) {
                        final View addView = layoutInflater.inflate(R.layout.list_task_note, null);
                        TextView userName = (TextView) addView.findViewById(R.id.user_name);
                        TextView description = (TextView) addView.findViewById(R.id.description);
                        TextView created_date = (TextView) addView.findViewById(R.id.created_date);
                        userName.setText("" + data.get(i).getNoteModels().get(j).getUsername());
                        description.setText("" + data.get(i).getNoteModels().get(j).getDescription());
                        created_date.setText("" + data.get(i).getNoteModels().get(j).getCreated_date());
                        viewHolder.noteLayout.addView(addView);
                    }
                }


                if (data.get(i).getAttachment().equals("0")) {
                    viewHolder.attachmentLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.attachmentLayout.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            viewHolder.attachmentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(context, AddTaskActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("data", data.get(i));
                        intent.putExtra("status", "" + status);
                        intent.putExtra("position", i);
                        intent.putExtra("type", "2");
                        intent.putExtras(b);
                        ((Activity) context).startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    PopupMenu popupMenu = new PopupMenu(context, view);
//                    Menu m = popupMenu.getMenu();
//                    MenuInflater inflater = popupMenu.getMenuInflater();
//                    inflater.inflate(R.menu.popup_menu_task, popupMenu.getMenu());
//                    if (data.get(i).getIsActive().equals("0")) {
//                        m.findItem(R.id.action_archive).setTitle("Un Hide");
//                    } else {
//                        m.findItem(R.id.action_archive).setTitle("Hide");
//                    }
//                    if (!myPreferences.getPreferences(MyPreferences.task_insert).equals("1")) {
//                        m.removeItem(R.id.action_edit);
//                    }
//
//                    if (!myPreferences.getPreferences(MyPreferences.task_delete).equals("1")) {
//                        m.removeItem(R.id.action_delete);
//                    }
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            switch (item.getItemId()) {
//                                case R.id.action_move:
//                                    try {
//                                        moveTask task = (moveTask) fragment;
//                                        task.moveTask(i);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case R.id.action_view:
//                                    try {
//                                        Intent intent = new Intent(context, AddTaskActivity.class);
//                                        Bundle b = new Bundle();
//                                        b.putSerializable("data", data.get(i));
//                                        intent.putExtra("status", "" + status);
//                                        intent.putExtra("position", i);
//                                        intent.putExtra("type", "2");
//                                        intent.putExtras(b);
//                                        ((Activity) context).startActivityForResult(intent, 0);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case R.id.action_edit:
//                                    try {
//                                        Intent intent = new Intent(context, AddTaskActivity.class);
//                                        Bundle b = new Bundle();
//                                        b.putSerializable("data", data.get(i));
//                                        intent.putExtra("status", "" + status);
//                                        intent.putExtra("position", i);
//                                        intent.putExtra("type", "1");
//                                        intent.putExtras(b);
//                                        ((Activity) context).startActivityForResult(intent, 0);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case R.id.action_delete:
//                                    try {
//                                        AlertDialog buildInfosDialog;
//                                        AlertDialog.Builder alertDialog2;
//                                        alertDialog2 = new AlertDialog.Builder(context);
//                                        alertDialog2.setTitle("Are you sure want to delete");
//
//                                        alertDialog2.setPositiveButton("Yes",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        // Write your code here to execute after dialog
//                                                        //new All().execute("clear_wishlist");
//                                                        if (GlobalElements.isConnectingToInternet(context)) {
//                                                            deleteTask("delete", data.get(i).getId(), i, "0");
//                                                        } else {
//                                                            GlobalElements.showDialog(context);
//                                                        }
//                                                    }
//                                                });
//
//                                        alertDialog2.setNegativeButton("NO",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        // Write your code here to execute after dialog
//
//                                                    }
//                                                });
//                                        buildInfosDialog = alertDialog2.create();
//                                        buildInfosDialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case R.id.action_archive:
//                                    try {
//                                        AlertDialog buildInfosDialog;
//                                        AlertDialog.Builder alertDialog2;
//                                        alertDialog2 = new AlertDialog.Builder(context);
//                                        if (data.get(i).getIsActive().equals("0")) {
//                                            alertDialog2.setTitle("Are you sure want to Un hide this task");
//                                        } else {
//                                            alertDialog2.setTitle("Are you sure want to hide this task");
//                                        }
//
//                                        alertDialog2.setPositiveButton("Yes",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        // Write your code here to execute after dialog
//                                                        if (GlobalElements.isConnectingToInternet(context)) {
//                                                            if (data.get(i).getIsActive().equals("0")) {
//                                                                deleteTask("archive", data.get(i).getId(), i, "1");
//                                                            } else {
//                                                                deleteTask("archive", data.get(i).getId(), i, "0");
//                                                            }
//
//                                                        } else {
//                                                            GlobalElements.showDialog(context);
//                                                        }
//                                                    }
//                                                });
//
//                                        alertDialog2.setNegativeButton("NO",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        // Write your code here to execute after dialog
//
//                                                    }
//                                                });
//                                        buildInfosDialog = alertDialog2.create();
//                                        buildInfosDialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case R.id.action_note:
//                                    try {
//                                        AlertDialog.Builder alertDialog2;
//                                        alertDialog2 = new AlertDialog.Builder(context);
//
//                                        LayoutInflater inflater = LayoutInflater.from(context);
//                                        final View dialogView = inflater.inflate(R.layout.dialog_note, null);
//                                        alertDialog2.setView(dialogView);
//
//                                        ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelbtn);
//                                        final EditText noteEdt = (EditText) dialogView.findViewById(R.id.note_edt);
//                                        Button close = (Button) dialogView.findViewById(R.id.close);
//                                        Button create_note = (Button) dialogView.findViewById(R.id.create_note);
//                                        LinearLayout attachmentLayout = (LinearLayout) dialogView.findViewById(R.id.attachment_layout);
//                                        attachmentLayout.setVisibility(View.VISIBLE);
//                                        fileNameTxt = (TextView) dialogView.findViewById(R.id.file_name_txt);
//                                        TextView attachmentTxt = (TextView) dialogView.findViewById(R.id.attachment_txt);
//
//                                        cancelImg.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                buildInfosDialog.dismiss();
//                                            }
//                                        });
//
//                                        close.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                buildInfosDialog.dismiss();
//                                            }
//                                        });
//
//                                        attachmentTxt.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                try {
//                                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                                                    intent.setType("*/*");
//                                                    ((Activity) context).startActivityForResult(intent, 7);
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        });
//
//                                        noteEdt.addTextChangedListener(new TextWatcher() {
//                                            @Override
//                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                                            }
//
//                                            @Override
//                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                                try {
//                                                    noteEdt.setError(null);
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void afterTextChanged(Editable editable) {
//
//                                            }
//                                        });
//
//                                        create_note.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                if (noteEdt.getText().toString().equals("")) {
//                                                    noteEdt.setError("This is a required field");
//                                                } else {
//                                                    if (GlobalElements.isConnectingToInternet(context)) {
//                                                        createNote(data.get(i).getId(), i, noteEdt.getText().toString());
//                                                    } else {
//                                                        GlobalElements.showDialog(context);
//                                                    }
//                                                    buildInfosDialog.dismiss();
//                                                }
//                                            }
//                                        });
//                                        buildInfosDialog = alertDialog2.create();
//                                        buildInfosDialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                                case R.id.action_attachment:
//                                    try {
//                                        AlertDialog.Builder alertDialog2;
//                                        alertDialog2 = new AlertDialog.Builder(context);
//
//                                        LayoutInflater inflater = LayoutInflater.from(context);
//                                        final View dialogView = inflater.inflate(R.layout.dialog_note, null);
//                                        alertDialog2.setView(dialogView);
//
//                                        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
//                                        dialogTitle.setText("Create Attachment");
//
//                                        ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelbtn);
//                                        final EditText noteEdt = (EditText) dialogView.findViewById(R.id.note_edt);
//                                        Button close = (Button) dialogView.findViewById(R.id.close);
//                                        Button create_note = (Button) dialogView.findViewById(R.id.create_note);
//                                        LinearLayout attachmentLayout = (LinearLayout) dialogView.findViewById(R.id.attachment_layout);
//                                        attachmentLayout.setVisibility(View.VISIBLE);
//                                        fileNameTxt = (TextView) dialogView.findViewById(R.id.file_name_txt);
//                                        TextView attachmentTxt = (TextView) dialogView.findViewById(R.id.attachment_txt);
//
//                                        create_note.setText("Add Attachment");
//
//                                        cancelImg.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                buildInfosDialog.dismiss();
//                                            }
//                                        });
//
//                                        close.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                buildInfosDialog.dismiss();
//                                            }
//                                        });
//
//                                        attachmentTxt.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                try {
//                                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                                                    intent.setType("*/*");
//                                                    ((Activity) context).startActivityForResult(intent, 7);
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        });
//
//                                        noteEdt.addTextChangedListener(new TextWatcher() {
//                                            @Override
//                                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                                            }
//
//                                            @Override
//                                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                                                try {
//                                                    noteEdt.setError(null);
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void afterTextChanged(Editable editable) {
//
//                                            }
//                                        });
//
//                                        create_note.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                if (file == null) {
//                                                    Toaster.show(context, "Select attachment", false, Toaster.DANGER);
//                                                } else {
//                                                    if (GlobalElements.isConnectingToInternet(context)) {
//                                                        createNote(data.get(i).getId(), i, noteEdt.getText().toString());
//                                                    } else {
//                                                        GlobalElements.showDialog(context);
//                                                    }
//                                                    buildInfosDialog.dismiss();
//                                                }
//                                            }
//                                        });
//                                        buildInfosDialog = alertDialog2.create();
//                                        buildInfosDialog.show();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                            }
//                            return false;
//                        }
//                    });
//                    popupMenu.show();

                    try {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View customView = inflater.inflate(R.layout.layout_task_more, null);
                        mPopupWindow = new PopupWindow(
                                customView,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT, true
                        );

                        mPopupWindow.setOutsideTouchable(true);
                        mPopupWindow.setFocusable(true);
                        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                    mPopupWindow.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });
                        // Call requires API level 21
                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5);
                        }

                        LinearLayout change_layout = (LinearLayout) customView.findViewById(R.id.change_layout);
                        LinearLayout view_layout = (LinearLayout) customView.findViewById(R.id.view_layout);
                        LinearLayout editLayout = (LinearLayout) customView.findViewById(R.id.edit_layout);
                        LinearLayout deleteLayout = (LinearLayout) customView.findViewById(R.id.delete_layout);
                        LinearLayout hideLayout = (LinearLayout) customView.findViewById(R.id.hide_layout);
                        TextView hidetxt = (TextView) customView.findViewById(R.id.hide_txt);
                        LinearLayout noteLayout = (LinearLayout) customView.findViewById(R.id.note_layout);
                        LinearLayout attachmentLayout = (LinearLayout) customView.findViewById(R.id.attachment_layout);

                        if (data.get(i).getIsActive().equals("0")) {
                            hidetxt.setText("Un Hide");
                        } else {
                            hidetxt.setText("Hide");
                        }
                        if (myPreferences.getPreferences(MyPreferences.task_insert).equals("1")) {
                            editLayout.setVisibility(View.VISIBLE);
                        }

                        if (myPreferences.getPreferences(MyPreferences.task_delete).equals("1")) {
                            deleteLayout.setVisibility(View.VISIBLE);
                        }

                        change_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    moveTask task = (moveTask) fragment;
                                    task.moveTask(i);
                                    mPopupWindow.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        view_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = new Intent(context, AddTaskActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("data", data.get(i));
                                    intent.putExtra("status", "" + status);
                                    intent.putExtra("position", i);
                                    intent.putExtra("type", "2");
                                    intent.putExtras(b);
                                    ((Activity) context).startActivityForResult(intent, 0);
                                    mPopupWindow.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        editLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    Intent intent = new Intent(context, AddTaskActivity.class);
                                    Bundle b = new Bundle();
                                    b.putSerializable("data", data.get(i));
                                    intent.putExtra("status", "" + status);
                                    intent.putExtra("position", i);
                                    intent.putExtra("type", "1");
                                    intent.putExtras(b);
                                    ((Activity) context).startActivityForResult(intent, 0);
                                    mPopupWindow.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        deleteLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
                                                        deleteTask("delete", data.get(i).getId(), i, "0");
                                                        mPopupWindow.dismiss();
                                                    } else {
                                                        GlobalElements.showDialog(context);
                                                    }
                                                }
                                            });

                                    alertDialog2.setNegativeButton("NO",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to execute after dialog
                                                    mPopupWindow.dismiss();
                                                }
                                            });
                                    buildInfosDialog = alertDialog2.create();
                                    buildInfosDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        hideLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    AlertDialog buildInfosDialog;
                                    AlertDialog.Builder alertDialog2;
                                    alertDialog2 = new AlertDialog.Builder(context);
                                    if (data.get(i).getIsActive().equals("0")) {
                                        alertDialog2.setTitle("Are you sure want to Un hide this task");
                                    } else {
                                        alertDialog2.setTitle("Are you sure want to hide this task");
                                    }

                                    alertDialog2.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to execute after dialog
                                                    if (GlobalElements.isConnectingToInternet(context)) {
                                                        if (data.get(i).getIsActive().equals("0")) {
                                                            deleteTask("archive", data.get(i).getId(), i, "1");
                                                        } else {
                                                            deleteTask("archive", data.get(i).getId(), i, "0");
                                                        }
                                                        mPopupWindow.dismiss();

                                                    } else {
                                                        GlobalElements.showDialog(context);
                                                    }
                                                }
                                            });

                                    alertDialog2.setNegativeButton("NO",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to execute after dialog
                                                    mPopupWindow.dismiss();
                                                }
                                            });
                                    buildInfosDialog = alertDialog2.create();
                                    buildInfosDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        noteLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
                                    LinearLayout attachmentLayout = (LinearLayout) dialogView.findViewById(R.id.attachment_layout);
                                    attachmentLayout.setVisibility(View.VISIBLE);
                                    fileNameTxt = (TextView) dialogView.findViewById(R.id.file_name_txt);
                                    TextView attachmentTxt = (TextView) dialogView.findViewById(R.id.attachment_txt);

                                    cancelImg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            buildInfosDialog.dismiss();
                                            mPopupWindow.dismiss();
                                        }
                                    });

                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            buildInfosDialog.dismiss();
                                            mPopupWindow.dismiss();
                                        }
                                    });

                                    attachmentTxt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                mPopupWindow.dismiss();
                                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                                intent.setType("*/*");
                                                ((Activity) context).startActivityForResult(intent, 7);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
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

                                                    createNote(data.get(i).getId(), i, noteEdt.getText().toString());
                                                } else {
                                                    GlobalElements.showDialog(context);
                                                }
                                                mPopupWindow.dismiss();
                                                buildInfosDialog.dismiss();
                                            }
                                        }
                                    });
                                    buildInfosDialog = alertDialog2.create();
                                    buildInfosDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        attachmentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    AlertDialog.Builder alertDialog2;
                                    alertDialog2 = new AlertDialog.Builder(context);

                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    final View dialogView = inflater.inflate(R.layout.dialog_note, null);
                                    alertDialog2.setView(dialogView);

                                    TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
                                    dialogTitle.setText("Create Attachment");

                                    ImageView cancelImg = (ImageView) dialogView.findViewById(R.id.cancelbtn);
                                    final EditText noteEdt = (EditText) dialogView.findViewById(R.id.note_edt);
                                    Button close = (Button) dialogView.findViewById(R.id.close);
                                    Button create_note = (Button) dialogView.findViewById(R.id.create_note);
                                    LinearLayout attachmentLayout = (LinearLayout) dialogView.findViewById(R.id.attachment_layout);
                                    attachmentLayout.setVisibility(View.VISIBLE);
                                    fileNameTxt = (TextView) dialogView.findViewById(R.id.file_name_txt);
                                    TextView attachmentTxt = (TextView) dialogView.findViewById(R.id.attachment_txt);

                                    create_note.setText("Add Attachment");

                                    cancelImg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            buildInfosDialog.dismiss();
                                            mPopupWindow.dismiss();
                                        }
                                    });

                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            buildInfosDialog.dismiss();
                                            mPopupWindow.dismiss();
                                        }
                                    });

                                    attachmentTxt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                mPopupWindow.dismiss();
                                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                                intent.setType("*/*");
                                                ((Activity) context).startActivityForResult(intent, 7);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
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
                                            if (file == null) {
                                                Toaster.show(context, "Select attachment", false, Toaster.DANGER);
                                            } else {
                                                if (GlobalElements.isConnectingToInternet(context)) {
                                                    createNote(data.get(i).getId(), i, noteEdt.getText().toString());
                                                } else {
                                                    GlobalElements.showDialog(context);
                                                }
                                                mPopupWindow.dismiss();
                                                buildInfosDialog.dismiss();
                                            }
                                        }
                                    });
                                    buildInfosDialog = alertDialog2.create();
                                    buildInfosDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        mPopupWindow.showAtLocation(viewHolder.more, Gravity.RIGHT | Gravity.RIGHT, 50, 30);
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
        @BindView(R.id.list_task_name)
        TextView taskName;
        @BindView(R.id.list_task_desc)
        TextView taskDescription;

        @BindView(R.id.list_task_assigned_by)
        TextView taskAssignedBy;
        @BindView(R.id.created_date)
        TextView createdDate;
        @BindView(R.id.deadline_date)
        TextView deadLineDate;
        @BindView(R.id.task_rate)
        RatingBar taskRate;
        @BindView(R.id.list_task_main)
        LinearLayout layout;
        @BindView(R.id.note_layout)
        LinearLayout noteLayout;
        @BindView(R.id.attachment_layout)
        LinearLayout attachmentLayout;
        @BindView(R.id.list_task_view)
        View colorCode;
        @BindView(R.id.task_more)
        ImageView more;
        @BindView(R.id.task_icon)
        ImageView taskIcon;
        @BindView(R.id.deadline_img)
        ImageView deadlineImg;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void getFilePath(File file, String mimeType) {
        try {
            this.file = file;
            fileNameTxt.setText("" + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(String service_type, String tid, final int position, String archive) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        Call<ResponseBody> call;

        if (service_type.equals("delete")) {
            call = req.deleteTask(myPreferences.getPreferences(MyPreferences.id), tid);
        } else {

            call = req.archiveTask(myPreferences.getPreferences(MyPreferences.id), tid, status, archive);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        if (data.get(position).getIsActive().equals("0")) {
                            data.get(position).setIsActive("1");
                            notifyItemChanged(position);
                        } else {
                            data.remove(position);
                            notifyItemRemoved(position);
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

    private void createNote(String tid, final int position, final String note) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        RequestInterface req = RetrofitClient.getClient().create(RequestInterface.class);
        RequestBody requestfile_image;
        MultipartBody.Part body_image;

        Call<ResponseBody> call;
        if (file == null) {
            call = req.createNote(myPreferences.getPreferences(MyPreferences.id), tid, status, "" + note);
        } else {
            requestfile_image = RequestBody.create(MediaType.parse("*/*"), file);
            body_image = MultipartBody.Part.createFormData("attachment", file.getName(), requestfile_image);
            call = req.createNote(myPreferences.getPreferences(MyPreferences.id), tid, status, "" + note, body_image);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String json_response = response.body().string();
                    JSONObject json = new JSONObject(json_response);
                    if (json.getInt("ack") == 1) {
                        JSONObject result = json.getJSONObject("result");
                        if (!note.equals("")) {
                            JSONObject notes = result.getJSONObject("notes");
                            TaskNoteModel na = new TaskNoteModel();
                            na.setId("" + notes.getString("id"));
                            na.setUsername("" + notes.getString("username"));
                            na.setDescription("" + notes.getString("description"));
                            na.setCreated_date(notes.getString("created_date"));
                            data.get(position).getNoteModels().add(na);
                        }

                        try {
                            if (file != null) {
                                if (data.get(position).getAttachment().equals("0")) {
                                    data.get(position).setAttachment("1");
                                }
                                JSONObject attachment = result.getJSONObject("attachment");
                                if (!attachment.equals("")) {
                                    TaskAttachmentModel da = new TaskAttachmentModel();
                                    da.setId("" + attachment.getString("id"));
                                    File file = new File("" + attachment.getString("file_path"));
                                    da.setFile_name("" + file.getName());
                                    da.setFile_path("" + attachment.getString("file_path"));
                                    da.setCreated_date(attachment.getString("created_date"));
                                    data.get(position).getAttachmentModels().add(da);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        file = null;
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

}
