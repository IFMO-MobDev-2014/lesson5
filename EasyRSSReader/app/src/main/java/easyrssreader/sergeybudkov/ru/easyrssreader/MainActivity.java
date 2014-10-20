package easyrssreader.sergeybudkov.ru.easyrssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    EditText newRSS;
    Button addRSS;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        addRSS = (Button) findViewById(R.id.open);
        newRSS = (EditText) findViewById(R.id.inputStr);
        setRssList("http://bash.im/rss/");

        addRSS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String url = newRSS.getText().toString();
                setRssList(url);

            }
        });
    }

    ArrayAdapter<Entry> adapter;
    ArrayList<Entry> links = new ArrayList<Entry>();

    public void setRssList(String link) {
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<Entry>(this, R.layout.item, links);
        listView.setAdapter(adapter);

        new Load() {
            @Override
            protected void onPostExecute(FeedBack feed) {
                try {
                    if (feed.getException() != null) {
                        Toast.makeText(getApplicationContext(),
                                "Sorry, invalid url", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    links.clear();
                    for (int i = 0; i < feed.getArray().size(); i++)
                        links.add(feed.getArray().get(i));
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(link);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Entry entry = links.get(i);
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SingleArticle.class);
                intent.putExtra("URL", entry.getLink());
                if (entry.getDescription() != null) {
                    intent.putExtra("Content", entry.getTitle() + "<br>" + entry.getDescription());
                }
                startActivity(intent);
            }

        });
    }
}



