package ru.ifmo.md.lesson5;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Svet on 19.10.2014.
 */
public class LoadInfoTask extends AsyncTask<String, Void, String> {
    ProgressDialog progress;

    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpCliend = new DefaultHttpClient();
        HttpResponse response;
        String answer = null;
        try {
            response = httpCliend.execute(new HttpGet(strings[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                answer = out.toString();
            } else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch(ClientProtocolException ex) {
            Log.e("ERROR", "ClientProtocolException in LoadInfoTask");
        } catch(IOException e) {
            Log.e("ERROR", "IOException in LoadInfoTask");
        }
        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
