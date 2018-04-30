/**
 * Copyright (C) 2015 Gimbal, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of Gimbal, Inc.
 *
 * The following sample code illustrates various aspects of the Gimbal SDK.
 *
 * The sample code herein is provided for your convenience, and has not been
 * tested or designed to work on any particular system configuration. It is
 * provided AS IS and your use of this sample code, whether as provided or
 * with any modification, is at your own risk. Neither Gimbal, Inc.
 * nor any affiliate takes any liability nor responsibility with respect
 * to the sample code, and disclaims all warranties, express and
 * implied, including without limitation warranties on merchantability,
 * fitness for a specified purpose, and against infringement.
 */
package com.poc.gimbal.korcomptenz.gimbalpoc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class AppActivity extends AppCompatActivity{

    private GimbalEventReceiver gimbalEventReceiver;
    private GimbalEventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MS-AppActivity", "onCreate");
        startService(new Intent(this, AppService.class));

        if (GimbalDAO.showOptIn(getApplicationContext())) {
            Log.e("MS-AppActivity", "GimbalDAO.showOptIn()");
            startActivity(new Intent(this, OptInActivity.class));
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        adapter = new GimbalEventListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AppActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("MS-AppActivity", "onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MS-AppActivity", "onResume");
        adapter.setEvents(GimbalDAO.getEvents(getApplicationContext()));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MS-AppActivity", "onStart");
        gimbalEventReceiver = new GimbalEventReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GimbalDAO.GIMBAL_NEW_EVENT_ACTION);
        intentFilter.addAction(AppService.APPSERVICE_STARTED_ACTION);
        registerReceiver(gimbalEventReceiver, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
       unregisterReceiver(gimbalEventReceiver);
      //  Log.e("MS-AppActivity", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
       // Log.e("MS-AppActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // Log.e("MS-AppActivity", "onDestroy");
      //  unregisterReceiver(gimbalEventReceiver);
    }

    // --------------------
    // EVENT RECEIVER
    // --------------------

    class GimbalEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MS-AppActivity", "GimbalEventReceiver");
            if (intent.getAction() != null) {
                if (intent.getAction().compareTo(GimbalDAO.GIMBAL_NEW_EVENT_ACTION) == 0) {
                    adapter.setEvents(GimbalDAO.getEvents(getApplicationContext()));
                    Log.e("MS-AppActivity", "GimbalEventReceiver GIMBAL_NEW_EVENT_ACTION");
                }
            }
        }
    }

    // --------------------
    // SETTINGS MENU
    // --------------------

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Log.e("MS-AppActivity", "onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            Log.e("MS-AppActivity", "onOptionsItemSelected");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
}
