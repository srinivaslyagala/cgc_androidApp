package com.rolvatech.cgc.activities;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.rolvatech.cgc.adapters.Pager;
import com.rolvatech.cgc.R;

public class ChildActivity extends CgcBaseActivity implements TabLayout.OnTabSelectedListener {
    ViewPager viewPager;

    @Override
    public void initialize() {
        setContentView(R.layout.activity_child);

        viewPager = findViewById(R.id.view_pager);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("About Child"));
        tabs.addTab(tabs.newTab().setText("Task List"));
        tabs.addTab(tabs.newTab().setText("Reports"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(viewPager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(adapter);
        //Adding onTabSelectedListener to swipe views
        tabs.setOnTabSelectedListener(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}