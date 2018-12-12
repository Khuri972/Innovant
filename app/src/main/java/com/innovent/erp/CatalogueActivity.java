package com.innovent.erp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovent.erp.adapter.CatalogueAdapter;
import com.innovent.erp.adapter.ChequeHistoryAdapter;
import com.innovent.erp.custom.RxSearchObservable;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.dialog.DateFilterDialog;
import com.innovent.erp.model.CatalogueModel;
import com.innovent.erp.model.ChequeModel;
import com.innovent.erp.model.DocumentModel;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogueActivity extends AppCompatActivity implements DateFilterDialog.InterfaceCommunicator {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    ArrayList<CatalogueModel> data = new ArrayList<>();
    CatalogueAdapter adapter;
    MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        ButterKnife.bind(this);
        myPreferences = new MyPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Catalog List");

        if (GlobalElements.isConnectingToInternet(CatalogueActivity.this)) {
            getCatalogue("", "");
        } else {
            GlobalElements.showDialog(CatalogueActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.filter:
                try {
                    DateFilterDialog dialog = DateFilterDialog.newInstance(CatalogueActivity.this);
                    dialog.show(getSupportFragmentManager(), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalogue_activity, menu);
        SearchManager searchManager = (SearchManager) CatalogueActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(CatalogueActivity.this.getComponentName()));
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

    private void getCatalogue(String toDate, String fromDate) {
        try {
            final ProgressDialog pd = new ProgressDialog(CatalogueActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call;

            if (toDate.equals("") && fromDate.equals("")) {
                call = request.getCatalogue(myPreferences.getPreferences(MyPreferences.id));
            } else {
                call = request.getCatalogue(myPreferences.getPreferences(MyPreferences.id), toDate, fromDate);
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        getResult(json_response);
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

    private void setUpSearchObservable(SearchView search) {
        RxSearchObservable.fromView(search)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        if (text.isEmpty()) {
                            //textViewResult.setText("");
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
                                getResult(json_response);
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
        return request.getCatalogue(myPreferences.getPreferences(MyPreferences.id), query);
    }

    @Override
    public void filterDate(String toDate, String fromDate) {
        if (GlobalElements.isConnectingToInternet(CatalogueActivity.this)) {
            getCatalogue(toDate, fromDate);
        } else {
            GlobalElements.showDialog(CatalogueActivity.this);
        }
    }

    public void getResult(String json_response) {
        try {
            JSONObject json = new JSONObject(json_response);
            data.clear();
            if (json.getInt("ack") == 1) {
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject c = result.getJSONObject(i);
                    CatalogueModel da = new CatalogueModel();
                    da.setId(c.getString("id"));
                    da.setTitle(c.getString("title"));
                    File folder = new File(c.getString("pdf_file"));
                    da.setFile_name("" + folder.getName());
                    da.setFilePath(c.getString("pdf_file"));
                    da.setDate(c.getString("created_date"));
                    data.add(da);
                }
                adapter = new CatalogueAdapter(CatalogueActivity.this, data);
                recycleview.setAdapter(adapter);
                recycleview.setLayoutManager(new LinearLayoutManager(CatalogueActivity.this, LinearLayoutManager.VERTICAL, false));
                recycleview.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            } else {
                recycleview.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyText.setText("" + json.getString("ack_msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
