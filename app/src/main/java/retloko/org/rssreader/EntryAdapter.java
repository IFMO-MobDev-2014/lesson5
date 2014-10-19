package retloko.org.rssreader;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

/**
 * Created by anton on 14/10/14.
 */
public class EntryAdapter extends BaseAdapter {
    public static final String ARTICLE_TITLE = "title";
    public static final String ARTICLE_URL = "url";

    private class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void showArticle(int i) {
            RssLoader.Entry entry = (RssLoader.Entry) getItem(i);
            Intent articleIntent = new Intent(activity, ArticleActivity.class);

            articleIntent.putExtra(ARTICLE_TITLE, entry.title);
            articleIntent.putExtra(ARTICLE_URL, entry.link);

            activity.startActivity(articleIntent);
        }
    }

    private final String javascriptNamespace;
    private final MainActivity activity;
    private final List<RssLoader.Entry> data;
    private final JavascriptInterface javascriptInterface = new JavascriptInterface();

    public EntryAdapter(MainActivity activity, List<RssLoader.Entry> data) {
        this.activity = activity;
        this.data = data;

        Random random = new Random();
        this.javascriptNamespace = "A" + Math.abs(random.nextInt());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(RssLoader.Entry what) {
        data.add(what);
        notifyDataSetChanged();
    }

    public void removeItem(int id) {
        data.remove(id);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        }
        TextView titleView = (TextView) convertView.findViewById(android.R.id.text1);
        final WebView summaryView = (WebView) convertView.findViewById(android.R.id.text2);

        final RssLoader.Entry entry = (RssLoader.Entry) getItem(i);

        titleView.setText(entry.title);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entry.toggleSummary();
                summaryView.setVisibility(entry.isShowSummary() ? View.VISIBLE : View.GONE);
            }
        });

        String showArticleHtml = String.format(activity.getString(R.string.show_article_html), javascriptNamespace, i);

        summaryView.setVisibility(entry.isShowSummary() ? View.VISIBLE : View.GONE);
        summaryView.loadData(entry.summary + showArticleHtml, "text/html", null);

        WebSettings webSettings = summaryView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        summaryView.addJavascriptInterface(javascriptInterface, javascriptNamespace);

        return convertView;
    }
}
