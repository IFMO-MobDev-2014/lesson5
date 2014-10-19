package ru.ifmo.md.lesson5;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LoadTitlesTask extends AsyncTask<Void, Post, Exception> {
    private PostsAdapter adapter;
    private Context context;
    private URL url;

    public LoadTitlesTask(Context context, PostsAdapter adapter) {
        this.adapter = adapter;
        this.context = context;
        try {
            url = new URL("http://stackoverflow.com/feeds/tag/android");
        } catch (MalformedURLException e) {
            Log.e("", "", e);
        }
    }

    protected Exception doInBackground(Void... voids) {
        URLConnection conn;
        Document doc;
        try {
            conn = url.openConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(conn.getInputStream());
        } catch (IOException e) {
            return e;
        } catch (ParserConfigurationException e) {
            return e;
        } catch (SAXException e) {
            return e;
        }

        NodeList nodes = doc.getElementsByTagName("entry");
        for (int i = 0; i < nodes.getLength(); i++) {
            Post post = new Post();
            Node entry = nodes.item(i);
            NodeList children = entry.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node child = children.item(j);
                if (child.getNodeName().equalsIgnoreCase("id")) {
                    post.url = child.getTextContent();
                }
                if (child.getNodeName().equalsIgnoreCase("title")) {
                    post.title = child.getTextContent();
                }
            }
            publishProgress(post);
        }
        return null;
    }

    protected void onProgressUpdate(Post... posts) {
        adapter.add(posts[0]);
        adapter.notifyDataSetChanged();
    }

    protected void onPostExecute(Exception e) {
        if (e != null) {
            Log.e("", "", e);
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
