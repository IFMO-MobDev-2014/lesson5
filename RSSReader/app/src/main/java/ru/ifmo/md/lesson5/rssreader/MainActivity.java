package ru.ifmo.md.lesson5.rssreader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
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
        mEditTextAdd = (EditText) findViewById(R.id.editText_addUrl);
        mListViewRss = (ListView) findViewById(R.id.listView_rss);

        mEditTextAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String potentialUrl = mEditTextAdd.getText().toString();
                if (Patterns.WEB_URL.matcher(potentialUrl).matches()) {
                    mEditTextAdd.setBackgroundColor(Color.argb(0, 128, 128, 128));
                } else {
                    mEditTextAdd.setBackgroundColor(Color.argb(255, 128, 128, 128));
                }
            }
        });

        mEditTextAdd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                Log.d("TAG", "actionId = " + actionId);
                if (actionId == getResources().getInteger(R.integer.actionAdd)) {
                    String potentialUrl = mEditTextAdd.getText().toString();
                    if (Patterns.WEB_URL.matcher(potentialUrl).matches()) {
                        addRss(potentialUrl);
                    } else {
                        //Toast.makeText(getApplicationContext(), "Invalid URL", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "Bad url: " + potentialUrl);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void addRss(String url) {
        Log.d("TAG", "Add url: " + url);
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
