package ru.ifmo.ctd.fotjev.rss_reader;

/**
 * Created by fotyev on 19-Oct-14.
 */

import android.os.AsyncTask;
import android.text.Spanned;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedDLParseTask extends AsyncTask<String, Void, ArrayList<HashMap<String, Spanned>>> {
    private final Callback<ArrayList<HashMap<String, Spanned>>> callback;
    private Exception cause;

    public FeedDLParseTask(Callback<ArrayList<HashMap<String, Spanned>>> callback) {
        this.callback = callback;
    }

    @Override
    protected ArrayList<HashMap<String, Spanned>> doInBackground(String... params) {
        ArrayList<HashMap<String, Spanned>> result = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            RSSAtomParser parser = new RSSAtomParser(httpEntity.getContent());
            result = parser.parse();
        } catch (Exception e) {
            Log.d("FeedDLParseTask", e.getLocalizedMessage());
            cause = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, Spanned>> result) {
        if (cause == null) {
            callback.onSuccess(result);
            Log.d("FeedDLParseTask", "feed downloaded and parsed");
        } else {
            callback.onError(cause);
        }

    }
}

