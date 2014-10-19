package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);
        PostsAdapter adapter = new PostsAdapter(this);
        listView.setAdapter(adapter);
        new LoadTitlesTask(getApplicationContext(), adapter).execute();
    }

    public void showPost(String url) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("post_url", url);
        startActivity(intent);
    }

}
