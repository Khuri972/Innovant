package com.innovent.erp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.adapter.DocumentAdapter;
import com.innovent.erp.adapter.DrivePathAdapter;
import com.innovent.erp.adapter.TaskDialogAdapter;
import com.innovent.erp.custom.DividerItemDecoration;
import com.innovent.erp.custom.PathUtil;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.custom.SpacesItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.dialog.BottomSheetFragment;
import com.innovent.erp.dialog.DocumentFilterBottomSheetFragment;
import com.innovent.erp.model.DocumentModel;
import com.innovent.erp.model.DrivePathModel;
import com.innovent.erp.model.GeneralModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;
import com.innovent.erp.tutoral.TapIntroHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentActivity extends AppCompatActivity implements BottomSheetFragment.newFolderCreate, DocumentAdapter.clickFolder, DrivePathAdapter.ChangeDrive,
        DocumentFilterAdapter.shortFilter {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.add_document)
    FloatingActionButton addDocument;
    @BindView(R.id.recycleView_drive_path)
    RecyclerView recycleViewDrivePath;
    @BindView(R.id.nested)
    NestedScrollView nested;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    DocumentFilterBottomSheetFragment bottomSheetFragment;

    ArrayList<DocumentModel> data = new ArrayList<>();
    ArrayList<DrivePathModel> pathModels = new ArrayList<>();
    DrivePathAdapter pathAdapter;
    DocumentAdapter adapter;
    StaggeredGridLayoutManager gaggeredGridLayoutManager;
    String name, id = "0", sort = "0";
    MyPreferences myPreferences;
    File file = null;
    String mimeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            if (myPreferences.getPreferences(MyPreferences.document_insert).equals("1")) {
                addDocument.setVisibility(View.VISIBLE);
            } else {
                addDocument.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        recycleView.setNestedScrollingEnabled(false);
        recycleViewDrivePath.setNestedScrollingEnabled(false);

        try {
            Intent intent = getIntent();
            name = intent.getStringExtra("name");
            id = intent.getStringExtra("id");
            if (name == null) {
                id = "0";
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DocumentActivity.this, 2);
                int spanCount = 10; // 3 columns
                int spacing = 10; // 50px
                boolean includeEdge = false;
                recycleView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
                gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                adapter = new DocumentAdapter(DocumentActivity.this, data);
                recycleView.setAdapter(adapter);
                recycleView.setLayoutManager(layoutManager);

                recycleViewDrivePath.setAdapter(adapter);
                recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));
            } else {
                getSupportActionBar().setTitle("" + name);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(DocumentActivity.this, 2);
                int spanCount = 10; // 3 columns
                int spacing = 10; // 50px
                boolean includeEdge = false;
                recycleView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));
                adapter = new DocumentAdapter(DocumentActivity.this, data);
                recycleView.setAdapter(adapter);
                gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                recycleView.setLayoutManager(layoutManager);
                recycleViewDrivePath.setAdapter(adapter);
                recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalElements.isConnectingToInternet(DocumentActivity.this)) {
                    getDirectoryHistory(false);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        getDirectoryHistory(false);

        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document_history_activity, menu);
        SearchManager searchManager = (SearchManager) DocumentActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(DocumentActivity.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search ...");
        ImageView searchMagIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchMagIcon.setImageResource(R.drawable.ic_close_white_24dp);
        setUpSearchObservable(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    if (pathModels.size() > 1) {
                        String id = pathModels.get(pathModels.size() - 2).getId();
                        Intent intent = new Intent(DocumentActivity.this, DocumentActivity.class);
                        intent.putExtra("name", "" + pathModels.get(pathModels.size() - 2).getResource_title());
                        intent.putExtra("id", "" + id);
                        startActivity(intent);
                    }
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.filter:
                try {
                    bottomSheetFragment = new DocumentFilterBottomSheetFragment();
                    Bundle b = new Bundle();
                    b.putString("sort", sort);
                    bottomSheetFragment.setArguments(b);
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            try {
                if (pathModels.size() > 1) {
                    String id = pathModels.get(pathModels.size() - 2).getId();
                    Intent intent = new Intent(DocumentActivity.this, DocumentActivity.class);
                    intent.putExtra("name", "" + pathModels.get(pathModels.size() - 2).getResource_title());
                    intent.putExtra("id", "" + id);
                    startActivity(intent);
                }
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri ur = data.getData();
            try {
                mimeType = getMimeType(ur);
                String PathHolder = PathUtil.getPath(DocumentActivity.this, ur);
                file = new File(PathHolder);
                if (file != null) {
                    uploadFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = DocumentActivity.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void getDirectoryHistory(final boolean refresh_flag) {
        try {
            final ProgressDialog pd = new ProgressDialog(DocumentActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            if (refresh_flag) {
                pd.show();
            }
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getDocumentHistory(myPreferences.getPreferences(MyPreferences.id), id);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (refresh_flag) {
                            pd.dismiss();
                        } else {
                            try {
                                swipeRefreshLayout.setRefreshing(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            data.clear();
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                DocumentModel da = new DocumentModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("resource_title"));
                                da.setType("" + c.getString("resource_type"));
                                da.setVisibility(c.getString("visibility"));
                                da.setResource_media_url(c.getString("resource_media_full_url"));
                                data.add(da);
                            }
                            adapter.notifyDataSetChanged();
                            nested.setVisibility(View.VISIBLE);

                            pathModels.clear();
                            if (json.getString("roots").equals("")) {
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                            } else {
                                JSONObject roots = json.getJSONObject("roots");
                                JSONArray parents = roots.getJSONArray("parents");
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                                for (int i = 0; i < parents.length(); i++) {
                                    JSONObject c = parents.getJSONObject(i);
                                    da = new DrivePathModel();
                                    da.setId(c.getString("id"));
                                    da.setResource_title(c.getString("resource_title"));
                                    pathModels.add(da);
                                }
                            }

                            pathAdapter = new DrivePathAdapter(DocumentActivity.this, pathModels);
                            recycleViewDrivePath.setAdapter(pathAdapter);
                            recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        } else {
                            Toaster.show(DocumentActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);

                            pathModels.clear();
                            if (json.getString("roots").equals("")) {
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                            } else {
                                JSONObject roots = json.getJSONObject("roots");
                                JSONArray parents = roots.getJSONArray("parents");
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                                for (int i = 0; i < parents.length(); i++) {
                                    JSONObject c = parents.getJSONObject(i);
                                    da = new DrivePathModel();
                                    da.setId(c.getString("id"));
                                    da.setResource_title(c.getString("resource_title"));
                                    pathModels.add(da);
                                }
                            }
                            pathAdapter = new DrivePathAdapter(DocumentActivity.this, pathModels);
                            recycleViewDrivePath.setAdapter(pathAdapter);
                            recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        }

                        try {
                            TapIntroHelper.showDocumentIntro(DocumentActivity.this, ContextCompat.getColor(DocumentActivity.this, R.color.colorPrimary));
                        } catch (Exception e) {
                            e.printStackTrace();
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

    @Override
    public void createFolder() {
        try {
            AlertDialog buildInfosDialog;
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(DocumentActivity.this);
            alertDialog2.setTitle("New Folder");

            LayoutInflater inflater = DocumentActivity.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_new_folder, null);
            alertDialog2.setView(dialogView);

            final EditText newFolderEdt = (EditText) dialogView.findViewById(R.id.new_folder_edt);

            alertDialog2.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            try {
                                if (GlobalElements.isConnectingToInternet(DocumentActivity.this)) {
                                    createDirectory(newFolderEdt.getText().toString());
                                } else {
                                    GlobalElements.showDialog(DocumentActivity.this);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

            alertDialog2.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog

                        }
                    });

            buildInfosDialog = alertDialog2.create();
            buildInfosDialog.show();
            buildInfosDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(DocumentActivity.this, R.color.colorPrimary));
            buildInfosDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DocumentActivity.this, R.color.colorPrimary));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void UploadFile() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDirectory(final String name) {
        try {
            final ProgressDialog pd = new ProgressDialog(DocumentActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.createDirectory(myPreferences.getPreferences(MyPreferences.id), "" + name, "0", id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            DocumentModel da = new DocumentModel();
                            da.setId("" + result.getString("id"));
                            da.setName("" + result.getString("resource_title"));
                            da.setType("" + result.getString("resource_type"));
                            da.setVisibility(result.getString("visibility"));
                            da.setResource_media_url(result.getString("resource_media_full_url"));
                            data.add(da);


                            Collections.sort(data, new Comparator<DocumentModel>() {
                                @Override
                                public int compare(DocumentModel p1, DocumentModel p2) {
                                    // return p1.age+"".compareTo(p2.age+""); //sort by age
                                    return p2.getType().compareTo(p1.getType()); // if you want to short by name
                                }
                            });
                            Collections.reverse(data);
                            adapter.notifyDataSetChanged();

                        } else {
                            Toaster.show(DocumentActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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

    private void uploadFile() {
        try {
            final ProgressDialog pd = new ProgressDialog(DocumentActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            RequestBody requestfile_image;
            MultipartBody.Part body_image;
            Call<ResponseBody> call;
            requestfile_image = RequestBody.create(MediaType.parse("" + mimeType), file);
            body_image = MultipartBody.Part.createFormData("resource_media", file.getName(), requestfile_image);
            call = request.uploadFile(myPreferences.getPreferences(MyPreferences.id), "1", id, body_image);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONObject result = json.getJSONObject("result");
                            DocumentModel da = new DocumentModel();
                            da.setId("" + result.getString("id"));
                            da.setName("" + result.getString("resource_title"));
                            da.setType("" + result.getString("resource_type"));
                            da.setVisibility(result.getString("visibility"));
                            da.setResource_media_url(result.getString("resource_media_full_url"));
                            data.add(da);
                            Collections.sort(data, new Comparator<DocumentModel>() {
                                @Override
                                public int compare(DocumentModel p1, DocumentModel p2) {
                                    // return p1.age+"".compareTo(p2.age+""); //sort by age
                                    return p2.getType().compareTo(p1.getType()); // if you want to short by name
                                }
                            });
                            Collections.reverse(data);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toaster.show(DocumentActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toaster.show(DocumentActivity.this, "" + t.getLocalizedMessage(), false, Toaster.DANGER);
                    pd.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeDrive(int position) {
        try {
            Intent intent = new Intent(DocumentActivity.this, DocumentActivity.class);
            intent.putExtra("name", "" + pathModels.get(position).getResource_title());
            intent.putExtra("id", "" + pathModels.get(position).getId());
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void clickFolder(int position) {
        try {
            Intent intent = new Intent(DocumentActivity.this, DocumentActivity.class);
            intent.putExtra("name", "" + data.get(position).getName());
            intent.putExtra("id", "" + data.get(position).getId());
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void setUpSearchObservable(SearchView search) {
        RxSearchObservable.fromView(search)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        if (text.isEmpty()) {
                            return true;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .switchMap(new Function<String, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(String query) throws Exception {
                        return dataFromNetwork(query);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            System.out.print("" + responseBody.byteStream());
                            String json_response = null;
                            try {
                                json_response = responseBody.string();
                                JSONObject json = new JSONObject(json_response);
                                data.clear();
                                if (json.getInt("ack") == 1) {
                                    data.clear();
                                    JSONArray result = json.getJSONArray("result");
                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject c = result.getJSONObject(i);
                                        DocumentModel da = new DocumentModel();
                                        da.setId("" + c.getString("id"));
                                        da.setName("" + c.getString("resource_title"));
                                        da.setType("" + c.getString("resource_type"));
                                        da.setVisibility(c.getString("visibility"));
                                        da.setResource_media_url(c.getString("resource_media_full_url"));
                                        data.add(da);
                                    }
                                    adapter.notifyDataSetChanged();
                                    nested.setVisibility(View.VISIBLE);

                                    pathModels.clear();
                                    if (json.getString("roots").equals("")) {
                                        DrivePathModel da = new DrivePathModel();
                                        da.setId("0");
                                        da.setResource_title("Drive");
                                        pathModels.add(da);
                                    } else {
                                        JSONObject roots = json.getJSONObject("roots");
                                        JSONArray parents = roots.getJSONArray("parents");
                                        DrivePathModel da = new DrivePathModel();
                                        da.setId("0");
                                        da.setResource_title("Drive");
                                        pathModels.add(da);
                                        for (int i = 0; i < parents.length(); i++) {
                                            JSONObject c = parents.getJSONObject(i);
                                            da = new DrivePathModel();
                                            da.setId(c.getString("id"));
                                            da.setResource_title(c.getString("resource_title"));
                                            pathModels.add(da);
                                        }
                                    }

                                    pathAdapter = new DrivePathAdapter(DocumentActivity.this, pathModels);
                                    recycleViewDrivePath.setAdapter(pathAdapter);
                                    recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));

                                } else {
                                    adapter.notifyDataSetChanged();
                                    nested.setVisibility(View.VISIBLE);


                                    Toaster.show(DocumentActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);

                                    pathModels.clear();
                                    if (json.getString("roots").equals("")) {
                                        DrivePathModel da = new DrivePathModel();
                                        da.setId("0");
                                        da.setResource_title("Drive");
                                        pathModels.add(da);
                                    } else {
                                        JSONObject roots = json.getJSONObject("roots");
                                        JSONArray parents = roots.getJSONArray("parents");
                                        DrivePathModel da = new DrivePathModel();
                                        da.setId("0");
                                        da.setResource_title("Drive");
                                        pathModels.add(da);
                                        for (int i = 0; i < parents.length(); i++) {
                                            JSONObject c = parents.getJSONObject(i);
                                            da = new DrivePathModel();
                                            da.setId(c.getString("id"));
                                            da.setResource_title(c.getString("resource_title"));
                                            pathModels.add(da);
                                        }
                                    }
                                    pathAdapter = new DrivePathAdapter(DocumentActivity.this, pathModels);
                                    recycleViewDrivePath.setAdapter(pathAdapter);
                                    recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", "" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("compate", "");
                    }
                });
    }

    private Observable<ResponseBody> dataFromNetwork(final String query) {
        RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
        return request.searchDocumentHistory(myPreferences.getPreferences(MyPreferences.id), id, sort, query);
    }

    @Override
    public void shortFilter(String sort) {
        try {
            this.sort = sort;
            bottomSheetFragment.dismiss();
            getSortDirectoryHistory(sort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSortDirectoryHistory(String sort) {
        try {
            final ProgressDialog pd = new ProgressDialog(DocumentActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getDocumentHistory(myPreferences.getPreferences(MyPreferences.id), id, sort, "");

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        data.clear();
                        if (json.getInt("ack") == 1) {
                            data.clear();
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                DocumentModel da = new DocumentModel();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("resource_title"));
                                da.setType("" + c.getString("resource_type"));
                                da.setVisibility(c.getString("visibility"));
                                da.setResource_media_url(c.getString("resource_media_full_url"));
                                data.add(da);
                            }
                            adapter.notifyDataSetChanged();
                            nested.setVisibility(View.VISIBLE);

                            pathModels.clear();
                            if (json.getString("roots").equals("")) {
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                            } else {
                                JSONObject roots = json.getJSONObject("roots");
                                JSONArray parents = roots.getJSONArray("parents");
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                                for (int i = 0; i < parents.length(); i++) {
                                    JSONObject c = parents.getJSONObject(i);
                                    da = new DrivePathModel();
                                    da.setId(c.getString("id"));
                                    da.setResource_title(c.getString("resource_title"));
                                    pathModels.add(da);
                                }
                            }

                            pathAdapter = new DrivePathAdapter(DocumentActivity.this, pathModels);
                            recycleViewDrivePath.setAdapter(pathAdapter);
                            recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        } else {
                            adapter.notifyDataSetChanged();
                            nested.setVisibility(View.VISIBLE);

                            Toaster.show(DocumentActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
                            pathModels.clear();
                            if (json.getString("roots").equals("")) {
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                            } else {
                                JSONObject roots = json.getJSONObject("roots");
                                JSONArray parents = roots.getJSONArray("parents");
                                DrivePathModel da = new DrivePathModel();
                                da.setId("0");
                                da.setResource_title("Drive");
                                pathModels.add(da);
                                for (int i = 0; i < parents.length(); i++) {
                                    JSONObject c = parents.getJSONObject(i);
                                    da = new DrivePathModel();
                                    da.setId(c.getString("id"));
                                    da.setResource_title(c.getString("resource_title"));
                                    pathModels.add(da);
                                }
                            }
                            pathAdapter = new DrivePathAdapter(DocumentActivity.this, pathModels);
                            recycleViewDrivePath.setAdapter(pathAdapter);
                            recycleViewDrivePath.setLayoutManager(new LinearLayoutManager(DocumentActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        }

                        try {
                            TapIntroHelper.showDocumentIntro(DocumentActivity.this, ContextCompat.getColor(DocumentActivity.this, R.color.colorPrimary));
                        } catch (Exception e) {
                            e.printStackTrace();
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
}
