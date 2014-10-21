package ru.md.ifmo.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;



public class MyActivity extends Activity  {

    ImageButton doit;
    ListView listView;
    RSSEvaluation loader;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        listView =(ListView) findViewById(R.id.listView2);
        doit =(ImageButton) findViewById(R.id.imageButton);
        doit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseXML();
            }
        });
    }

    private void parseXML() {
        try {
            loader = new RSSEvaluation();
            loader.execute("http://feeds.bbci.co.uk/news/rss.xml");
            if (loader.exception != null) {
                throw new Exception();
            }
            ArrayList<RSSItem> itemsList = loader.get();
            BadAdapter adapter = new BadAdapter(this, itemsList);
            listView.setAdapter(adapter);
        } catch (Exception e) {
        }
    }
}
