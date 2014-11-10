package com.example.alexey.lesson5;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class IServise extends IntentService {


    public IServise(){
        this("IServise");
    }

    public IServise(String name) {
        super("myname");
       }



    public void onCreate() {
        super.onCreate();

        Log.i("Started","IService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("started","onhandle");
        final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
        receiver.send(Constants.STATUS_RUNNING, Bundle.EMPTY);
        final Bundle data = new Bundle();

      


        XMLParser xp=new XMLParser();
        URL url=null;
        try {

            url = new URL(intent.getStringExtra("task"));
            URLConnection urlConnection = null;

                urlConnection = url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());


            xp.parse(in);

            in.close();



            data.putString(Constants.RECEIVER_DATA, "Sample result data");
        } catch (IOException e) {
            data.putString(Constants.RECEIVER_DATA, "Error");
        }
        receiver.send(Constants.STATUS_FINISHED, data);}
    }




