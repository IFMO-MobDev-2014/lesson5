package homework5.rssreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;



public class Main extends Activity {
    Button button;
    Button rButton;
    EditText text;
    ListView rssList;
//    public static ArrayList<String> links = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("main", "start");

        button = (Button) findViewById(R.id.okButton);
        rButton = (Button) findViewById(R.id.refreshButton);
        text = (EditText) findViewById(R.id.rssUrl);
        rssList = (ListView) findViewById(R.id.rssListView);

        //links.add("http://feeds.bbci.co.uk/news/rss.xml");

        download();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String res = String.valueOf(text.getText());
                Toast.makeText(Main.this, "Nothing is going on", Toast.LENGTH_SHORT).show();
                //links.add(res);
                text.setText("");
                //download();
            }
        });

        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Main.this, "Refreshing", Toast.LENGTH_SHORT).show();
                download();
            }
        });

        final Intent intent = new Intent(Main.this, ShowWeb.class);
        rssList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int pos, long l) {
                MyAdapter a = (MyAdapter) av.getAdapter();
                TNews n = a.getItem(pos);
                intent.putExtra("url", n.getLink());
                startActivity(intent);
            }
        });
    }

    public void download() {
       new Update(this, rssList).execute("http://feeds.bbci.co.uk/news/rss.xml");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
