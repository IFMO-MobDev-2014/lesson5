package com.example.pva701.rssreader;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pva701 on 19.10.14.
 */
public class SaveDataService extends IntentService {
    public static final String TAG = "SaveDataService";
    public SaveDataService() {
        super(TAG);
    }
    public static boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRunning)
            return Service.START_NOT_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isRunning = true;
        Log.i("SaveDataService", "onHandleIntent");
        NewsManager.getInstance(getApplicationContext()).resume();
        SourcesManager.getInstance(getApplicationContext()).resume();
        isRunning = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SaveDataService", "onDestroy");
    }
}
