package mob_dev_lesson2.katunina.ctddev.ifmo.ru.rss_readerhw5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class DescriptionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Intent intent = getIntent();
        setTitle(intent.getStringExtra(FeedActivity.TITLE_EXTRA));
        WebView wv = ((WebView) findViewById(R.id.web_view));
        wv.getSettings().setDefaultTextEncodingName("utf-8");
        wv.loadDataWithBaseURL(null,getIntent().getStringExtra(FeedActivity.DESCRIPTION_EXTRA), "text/html",
                "en_US",null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
