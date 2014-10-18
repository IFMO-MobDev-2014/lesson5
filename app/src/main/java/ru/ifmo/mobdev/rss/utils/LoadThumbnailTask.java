package ru.ifmo.mobdev.rss.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author sugakandrey
 */
class LoadThumbnailTask extends AsyncTask<String, Void, Bitmap> {
    private final RssArticle callback;

    LoadThumbnailTask(RssArticle callback) {
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap image = null;
        URL url;
        try {
            url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            image = BitmapFactory.decodeStream(input);
        } catch (MalformedURLException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, 250, 140, false);
        callback.onThumbnailLoaded(thumbnail);
    }
}