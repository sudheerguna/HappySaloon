package com.product.happysaloon;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.product.happysaloon.authentication.LoginActivity;
import com.product.happysaloon.menuslide.FragmentDrawerForMerchant;
import com.product.happysaloon.menuslide.FragmentDrawerForUser;
import com.product.happysaloon.menuslide.NavigationDrawerAdapter;
import com.product.happysaloon.menuslide.NavigationDrawerMerchant;
import com.product.happysaloon.menuslide.UserHomeFragment;
import com.product.happysaloon.shopowner.DataUpdate;
import com.product.happysaloon.shopowner.EditDetails;
import com.product.happysaloon.shopowner.ShopOwnerFragment;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;

public class Home_Activity extends AppCompatActivity implements FragmentDrawerForUser.FragmentDrawerListener,FragmentDrawerForMerchant.FragmentDrawerListener {
    SharedPref mSharedPref;
    private FragmentDrawerForUser drawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        mSharedPref = new SharedPref(this);

        if (!Constant.isEmptyString(mSharedPref.getString(Constant.Login_type))) {
            if (mSharedPref.getString(Constant.Login_type).equals("1")) {
                setuserUI();
            }else if (mSharedPref.getString(Constant.Login_type).equals("2")) {
                setmerchantUI();
            }
        }
    }

    private void setuserUI() {
        setContentView(R.layout.activity_home);
        init_toolbar();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawerForUser)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        drawerFragment.txt_user.setText(mSharedPref.getString(Constant.UserName));
        displayView(0);
    }

    private void setmerchantUI(){
        setContentView(R.layout.activity_main_merchant);
        init_toolbar();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FragmentDrawerForMerchant  drawerFragment = (FragmentDrawerForMerchant)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_merchant);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_merchant, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        drawerFragment.txt_user.setText(mSharedPref.getString(Constant.UserName));
        displayView(0);
    }

    private void init_toolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
    }

    @Override
    public void onDrawerItemSelected(View view, int position, NavigationDrawerAdapter adapter) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        if (mSharedPref.getString(Constant.Login_type).equals("1")) {
            switch (position) {
                case 0:
                    fragment = new UserHomeFragment();
                    title = getString(R.string.home);
                    break;
                case 1:
                    startActivity(new Intent(Home_Activity.this,userProfileActivity.class));
                    break;
                case 2:
                    Logout();
                    break;
            }
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                // set the toolbar title
                getSupportActionBar().setTitle(title);

                this.getSupportActionBar().setDisplayShowCustomEnabled(true);
                this.getSupportActionBar().setDisplayShowTitleEnabled(false);
                LayoutInflater inflator = LayoutInflater.from(this);
                View v = inflator.inflate(R.layout.titleview, null);
                ((TextView) v.findViewById(R.id.txt_title)).setText(title);
                getSupportActionBar().setCustomView(v);
            }
        }else if (mSharedPref.getString(Constant.Login_type).equals("2")) {
            switch (position) {
                case 0:
                    fragment = new ShopOwnerFragment();
                    title = getString(R.string.home);
                    break;
                case 1:
                    startActivity(new Intent(Home_Activity.this, EditDetails.class));
                    break;
                case 2:
                    Logout();
                    break;
            }
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                // set the toolbar title
                getSupportActionBar().setTitle(title);

                this.getSupportActionBar().setDisplayShowCustomEnabled(true);
                this.getSupportActionBar().setDisplayShowTitleEnabled(false);
                LayoutInflater inflator = LayoutInflater.from(this);
                View v = inflator.inflate(R.layout.titleview, null);
                ((TextView) v.findViewById(R.id.txt_title)).setText(title);
                getSupportActionBar().setCustomView(v);
            }

        }
    }

    private void Logout() {
        mSharedPref.clear();
        Intent intent = new Intent(Home_Activity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDrawerItemSelected(View view, int position, NavigationDrawerMerchant adapter) {
        displayView(position);
    }
}
