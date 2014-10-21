package freemahn.com.lesson5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Freemahn on 17.10.2014.
 */
public class DownloadNewsTask extends AsyncTask<String, Integer, ArrayList<Entry>> {
    static String rssUrl = "http://stackoverflow.com/feeds/tag/android";
    //static String rssUrl = "http://echo.msk.ru/interview/rss-fulltext.xml";
    Context context;
    ListView lw;

    @Override
    protected ArrayList<Entry> doInBackground(String... urls) {
        try {
            return loadFromXML(rssUrl);
        } catch (IOException e) {
            Log.e("IOEx", e.toString());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            //Log.e("XMLparseerror", e.toString());
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Entry> loadFromXML(String urlString) throws IOException, XmlPullParserException {
        ArrayList<Entry> contentAsString = new ArrayList<Entry>();
        InputStream is = null;


        StackOverflowXmlParser parser = new StackOverflowXmlParser();
        //EchoMoscowParser parser = new EchoMoscowParser();
        is = getInpStream(urlString);
        contentAsString = (ArrayList<Entry>) parser.parse(is);
        if (is != null)
            is.close();

        return contentAsString;

    }

    public InputStream getInpStream(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        int response = conn.getResponseCode();
        Log.d("TAG", "The response is: " + response);
        return conn.getInputStream();

    }

}
