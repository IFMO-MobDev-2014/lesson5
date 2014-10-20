package com.dreamteam.app.exercise1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by flyingleafe on 21.10.14.
 */
public class FetchFeedTask extends AsyncTask<String, Void, Feed> {
    private MainActivity activity;
    private Handler handler;

    public FetchFeedTask(MainActivity act) {
        activity = act;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                activity.showToast(msg.getData().getString("message"));
            }
        };
    }

    @Override
    protected Feed doInBackground(String... strings) {
        String url = strings[0];
        try {
            InputStream response = new URL(url).openStream();
            return new FeedParser().parse(response);
        } catch (IOException e) {
            e.printStackTrace();
            sendHandlerMsg(activity.getString(R.string.fetching_error));
        }
        return null;
    }

    public void sendHandlerMsg(String s) {
        Message msg = handler.obtainMessage();
        Bundle data = new Bundle();
        data.putString("message", s);
        msg.setData(data);
        handler.sendMessage(msg);
    }

    @Override
    protected void onPostExecute(Feed feed) {
        super.onPostExecute(feed);
        activity.setListContents(feed);
    }
}
