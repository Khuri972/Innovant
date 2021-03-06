package com.calculator;/*
 * Copyright 2017 Phil Shadlyn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.calculator.fragments.ConversionFragment;
import com.calculator.models.Conversion;
import com.calculator.util.Conversions;


/**
 * Main activity
 * Created by Phizz on 15-07-28.
 */
public class CalCulateActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private DrawerLayout mDrawerLayout;
    private Conversions mConversions;

    //private MainActivityPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO - replace with Dagger2 injection
        //mPresenter = new MainActivityPresenter(this, this, Preferences.getInstance(this));
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        Preferences.getInstance(this).getPreferences().registerOnSharedPreferenceChangeListener(this);
        mConversions = Conversions.getInstance();

        // setup language
        // mPresenter.setLanguageToDisplay();

        setContentView(R.layout.activity_calculate);
        setupToolbar();
        setToolbarHomeNavigation(true);
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        int conversion = Preferences.getInstance(this).getLastConversion();
        setToolbarTitle(mConversions.getById(conversion).getLabelResource());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        setupDrawer(getMenuPositionOfConversion(conversion));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ConversionFragment.newInstance(conversion))
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Show help dialog if never seen before
        /*if (Preferences.getInstance(this).showHelp()) {
            HelpDialogFragment.newInstance().show(getSupportFragmentManager(), HelpDialogFragment.TAG);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Preferences.getInstance(this).getPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Setup navigation drawer
     *
     * @param state index of item to be selected initially
     */
    private void setupDrawer(int state) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.getMenu().getItem(state).setChecked(true);

        if (false) {
            navigationView.setItemBackgroundResource(R.drawable.navigation_item_background);
        } else {
            navigationView.setItemBackgroundResource(
                    R.drawable.navigation_item_background_light);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    /*case R.id.drawer_settings:
                        PreferencesActivity.start(CalCulateActivity.this);
                        return true;*/

                    default:
                        menuItem.setChecked(true);
                        int conversion = getConversionFromDrawer(menuItem.getItemId());
                        setToolbarTitle(mConversions.getById(conversion).getLabelResource());
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, ConversionFragment.newInstance(conversion))
                                .commit();
                        return true;
                }
            }
        });
    }

    @Conversion.id
    private int getConversionFromDrawer(int itemId) {
        if (itemId == R.id.drawer_area) {
            return Conversion.AREA;
        } else if (itemId == R.id.drawer_cooking) {
            return Conversion.COOKING;
        } else if (itemId == R.id.drawer_currency) {
            return Conversion.CURRENCY;
        } else if (itemId == R.id.drawer_storage) {
            return Conversion.STORAGE;
        } else if (itemId == R.id.drawer_energy) {
            return Conversion.ENERGY;
        } else if (itemId == R.id.drawer_fuel) {
            return Conversion.FUEL;
        } else if (itemId == R.id.drawer_length) {
            return Conversion.LENGTH;
        } else if (itemId == R.id.drawer_mass) {
            return Conversion.MASS;
        } else if (itemId == R.id.drawer_power) {
            return Conversion.POWER;
        } else if (itemId == R.id.drawer_pressure) {
            return Conversion.PRESSURE;
        } else if (itemId == R.id.drawer_speed) {
            return Conversion.SPEED;
        } else if (itemId == R.id.drawer_temperature) {
            return Conversion.TEMPERATURE;
        } else if (itemId == R.id.drawer_time) {
            return Conversion.TIME;
        } else if (itemId == R.id.drawer_torque) {
            return Conversion.TORQUE;
        } else if (itemId == R.id.drawer_volume) {
            return Conversion.VOLUME;
        }

        return Conversion.AREA;
    }

    private int getMenuPositionOfConversion(@Conversion.id final int conversion) {
        switch (conversion) {
            case Conversion.AREA:
                return 0;
            case Conversion.COOKING:
                return 1;
            case Conversion.CURRENCY:
                return 2;
            case Conversion.STORAGE:
                return 3;
            case Conversion.ENERGY:
                return 4;
            case Conversion.FUEL:
                return 5;
            case Conversion.LENGTH:
                return 6;
            case Conversion.MASS:
                return 7;
            case Conversion.POWER:
                return 8;
            case Conversion.PRESSURE:
                return 9;
            case Conversion.SPEED:
                return 10;
            case Conversion.TEMPERATURE:
                return 11;
            case Conversion.TIME:
                return 12;
            case Conversion.TORQUE:
                return 13;
            case Conversion.VOLUME:
                return 14;
            default:
                return 0;
        }
    }

    private void hideKeyboard() {
        View v = getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Preferences.PREFS_THEME)) {
            recreate();
        }
        /*else if (key.equals(Preferences.PREFS_LANGUAGE)) {
            mPresenter.onLanguageChanged();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}