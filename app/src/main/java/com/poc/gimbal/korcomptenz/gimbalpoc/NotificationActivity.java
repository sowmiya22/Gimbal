package com.poc.gimbal.korcomptenz.gimbalpoc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Created by MadeshS on 1/23/2018.
 */

public class NotificationActivity extends AppCompatActivity {
    TextView txt_notification, txt_summary;
    String messageBody = "", summary="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        txt_notification =(TextView) findViewById(R.id.txt_notification);
        txt_summary =(TextView) findViewById(R.id.txt_summary);
        SharedPreferences sharedpreferences = getSharedPreferences("notifyPref", Context.MODE_PRIVATE);

        messageBody = sharedpreferences.getString("messageBody", "");
        txt_notification.setText(""+messageBody);

        String entryDataDelay = ""+sharedpreferences.getString("entryDataDelay", "");

            summary = "Visit Summary\n"
                    +sharedpreferences.getString("entryData", "") +"\n"
                    +sharedpreferences.getString("entryDataDelay", "") +"\n\n"
                    +sharedpreferences.getString("visitDataDelay", "") +"\n\n"
                    +sharedpreferences.getString("exitData", "");
            txt_summary.setText(""+summary);

    }
}
