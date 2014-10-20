package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class MainActivity extends Activity {

    public static String string;
    public EditText editText;
    static public ListView listView;
    static LinkedList<Item> items = new LinkedList<Item>();
    static ArrayAdapter<Item> adapter;
    static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.layout);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<Item>(this, R.layout.item, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("text", items.get(pos).link);
                startActivity(intent);
            }

        });
        loadAndParse();
    }

    public void onConnectPress(View view) {
        loadAndParse();
    }

    public void loadAndParse(){
        Toast toast = Toast.makeText(this, "Please wait while the RSS feed is being downloaded", Toast.LENGTH_LONG);
        toast.show();
        items.clear();
        adapter.notifyDataSetChanged();
        try{
            new RssParseTask().execute(editText.getText().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
