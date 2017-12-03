package com.example.thiscord;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;



public class MainActivity extends AppCompatActivity {

    Main_JoinState_Fragment mainFragment;
    Main_roomlist_Fragment mainFragment2;
    Main_unknown_Fragment mainFragment3;

    private TabLayout tabLayout;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private ViewPager viewPager;

    private DrawerLayout drawerLayout = null;
    private ActionBarDrawerToggle toggle = null;
    private boolean isDrawerOpened = false;

    SharedPreferences.Editor sharededitor;
    public SharedPreferences sharedPreferences;
    private String login_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_id", MODE_PRIVATE);
        login_user = sharedPreferences.getString("userid", "01012345678");
        sharededitor = sharedPreferences.edit();
        sharededitor.putString("userid", login_user);
        sharededitor.commit();

        System.out.println("유저 : " + login_user);

        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.app_name, R.string.app_name);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.main_menu_drawable);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout.addDrawerListener(toggle);


        tabLayout = (TabLayout)findViewById(R.id.main_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("사용자"));
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.main_menu_drawable));
        tabLayout.addTab(tabLayout.newTab().setText("채팅"));
        tabLayout.addTab(tabLayout.newTab().setText("음성 채팅"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.main_viewPager);

        mainFragment = new Main_JoinState_Fragment();
        mainFragment2 = new Main_roomlist_Fragment();
        mainFragment3 = new Main_unknown_Fragment();

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("포지션 : " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager manager){
            super(manager);
        }
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(position){
                case 0 : return mainFragment;
                case 1 : return mainFragment2;
                case 2 : return mainFragment3;
            }
            return null;
        }
    }




    @Override
    protected  void attachBaseContext(Context newBase){
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed(){
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
            ActivityCompat.finishAffinity(this);
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }
}
