package com.innovent.erp.music;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.innovent.erp.GlobalElements;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.music.adapter.MainCategoryAdapter;
import com.innovent.erp.music.adapter.SubCategoryAdapter;
import com.innovent.erp.music.custom.AppConstant;
import com.innovent.erp.music.custom.Util;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.model.MainCategory;
import com.innovent.erp.music.model.SubCategory;
import com.innovent.erp.music.service.backgroundMusicService;
import com.innovent.erp.R;
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

public class SubCategoryActivity extends BaseActivity implements MusicDetailFragment.OnMusicServiceUpdated {

    @BindView(R.id.rv_sub_category)
    RecyclerView rvSubCategory;
    @BindView(R.id.layout_play_music)
    ContentFrameLayout layoutPlayMusic;
    private int position = 0;
    String category_id;
    FragmentManager fm = getSupportFragmentManager();
    public static Fragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            position = getIntent().getIntExtra(AppConstant.MAIN_CATEGORY, 0);
            String title = backgroundMusicService.mainCategories.get(position).getName();
            category_id= backgroundMusicService.mainCategories.get(position).getId();
            if (!title.equals(""))
                getSupportActionBar().setTitle(title);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backgroundMusicService.subCategories = new ArrayList<>();
        newFragment = Util.UpdatePlayerLayout(SubCategoryActivity.this, layoutPlayMusic, fm, newFragment);
        if (!GlobalElements.isConnectingToInternet(SubCategoryActivity.this)) {
            GlobalElements.showDialog(SubCategoryActivity.this);
        } else {

            getSubCategory();

            /*SubCategory subCategory = new SubCategory("1", "1", "sub category 1", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory1 = new SubCategory("2", "1", "sub category 2", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory2 = new SubCategory("3", "1", "sub category 3", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory3 = new SubCategory("4", "1", "sub category 4", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory4 = new SubCategory("5", "2", "sub category 5", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory5 = new SubCategory("6", "3", "sub category 6", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory6 = new SubCategory("7", "4", "sub category 7", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory7 = new SubCategory("8", "5", "sub category 8", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory8 = new SubCategory("9", "6", "sub category 9", "http://www.nestsoft.com/images/services/android.jpeg");
            SubCategory subCategory9 = new SubCategory("10", "7", "sub category 10", "http://www.nestsoft.com/images/services/android.jpeg");
            backgroundMusicService.subCategories.add(subCategory);
            backgroundMusicService.subCategories.add(subCategory1);
            backgroundMusicService.subCategories.add(subCategory2);
            backgroundMusicService.subCategories.add(subCategory3);
            backgroundMusicService.subCategories.add(subCategory4);
            backgroundMusicService.subCategories.add(subCategory5);
            backgroundMusicService.subCategories.add(subCategory6);
            backgroundMusicService.subCategories.add(subCategory7);
            backgroundMusicService.subCategories.add(subCategory8);
            backgroundMusicService.subCategories.add(subCategory9);

            SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, backgroundMusicService.subCategories);
            rvSubCategory.setLayoutManager(new LinearLayoutManager(SubCategoryActivity.this));
            rvSubCategory.setAdapter(subCategoryAdapter);*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        newFragment = Util.UpdatePlayerLayout(SubCategoryActivity.this, layoutPlayMusic, fm, newFragment);
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
        newFragment = Util.UpdatePlayerLayout(SubCategoryActivity.this, layoutPlayMusic, fm, newFragment);
    }

    private void getSubCategory() {
        try {
            final ProgressDialog pd = new ProgressDialog(SubCategoryActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getMusicSubCategory("" + category_id);

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
                                SubCategory da = new SubCategory();
                                da.setId("" + c.getString("id"));
                                da.setName("" + c.getString("title"));
                                da.setCid("" + c.getString("cid"));
                                da.setImageUrl("" + c.getString("image_path"));
                                backgroundMusicService.subCategories.add(da);
                            }
                            SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(SubCategoryActivity.this, backgroundMusicService.subCategories, "" + category_id);
                            rvSubCategory.setLayoutManager(new LinearLayoutManager(SubCategoryActivity.this));
                            rvSubCategory.setAdapter(subCategoryAdapter);
                        } else {
                            Toaster.show(SubCategoryActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
