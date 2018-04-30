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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gimbal.android.CommunicationManager;
import com.gimbal.android.PlaceManager;

public class OptInActivity extends Activity {
    LocationPermissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optin);
        Log.e("MS-OptInActivity", "onCreate");
    }

    public void onEnableClicked(View view) {
        GimbalDAO.setOptInShown(getApplicationContext());
        enablePlaceMonitoring();
        enableCommunications();
        Log.e("MS-OptInActivity", "onEnableClicked");
    }

    private void enablePlaceMonitoring(){
        permissions = new LocationPermissions(this);
        permissions.checkAndRequestPermission();
        Log.e("MS-OptInActivity", "enablePlaceMonitoring");
    }

    public void onNotNowClicked(View view) {
        GimbalDAO.setOptInShown(getApplicationContext());
        PlaceManager.getInstance().stopMonitoring();
        finish();
        Log.e("MS-OptInActivity", "onNotNowClicked");
    }

    public void onPrivacyPolicyClicked(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://your-privacy-policy-url")));
        Log.e("MS-OptInActivity", "onPrivacyPolicyClicked");
    }

    public void onTermsOfServiceClicked(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://your-terms-of-use-url")));
        Log.e("MS-OptInActivity", "onTermsOfServiceClicked");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        this.permissions.onRequestPermissionResult(requestCode, permissions, grantResults);
        Log.e("MS-OptInActivity", "onRequestPermissionsResult");
    }

    private void enableCommunications() {
        CommunicationManager.getInstance().startReceivingCommunications();
        PushRegistrationHelper.registerForPush();
        Log.e("MS-OptInActivity", "enableCommunications");
    }

}
