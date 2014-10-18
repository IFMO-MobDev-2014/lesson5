package ru.ifmo.md.lesson5.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MainActivity extends Activity {
    private Button mButtonAdd;
    private EditText mEditTextAdd;
    private ListView mListViewRss;

    private RssManager mRssManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        mRssManager = RssManager.get(this);
    }

    private void setupViews() {
        mButtonAdd = (Button) findViewById(R.id.button_addUrl);
        mEditTextAdd = (EditText) findViewById(R.id.editText_addUrl);
        mListViewRss = (ListView) findViewById(R.id.listView_rss);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String potentialUrl = mEditTextAdd.getText().toString();
                if (Patterns.WEB_URL.matcher(potentialUrl).matches()) {
                    addRss(potentialUrl);
                } else {

                }
            }
        });
    }

    private void addRss(String url) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
