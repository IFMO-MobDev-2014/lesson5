package homework5.rssreader;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Anstanasia on 21.10.2014.
 */
public class Update extends AsyncTask<String, Void, List<TNews>> {
    Context context;
    MyAdapter adapter;
    ListView rssList;

    Update(Context c, ListView l) {
        context = c;
        rssList = l;
    }

    @Override
    protected List<TNews> doInBackground(String... urls) {
       // Log.d("Updater: ", Thread.currentThread().getName());

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream stream = connection.getInputStream();

            Log.d("Updater", "OK 1");

            Reader reader = new InputStreamReader(stream, "windows-1251");
            InputSource is = new InputSource(reader);
            is.setEncoding("windows-1251");

            Log.d("Updater", "OK 2");

            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            MyParser myParser = new MyParser();
            saxParser.parse(is, myParser);

            Log.d("Updater", "OK 3");

            return myParser.getNews();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<TNews> res) {
        if (res == null) {
            Log.d("Update", "null pointer!!!!!!!!!!!!!!");
        } else {
            Log.d("Update", res.get(0).getDescription());
        }

        adapter = new MyAdapter();
        adapter.setData((java.util.ArrayList<TNews>) res);
        adapter.notifyDataSetChanged();
        rssList.setAdapter(adapter);
    }
}
