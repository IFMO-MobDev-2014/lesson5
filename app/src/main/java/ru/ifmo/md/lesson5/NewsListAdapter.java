package ru.ifmo.md.lesson5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsListAdapter extends BaseAdapter {
    List<NewsItem> news;

    public NewsListAdapter(List<NewsItem> news) {
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.title)).setText(news.get(i).getTitle());
        ((TextView) view.findViewById(R.id.date)).setText(news.get(i).getDate());
        ((TextView) view.findViewById(R.id.description)).setText(news.get(i).getDescription());
        return view;
    }
}
