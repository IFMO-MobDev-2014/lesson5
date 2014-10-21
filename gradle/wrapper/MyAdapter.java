package ru.md.ifmo.lesson5;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<ItemMaster> {
    Context context;
    ArrayList<ItemMaster> items;
    boolean browser;

    public MyAdapter(Context context, ArrayList<ItemMaster> items, boolean browser) {
        super(context, R.layout.list_element, items);

        this.context = context;
        this.items = items;
        this.browser = browser;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View elementView = inflater.inflate(R.layout.list_element, parent, false);
        TextView title = (TextView) elementView.findViewById(R.id.title);
        TextView date = (TextView) elementView.findViewById(R.id.date);
        TextView description = (TextView) elementView.findViewById(R.id.description);
        Button button = (Button) elementView.findViewById((R.id.more));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView link = (WebView) elementView.findViewById(R.id.link);
                if (!browser) {
                    link.setWebViewClient(new AppWebViewClient());
                } else link.getSettings().setJavaScriptEnabled(true);
                if (items.get(position).getLink()!=null) {
                    link.loadUrl(items.get(position).getLink());
                } else {
                    Toast toast = Toast.makeText(getContext(), "Can't find link", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        title.setText(items.get(position).getTitle());
        date.setText(items.get(position).getPubDate());
        description.setText(items.get(position).getDescription());
        return elementView;
    }

    private class AppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }


}
