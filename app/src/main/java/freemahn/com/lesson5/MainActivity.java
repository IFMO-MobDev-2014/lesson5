package freemahn.com.lesson5;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ListActivity {


    ArrayList<Entry> entries = null;
    ListView lw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Button refreshBtn = (Button) findViewById(R.id.refresh_btn);

    }

    public void myOnClick(View v) {
        setContentView(R.layout.activity_main);

        try {
            entries = new DownloadNewsTask().execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayList<String> toAdapter = new ArrayList<String>();
        Log.d("END", "END");
        for (int i = 0; i < entries.size(); i++) {
            toAdapter.add(entries.get(i).title + "\n" + entries.get(i).link);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.layout_entry, R.id.text_view, toAdapter);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Entry item = entries.get(position);
        String url = item.link;
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        intent.putExtra("link", url);
        startActivity(intent);


    }
}
