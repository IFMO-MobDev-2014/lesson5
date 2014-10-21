package retloko.org.rssreader;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by anton on 19/10/14.
 */
public class RssLoader {
    private MainActivity activity;

    public RssLoader(MainActivity activity) {
        this.activity = activity;
    }

    public void loadUrl(String url) {
        new LoadRssTask().execute(url);
    }

    public static class Entry {
        public final String title;
        public final String link;
        public final String summary;

        private boolean showSummary = false;

        private Entry(String title, String summary, String link) {
            this.title = title;
            this.summary = summary;
            this.link = link;
        }

        protected void toggleSummary() {
            showSummary = !showSummary;
        }

        protected boolean isShowSummary() {
            return showSummary;
        }
    }

    private class LoadRssTask extends AsyncTask<String, Void, EntryAdapter> {
        @Override
        protected EntryAdapter doInBackground(String... urls) {
            ArrayList<Entry> entries = new ArrayList<Entry>();

            try {
                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                final Document xmlResponse = docBuilder.parse(downloadUrl(urls[0]));

                xmlResponse.getDocumentElement().normalize();

                // check if we loaded atom or rss feed
                boolean isRss = xmlResponse.getElementsByTagName("rss").getLength() != 0;

                NodeList nodeList = isRss ?
                        xmlResponse.getElementsByTagName("item") :
                        xmlResponse.getElementsByTagName("entry");

                // get feed's title
                final String feedTitle;
                if (isRss) {
                    Element channelNode = (Element) xmlResponse.getElementsByTagName("channel").item(0);
                    feedTitle = channelNode.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
                } else {
                    Element feedNode = (Element) xmlResponse.getElementsByTagName("feed").item(0);
                    feedTitle = feedNode.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.setTitle(feedTitle.trim());
                    }
                });

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        final String title = element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
                        final String summary;
                        final String link;
                        if (isRss) {
                            summary = element.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
                            link = element.getElementsByTagName("link").item(0).getFirstChild().getNodeValue();
                        } else {
                            summary = element.getElementsByTagName("summary").item(0).getFirstChild().getNodeValue();
                            link = ((Element) element.getElementsByTagName("link").item(0)).getAttribute("href");
                        }

                        entries.add(new Entry(title, summary, link));
                    }

                }
            } catch (IOException e) {
                activity.onNetworkError(e);
            } catch (ParserConfigurationException e) {
                activity.onFeedError(e);
            } catch (SAXException e) {
                activity.onFeedError(e);
            }

            return new EntryAdapter(activity, entries);
        }

        @Override
        protected void onPostExecute(EntryAdapter adapter) {
            activity.onRefreshDone(adapter);
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }
}
