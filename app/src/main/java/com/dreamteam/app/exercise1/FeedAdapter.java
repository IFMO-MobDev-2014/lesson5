package com.dreamteam.app.exercise1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by flyingleafe on 14.10.14.
 */
public class FeedAdapter extends BaseAdapter {

    private Feed data;

    public FeedAdapter(Feed data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getItems().size();
    }

    @Override
    public Object getItem(int i) {
        return data.getItems().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void deleteItem(int i) {
        data.getItems().remove(i);
        notifyDataSetChanged();
    }

    public void add(FeedItem o) {
        data.getItems().add(o);
        notifyDataSetChanged();
    }

    public void setData(Feed data) {
        this.data = data;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        }
        TextView title = (TextView) view.findViewById(android.R.id.text1);
        title.setText(data.getItems().get(i).getTitle());
        final TextView desc = (TextView) view.findViewById(android.R.id.text2);
        desc.setText(data.getItems().get(i).getDescription());
        title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int vis = desc.getVisibility();
                if(vis == View.VISIBLE) {
                    desc.setVisibility(View.GONE);
                } else {
                    desc.setVisibility(View.VISIBLE);
                }
            }
        });
        Button button = (Button) view.findViewById(android.R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) viewGroup.getContext()).startWebPreview(data.getItems().get(i).getLink());
            }
        });
        return view;
    }
}
