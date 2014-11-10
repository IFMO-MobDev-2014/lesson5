package com.example.alexey.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MyActivity15 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ListView lv = (ListView) findViewById(R.id.list);

    final Cursor cursor = MyActivity.sqdb.query(DataBase.TABLE_NAME, null, null, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
                new String[]{DataBase.POSTNAME}, new int[]{android.R.id.text1});
        lv.setAdapter(adapter);

     //   cursor.close();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(MyActivity15.this, MyActivity3.class);

                TextView tv=(TextView) itemClicked;
                String s=tv.getText().toString();

                //cursor = MyActivity.sqdb.query(DataBase.TABLE_NAME, null, null, null, null, null, null);
                boolean t=true;
                cursor.moveToFirst();
                while(cursor.moveToNext()&&t){
                    String y=cursor.getString(cursor.getColumnIndex(DataBase.POSTNAME));
                    if (y!=null&&y.equals(s)) {
                        s = cursor.getString(cursor.getColumnIndex(DataBase.EMAIL));
                        t=false;
                    }
                }
                //String name = cursor1.getString(cursor1.getColumnIndex(DataBase.EMAIL));
                intent.putExtra("count", s);
                startActivity(intent);
                onDestroy();
            }
        });
    }


}
