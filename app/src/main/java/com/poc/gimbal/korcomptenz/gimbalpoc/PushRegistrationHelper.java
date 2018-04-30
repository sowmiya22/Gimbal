package com.poc.gimbal.korcomptenz.gimbalpoc;

import android.util.Log;

import com.gimbal.android.Gimbal;

public class PushRegistrationHelper {
    static String TAG = "PushRegistrat";
    public static void registerForPush() {


        Log.e("MS-"+TAG, "");
        // Setup Push Communication
        // sowmiatkor@gmail.com
        // kor@1234
        // https://console.firebase.google.com/project/korgimbalpocapp/settings/cloudmessaging/android:com.developer.sowmiya.korgimbalpoc


        // Gimbal SETUP
        // https://manager.gimbal.com/apps/37068/edit

//https://console.firebase.google.com/project/gimbalpoc-e16d4/settings/cloudmessaging/android:com.poc.gimbal.korcomptenz.gimbalpoc
        String gcmSenderId = "839777405216"; // <--- SET THIS STRING TO YOUR PUSH SENDER ID HERE (Google API project #) ##

        if (gcmSenderId != null) {
            Gimbal.registerForPush(gcmSenderId);
        }
    }
}
