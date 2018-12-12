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
import com.innovent.erp.custom.DividerItemDecoration;
import com.innovent.erp.custom.Toaster;
import com.innovent.erp.music.adapter.MusicListAdapter;
import com.innovent.erp.music.adapter.SubCategoryAdapter;
import com.innovent.erp.music.custom.AppConstant;
import com.innovent.erp.music.custom.Util;
import com.innovent.erp.music.fragments.MusicDetailFragment;
import com.innovent.erp.music.model.Music;
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

public class MusicListActivity extends BaseActivity implements MusicDetailFragment.OnMusicServiceUpdated {

    @BindView(R.id.rv_music)
    RecyclerView rvMusic;
    @BindView(R.id.layout_play_music)
    ContentFrameLayout layoutPlayMusic;
    int subCategoryPosition;
    String category_id, sub_id;
    FragmentManager fm = getSupportFragmentManager();
    public static Fragment newFragment;
    MusicListAdapter viewVisitorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            category_id = getIntent().getStringExtra("category_id");
            subCategoryPosition = getIntent().getIntExtra(AppConstant.SUB_CATEGORY, 0);
            String title = backgroundMusicService.subCategories.get(subCategoryPosition).getName();
            sub_id = backgroundMusicService.subCategories.get(subCategoryPosition).getId();
            if (!title.equals(""))
                getSupportActionBar().setTitle(title);

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backgroundMusicService.musicArrayList = new ArrayList<>();
        if (!GlobalElements.isConnectingToInternet(MusicListActivity.this)) {
            GlobalElements.showDialog(MusicListActivity.this);
        } else {

            viewVisitorAdapter = new MusicListAdapter(MusicListActivity.this, backgroundMusicService.musicArrayList);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(MusicListActivity.this);
            rvMusic.addItemDecoration(itemDecoration);
            rvMusic.setLayoutManager(new LinearLayoutManager(MusicListActivity.this, LinearLayoutManager.VERTICAL, false));
            if (backgroundMusicService.musicArrayList.size() != 0) {
                if (backgroundMusicService.subCategories.get(subCategoryPosition).getId().equals("1")) {
                    rvMusic.setAdapter(viewVisitorAdapter);
                }
            }

            getMusicList();

            /*Music music = new Music("1", "1", "song 1", url,
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
            Music music1 = new Music("2", "1", "song 2", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3");
            Music music2 = new Music("3", "1", "song 3", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3");
            Music music3 = new Music("4", "1", "song 4", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3");
            Music music4 = new Music("5", "1", "song 5", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3");
            Music music5 = new Music("6", "1", "song 6", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3");
            Music music6 = new Music("7", "1", "song 7", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3");
            Music music7 = new Music("8", "1", "song 8", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3");
            Music music8 = new Music("9", "1", "song 9", url, "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3");
            backgroundMusicService.musicArrayList.add(music);
            backgroundMusicService.musicArrayList.add(music1);
            backgroundMusicService.musicArrayList.add(music2);
            backgroundMusicService.musicArrayList.add(music3);
            backgroundMusicService.musicArrayList.add(music4);
            backgroundMusicService.musicArrayList.add(music5);
            backgroundMusicService.musicArrayList.add(music6);
            backgroundMusicService.musicArrayList.add(music7);
            backgroundMusicService.musicArrayList.add(music8);

            viewVisitorAdapter = new MusicListAdapter(MusicListActivity.this, backgroundMusicService.musicArrayList);
            rvMusic.setLayoutManager(new LinearLayoutManager(MusicListActivity.this, LinearLayoutManager.VERTICAL, false));
            if (backgroundMusicService.musicArrayList.size() != 0) {
                if (backgroundMusicService.subCategories.get(subCategoryPosition).getId().equals("1")) {
                    rvMusic.setAdapter(viewVisitorAdapter);
                }
            }
            Logger.LogInfo("on create activty musiclist");*/
        }
    }

    @Override
    protected void onResume() {
        Logger.LogInfo("on resume activty musiclist");
        super.onResume();
        newFragment = Util.UpdatePlayerLayout(MusicListActivity.this, layoutPlayMusic, fm, newFragment);
        viewVisitorAdapter.notifyDataSetChanged();
    }


    @Override
    public void OnStopMusic() {
        newFragment = Util.UpdatePlayerLayout(MusicListActivity.this, layoutPlayMusic, fm, newFragment);
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
        viewVisitorAdapter.notifyDataSetChanged();
    }

    private void getMusicList() {
        try {
            final ProgressDialog pd = new ProgressDialog(MusicListActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Loading");
            pd.setCancelable(true);
            pd.show();
            RequestInterface request = RetrofitClient.getClient().create(RequestInterface.class);
            Call<ResponseBody> call = request.getMusicList("" + category_id, sub_id);

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
                                Music da = new Music();
                                da.setId(c.getString("id"));
                                da.setName(c.getString("title"));
                                da.setSubCategoryId(c.getString("sid"));
                                da.setImageUrl("" + c.getString("image_path"));
                                da.setMusicUrl(c.getString("mp3_file"));
                                da.setDesc(c.getString("music_detail"));
                                backgroundMusicService.musicArrayList.add(da);
                            }
                            viewVisitorAdapter = new MusicListAdapter(MusicListActivity.this, backgroundMusicService.musicArrayList);
                            rvMusic.setLayoutManager(new LinearLayoutManager(MusicListActivity.this, LinearLayoutManager.VERTICAL, false));
                            if (backgroundMusicService.musicArrayList.size() != 0) {
                                /*if (backgroundMusicService.subCategories.get(subCategoryPosition).getId().equals("1")) {
                                    rvMusic.setAdapter(viewVisitorAdapter);
                                }*/
                                rvMusic.setAdapter(viewVisitorAdapter);
                            }
                        } else {
                            Toaster.show(MusicListActivity.this, "" + json.getString("ack_msg"), false, Toaster.DANGER);
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
