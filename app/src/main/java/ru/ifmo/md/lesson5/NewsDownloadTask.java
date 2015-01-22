package ru.ifmo.md.lesson5;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsDownloadTask extends AsyncTask<String, Void, List<Item> > {
    private ProgressDialog progressDialog;
    private Context context;
    private ListView listView;

    protected NewsDownloadTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    static String normalize(String s) {
        String res = s.replace("<br>", "\n");
        res = res.replace("&quot;", "\"");
        Log.i("!", res);
        return res;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.DOWNLOADING_NEWS));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected List<Item> doInBackground(String... params) {
        ArrayList<Item> news = new ArrayList<Item>();
        try {
            XmlPullParser xpp = XmlPullParserFactory.newInstance().newPullParser();
            InputStream inputStream = new URL(params[0]).openStream();
            xpp.setInput(inputStream, null);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                    String title = "";
                    String description = "";
                    String date = "";
                    String url = "";
                    xpp.next();
                    while (!(xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals("item"))) {
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("link")) {
                            xpp.next();
                            url = xpp.getText();
                            xpp.next();
                        }
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("title")) {
                            xpp.next();
                            title = xpp.getText();
                            xpp.next();
                        }
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("pubDate")) {
                            xpp.next();
                            date = xpp.getText();
                            xpp.next();
                        }
                        if (xpp.getEventType() == XmlPullParser.START_TAG && xpp.getName().equals("description")) {
                            xpp.next();
                            description = xpp.getText();
                        }
                        xpp.next();
                    }
                    news.add(new Item(title, normalize(description), date, url));
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return news;
        } catch (IOException e) {
            e.printStackTrace();
            return news;
        }
        return news;
    }

    @Override
    protected void onPostExecute(List<Item> news) {
        NewsListAdapter adapter = new NewsListAdapter(news);
        listView.setAdapter(adapter);
        if (news.isEmpty()) {
            news.add(new Item(context.getString(R.string.ERROR), "", "", ""));
        }
        progressDialog.dismiss();
    }
}
