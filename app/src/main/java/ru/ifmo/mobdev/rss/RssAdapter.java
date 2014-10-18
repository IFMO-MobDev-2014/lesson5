package ru.ifmo.mobdev.rss;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import ru.ifmo.mobdev.rss.utils.RssArticle;

/**
 * @author sugakandrey
*/
public class RssAdapter extends BaseAdapter {
    private ArrayList<RssArticle> rssItems = new ArrayList<RssArticle>();
    private HashMap<String, ArrayList<RssArticle>> feeds = new HashMap<String, ArrayList<RssArticle>>();
    private LoaderActivity ctx;

    public RssAdapter(LoaderActivity ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return rssItems.size();
    }

    @Override
    public Object getItem(int i) {
        return rssItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        final int index = i;
        RssArticle article = rssItems.get(i);
        View view = article.inflate(convertView, inflater);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.onArticleClick(rssItems.get(index).getUrl());
            }
        });
        return view;
    }

    public ArrayList<RssArticle> get(String key) {
        return feeds.get(key);
    }

    public void remove(String first) {
        rssItems.removeAll(feeds.get(first));
    }

    public void put(Pair<String, ArrayList<RssArticle>> p) {
        feeds.put(p.first, p.second);
        rssItems.addAll(0, p.second);
        notifyDataSetChanged();
    }
}
