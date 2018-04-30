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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Push;
import com.gimbal.android.Visit;
import com.poc.gimbal.korcomptenz.gimbalpoc.GimbalEvent.TYPE;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AppService extends Service {
    public static final String APPSERVICE_STARTED_ACTION = "appservice_started";


    private static final int MAX_NUM_EVENTS = 100;
    private LinkedList<GimbalEvent> events;
    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;

    BeaconEventListener beaconEventListener;
    BeaconManager beaconManager;

    @Override
    public void onCreate() {

        Log.e("MS-AppService", "onCreate");
        events = new LinkedList<GimbalEvent>(GimbalDAO.getEvents(getApplicationContext()));
//https://manager.gimbal.com/apps/37337/edit#copy
        Gimbal.setApiKey(this.getApplication(), "d1077125-8296-44a6-9551-588a455abd66");

        beaconManager = new BeaconManager();
        beaconManager.startListening();

   beaconEventListener = new BeaconEventListener() {
       @Override
       public void onBeaconSighting(BeaconSighting beaconSighting) {
           super.onBeaconSighting(beaconSighting);
           Log.e("onBeaconSighting",""+beaconSighting.getBeacon().getName());
           Toast.makeText(getApplicationContext(), "onBeaconSighting = "+beaconSighting.getBeacon().getName(), Toast.LENGTH_LONG).show();
       }
   };


        beaconManager.addListener(beaconEventListener);

        placeEventListener = new PlaceEventListener() {

            @Override
            public void locationDetected(Location location) {
                super.locationDetected(location);
                Log.e("MS-AppService", "placeEventListener locationDetected");

                Log.e("getProvider", ""+location.getProvider());
                Log.e("getAccuracy", ""+location.getAccuracy());
                Log.e("getAltitude", ""+location.getAltitude());
                Log.e("getBearing", ""+location.getBearing());
               // Log.e("getProvider", ""+location.getElapsedRealtimeNanos());
                Log.e("getExtras", ""+location.getExtras());
                Log.e("getLatitude", ""+location.getLatitude());
                Log.e("getLongitude", ""+location.getLongitude());
                Log.e("getSpeed", ""+location.getSpeed());
                Log.e("getTime", ""+location.getTime());

            }

            @Override
            public void onVisitStart(Visit visit) {
                Log.e("MS-AppService", "placeEventListener onVisitStart");
                addEvent(new GimbalEvent(TYPE.PLACE_ENTER, visit.getPlace().getName(), new Date(visit.getArrivalTimeInMillis())));

                Date arrivalDate = new Date(visit.getArrivalTimeInMillis());
                DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                String arrivalDateFormatted = formatter.format(arrivalDate);

                Log.e("*****getVisitID", ""+visit.getVisitID());
                Log.e("*arrivalDateFormatted", ""+arrivalDateFormatted);
                Log.e("*getName", ""+visit.getPlace().getName());
                Log.e("*getIdentifier", ""+visit.getPlace().getIdentifier());
                Log.e("*getAttributes", ""+visit.getPlace().getAttributes());
                Log.e("*getDwellTimeInMillis", ""+visit.getDwellTimeInMillis());

                String getPlace = ""+visit.getPlace();
                getPlace = getPlace.substring(getPlace.indexOf("name=") + 5);
                getPlace = getPlace.substring(0, getPlace.indexOf(","));


                String data ="\nPlace : "+getPlace+
                        "\nArrived at : "+arrivalDateFormatted;
                SharedPreferences sharedpreferences = getSharedPreferences("notifyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("entryData", data);
                editor.commit();
             //   sendNotification("Tester Entry Found", "Tester Entry Found ! \n" +data );
            }

            @Override
            public void onVisitStartWithDelay(Visit visit, int delayTimeInSeconds) {
                Log.e("MS-AppService", "onVisitStartWithDelay delayTimeInSeconds = "+delayTimeInSeconds);

                if (delayTimeInSeconds > 0) {

                    Log.e("MS-AppService", "delayTimeInSeconds = "+delayTimeInSeconds);

                    addEvent(new GimbalEvent(TYPE.PLACE_ENTER_DELAY, visit.getPlace().getName(), new Date(System.currentTimeMillis())));
                    Log.e("_getVisitID", ""+visit.getVisitID());
                    Log.e("_getArrivalTimeInMillis", ""+visit.getArrivalTimeInMillis());
                    Log.e("_getDepartureTimeInMi", ""+visit.getDepartureTimeInMillis());
                    Log.e("_getDwellTimeInMillis", ""+visit.getDwellTimeInMillis());
                    Log.e("_getPlace", ""+visit.getPlace());

                    Date arrivalDate = new Date(visit.getArrivalTimeInMillis());
                    DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                    String arrivalDateFormatted = formatter.format(arrivalDate);

                    Date departureDate = new Date(visit.getDepartureTimeInMillis());
                    DateFormat departureFormatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                    String departureDateFormatted = departureFormatter.format(departureDate);

                    Date dwellTimeDate = new Date(visit.getDwellTimeInMillis());
                    DateFormat dwellTimeFormatter = new SimpleDateFormat("hh:mm:ss");
                    String dwellTimeFormatted = dwellTimeFormatter.format(dwellTimeDate);
                    String getPlace = ""+visit.getPlace();
                    getPlace = getPlace.substring(getPlace.indexOf("name=") + 5);
                    getPlace = getPlace.substring(0, getPlace.indexOf(","));
                    String data =  "\nPlace : "+getPlace
                                    +"\nArrived at : "+arrivalDateFormatted
                       /* +" - Departure at : "+departureDateFormatted
                        +" - Dwell Time : "+dwellTimeFormatted*/
                            +"\n\n\nCongratulation!!! Your entry will be recorded \n";
                    SharedPreferences sharedpreferences = getSharedPreferences("notifyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("visitDataDelay", data);
                    editor.commit();
                    sendNotification("Delayed Entry Found", "Delayed Entry Found ! \n" +data );

                }
            }

            @Override
            public void onVisitEnd(Visit visit) {
                Log.e("MS-AppService", "placeEventListener onVisitEnd");
                addEvent(new GimbalEvent(TYPE.PLACE_EXIT, visit.getPlace().getName(), new Date(visit.getDepartureTimeInMillis())));

                Log.e("getVisitID", ""+visit.getVisitID());
                Log.e("getArrivalTimeInMillis", ""+visit.getArrivalTimeInMillis());
                Log.e("getDepartureTimeInMi", ""+visit.getDepartureTimeInMillis());
                Log.e("getDwellTimeInMillis", ""+visit.getDwellTimeInMillis());
                Log.e("getPlace", ""+visit.getPlace());

                Date arrivalDate = new Date(visit.getArrivalTimeInMillis());
                DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                String arrivalDateFormatted = formatter.format(arrivalDate);

                Date departureDate = new Date(visit.getDepartureTimeInMillis());
                DateFormat departureFormatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                String departureDateFormatted = departureFormatter.format(departureDate);

                Log.e("getDwellTimeInMillis", ""+visit.getDwellTimeInMillis());
                long second = (visit.getDwellTimeInMillis() / 1000) % 60;
                long minute = (visit.getDwellTimeInMillis() / (1000 * 60)) % 60;
                long hour = (visit.getDwellTimeInMillis() / (1000 * 60 * 60)) % 24;
                String time = String.format("%02d:%02d:%02d", hour, minute, second);
                Log.e("time", ""+time);


                String getPlace = ""+visit.getPlace();
                getPlace = getPlace.substring(getPlace.indexOf("name=") + 5);
                getPlace = getPlace.substring(0, getPlace.indexOf(","));
                String data = "\nPlace : "+getPlace
                        +"\nArrived at : "+arrivalDateFormatted
                        +"\nDeparture at : "+departureDateFormatted
                        +"\nDwell Time : "+time
                        ;
                SharedPreferences sharedpreferences = getSharedPreferences("notifyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("exitData", data);
                editor.commit();
               // sendNotification("Tester Exit Found", "Tester Exit Found ! "+data );
            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);

        // Setup CommunicationListener
        communicationListener = new CommunicationListener() {
            @Override
            public Notification.Builder prepareCommunicationForDisplay(Communication communication, Visit visit, int notificationId) {
                addEvent(new GimbalEvent(TYPE.COMMUNICATION_PRESENTED, communication.getTitle() + ":  Content Deliver", new Date()));
                Log.e("MS-AppService", "communicationListener COMMUNICATION_PRESENTED for visit");


                if (communication.getTitle().contains("Congratulations")){
                    SharedPreferences sharedpreferences = getSharedPreferences("notifyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    Date arrivalDate = new Date(visit.getArrivalTimeInMillis());
                    DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                    String arrivalDateFormatted = formatter.format(arrivalDate);
                    Log.e("arrivalDateFormatted", ""+arrivalDateFormatted);

                    Date departureDate = new Date(visit.getDepartureTimeInMillis());
                    DateFormat departureFormatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                    String departureDateFormatted = departureFormatter.format(departureDate);
                    Log.e("departureDateFormatted", ""+departureDateFormatted);

                    Log.e("getDwellTimeInMillis", ""+visit.getDwellTimeInMillis());
                    long second = (visit.getDwellTimeInMillis() / 1000) % 60;
                    long minute = (visit.getDwellTimeInMillis() / (1000 * 60)) % 60;
                    long hour = (visit.getDwellTimeInMillis() / (1000 * 60 * 60)) % 24;
                    String time = String.format("%02d:%02d:%02d", hour, minute, second);
                    Log.e("time", ""+time);

                    Date deliveryDate = new Date(communication.getDeliveryDate());
                    DateFormat deliveryDateFormatter = new SimpleDateFormat("dd-MMM-yyyy  hh:mm:ss a z");
                    String deliveryDateFormatted = deliveryDateFormatter.format(deliveryDate);
                    Log.e("deliveryDateFormatted", ""+deliveryDateFormatted);

                    editor.putString("entryDataDelay", ""+"\n\n\nCongratulation!!! Your entry will be recorded \n"
                            +"\nDwell Time : "+time);
                    editor.commit();
                }

                return null;
            }

            /*@Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> collection, Push push) {
                Log.e("MS-AppService", "presentNotificationForCommunications push");
                return null;
            }

            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> collection, Visit visit) {
                Log.e("MS-AppService", "presentNotificationForCommunications visit");
                for (Communication communication : collection) {
                    if (communication != null) {
                        Log.e("communication", ""+communication.getTitle());
                        if (communication.getTitle().contains("Congratulations")) {
                            Log.e("communication", "Recorded ::: "+communication.getTitle());
                        }else{
                            Log.e("communication", "Others ::: "+communication.getTitle());
                        }
                    }
                }
                return null;
            }*/

            @Override
            public Notification.Builder prepareCommunicationForDisplay(Communication communication, Push push, int notificationId) {
               /* addEvent(new GimbalEvent(TYPE.COMMUNICATION_INSTANT_PUSH, communication.getTitle() + ":  Instant Content Deliver", new Date()));*/
                Log.e("MS-AppService", "communicationListener COMMUNICATION_INSTANT_PUSH for push");
                return null;
            }

            @Override
            public void onNotificationClicked(List<Communication> communications) {
                for (Communication communication : communications) {
                    if(communication != null) {
                       /* addEvent(new GimbalEvent(TYPE.NOTIFICATION_CLICKED, communication.getTitle() + ": Content is clicked", new Date()));*/

                        Log.e("MS-AppService", "communicationListener NOTIFICATION_CLICKED");
                        /*Log.e("MS-AppService", "getTitle"+communication.getTitle());
                        Log.e("MS-AppService", "getDescription"+communication.getDescription());
                        Log.e("MS-AppService", "getIdentifier"+communication.getIdentifier());
                        Log.e("MS-AppService", "getURL"+communication.getURL());
                        Log.e("MS-AppService", "getAttributes"+communication.getAttributes());
                        Log.e("MS-AppService", "getDeliveryDate"+communication.getDeliveryDate());
                        Log.e("MS-AppService", "getExpiryTimeInMillis"+communication.getExpiryTimeInMillis());*/

                        if (communication.getTitle().contains("Congratulations")){
                           /* SharedPreferences sharedpreferences = getSharedPreferences("notifyPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();


                            Date deliveryDate = new Date(communication.getDeliveryDate());
                            DateFormat deliveryDateFormatter = new SimpleDateFormat("hh:mm:ss");
                            String deliveryDateFormatted = deliveryDateFormatter.format(deliveryDate);
                            Log.e("*deliveryDateFormatted", ""+deliveryDateFormatted);

                            Date expiryTimeInMillis = new Date(communication.getExpiryTimeInMillis());
                            DateFormat expiryTimeInMillisFormatter = new SimpleDateFormat("hh:mm:ss");
                            String expiryTimeInMillisFormatted = expiryTimeInMillisFormatter.format(expiryTimeInMillis);
                            Log.e("*expiryTimeInMillisForm", ""+expiryTimeInMillisFormatted);

                            editor.putString("entryDataDelay", ""+"\n\n\nCongratulation!!! Your entry will be recorded now.\n"+deliveryDateFormatted);
                            editor.commit();*/
                        }
                        //Intent intent  = new Intent(getApplicationContext(),AppActivity.class);
                        Intent intent  = new Intent(getApplicationContext(),NotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        };
        CommunicationManager.getInstance().addListener(communicationListener);

    }


    public void onBeaconSighting(BeaconSighting sighting) {
        // This will be invoked upon beacon sighting
        Log.e("onBeaconSighting", "getName"+sighting.getBeacon().getName());
    }

    public void sendNotification(String Title, String messageBody) {

        SharedPreferences sharedpreferences = getSharedPreferences("notifyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("messageBody", messageBody);
        editor.commit();
        String TAG = "AppServices";
        Log.e("MS-"+TAG, "sendNotification Body = "+messageBody);
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notify)
                .setContentTitle(Title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }
    private void addEvent(GimbalEvent event) {
        Log.e("MS-AppService", "addEvent");
        while (events.size() >= MAX_NUM_EVENTS) {
            events.removeLast();
        }
        events.add(0, event);
        GimbalDAO.setEvents(getApplicationContext(), events);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e("MS-AppService", "onStartCommand");
        notifyServiceStarted();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("MS-AppService", "onDestroy");
        PlaceManager.getInstance().removeListener(placeEventListener);
        CommunicationManager.getInstance().removeListener(communicationListener);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyServiceStarted() {
        Log.e("MS-AppService", "notifyServiceStarted");
        Intent intent = new Intent(APPSERVICE_STARTED_ACTION);
        sendBroadcast(intent);
    }

}
