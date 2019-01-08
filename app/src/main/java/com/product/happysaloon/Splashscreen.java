package com.product.happysaloon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.product.happysaloon.authentication.LoginActivity;
import com.product.happysaloon.firebase.Config;
import com.product.happysaloon.firebase.NotificationUtils;
import com.product.happysaloon.utils.Constant;
import com.product.happysaloon.utils.SharedPref;

public class Splashscreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPref mSharedPref;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        init();
    }

    private void init(){
        mSharedPref = new SharedPref(Splashscreen.this);
        registerFCM();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                /*mSharedPref.putString(Constant.Login_success,"1");
                mSharedPref.putString(Constant.Login_type,"2");*/
                if(!Constant.isEmptyString(mSharedPref.getString(Constant.Login_success))){
                    if(mSharedPref.getString(Constant.Login_success).equals("1")){
                        Intent i = new Intent(Splashscreen.this, Home_Activity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(Splashscreen.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else{
                    Intent i = new Intent(Splashscreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void registerFCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        Log.e("FCMID reg id:", "#" + mSharedPref.getString(Constant.FCMID));
    }

    @Override
    protected void onResume() {
        super.onResume();


        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
