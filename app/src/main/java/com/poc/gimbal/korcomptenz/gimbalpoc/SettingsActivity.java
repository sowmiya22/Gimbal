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


import android.app.Activity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceManager;

public class SettingsActivity extends Activity {

    private CheckBox gimbalMonitoringCheckBox;
    LocationPermissions locationPermissions;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        gimbalMonitoringCheckBox = (CheckBox) findViewById(R.id.gimbal_monitoring_checkbox);
        locationPermissions= new LocationPermissions(this);
        Log.e("MS-SettingsActivity", "onCreate");
    }

    public void onGimbalMonitoringClicked(View view) {
        gimbalMonitoringCheckBox.setChecked(!gimbalMonitoringCheckBox.isChecked());
        if (gimbalMonitoringCheckBox.isChecked()) {
            locationPermissions = new LocationPermissions(this);
            locationPermissions.checkAndRequestPermission();
            Log.e("MS-SettingsActivity", "isChecked");
        }
        else {
            Gimbal.stop();
            Log.e("MS-SettingsActivity", "Gimbal.stop()");
        }
    }

    public void onResetAppInstance(View view) {
        Gimbal.resetApplicationInstanceIdentifier();
        Log.e("MS-SettingsActivity", "onResetAppInstance");
        Toast.makeText(this, "App Instance ID reset successful", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        gimbalMonitoringCheckBox.setChecked(PlaceManager.getInstance().isMonitoring());
        Log.e("MS-SettingsActivity", "onResetAppInstance");

        Log.e("MS-SettingsActivity", "onResetAppInstance");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        locationPermissions.onRequestPermissionResult(requestCode, permissions, grantResults);
        Log.e("MS-SettingsActivity", "onRequestPermissionsResult");

    }

}
