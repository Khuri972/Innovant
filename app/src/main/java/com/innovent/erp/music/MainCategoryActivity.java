package com.innovent.erp.music;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.innovent.erp.CatalogueActivity;
import com.innovent.erp.GlobalElements;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.music.adapter.MainCategoryAdapter;
import com.innovent.erp.music.custom.Util;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.model.MainCategory;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;
import com.innovent.erp.netUtils.MyPreferences;
import com.innovent.erp.netUtils.RequestInterface;
import com.innovent.erp.netUtils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainCategoryActivity extends BaseActivity implements MusicDetailFragment.OnMusicServiceUpdated {

    @BindView(R.id.rv_main_category)
    RecyclerView rvMainCategory;
    @BindView(R.id.layout_play_music)
    ContentFrameLayout layoutPlayMusic;
    FragmentManager fm = getSupportFragmentManager();
    public static Fragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(this.getString(R.string.main_category_activity_title));
        backgroundMusicService.mainCategories = new ArrayList<>();
        if (!GlobalElements.isConnectingToInternet(MainCategoryActivity.this)) {
            GlobalElements.showDialog(MainCategoryActivity.this);
        } else {
            
            getCategory();

            /*MainCategory mainCategory = new MainCategory("1"
                    , "category1"
                    , "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory1 = new MainCategory("2", "category2", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory2 = new MainCategory("3", "category3", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory3 = new MainCategory("4", "category4", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory4 = new MainCategory("5", "category5", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory5 = new MainCategory("6", "category6", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory6 = new MainCategory("7", "category7", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory7 = new MainCategory("8", "category8", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory8 = new MainCategory("9", "category9", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory9 = new MainCategory("10", "category10", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory10 = new MainCategory("11", "category11", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory11 = new MainCategory("12", "category12", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory12 = new MainCategory("13", "category13", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory13 = new MainCategory("14", "category14", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory14 = new MainCategory("15", "category15", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");
            MainCategory mainCategory15 = new MainCategory("16", "category16", "http://www.iitg.ac.in/stud/abhijit.saha/link.jpg");

            backgroundMusicService.mainCategories.add(mainCategory);
            backgroundMusicService.mainCategories.add(mainCategory1);
            backgroundMusicService.mainCategories.add(mainCategory2);
            backgroundMusicService.mainCategories.add(mainCategory3);
            backgroundMusicService.mainCategories.add(mainCategory4);
            backgroundMusicService.mainCategories.add(mainCategory5);
            backgroundMusicService.mainCategories.add(mainCategory6);
            backgroundMusicService.mainCategories.add(mainCategory7);
            backgroundMusicService.mainCategories.add(mainCategory8);
            backgroundMusicService.mainCategories.add(mainCategory9);
            backgroundMusicService.mainCategories.add(mainCategory10);
            backgroundMusicService.mainCategories.add(mainCategory11);
            backgroundMusicService.mainCategories.add(mainCategory12);
            backgroundMusicService.mainCategories.add(mainCategory13);
            backgroundMusicService.mainCategories.add(mainCategory14);
            backgroundMusicService.mainCategories.add(mainCategory15);

            MainCategoryAdapter viewVisitorAdapter = new MainCategoryAdapter(MainCategoryActivity.this, backgroundMusicService.mainCategories);
            rvMainCategory.setLayoutManager(new LinearLayoutManager(MainCategoryActivity.this));
            rvMainCategory.setAdapter(viewVisitorAdapter);
            newFragment = Util.UpdatePlayerLayout(MainCategoryActivity.this, layoutPlayMusic, fm, newFragment);*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        newFragment = Util.UpdatePlayerLayout(MainCategoryActivity.this, layoutPlayMusic, fm, newFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateView() {

    }

    @Override
    public void OnStopMusic() {
        newFragment = Util.UpdatePlayerLayout(MainCategoryActivity.this, layoutPlayMusic, fm, newFragment);
    }

    private void getCategory() {
        try {
            final ProgressDialog pd = new ProgressDialog(MainCategoryActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getMusicCategory();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        pd.dismiss();
                        String json_response = response.body().string();
                        JSONObject json = new JSONObject(json_response);
                        if (json.getInt("ack") == 1) {
                            JSONArray result = json.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject c = result.getJSONObject(i);
                                MainCategory mainCategory = new MainCategory();
                                mainCategory.setId("" + c.getString("id"));
                                mainCategory.setName("" + c.getString("title"));
                                mainCategory.setImageUrl("" + c.getString("image_path"));
                                backgroundMusicService.mainCategories.add(mainCategory);
                            }
                            MainCategoryAdapter viewVisitorAdapter = new MainCategoryAdapter(MainCategoryActivity.this, backgroundMusicService.mainCategories);
                            rvMainCategory.setLayoutManager(new LinearLayoutManager(MainCategoryActivity.this));
                            rvMainCategory.setAdapter(viewVisitorAdapter);
                            newFragment = Util.UpdatePlayerLayout(MainCategoryActivity.this, layoutPlayMusic, fm, newFragment);
                        } else {
                            Toaster.show(MainCategoryActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
