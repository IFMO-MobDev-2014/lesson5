package ru.itmo.delf.rssreader;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RssReader extends ListActivity{

    RssViewAdapter<RssItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_reader);
        adapter = new RssViewAdapter<RssItem>(new ArrayList<RssItem>());
        adapter.add(new RssItem( "Text","", new Date(),""));
        setListAdapter(adapter);
    }

    public void onClickAddButton(View view){
        TextView urlText = (TextView) findViewById(R.id.editText);
        Toast.makeText(getBaseContext(),urlText.getText(),Toast.LENGTH_SHORT).show();
        adapter = new RssViewAdapter<RssItem>(new ArrayList<RssItem>());
        adapter.add(new RssItem( String.valueOf( urlText.getText()),"", new Date(),""));
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rss_reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


