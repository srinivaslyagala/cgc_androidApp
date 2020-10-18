package com.rolvatech.cgc.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rolvatech.cgc.R;
import com.rolvatech.cgc.fragments.AreaListFragment;
import com.rolvatech.cgc.fragments.ChangePasswordFragment;
import com.rolvatech.cgc.fragments.ChildListFragment;
import com.rolvatech.cgc.fragments.HomeFragment;
import com.rolvatech.cgc.fragments.MyProfileFragment;
import com.rolvatech.cgc.fragments.StaffListFragment;
import com.rolvatech.cgc.utils.PrefUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  //  private AppBarConfiguration mAppBarConfiguration;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;

    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_black_24dp));
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        Log.e("image", PrefUtils.getStringPreference(DashboardActivity.this, PrefUtils.PROFILE_IMAGE));
        Log.e("email", PrefUtils.getStringPreference(DashboardActivity.this, PrefUtils.EMAIL));
//        fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.name);
        TextView email = headerView.findViewById(R.id.email);
        CircleImageView profileImage = headerView.findViewById(R.id.profile_image);
        Bitmap bm = StringToBitMap(PrefUtils.getStringPreference(DashboardActivity.this, PrefUtils.PROFILE_IMAGE));
        profileImage.setImageBitmap(bm);
        Log.e("email", PrefUtils.getStringPreference(DashboardActivity.this, PrefUtils.USER_NAME));
        email.setText(PrefUtils.getStringPreference(DashboardActivity.this, PrefUtils.USER_NAME));
        name.setText(PrefUtils.getStringPreference(DashboardActivity.this, PrefUtils.FIRST_NAME) + " " + PrefUtils.getStringPreference(DashboardActivity.this, PrefUtils.LAST_NAME));

        fragment = new HomeFragment();
        loadView(fragment);

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public void loadView(Fragment fragment) {
        fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
            loadView(new HomeFragment());}
        else {
            super.onBackPressed();
            loadView(new HomeFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        drawer = findViewById(R.id.drawer_layout);

        if (id == R.id.nav_dashboard) {
            toolbar.setTitle("Home");
            fragment = new HomeFragment();
        } else if (id == R.id.nav_profile) {
            toolbar.setTitle("Profile");
            fragment = new MyProfileFragment();
        } else if (id == R.id.nav_staff) {
            toolbar.setTitle("Staff List");
            fragment = new StaffListFragment();
        } else if (id == R.id.nav_childlist) {
            toolbar.setTitle("Child List");
            fragment = new ChildListFragment();
        } else if (id == R.id.nav_areas) {
            toolbar.setTitle("Area List");
            fragment = new AreaListFragment();
        } else if (id == R.id.nav_change) {
            toolbar.setTitle("Change Password");
            fragment = new ChangePasswordFragment();
        } else if (id == R.id.nav_logout) {
            PrefUtils.clearPreference(getApplicationContext());
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            // if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            finish();
        }
        loadView(fragment);
        drawer.closeDrawers();
        return true;

    }
}
