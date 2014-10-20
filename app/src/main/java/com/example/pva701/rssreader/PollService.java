package com.example.pva701.rssreader;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

/**
 * Created by pva701 on 16.10.14.
 */
public class PollService extends IntentService  {
    private static final String TAG = "PollService";
    public static final String NOTIFICATION = "notification";
    public static final String POLL_INTERVAL = "poll_interval";
    public static final String TARGET_URL = "target_url";
    public static final String TARGET_SOURCE_ID = "target_source_id";

    public PollService() {
        super(TAG);
    }

    private ArrayList <SourcesManager.Source> loadSources() {
        return SourcesManager.getInstance(getApplicationContext()).getSources();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {//Two modes: load one url and background load all sources
        if (!isOnline())
            return;
        /*String targetUrl = intent.getStringExtra(TARGET_URL);
        if (targetUrl != null) {
            int targetSourceId = intent.getIntExtra(TARGET_SOURCE_ID, -1);
            if (targetSourceId == -1)
                throw new RuntimeException("wrong targetSourceId");
            NewsManager.getInstance(getApplicationContext()).mergeNews(RSSFetcher.fetch(targetUrl), targetSourceId);
            return;
        }*/

        //TODO update last_update
        ArrayList <SourcesManager.Source> sources = loadSources();
        String[] urls = new String[sources.size()];
        for (int i = 0; i < urls.length; ++i)
            urls[i] = sources.get(i).getUrl();
        for (int i = 0; i < urls.length; ++i)
            NewsManager.getInstance(getApplicationContext()).mergeNews(RSSFetcher.fetch(urls[i]), sources.get(i).getId());

        boolean isNotify = intent.getExtras().getBoolean(NOTIFICATION);
        int count = NewsManager.getInstance(getApplicationContext()).getCountUnread();
        if (isNotify) {
            PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, null), 0);//TODO change it
            Notification notification = new NotificationCompat.Builder(this).setTicker(count + " new news!").
                    setContentTitle("New " + count + " RSS news!").setContentText("Read news").setContentIntent(pi).setAutoCancel(true).build();
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }
    }

    public static void setServiceAlarm(Context context, boolean isOn, Bundle bundle) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            int pollInterval = bundle.getInt(POLL_INTERVAL);
            boolean isNotification = bundle.getBoolean(NOTIFICATION);
            Intent intent = new Intent(context, PollService.class);
            intent.putExtra(NOTIFICATION, isNotification);
            PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + pollInterval, pollInterval, pi);
        } else {
            Intent intent = new Intent(context, PollService.class);
            PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = new Intent(context, PollService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NewsManager.getInstance(getApplicationContext()).resume();
    }
}
