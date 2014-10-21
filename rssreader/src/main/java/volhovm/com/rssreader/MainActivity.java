package volhovm.com.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private PostAdapter adapter;
    private URL rssUrl = null;
    private TextView textView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.editText);
        postRSS("http://bash.im/rss/");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Upon button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true; }

        return super.onOptionsItemSelected(item);
    }

    public void onRssRequest(View view) {
        String rss = textView.getText().toString();
        postRSS(rss);
    }

    void postRSS(String rss) {
        try {
            rssUrl = new URL(rss);
            new LoadRSSTask(this).execute(rssUrl);
        } catch (MalformedURLException e) {
            Toast t = new Toast(this);
            t.setText("Wrong url");
            t.setDuration(Toast.LENGTH_LONG);
            t.show();
        }
    }

    void onRSSUpdate(ArrayList<LoadRSSTask.Item> list) {
        String[] listA = new String[list.size()];
        for (int i = 0; i < list.size(); i++) listA[i] = list.get(i).title;
        adapter = new PostAdapter(this, list, listA);
        listView.setAdapter(adapter);
    }

    void onPostClick(View view) {
        // TODO
    }
}
