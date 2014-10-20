package com.example.alexey.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MyActivity15 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ListView lv = (ListView) findViewById(R.id.listView);

        int size = MyActivity.a.size();
        final String[] catnames = new String[size - 2];
        for (int i = 2; i < size; i++) {
            catnames[i - 2] = MyActivity.a.get(i);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, catnames);

        lv.setAdapter(adapter);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(MyActivity15.this, MyActivity2.class);
                intent.putExtra("count", position+2);
                startActivity(intent);
                onDestroy();
            }
        });
    }


}
