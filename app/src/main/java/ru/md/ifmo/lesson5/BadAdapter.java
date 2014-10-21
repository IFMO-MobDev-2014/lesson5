package ru.md.ifmo.lesson5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebViewClient;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Илья on 21.10.2014.
 */
public class BadAdapter extends ArrayAdapter<RSSItem>{
    Context context;
    ArrayList<RSSItem> rssItemArrayList;
    public BadAdapter(Context context, ArrayList<RSSItem> rssItemArrayList) {
        super(context,R.layout.list_element,rssItemArrayList);

        this.context = context;
        this.rssItemArrayList = rssItemArrayList;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View elementView = inflater.inflate(R.layout.list_element, parent,false);
        TextView title = (TextView) elementView.findViewById(R.id.title);
        TextView description = (TextView) elementView.findViewById(R.id.description);
        TextView datetime = (TextView) elementView.findViewById(R.id.datetime);
        Button button = (Button) elementView.findViewById(R.id.more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView link = (WebView) elementView.findViewById(R.id.link);
                link.setWebViewClient(new AppWebViewClient());
                if (rssItemArrayList.get(position).link!=null) {
                    link.loadUrl(rssItemArrayList.get(position).link);
                }
            }
        });
        title.setText(rssItemArrayList.get(position).title);
        description.setText(rssItemArrayList.get(position).description);
        datetime.setText(rssItemArrayList.get(position).datetime);
        return elementView;
    }

    private class AppWebViewClient extends  WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }
}
