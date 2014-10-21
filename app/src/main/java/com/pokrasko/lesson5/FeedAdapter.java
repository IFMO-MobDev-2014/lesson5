package com.pokrasko.lesson5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by pokrasko on 21.10.14.
 */
public class FeedAdapter extends BaseAdapter {
    private Feed feed;

    public FeedAdapter(Feed feed) {
        this.feed = feed;
    }

    public void add(FeedItem item) {
        feed.addItem(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return feed.getSize();
    }

    @Override
    public Object getItem(int i) {
        return feed.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        final TextView description = (TextView) view.findViewById(R.id.description);
        title.setText(feed.getItem(i).getTitle());
        description.setText(feed.getItem(i).getDescription());

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feed.getItem(i).changeVisibility();
                if (feed.getItem(i).isVisible()) {
                    description.setVisibility(View.VISIBLE);
                } else {
                    description.setVisibility(View.GONE);
                }
            }
        });

        Button button = (Button) view.findViewById(R.id.fullButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) viewGroup.getContext()).showMore(
                        feed.getItem(i).getTitle()
                      , feed.getItem(i).getLink());
            }
        });
        return view;
    }
}
