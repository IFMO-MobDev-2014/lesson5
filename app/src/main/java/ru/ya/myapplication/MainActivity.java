package ru.ya.myapplication;

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
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("url", ((MyAdapter) listView.getAdapter()).getItem(i).link.toString());
                startActivity(intent);
            }
        });

    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
    return false;
}

    public void myClick(View view) {
        ListView listView = (ListView)findViewById(R.id.listView);
        UploadXML uploadXML = new UploadXML(listView);

        Log.e("abacaba", "abacaba");
        if (!isOnline()) {
            Log.e("abacaba1", "abacaba");
            Toast toast = Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Log.e("abacaba2", "abacaba");

        uploadXML.execute("http://bash.im/rss");
    }
}
