package ifmo.ru.lesson5;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by mariashka on 10/19/14.
 */
public class RSSDownloader extends AsyncTask <List<String>, String, List<RSSItem> > {
    ProgressDialog dialog;
    Context context;

    RSSDownloader(Context c) {
        context = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(context);
        dialog.setMessage("Downloading content. Please, wait");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
    }
    @Override
    protected List<RSSItem> doInBackground(List<String>... params) {
        List <RSSItem> list = new ArrayList<RSSItem>();
        try {
            for (int i = 0; i < params[0].size(); i++) {

                URL url = new URL(params[0].get(i));

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(in);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("item");
                Log.d("nodeList", nodeList.getLength() +"");
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node curr = nodeList.item(j);
                    Element e = (Element) curr;

                    String title = e.getElementsByTagName("title").item(0).getTextContent();
                    String text = e.getElementsByTagName("description").item(0).getTextContent();
                    String link = e.getElementsByTagName("link").item(0).getTextContent();
                    list.add(new RSSItem(title, text, link));
                    Log.d("item", title +" " + text+" "+link);
                }
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<RSSItem> s) {
        super.onPostExecute(s);
        dialog.dismiss();
    }
}
