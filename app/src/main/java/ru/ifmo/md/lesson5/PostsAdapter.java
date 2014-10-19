package ru.ifmo.md.lesson5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends BaseAdapter {
    private List<Post> posts;
    private MainActivity mainActivity;

    public PostsAdapter(MainActivity mainActivity) {
        posts = new ArrayList<Post>();
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        }
        final Post post = posts.get(i);
        ((TextView) view).setText(post.title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.showPost(post.url);
            }
        });
        return view;
    }

    public void add(Post post) {
        posts.add(post);
    }
}
