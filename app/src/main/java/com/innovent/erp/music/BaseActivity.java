package com.innovent.erp.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.innovent.erp.GlobalElements;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalElements.activityCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalElements.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalElements.activityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalElements.activityDestroyed();
    }
}
