package ru.itmo.delf.rssreader;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;


public class RssReader extends ListActivity{

    RssViewAdapter<RssItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_reader);
        adapter = new RssViewAdapter<RssItem>(new Handler());
        adapter.add(new RssItem( "Text","", new Date(),""));
        setListAdapter(adapter);
        if(!hasConnect())
        {
            Toast.makeText(getBaseContext(),"Для зарузки нужен доступ в интернет",Toast.LENGTH_SHORT).show();
            return;
        }
        (new RssDownloader( "http://bash.im/rss/",adapter, new Handler())).execute();

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView title = (TextView) view.findViewById(R.id.titleTextView);
                Intent intent = new Intent(view.getContext(), WebScreen.class);
                intent.putExtra(WebScreen.REQUEST_URL, (String)title.getTag());
                startActivity(intent);
            }
        });

    }

    public void onClickAddButton(View view){
        TextView urlText = (TextView) findViewById(R.id.editText);
        /*Toast.makeText(getBaseContext(),urlText.getText(),Toast.LENGTH_SHORT).show();
        adapter = new RssViewAdapter<RssItem>(new ArrayList<RssItem>());
        adapter.add(new RssItem( String.valueOf( urlText.getText()),"", new Date(),""));
        setListAdapter(adapter);*/
        if(!hasConnect())
        {
            Toast.makeText(getBaseContext(),"Для зарузки нужен доступ в интернет",Toast.LENGTH_SHORT).show();
            return;
        }
        (new RssDownloader(String.valueOf( urlText.getText()),adapter, new Handler())).execute();

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

    public boolean hasConnect() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}


