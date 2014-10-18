package com.pinguinson.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinguinson.reader.models.Article;
import com.pinguinson.reader.models.Feed;

/**
 * Created by pinguinson on 18.10.2014.
 */

public class ArticleAdapter extends BaseAdapter {
    Context context;
    Feed feed;
    LayoutInflater inflater;

    public ArticleAdapter(Context context, Feed feed) {
        this.context = context;
        this.feed = feed;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return feed.totalArticles();
    }

    @Override
    public Object getItem(int position) {
        if (0 <= position && position < feed.totalArticles()) {
            return feed.getArticle(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.article, parent, false);
        }
        Article article = feed.getArticle(position);

        TextView title = (TextView) view.findViewById(R.id.article_title);
        TextView author = (TextView) view.findViewById(R.id.article_author);
        title.setText(article.getTitle());
        author.setText("@" + article.getAuthor());

        return view;
    }


}
