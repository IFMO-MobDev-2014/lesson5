package com.wibk.rss;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        Button OKbutton = (Button) findViewById(R.id.OKButton);
        final ListView listView = (ListView) findViewById(android.R.id.list);
        List<String> strings = new ArrayList<String>();
        final MyMegaCoolSuperPuperAdapter2000<String> myMegaCoolSuperPuperAdapter2000 = new MyMegaCoolSuperPuperAdapter2000<String>(strings);
        listView.setAdapter(myMegaCoolSuperPuperAdapter2000);
        OKbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
